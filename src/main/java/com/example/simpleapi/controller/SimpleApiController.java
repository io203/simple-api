package com.example.simpleapi.controller;

import java.io.File;
import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class SimpleApiController {
	@Autowired
	ObjectMapper mapper ;

	@GetMapping("/encrypt")
	public String encrypt() throws Exception {
	
		URL path = this.getClass().getResource("/testData2.json");

		File jsonFile = new File(path.getFile());

		Map<String, Object> jsonMap = mapper.readValue(jsonFile, Map.class);
		String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
	
		log.info("jsonStr : {}", jsonStr);
		return jsonStr;
	}

}
