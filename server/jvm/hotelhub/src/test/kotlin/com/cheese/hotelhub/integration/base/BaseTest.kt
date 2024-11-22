package com.cheese.hotelhub.integration.base

import com.cheese.hotelhub.domain.enums.Role
import com.cheese.hotelhub.domain.hotel.Hotel
import com.cheese.hotelhub.domain.review.Review
import com.cheese.hotelhub.domain.review.ReviewKey
import com.cheese.hotelhub.domain.user.User
import com.cheese.hotelhub.repository.hotel.HotelRepository
import com.cheese.hotelhub.repository.review.ReviewRepository
import com.cheese.hotelhub.repository.user.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import java.util.*

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var hotelRepository: HotelRepository

    @BeforeAll
    fun setup() {
        USERS.forEach { userRepository.save(it) }
        HOTELS.forEach { hotelRepository.save(it) }
        REVIEWS.forEach { reviewRepository.save(it) }
        userRepository.save(COMMENTER)
        userRepository.save(OWNER)
        userRepository.save(ADMIN)
    }

    companion object {
        val USERS = listOf(
            User(id = 1L, oauthId = "1", name = "Test User 1", email = "testuser1@example.com", role = Role.MEMBER),
            User(id = 2L, oauthId = "2", name = "Test User 2", email = "testuser2@example.com", role = Role.MEMBER),
        )

        val COMMENTER = User(id = 3L, oauthId = "3", name = "Test User 3", email = "testuser3@example.com", role = Role.MEMBER)
        val OWNER = User(id = 4L, oauthId = "4", name = "Test User 4", email = "testuser4@example.com", role = Role.OWNER)
        val ADMIN = User(id = 5L, oauthId = "5", name = "Test User 5", email = "testuser5@example.com", role = Role.ADMIN)

        val HOTELS = listOf(
            Hotel(
                id = 1, name = "Hotel 1", description = "Hotel 1 description", rating = 2.0, lat = 2,
                lng = 2
            ),
            Hotel(
                id = 2, name = "Hotel 2", description = "Hotel 2 description", rating = 3.0, lat = 3,
                lng = 3
            ),
            Hotel(
                id = 3, name = "Hotel 3", description = "Hotel 3 description", rating = 4.0, lat = 4,
                lng = 4
            ),
        )

        val REVIEWS = listOf(
            Review(
                id = ReviewKey(USERS.first().id, HOTELS.first().id),
                text = "Review 1",
                rating = 5
            ),
            Review(
                id = ReviewKey(USERS.first().id, HOTELS.first().id),
                text = "Review 2",
                rating = 4
            ),
            Review(
                id = ReviewKey(USERS.last().id, HOTELS.last().id),
                text = "Review 3",
                rating = 5
            ),
        )

        fun createHeaders(user: User): HttpHeaders {
            val token = createMockToken(user)

            return HttpHeaders().apply {
                set("Authorization", "Bearer $token")
            }
        }

        private fun createMockToken(
            user: User,
            secretKey: String = "super-duper-ultra-secret-test-key"
        ): String {
            return Jwts.builder()
                .setSubject(user.oauthId) //    OAuth Id as the subject
                .setIssuer("mock-issuer")
                .setExpiration(Date(System.currentTimeMillis() + 3600000)) //   1 hour
                .claim("id", user.id)
                .claim("name", user.name)
                .claim("email", user.email)
                .claim("role", user.role.name)
                .signWith(SignatureAlgorithm.HS256, secretKey.toByteArray())
                .compact()
        }
    }
}