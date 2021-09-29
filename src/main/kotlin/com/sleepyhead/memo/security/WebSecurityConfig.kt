package com.sleepyhead.memo.security

import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig {
  
  @Autowired
  lateinit var authenticationManager: AuthenticationManager
  
  @Autowired
  lateinit var securityContextRepository: SecurityContextRepository
  
  @Bean
  fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http
      .exceptionHandling()
      .authenticationEntryPoint { exchange, ex ->
        Mono.fromRunnable { exchange.response.statusCode = HttpStatus.UNAUTHORIZED }
      }.accessDeniedHandler { exchange, denied ->
        Mono.fromRunnable { exchange.response.statusCode = HttpStatus.FORBIDDEN }
      }
      .and()
      .csrf().disable()
      .formLogin().disable()
      .httpBasic().disable()
      
      .authenticationManager(authenticationManager)
      .securityContextRepository(securityContextRepository)
      
      .authorizeExchange()
      
      .pathMatchers("/login").permitAll()
      .anyExchange().authenticated()
      .and()
      .build()
    
  }
}
