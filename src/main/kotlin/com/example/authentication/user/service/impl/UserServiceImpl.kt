package com.example.authentication.user.service.impl

import com.example.authentication.entity.User
import com.example.authentication.user.dto.AuthResponse
import com.example.authentication.user.repository.UserRepository
import com.example.authentication.user.request.LoginRequest
import com.example.authentication.user.request.RegisterRequest
import com.example.authentication.user.service.UserService
import com.example.authentication.utils.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
  private val userRepository: UserRepository,
  private val passwordEncoder: PasswordEncoder,
  private val jwtUtils: JwtUtils,
  private val authenticationManager: AuthenticationManager
) : UserService {
  override fun register(request: RegisterRequest): AuthResponse {
    userRepository.existsByEmail(request.email).let {
      if (it) {
        throw IllegalArgumentException("Email already exists")
      }
    }

    val user = User(
      email = request.email,
      password = passwordEncoder.encode(request.password),
      firstName = request.firstName,
      lastName = request.lastName,
      role = 0,
    )

    val savedUser = userRepository.save(user)
    val token = jwtUtils.generateToken(savedUser)

    return AuthResponse(
      token = token,
    )
  }

  override fun login(request: LoginRequest): AuthResponse {
    authenticationManager.authenticate(
      UsernamePasswordAuthenticationToken(request.email, request.password)
    )

    val user = userRepository.findByEmail(request.email)
      .orElseThrow { IllegalArgumentException("User not found") }

    val token = jwtUtils.generateToken(user)

    return AuthResponse(
      token = token,
    )
  }
}