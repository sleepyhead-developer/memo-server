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
@Table(name = "memo")
class Memo (
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  val id: Int? = null,

  @Column(name = "user_id")
  val userId: Int,
  
  @Column(name = "title")
  var title: String,
  
  @Column(name = "contents")
  var contents: String,
  
  @Column(name = "creation_time")
  var creationTime: Long
  
 )
{

}