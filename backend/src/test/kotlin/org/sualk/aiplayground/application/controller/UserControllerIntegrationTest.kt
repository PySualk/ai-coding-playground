package org.sualk.aiplayground.application.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
    private lateinit var userRepository: UserRepository

    private lateinit var mockMvc: MockMvc
    private val objectMapper = jacksonObjectMapper()

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
    fun `DELETE users by id should delete and return 204`() {
        val savedUser = userRepository.save(User(
            email = "todelete@example.com",
            firstName = "Delete",
            lastName = "Me"
        ))

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/${savedUser.id}"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        assertFalse(userRepository.findById(savedUser.id!!).isPresent)
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

    @Test
    fun `GET users should filter by active status true`() {
        userRepository.save(User(email = "active1@example.com", firstName = "Active", lastName = "One", active = true))
        userRepository.save(User(email = "active2@example.com", firstName = "Active", lastName = "Two", active = true))
        userRepository.save(User(email = "inactive@example.com", firstName = "Inactive", lastName = "User", active = false))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?active=true"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].active").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].active").value(true))
    }

    @Test
    fun `GET users should filter by active status false`() {
        userRepository.save(User(email = "active@example.com", firstName = "Active", lastName = "User", active = true))
        userRepository.save(User(email = "inactive1@example.com", firstName = "Inactive", lastName = "One", active = false))
        userRepository.save(User(email = "inactive2@example.com", firstName = "Inactive", lastName = "Two", active = false))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?active=false"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].active").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].active").value(false))
    }

    @Test
    fun `GET users should return all users when active filter not specified`() {
        userRepository.save(User(email = "active@example.com", firstName = "Active", lastName = "User", active = true))
        userRepository.save(User(email = "inactive@example.com", firstName = "Inactive", lastName = "User", active = false))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
    }

    @Test
    fun `GET users should escape LIKE wildcards and not match all users`() {
        // Create users with specific patterns
        userRepository.save(User(email = "alice@example.com", firstName = "Alice", lastName = "Smith"))
        userRepository.save(User(email = "bob@example.com", firstName = "Bob", lastName = "Jones"))
        userRepository.save(User(email = "charlie@example.com", firstName = "Charlie", lastName = "Wilson"))

        // Search with % wildcard - should NOT match all users (would match all if not escaped)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users").param("search", "%"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))

        // Search with _ wildcard - should NOT match single character names (would match if not escaped)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users").param("search", "_"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(0))

        // Normal search should still work
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users").param("search", "Alice"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Alice"))
    }

    @Test
    fun `PUT users should update active status to false`() {
        val savedUser = userRepository.save(User(
            email = "user@example.com",
            firstName = "Test",
            lastName = "User",
            active = true
        ))

        val updateRequest = UpdateUserRequest(active = false)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/${savedUser.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(false))

        val updatedUser = userRepository.findById(savedUser.id!!).get()
        assertFalse(updatedUser.active)
    }

    @Test
    fun `PUT users should update active status to true`() {
        val savedUser = userRepository.save(User(
            email = "user@example.com",
            firstName = "Test",
            lastName = "User",
            active = false
        ))

        val updateRequest = UpdateUserRequest(active = true)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/${savedUser.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(true))

        val updatedUser = userRepository.findById(savedUser.id!!).get()
        assertTrue(updatedUser.active)
    }

    @Test
    fun `PUT users should update only email when only email provided`() {
        val savedUser = userRepository.save(User(
            email = "old@example.com",
            firstName = "Original",
            lastName = "Name"
        ))

        val updateRequest = UpdateUserRequest(email = "new@example.com")

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/${savedUser.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("new@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Original"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Name"))
    }

    @Test
    fun `PUT users should update only firstName when only firstName provided`() {
        val savedUser = userRepository.save(User(
            email = "user@example.com",
            firstName = "Old",
            lastName = "Name"
        ))

        val updateRequest = UpdateUserRequest(firstName = "New")

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/${savedUser.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("New"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Name"))
    }

    @Test
    fun `PUT users should update only lastName when only lastName provided`() {
        val savedUser = userRepository.save(User(
            email = "user@example.com",
            firstName = "First",
            lastName = "Old"
        ))

        val updateRequest = UpdateUserRequest(lastName = "New")

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/${savedUser.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("First"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("New"))
    }

    @Test
    fun `GET users should return empty page when no users exist`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.empty").value(true))
    }

    @Test
    fun `GET users should return last page correctly`() {
        // Create 5 users
        for (i in 1..5) {
            userRepository.save(User(email = "user$i@example.com", firstName = "User", lastName = "$i"))
        }

        // Request page 1 with size 3 (should get 2 remaining users)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?page=1&size=3"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(false))
    }

    @Test
    fun `GET users should sort by id in ascending order by default`() {
        val user3 = userRepository.save(User(email = "user3@example.com", firstName = "User", lastName = "Three"))
        val user1 = userRepository.save(User(email = "user1@example.com", firstName = "User", lastName = "One"))
        val user2 = userRepository.save(User(email = "user2@example.com", firstName = "User", lastName = "Two"))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(user3.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(user1.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].id").value(user2.id))
    }

    @Test
    fun `POST users should validate maximum email length`() {
        val longEmail = "a".repeat(90) + "@example.com" // 102 chars - exceeds 100 limit

        val request = mapOf(
            "email" to longEmail,
            "firstName" to "Test",
            "lastName" to "User"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors").exists())
    }

    @Test
    fun `PUT users should validate email format when updating email`() {
        val savedUser = userRepository.save(User(
            email = "valid@example.com",
            firstName = "Test",
            lastName = "User"
        ))

        val updateRequest = UpdateUserRequest(email = "invalid-email-format")

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/${savedUser.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors").exists())
    }

    @Test
    fun `GET users with pagination should return correct page metadata`() {
        // Create 25 users
        for (i in 1..25) {
            userRepository.save(User(email = "user$i@example.com", firstName = "User", lastName = "$i"))
        }

        // Request page 1 (second page) with size 10
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?page=1&size=10"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(25))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(false))
    }
}
