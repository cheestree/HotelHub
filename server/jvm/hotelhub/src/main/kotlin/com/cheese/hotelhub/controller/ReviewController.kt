package com.cheese.hotelhub.controller

import com.cheese.hotelhub.domain.annotation.ProtectedRoute
import com.cheese.hotelhub.domain.model.input.ReviewInputModel
import com.cheese.hotelhub.domain.review.Review
import com.cheese.hotelhub.domain.user.AuthenticatedUser
import com.cheese.hotelhub.service.ReviewService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reviews")
class ReviewController(
    private val reviewService: ReviewService,
) {

    @ProtectedRoute
    @PostMapping("/{hotelId}")
    fun postReview(
        @RequestAttribute("authenticatedUser") user: AuthenticatedUser,
        @PathVariable hotelId: Long,
        @RequestBody review: ReviewInputModel
    ): Review {
        return reviewService.createReview(hotelId, user.id, review.content, review.rating)
    }

    @ProtectedRoute
    @DeleteMapping("/{hotelId}")
    fun deleteReview(
        @RequestAttribute("authenticatedUser") user: AuthenticatedUser,
        @PathVariable hotelId: Long,
    ): Boolean {
        return reviewService.deleteReview(hotelId, user.id)
    }

    @GetMapping("/{hotelId}/review/{reviewId}")
    fun getReview(
        @PathVariable hotelId: Long,
        @PathVariable reviewId: Long
    ): Review {
        return reviewService.getReview(hotelId, reviewId)
    }

    @GetMapping("/{hotelId}")
    fun getReviews(
        @PathVariable hotelId: Long,
    ): List<Review> {
        return reviewService.getReviews(hotelId)
    }
}