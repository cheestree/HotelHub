package org.cheese.hotelhubserver.repository

import org.cheese.hotelhubserver.domain.Critique

interface CritiqueRepository {
    fun createCritique(user: Int, hotel: Int, stars: Int, description: String): Boolean

    fun deleteCritique(user: Int, hotel: Int): Boolean

    fun editCritique(critiqueId: Int, stars: Int, description: String): Boolean

    fun getCritique(user: Int, hotel: Int): Critique

    fun getCritiques(hotel: Int): List<Critique>

    fun critiqueExists(user: Int, hotel: Int): Boolean
}