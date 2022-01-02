package com.example.simpleapi.controller;

import com.example.simpleapi.service.SimpleService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SimpleController {
    private final SimpleService service;

    @GetMapping("/hello2")
    public Mono<String> hello2() {
        return  Mono.just(service.hello2());
    }
    
    
}
