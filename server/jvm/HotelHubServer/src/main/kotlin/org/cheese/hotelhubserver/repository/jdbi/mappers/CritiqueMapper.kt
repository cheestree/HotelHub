package org.cheese.hotelhubserver.repository.jdbi.mappers

import org.cheese.hotelhubserver.domain.critique.Critique
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp

class CritiqueMapper : ColumnMapper<Critique> {
    @Throws(SQLException::class)
    override fun map(
        r: ResultSet,
        columnNumber: Int,
        ctx: StatementContext
    ): Critique {
        return Critique(
            id = r.getInt("id"),
            userId = r.getInt("user_id"),
            hotelId = r.getInt("hotel_id"),
            edited = r.getBoolean("edited"),
            createdAt = truncateSeconds(r.getTimestamp("created_at")),
            editedAt = truncateSeconds(r.getTimestamp("edited_at")),
            stars = r.getInt("stars"),
            description = r.getString("description")
        )
    }

    private fun truncateSeconds(timestamp: Timestamp): Timestamp {
        val time = timestamp.time / 1000 * 1000
        return Timestamp(time)
    }
}