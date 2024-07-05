package org.cheese.hotelhubserver.integration

import jakarta.servlet.http.HttpServletRequest
import org.cheese.hotelhubserver.repository.jdbi.JdbiTransactionManager
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UserTests {

    @Autowired
    private lateinit var tm: JdbiTransactionManager

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Nested
    inner class `no DB access` {
        @Test
        fun `should return role check failure`() {
            val json = buildRegisterModel("chef", "chef@gmail.com", "Chef@123", "ADMI")
            mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.title").value("Serialization Error"))
                .andExpect(jsonPath("$.detail.role").value("Invalid role: ADMI"))
        }

        @Test
        fun `should return user not found`() {
            val userId = "nonexistent-id"
            mockMvc.perform(get("/api/users/$userId"))
                .andExpect(status().isNotFound)
        }

        @Test
        fun `should return blank checks failure`() {
            val json = buildRegisterModel("", "", "", "ADMIN")
            mockMvc.perform(
                post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.detail.username").value("can't be blank"))
                .andExpect(jsonPath("$.detail.email").value("can't be blank"))
                .andExpect(jsonPath("$.detail.password").value("can't be blank"))
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
        fun `should return user on fetch by ID successfully`() {
            val json = buildRegisterModel("chef", "chef@gmail.com", "Chef@123", "USER")
            mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is2xxSuccessful)
                .andDo { result ->
                    mockMvc.perform(get("/api/user/${result.response.contentAsString.toInt()}"))
                        .andExpect(status().isOk)
                }
        }

        @Test
        fun `should login successfully`() {
            val json = buildRegisterModel("chef", "chef@gmail.com", "Chef@123", "USER")
            mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is2xxSuccessful)
                .andReturn()

            val loginJson = buildLoginModel("chef","Chef@123")
            val loginResult = mockMvc.perform(post("/api/user/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk)
                .andReturn()


            val tokenCookie = loginResult.response.getCookie("token")
            val playerCookie = loginResult.response.getCookie("player")

            assertNotNull(tokenCookie)
            assertEquals("token", tokenCookie!!.name)
            assertNotNull(playerCookie)
            assertEquals("player", playerCookie!!.name)
        }
    }

    fun buildRegisterModel(username: String? = null, email: String? = null, password: String? = null, role: String? = null): String {
        return """
        {
            "username": "${username ?: ""}",
            "email": "${email ?: ""}",
            "password": "${password ?: ""}",
            "role": "${role ?: ""}"
        }
    """.trimIndent()
    }

    fun buildLoginModel(username: String? = null, password: String? = null): String {
        return """
        {
            "username": "${username ?: ""}",
            "password": "${password ?: ""}"
        }
    """.trimIndent()
    }
}