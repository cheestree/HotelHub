package com.cheese.hotelhub.integration

import com.cheese.hotelhub.domain.error.Error
import com.cheese.hotelhub.domain.model.input.ReviewInputModel
import com.cheese.hotelhub.domain.path.ApiPaths
import com.cheese.hotelhub.domain.path.ApiPaths.REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.DELETE_REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.GET_REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.GET_REVIEWS
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.POST_REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.URL
import com.cheese.hotelhub.domain.review.Review
import com.cheese.hotelhub.integration.base.BaseTest
import com.cheese.hotelhub.integration.base.Config
import com.fasterxml.jackson.core.type.TypeReference
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(Config::class)
class ReviewTest : BaseTest() {

    @LocalServerPort
    private var port: Int = 0

    @Test
    fun `should return NotFound hotel not found on hotel review`(){
        val path = ApiPaths.resolvePath(URL+REVIEW+GET_REVIEW, mapOf("port" to "$port", "hotelId" to "999", "reviewId" to "1"))
        val response = createGET(path)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }

    @Test
    fun `should return NotFound user not found on review of an hotel`(){
        val path = ApiPaths.resolvePath(URL+REVIEW+GET_REVIEW, mapOf("port" to "$port", "hotelId" to "1", "reviewId" to "999"))
        val response = createGET(path)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }


    @Test
    fun `should return NotFound review not found of an hotel`(){
        val path = ApiPaths.resolvePath(URL+REVIEW+GET_REVIEW, mapOf("port" to "$port", "hotelId" to "2", "reviewId" to "1"))
        val response = createGET(path)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }

    @Test
    fun `should return NotFound hotel to delete review`(){
        val headers = createHeaders(COMMENTER)
        val entity = HttpEntity(null, headers)

        val path = ApiPaths.resolvePath(URL+REVIEW+DELETE_REVIEW, mapOf("port" to "$port", "hotelId" to "999"))

        val deleteReview = createDELETE(path, entity)

        assertEquals(HttpStatus.NOT_FOUND, deleteReview.statusCode)
    }

    @Test
    fun `should return NotFound review to delete review`(){
        val headers = createHeaders(COMMENTER)
        val entity = HttpEntity(null, headers)

        val path = ApiPaths.resolvePath(URL+REVIEW+DELETE_REVIEW, mapOf("port" to "$port", "hotelId" to "4"))

        val deleteReview = createDELETE(path, entity)

        assertEquals(HttpStatus.NOT_FOUND, deleteReview.statusCode)
    }

    @Test
    fun `should return Success review`(){
        val path = ApiPaths.resolvePath(URL+REVIEW+GET_REVIEW, mapOf("port" to "$port", "hotelId" to "1", "reviewId" to "1"))
        val response = createGET(path)

        assertEquals(HttpStatus.OK, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Review::class.java)

        assertNotNull(actualUserResponse)
    }

    @Test
    fun `should return NotFound hotel not found on list of reviews`(){
        val path = ApiPaths.resolvePath(URL+REVIEW+GET_REVIEWS, mapOf("port" to "$port", "hotelId" to "999"))
        val response = createGET(path)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }

    @Test
    fun `should return Success list of reviews`(){
        val path = ApiPaths.resolvePath(URL+REVIEW+GET_REVIEWS, mapOf("port" to "$port", "hotelId" to "1"))
        val response = createGET(path)

        assertEquals(HttpStatus.OK, response.statusCode)

        val actualUserResponse: List<Review> = objectMapper.readValue(response.body, object : TypeReference<List<Review>>() {})

        assertNotNull(actualUserResponse)
    }

    @Test
    fun `should return NotFound hotel when trying to create review`() {
        val headers = createHeaders(COMMENTER)
        val body = ReviewInputModel(
            content = "Good hotel",
            rating = 2
        )
        val entity = HttpEntity(body, headers)

        val path = ApiPaths.resolvePath(URL+REVIEW+POST_REVIEW, mapOf("port" to "$port", "hotelId" to "999"))
        val response = createPOST(path, entity)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualReview = objectMapper.readValue(response.body, Error::class.java)

        assertNotNull(actualReview)
    }

    @Test
    fun `should return OK when review is created`() {
        val headers = createHeaders(COMMENTER)
        val hotel = HOTELS.random()
        val body = ReviewInputModel(
            content = "Good hotel",
            rating = 2
        )
        val entity = HttpEntity(body, headers)

        val path = ApiPaths.resolvePath(URL+REVIEW+POST_REVIEW, mapOf("port" to "$port", "hotelId" to "${hotel.id}"))
        val response = createPOST(path, entity)

        assertEquals(HttpStatus.OK, response.statusCode)

        val actualReview = objectMapper.readValue(response.body, Review::class.java)

        assertNotNull(actualReview)
    }

    @Test
    fun `should return OK when review is deleted`() {
        val headers = createHeaders(COMMENTER)
        val hotel = HOTELS.random()
        val body = ReviewInputModel(
            content = "Good hotel",
            rating = 2
        )
        val entity = HttpEntity(body, headers)

        val path = ApiPaths.resolvePath(URL+REVIEW+POST_REVIEW, mapOf("port" to "$port", "hotelId" to "${hotel.id}"))
        val createReview = createPOST(path, entity)

        assertEquals(HttpStatus.OK, createReview.statusCode)

        val actualReview = objectMapper.readValue(createReview.body, Review::class.java)

        assertNotNull(actualReview)

        val deleteReview = createDELETE(path, entity)

        assertEquals(HttpStatus.OK, deleteReview.statusCode)

        val deletedSuccessfully = objectMapper.readValue(deleteReview.body, Boolean::class.java)

        assertTrue(deletedSuccessfully)
    }

    @Test
    fun `should return NotFound when trying to delete Review`() {
        val headers = createHeaders(COMMENTER)
        val hotel = HOTELS.random()
        val body = ReviewInputModel(
            content = "Good hotel",
            rating = 2
        )
        val entity = HttpEntity(body, headers)

        val path = ApiPaths.resolvePath(URL+REVIEW+POST_REVIEW, mapOf("port" to "$port", "hotelId" to "${hotel.id}"))
        val createReview = createPOST(path, entity)

        assertEquals(HttpStatus.OK, createReview.statusCode)

        val postedReview = objectMapper.readValue(createReview.body, Review::class.java)

        assertNotNull(postedReview)

        val deletePath = ApiPaths.resolvePath(URL+REVIEW+DELETE_REVIEW, mapOf("port" to "$port", "hotelId" to "999"))
        val deleteReview = createDELETE(deletePath, entity)

        assertEquals(HttpStatus.NOT_FOUND, deleteReview.statusCode)

        val deletedResponse = objectMapper.readValue(deleteReview.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, deletedResponse.status)
    }
}