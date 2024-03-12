package org.cheese.hotelhubserver.repository

interface Transaction {
    val userRepository: UserRepository
    val hotelRepository: HotelRepository

    // other repository types
    fun rollback()
}
