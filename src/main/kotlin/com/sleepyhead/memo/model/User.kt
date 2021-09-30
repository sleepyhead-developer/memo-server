package com.sleepyhead.memo.model

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "user")
class User(
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @JsonIgnore
  val id: Int? = null,
  
  @JsonIgnore
  @Column(name = "name")
  val name: String,
  
  @JsonIgnore
  @Column(name = "uid")
  val uid: String,
  
  @Column(name = "email")
  val email: String,

  @JsonIgnore
  @Column(name="password")
  val password: String,
  
  @Column(name = "photo_url")
  val photoUrl: String,
  
  @Column(name = "creation_time")
  val creationTime: Long,
  
  @Column(name = "last_sign_in_time")
  val lastSignInTime: Long
)


