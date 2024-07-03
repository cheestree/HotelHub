package org.cheese.hotelhubserver.http.model.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserCreateTokenInputModel(
    @field:NotBlank(message = "can't be blank")
    @field:Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = "must be between $USERNAME_MIN_LENGTH and $USERNAME_MAX_LENGTH characters long")
    val username: String,
    @field:NotBlank(message = "can't be blank")
    @field:Size(min = PASSWORD_MIN_LENGTH, message = "must be at least $PASSWORD_MIN_LENGTH characters long")
    val password: String,
)
