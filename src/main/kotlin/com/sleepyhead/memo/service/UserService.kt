package com.sleepyhead.memo.service

import com.sleepyhead.memo.model.User
import com.sleepyhead.memo.repository.UserRepository
import javassist.bytecode.DuplicateMemberException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Service
class UserService {
  
  @Autowired
  lateinit var userRepository: UserRepository
  
  
  @PostConstruct
  fun init() {
    userRepository.save(User(email = "user1@sleepy.com", password = "1234", id = null, name = "jenny", uid="", photoUrl = "www", creationTime = 0L, lastSignInTime = 0L))
    userRepository.save(User(email = "user2@sleepy.com", password = "1111", name = "lcyer", uid = "", photoUrl = "jjj", creationTime = 0L, lastSignInTime = 0L))
  }

//  @Autowired
//  lateinit var userRepository: UserRepository

//  fun findByUserName(userName: String): Mono<User> {
//    return Mono.justOrEmpty()
//  }
}