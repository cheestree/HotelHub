package org.cheese.hotelhubserver.domain

data class AuthenticatedUser(
    val user: User,
    val token: String
)