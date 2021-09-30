package com.sleepyhead.memo.handler

import com.google.firebase.auth.FirebaseAuth
import com.sleepyhead.memo.model.User
import com.sleepyhead.memo.model.security.AuthRequest
import com.sleepyhead.memo.model.security.AuthResponse
import com.sleepyhead.memo.repository.UserRepository
import com.sleepyhead.memo.security.JWTUtil
import com.sleepyhead.memo.security.PBKDF2Encoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty


@Component
class UserHandler {
  
  @Autowired
  lateinit var userRepository: UserRepository
  
  @Autowired
  lateinit var passwordEncoder: PBKDF2Encoder
  
  @Autowired
  lateinit var jwtUtil: JWTUtil
  
  fun login(req:ServerRequest): Mono<ServerResponse> {
    
    val loginRequest= req.bodyToMono(AuthRequest::class.java)
    var loginPassword = ""
    
    return loginRequest.map { login ->
      loginPassword = login.password
      userRepository.findByEmail(login.email)
    }.filter { user -> passwordEncoder.encode(loginPassword) == user.password }
      .flatMap { user ->
        ServerResponse.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(AuthResponse(jwtUtil.doGenerateToken(user))))
//          .body(Mono.just(jwtUtil.doGenerateToken(user)))
          .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
      }
    }
  
  fun getAllUsers(req:ServerRequest): Mono<ServerResponse> {
    return ServerResponse.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(Mono.just(userRepository.findAll()))
  }
  
  fun getUser(req: ServerRequest): Mono<ServerResponse> {
    val uid = req.pathVariable("uid")
    val userRecord = FirebaseAuth.getInstance().getUser(uid)
    
    val user = User(
      name = userRecord.displayName,
      uid = userRecord.uid,
      password = "",
      email = userRecord.email,
      photoUrl = userRecord.photoUrl,
      creationTime = userRecord.userMetadata.creationTimestamp,
      lastSignInTime = userRecord.userMetadata.lastSignInTimestamp
    )
    
    userRepository.save(user)
    
    return ServerResponse.ok()
      .body(Mono.just(user))
      .switchIfEmpty { ServerResponse.notFound().build() }
  }
  
  
}