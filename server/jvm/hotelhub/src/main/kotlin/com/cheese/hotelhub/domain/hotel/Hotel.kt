package com.cheese.hotelhub.domain.hotel

import com.cheese.hotelhub.domain.user.User
import jakarta.persistence.*

@Entity
@Table(name = "hotels")
data class Hotel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,
    val description: String,

    val rating: Double,

    val lat: Long,
    val lng: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    val owner: User? = null
)
