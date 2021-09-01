package com.sleepyhead.memo.handler

import com.sleepyhead.memo.model.Memo
import com.sleepyhead.memo.model.User
import com.sleepyhead.memo.repository.MemoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.function.Function

@Component
class MemoHandler {
  
  @Autowired
  lateinit var memoRepository: MemoRepository
  
  val notFound: Mono<ServerResponse> = ServerResponse.notFound().build()
  
  fun getAllMemos(req: ServerRequest): Mono<ServerResponse> {
    return ServerResponse.ok()
      .contentType(MediaType.APPLICATION_JSON)
//      .body<Memo>(memoRepository.findAll())
      .body(Mono.just(memoRepository.findAll()))
  }
  
  fun getMemo(req: ServerRequest): Mono<ServerResponse> {
    val id = req.pathVariable("id").toInt()
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
      .body(Mono.just(memoRepository.findById(id)))
      .switchIfEmpty(notFound)
  }
  
  fun saveMemo(req: ServerRequest): Mono<ServerResponse> {
    val memoWrapper = req.bodyToMono(Memo::class.java)
    return memoWrapper.flatMap { memo ->
      ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(memoRepository.save(memo)))
    }
  }

  fun updateMemo(req: ServerRequest): Mono<ServerResponse> {
    val id = req.pathVariable("id").toInt()
    val newMemo = req.bodyToMono(Memo::class.java)
    val item = Mono.just(memoRepository.findById(id))
    
    val updatedMemo: Mono<Memo> = newMemo.flatMap { new ->
      item.flatMap { old ->
          old.title = new.title
          old.contents = new.contents
          old.creationTime = new.creationTime
          Mono.just(memoRepository.save(old))
      }
    }
    return updatedMemo.flatMap {
      ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .bodyValue(it)
        .switchIfEmpty(notFound)
    }
  }

  
  fun deleteMemo(req: ServerRequest): Mono<ServerResponse> {
    val id = req.pathVariable("id").toInt()
    val deleteMemo = memoRepository.deleteById(id)
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(deleteMemo, Void::class.java)
  }
  
  
//  fun getTest(req: ServerRequest): Mono<ServerResponse> = ServerResponse.ok()
//    .body<String>(Mono.just("pong"))
//    .switchIfEmpty(ServerResponse.notFound().build())

  
}
