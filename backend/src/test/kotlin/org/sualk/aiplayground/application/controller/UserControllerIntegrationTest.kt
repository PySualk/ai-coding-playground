package org.sualk.aiplayground.application.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.sualk.aiplayground.application.dto.request.CreateUserRequest
import org.sualk.aiplayground.application.dto.request.UpdateUserRequest
import org.sualk.aiplayground.domain.entity.User
import org.sualk.aiplayground.domain.repository.UserRepository
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@Transactional
class UserControllerIntegrationTest {

    companion object {
        @Container
        val postgres = PostgreSQLContainer<Nothing>("postgres:18").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        userRepository.deleteAll()
    }

    @Test
    fun `POST users should create user and return 201`() {
        val request = CreateUserRequest(
            email = "new@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("new@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())

        assertTrue(userRepository.existsByEmail("new@example.com"))
    }

    @Test
    fun `POST users should return 400 when validation fails`() {
        val request = mapOf(
            "email" to "invalid-email",
            "firstName" to "",
            "lastName" to "Doe"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors").exists())
    }

    @Test
    fun `POST users should return 409 when email already exists`() {
        userRepository.save(User(
            email = "existing@example.com",
            firstName = "Jane",
            lastName = "Smith"
        ))

        val request = CreateUserRequest(
            email = "existing@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Conflict"))
    }

    @Test
    fun `GET users by id should return 200 when user exists`() {
        val savedUser = userRepository.save(User(
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe"
        ))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/${savedUser.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedUser.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
    }

    @Test
    fun `GET users by id should return 404 when user not found`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"))
    }

    @Test
    fun `GET users should return paginated users with 200`() {
        userRepository.save(User(email = "user1@example.com", firstName = "User", lastName = "One"))
        userRepository.save(User(email = "user2@example.com", firstName = "User", lastName = "Two"))
        userRepository.save(User(email = "user3@example.com", firstName = "User", lastName = "Three"))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?page=0&size=2"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(2))
    }

    @Test
    fun `PUT users by id should update user and return 200`() {
        val savedUser = userRepository.save(User(
            email = "old@example.com",
            firstName = "Old",
            lastName = "Name"
        ))

        val updateRequest = UpdateUserRequest(
            firstName = "New",
            lastName = "Updated"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/${savedUser.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("New"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Updated"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("old@example.com"))

        val updatedUser = userRepository.findById(savedUser.id!!).get()
        assertEquals("New", updatedUser.firstName)
        assertEquals("Updated", updatedUser.lastName)
    }

    @Test
    fun `PUT users by id should return 404 when user not found`() {
        val updateRequest = UpdateUserRequest(firstName = "New")

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
    }

    @Test
    fun `PUT users by id should return 409 when email already exists`() {
        userRepository.save(User(email = "existing@example.com", firstName = "Jane", lastName = "Smith"))
        val user = userRepository.save(User(email = "user@example.com", firstName = "John", lastName = "Doe"))

        val updateRequest = UpdateUserRequest(email = "existing@example.com")

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/${user.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
    }

    @Test
    fun `DELETE users by id should soft delete and return 204`() {
        val savedUser = userRepository.save(User(
            email = "todelete@example.com",
            firstName = "Delete",
            lastName = "Me"
        ))

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/${savedUser.id}"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        val deletedUser = userRepository.findById(savedUser.id!!).get()
        assertFalse(deletedUser.active)
    }

    @Test
    fun `DELETE users by id should return 404 when user not found`() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
    }

    @Test
    fun `GET users by email should return 200 when user exists`() {
        userRepository.save(User(
            email = "find@example.com",
            firstName = "Find",
            lastName = "Me"
        ))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/by-email?email=find@example.com"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("find@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Find"))
    }

    @Test
    fun `GET users by email should return 404 when user not found`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/by-email?email=notfound@example.com"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
    }

    @Test
    fun `GET users by email should return 400 when email is invalid`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/by-email?email=invalid-email"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
