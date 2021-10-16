package com.sleepyhead.memo.service

import com.sleepyhead.memo.model.Account
import com.sleepyhead.memo.model.Role
import com.sleepyhead.memo.repository.RoleRepository
import com.sleepyhead.memo.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Service
class AccountService {
  
  @Autowired
  lateinit var userRepository: AccountRepository
  
  @Autowired
  lateinit var roleRepository: RoleRepository
  
  @PostConstruct
  fun init() {
    
    val ac = Account(
      id = 1,
      email = "user1@sleepy.com",
      pwd = "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=",
      name = "jenny",
      uid = "",
      photoUrl = "www",
      creationTime = 0L,
      lastSignInTime = 0L
    )
  
    val role = Role(
      name = "user",
      id = 0L,
      account = ac
      )
    
    ac.role.add(role)
    userRepository.save(ac)
    roleRepository.save(role)


//    userRepository.save(
//      Account(
//        id = 2,
//        email = "user2@sleepy.com",
//        pwd = "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=",
//        name = "lcyer",
//        uid = "",
//        photoUrl = "jjj",
//        creationTime = 0L,
//        lastSignInTime = 0L,
//        roles = mutableListOf(Role(name = "ROLE_ADMIN", id = 0L))
//      )
//    )
  }
}