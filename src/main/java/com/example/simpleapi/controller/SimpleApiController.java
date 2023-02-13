package com.example.simpleapi.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleapi.model.Simple;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@Slf4j
public class SimpleApiController {
	@GetMapping("/hello")
	public Mono<String> hello() {
		
		log.info("==========simple-api home()");
		
		return Mono.just("hello world");
		
	}
	@GetMapping("/simple")
	public Flux<List<Simple>> listSimple(){
		List<Simple> list = new ArrayList<>();
		
		for(int i=0 ; i< 10;i++) {
			list.add(new Simple(i+1,"test-"+i, "contents-"+i));
			log.info("for "+i);
		}
		log.info(list.toString());
		return Flux.just(list);
		
	}
	
	@GetMapping("/version")
	public Mono<String> version(){
		log.info("version 1.0");
		
		return Mono.just("version 1.0");
		
	}
	
}

