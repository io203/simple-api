package com.example.simpleapi;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.Mono;
 

@SpringBootApplication
public class SimpleApiApplication {


	@Bean
	public Function<Mono<String>, Mono<String>> hello() {
		return mono -> mono.map(value -> value.toUpperCase());
	}

	public static void main(String[] args) {
		SpringApplication.run(SimpleApiApplication.class, args);
	}

}
