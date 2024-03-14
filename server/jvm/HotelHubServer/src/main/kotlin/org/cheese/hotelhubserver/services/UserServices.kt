package org.cheese.hotelhubserver.services

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.cheese.hotelhubserver.domain.user.User
import org.cheese.hotelhubserver.domain.user.UserDomain
import org.cheese.hotelhubserver.domain.user.token.Token
import org.cheese.hotelhubserver.repository.TransactionManager
import org.cheese.hotelhubserver.util.Either
import org.cheese.hotelhubserver.util.failure
import org.cheese.hotelhubserver.util.success
import org.springframework.stereotype.Component

data class TokenExternalInfo(
    val tokenValue: String,
    val tokenExpiration: Instant,
)

sealed class UserCreationError {
    object UserAlreadyExists : UserCreationError()

    object InsecurePassword : UserCreationError()
}
typealias UserCreationResult = Either<UserCreationError, Int>

sealed class TokenCreationError {
    object UserOrPasswordAreInvalid : TokenCreationError()
}
typealias TokenCreationResult = Either<TokenCreationError, TokenExternalInfo>

@Component
class UserServices(
    private val tm: TransactionManager,
    private val userDomain: UserDomain,
    private val clock: Clock,
) {
    fun createUser(
        username: String,
        email: String,
        password: String,
    ): UserCreationResult {
        if (!userDomain.isSafePassword(password)) {
            return failure(UserCreationError.InsecurePassword)
        }

        val passwordValidationInfo = userDomain.createPasswordValidationInformation(password)

        return tm.run {
            val usersRepository = it.userRepository
            if (usersRepository.isUserStoredByUsername(username)) {
                failure(UserCreationError.UserAlreadyExists)
            } else {
                val id = usersRepository.storeUser(username, email, passwordValidationInfo)
                success(id)
            }
        }
    }

    fun createToken(
        username: String,
        password: String,
    ): TokenCreationResult {
        if (username.isBlank() || password.isBlank()) {
            failure(TokenCreationError.UserOrPasswordAreInvalid)
        }
        return tm.run {
            val usersRepository = it.userRepository
            val user: User =
                usersRepository.getUserByUsername(username)
                    ?: return@run failure(TokenCreationError.UserOrPasswordAreInvalid)
            if (!userDomain.validatePassword(password, user.passwordValidation)) {
                if (!userDomain.validatePassword(password, user.passwordValidation)) {
                    return@run failure(TokenCreationError.UserOrPasswordAreInvalid)
                }
            }
            val tokenValue = userDomain.generateTokenValue()
            val now = clock.now()
            val newToken =
                Token(
                    userDomain.createTokenValidationInformation(tokenValue),
                    user.id,
                    createdAt = now,
                    lastUsedAt = now,
                )
            usersRepository.createToken(newToken, userDomain.maxNumberOfTokensPerUser)
            Either.Right(
                TokenExternalInfo(
                    tokenValue,
                    userDomain.getTokenExpiration(newToken),
                ),
            )
        }
    }

    fun getUserByToken(token: String): User? {
        if (!userDomain.canBeToken(token)) {
            return null
        }
        return tm.run {
            val usersRepository = it.userRepository
            val tokenValidationInfo = userDomain.createTokenValidationInformation(token)
            val userAndToken = usersRepository.getTokenByTokenValidationInfo(tokenValidationInfo)
            if (userAndToken != null && userDomain.isTokenTimeValid(clock, userAndToken.second)) {
                usersRepository.updateTokenLastUsed(userAndToken.second, clock.now())
                userAndToken.first
            } else {
                null
            }
        }
    }

    fun revokeToken(token: String): Boolean {
        val tokenValidationInfo = userDomain.createTokenValidationInformation(token)
        return tm.run {
            it.userRepository.removeTokenByValidationInfo(tokenValidationInfo)
            true
        }
    }
}
