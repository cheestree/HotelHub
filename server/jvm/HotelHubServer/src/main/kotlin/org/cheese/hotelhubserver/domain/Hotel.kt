package org.cheese.hotelhubserver.domain

data class Hotel(
    val name: String,
    val address: String,
    val stars: Int,
    val latitude: Double,
    val longitude: Double,
)
