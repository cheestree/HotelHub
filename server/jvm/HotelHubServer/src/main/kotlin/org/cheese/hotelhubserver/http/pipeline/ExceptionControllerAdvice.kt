package org.cheese.hotelhubserver.http.pipeline

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.JsonMappingException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.cheese.hotelhubserver.domain.exceptions.CritiqueExceptions.CritiqueAlreadyExists
import org.cheese.hotelhubserver.domain.exceptions.CritiqueExceptions.CritiqueDoesntExist
import org.cheese.hotelhubserver.domain.exceptions.HotelExceptions.HotelAlreadyExists
import org.cheese.hotelhubserver.domain.exceptions.HotelExceptions.HotelDoesntExist
import org.cheese.hotelhubserver.domain.exceptions.UserExceptions.UserOrPasswordAreInvalid
import org.cheese.hotelhubserver.domain.exceptions.UserExceptions.InsecurePassword
import org.cheese.hotelhubserver.domain.exceptions.UserExceptions.InvalidRole
import org.cheese.hotelhubserver.domain.exceptions.UserExceptions.UserNotFound
import org.cheese.hotelhubserver.domain.exceptions.UserExceptions.UserAlreadyExists
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice



@RestControllerAdvice
class ExceptionControllerAdvice {

    data class Problem(
        val title: String?,
        val detail: Any?
    )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Problem> {
        val errors = ex.bindingResult.fieldErrors.associate {
            it.field to it.defaultMessage
        }
        return Problem(title = "Validation Error", detail = errors).toResponse(HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationExceptions(ex: ConstraintViolationException): ResponseEntity<Problem> {
        val errors = ex.constraintViolations.associate {
            it.propertyPath.toString() to it.message
        }
        return Problem(title = "Constraint Violation", detail = errors).toResponse(HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JacksonException::class)
    fun handleSerializationExceptions(ex: JacksonException): ResponseEntity<Problem> {
        val detail = when (ex) {
            is JsonMappingException -> {
                ex.path.associate { reference ->
                    (reference.fieldName ?: "unknown") to ex.originalMessage
                }
            }
            else -> mapOf("error" to ex.message)
        }
        return Problem(title = "Serialization Error", detail = detail).toResponse(HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(
        value = [
            HotelDoesntExist::class,
            CritiqueDoesntExist::class,
            UserNotFound::class
        ],
    )
    fun handleNotFound(
        request: HttpServletRequest,
        ex: Exception,
    ) = ex.handle(request, HttpStatus.NOT_FOUND)

    @ExceptionHandler(
        value = [
            IllegalArgumentException::class,
            HotelAlreadyExists::class,
            CritiqueAlreadyExists::class,
            InsecurePassword::class,
            InvalidRole::class,
            UserAlreadyExists::class,
            UserOrPasswordAreInvalid::class
        ],
    )
    fun handleBadRequest(
        request: HttpServletRequest,
        ex: Exception,
    ) = ex.handle(request, HttpStatus.BAD_REQUEST)

    private fun Exception.handle(
        request: HttpServletRequest,
        status: HttpStatus,
        title: String = getName(),
        detail: Any? = message,
        headers: HttpHeaders? = null,
    ): ResponseEntity<Problem> =
        Problem(title = title, detail = detail).toResponse(status, headers)

    private fun Exception.getName(): String =
        (this::class.simpleName ?: "Unknown")
            .replace("Exception", "")
            .replace(Regex("([a-z])([A-Z])")) { "${it.groupValues[1]} ${it.groupValues[2]}" }

    private fun Problem.toResponse(status: HttpStatus, headers: HttpHeaders? = null): ResponseEntity<Problem> =
        ResponseEntity(this, headers, status)
}
