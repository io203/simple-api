package com.example.simpleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.simpleapi.config.AppKafkaProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppKafkaProperties.class)
public class SimpleApiApplication {	


	public static void main(String[] args) {
		SpringApplication.run(SimpleApiApplication.class, args);
	}

}
