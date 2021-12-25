package com.example.simpleapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class BindingController {
    @PostMapping("/onevent")
	public Mono<String> onevent(@RequestBody(required = false) byte[] body) {
        return Mono.fromRunnable(() ->
                log.info("Received Message: " + new String(body)));
    }
    
}
