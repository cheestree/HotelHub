package com.cheese.hotelhub.interceptor

import com.cheese.hotelhub.domain.enums.AuthProvider
import com.cheese.hotelhub.domain.enums.Role
import com.cheese.hotelhub.domain.user.User
import com.cheese.hotelhub.repository.user.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(private val userRepository: UserRepository) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oauthUser = authentication.principal as DefaultOAuth2User
        val email = oauthUser.attributes["email"] as String
        val name = oauthUser.attributes["name"] as String
        val oauthId = oauthUser.attributes["id"] as String

        userRepository.findByEmail(email).orElseGet {
            val newUser = User(
                oauthId = oauthId,
                authProvider = AuthProvider.GOOGLE,
                name = name,
                email = email,
                role = Role.MEMBER
            )
            userRepository.save(newUser)
        }

        super.onAuthenticationSuccess(request, response, authentication)
    }
}