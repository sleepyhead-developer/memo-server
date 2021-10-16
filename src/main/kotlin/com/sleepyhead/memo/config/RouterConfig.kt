package com.sleepyhead.memo.config

import com.sleepyhead.memo.handler.MemoHandler
import com.sleepyhead.memo.handler.AccountHandler
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router


@Component
class RouterConfig(private val memoHandler: MemoHandler, private val accountHandler: AccountHandler) {
  
  @Bean
  fun routerFunction() = nest(path("/memo"),
    router {
      listOf(
        GET("/{id}", memoHandler::getMemo),
        GET(memoHandler::getAllMemos),
        POST(memoHandler::saveMemo),
        PUT("/{id}", memoHandler::updateMemo),
        DELETE("/{id}", memoHandler::deleteMemo)
      )
    }
  )
  

  @Bean
  fun accountRouter() = nest(path("/user"),
    router {
      listOf(
//        GET("/{uid}", userHandler::getUser),
        POST("/authenticate", accountHandler::firebaseAuthenticate),
      )
    }
  )
  
  @Bean
  fun memoAppRoutes(accountHandler: AccountHandler) : RouterFunction<ServerResponse> {
    return RouterFunctions
      .route(POST("/login").and(accept(MediaType.APPLICATION_JSON)), accountHandler::login)
  }

}