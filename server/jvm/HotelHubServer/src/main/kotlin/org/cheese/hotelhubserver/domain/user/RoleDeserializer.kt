package org.cheese.hotelhubserver.domain.user

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.cheese.hotelhubserver.domain.exceptions.UserExceptions.InvalidRole
import org.cheese.hotelhubserver.util.requireOrThrow

class RoleDeserializer : JsonDeserializer<Role>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): Role {
        val roleString = Role.fromString(parser.valueAsString)
        requireOrThrow<InvalidRole>(roleString != null) { "Invalid role: ${parser.valueAsString}" }
        return roleString
    }
}