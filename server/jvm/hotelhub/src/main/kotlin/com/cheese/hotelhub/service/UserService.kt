package com.cheese.hotelhub.service

import com.cheese.hotelhub.domain.enums.AuthProvider
import com.cheese.hotelhub.domain.enums.Role
import com.cheese.hotelhub.domain.exception.HotelHubException.ResourceNotFoundException
import com.cheese.hotelhub.domain.user.User
import com.cheese.hotelhub.repository.user.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("User $id not found")
        }
    }

    fun getUserByOauthId(oauthId: String): User? {
        return userRepository.findByOauthId(oauthId).orElse(null)
    }

    fun register(username: String, password: String, email: String): User {
        val passwordEncoder = BCryptPasswordEncoder()
        val hash = passwordEncoder.encode(password)
        return userRepository.save(User(name = username, hash = hash, email = email, authProvider = AuthProvider.EMAIL, role = Role.MEMBER))
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email).orElse(null)
    }
}
