package com.cheese.hotelhub.domain.exception

import kotlin.Exception

sealed class HotelHubException(message: String) : Exception(message) {
    class UnauthorizedAccessException(message: String) : HotelHubException(message)
    class ResourceNotFoundException(message: String) : HotelHubException(message)
    class InvalidInputException(message: String) : HotelHubException(message)
}