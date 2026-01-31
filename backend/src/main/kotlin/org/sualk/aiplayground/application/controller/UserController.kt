package org.sualk.aiplayground.application.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.sualk.aiplayground.application.dto.request.CreateUserRequest
import org.sualk.aiplayground.application.dto.request.UpdateUserRequest
import org.sualk.aiplayground.application.dto.response.UserResponse
import org.sualk.aiplayground.application.service.UserService

@RestController
@RequestMapping("/api/users")
@Validated
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        val response = userService.createUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val response = userService.getUserById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAllUsers(
        @PageableDefault(size = 20, sort = ["id"], direction = Sort.Direction.ASC) pageable: Pageable,
        @RequestParam(required = false) active: Boolean?
    ): ResponseEntity<Page<UserResponse>> {
        val response = userService.getAllUsers(pageable, active)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val response = userService.updateUser(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/by-email")
    fun getUserByEmail(@RequestParam @Email email: String): ResponseEntity<UserResponse> {
        val response = userService.getUserByEmail(email)
        return ResponseEntity.ok(response)
    }
}
