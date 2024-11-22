package com.cheese.hotelhub.service

import com.cheese.hotelhub.domain.enums.Role
import com.cheese.hotelhub.domain.exception.HotelHubException.ResourceNotFoundException
import com.cheese.hotelhub.domain.user.User
import com.cheese.hotelhub.repository.user.UserRepository
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

    fun createUser(oauthId: String, name: String, email: String): User {
        return userRepository.save(User(oauthId = oauthId, name = name, email = email, role = Role.MEMBER))
    }

    fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email).orElseThrow {
            ResourceNotFoundException("User $email not found")
        }
    }
}
