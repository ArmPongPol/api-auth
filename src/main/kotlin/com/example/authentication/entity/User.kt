package com.example.authentication.entity

import com.example.authentication.user.constant.EnumUserRole
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "users")
data class User(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  @Column(unique = true, nullable = false)
  val email: String = "",
  private val password: String = "",
  val firstName: String = "",
  val lastName: String = "",
  val role: Int = 0,
  val isActive: Boolean = true,
  val createdAt: Date = Date(),
  val updatedAt: Date = Date()
): UserDetails {
  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return mutableListOf(SimpleGrantedAuthority("ROLE_${EnumUserRole.fromValue(role)}"))
  }

  override fun getPassword(): String = password

  override fun getUsername(): String = email

  override fun isAccountNonExpired(): Boolean = true

  override fun isAccountNonLocked(): Boolean = true

  override fun isCredentialsNonExpired(): Boolean = true

  override fun isEnabled(): Boolean = isActive
}