package org.cheese.hotelhubserver.http.pipeline

import org.cheese.hotelhubserver.domain.user.AuthenticatedUser
import org.cheese.hotelhubserver.services.UserServices
import org.springframework.stereotype.Component
import jakarta.servlet.http.Cookie

@Component
class RequestTokenProcessor(
    val usersService: UserServices,
) {
    fun processAuthorizationHeaderValue(authorizationValue: String?): AuthenticatedUser? {
        if (authorizationValue == null) return null
        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2 || parts[0].lowercase() != SCHEME) return null
        return usersService.getUserByToken(parts[1])?.let {
            AuthenticatedUser(
                it,
                parts[1],
            )
        }
    }

    fun processCookieValue(authorizationValue: Array<Cookie>?): AuthenticatedUser? {
        if(authorizationValue == null) return null
        val cookie = authorizationValue.firstOrNull { it.name == "token" } ?: return null
        val cookieVal = cookie.value
        return usersService.getUserByToken(cookieVal)?.let {
            AuthenticatedUser(it, cookieVal)
        }
    }

    companion object {
        const val SCHEME = "bearer"
    }
}
