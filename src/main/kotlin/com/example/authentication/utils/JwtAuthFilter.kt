package com.example.authentication.utils

import com.example.authentication.user.service.impl.CustomUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
  private val jwtUtils: JwtUtils,
  private val userService: CustomUserDetailsService
) : OncePerRequestFilter() {

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    val authHeader = request.getHeader("Authorization")

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response)
      return
    }

    val jwt = authHeader.substring(7)
    val userEmail = jwtUtils.extractUsername(jwt)

    if (SecurityContextHolder.getContext().authentication == null) {
      val userDetails = userService.loadUserByUsername(userEmail)

      if (jwtUtils.isTokenValid(jwt, userDetails)) {
        val authToken = UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.authorities
        )
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
      }
    }

    filterChain.doFilter(request, response)
  }
}