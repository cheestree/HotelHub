package com.cheese.hotelhub.integration

import com.cheese.hotelhub.domain.error.Error
import com.cheese.hotelhub.domain.hotel.Hotel
import com.cheese.hotelhub.domain.model.input.HotelInputModel
import com.cheese.hotelhub.domain.path.ApiPaths
import com.cheese.hotelhub.domain.path.ApiPaths.HOTEL
import com.cheese.hotelhub.domain.path.ApiPaths.Hotel.DELETE_HOTEL
import com.cheese.hotelhub.domain.path.ApiPaths.Hotel.GET_HOTEL
import com.cheese.hotelhub.domain.path.ApiPaths.Hotel.POST_HOTEL
import com.cheese.hotelhub.domain.path.ApiPaths.REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.GET_REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.URL
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

    @Test
    fun `should return NotFound hotel not found`() {
        val getHotelPath = ApiPaths.resolvePath(URL + HOTEL + GET_HOTEL, mapOf("port" to "$port", "hotelId" to "999"))
        val response = createGET(getHotelPath)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val actualUserResponse = objectMapper.readValue(response.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
    }

    @Test
    fun `should return OK hotel found`() {
        val getHotelPath = ApiPaths.resolvePath(URL + HOTEL + GET_HOTEL, mapOf("port" to "$port", "hotelId" to "1"))
        val response = createGET(getHotelPath)

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

        val createHotelPath = ApiPaths.resolvePath(URL + HOTEL + POST_HOTEL, mapOf("port" to "$port"))
        val response = createPOST(createHotelPath, entity)

        assertEquals(HttpStatus.OK, response.statusCode)

        val actualHotel = objectMapper.readValue(response.body, Hotel::class.java)

        assertNotNull(actualHotel)
    }

    @Test
    fun `should return NotFound when trying to delete hotel`() {
        val headers = createHeaders(ADMIN)
        val entity = HttpEntity("", headers)

        val deleteHotelPath = ApiPaths.resolvePath(URL + HOTEL + DELETE_HOTEL, mapOf("port" to "$port", "hotelId" to "999"))
        val deleteResponse = createDELETE(deleteHotelPath, entity)

        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.statusCode)

        val actualUserResponse = objectMapper.readValue(deleteResponse.body, Error::class.java)

        assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.status)
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

        val createHotelPath = ApiPaths.resolvePath(URL + HOTEL + POST_HOTEL, mapOf("port" to "$port"))
        val createResponse = createPOST(createHotelPath, entity)

        assertEquals(HttpStatus.OK, createResponse.statusCode)

        val actualHotel = objectMapper.readValue(createResponse.body, Hotel::class.java)

        assertNotNull(actualHotel)

        val deleteHotelPath = ApiPaths.resolvePath(URL + HOTEL + DELETE_HOTEL, mapOf("port" to "$port", "hotelId" to "${actualHotel.id}"))
        val deleteResponse = createDELETE(deleteHotelPath, entity)

        assertEquals(HttpStatus.OK, createResponse.statusCode)

        val deletedHotel = objectMapper.readValue(deleteResponse.body, Boolean::class.java)

        assertTrue(deletedHotel)
    }
}
