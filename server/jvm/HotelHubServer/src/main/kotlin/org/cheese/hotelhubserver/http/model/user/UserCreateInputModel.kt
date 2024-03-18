package org.cheese.hotelhubserver.http.model.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

const val USERNAME_MIN_LENGTH = 8L
const val USERNAME_MAX_LENGTH = 20L
const val PASSWORD_MIN_LENGTH = 20L

data class UserCreateInputModel(
    @NotEmpty
    @Min(value = USERNAME_MIN_LENGTH, message = "Username must be at least $USERNAME_MIN_LENGTH characters long")
    @Max(value = USERNAME_MAX_LENGTH, message = "Username must be at most $USERNAME_MAX_LENGTH characters long")
    val username: String,
    @NotEmpty
    @Email
    val email: String,
    @NotEmpty
    @Min(value = 6, message = "Password must be at least $PASSWORD_MIN_LENGTH characters long")
    val password: String,
)
