package org.cheese.hotelhubserver.domain.user

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val passwordValidation: PasswordValidationInfo,
    val role: Role
)
