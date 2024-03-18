package org.cheese.hotelhubserver.repository.jdbi

import org.cheese.hotelhubserver.domain.Critique
import org.cheese.hotelhubserver.repository.CritiqueRepository
import org.jdbi.v3.core.Handle

class JdbiCritiqueRepository(
    private val handle: Handle,
): CritiqueRepository {
    override fun createCritique(user: Int, hotel: Int, stars: Int, description: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteCritique(user: Int, hotel: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun editCritique(critiqueId: Int, stars: Int, description: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCritique(user: Int, hotel: Int): Critique {
        TODO("Not yet implemented")
    }

    override fun getCritiques(hotel: Int): List<Critique> {
        TODO("Not yet implemented")
    }

    override fun critiqueExists(user: Int, hotel: Int): Boolean {
        TODO("Not yet implemented")
    }
}