package org.cheese.hotelhubserver.domain.exceptions

sealed class UserExceptions(msg: String) : Exception(msg) {
    class UserAlreadyExists(msg: String) : UserExceptions(msg)
    class InsecurePassword(msg: String) : UserExceptions(msg)
    class UserDoesntExist(msg: String) : UserExceptions(msg)
    class UserOrPasswordAreInvalid(msg: String) : UserExceptions(msg)
    class UserNotFound(msg: String) : UserExceptions(msg)
    class InvalidRole(msg: String) : UserExceptions(msg)
}
