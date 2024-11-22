package com.cheese.hotelhub.domain.user

import com.cheese.hotelhub.domain.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0, // Auto-incremented primary key

    @Column(unique = true)
    val oauthId: String,

    val name: String,
    val email: String,

    @Enumerated(EnumType.STRING)
    val role: Role
)