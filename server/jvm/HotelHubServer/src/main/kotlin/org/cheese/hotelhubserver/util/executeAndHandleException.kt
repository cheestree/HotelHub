package org.cheese.hotelhubserver.util

fun <T : Exception> executeAndHandleException(
    block: () -> Unit,
    exceptionType: Class<T>,
    errorMessage: () -> Any
): String? {
    return try {
        block()
        null
    } catch (ex: Exception) {
        if (exceptionType.isInstance(ex)) {
            errorMessage().toString()
        } else {
            throw ex
        }
    }
}