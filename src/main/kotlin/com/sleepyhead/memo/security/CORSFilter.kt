package com.sleepyhead.memo.security

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer


@Configuration
@EnableWebFlux
class CORSFilter: WebFluxConfigurer {
  
  override fun addCorsMappings(registry: CorsRegistry) {
    registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
  }
}