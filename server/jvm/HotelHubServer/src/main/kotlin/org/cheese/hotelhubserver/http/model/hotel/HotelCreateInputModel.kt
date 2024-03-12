package org.cheese.hotelhubserver.http.model.hotel

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class HotelCreateInputModel(
    @field:NotBlank(message = "Must not be empty")
    @field:Size(min = 4, max = 32, message = "Must be between 4-32 characters")
    val name: String,
    @field:NotBlank(message = "Must not be empty")
    @field:Size(max = 64, message = "Must be between 1-32 characters")
    val address: String,
    @field:Min(1, message = "Must be between 1-5")
    @field:Max(5, message = "Must be between 1-5")
    val stars: Int,
    @field:Min(value = -90, message = "Must be between -90 to 90")
    @field:Max(value = 90, message = "Must be between -90 to 90")
    val latitude: Double,
    @field:Min(value = -180, message = "Must be between -180 to 180")
    @field:Max(value = 180, message = "Must be between -180 to 180")
    val longitude: Double,
)
