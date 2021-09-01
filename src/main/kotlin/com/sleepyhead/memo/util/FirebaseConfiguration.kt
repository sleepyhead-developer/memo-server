package com.sleepyhead.memo.util

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.FileInputStream
import javax.annotation.PostConstruct


@Configuration
class FirebaseConfiguration {

  lateinit var firebaseApp: FirebaseApp
  
  // Bean이 생성되기 전에 @PostConstruct를 사용하여 firebase.json파일을 읽고 설정
  @PostConstruct
  fun init(): FirebaseApp {
    val fis = FileInputStream("src/main/resources/sleepyheads-8ef20-firebase-adminsdk-zj5oj-ee69b8f818.json")
    val options = FirebaseOptions.Builder()
      .setCredentials(GoogleCredentials.fromStream(fis))
      .build()
    return FirebaseApp.initializeApp(options)
  }

}