package com.cheese.hotelhub.repository.review

import com.cheese.hotelhub.domain.review.Review
import com.cheese.hotelhub.domain.review.ReviewKey
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReviewRepository : JpaRepository<Review, ReviewKey> {
    @Transactional
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.id.hotelId = :hotelId")
    fun findAverageRatingByHotelId(hotelId: Long): Double

    fun findByIdHotelId(hotelId: Long): List<Review>

    fun getReviewById(review: ReviewKey): Optional<Review>
}