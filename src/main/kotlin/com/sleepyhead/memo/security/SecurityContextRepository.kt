package com.sleepyhead.memo.security

import com.sleepyhead.memo.config.FirebaseConfig
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@AllArgsConstructor
@Component
class SecurityContextRepository(
  @Autowired
  private val jwtUtil: JWTUtil
) : ServerSecurityContextRepository {
  
  private val authenticationManager = AuthenticationManager(jwtUtil)
  
  @Autowired
  private var firebaseConfig: FirebaseConfig ?= null
  
  override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
    throw UnsupportedOperationException("Not supported yet.")
  }
  
  override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
    return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
      .filter { authHeader -> authHeader.startsWith(TOKEN_PREFIX) }
      .flatMap<SecurityContext?> { authHeader ->
        val authToken: String = authHeader.substring(7)
        val auth: Authentication = UsernamePasswordAuthenticationToken(authToken, authToken)
//        authenticationManager.authenticate(auth).map(::SecurityContextImpl)
        firebaseConfig!!.authenticate(auth).map(::SecurityContextImpl)
      }
  }
  
  companion object {
    const val TOKEN_PREFIX = "Bearer "
  }
}


