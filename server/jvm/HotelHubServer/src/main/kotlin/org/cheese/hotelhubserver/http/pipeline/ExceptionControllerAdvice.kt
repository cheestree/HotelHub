package org.cheese.hotelhubserver.http.pipeline

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
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
}
