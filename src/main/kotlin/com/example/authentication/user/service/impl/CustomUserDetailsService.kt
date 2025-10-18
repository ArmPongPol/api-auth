package com.example.authentication.user.service.impl

import com.example.authentication.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService @Autowired constructor(
  private val userRepository: UserRepository
) : UserDetailsService {
  override fun loadUserByUsername(username: String): UserDetails {
    return userRepository.findByEmail(username)
      .orElseThrow { UsernameNotFoundException("User not found with email: $username") }
  }
}