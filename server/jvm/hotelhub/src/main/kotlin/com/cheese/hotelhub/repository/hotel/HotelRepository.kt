package com.cheese.hotelhub.repository.hotel

import com.cheese.hotelhub.domain.hotel.Hotel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HotelRepository : JpaRepository<Hotel, Long> {
    fun getHotelById(id: Long): Optional<Hotel>
}
