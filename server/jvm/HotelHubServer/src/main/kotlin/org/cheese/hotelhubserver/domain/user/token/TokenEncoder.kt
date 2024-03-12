package org.cheese.hotelhubserver.domain.user.token

interface TokenEncoder {
    fun createValidationInformation(token: String): TokenValidationInfo
}
