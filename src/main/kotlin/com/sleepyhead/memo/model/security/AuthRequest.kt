package com.sleepyhead.memo.model.security

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor


data class AuthRequest (
  val userEmail: String,
  val password: String
)