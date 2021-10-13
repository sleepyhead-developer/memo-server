package com.sleepyhead.memo.security

import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@AllArgsConstructor
@EnableWebFluxSecurity
class WebSecurityConfig (
  
  @Autowired
  private val authenticationManager: AuthenticationManager,
  
  @Autowired
  private val securityContextRepository: SecurityContextRepository
 ){
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
      .pathMatchers(HttpMethod.OPTIONS).permitAll()
      .pathMatchers("/login").permitAll()
      .anyExchange().authenticated()
      .and()
      .build()
    
  }
}
