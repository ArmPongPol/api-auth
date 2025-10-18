package com.example.authentication.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtUtils {
  @Value("\${app.jwt.secret}")
  private lateinit var secretKey: String

  @Value("\${app.jwt.expiration}")
  private var expiration: Long = 0

  private fun getSigningKey(): Key {
    return Keys.hmacShaKeyFor(secretKey.toByteArray())
  }

  fun generateToken(userDetail: UserDetails): String {
    return generateToken(HashMap(), userDetail)
  }

  fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
    return Jwts.builder()
      .setIssuer("Authentication Service")
      .setSubject(userDetails.username)
      .setIssuedAt(Date(System.currentTimeMillis()))
      .setExpiration(Date(System.currentTimeMillis() + expiration))
      .signWith(getSigningKey())
      .compact()
  }

  fun extractUsername(token: String): String {
    return extractClaim(token) { claims -> claims.subject }
  }

  fun extractExpiration(token: String): Date {
    return extractClaim(token) { claims -> claims.expiration }
  }

  fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
    val claims = extractAllClaims(token)
    return claimsResolver(claims)
  }

  private fun extractAllClaims(token: String): Claims {
    return Jwts
      .parser()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .body
  }

  private fun isTokenExpired(token: String): Boolean {
    return extractExpiration(token).before(Date())
  }

  fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
    val username = extractUsername(token)
    return username == userDetails.username && !isTokenExpired(token)
  }
}