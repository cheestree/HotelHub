package com.cheese.hotelhub.service

import com.cheese.hotelhub.domain.exception.HotelHubException.UnauthorizedAccessException
import com.cheese.hotelhub.domain.exception.HotelHubException.ResourceNotFoundException
import com.cheese.hotelhub.domain.review.Review
import com.cheese.hotelhub.domain.review.ReviewKey
import com.cheese.hotelhub.repository.hotel.HotelRepository
import com.cheese.hotelhub.repository.review.ReviewRepository
import com.cheese.hotelhub.repository.user.UserRepository
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val hotelRepository: HotelRepository
) {
    fun createReview(hotelId: Long, userId: Long, content: String, rating: Int): Review {
        val user = userRepository.findById(userId).orElseThrow {
            ResourceNotFoundException("User with ID $userId not found")
        }

        val hotel = hotelRepository.findById(hotelId).orElseThrow {
            ResourceNotFoundException("Hotel with ID $hotelId not found")
        }

        val reviewKey = ReviewKey(userId = user.id, hotelId = hotel.id)
        val review = Review(id = reviewKey, text = content, rating = rating)

        return reviewRepository.save(review)
    }

    fun deleteReview(hotelId: Long, userId: Long): Boolean {
        hotelRepository.findById(hotelId).orElseThrow {
            ResourceNotFoundException("Hotel with ID $hotelId not found")
        }

        val review = reviewRepository.findById(ReviewKey(userId, hotelId)).orElseThrow {
            throw ResourceNotFoundException("Review not found for user $userId and hotel $hotelId")
        }

        if(review.id.userId != userId) {
            throw UnauthorizedAccessException("User $userId is not the author of this review")
        }

        reviewRepository.deleteById(ReviewKey(userId, hotelId))

        return true
    }

    fun getReview(hotelId: Long, userId: Long): Review {
        val reviewKey = ReviewKey(userId, hotelId)

        userRepository.findById(userId).orElseThrow {
            ResourceNotFoundException("User with ID $userId not found")
        }

        hotelRepository.findById(hotelId).orElseThrow {
            ResourceNotFoundException("Hotel $hotelId not found")
        }

        return reviewRepository.getReviewById(reviewKey).orElseThrow {
            ResourceNotFoundException("Review not found")
        }
    }

    fun getReviews(hotelId: Long): List<Review> {
        hotelRepository.findById(hotelId).orElseThrow {
            ResourceNotFoundException("Hotel $hotelId not found")
        }

        return reviewRepository.findByIdHotelId(hotelId)
    }
}