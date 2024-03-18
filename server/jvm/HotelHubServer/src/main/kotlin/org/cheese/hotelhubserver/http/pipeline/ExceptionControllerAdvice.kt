package org.cheese.hotelhubserver.http.pipeline

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.cheese.hotelhubserver.domain.exceptions.HotelExceptions.HotelDoesntExist
import org.cheese.hotelhubserver.domain.exceptions.HotelExceptions.HotelAlreadyExists
import org.cheese.hotelhubserver.http.model.Problem
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): Map<String, String?>? {
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.forEach { error: ObjectError ->
            val fieldName = (error as FieldError).field
            errors[fieldName] = error.getDefaultMessage()
        }
        return errors
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationExceptions(ex: ConstraintViolationException): Map<String, String?>? {
        val errors: MutableMap<String, String?> = HashMap()
        ex.constraintViolations.forEach { violation ->
            val fieldName = violation.propertyPath.toString()
            errors[fieldName] = violation.message
        }
        return errors
    }

    @ExceptionHandler(
        value = [
            IllegalArgumentException::class,
            HotelDoesntExist::class,
            HotelAlreadyExists::class,
        ]
    )
    fun handleBadRequest(request: HttpServletRequest, ex: Exception) =
        ex.handle(
            request = request,
            status = HttpStatus.BAD_REQUEST
        )

    companion object {
        private fun Exception.handle(
            request: HttpServletRequest,
            status: HttpStatus,
            title: String = getName(),
            detail: String? = message,
            headers: HttpHeaders? = null
        ): ResponseEntity<Problem> =
            Problem(
                title = title,
                detail = detail,
            ).toResponse(status, headers)
                //.also {
                //logger.warn("Handled Exception: {}", message)
            //}


        private fun Exception.getName(): String =
            (this::class.simpleName ?: "Unknown")
                .replace("Exception", "")
                .replace(Regex("([a-z])([A-Z])")) { "${it.groupValues[1]} ${it.groupValues[2]}" }
    }
}
