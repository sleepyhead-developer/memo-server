package com.sleepyhead.memo.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono


@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
  
  @Bean
fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
  return http.authorizeExchange()
    .pathMatchers("/**").permitAll()
    .pathMatchers(HttpMethod.OPTIONS).permitAll()
    .anyExchange().authenticated()
    .and()
    .httpBasic().disable()
    .cors().disable()
    .exceptionHandling().authenticationEntryPoint { exchange, ex ->
      Mono.fromRunnable {
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
      }
    }
    .accessDeniedHandler { exchange, denied ->
      Mono.fromRunnable {
        exchange.response.statusCode = HttpStatus.FORBIDDEN
      }
    }
    .and()
    .csrf().disable()
    .formLogin().disable()
    .logout().disable()
    .build()
}

}