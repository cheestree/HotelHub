package org.cheese.hotelhubserver.repository.interfaces

interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}
