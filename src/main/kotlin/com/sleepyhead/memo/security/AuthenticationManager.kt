package com.sleepyhead.memo.security

import io.jsonwebtoken.Claims
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Component
@AllArgsConstructor
class AuthenticationManager (
  @Autowired
  private val jwtUtil: JWTUtil
) : ReactiveAuthenticationManager {
  
  override fun authenticate(authentication: Authentication): Mono<Authentication> {
    val authToken: String = authentication.credentials.toString()
    val userEmail: String = jwtUtil.getUserEmailFromToken(authToken)

    return Mono.just(jwtUtil.validateToken(authToken))
      .filter { valid -> valid }
      .switchIfEmpty(Mono.empty())
      .map { valid ->
        val claims: Claims = jwtUtil.getAllClaimsFromToken(authToken)
        val role =  (claims["role"] as List<Map<String, *>>).map { SimpleGrantedAuthority(it["name"] as String) }
        UsernamePasswordAuthenticationToken(
          userEmail,
          null,
          role
        )
      }
  }
  
}