package org.cheese.hotelhubserver.domain

import java.sql.Timestamp

data class Critique(
    val id: Int,
    val user_id: Int,
    val hotel_id: Int,
    val edited: Boolean,
    val created_at: Timestamp,
    val edited_at: Timestamp?,
    val stars: Int,
    val description: String
)
