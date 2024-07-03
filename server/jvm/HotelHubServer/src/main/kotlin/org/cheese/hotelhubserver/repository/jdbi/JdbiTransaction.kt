package org.cheese.hotelhubserver.repository.jdbi

import org.cheese.hotelhubserver.repository.interfaces.Transaction
import org.cheese.hotelhubserver.repository.interfaces.critique.CritiqueRepository
import org.cheese.hotelhubserver.repository.interfaces.hotel.HotelRepository
import org.cheese.hotelhubserver.repository.interfaces.user.UserRepository
import org.cheese.hotelhubserver.repository.jdbi.critique.JdbiCritiqueRepository
import org.cheese.hotelhubserver.repository.jdbi.hotel.JdbiHotelRepository
import org.cheese.hotelhubserver.repository.jdbi.user.JdbiUserRepository
import org.jdbi.v3.core.Handle

class JdbiTransaction(
    private val handle: Handle,
) : Transaction {
    override val userRepository: UserRepository = JdbiUserRepository(handle)
    override val hotelRepository: HotelRepository = JdbiHotelRepository(handle)
    override val critiqueRepository: CritiqueRepository = JdbiCritiqueRepository(handle)

    override fun rollback() {
        handle.rollback()
    }
}
