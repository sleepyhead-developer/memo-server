package com.sleepyhead.memo.model

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import javax.persistence.*

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class Memo (
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Int? = null,
  
  val accountId: Int,
  
  var title: String,
  
  var contents: String,
  
  var creationTime: Long
  
 )
