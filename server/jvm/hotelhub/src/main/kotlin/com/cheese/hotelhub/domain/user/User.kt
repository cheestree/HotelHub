package com.cheese.hotelhub.domain.user

import com.cheese.hotelhub.domain.enums.AuthProvider
import com.cheese.hotelhub.domain.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0, // Auto-incremented primary key

    @Column(nullable = true, unique = true)
    val oauthId: String? = null,
    val authProvider: AuthProvider,

    @Column(nullable = true)
    val hash: String? = null,

    val name: String,
    val email: String,

    @Enumerated(EnumType.STRING)
    val role: Role
)