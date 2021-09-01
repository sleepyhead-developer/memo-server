package com.sleepyhead.memo.model

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import javax.persistence.*

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
class User(
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  val id: Int? = null,
  
  @Column(name = "name")
  val name: String,
  
  @Column(name = "uid")
  val uid: String,
  
  @Column(name = "email")
  val email: String,
  
  @Column(name = "photo_url")
  val photoUrl: String,
  
  @Column(name = "creation_time")
  val creationTime: Long,
  
  @Column(name = "last_sign_in_time")
  val lastSignInTime: Long
)

