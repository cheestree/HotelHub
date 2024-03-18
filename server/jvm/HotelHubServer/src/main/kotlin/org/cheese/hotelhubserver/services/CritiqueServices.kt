package org.cheese.hotelhubserver.services

import kotlinx.datetime.Clock
import org.cheese.hotelhubserver.domain.exceptions.CritiqueExceptions.CritiqueAlreadyExists
import org.cheese.hotelhubserver.domain.exceptions.CritiqueExceptions.HotelDoesntExist
import org.cheese.hotelhubserver.domain.exceptions.CritiqueExceptions.CritiqueDoesntExist
import org.cheese.hotelhubserver.domain.user.UserDomain
import org.cheese.hotelhubserver.repository.TransactionManager
import org.cheese.hotelhubserver.util.requireOrThrow
import org.springframework.stereotype.Component

@Component
class CritiqueServices(
    private val tm: TransactionManager,
    private val domain: UserDomain,
    private val clock: Clock,
) {
    fun createCritique(
        user: Int,
        hotel: Int,
        stars: Int,
        description: String
    ): Boolean = tm.run {
        requireOrThrow<HotelDoesntExist>(it.hotelRepository.hotelExists(hotel)) { "Hotel doesn't exist" }
        requireOrThrow<CritiqueAlreadyExists>(it.critiqueRepository.critiqueExists(user, hotel)) { "A critique already exists" }
        it.critiqueRepository.createCritique(user, hotel, stars, description)
    }

    fun deleteCritique(
        user: Int,
        hotel: Int
    ): Boolean = tm.run {
        requireOrThrow<CritiqueDoesntExist>(it.critiqueRepository.critiqueExists(user, hotel)) { "Critique doesn't exist" }
        it.critiqueRepository.deleteCritique(user, hotel)
    }

    fun editCritique(
        user: Int,
        hotel: Int,
        critiqueId: Int,
        stars: Int,
        description: String
    ): Boolean = tm.run {
        requireOrThrow<CritiqueDoesntExist>(it.critiqueRepository.critiqueExists(user, hotel)) { "Critique doesn't exist" }
        it.critiqueRepository.editCritique(critiqueId, stars, description)
    }
}