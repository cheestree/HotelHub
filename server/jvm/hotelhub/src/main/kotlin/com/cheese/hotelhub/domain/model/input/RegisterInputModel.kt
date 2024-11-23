package com.cheese.hotelhub.domain.model.input

data class RegisterInputModel(
    val oauthId: String,
    val username: String,
    val password: String,
    val email: String,
)