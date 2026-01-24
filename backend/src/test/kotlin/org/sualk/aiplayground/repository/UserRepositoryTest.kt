package org.sualk.aiplayground.repository

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.annotation.Transactional
import org.sualk.aiplayground.domain.entity.User
import org.sualk.aiplayground.domain.repository.UserRepository
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@Transactional
class UserRepositoryTest {

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
    private lateinit var userRepository: UserRepository

    @Test
    fun `should save and retrieve user`() {
        val user = User(
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        val savedUser = userRepository.save(user)

        assertNotNull(savedUser.id)
        assertEquals("test@example.com", savedUser.email)
        assertEquals("John", savedUser.firstName)
    }

    @Test
    fun `should find user by email`() {
        val user = User(
            email = "unique@example.com",
            firstName = "Jane",
            lastName = "Smith"
        )
        userRepository.save(user)

        val found = userRepository.findByEmail("unique@example.com")

        assertTrue(found.isPresent)
        assertEquals("Jane", found.get().firstName)
    }

    @Test
    fun `should check if email exists`() {
        val user = User(
            email = "exists@example.com",
            firstName = "Bob",
            lastName = "Jones"
        )
        userRepository.save(user)

        assertTrue(userRepository.existsByEmail("exists@example.com"))
        assertFalse(userRepository.existsByEmail("notexists@example.com"))
    }
}
