package org.cheese.hotelhubserver.http

object Uris {
    const val PREFIX = "/api"

    object User {
        const val CREATE = "$PREFIX/user"
        const val TOKEN = "$PREFIX/user/token"
        const val LOGOUT = "$PREFIX/logout"
        const val GET_BY_ID = "$PREFIX/user/{id}"
        const val HOME = "$PREFIX/me"
    }

    object Hotel {
        const val CREATE = "$PREFIX/hotel"
        const val GETHOTEL = "$PREFIX/hotel/{hotelId}"
        const val GETHOTELS = "$PREFIX/hotel"
    }

    object Critique {
        const val CREATE = "$PREFIX/critique"
        const val GET = "$PREFIX/critique/{critiqueId}"
        const val DELETE = "$PREFIX/critique/{critiqueId}"
        const val EDIT = "$PREFIX/critique/{critiqueId}"
    }
}
