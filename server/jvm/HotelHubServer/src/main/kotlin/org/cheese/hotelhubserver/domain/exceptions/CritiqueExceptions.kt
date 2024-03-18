package org.cheese.hotelhubserver.domain.exceptions

sealed class CritiqueExceptions(msg: String): Exception(msg) {
    class CritiqueAlreadyExists(msg: String) : CritiqueExceptions(msg)
    class CritiqueDoesntExist(msg: String) : CritiqueExceptions(msg)
    class HotelDoesntExist(msg: String) : CritiqueExceptions(msg)
}