package org.sualk.aiplayground.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.sualk.aiplayground.domain.entity.User
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun findByActive(active: Boolean): List<User>
    fun existsByEmail(email: String): Boolean
}
