package com.sleepyhead.memo.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanInitializationException
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import reactor.core.publisher.Mono
import java.io.FileInputStream
import java.io.IOException
import java.util.stream.Collectors
import javax.annotation.PostConstruct
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService


@Configuration
class FirebaseConfig: ReactiveAuthenticationManager, InitializingBean {
  
  private val log: Logger = LoggerFactory.getLogger(FirebaseConfig::class.java)
  
  private var userDetailsService: UserDetailsService ?= null

  override fun authenticate(authentication: Authentication): Mono<Authentication> {
    val authToken = authentication.credentials.toString()
    return try {
      val decodedToken = FirebaseAuth.getInstance().verifyIdToken(authToken)
      val userId = decodedToken.uid
      val user: UserDetails? = userDetailsService?.loadUserByUsername(decodedToken.uid)
//      val authorities: List<String> = getAuthoritiesForToken(decodedToken)
      val auth = UsernamePasswordAuthenticationToken(
        userId,
        null,
      user?.authorities
      )
      auth.details = getDetailsForToken(decodedToken)
      Mono.just(auth)
    } catch (e: FirebaseAuthException) {
      Mono.empty()
    } catch (e: IllegalArgumentException) {
      Mono.empty()
    }
  }
  
  override fun afterPropertiesSet() {
    try {
      if (FirebaseApp.getApps().size == 0) {
        val serviceAccount = FileInputStream("src/main/resources/sleepyheads-8ef20-firebase-adminsdk-zj5oj-ee69b8f818.json")
        val options = FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build()
        FirebaseApp.initializeApp(options)
        log.info("Firebase initialized")
      }
    } catch (e: IOException) {
      throw BeanInitializationException("Unable to initialize firebase", e)
    }
  }

  protected fun getDetailsForToken(token: FirebaseToken): Any {
    return token
  }
}