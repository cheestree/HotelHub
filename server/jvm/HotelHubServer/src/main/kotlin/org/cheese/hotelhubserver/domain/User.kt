package org.cheese.hotelhubserver.domain

data class User(
    val id: Int,
    val username: String,
    val passwordValidation: PasswordValidationInfo
)