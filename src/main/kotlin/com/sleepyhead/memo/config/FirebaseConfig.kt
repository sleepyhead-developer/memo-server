package com.sleepyhead.memo.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanInitializationException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import java.io.IOException
import javax.annotation.PostConstruct


@Configuration
class FirebaseConfig{
  
  private val log: Logger = LoggerFactory.getLogger(FirebaseConfig::class.java)

  // Bean이 생성되기 전에 @PostConstruct를 사용하여 firebase.json파일을 읽고 설정
  @PostConstruct
  fun firebaseAuth(): FirebaseAuth {
    try {
      val fis = FileInputStream("src/main/resources/sleepyheads-8ef20-firebase-adminsdk-zj5oj-ee69b8f818.json")
      val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(fis))
        .build()
      FirebaseApp.initializeApp(options)
      return FirebaseAuth.getInstance(FirebaseApp.getInstance())
    } catch (e: IOException) {
      throw BeanInitializationException("Unable to initialize firebase", e)
    }
  }
}