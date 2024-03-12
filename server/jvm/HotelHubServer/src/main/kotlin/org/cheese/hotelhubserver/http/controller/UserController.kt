package org.cheese.hotelhubserver.http.controller

import org.cheese.hotelhubserver.domain.user.AuthenticatedUser
import org.cheese.hotelhubserver.http.Uris
import org.cheese.hotelhubserver.http.model.user.Problem
import org.cheese.hotelhubserver.http.model.user.UserCreateInputModel
import org.cheese.hotelhubserver.http.model.user.UserCreateTokenInputModel
import org.cheese.hotelhubserver.http.model.user.UserHomeOutputModel
import org.cheese.hotelhubserver.http.model.user.UserTokenCreateOutputModel
import org.cheese.hotelhubserver.services.TokenCreationError
import org.cheese.hotelhubserver.services.UserCreationError
import org.cheese.hotelhubserver.services.UserServices
import org.cheese.hotelhubserver.util.Failure
import org.cheese.hotelhubserver.util.Success
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userServices: UserServices,
) {
    @PostMapping(Uris.User.CREATE)
    fun create(
        @RequestBody input: UserCreateInputModel,
    ): ResponseEntity<*> {
        val res = userServices.createUser(input.username, input.email, input.password)
        return when (res) {
            is Success ->
                ResponseEntity.status(201)
                    .header(
                        "Location",
                        Uris.User.byId(res.value).toASCIIString(),
                    ).build<Unit>()

            is Failure ->
                when (res.value) {
                    UserCreationError.InsecurePassword -> Problem.response(400, Problem.insecurePassword)
                    UserCreationError.UserAlreadyExists -> Problem.response(400, Problem.alreadyExists)
                }
        }
    }

    @PostMapping(Uris.User.TOKEN)
    fun token(
        @RequestBody input: UserCreateTokenInputModel,
    ): ResponseEntity<*> {
        val res = userServices.createToken(input.username, input.password)
        return when (res) {
            is Success ->
                ResponseEntity.status(200)
                    .body(UserTokenCreateOutputModel(res.value.tokenValue))

            is Failure ->
                when (res.value) {
                    TokenCreationError.UserOrPasswordAreInvalid ->
                        Problem.response(400, Problem.userOrPasswordAreInvalid)
                }
        }
    }

    @PostMapping(Uris.User.LOGOUT)
    fun logout(user: AuthenticatedUser) {
        userServices.revokeToken(user.token)
    }

    @GetMapping(Uris.User.GET_BY_ID)
    fun getById(
        @PathVariable id: String,
    ) {
        TODO("TODO")
    }

    @GetMapping(Uris.User.HOME)
    fun getUserHome(userAuthenticatedUser: AuthenticatedUser): UserHomeOutputModel {
        return UserHomeOutputModel(
            id = userAuthenticatedUser.user.id,
            username = userAuthenticatedUser.user.username,
        )
    }
}
