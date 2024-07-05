package org.cheese.hotelhubserver.unit

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.cheese.hotelhubserver.domain.exceptions.UserExceptions.*
import org.cheese.hotelhubserver.domain.user.Role
import org.cheese.hotelhubserver.repository.jdbi.JdbiTransactionManager
import org.cheese.hotelhubserver.services.UserServices
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@SpringBootTest
class UserTests {
    @Autowired
    private lateinit var tm: JdbiTransactionManager

    @Autowired
    private lateinit var userServices: UserServices

    @Nested
    inner class `no DB access` {
        @Test
        fun `should throw InsecurePassword exception on register`() {
            assertFailsWith<InsecurePassword>{
                userServices.createUser("chef", "chef@chef.com", "abc", Role.USER)
            }
        }
    }

    @Nested
    inner class `DB access` {
        @BeforeEach
        fun setup(){
            mock(HttpServletRequest::class.java)
            tm.run { it.userRepository.deleteAll() }
        }

        @AfterEach
        fun cleanup() = tm.run { it.userRepository.deleteAll() }

        @Test
        fun `should throw UserAlreadyExists exception on register`() {
            userServices.createUser("chef", "chef@chef.com", "abcde", Role.USER)

            assertFailsWith<UserAlreadyExists>{
                userServices.createUser("chef", "chef@chef.com", "abcde", Role.USER)
            }
        }

        @Test
        fun `should throw UserDoesntExist exception on login`() {
            userServices.createUser("chef", "chef@chef.com", "abcde", Role.USER)

            assertFailsWith<UserDoesntExist>{
                userServices.login("che", "abcde")
            }
        }

        @Test
        fun `should throw UserOrPasswordAreInvalid exception on login`() {
            userServices.createUser("chef", "chef@chef.com", "abcde", Role.USER)

            assertFailsWith<UserOrPasswordAreInvalid>{
                userServices.login("chef", "abcdef")
            }
        }

        @Test
        fun `should login successfully`() {
            userServices.createUser("chef", "chef@chef.com", "abcde", Role.USER)
            val token = userServices.login("chef", "abcde")
            val response = mock(HttpServletResponse::class.java)
            userServices.createCookie(response, token, "chef")

            val cookieCaptor = ArgumentCaptor.forClass(Cookie::class.java)
            verify(response, times(2)).addCookie(cookieCaptor.capture())

            val cookies = cookieCaptor.allValues
            val cookieExists = cookies.any { it.name == "token" } && cookies.any { it.name == "player" }

            assertTrue(cookieExists)
        }

        @Test
        fun `should throw UserNotFound exception`() {
            assertFailsWith<UserNotFound>{
                userServices.getUserById(1)
            }
        }

        @Test
        fun `should fetch user successfully`() {
            val id = userServices.createUser("chef", "chef@chef.com", "abcde", Role.USER)
            val user = userServices.getUserById(id)

            assertEquals(id, user.id)
        }
    }
}