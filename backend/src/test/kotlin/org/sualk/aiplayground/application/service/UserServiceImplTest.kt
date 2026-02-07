package org.sualk.aiplayground.application.service

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.sualk.aiplayground.application.dto.request.CreateUserRequest
import org.sualk.aiplayground.application.dto.request.UpdateUserRequest
import org.sualk.aiplayground.application.exception.UserAlreadyExistsException
import org.sualk.aiplayground.application.exception.UserNotFoundException
import org.sualk.aiplayground.application.service.impl.UserServiceImpl
import org.sualk.aiplayground.domain.entity.User
import org.sualk.aiplayground.domain.repository.UserRepository
import java.time.LocalDateTime
import java.util.*

class UserServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        userService = UserServiceImpl(userRepository)
    }

    @Test
    fun `createUser should create user successfully`() {
        val request = CreateUserRequest(
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        val savedUser = User(
            id = 1L,
            email = request.email,
            firstName = request.firstName,
            lastName = request.lastName,
            active = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.save(any()) } returns savedUser

        val response = userService.createUser(request)

        assertEquals(savedUser.id, response.id)
        assertEquals(savedUser.email, response.email)
        assertEquals(savedUser.firstName, response.firstName)
        assertEquals(savedUser.lastName, response.lastName)
        assertTrue(response.active)

        verify { userRepository.existsByEmail(request.email) }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `createUser should throw UserAlreadyExistsException when email exists`() {
        val request = CreateUserRequest(
            email = "existing@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        every { userRepository.existsByEmail(request.email) } returns true

        val exception = assertThrows<UserAlreadyExistsException> {
            userService.createUser(request)
        }

        assertTrue(exception.message!!.contains(request.email))
        verify { userRepository.existsByEmail(request.email) }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `getUserById should return user successfully`() {
        val userId = 1L
        val user = User(
            id = userId,
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        every { userRepository.findById(userId) } returns Optional.of(user)

        val response = userService.getUserById(userId)

        assertEquals(userId, response.id)
        assertEquals(user.email, response.email)
        assertEquals(user.firstName, response.firstName)
        assertEquals(user.lastName, response.lastName)

        verify { userRepository.findById(userId) }
    }

    @Test
    fun `getUserById should throw UserNotFoundException when user not found`() {
        val userId = 999L

        every { userRepository.findById(userId) } returns Optional.empty()

        val exception = assertThrows<UserNotFoundException> {
            userService.getUserById(userId)
        }

        assertTrue(exception.message!!.contains(userId.toString()))
        verify { userRepository.findById(userId) }
    }

    @Test
    fun `getAllUsers should return paginated users`() {
        val users = listOf(
            User(id = 1L, email = "user1@example.com", firstName = "John", lastName = "Doe"),
            User(id = 2L, email = "user2@example.com", firstName = "Jane", lastName = "Smith")
        )
        val pageable = PageRequest.of(0, 20)
        val page = PageImpl(users, pageable, users.size.toLong())

        every {
            userRepository.findAll(
                any<org.springframework.data.jpa.domain.Specification<User>>(),
                any<Pageable>()
            )
        } returns page

        val response = userService.getAllUsers(pageable, null, null)

        assertEquals(2, response.content.size)
        assertEquals("user1@example.com", response.content[0].email)
        assertEquals("user2@example.com", response.content[1].email)

        verify {
            userRepository.findAll(
                any<org.springframework.data.jpa.domain.Specification<User>>(),
                any<Pageable>()
            )
        }
    }

    @Test
    fun `updateUser should update user partially`() {
        val userId = 1L
        val existingUser = User(
            id = userId,
            email = "old@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        val updateRequest = UpdateUserRequest(
            firstName = "Jane",
            lastName = null,
            email = null,
            active = null
        )

        every { userRepository.findById(userId) } returns Optional.of(existingUser)
        every { userRepository.save(any()) } returns existingUser.apply {
            firstName = "Jane"
        }

        val response = userService.updateUser(userId, updateRequest)

        assertEquals("Jane", response.firstName)
        assertEquals("old@example.com", response.email)
        assertEquals("Doe", response.lastName)

        verify { userRepository.findById(userId) }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `updateUser should throw UserAlreadyExistsException when email exists`() {
        val userId = 1L
        val existingUser = User(
            id = userId,
            email = "old@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        val updateRequest = UpdateUserRequest(
            email = "existing@example.com"
        )

        every { userRepository.findById(userId) } returns Optional.of(existingUser)
        every { userRepository.existsByEmail("existing@example.com") } returns true

        val exception = assertThrows<UserAlreadyExistsException> {
            userService.updateUser(userId, updateRequest)
        }

        assertTrue(exception.message!!.contains("existing@example.com"))
        verify { userRepository.findById(userId) }
        verify { userRepository.existsByEmail("existing@example.com") }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `deleteUser should delete user`() {
        val userId = 1L
        val user = User(
            id = userId,
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe",
            active = true
        )

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { userRepository.delete(any<User>()) } just Runs

        userService.deleteUser(userId)

        verify { userRepository.findById(userId) }
        verify { userRepository.delete(user) }
    }

    @Test
    fun `getUserByEmail should return user successfully`() {
        val email = "test@example.com"
        val user = User(
            id = 1L,
            email = email,
            firstName = "John",
            lastName = "Doe"
        )

        every { userRepository.findByEmail(email) } returns Optional.of(user)

        val response = userService.getUserByEmail(email)

        assertEquals(email, response.email)
        assertEquals(user.firstName, response.firstName)

        verify { userRepository.findByEmail(email) }
    }

    @Test
    fun `getUserByEmail should throw UserNotFoundException when user not found`() {
        val email = "nonexistent@example.com"

        every { userRepository.findByEmail(email) } returns Optional.empty()

        assertThrows<UserNotFoundException> {
            userService.getUserByEmail(email)
        }

        verify { userRepository.findByEmail(email) }
    }

    @Test
    fun `getAllUsers should use specifications for search and filter`() {
        val users = listOf(
            User(id = 1L, email = "user1@example.com", firstName = "John", lastName = "Doe")
        )
        val pageable = PageRequest.of(0, 20)
        val page = PageImpl(users, pageable, users.size.toLong())

        every {
            userRepository.findAll(
                any<org.springframework.data.jpa.domain.Specification<User>>(),
                any<Pageable>()
            )
        } returns page

        val response = userService.getAllUsers(pageable, "test", true)

        assertEquals(1, response.content.size)
        verify {
            userRepository.findAll(
                any<org.springframework.data.jpa.domain.Specification<User>>(),
                any<Pageable>()
            )
        }
    }
}
