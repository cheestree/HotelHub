package com.cheese.hotelhub.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {
    @GetMapping("/login")
    fun login(): String {
        return "login" // Renders a custom login page (e.g., login.html)
    }

    @GetMapping("/logout")
    fun logout(): String {
        return "logout" // Optional logout confirmation page
    }
}