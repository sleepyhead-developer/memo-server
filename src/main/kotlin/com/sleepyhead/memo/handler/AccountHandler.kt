package com.sleepyhead.memo.handler

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.sleepyhead.memo.dto.LoginDto
import com.sleepyhead.memo.model.Account
import com.sleepyhead.memo.model.security.AuthRequest
import com.sleepyhead.memo.model.security.AuthResponse
import com.sleepyhead.memo.repository.AccountRepository
import com.sleepyhead.memo.security.JWTUtil
import com.sleepyhead.memo.security.PBKDF2Encoder
import com.sleepyhead.memo.util.getAuthorizationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.server.ResponseStatusException
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
  
  fun firebaseAuthenticate(req: ServerRequest): Mono<ServerResponse> {
    val token = getAuthorizationToken(req.headers().firstHeader("Authorization")!!)
    val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
    
    try {
      val newAccount = Account(
        name = decodedToken.name,
        uid = decodedToken.uid,
        email = decodedToken.email,
        photoUrl = decodedToken.picture,
        creationTime = 0L,
        lastSignInTime = 0L,
        pwd = ""
      )
      accountRepository.save(newAccount)
      return ok().contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(LoginDto("Success", "Created User")))
    } catch (e: IllegalArgumentException) {
      ServerResponse.status(500).contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(LoginDto("Failed", "Invalid Token")))
      throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    } catch (e: FirebaseAuthException) {
      ServerResponse.status(500).contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(LoginDto("Failed", "Invalid Token")))
      throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
  }
}


