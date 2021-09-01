package com.sleepyhead.memo.handler

import com.google.firebase.auth.FirebaseAuth
import com.sleepyhead.memo.model.Memo
import com.sleepyhead.memo.model.User
import com.sleepyhead.memo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class UserHandler {
  
  @Autowired
  lateinit var userRepository: UserRepository
  
  
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
  
  fun createUser(@RequestBody user: User): User {
    return userRepository.save(user)
  }
  
  
}