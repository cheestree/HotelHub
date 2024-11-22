package com.cheese.hotelhub.integration.base

import com.cheese.hotelhub.interceptor.Interceptor
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.crypto.spec.SecretKeySpec


@TestConfiguration
class Config(
    private val interceptors: Interceptor,
) : WebMvcConfigurer {
    @Bean("testSecurityFilterChain")
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { requests ->
                requests.anyRequest().permitAll()
            }
            .oauth2ResourceServer {
                it.jwt { jw ->
                    jw.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }
        return http.build()
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptors)
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()

        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val userId = jwt.claims["sub"] as String
            listOf(SimpleGrantedAuthority(userId))
        }

        return converter
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val secretKey = "super-duper-ultra-secret-test-key"
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        return NimbusJwtDecoder
            .withSecretKey(secretKeySpec)
            .build()
    }
}