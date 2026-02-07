package org.sualk.aiplayground.domain.repository

import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import org.sualk.aiplayground.domain.entity.User

object UserSpecifications {

    /**
     * Escapes LIKE wildcard characters to prevent injection.
     * Replaces \ with \\, % with \%, and _ with \_ to treat them as literals.
     */
    private fun escapeLikeWildcards(input: String): String {
        return input
            .replace("\\", "\\\\")  // Escape backslash first
            .replace("%", "\\%")     // Escape percent wildcard
            .replace("_", "\\_")     // Escape underscore wildcard
    }

    fun hasSearch(search: String?): Specification<User> {
        return Specification { root, _, criteriaBuilder ->
            if (search.isNullOrBlank()) {
                criteriaBuilder.conjunction()
            } else {
                val escapedSearch = escapeLikeWildcards(search)
                val searchPattern = "%${escapedSearch.lowercase()}%"
                val escape = criteriaBuilder.literal('\\')
                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), searchPattern, escape),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchPattern, escape),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern, escape)
                )
            }
        }
    }

    fun hasActive(active: Boolean?): Specification<User> {
        return Specification { root, _, criteriaBuilder ->
            active?.let {
                criteriaBuilder.equal(root.get<Boolean>("active"), it)
            } ?: criteriaBuilder.conjunction()
        }
    }

    fun searchUsers(search: String?, active: Boolean?): Specification<User> {
        return Specification.where(hasSearch(search)).and(hasActive(active))
    }
}
