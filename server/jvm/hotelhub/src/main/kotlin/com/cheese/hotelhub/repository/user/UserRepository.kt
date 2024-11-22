package com.cheese.hotelhub.repository.user

import com.cheese.hotelhub.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun findByOauthId(oauthId: String): Optional<User>
    fun existsUserByOauthId(oauthId: String): Boolean
}