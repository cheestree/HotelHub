package com.cheese.hotelhub.controller

import com.cheese.hotelhub.domain.annotation.ProtectedRoute
import com.cheese.hotelhub.domain.model.input.ReviewInputModel
import com.cheese.hotelhub.domain.path.ApiPaths.API_EXTENSION
import com.cheese.hotelhub.domain.path.ApiPaths.REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.DELETE_REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.GET_REVIEW
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.GET_REVIEWS
import com.cheese.hotelhub.domain.path.ApiPaths.Reviews.POST_REVIEW
import com.cheese.hotelhub.domain.review.Review
import com.cheese.hotelhub.domain.user.AuthenticatedUser
import com.cheese.hotelhub.service.ReviewService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_EXTENSION+REVIEW)
class ReviewController(
    private val reviewService: ReviewService,
) {
    @ProtectedRoute
    @PostMapping(POST_REVIEW)
    fun postReview(
        @RequestAttribute("authenticatedUser") user: AuthenticatedUser,
        @PathVariable hotelId: Long,
        @RequestBody review: ReviewInputModel
    ): Review {
        return reviewService.createReview(hotelId, user.id, review.content, review.rating)
    }

    @ProtectedRoute
    @DeleteMapping(DELETE_REVIEW)
    fun deleteReview(
        @RequestAttribute("authenticatedUser") user: AuthenticatedUser,
        @PathVariable hotelId: Long,
    ): Boolean {
        return reviewService.deleteReview(hotelId, user.id)
    }

    @GetMapping(GET_REVIEW)
    fun getReview(
        @PathVariable hotelId: Long,
        @PathVariable reviewId: Long
    ): Review {
        return reviewService.getReview(hotelId, reviewId)
    }

    @GetMapping(GET_REVIEWS)
    fun getReviews(
        @PathVariable hotelId: Long,
    ): List<Review> {
        return reviewService.getReviews(hotelId)
    }
}