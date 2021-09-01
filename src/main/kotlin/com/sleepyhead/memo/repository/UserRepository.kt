package com.sleepyhead.memo.repository

import com.sleepyhead.memo.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono


interface UserRepository : JpaRepository<User, Long> {
  
  fun findByUid(uid: String): User
}