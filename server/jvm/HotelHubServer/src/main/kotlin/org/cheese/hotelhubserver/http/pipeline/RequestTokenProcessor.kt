package org.cheese.hotelhubserver.http.pipeline

import org.cheese.hotelhubserver.domain.AuthenticatedUser
import org.cheese.hotelhubserver.services.UserServices
import org.springframework.stereotype.Component

@Component
class RequestTokenProcessor(
    val usersService: UserServices
) {

    fun processAuthorizationHeaderValue(authorizationValue: String?): AuthenticatedUser? {
        if (authorizationValue == null) {
            return null
        }
        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2) {
            return null
        }
        if (parts[0].lowercase() != SCHEME) {
            return null
        }
        return usersService.getUserByToken(parts[1])?.let {
            AuthenticatedUser(
                it,
                parts[1]
            )
        }
    }

    companion object {
        const val SCHEME = "bearer"
    }
}