package com.example.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.observer.Statistic;

@Service
@RestController
@RequestMapping("api/statistics")
public class StatisticRestController {
	
	@Autowired
	StatisticRestService statisticsService;
	
	@GetMapping("/all/{appName}")
	public ResponseEntity<ArrayList<Statistic>> getAllStats(@PathVariable("appName") String appName) {
		//Restituisco solo le statistiche di oggi
		
		ArrayList<Statistic> allS = statisticsService.getAllStats(appName);
		return ResponseEntity.ok(allS);
		//return allS;
	}
	
	@GetMapping("/{logLevel}/{appName}")
	public ResponseEntity<ArrayList<Statistic>> getAllLevelStats(@PathVariable("logLevel") String logLevel,@PathVariable("appName") String appName) {
		//Restituisco solo le statistiche di oggi
		ArrayList<Statistic> allS = statisticsService.getAllLogLevelStats(logLevel, appName);
		
		//return allS;
		return ResponseEntity.ok(allS);
	}
	
	@GetMapping("/{logLevel}/{appName}/{module}")
	public ResponseEntity<ArrayList<Statistic>> getAllModuleStats(@PathVariable("logLevel") String logLevel,@PathVariable("appName") String appName, @PathVariable("module") String module) {
		//Restituisco solo le statistiche di oggi
		ArrayList<Statistic> allS = statisticsService.getAllModuleStats(logLevel, appName,module);
		
		//return allS;
		return ResponseEntity.ok(allS);
	}
	
	@GetMapping("/{logLevel}/{appName}/{module}/{functionality}")
	public ResponseEntity<Statistic> getAllFunctionalityStats(@PathVariable("logLevel") String logLevel,@PathVariable("appName") String appName, @PathVariable("module") String module, @PathVariable("functionality")String functionality) {
		//Restituisco solo le statistiche di oggi
		Statistic statistic = statisticsService.getAllFunctionalityStats(logLevel, appName,module,functionality);
		
		//return statistic;
		return ResponseEntity.ok(statistic);
	}
	
	
	//Restituisce tutte le statistiche di oggi che hanno alert a true in una data Applicazione
	@GetMapping("/{appName}")
	public ResponseEntity<ArrayList<Statistic>> getAppAlertStats(@PathVariable("appName") String appName) {
		ArrayList<Statistic> statistics = statisticsService.getAppAlert(appName);
		return ResponseEntity.ok(statistics);
	}
	
	//Restituisce tutte le statistiche di oggi che hanno alert a true in un dato modulo di una applicazione
	@GetMapping("/{appName}/{module}")
	public ResponseEntity<ArrayList<Statistic>> getModuleAlertStats(@PathVariable("logLevel") String logLevel,@PathVariable("appName") String appName, @PathVariable("module") String module) {
		ArrayList<Statistic> statistics = statisticsService.getModuleAlert(appName,module);
		return ResponseEntity.ok(statistics);
	}
}
