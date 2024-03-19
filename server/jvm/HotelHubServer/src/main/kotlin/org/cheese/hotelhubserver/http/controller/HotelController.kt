package org.cheese.hotelhubserver.http.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.cheese.hotelhubserver.domain.Feature
import org.cheese.hotelhubserver.http.Uris
import org.cheese.hotelhubserver.http.model.hotel.HotelCreateInputModel
import org.cheese.hotelhubserver.services.HotelServices
import org.springframework.http.HttpStatus.CREATED
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
        return ResponseEntity.status(CREATED).body(res)
    }

    @GetMapping(Uris.Hotel.GETHOTELS)
    fun getHotels(
        @Valid @RequestParam stars: Int?,
        @Valid @RequestParam features: List<Feature>?,
    ): ResponseEntity<*> {
        val res = hotelServices.getHotels(stars, features)
        return ResponseEntity.ok().body(res)
    }

    @GetMapping(Uris.Hotel.GETHOTEL)
    fun getHotel(
        @Valid @Min(value = 1, message = "Must be at least 1") @PathVariable hotelId: Int
    ): ResponseEntity<*> {
        val res = hotelServices.getHotel(hotelId)
        return ResponseEntity.ok().body(res)
    }
}
