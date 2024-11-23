package com.cheese.hotelhub.integration

import com.cheese.hotelhub.domain.error.Error
import com.cheese.hotelhub.domain.path.ApiPaths
import com.cheese.hotelhub.domain.path.ApiPaths.REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.GET_REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.URL
import com.cheese.hotelhub.domain.path.ApiPaths.USER
import com.cheese.hotelhub.domain.path.ApiPaths.Users.GET_USER
import com.cheese.hotelhub.integration.base.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GlobalExceptionHandlerTest : BaseTest() {

    @LocalServerPort
    private var port: Int = 0

    @Test
    fun `should return Bad Request for invalid path variable`() {
        val path = ApiPaths.resolvePath(URL + USER + GET_USER, mapOf("port" to "$port", "userId" to "abc"))
        val response = createGET(path)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

        val actualErrorResponse = objectMapper.readValue(response.body, Error::class.java)

        val expectedErrorResponse = Error(
            "Invalid value for path variable: userId",
            "Type mismatch",
            HttpStatus.BAD_REQUEST
        )

        assertEquals(expectedErrorResponse, actualErrorResponse)
    }
}
