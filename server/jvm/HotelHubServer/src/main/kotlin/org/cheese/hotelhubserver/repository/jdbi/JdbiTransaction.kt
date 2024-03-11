package org.cheese.hotelhubserver.repository.jdbi

import org.cheese.hotelhubserver.repository.Transaction
import org.jdbi.v3.core.Handle

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    override val userRepository: UserRepository = JdbiUserRepository(handle)

    override fun rollback() {
        handle.rollback()
    }
}