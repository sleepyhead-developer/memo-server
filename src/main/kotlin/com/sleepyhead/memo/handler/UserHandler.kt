package com.sleepyhead.memo.handler

import com.google.firebase.auth.FirebaseAuth
import com.sleepyhead.memo.model.Memo
import com.sleepyhead.memo.model.User
import com.sleepyhead.memo.model.security.AuthRequest
import com.sleepyhead.memo.model.security.AuthResponse
import com.sleepyhead.memo.repository.UserRepository
import com.sleepyhead.memo.security.JWTUtil
import com.sleepyhead.memo.security.PBKDF2Encoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono


@Component
class UserHandler {
  
  @Autowired
  lateinit var userRepository: UserRepository
  
  @Autowired
  lateinit var passwordEncoder: PBKDF2Encoder
  
  @Autowired
  lateinit var jwtUtil: JWTUtil
  
  
//  fun login(@RequestBody ar: AuthRequest): Mono<ServerResponse> {
//    return userRepository.findByEmail(ar.userEmail)
//      .filter { user -> passwordEncoder.encode(ar.password) == user.password }
//      .flatMap { user ->
//        ServerResponse.ok()
//          .body<ServerResponse>(jwtUtil.doGenerateToken(user))
//          .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
//      }
//  }
  
  fun login(req: ServerRequest): Mono<ServerResponse> {
    val userEmail = req.pathVariable("email")
    val password = req.pathVariable("password")
    return userRepository.findByEmail(userEmail).toMono()
      .filter { user -> passwordEncoder.encode(password) == user.password }
      .flatMap { user ->
        ServerResponse.ok()
          .body<ServerResponse>(jwtUtil.doGenerateToken(user))
          .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
      }
  }
  
  fun getAllUsers(): Mono<ServerResponse> {
    return ServerResponse.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body<Memo>(userRepository.findAll())
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