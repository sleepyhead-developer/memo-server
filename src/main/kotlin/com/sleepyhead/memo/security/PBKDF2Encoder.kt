package com.sleepyhead.memo.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


@Component
class PBKDF2Encoder: PasswordEncoder {
  
  @Value("\${springboot.password.encoder.secret}")
  private val secret: String? = null
  
  @Value("\${springboot.password.encoder.iteration}")
  private val iteration: Int = 0
  
  @Value("\${springboot.password.encoder.keylength}")
  private val keyLength: Int = 0
  
  override fun encode(rawPassword: CharSequence?): String {
    return try {
      val result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
        .generateSecret(PBEKeySpec(rawPassword.toString().toCharArray(), secret!!.toByteArray(), iteration, keyLength))
        .encoded
      Base64.getEncoder().encodeToString(result)
    } catch (ex: NoSuchAlgorithmException) {
      throw RuntimeException(ex)
    } catch (ex: InvalidKeySpecException) {
      throw RuntimeException(ex)
    }
  }
  
  override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
    return encode(rawPassword) == encodedPassword
//    return encode(rawPassword).equals(encodedPassword)
  }
}