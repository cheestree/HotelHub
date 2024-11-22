package com.cheese.hotelhub

import com.cheese.hotelhub.interceptor.Interceptor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class HotelHubApplication

@Configuration
@EnableWebSecurity
class WebConfig(
	private val interceptors: Interceptor,
) : WebMvcConfigurer {

	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.authorizeHttpRequests { requests ->
				requests.anyRequest().permitAll()
			}
			.oauth2Login { oauth2Login ->
				oauth2Login
					.loginPage("/login") //	Custom login page
					.defaultSuccessUrl("/home", true) // Redirect after login
			}
			.logout { logout ->
				logout
					.logoutUrl("/logout") // Logout endpoint
					.logoutSuccessUrl("/") // Redirect after logout
					.invalidateHttpSession(true) // Invalidate session
					.deleteCookies("JSESSIONID") // Clean session cookies
			}

		return http.build()
	}

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(interceptors)
	}
}

fun main(args: Array<String>) {
	runApplication<HotelHubApplication>(*args)
}
