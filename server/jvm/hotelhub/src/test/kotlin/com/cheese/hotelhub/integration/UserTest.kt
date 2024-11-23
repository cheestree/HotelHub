package com.cheese.hotelhub.integration

import com.cheese.hotelhub.domain.error.Error
import com.cheese.hotelhub.domain.path.ApiPaths
import com.cheese.hotelhub.domain.path.ApiPaths.REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.GET_REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.URL
import com.cheese.hotelhub.domain.path.ApiPaths.USER
import com.cheese.hotelhub.domain.path.ApiPaths.Users.GET_USER
import com.cheese.hotelhub.domain.user.User
import com.cheese.hotelhub.integration.base.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest : BaseTest() {

    @LocalServerPort
    private var port: Int = 0

    @Test
    fun `should return Success user profile`() {
        val path = ApiPaths.resolvePath(URL + USER + GET_USER, mapOf("port" to "$port", "userId" to "1"))
        val response = createGET(path)

        assertEquals(HttpStatus.OK, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, User::class.java)

        assertTrue(USERS.contains(actualUserResponse))
    }

    @Test
    fun `should return NotFound user profile`() {
        val path = ApiPaths.resolvePath(URL + USER + GET_USER, mapOf("port" to "$port", "userId" to "999"))
        val response = createGET(path)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }
}