package com.sleepyhead.memo.service

import com.sleepyhead.memo.model.User
import com.sleepyhead.memo.repository.UserRepository
import javassist.bytecode.DuplicateMemberException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserService {
  
  @Autowired
  lateinit var userRepository: UserRepository
  
  
//  @PostConstruct
//  fun init() {
////    val data = HashMap<Any, Any>()
//
//    //username:passwowrd -> user:user
//    User(10, "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", "abcd", "user1@sleepy.com",""))
//
//    //username:passwowrd -> admin:admin
//
//      User(11, "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=", true, Arrays.asList(Role.ROLE_ADMIN))
//    )
//  }

//  @Autowired
//  lateinit var userRepository: UserRepository

//  fun findByUserName(userName: String): Mono<User> {
//    return Mono.justOrEmpty()
//  }

}