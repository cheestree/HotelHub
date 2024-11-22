package com.cheese.hotelhub.domain.model.input

import jakarta.validation.constraints.NotBlank

data class ReviewInputModel(
    @field:NotBlank(message = "Review content cannot be empty")
    val content: String,

    @field:NotBlank(message = "Rating can't be empty")
    val rating: Int
)