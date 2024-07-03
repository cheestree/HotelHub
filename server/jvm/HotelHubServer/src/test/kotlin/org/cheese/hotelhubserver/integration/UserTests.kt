package org.cheese.hotelhubserver.integration

import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
class UserTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup(){
        mock(HttpServletRequest::class.java)
    }

    @Test
    fun `handle Validation Exceptions should return all blank`() {
        val json = """
            {
                "username": "",
                "email": "",
                "password": "",
                "role": "ADMIN"
            }
        """.trimIndent()

        mockMvc.perform(post("/api/user")
            .contentType("application/json")
            .content(json))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.title").value("Validation Error"))
            .andExpect(jsonPath("$.detail.username").value("can't be blank"))
            .andExpect(jsonPath("$.detail.email").value("can't be blank"))
            .andExpect(jsonPath("$.detail.password").value("can't be blank"))
    }

    @Test
    fun `handle Validation Exceptions should return all checks`() {
        val json = """
            {
                "username": "abc",
                "email": "abc",
                "password": "abc",
                "role": "ADMIN"
            }
        """.trimIndent()

        mockMvc.perform(post("/api/user")
            .contentType("application/json")
            .content(json))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.title").value("Validation Error"))
            .andExpect(jsonPath("$.detail.username").value("must be between 8 and 20 characters long"))
            .andExpect(jsonPath("$.detail.email").value("not valid"))
            .andExpect(jsonPath("$.detail.password").value("must be at least 6 characters long"))
    }

    @Test
    fun `handle Serializer Exceptions should return role check`() {
        val json = """
            {
                "username": "abc",
                "email": "abc",
                "password": "abc",
                "role": "ADMI"
            }
        """.trimIndent()

        mockMvc.perform(post("/api/user")
            .contentType("application/json")
            .content(json))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.title").value("Serialization Error"))
            .andExpect(jsonPath("$.detail.role").value("Invalid role: ADMI"))
    }

    @Test
    fun `handle Not Found should return correct Problem response`() {
        val userId = "nonexistent-id"

        mockMvc.perform(get("/api/users/$userId"))
            .andExpect(status().isNotFound)

    }
}