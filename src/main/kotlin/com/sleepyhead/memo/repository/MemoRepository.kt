package com.sleepyhead.memo.repository

import com.sleepyhead.memo.model.Memo
import org.springframework.data.jpa.repository.JpaRepository
import reactor.core.publisher.Mono

interface MemoRepository: JpaRepository<Memo, Long> {
  
  fun findById(id: Int): Memo
  fun deleteById(id: Int) : Void
  
}