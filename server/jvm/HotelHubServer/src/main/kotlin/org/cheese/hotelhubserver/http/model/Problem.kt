package org.cheese.hotelhubserver.http.model

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

data class Problem(
    val title: String,
    val detail: String? = null,
) {
    fun toResponse(
        status: HttpStatusCode,
        headers: HttpHeaders? = null,
    ) = ResponseEntity
        .status(status)
        .header("Content-Type", MEDIA_TYPE)
        .headers(headers)
        .body<Problem>(this)

    companion object {
        const val MEDIA_TYPE = "application/problem+json"
    }
}
