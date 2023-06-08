package com.example.demo.configurations;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("Configurations")
@Getter
@Setter
@NoArgsConstructor
public class Configuration {
	@Id
	private String id;
	private long thresholdLevel;
	private String applicationName;
	private String thresholdLogLevel;
	private String module;
	private String functionality;
	
	public String toString() {
		return "{\"thresholdLevel\":\"" + thresholdLevel + "\", \"applicationName\":\"" + applicationName
				+ "\", \"thresholdLogLevel\":\"" + thresholdLogLevel + "\", \"module\":" + module + "\", \"functionality\":\"" + functionality
				+ "\"}";
	}
	
}
