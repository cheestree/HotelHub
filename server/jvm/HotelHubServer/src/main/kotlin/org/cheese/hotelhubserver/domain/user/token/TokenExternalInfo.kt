package org.cheese.hotelhubserver.domain.user.token

import kotlinx.datetime.Instant

data class TokenExternalInfo(
    val tokenValue: String,
    val tokenExpiration: Instant,
)