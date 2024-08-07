package org.cheese.hotelhubserver.domain.hotel

data class Hotel(
    val id: Int,
    val name: String,
    val address: String,
    val stars: Int,
    val latitude: Double,
    val longitude: Double,
)
