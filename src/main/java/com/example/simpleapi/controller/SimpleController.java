package com.example.simpleapi.controller;
import java.util.ArrayList;
import java.util.List;

import com.example.simpleapi.model.Simple;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class SimpleController {
	@GetMapping("/hello")
	public String home() {
		
		log.info("==========simple-api home()");
		
		return "hello world";
		
	}
	@GetMapping("/simple")
	public List<Simple> listBoard(){
		List<Simple> list = new ArrayList<>();
		
		for(int i=0 ; i< 10;i++) {
			list.add(new Simple(i+1,"test-"+i, "contents-"+i));
			log.info("for "+i);
		}
		log.info(list.toString());
		return list;
		
	}
	
	@GetMapping("/version")
	public String version(){
		//log.info("version 2.0");
		return "=====  version 2.0";
		
	}

}
