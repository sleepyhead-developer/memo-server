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
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import reactor.core.publisher.Mono
import java.io.FileInputStream
import java.io.IOException


@Configuration
class FirebaseConfig(
  @Value("\${firebase.service.account.id}")
  var firebaseAccountId:String
) : ReactiveAuthenticationManager, InitializingBean {
  
  private val log: Logger = LoggerFactory.getLogger(FirebaseConfig::class.java)
  
  private var userDetailsService: UserDetailsService? = null
  
  override fun authenticate(authentication: Authentication): Mono<Authentication> {
    val authToken = authentication.credentials.toString()
    return try {
      val decodedToken = FirebaseAuth.getInstance().verifyIdToken(authToken)
      val claims: MutableMap<String, Any> = HashMap()
      claims["user"] = true
      FirebaseAuth.getInstance().setCustomUserClaims(decodedToken.uid, claims)
      val userId = decodedToken.uid
      val user: UserDetails? = userDetailsService?.loadUserByUsername(decodedToken.uid)

      val auth = UsernamePasswordAuthenticationToken(
        userId,
        null,
        user?.authorities
      )
      auth.details = getDetailsForToken(decodedToken)
      Mono.just(auth)
    } catch (e: FirebaseAuthException) {
      log.error("Firebase Auth Exception", e)
      Mono.empty()
    } catch (e: IllegalArgumentException) {
      log.error("IllegalArgumentException", e)
      Mono.empty()
    }
  }
  
  override fun afterPropertiesSet() {
    try {
      if (FirebaseApp.getApps().size == 0) {
        val serviceAccount = FileInputStream("src/main/resources/sleepyheads-8ef20-firebase-adminsdk-zj5oj-ee69b8f818.json")
        val options = FirebaseOptions.Builder()
          .setServiceAccountId(firebaseAccountId)
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