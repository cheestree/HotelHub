package org.cheese.hotelhubserver.domain

interface TokenEncoder {
    fun createValidationInformation(token: String): TokenValidationInfo
}