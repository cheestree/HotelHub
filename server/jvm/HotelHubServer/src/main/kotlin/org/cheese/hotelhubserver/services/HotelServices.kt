package org.cheese.hotelhubserver.services

import kotlinx.datetime.Clock
import org.cheese.hotelhubserver.domain.Hotel
import org.cheese.hotelhubserver.domain.user.UserDomain
import org.cheese.hotelhubserver.repository.TransactionManager
import org.cheese.hotelhubserver.util.Either
import org.cheese.hotelhubserver.util.executeAndHandleException
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
    private val transactionManager: TransactionManager,
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
        var success = false
        transactionManager.run {
            success = it.hotelRepository.createHotel(name, address, stars, latitude, longitude)
        }
        return success(success)
    }

    fun getHotel(
        id: Int
    ): HotelFetchResult {
        var hotel: Hotel? = null
        transactionManager.run {
            hotel = it.hotelRepository.getHotel(id)
        }
        return if(hotel != null) success(hotel) else failure(HotelFetchError.HotelDoesntExist)
    }
}
