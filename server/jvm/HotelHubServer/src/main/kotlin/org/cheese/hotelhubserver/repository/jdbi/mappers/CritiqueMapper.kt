package org.cheese.hotelhubserver.repository.jdbi.mappers

import org.cheese.hotelhubserver.domain.Critique
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
            user_id = r.getInt("user_id"),
            hotel_id = r.getInt("hotel_id"),
            edited = r.getBoolean("edited"),
            created_at = truncateSeconds(r.getTimestamp("created_at")),
            edited_at = truncateSeconds(r.getTimestamp("edited_at")),
            stars = r.getInt("stars"),
            description = r.getString("description")
        )
    }

    private fun truncateSeconds(timestamp: Timestamp): Timestamp {
        val time = timestamp.time / 1000 * 1000
        return Timestamp(time)
    }
}