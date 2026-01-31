package org.sualk.aiplayground.application.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.sualk.aiplayground.application.dto.request.CreateUserRequest
import org.sualk.aiplayground.application.dto.request.UpdateUserRequest
import org.sualk.aiplayground.application.dto.response.UserResponse

interface UserService {
    fun createUser(request: CreateUserRequest): UserResponse
    fun getUserById(id: Long): UserResponse
    fun getAllUsers(pageable: Pageable, active: Boolean? = null): Page<UserResponse>
    fun updateUser(id: Long, request: UpdateUserRequest): UserResponse
    fun deleteUser(id: Long)
    fun getUserByEmail(email: String): UserResponse
}
