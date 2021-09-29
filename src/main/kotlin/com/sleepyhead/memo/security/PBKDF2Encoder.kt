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
class PBKDF2Encoder (
  
  @Value("\${com.sleepyhead.password.encoder.secret}")
  val secret: String,
  
  @Value("\${com.sleepyhead.password.encoder.iteration}")
  val iteration: Int,
  
  @Value("\${com.sleepyhead.password.encoder.keylength}")
  val keyLength: Int
 ): PasswordEncoder
{
  override fun encode(rawPassword: CharSequence?): String {
    return try {
      val result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
        .generateSecret(PBEKeySpec(rawPassword.toString().toCharArray(), secret.toByteArray(), iteration, keyLength))
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