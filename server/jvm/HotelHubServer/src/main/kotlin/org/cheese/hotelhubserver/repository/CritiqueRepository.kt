package org.cheese.hotelhubserver.repository

import kotlinx.datetime.Instant
import org.cheese.hotelhubserver.domain.Critique

interface CritiqueRepository {
    fun createCritique(user: Int, hotel: Int, time: Instant, stars: Int, description: String): Int

    fun deleteCritique(user: Int, hotel: Int): Boolean

    fun editCritique(critiqueId: Int, time: Instant, stars: Int, description: String): Boolean

    fun getCritique(critiqueId: Int): Critique

    fun getCritiques(hotel: Int): List<Critique>

    fun critiqueExists(critiqueId: Int): Boolean

    fun critiqueExistsByUser(user: Int, hotel: Int): Boolean
}