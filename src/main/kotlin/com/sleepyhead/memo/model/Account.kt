package com.sleepyhead.memo.model

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.AllArgsConstructor
import lombok.ToString
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors
import javax.persistence.*


@Entity
@ToString
@AllArgsConstructor
class Account(
  
  @Id
  @GeneratedValue
  val id: Int? = null,
  
  val name: String,
  
  val uid: String,
  
  val email: String,
  
  @Column(name = "password")
  val pwd: String,
  
  val photoUrl: String,
  
  val creationTime: Long,
  
  val lastSignInTime: Long,
  
  /* TODO lazily initialize failed */
  @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
  @JsonIgnore
  var role: MutableSet<Role> = mutableSetOf()
) : UserDetails {
  
  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return role.stream().map { authority -> SimpleGrantedAuthority(authority.name) }.collect(Collectors.toList())
  }
  
  override fun getPassword(): String {
    return pwd
  }
  
  override fun getUsername(): String {
    return email
  }
  
  override fun isAccountNonExpired(): Boolean {
    return false
  }
  
  override fun isAccountNonLocked(): Boolean {
    return false
  }
  
  override fun isCredentialsNonExpired(): Boolean {
    return false
  }
  
  override fun isEnabled(): Boolean {
    return false
  }
  
}


