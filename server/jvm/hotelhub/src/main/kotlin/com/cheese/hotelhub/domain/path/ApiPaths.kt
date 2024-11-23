package com.cheese.hotelhub.domain.path

object ApiPaths {
    private const val BASE_URL = "http://localhost:{port}"
    const val API_EXTENSION = "/api"
    const val URL = BASE_URL+API_EXTENSION
    const val HOTEL = "/hotel"
    const val REVIEW = "/review"
    const val USER = "/user"

    object Hotel {
        const val GET_HOTEL = "/{hotelId}"
        const val POST_HOTEL = ""
        const val DELETE_HOTEL = "/{hotelId}"
    }

    object Users {
        const val LOGIN = "/login"
        const val REGISTER = ""
        const val LOGOUT = "/logout"
        const val GET_USER = "/{userId}"
    }

    object Reviews {
        const val POST_REVIEW = "/{hotelId}"
        const val DELETE_REVIEW = "/{hotelId}"
        const val GET_REVIEW = "/{hotelId}/review/{reviewId}"
        const val GET_REVIEWS = "/{hotelId}"
    }

    fun resolvePath(path: String, params: Map<String, String>): String {
        var resolvedPath = path
        params.forEach { (key, value) ->
            resolvedPath = resolvedPath.replace("{$key}", value)
        }
        return resolvedPath
    }
}