package com.example.simpleapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleapi.model.Simple;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class SimpleApiController {
	@GetMapping("/hello")
	public String hello() {

		log.info("==========simple-api home()");

		return "hello world";

	}

	@GetMapping("/simple")
	public List<Simple> listSimple() {
		List<Simple> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Simple simple = new Simple(i, "Item " + i, "Content for item " + i);
			list.add(simple);
			log.info("Created Simple object: {}", simple);
		}

		
		return list;

	}

	@GetMapping("/version")
	public String version() {
		// log.info("version 1.0");
		return "=====simple-api  version 2.0";


	}

	@PostMapping("/log-request")
    public String logRequest(
            @RequestBody String requestBody,
            @RequestHeader HttpHeaders headers) {
            
        // 1. Request Body 출력
        log.info("=======Request Body: {}", requestBody);
        
        // 2. 모든 Header 값 출력
        headers.forEach((key, value) -> {
            log.info("=========Header '{}' = {}", key, value);
        });
        
        // // 3. 특정 Header 값 가져오기 예시
        // String contentType = headers.getFirst(HttpHeaders.CONTENT_TYPE);
        // String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        
        // log.info("Content-Type: {}", contentType);
        // log.info("Authorization: {}", authorization);
        
        return "Request logged successfully";
    }

}
