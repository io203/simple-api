package com.example.simpleapi.controller;
import com.example.simpleapi.service.SimpleService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StateController {
	private final SimpleService service;



	@GetMapping("/redis/{key}")
	public String redis(@PathVariable String key){

		return service.redisHello(key);
	}

	@PostMapping("redis")
	public String add(@RequestBody String data){

		service.insertState(data);
		return "정상 저장";
	}




}
