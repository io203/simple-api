package com.example.simpleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.micrometer.observation.ObservationTextPublisher;

@SpringBootApplication
public class SimpleApiApplication {	
	

	public static void main(String[] args) {
		SpringApplication.run(SimpleApiApplication.class, args);
	}

	
}
