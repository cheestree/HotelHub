package com.cheese.hotelhub.integration

import com.cheese.hotelhub.domain.error.Error
import com.cheese.hotelhub.domain.hotel.Hotel
import com.cheese.hotelhub.domain.model.input.HotelInputModel
import com.cheese.hotelhub.integration.base.BaseTest
import com.cheese.hotelhub.integration.base.Config
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(Config::class)
class HotelTest : BaseTest() {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return NotFound hotel not found`() {
        val response = restTemplate.getForEntity("http://localhost:$port/hotels/999", String::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }

    @Test
    fun `should return OK hotel found`() {
        val response = restTemplate.getForEntity("http://localhost:$port/hotels/1", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Hotel::class.java)

        assertNotNull(actualUserResponse)
    }


    @Test
    fun `should return OK when hotel is created`() {
        val headers = createHeaders(ADMIN)
        val body = HotelInputModel(
            name = "Hotel Transylvania",
            description = "Funny",
            lat = 101L,
            lng = 102L,
            owner = OWNER.id
        )
        val entity = HttpEntity(body, headers)

        val response = restTemplate.exchange(
            "http://localhost:$port/hotels",
            HttpMethod.POST,
            entity,
            String::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)

        val actualHotel = objectMapper.readValue(response.body, Hotel::class.java)

        assertNotNull(actualHotel)
    }

    @Test
    fun `should return OK when hotel is deleted`() {
        val headers = createHeaders(ADMIN)
        val body = HotelInputModel(
            name = "Hotel Transylvania",
            description = "Funny",
            lat = 101L,
            lng = 102L,
            owner = OWNER.id
        )
        val entity = HttpEntity(body, headers)

        val createResponse = restTemplate.exchange(
            "http://localhost:$port/hotels",
            HttpMethod.POST,
            entity,
            String::class.java
        )

        assertEquals(HttpStatus.OK, createResponse.statusCode)

        val actualHotel = objectMapper.readValue(createResponse.body, Hotel::class.java)

        assertNotNull(actualHotel)

        val deleteResponse = restTemplate.exchange(
            "http://localhost:$port/hotels/"+actualHotel.id,
            HttpMethod.DELETE,
            entity,
            String::class.java
        )

        assertEquals(HttpStatus.OK, createResponse.statusCode)

        val deletedHotel = objectMapper.readValue(deleteResponse.body, Boolean::class.java)

        assertTrue(deletedHotel)
    }
}
