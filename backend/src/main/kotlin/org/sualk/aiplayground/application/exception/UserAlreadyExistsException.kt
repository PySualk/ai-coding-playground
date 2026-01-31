package org.sualk.aiplayground.application.exception

class UserAlreadyExistsException(email: String) : RuntimeException("User already exists with email: $email")
