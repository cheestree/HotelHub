package org.cheese.hotelhubserver.repository

import org.cheese.hotelhubserver.repository.jdbi.UserRepository

interface Transaction {

    val userRepository: UserRepository

    // other repository types
    fun rollback()
}