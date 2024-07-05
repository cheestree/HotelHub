package org.cheese.hotelhubserver.repository.interfaces.user

import kotlinx.datetime.Instant
import org.cheese.hotelhubserver.domain.user.PasswordValidationInfo
import org.cheese.hotelhubserver.domain.user.Role
import org.cheese.hotelhubserver.domain.user.User
import org.cheese.hotelhubserver.domain.user.token.Token
import org.cheese.hotelhubserver.domain.user.token.TokenValidationInfo

interface UserRepository {
    fun storeUser(
        username: String,
        email: String,
        passwordValidation: PasswordValidationInfo,
        role: Role,
    ): Int

    fun getUserByUsername(username: String): User

    fun getUserById(user: Int): User

    fun userExists(id: Int): Boolean

    fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>?

    fun userExistsByUsername(username: String): Boolean

    fun createToken(
        token: Token,
        maxTokens: Int,
    )

    fun updateTokenLastUsed(
        token: Token,
        now: Instant,
    )

    fun removeTokenByValidationInfo(tokenValidationInfo: TokenValidationInfo): Int

    fun deleteAll()
}
