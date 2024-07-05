package org.cheese.hotelhubserver.repository.interfaces

import org.cheese.hotelhubserver.repository.interfaces.critique.CritiqueRepository
import org.cheese.hotelhubserver.repository.interfaces.hotel.HotelRepository
import org.cheese.hotelhubserver.repository.interfaces.user.UserRepository

interface Transaction {
    val userRepository: UserRepository
    val hotelRepository: HotelRepository
    val critiqueRepository: CritiqueRepository

    fun commit()

    fun rollback()
}
