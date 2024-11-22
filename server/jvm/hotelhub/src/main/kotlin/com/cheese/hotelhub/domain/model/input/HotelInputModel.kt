package com.cheese.hotelhub.domain.model.input

data class HotelInputModel(
    val name: String,
    val description: String,
    val lat: Long,
    val lng: Long,
    val owner: Long? = null
)
