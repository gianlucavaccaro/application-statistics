package com.example.demo.observer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("Statistics")
@NoArgsConstructor
@Getter
@Setter 
public class Statistic {
	@Id
	private String id;
	
	private String applicationName;
	private String moduleName;
	private String functionality;
	private boolean alert;
	private long errorNumber;
	private String timestampInsert;
	private String logLevel;
	
	
	@Override
	public String toString() {
		return "{\"applicationName\":\"" + applicationName + "\", \"moduleName\":\"" + moduleName + "\", \"functionality\":\""
				+ functionality + "\", \"alert\":\"" + alert + "\", \"errorNumber\":\""+errorNumber+"\",\"timestampInsert\":\""+timestampInsert+"\",\"logLevel\":\""+logLevel+"\"}";
	}

}
