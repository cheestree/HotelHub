package com.cheese.hotelhub.advice

import com.cheese.hotelhub.domain.error.Error
import com.cheese.hotelhub.domain.exception.HotelHubException.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<Error> {
        val errorDetails = Error(
            "Invalid value for path variable: ${ex.name}",
            "Type mismatch",
             HttpStatus.BAD_REQUEST
        )

        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NumberFormatException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNumberFormatException(ex: NumberFormatException): ResponseEntity<Error> {
        val errorResponse = Error(
            "Invalid number format: ${ex.message}",
            "Number format error",
            HttpStatus.BAD_REQUEST
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<Error> {
        val errorResponse = Error(
            "Not found: ${ex.message}",
            "Resource not found",
            HttpStatus.NOT_FOUND
        )

        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
}