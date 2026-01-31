package org.sualk.aiplayground.application.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.sualk.aiplayground.application.dto.request.CreateUserRequest
import org.sualk.aiplayground.application.dto.request.UpdateUserRequest
import org.sualk.aiplayground.application.dto.response.UserResponse
import org.sualk.aiplayground.application.exception.UserAlreadyExistsException
import org.sualk.aiplayground.application.exception.UserNotFoundException
import org.sualk.aiplayground.application.service.UserService
import org.sualk.aiplayground.domain.entity.User
import org.sualk.aiplayground.domain.repository.UserRepository

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    @Transactional
    override fun createUser(request: CreateUserRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException(request.email)
        }
        val user = request.toEntity()
        val savedUser = userRepository.save(user)
        return savedUser.toResponse()
    }

    override fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(id) }
        return user.toResponse()
    }

    override fun getAllUsers(pageable: Pageable, active: Boolean?): Page<UserResponse> {
        return if (active != null) {
            userRepository.findByActive(active, pageable).map { it.toResponse() }
        } else {
            userRepository.findAll(pageable).map { it.toResponse() }
        }
    }

    @Transactional
    override fun updateUser(id: Long, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(id) }

        request.email?.let {
            if (it != user.email && userRepository.existsByEmail(it)) {
                throw UserAlreadyExistsException(it)
            }
            user.email = it
        }

        request.firstName?.let { user.firstName = it }
        request.lastName?.let { user.lastName = it }
        request.active?.let { user.active = it }

        val updatedUser = userRepository.save(user)
        return updatedUser.toResponse()
    }

    @Transactional
    override fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(id) }
        user.active = false
        userRepository.save(user)
    }

    override fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UserNotFoundException(-1) }
        return user.toResponse()
    }

    private fun CreateUserRequest.toEntity(): User {
        return User(
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName
        )
    }

    private fun User.toResponse(): UserResponse {
        return UserResponse(
            id = this.id ?: 0,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            active = this.active,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
