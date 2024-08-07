package org.cheese.hotelhubserver.http.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.cheese.hotelhubserver.domain.user.AuthenticatedUser
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
        user: AuthenticatedUser
        ): ResponseEntity<*> {
        val res = hotelServices.createHotel(input.name, input.address, input.stars, input.latitude, input.longitude, user.user.role)
        return ResponseEntity.status(CREATED).body(res)
    }

    @GetMapping(Uris.Hotel.GETHOTELS)
    fun getHotels(
        @Valid @RequestParam location: String?,
        @Valid @RequestParam stars: Int?,
        @Valid @RequestParam features: List<String>?,
    ): ResponseEntity<*> {
        val res = hotelServices.getHotels(stars, features)
        return ResponseEntity.ok(res)
    }

    @GetMapping(Uris.Hotel.GETHOTEL)
    fun getHotel(
        @Valid @Min(value = 1, message = "Must be at least 1") @PathVariable hotelId: Int,
    ): ResponseEntity<*> {
        val res = hotelServices.getHotel(hotelId)
        return ResponseEntity.ok(res)
    }

    @GetMapping(Uris.Hotel.GETFEATURES)
    fun getFeatures(): ResponseEntity<*> {
        val res = hotelServices.getFeatures()
        return ResponseEntity.ok(res)
    }
}
