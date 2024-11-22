package com.cheese.hotelhub.domain.user

import com.cheese.hotelhub.domain.enums.Role

data class AuthenticatedUser(
    val id: Long,
    val oauthId: String,
    val name: String,
    val email: String,
    val role: Role,
)