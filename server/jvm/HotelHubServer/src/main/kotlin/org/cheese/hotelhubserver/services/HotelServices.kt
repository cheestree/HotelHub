package org.cheese.hotelhubserver.services

import kotlinx.datetime.Clock
import org.cheese.hotelhubserver.domain.Feature
import org.cheese.hotelhubserver.domain.Hotel
import org.cheese.hotelhubserver.domain.user.UserDomain
import org.cheese.hotelhubserver.repository.TransactionManager
import org.cheese.hotelhubserver.util.Either
import org.cheese.hotelhubserver.util.failure
import org.cheese.hotelhubserver.util.success
import org.springframework.stereotype.Component

sealed class HotelFetchError {
    object HotelDoesntExist : HotelFetchError()
}
typealias HotelFetchResult = Either<HotelFetchError, Hotel?>

sealed class HotelCreationError {
    object HotelAlreadyExists : HotelCreationError()
}
typealias HotelCreationResult = Either<HotelCreationError, Boolean>

@Component
class HotelServices(
    private val tm: TransactionManager,
    private val hotelDomain: UserDomain,
    private val clock: Clock,
) {
    fun createHotel(
        name: String,
        address: String,
        stars: Int,
        latitude: Double,
        longitude: Double,
    ): HotelCreationResult {
        return tm.run { success(it.hotelRepository.createHotel(name, address, stars, latitude, longitude)) }
    }

    fun getHotel(
        id: Int
    ): HotelFetchResult {
        val hotel = tm.run { it.hotelRepository.getHotel(id) }
        if(hotel == null) failure(HotelFetchError.HotelDoesntExist)
        return success(hotel)
    }

    fun getHotels(
        stars: Int?,
        features: List<Feature>?
    ): List<Hotel> {
        return tm.run {
            it.hotelRepository.getHotels(stars, features)
        }
    }
}
