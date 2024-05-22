package org.cheese.hotelhubserver.http.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.cheese.hotelhubserver.domain.user.AuthenticatedUser
import org.cheese.hotelhubserver.http.Uris
import org.cheese.hotelhubserver.http.model.user.UserCreateInputModel
import org.cheese.hotelhubserver.http.model.user.UserCreateTokenInputModel
import org.cheese.hotelhubserver.http.model.user.UserHomeOutputModel
import org.cheese.hotelhubserver.services.UserServices
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userServices: UserServices,
) {
    @PostMapping(Uris.User.CREATE)
    fun register(
        @Valid @RequestBody input: UserCreateInputModel,
    ): ResponseEntity<*> {
        val res = userServices.createUser(input.username, input.email, input.password)
        return ResponseEntity.status(CREATED).body(res)
    }

    @PostMapping(Uris.User.TOKEN)
    fun login(
        @Valid @RequestBody input: UserCreateTokenInputModel,
        response: HttpServletResponse
    ): ResponseEntity<*> {
        val res = userServices.login(input.username, input.password)
        userServices.createCookie(response, res, input.username)
        return ResponseEntity.ok(res.tokenValue)
    }

    @PostMapping(Uris.User.LOGOUT)
    fun logout(
        user: AuthenticatedUser,
        response: HttpServletResponse
    ) {
        userServices.deleteCookie(response)
        userServices.revokeToken(user.token)
    }

    @GetMapping(Uris.User.GET_BY_ID)
    fun getById(
        @Valid @NotEmpty @Min(value = 1, message = "Must be at least 1") @PathVariable id: String,
    ) {
        TODO("TODO")
    }

    @GetMapping(Uris.User.HOME)
    fun getUserHome(
        userAuthenticatedUser: AuthenticatedUser
    ): UserHomeOutputModel {
        return UserHomeOutputModel(
            id = userAuthenticatedUser.user.id,
            username = userAuthenticatedUser.user.username,
        )
    }
}
