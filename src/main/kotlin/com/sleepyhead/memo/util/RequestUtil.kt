package com.sleepyhead.memo.util

import com.sleepyhead.memo.security.SecurityContextRepository.Companion.TOKEN_PREFIX

fun getAuthorizationToken(header: String): String {
  header.replace(TOKEN_PREFIX, "");
  if (!header.startsWith(TOKEN_PREFIX)) {
    throw IllegalArgumentException("Invalid authorization header");
  }
  val parts:Array<String> = header.split(" ").toTypedArray()
  if (parts.size != 2) {
    throw IllegalArgumentException("Invalid authorization header");
  }
  return parts[1]
}
