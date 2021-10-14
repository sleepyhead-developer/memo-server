package com.sleepyhead.memo.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono

@Configuration
class SimpleTokenMatchingAuthenticationManager (
  var users: MutableMap<String, UsernamePasswordAuthenticationToken> ?=null
): ReactiveAuthenticationManager {
  override fun authenticate(authentication: Authentication): Mono<Authentication> {
    println("simple token matching authentication manager-------------")
    val authToken = authentication.credentials.toString()
    
    return if (users?.containsKey(authToken) == true) {
      Mono.just(users!![authToken] as UsernamePasswordAuthenticationToken)
    } else Mono.empty()
  }
}