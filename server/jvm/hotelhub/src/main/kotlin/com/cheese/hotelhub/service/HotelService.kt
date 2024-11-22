package com.cheese.hotelhub.service

import com.cheese.hotelhub.domain.exception.HotelHubException.ResourceNotFoundException
import com.cheese.hotelhub.domain.hotel.Hotel
import com.cheese.hotelhub.repository.hotel.HotelRepository
import com.cheese.hotelhub.repository.review.ReviewRepository
import com.cheese.hotelhub.repository.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class HotelService(
    private val reviewRepository: ReviewRepository,
    private val hotelRepository: HotelRepository,
    private val userRepository: UserRepository
) {
    fun getHotel(hotelId: Long): Hotel {
        return hotelRepository.getHotelById(hotelId).orElseThrow {
            ResourceNotFoundException("Hotel $hotelId not found")
        }
    }

    fun createHotel(name: String, description: String, lat: Long, lng: Long, ownerId: Long?): Hotel {
        //  Sanitize everything

        val owner = userRepository.findByIdOrNull(ownerId)

        val hotel = Hotel(name = name, description = description, rating = 0.0, lat = lat, lng = lng, owner = owner)

        return hotelRepository.save(hotel)
    }

    fun deleteHotel(hotelId: Long): Boolean {
        hotelRepository.getHotelById(hotelId).orElseThrow {
            ResourceNotFoundException("Hotel $hotelId not found")
        }

        hotelRepository.deleteById(hotelId)

        return true
    }
}