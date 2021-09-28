package com.sleepyhead.memo.security

import lombok.AllArgsConstructor
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@AllArgsConstructor
@Component
class SecurityContextRepository : ServerSecurityContextRepository {
  
  lateinit var authenticationManager: AuthenticationManager
  
  override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
    throw UnsupportedOperationException("Not supported yet.")
  }
  
  override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
    return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
      .filter { authHeader -> authHeader.startsWith(TOKEN_PREFIX) }
      .flatMap<SecurityContext?> { authHeader ->
        val authToken: String = authHeader.substring(7)
        val auth: Authentication = UsernamePasswordAuthenticationToken(authToken, authToken)
        authenticationManager.authenticate(auth).map(::SecurityContextImpl)
      }
  }

//    @Override
//    public Mono load(ServerWebExchange swe) {
//      ServerHttpRequest request = swe.getRequest();
//      String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//      String authToken = null;
//      if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
//        authToken = authHeader.replace(TOKEN_PREFIX, "");
//      }else {
//        logger.warn("couldn't find bearer string, will ignore the header.");
//      }
//      if (authToken != null) {
//        Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
//        return this.authenticationManager.authenticate(auth).map((authentication) -> new SecurityContextImpl(authentication));
//      } else {
//        return Mono.empty();
//      }
//    }


//  fun load2(swe: ServerWebExchange): Mono<SecurityContext>? {
//    return Mono.justOrEmpty(swe.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
//      .filter { authHeader: String -> authHeader.startsWith("Bearer ") }
//      .flatMap(Function<String, Mono<out SecurityContext>> { authHeader: String ->
//        val authToken = authHeader.substring(7)
//        val auth: Authentication = UsernamePasswordAuthenticationToken(authToken, authToken)
//        authenticationManager.authenticate(auth).map { SecurityContextImpl() }
//      })
  
  
  companion object {
    const val TOKEN_PREFIX = "Bearer "
  }
}


