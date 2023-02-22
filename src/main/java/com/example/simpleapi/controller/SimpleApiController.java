package com.example.simpleapi.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleapi.model.Simple;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class SimpleApiController {
	private final Tracer tracer;
	@GetMapping("/hello")
	public Mono<String> hello() {
		
		log.info("==========simple-api home()");
		
		return loggingTrace(Mono.just("hello world"));
		
	}
	@GetMapping("/simple")
	public Flux<Simple> listSimple(){
		List<Simple> list = new ArrayList<>();
		
		for(int i=0 ; i< 10;i++) {
			list.add(new Simple(i+1,"test-"+i, "contents-"+i));
			
		}
		log.info(list.toString());
		return loggingTrace(Flux.fromIterable(list));
		
	}
	
	@GetMapping("/version")
	public Mono<String> version(){
		log.info("version 1.0");
		
		return loggingTrace(Mono.just("version 1.0"));
		
	}

	// console log에 traceid 노출하기 위해서( 옵션사항)
	private Mono<String> loggingTrace(Mono<String> data){
		return Mono.deferContextual(contextView -> {
            try (ContextSnapshot.Scope scope = ContextSnapshot.setThreadLocalsFrom(contextView, ObservationThreadLocalAccessor.KEY)) {
				//log 출력이 있어야 traceid가 콘솔에 노출된다 
				log.info("====== Traceid: {}", tracer.currentSpan().context().traceId());
				return data;
			}
		});
	}
	// console log에 traceid 노출하기 위해서( 옵션사항)
	private Flux<Simple> loggingTrace(Flux<Simple> data){
		return Flux.deferContextual(contextView -> {
            try (ContextSnapshot.Scope scope = ContextSnapshot.setThreadLocalsFrom(contextView, ObservationThreadLocalAccessor.KEY)) {
				//log 출력이 있어야 traceid가 콘솔에 노출된다
				log.info("====== Traceid: {}", tracer.currentSpan().context().traceId());
				return data;
			}
		});
	}
	
}

