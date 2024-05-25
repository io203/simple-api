package com.example.simpleapi.controller;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleapi.model.Simple;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class SimpleApiController {
	@GetMapping("/hello")
	public String hello(@RequestParam(required = false) String error, HttpServletRequest request) throws Exception {
		
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			log.info("{} : {}", headerName, request.getHeader(headerName));
		}
		
		if("true".equals(error)){
			throw new Exception("Error: true");
		}
		
		log.info("==========simple-api home()");
		
		return "hello world";
		
	}
	
	@GetMapping("/simple")
	public List<Simple> listSimple(){
		List<Simple> list = new ArrayList<>();
		
		for(int i=0 ; i< 10;i++) {
			list.add(new Simple(i+1,"test-"+i, "contents-"+i));
			log.info("for "+i);
		}
		// for(int i=0 ; i< 10;i++) {
		// 	list.add(new Simple(i+1,"test-"+i, "contents-"+i));
		// 	log.info("for "+i);
		// }
		log.info(list.toString());
		return list;
		
	}
	
	@GetMapping("/version")
	public String version(){
		//log.info("version 1.0");
		return "=====simple-api  version 1.0";
		
	}

}
