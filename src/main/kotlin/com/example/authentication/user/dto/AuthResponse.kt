package com.example.authentication.user.dto

data class AuthResponse(
  val token: String,
)

data class ApiResponse<T>(
  val status: Boolean,
  val message: String,
  val data: T? = null
)
