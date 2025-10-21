package com.example.authentication.config

import com.example.authentication.user.service.impl.CustomUserDetailsService
import com.example.authentication.utils.JwtAuthFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(
  private val jwtAuthFilter: JwtAuthFilter,
  private val userService: CustomUserDetailsService
) {
  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Bean
  fun authenticationProvider(): AuthenticationProvider {
    val authProvider = DaoAuthenticationProvider()
    authProvider.setUserDetailsService(userService)
    authProvider.setPasswordEncoder(passwordEncoder())
    return authProvider
  }

  @Bean
  fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
    return config.authenticationManager
  }

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .csrf { it.disable() }
      .cors { it.configurationSource(corsConfigurationSource()) }
      .authorizeHttpRequests { auth ->
        auth
          .requestMatchers("/api/auth/**", "/api/public/**").permitAll()
          .requestMatchers(HttpMethod.OPTIONS).permitAll()
          .anyRequest().authenticated()
      }
      .sessionManagement {
        it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }
      .authenticationProvider(authenticationProvider())
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

    return http.build()
  }

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val config = CorsConfiguration()
    config.allowedOriginPatterns = listOf(
      "http://localhost:3000",
      "https://*.up.railway.app",
      "https://web-production-f1bf3.up.railway.app"
    )
    config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
    config.allowedHeaders = listOf("*")
    config.exposedHeaders = listOf("Authorization", "Content-Type")
    config.allowCredentials = true    // ✅ อนุญาต cookie / header token

    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", config)
    return source
  }

}