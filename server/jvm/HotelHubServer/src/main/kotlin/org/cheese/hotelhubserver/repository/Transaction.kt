package org.cheese.hotelhubserver.repository

interface Transaction {
    val userRepository: UserRepository
    val hotelRepository: HotelRepository
    val critiqueRepository: CritiqueRepository

    // other repository types
    fun rollback()
}
