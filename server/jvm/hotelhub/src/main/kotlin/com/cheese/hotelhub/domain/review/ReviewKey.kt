package com.cheese.hotelhub.domain.review

import jakarta.persistence.Embeddable

@Embeddable
data class ReviewKey(
    val userId: Long,
    val hotelId: Long
)