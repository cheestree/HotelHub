package org.cheese.hotelhubserver.domain.annotations

import jakarta.validation.Constraint
import jakarta.validation.Payload
import org.cheese.hotelhubserver.domain.validators.StringValidator
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [StringValidator::class])
annotation class SanitizeString(
    val message: String = "Invalid string (illegal characters)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)