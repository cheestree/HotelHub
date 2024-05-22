package org.cheese.hotelhubserver.repository.interfaces.critique

import kotlinx.datetime.Instant
import org.cheese.hotelhubserver.domain.critique.Critique

const val PAGE_LIMIT = 50

interface CritiqueRepository {
    fun createCritique(user: Int, hotel: Int, time: Instant, stars: Int, description: String): Int

    fun deleteCritique(user: Int, hotel: Int): Boolean

    fun editCritique(critiqueId: Int, time: Instant, stars: Int, description: String): Boolean

    fun getCritiqueById(critiqueId: Int): Critique

    fun getCritiques(hotel: Int, offset: Int = 0, limit: Int = PAGE_LIMIT): List<Critique>

    fun critiqueExists(critiqueId: Int): Boolean

    fun critiqueExistsByUser(user: Int, hotel: Int): Boolean
}