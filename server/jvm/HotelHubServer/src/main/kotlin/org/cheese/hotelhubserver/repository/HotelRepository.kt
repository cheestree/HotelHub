package org.cheese.hotelhubserver.repository

import org.cheese.hotelhubserver.domain.Feature
import org.cheese.hotelhubserver.domain.Hotel

interface HotelRepository {
    fun createHotel(
        name: String,
        address: String,
        stars: Int,
        latitude: Double,
        longitude: Double): Boolean
    fun getHotel(id: Int): Hotel
    fun getHotels(stars: Int? = null, features: List<Feature>? = null): List<Hotel>
    fun hotelExists(id: Int): Boolean
}