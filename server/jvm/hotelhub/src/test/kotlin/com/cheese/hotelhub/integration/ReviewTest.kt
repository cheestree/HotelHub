package com.cheese.hotelhub.integration

import com.cheese.hotelhub.domain.error.Error
import com.cheese.hotelhub.domain.review.Review
import com.cheese.hotelhub.integration.base.BaseTest
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewTest : BaseTest() {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return NotFound hotel not found on hotel review`(){
        val response = restTemplate.getForEntity("http://localhost:$port/reviews/999/review/1", String::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }

    @Test
    fun `should return NotFound user not found on review of an hotel`(){
        val response = restTemplate.getForEntity("http://localhost:$port/reviews/1/review/999", String::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }


    @Test
    fun `should return NotFound review not found of an hotel`(){
        val response = restTemplate.getForEntity("http://localhost:$port/reviews/2/review/1", String::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }

    @Test
    fun `should return Success review`(){
        val response = restTemplate.getForEntity("http://localhost:$port/reviews/1/review/1", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Review::class.java)

        assertNotNull(actualUserResponse)
    }

    @Test
    fun `should return NotFound hotel not found on list of reviews`(){
        val response = restTemplate.getForEntity("http://localhost:$port/reviews/999", String::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }

    @Test
    fun `should return Success list of reviews`(){
        val response = restTemplate.getForEntity("http://localhost:$port/reviews/1", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val actualUserResponse: List<Review> = objectMapper.readValue(response.body, object : TypeReference<List<Review>>() {})

        assertNotNull(actualUserResponse)
    }
}