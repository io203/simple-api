package com.example.simpleapi.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class Simple {
	
	private int num;
	private String title;
	private String content;
	
	

}
