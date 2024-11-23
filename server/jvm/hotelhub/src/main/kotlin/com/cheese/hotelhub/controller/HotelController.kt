package com.cheese.hotelhub.controller

import com.cheese.hotelhub.domain.annotation.ProtectedRoute
import com.cheese.hotelhub.domain.annotation.RoleRoute
import com.cheese.hotelhub.domain.enums.Role
import com.cheese.hotelhub.domain.hotel.Hotel
import com.cheese.hotelhub.domain.model.input.HotelInputModel
import com.cheese.hotelhub.domain.path.ApiPaths.API_EXTENSION
import com.cheese.hotelhub.domain.path.ApiPaths.HOTEL
import com.cheese.hotelhub.domain.path.ApiPaths.Hotel.DELETE_HOTEL
import com.cheese.hotelhub.domain.path.ApiPaths.Hotel.GET_HOTEL
import com.cheese.hotelhub.domain.path.ApiPaths.Hotel.POST_HOTEL
import com.cheese.hotelhub.domain.user.AuthenticatedUser
import com.cheese.hotelhub.service.HotelService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_EXTENSION+HOTEL)
class HotelController(
    private val hotelService: HotelService
) {
    @GetMapping(GET_HOTEL)
    fun getHotel(
        @PathVariable hotelId: Long
    ): Hotel{
        return hotelService.getHotel(hotelId)
    }

    @ProtectedRoute
    @RoleRoute(Role.ADMIN)
    @PostMapping(POST_HOTEL)
    fun createHotel(
        @RequestAttribute("authenticatedUser") user: AuthenticatedUser,
        @RequestBody hotel: HotelInputModel
    ): Hotel {
        return hotelService.createHotel(hotel.name, hotel.description, hotel.lat, hotel.lng, hotel.owner)
    }

    @ProtectedRoute
    @RoleRoute(Role.ADMIN)
    @DeleteMapping(DELETE_HOTEL)
    fun deleteHotel(
        @RequestAttribute("authenticatedUser") user: AuthenticatedUser,
        @PathVariable hotelId: Long
    ): Boolean {
        return hotelService.deleteHotel(hotelId)
    }
}