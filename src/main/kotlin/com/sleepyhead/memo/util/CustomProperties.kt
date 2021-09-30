package com.sleepyhead.memo.util

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("com.sleepyhead")
class CustomProperties {
  var name:String? = "100"
}