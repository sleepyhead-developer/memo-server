package com.sleepyhead.memo.handler

import com.google.firebase.auth.FirebaseAuth
import com.sleepyhead.memo.dto.LoginDto
import com.sleepyhead.memo.model.Message
import com.sleepyhead.memo.model.OauthRequest
import com.sleepyhead.memo.model.security.AuthRequest
import com.sleepyhead.memo.model.security.AuthResponse
import com.sleepyhead.memo.repository.AccountRepository
import com.sleepyhead.memo.security.JWTUtil
import com.sleepyhead.memo.security.PBKDF2Encoder
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
@Transactional
class AccountHandler {
  
  @Autowired
  lateinit var accountRepository: AccountRepository
  
  @Autowired
  lateinit var passwordEncoder: PBKDF2Encoder
  
  @Autowired
  lateinit var jwtUtil: JWTUtil
  
//  @Autowired
//  lateinit var firebaseAuth: FirebaseAuth
  
  fun login(req: ServerRequest): Mono<ServerResponse> {
    val loginRequest = req.bodyToMono(AuthRequest::class.java)
    var loginPassword = ""
    return loginRequest.mapNotNull { loginInfo ->
      loginPassword = loginInfo.password
      accountRepository.findByEmailOrEmailNull(loginInfo.email)
    }.filter {
      it != null && passwordEncoder.encode(loginPassword) == it.password
    }.flatMap { account ->
      val resultAccount = account!!
      ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
          BodyInserters.fromValue(
            AuthResponse(
              jwtUtil.generateToken(resultAccount)
            )
          )
        ).switchIfEmpty(
          ServerResponse.status(401).contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(LoginDto("failed", "expired")))
        )
    }.switchIfEmpty(
      ServerResponse.status(404).contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(LoginDto("failed", "user not found")))
    )
  }
  
//  fun googleLogin(req: ServerRequest): Mono<ServerResponse> {
//    val loginRequest = req.bodyToMono(OauthRequest::class.java)
//    var loginPassword = ""
//    return loginRequest.mapNotNull { loginInfo ->
//      loginPassword = loginInfo.password
//      accountRepository.findByEmailOrEmailNull(loginInfo.email)
//    }.filter {
//      it != null && passwordEncoder.encode(loginPassword) == it.password
//    }.flatMap { account ->
//      val resultAccount = account!!
//      ok()
//        .contentType(MediaType.APPLICATION_JSON)
//        .body(
//          BodyInserters.fromValue(
//            AuthResponse(
//              jwtUtil.generateToken(resultAccount)
//            )
//          )
//        ).switchIfEmpty(
//          ServerResponse.status(401).contentType(MediaType.APPLICATION_JSON)
//            .body(BodyInserters.fromValue(LoginDto("failed", "expired")))
//        )
//    }.switchIfEmpty(
//      ServerResponse.status(404).contentType(MediaType.APPLICATION_JSON)
//        .body(BodyInserters.fromValue(LoginDto("failed", "user not found")))
//    )
//  }
  
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  fun admin(req: ServerRequest): Mono<ServerResponse> {
    println("admiiin")
    return ServerResponse.ok()
      .body(Mono.just(Message("contents for admin")))
      .switchIfEmpty(Mono.empty())
  }
  
  @PreAuthorize("hasRole('ROLE_USER')")
  fun user(req: ServerRequest): Mono<ServerResponse> {
    println("user!!!")
    return ServerResponse.ok()
      .body(Mono.just(Message("contents for user")))
      .switchIfEmpty(Mono.empty())
  }
}


