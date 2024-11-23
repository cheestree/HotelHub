package com.cheese.hotelhub.interceptor

import com.cheese.hotelhub.domain.annotation.ProtectedRoute
import com.cheese.hotelhub.domain.annotation.RoleRoute
import com.cheese.hotelhub.domain.enums.AuthProvider
import com.cheese.hotelhub.domain.enums.Role
import com.cheese.hotelhub.domain.user.AuthenticatedUser
import com.cheese.hotelhub.service.JwtService
import com.cheese.hotelhub.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class Interceptor(
    private val jwtService: JwtService,
    private val userService: UserService
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authenticatedUser = extractUserInfo(request)

        if (handler is HandlerMethod) {
            val isProtected = handler.hasMethodAnnotation(ProtectedRoute::class.java)
            val roleRoute = handler.method.getAnnotation(RoleRoute::class.java)

            if (isProtected) {
                if (authenticatedUser == null) {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    return false
                }

                if (roleRoute != null && !checkMethodAccess(roleRoute.role, authenticatedUser.role)) {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    return false
                }

                val dbUser = userService.getUserByEmail(authenticatedUser.email)

                if (dbUser != null) {
                    val authenticatedUserFromDb = AuthenticatedUser(
                        id = dbUser.id,
                        email = dbUser.email,
                        oauthId = dbUser.oauthId,
                        authProvider = dbUser.authProvider,
                        name = dbUser.name,
                        role = dbUser.role
                    )
                    request.setAttribute("authenticatedUser", authenticatedUserFromDb)
                    return true
                } else {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    return false
                }
            } else {
                return true
            }
        }
        return true
    }

    private fun checkMethodAccess(requiredRole: Role?, userRole: Role): Boolean {
        if (requiredRole == null) return true

        return when (userRole) {
            Role.ADMIN -> true
            Role.MEMBER -> requiredRole == Role.MEMBER
            Role.OWNER -> requiredRole == Role.OWNER || requiredRole == Role.MEMBER
        }
    }

    private fun extractUserInfo(request: HttpServletRequest): AuthenticatedUser? {
        val principal = request.userPrincipal
        if(principal !is JwtAuthenticationToken) return null

        val email = jwtService.extractEmail(principal)
        val oauthId = jwtService.extractOauthId(principal)
        val authProvider = jwtService.extractAuthProvider(principal)
        val role = jwtService.extractRole(principal)
        val name = jwtService.extractName(principal)

        if(email == null) return null

        return AuthenticatedUser(
            id = 0L,
            email = email,
            oauthId = oauthId,
            authProvider = authProvider ?: AuthProvider.EMAIL,
            name = name ?: "Unknown",
            role = role ?: Role.MEMBER
        )
    }
}