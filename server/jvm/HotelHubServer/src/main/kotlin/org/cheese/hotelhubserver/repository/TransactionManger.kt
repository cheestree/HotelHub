package org.cheese.hotelhubserver.repository

interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}