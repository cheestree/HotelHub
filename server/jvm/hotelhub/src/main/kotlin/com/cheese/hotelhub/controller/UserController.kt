package com.cheese.hotelhub.controller

import com.cheese.hotelhub.domain.user.User
import com.cheese.hotelhub.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/{userId}")
    fun getUserProfile(
        @PathVariable userId: Long
    ): User {
        return userService.getUserById(userId)
    }
}