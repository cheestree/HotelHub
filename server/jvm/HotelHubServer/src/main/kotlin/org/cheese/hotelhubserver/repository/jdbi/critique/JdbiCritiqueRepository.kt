package org.cheese.hotelhubserver.repository.jdbi.critique

import kotlinx.datetime.*
import org.cheese.hotelhubserver.domain.critique.Critique
import org.cheese.hotelhubserver.repository.interfaces.critique.CritiqueRepository
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo

class JdbiCritiqueRepository(
    private val handle: Handle,
): CritiqueRepository {
    override fun createCritique(user: Int, hotel: Int, time: Instant, stars: Int, description: String): Int {
        val formattedTime = time.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
        return handle.createUpdate(
            """
                insert into hotelhub.critique(user_id, hotel_id, created_at, edited_at, stars, description) VALUES (:user, :hotel, :time, NULL, :stars, :description)
            """
        )
            .bind("user", user)
            .bind("hotel", hotel)
            .bind("time", formattedTime)
            .bind("stars", stars)
            .bind("description", description)
            .execute()
    }

    override fun deleteCritique(user: Int, hotel: Int): Boolean {
        return handle.createUpdate(
            """
                delete from hotelhub.critique where user_id = :user and hotel_id = :hotel
            """
        )
            .bind("user", user)
            .bind("hote", hotel)
            .executeAndReturnGeneratedKeys()
            .mapTo<Critique>()
            .singleOrNull() != null
    }

    override fun editCritique(critiqueId: Int, time: Instant, stars: Int, description: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCritiqueById(critiqueId: Int): Critique {
        return handle.createQuery(
            """
                select * from hotelhub.critique where id = :critiqueId
            """
        )
            .bind("critiqueId", critiqueId)
            .mapTo<Critique>()
            .single()
    }

    override fun getCritiques(hotel: Int, offset: Int, limit: Int): List<Critique> {
        return handle.createQuery(
            """
                select * from hotelhub.critique where hotel_id = :hotel
                offset :offset
                limit :limit
            """
        )
            .bind("hotel", hotel)
            .bind("offset", offset)
            .bind("limit", limit)
            .mapTo<Critique>()
            .list()
    }

    override fun critiqueExists(critiqueId: Int): Boolean {
        return handle.createQuery(
            """
                select * from hotelhub.critique where id = :critiqueId
            """
        )
            .bind("critiqueId", critiqueId)
            .mapTo<Critique>()
            .singleOrNull() != null
    }

    override fun critiqueExistsByUser(user: Int, hotel: Int): Boolean {
        return handle.createQuery(
            """
                select * from hotelhub.critique where user_id = :user and hotel_id = :hotel
            """
        )
            .bind("user", user)
            .bind("hotel", hotel)
            .mapTo<Critique>()
            .singleOrNull() != null
    }
}