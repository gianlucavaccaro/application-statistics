package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.observer.MyRepository;
import com.example.demo.observer.Statistic;

@Service
public class StatisticRestService {

	@Autowired
	MyRepository repo;
	
	
	public ArrayList<Statistic> getAllStats(String appName) {
		//Restituisco solo le statistiche di oggi
		ArrayList<Statistic> allES = (ArrayList<Statistic>) repo.findByApplicationName("ERROR", appName, LocalDate.now().toString());
		ArrayList<Statistic> allWS = (ArrayList<Statistic>) repo.findByApplicationName("WARNING", appName, LocalDate.now().toString());
		
		ArrayList<Statistic> allS = new ArrayList<Statistic>();
		allS.addAll(allES);
		allS.addAll(allWS);
		return allS;
	}
	
	public ArrayList<Statistic> getAllLogLevelStats(String logLevel,String appName) {
		//Restituisco solo le statistiche di oggi
		ArrayList<Statistic> allS = (ArrayList<Statistic>) repo.findByApplicationName(logLevel, appName, LocalDate.now().toString());
		
		return allS;
	}

	public ArrayList<Statistic> getAllModuleStats(String logLevel, String appName, String module) {
		ArrayList<Statistic> allS = (ArrayList<Statistic>) repo.findByModuleName(logLevel, logLevel, LocalDate.now().toString(), module);
		return allS;
	}

	public Statistic getAllFunctionalityStats(String logLevel, String appName, String module,
			String functionality) {
		Statistic fStatistic =repo.findByFunctionality(logLevel, appName, LocalDate.now().toString(), module, functionality);
		return fStatistic;
	}

	public ArrayList<Statistic> getAppAlert(String appName) {
		ArrayList<Statistic> errorsAlert = (ArrayList<Statistic>) repo.findApplicationAlert("ERROR", appName, LocalDate.now().toString());
		ArrayList<Statistic> warningsAlert = (ArrayList<Statistic>) repo.findApplicationAlert("WARNING", appName, LocalDate.now().toString());
		ArrayList<Statistic> result= Stream.concat(errorsAlert.stream(), warningsAlert.stream()).collect(Collectors.toCollection(ArrayList::new));
		return result;
	}

	public ArrayList<Statistic> getModuleAlert(String appName, String module) {
		ArrayList<Statistic> errorsAlert = (ArrayList<Statistic>) repo.findModuleAlert("ERROR", appName, LocalDate.now().toString(),module);
		ArrayList<Statistic> warningsAlert = (ArrayList<Statistic>) repo.findModuleAlert("WARNING", appName, LocalDate.now().toString(),module);
		ArrayList<Statistic> result= Stream.concat(errorsAlert.stream(), warningsAlert.stream()).collect(Collectors.toCollection(ArrayList::new));
		return result;
	}
	
}
