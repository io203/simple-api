package com.example.simpleapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleapi.model.Simple;
import com.example.simpleapi.service.SimplePublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PubsubController {
    private final SimplePublisher publisher;

    @PostMapping("/simple-sub")
	public Mono<String> getSub(@RequestBody(required = false) byte[] body) {
        return Mono.fromRunnable(() ->
                log.info("===spring boot ====Received Message: " + new String(body)));
    }
    

    @GetMapping("/pub")
    public String publish(){
        Simple simple = new Simple(1, "test1","tewst1");

        publisher.publish(simple);
        return "publish success";

    }
}
    
