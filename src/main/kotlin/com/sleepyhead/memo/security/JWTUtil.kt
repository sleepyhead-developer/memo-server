package com.sleepyhead.memo.security

import com.sleepyhead.memo.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*


@Component
class JWTUtil (
  
  @Value("\${com.sleepyhead.jjwt.secret}")
  val secret: String,

  @Value("\${com.sleepyhead.jjwt.expiration}")
  val expirationTime: String,
  
  @Value("\${com.sleepyhead.name}")
  val name:String
) {
  
  private var key: Key ?= null
  /*- 객체의 초기화 부분
  - 객체가 생성된 후 별도의 초기화 작업을 위해 실행하는 메소드를 선언한다.
  - @PostConstruct 어노테이션을 설정해놓은 init 메소드는 WAS가 띄워질 때 실행된다.*/
  
  init {
    key = Keys.hmacShaKeyFor(secret.toByteArray())
  }
  
  fun getAllClaimsFromToken(token: String): Claims {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
  }
  
//  fun getUsernameFromToken(token: String): String {
//    return getAllClaimsFromToken(token).subject
//  }
  
  fun getUserEmailFromToken(token: String): String {
    return getAllClaimsFromToken(token).subject
  }
  
  fun getExpirationDateFromToken(token: String): Date {
    return getAllClaimsFromToken(token).expiration
  }
  
  fun isTokenExpired(token: String): Boolean {
    val expiration = getExpirationDateFromToken(token)
    return expiration.before(Date())
  }
  
//  fun generateToken(user: User): String {
//    val claims: MutableMap<String, Any> = HashMap()
//    claims["role"] = user.getRoles()
//    return doGenerateToken(claims, user.name);
//  }
  
  fun doGenerateToken(user: User): String {
    val expirationTimeLong: Long = expirationTime!!.toLong()
    val createdDate = Date()
    val expirationDate = Date(createdDate.time + (expirationTimeLong.times(1000)))
    return Jwts.builder()
      .claim("uid", user.uid)
      .setSubject(user.email)
      .setIssuedAt(createdDate)
      .setExpiration(expirationDate)
      .signWith(key)
      .compact()
  }
  
  fun validateToken(token: String): Boolean {
    return !isTokenExpired(token)
  }
  
}