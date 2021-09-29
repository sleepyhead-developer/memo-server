package com.sleepyhead.memo.config

import com.sleepyhead.memo.handler.MemoHandler
import com.sleepyhead.memo.handler.UserHandler
import org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder
import org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router


@Component
class RouterConfig(private val memoHandler: MemoHandler, private val userHandler: UserHandler) {
  
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
  fun userRouter() = nest(path("/user"),
    router {
      listOf(
        GET("/{uid}", userHandler::getUser),
        POST("/login", userHandler::login)
      )
    }
  )
//  @Bean
//  fun memoAppRoutes(userHandler: UserHandler) : RouterFunction<ServerResponse>{
//    return RouterFunctions
//      .route(GET("/user/{uid}").and(accept(MediaType.APPLICATION_JSON)), userHandler::getUser)
//      .andRoute(GET("/user/{uid}").and(accept(MediaType.APPLICATION_JSON)), userHandler::getUser)
//  }

}