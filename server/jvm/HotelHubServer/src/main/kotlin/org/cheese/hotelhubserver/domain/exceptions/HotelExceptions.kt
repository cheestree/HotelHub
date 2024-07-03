package org.cheese.hotelhubserver.domain.exceptions

sealed class HotelExceptions(msg: String) : Exception(msg) {
    class HotelDoesntExist(msg: String) : HotelExceptions(msg)

    class HotelAlreadyExists(msg: String) : HotelExceptions(msg)
}
