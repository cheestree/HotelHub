package org.cheese.hotelhubserver.http.model.hotel

data class HotelOutputModel(
    val id: Int,
    val name: String,
    val address: String,
    val stars: Int,
    val latitude: Double,
    val longitude: Double,
)
