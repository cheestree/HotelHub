package org.cheese.hotelhubserver.http.model.user

data class UserCreateTokenInputModel(
    val username: String,
    val password: String
)