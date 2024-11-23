package com.cheese.hotelhub.controller

import com.cheese.hotelhub.domain.model.input.RegisterInputModel
import com.cheese.hotelhub.domain.path.ApiPaths.API_EXTENSION
import com.cheese.hotelhub.domain.path.ApiPaths.USER
import com.cheese.hotelhub.domain.path.ApiPaths.Users.GET_USER
import com.cheese.hotelhub.domain.path.ApiPaths.Users.LOGIN
import com.cheese.hotelhub.domain.path.ApiPaths.Users.LOGOUT
import com.cheese.hotelhub.domain.path.ApiPaths.Users.REGISTER
import com.cheese.hotelhub.domain.user.User
import com.cheese.hotelhub.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_EXTENSION+USER)
class UserController(
    private val userService: UserService
) {
    @GetMapping(GET_USER)
    fun getUserProfile(
        @PathVariable userId: Long
    ): User {
        return userService.getUserById(userId)
    }

    @GetMapping(LOGIN)
    fun login(): String {
        return "login"
    }

    @GetMapping(LOGOUT)
    fun logout(): String {
        return "logout"
    }

    @PostMapping(REGISTER)
    fun register(
        @RequestBody register: RegisterInputModel
    ): User {
        return userService.register(register.username, register.password, register.email)
    }
}