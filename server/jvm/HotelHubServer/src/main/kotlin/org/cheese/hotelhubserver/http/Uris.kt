package org.cheese.hotelhubserver.http

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uris {
    const val PREFIX = "/api"

    object User {
        const val CREATE = "$PREFIX/user"
        const val TOKEN = "$PREFIX/user/token"
        const val LOGOUT = "$PREFIX/logout"
        const val GET_BY_ID = "$PREFIX/user/{id}"
        const val HOME = "$PREFIX/me"

        fun byId(id: Int) = UriTemplate(GET_BY_ID).expand(id)

        fun home(): URI = URI(HOME)

        fun login(): URI = URI(TOKEN)

        fun register(): URI = URI(CREATE)
    }

    object Hotel {
        const val CREATE = "$PREFIX/hotel"
        const val GETHOTEL = "$PREFIX/hotel/{id}"
        const val GETHOTELS = "$PREFIX/hotel"
    }
}
