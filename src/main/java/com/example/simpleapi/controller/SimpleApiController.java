package com.example.simpleapi.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.simpleapi.model.Simple;
import com.example.simpleapi.utils.CommonUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
			list.add(new Simple(i + 1, "test-" + i, "contents-" + i));
			log.info("for " + i);
		}
		log.info(list.toString());
		return list;

	}

	@GetMapping("/version")
	public String version() {
		// log.info("version 1.0");
		return "=====simple-api  version 1.0";

	}

	@GetMapping("/board-xml")
	public String boardXml() {

		return (CommonUtil.objectToXml(new Simple(99, "xml-test-입니다", "xml-contents-입니다")));

	}

}
