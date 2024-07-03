package org.cheese.hotelhubserver.services

import kotlinx.datetime.Clock
import org.cheese.hotelhubserver.domain.exceptions.HotelExceptions.HotelDoesntExist
import org.cheese.hotelhubserver.domain.hotel.Hotel
import org.cheese.hotelhubserver.domain.hotel.HotelDomain
import org.cheese.hotelhubserver.http.model.hotel.HotelOutputModel
import org.cheese.hotelhubserver.repository.interfaces.TransactionManager
import org.cheese.hotelhubserver.util.requireOrThrow
import org.springframework.stereotype.Component

@Component
class HotelServices(
    private val tm: TransactionManager,
    private val domain: HotelDomain,
    private val clock: Clock,
) {
    fun createHotel(
        name: String,
        address: String,
        stars: Int,
        latitude: Double,
        longitude: Double,
    ): Boolean =
        tm.run {
            //  TODO: Add parameter checking via domain here
            it.hotelRepository.createHotel(name, address, stars, latitude, longitude)
        }

    fun getHotel(id: Int): Hotel =
        tm.run {
            requireOrThrow<HotelDoesntExist>(it.hotelRepository.hotelExists(id)) { "Hotel doesn't exist" }
            it.hotelRepository.getHotel(id)
        }

    fun getHotels(
        stars: Int?,
        features: List<String>?,
    ): List<HotelOutputModel> =
        tm.run {
            //  TODO: Add parameter checking via domain here
            it.hotelRepository.getHotels(stars, features)
        }

    fun getFeatures(): List<String> =
        tm.run {
            it.hotelRepository.getFeatures()
        }
}
