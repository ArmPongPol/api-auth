package com.example.authentication.user.service

import com.example.authentication.user.dto.AuthResponse
import com.example.authentication.user.request.LoginRequest
import com.example.authentication.user.request.RegisterRequest

interface UserService {
  fun register(request: RegisterRequest): AuthResponse
  fun login(request: LoginRequest): AuthResponse
}