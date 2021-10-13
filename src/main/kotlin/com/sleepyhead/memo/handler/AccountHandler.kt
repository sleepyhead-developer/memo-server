package com.sleepyhead.memo.handler

import com.google.firebase.auth.FirebaseAuth
import com.sleepyhead.memo.model.Message
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
    println("login --------------------")
    val loginRequest = req.bodyToMono(AuthRequest::class.java)
    val notFound = ServerResponse.notFound().build()
    println("loginRequest $loginRequest")
    
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
//        ).switchIfEmpty(ok().body(BodyInserters.fromValue(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())))
        ).switchIfEmpty(Mono.error(NotFoundException("recipe not found")))
//    }.switchIfEmpty(Mono.just(ServerResponse.status(HttpStatus.NOT_FOUND).build()).flatMap { it })
    }.switchIfEmpty(ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(AuthResponse("404!!!"))))

    
    /*return loginRequest.mapNotNull { login ->
      loginPassword = login.password
      accountRepository.findByEmailOrEmailNull(login.email)
    }.filter { user -> passwordEncoder.encode(loginPassword) == user?.password }
      .flatMap { user ->
        user?.let {
          jwtUtil.generateToken(it)
        }?.let {
          AuthResponse(it)
        }?.let {
          BodyInserters.fromValue(it)
        }?.let {
          ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(it)
            .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
        }
      }.switchIfEmpty(ok().body(Mono.just(ServerResponse.status(HttpStatus.NOT_FOUND))))*/
    
    
  }


//  fun googleLogin(req: ServerRequest): Mono<ServerResponse> {
//    println("jaeun -> google login function")
////    val request = req.bodyToMono(OAuthRequest::class.java)
//    return Mono.justOrEmpty(req.headers().firstHeader(HttpHeaders.AUTHORIZATION))
//      .filter { header -> header.startsWith(TOKEN_PREFIX)}
//      .flatMap { authHeader ->
//        println("jaeun authHeader -> $authHeader")
//        val token = authHeader.substring(7)
//        val decodeToken = firebaseAuth.verifyIdToken(token)
//        val auth: Authentication = UsernamePasswordAuthenticationToken( decodeToken, decodeToken)
//        SecurityContextHolder.getContext().authentication
//        ServerResponse.ok()
//          .contentType(MediaType.APPLICATION_JSON)
//          .body(auth.toMono())
//      }
//  }

//  fun OAuth(req: ServerRequest): Mono<ServerResponse> {
//     return Mono.justOrEmpty(req.headers().firstHeader(HttpHeaders.AUTHORIZATION))
//       .filter { header -> header.startsWith(TOKEN_PREFIX) }
//       .flatMap { authHeader ->
//         val token = authHeader.substring(7)
//         val decodedToken = firebaseAuth.verifyIdToken(token)
//         val user = User(
//           name = decodedToken.name,
//           email = decodedToken.email,
//           uid = decodedToken.uid,
//           password = "",
//           photoUrl = decodedToken.picture,
//           creationTime = System.currentTimeMillis(),
//           lastSignInTime = System.currentTimeMillis()
//         )
//         val user:User = userRepository.findByUid(decodedToken.uid)
//         val auth: Authentication = UsernamePasswordAuthenticationToken( user, null)
//         ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
//           .body(Mono.just(userRepository.save(user)))
//       }
//  }

//  fun getAllUsers(req: ServerRequest): Mono<ServerResponse> {
//    println("getaaaaaaaaaalllusers")
//    return ServerResponse.ok()
//      .contentType(MediaType.APPLICATION_JSON)
//      .body(Mono.just(userRepository.findAll()))
//  }

//  fun getUser(req: ServerRequest): Mono<ServerResponse> {
//    val uid = req.pathVariable("uid")
//    val userRecord = FirebaseAuth.getInstance().getUser(uid)
//
//    val user = User(
//      name = userRecord.displayName,
//      uid = userRecord.uid,
//      pwd = "",
//      email = userRecord.email,
//      photoUrl = userRecord.photoUrl,
//      roles = userRecord.,
//      creationTime = userRecord.userMetadata.creationTimestamp,
//      lastSignInTime = userRecord.userMetadata.lastSignInTimestamp
//    )
//
//    userRepository.save(user)
//
//    return ServerResponse.ok()
//      .body(Mono.just(user))
//      .switchIfEmpty { ServerResponse.notFound().build() }
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


