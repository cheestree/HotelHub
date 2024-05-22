package org.cheese.hotelhubserver.repository.interfaces.user

import kotlinx.datetime.Instant
import org.cheese.hotelhubserver.domain.user.PasswordValidationInfo
import org.cheese.hotelhubserver.domain.user.User
import org.cheese.hotelhubserver.domain.user.token.Token
import org.cheese.hotelhubserver.domain.user.token.TokenValidationInfo

interface UserRepository {
    fun storeUser(
        username: String,
        email: String,
        passwordValidation: PasswordValidationInfo,
    ): Int
    fun getUserByUsername(username: String): User
    fun getUserById(user: Int): Boolean
    fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>?
    fun isUserStoredByUsername(username: String): Boolean
    fun createToken(
        token: Token,
        maxTokens: Int,
    )
    fun updateTokenLastUsed(
        token: Token,
        now: Instant,
    )
    fun removeTokenByValidationInfo(tokenValidationInfo: TokenValidationInfo): Int
}
