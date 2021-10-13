package com.sleepyhead.memo.model

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import javax.persistence.*

@Entity
@AllArgsConstructor
@Getter
@Setter
class Role(
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  val id: Long = 0L,
  
  @Column(name = "name")
  val name: String,
  
  @ManyToOne(fetch = FetchType.EAGER)
  val account: Account
)