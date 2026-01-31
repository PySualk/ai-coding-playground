package org.sualk.aiplayground.application.exception

class UserNotFoundException(userId: Long) : RuntimeException("User not found with id: $userId")
