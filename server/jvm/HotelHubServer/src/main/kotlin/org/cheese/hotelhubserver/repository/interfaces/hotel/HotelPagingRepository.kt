package org.cheese.hotelhubserver.repository.interfaces.hotel

import org.cheese.hotelhubserver.domain.hotel.Hotel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface HotelPagingRepository : PagingAndSortingRepository<Int, Hotel> {
    override fun findAll(pageable: Pageable): Page<Int> {
        TODO("Not yet implemented")
    }
}