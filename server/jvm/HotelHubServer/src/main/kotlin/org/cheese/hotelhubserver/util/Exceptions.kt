package org.cheese.hotelhubserver.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <reified T : Exception> requireOrThrow(
    value: Boolean,
    lazyMessage: () -> Any,
) {
    contract {
        returns() implies value
    }
    if (!value) {
        val message = lazyMessage().toString()
        throw T::class.constructors.first().call(message)
    }
}

fun String.sanitize(): String {
    return this.trim()
        //  Remove non word characters
        .replace(Regex("[^\\w\\s-]"), "")
        //  Replace multiple spaces with a single space
        .replace(Regex("\\s+"), " ")
}
