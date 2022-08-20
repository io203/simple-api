package com.example.simpleapi.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.simpleapi.model.Simple;

@FeignClient(value = "redispub", url = "http://localhost:3500/v1.0/publish")
public interface SimplePublisher {
    @PostMapping( value = "/my-pubsub/simple2", produces = "application/json")
	public void  publish( @RequestBody  Simple simple);
    
}
