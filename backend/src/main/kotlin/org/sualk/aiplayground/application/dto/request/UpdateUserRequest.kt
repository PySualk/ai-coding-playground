package org.sualk.aiplayground.application.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:Email(message = "Email must be valid")
    @field:Size(max = 100, message = "Email must not exceed 100 characters")
    val email: String? = null,

    @field:Size(max = 100, message = "First name must not exceed 100 characters")
    val firstName: String? = null,

    @field:Size(max = 100, message = "Last name must not exceed 100 characters")
    val lastName: String? = null,

    val active: Boolean? = null
)
