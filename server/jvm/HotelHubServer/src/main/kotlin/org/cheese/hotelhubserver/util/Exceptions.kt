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
