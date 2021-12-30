package com.example.simpleapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class SimpleService {



	private  WebClient client;	
	private String stateUrl;

	public WebClient getWebClient(){
		return WebClient
			.builder()
			.baseUrl(this.stateUrl)
			// .defaultCookie("쿠키키","쿠키값")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			// .defaultHeader("apiKey", apiKey)
			//Memory 조정: 2M (default 256KB)
			.exchangeStrategies(ExchangeStrategies.builder()
            	.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2*1024*1024)) 
            	.build())
			.build();
	}
	
	public SimpleService(@Value("${dapr.state.url}") String stateUrl) {
		this.stateUrl = stateUrl;	
		this.client = getWebClient();
	}

    public String redisHello(String key) {
		
		return client
				.get()
				.uri("/"+key)
                .retrieve()
				.bodyToMono(String.class).block();
	}

	public void  insertState(String data)  {		 
		 client
				.post()
				.body(Mono.just(data), String.class)			
				.exchangeToMono(s -> s.toBodilessEntity())
				.block();
	}
	

}
