package com.cheese.hotelhub.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import kotlin.test.Test
import com.cheese.hotelhub.domain.error.Error

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GlobalExceptionHandlerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return Bad Request for invalid path variable`() {
        val response = restTemplate.getForEntity("http://localhost:$port/users/abc", String::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

        // Deserialize the response body into an Error object
        val actualErrorResponse = objectMapper.readValue(response.body, Error::class.java)

        val expectedErrorResponse = Error(
            "Invalid value for path variable: userId",
            "Type mismatch",
            HttpStatus.BAD_REQUEST
        )

        assertEquals(expectedErrorResponse, actualErrorResponse)
    }
}
