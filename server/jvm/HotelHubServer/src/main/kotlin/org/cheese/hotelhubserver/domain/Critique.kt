package org.cheese.hotelhubserver.domain

import java.sql.Timestamp

data class Critique(
    val id: Int,
    val userId: Int,
    val hotelId: Int,
    val edited: Boolean,
    val createdAt: Timestamp,
    val editedAt: Timestamp?,
    val stars: Int,
    val description: String
)
