package com.example.authentication.user.controller

import com.example.authentication.user.dto.ApiResponse
import com.example.authentication.user.request.LoginRequest
import com.example.authentication.user.request.RegisterRequest
import com.example.authentication.user.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class UserController @Autowired constructor(
  private val userService: UserService
) {
  @PostMapping("/register")
  fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<Any>> {
    return try {
      val authResponse = userService.register(request)
      ResponseEntity.ok(
        ApiResponse(
          status = true,
          message = "User registered successfully",
          data = authResponse
        )
      )
    } catch (e: Exception) {
      ResponseEntity.badRequest().body(
        ApiResponse(
          status = false,
          message = e.message ?: "Registration failed"
        )
      )
    }
  }

  @PostMapping("/login")
  fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<Any>> {
    return try {
      val authResponse = userService.login(request)
      ResponseEntity.ok(
        ApiResponse(
          status = true,
          message = "Login successfully",
          data = authResponse
        )
      )
    } catch (e: Exception) {
      ResponseEntity.badRequest().body(
        ApiResponse(
          status = false,
          message = e.message ?: "Login failed"
        )
      )
    }
  }
}