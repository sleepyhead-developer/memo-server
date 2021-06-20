package com.sleepyhead.memo.handler

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

@Component
class MemoHandler {
  
  fun getTest(req: ServerRequest): Mono<ServerResponse> = ServerResponse.ok()
//    .contentType(MediaType.APPLICATION_JSON)
    .body<String>(Mono.just("pong"))
    .switchIfEmpty(ServerResponse.notFound().build())
}