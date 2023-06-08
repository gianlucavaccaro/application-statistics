package com.example.demo.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.configurations.*;

import jakarta.annotation.PostConstruct;


@Service
public class ConfigurationCache {

	@Autowired
	ConfigRepository configurationRepository;
	
	private HashMap<String,List<Configuration>> configurationCache;
	
	@PostConstruct
	public void init() {
		ArrayList<Configuration> allConfigurations = (ArrayList<Configuration>) configurationRepository.findAll();
		
		configurationCache = (HashMap<String,List<Configuration>>)allConfigurations.stream().collect(Collectors.groupingBy(c->getKey(c)));
	}

	private String getKey(Configuration c) {
		String key="";
		
		//in realta in una configurazione applicationName non puo essere null, vero?
		if(StringUtils.isNotEmpty(c.getApplicationName()))
			key += c.getApplicationName()+"#";
		
		if(StringUtils.isNotEmpty(c.getModule()))
			key += c.getModule()+"#";
		else
			key += "ALL#";
		
		if(StringUtils.isNotEmpty(c.getFunctionality()))
			key += c.getFunctionality();
		else
			key += "ALL";
		
		return key;
	}
	
	
	public ArrayList<Configuration> getFunctionalityConfigurations(String applicationName, String module, String functionality, String thresholdLogLevel){
		Configuration con = new Configuration();
		con.setApplicationName(applicationName);
		con.setModule(module);
		con.setFunctionality(functionality);
		ArrayList<Configuration> configurations= new ArrayList<Configuration>();
		if(configurationCache.containsKey(getKey(con)))
			for(Configuration c : configurationCache.get(getKey(con)))
				if(c.getThresholdLogLevel().equals(thresholdLogLevel))
					configurations.add(c);
		
		return configurations;
	}
	
	public ArrayList<Configuration> findConfigurationsbyModuleName(String applicationName, String moduleName, String thresholdLogLevel){
		Configuration con = new Configuration();
		con.setApplicationName(applicationName);
		con.setModule(moduleName);
		ArrayList<Configuration> configurations= new ArrayList<Configuration>();
		
		if(configurationCache.containsKey(getKey(con)))
			for(Configuration c : configurationCache.get(getKey(con)))
				if(c.getThresholdLogLevel().equals(thresholdLogLevel))
					configurations.add(c);
		
		return configurations;
	}
	

	public ArrayList<Configuration> findConfigurationsByApplicationName(String applicationName, String thresholdLogLevel){
		Configuration con = new Configuration();
		con.setApplicationName(applicationName);
		ArrayList<Configuration> configurations= new ArrayList<Configuration>();
		
		if(configurationCache.containsKey(getKey(con)))
			for(Configuration c : configurationCache.get(getKey(con)))
				if(c.getThresholdLogLevel().equals(thresholdLogLevel))
					configurations.add(c);
		
		
		
		return configurations;
	}
	
	
}
