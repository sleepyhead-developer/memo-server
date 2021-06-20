package com.sleepyhead.memo.router

import com.sleepyhead.memo.handler.MemoHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Component
class MemoRouter(private val handler: MemoHandler) {
  
  @Bean
  fun routerFunction() = nest(path("/"),
    router {
      listOf(
        GET("/ping", handler::getTest)
      )
    }
  )
}