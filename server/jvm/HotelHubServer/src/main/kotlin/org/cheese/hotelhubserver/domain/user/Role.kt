package org.cheese.hotelhubserver.domain.user

enum class Role {
    ADMIN,
    OWNER,
    EMPLOYEE,
    USER;

    companion object {
        fun fromString(roleString: String): Role? {
            return entries.find { it.name.equals(roleString, ignoreCase = true) }
        }
    }
}