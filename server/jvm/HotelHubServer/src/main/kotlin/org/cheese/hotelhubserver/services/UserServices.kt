package org.cheese.hotelhubserver.services

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import kotlinx.datetime.Clock
import org.cheese.hotelhubserver.domain.exceptions.UserExceptions.*
import org.cheese.hotelhubserver.domain.user.User
import org.cheese.hotelhubserver.domain.user.UserDomain
import org.cheese.hotelhubserver.domain.user.token.Token
import org.cheese.hotelhubserver.domain.user.token.TokenExternalInfo
import org.cheese.hotelhubserver.repository.interfaces.TransactionManager
import org.cheese.hotelhubserver.util.requireOrThrow
import org.springframework.stereotype.Component

@Component
class UserServices(
    private val tm: TransactionManager,
    private val domain: UserDomain,
    private val clock: Clock,
) {
    fun createUser(
        username: String,
        email: String,
        password: String,
    ): Int {
        requireOrThrow<InsecurePassword>(domain.isSafePassword(password)) { "Insecure password" }

        val passwordValidationInfo = domain.createPasswordValidationInformation(password)

        return tm.run {
            val usersRepository = it.userRepository
            requireOrThrow<UserAlreadyExists>(!usersRepository.isUserStoredByUsername(username)) { "User already exists" }
            usersRepository.storeUser(username, email, passwordValidationInfo)
        }
    }

    fun login(
        username: String,
        password: String
    ): TokenExternalInfo = tm.run {
        requireOrThrow<UserOrPasswordAreInvalid>(it.userRepository.isUserStoredByUsername(username)) {
            "Incorrect username or password"
        }
        val user = it.userRepository.getUserByUsername(username)
        requireOrThrow<UserOrPasswordAreInvalid>(domain.validatePassword(password, user.passwordValidation)) {
            "Incorrect username or password"
        }
        createToken(user.id)
    }

    private fun createToken(userId: Int): TokenExternalInfo {
        val tokenValue = domain.generateTokenValue()
        val now = clock.now()
        val token = Token(
            tokenValidationInfo = domain.createTokenValidationInformation(tokenValue),
            userId = userId,
            createdAt = now,
            lastUsedAt = now
        )
        tm.run {
            requireOrThrow<UserNotFound>(it.userRepository.getUserById(userId)) { "User was not found" }
            it.userRepository.createToken(token, 1)
        }
        return TokenExternalInfo(
            tokenValue = tokenValue,
            tokenExpiration = domain.getTokenExpiration(token)
        )
    }

    fun getUserByToken(token: String): User? {
        if (!domain.canBeToken(token)) return null
        return tm.run {
            val tokenValidationInfo = domain.createTokenValidationInformation(token)
            val userAndToken = it.userRepository.getTokenByTokenValidationInfo(tokenValidationInfo)
            if (userAndToken != null && domain.isTokenTimeValid(clock, userAndToken.second)) {
                it.userRepository.updateTokenLastUsed(userAndToken.second, clock.now())
                userAndToken.first
            } else {
                null
            }
        }
    }

    fun revokeToken(token: String): Boolean {
        val tokenValidationInfo = domain.createTokenValidationInformation(token)
        return tm.run {
            it.userRepository.removeTokenByValidationInfo(tokenValidationInfo)
            true
        }
    }

    fun createCookie(response: HttpServletResponse, token: TokenExternalInfo, username: String) {
        val cookie = Cookie("token", token.tokenValue).also {
            it.path = "/api"
            it.maxAge = token.tokenExpiration.epochSeconds.toInt()
            it.isHttpOnly = true
        }
        val playerStats: User = tm.run {
            it.userRepository.getUserByUsername(username)
        }
        val player = Cookie("player", playerStats.toString()).also {
            it.path = "/"
            it.maxAge = token.tokenExpiration.epochSeconds.toInt()
            it.isHttpOnly = false
        }
        response.addCookie(cookie)
        response.addCookie(player)
    }

    fun deleteCookie(response: HttpServletResponse) {
        val cookie = Cookie("token", "").also {
            it.path = "/api"
            it.maxAge = 0
            it.isHttpOnly = true
        }
        val player = Cookie("player", "").also {
            it.path = "/"
            it.maxAge = 0
            it.isHttpOnly = false
        }
        response.addCookie(cookie)
        response.addCookie(player)
    }
}
