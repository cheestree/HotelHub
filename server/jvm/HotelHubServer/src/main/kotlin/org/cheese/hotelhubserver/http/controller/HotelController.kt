package org.cheese.hotelhubserver.http.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.cheese.hotelhubserver.http.Uris
import org.cheese.hotelhubserver.http.model.hotel.HotelCreateInputModel
import org.cheese.hotelhubserver.http.model.user.Problem
import org.cheese.hotelhubserver.services.HotelCreationError
import org.cheese.hotelhubserver.services.HotelFetchError
import org.cheese.hotelhubserver.services.HotelServices
import org.cheese.hotelhubserver.util.Failure
import org.cheese.hotelhubserver.util.Success
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class HotelController(
    private val hotelServices: HotelServices,
) {
    @PostMapping(Uris.Hotel.CREATE)
    fun createHotel(
        @Valid @RequestBody input: HotelCreateInputModel,
    ): ResponseEntity<*> {
        val res = hotelServices.createHotel(input.name, input.address, input.stars, input.latitude, input.longitude)
        return when (res) {
            is Success -> ResponseEntity.status(201).body(res)
            is Failure ->
                when (res.value) {
                    HotelCreationError.HotelAlreadyExists ->
                        Problem.response(400, Problem.alreadyExists)
                }
        }
    }

    /*
    @GetMapping(Uris.Hotel.GETHOTELS)
    fun getHotels(): ResponseEntity<*> {

    }
    */

    @GetMapping(Uris.Hotel.GETHOTEL)
    fun getHotel(
        @Valid @NotEmpty @Min(value = 1, message = "Must be at least 1") @PathVariable id: Int
    ): ResponseEntity<*> {
        val res = hotelServices.getHotel(id)
        return when (res) {
            is Success -> ResponseEntity.status(201).body(res)
            is Failure ->
                when (res.value) {
                    HotelFetchError.HotelDoesntExist -> Problem.response(404, Problem.doesntExist)
                }
        }
    }
}
