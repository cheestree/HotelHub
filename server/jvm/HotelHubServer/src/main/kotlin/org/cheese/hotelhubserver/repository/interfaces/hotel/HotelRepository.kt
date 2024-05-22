package org.cheese.hotelhubserver.repository.interfaces.hotel

import org.cheese.hotelhubserver.domain.hotel.Hotel
import org.cheese.hotelhubserver.http.model.hotel.HotelOutputModel

interface HotelRepository {
    fun createHotel(
        name: String,
        address: String,
        stars: Int,
        latitude: Double,
        longitude: Double,
        features: List<Int>? = null
    ): Boolean
    fun getHotel(id: Int): Hotel
    fun getHotels(stars: Int? = null, features: List<String>? = null): List<HotelOutputModel>
    fun hotelExists(id: Int): Boolean
    fun getFeatures(): List<String>
}