package org.sualk.aiplayground.domain.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.sualk.aiplayground.domain.entity.User
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun findByActive(active: Boolean, pageable: Pageable): Page<User>
    fun existsByEmail(email: String): Boolean

    @Query("""
        SELECT u FROM User u
        WHERE (:search IS NULL OR
               LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:active IS NULL OR u.active = :active)
    """)
    fun searchUsers(
        @Param("search") search: String?,
        @Param("active") active: Boolean?,
        pageable: Pageable
    ): Page<User>
}
