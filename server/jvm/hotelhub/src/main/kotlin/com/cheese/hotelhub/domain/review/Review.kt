package com.cheese.hotelhub.domain.review

import jakarta.persistence.*

@Entity
@Table(name = "reviews")
data class Review(
    @EmbeddedId
    val id: ReviewKey,

    val text: String,
    val rating: Int,
)