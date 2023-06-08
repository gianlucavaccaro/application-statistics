package com.example.demo.observer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;

import com.example.demo.configurations.*;


@Service
public class EvaluateAlert {
	
	@Autowired
	ConfigurationCache configurations;
	
	@Autowired
	MyRepository repo;
	
	public boolean evaluateAlert(Statistic st) {
		boolean alertF,alertM,alertA;
		
		//controllo il numero di errori per la funzionalita
		if(StringUtils.isEmpty(st.getFunctionality()))
			alertF=false;
		else {
			ArrayList<Configuration> configurationsFunctionality = configurations.getFunctionalityConfigurations(st.getApplicationName(), st.getModuleName(), st.getFunctionality(), st.getLogLevel());	
			alertF=checkFunctionality(configurationsFunctionality,st);
			}
		
		//controllo il numero di errori per il modulo
		ArrayList<Configuration> configurationsModule = configurations.findConfigurationsbyModuleName(st.getApplicationName(), st.getModuleName(),  st.getLogLevel());
		if(CollectionUtils.isEmpty(configurationsModule))
			alertM=false;
		else {
			ArrayList<Statistic> moduleStatistics= (ArrayList<Statistic>) repo.findByModuleName(st.getLogLevel(), st.getApplicationName(), st.getTimestampInsert(),st.getModuleName());
			alertM= checkModule(configurationsModule,moduleStatistics);
		}
		
		//controllo il numero di errori per l'intera applicazione
		ArrayList<Configuration> configurationsApplication = configurations.findConfigurationsByApplicationName(st.getApplicationName(),st.getLogLevel());
		
		if(CollectionUtils.isEmpty(configurationsApplication))
			alertA=false;
		else {
			ArrayList<Statistic> applicationStatistics = (ArrayList<Statistic>) repo.findByApplicationName(st.getLogLevel(), st.getApplicationName(), st.getTimestampInsert());
			alertA= checkApplication(configurationsApplication,applicationStatistics);
		}
		
		return alertF || alertM || alertA;
	}
	
	private boolean checkFunctionality(ArrayList<Configuration> configurationsFunctionality,
			Statistic statistic) {
		if(CollectionUtils.isEmpty(configurationsFunctionality)) 
			return false;
		else{
			for(Configuration c : configurationsFunctionality) 
				if(statistic.getErrorNumber() >= c.getThresholdLevel())
					return true;		
		}
		return false;
	}

	private boolean checkModule(ArrayList<Configuration> configurationsModule,
			ArrayList<Statistic> statisticsModule) {
		long moduleErrors=0;
		for(Statistic s: statisticsModule)
			moduleErrors+=s.getErrorNumber();
		System.out.println("Totale errori nel modulo "+moduleErrors);
		for(Configuration c: configurationsModule)
			if(moduleErrors>=c.getThresholdLevel())
				return true;
		return false;
	}
	
	private boolean checkApplication(ArrayList<Configuration> configurationsApplication,
			ArrayList<Statistic> statisticsApplication) {
		long applicationErrors=0;
		for(Statistic s: statisticsApplication)
			applicationErrors += s.getErrorNumber();
		System.out.println("Totale errori nell'applicazione "+applicationErrors);
		for(Configuration c: configurationsApplication)
			if(applicationErrors>=c.getThresholdLevel())
				return true;
		return false;
	}
}
