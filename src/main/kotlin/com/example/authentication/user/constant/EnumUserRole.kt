package com.example.authentication.user.constant

enum class EnumUserRole(val value: Int) {
  USER(0), ADMIN(1);

  companion object {
    fun fromValue(value: Int): String {
      return EnumUserRole.entries.first { it.value == value }.name
    }
  }
}