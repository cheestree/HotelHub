package org.cheese.hotelhubserver.domain.user

data class User(
    val id: Int,
    val username: String,
    val passwordValidation: PasswordValidationInfo,
)
