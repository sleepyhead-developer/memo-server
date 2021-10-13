package com.sleepyhead.memo.repository

import com.sleepyhead.memo.model.Account
import org.springframework.data.jpa.repository.JpaRepository


interface AccountRepository : JpaRepository<Account, Long> {
  fun findByEmailOrEmailNull(email: String): Account?
}