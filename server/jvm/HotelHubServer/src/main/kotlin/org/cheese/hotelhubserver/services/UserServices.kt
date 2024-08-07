package org.cheese.hotelhubserver.services

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import kotlinx.datetime.Clock
import org.cheese.hotelhubserver.domain.exceptions.UserExceptions.*
import org.cheese.hotelhubserver.domain.user.AuthenticatedUser
import org.cheese.hotelhubserver.domain.user.Role
import org.cheese.hotelhubserver.domain.user.User
import org.cheese.hotelhubserver.domain.user.UserDomain
import org.cheese.hotelhubserver.domain.user.token.Token
import org.cheese.hotelhubserver.domain.user.token.TokenExternalInfo
import org.cheese.hotelhubserver.repository.interfaces.TransactionManager
import org.cheese.hotelhubserver.util.requireOrThrow
import org.springframework.stereotype.Component
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
        role: Role,
    ): Int {
        requireOrThrow<InsecurePassword>(domain.isSafePassword(password)) { "Password is not secure" }

        val passwordValidationInfo = domain.createPasswordValidationInformation(password)

        return tm.run {
            val usersRepository = it.userRepository
            requireOrThrow<UserAlreadyExists>(!usersRepository.userExistsByUsername(username)) { "User already exists" }
            usersRepository.storeUser(username, email, passwordValidationInfo, role)
        }
    }

    fun login(
        username: String,
        password: String,
    ): TokenExternalInfo =
        tm.run {
            requireOrThrow<UserDoesntExist>(it.userRepository.userExistsByUsername(username)) {
                "User doesn't exist"
            }
            val user = it.userRepository.getUserByUsername(username)
            requireOrThrow<UserOrPasswordAreInvalid>(domain.validatePassword(password, user.passwordValidation)) {
                "Incorrect username or password"
            }
            createToken(user.id)
        }

    fun logout(
        response: HttpServletResponse,
        user: AuthenticatedUser,
    ){
        deleteCookie(response)
        revokeToken(user.token)
    }

    private fun createToken(userId: Int): TokenExternalInfo {
        val tokenValue = domain.generateTokenValue()
        val now = clock.now()
        val token =
            Token(
                tokenValidationInfo = domain.createTokenValidationInformation(tokenValue),
                userId = userId,
                createdAt = now,
                lastUsedAt = now,
            )
        tm.run { it.userRepository.createToken(token, 1) }
        return TokenExternalInfo(
            tokenValue = tokenValue,
            tokenExpiration = domain.getTokenExpiration(token),
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

    fun getUserById(id: Int): User {
        return tm.run {
            requireOrThrow<UserNotFound>(it.userRepository.userExists(id)) { "User was not found" }
            it.userRepository.getUserById(id)
        }
    }

    fun revokeToken(token: String): Boolean {
        val tokenValidationInfo = domain.createTokenValidationInformation(token)
        return tm.run {
            it.userRepository.removeTokenByValidationInfo(tokenValidationInfo)
            true
        }
    }

    fun createCookie(
        response: HttpServletResponse,
        token: TokenExternalInfo,
        username: String,
    ) {
        val cookie = Cookie("token", token.tokenValue).apply {
            path = "/api"
            maxAge = token.tokenExpiration.epochSeconds.toInt()
            isHttpOnly = true
        }
        /*
        val playerStats = tm.run {
            domain.toUserDetails(it.userRepository.getUserByUsername(username))
        }
        val encodedPlayerStats = URLEncoder.encode(playerStats, StandardCharsets.UTF_8.toString())
        val player = Cookie("player", encodedPlayerStats).apply {
            path = "/"
            maxAge = token.tokenExpiration.epochSeconds.toInt()
            isHttpOnly = false
        }
        */
        response.addCookie(cookie)
        //  response.addCookie(player)
    }

    fun deleteCookie(response: HttpServletResponse) {
        val cookie = Cookie("token", "").apply {
            path = "/api"
            maxAge = 0
            isHttpOnly = true
            secure = true
        }
        /*
        val player = Cookie("player", "").apply {
            path = "/"
            maxAge = 0
            isHttpOnly = false
            secure = true
        }
        */
        response.addCookie(cookie)
        //  response.addCookie(player)
    }
}
