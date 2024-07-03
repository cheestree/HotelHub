package org.cheese.hotelhubserver.http.model.user

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.jayway.jsonpath.internal.function.sequence.First
import jakarta.validation.GroupSequence
import jakarta.validation.constraints.*
import jakarta.validation.groups.Default
import org.cheese.hotelhubserver.domain.user.Role
import org.cheese.hotelhubserver.domain.user.RoleDeserializer

const val USERNAME_MIN_LENGTH = 8
const val USERNAME_MAX_LENGTH = 20
const val PASSWORD_MIN_LENGTH = 6

interface Second

@GroupSequence(Default::class, First::class, Second::class)
interface OrderedChecks

@GroupSequence(UserCreateInputModel::class, OrderedChecks::class)
data class UserCreateInputModel(
    @field:NotBlank(message = "can't be blank", groups = [First::class])
    @field:Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = "must be between $USERNAME_MIN_LENGTH and $USERNAME_MAX_LENGTH characters long", groups = [Second::class])
    val username: String,
    @field:NotBlank(message = "can't be blank", groups = [First::class])
    @field:Email(message = "not valid", groups = [Second::class])
    val email: String,
    @field:NotBlank(message = "can't be blank", groups = [First::class])
    @field:Size(min = PASSWORD_MIN_LENGTH, message = "must be at least $PASSWORD_MIN_LENGTH characters long", groups = [Second::class])
    val password: String,
    @field:JsonDeserialize(using = RoleDeserializer::class)
    val role: Role,
)
