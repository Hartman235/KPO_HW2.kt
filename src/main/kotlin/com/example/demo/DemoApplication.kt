package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication

@Configuration
class SecurityConfig {
	@Bean
	fun userDetailsManager(passwordEncoder: PasswordEncoder): UserDetailsManager {
		val admin = User.builder().username("admin").password(passwordEncoder.encode("admin")).roles("ADMIN").build()
		val user = User.builder().username("user").password(passwordEncoder.encode("password")).roles("USER").build()
		return InMemoryUserDetailsManager(admin, user)
	}

	@Bean
	fun securityFilterChain(httpSecurity: HttpSecurity) : SecurityFilterChain {
		httpSecurity.csrf { it.disable() }
		httpSecurity.formLogin { it.permitAll() }

		httpSecurity.authorizeHttpRequests {
			it.requestMatchers("/hello-all").permitAll()
			it.requestMatchers("/hello-admin").hasRole("ADMIN")
			it.requestMatchers("/hello-user").hasRole("USER")
		}
		return httpSecurity.build()
	}
	@Bean
	fun passwordEncoder() = BCryptPasswordEncoder()
}

@RestController
class HelloController {
	@GetMapping("/hello-all")
	fun helloAllController() = "Hi"

	@GetMapping("/hello-user")
	fun helloUserController() = "Hi user"

	@GetMapping("/hello-admin")
	fun helloAdminController() = "Hi admin"
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
