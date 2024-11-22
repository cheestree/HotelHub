package com.cheese.hotelhub.service

import com.cheese.hotelhub.domain.enums.Role
import com.cheese.hotelhub.domain.user.AuthenticatedUser
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class JwtService {
    fun extractAuthenticatedUser(principal: JwtAuthenticationToken): AuthenticatedUser? {
        val jwt = principal.token
        val role = Role.valueOf(jwt.claims["role"] as String)

        return AuthenticatedUser(
            id = 0,
            oauthId = jwt.subject,
            name = jwt.claims["name"] as String,
            email = jwt.claims["email"] as String,
            role = role
        )
    }
}