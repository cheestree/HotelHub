package org.cheese.hotelhubserver.repository.jdbi

import kotlinx.datetime.Instant
import org.cheese.hotelhubserver.domain.PasswordValidationInfo
import org.cheese.hotelhubserver.domain.Token
import org.cheese.hotelhubserver.domain.TokenValidationInfo
import org.cheese.hotelhubserver.domain.User

interface UserRepository {

    fun storeUser(
        username: String,
        email: String,
        passwordValidation: PasswordValidationInfo
    ): Int

    fun getUserByUsername(username: String): User?

    fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>?

    fun isUserStoredByUsername(username: String): Boolean

    fun createToken(token: Token, maxTokens: Int)

    fun updateTokenLastUsed(token: Token, now: Instant)

    fun removeTokenByValidationInfo(tokenValidationInfo: TokenValidationInfo): Int
}