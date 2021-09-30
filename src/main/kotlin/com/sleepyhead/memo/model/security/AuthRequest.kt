package com.sleepyhead.memo.model.security

data class AuthRequest(
  val email: String,
  val password: String
) {
}