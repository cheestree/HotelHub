package com.cheese.hotelhub.service

import com.cheese.hotelhub.domain.enums.AuthProvider
import com.cheese.hotelhub.domain.enums.Role
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class JwtService {
    fun extractEmail(token: JwtAuthenticationToken): String? {
        return token.tokenAttributes["email"] as? String
    }

    fun extractOauthId(token: JwtAuthenticationToken): String? {
        return token.tokenAttributes["oauthId"] as? String
    }

    fun extractAuthProvider(token: JwtAuthenticationToken): AuthProvider? {
        val authProviderString = token.tokenAttributes["authProvider"] as? String
        return if (authProviderString != null) {
            try {
                AuthProvider.valueOf(authProviderString)
            } catch (e: IllegalArgumentException) {
                null
            }
        } else {
            null
        }
    }

    fun extractRole(token: JwtAuthenticationToken): Role? {
        val roleString = token.tokenAttributes["role"] as? String
        return if (roleString != null) {
            try {
                Role.valueOf(roleString)
            } catch (e: IllegalArgumentException) {
                null
            }
        } else {
            null
        }
    }

    fun extractName(token: JwtAuthenticationToken): String? {
        return token.tokenAttributes["name"] as? String
    }
}