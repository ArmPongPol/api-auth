package com.example.authentication.user.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
  @field:Email(message = "Email should be valid")
  @field:NotBlank(message = "Email is required")
  val email: String,

  @field:NotBlank(message = "Password is required")
  @field:Size(min = 6, message = "Password should have at least 6 characters")
  val password: String,

  @field:NotBlank(message = "First name is required")
  val firstName: String,

  @field:NotBlank(message = "Last name is required")
  val lastName: String
)

data class LoginRequest(
  @field:Email(message = "Email should be valid")
  @field:NotBlank(message = "Email is required")
  val email: String,

  @field:NotBlank(message = "Password is required")
  val password: String
)