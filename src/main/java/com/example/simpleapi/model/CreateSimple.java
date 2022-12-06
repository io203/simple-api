package com.example.simpleapi.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSimple {
	private String logicType;  
	
	private int num;
	private String title;
	private String content;

	private String message;

	
	
	

}
