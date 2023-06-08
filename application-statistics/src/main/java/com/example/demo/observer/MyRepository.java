package com.example.demo.observer;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MyRepository extends MongoRepository<Statistic, String> {
	//query che prende i campi: timestamp e errornumber, degli obj che hanno appName,modName e func uguali ai parametri, e li ordina per timestamp dal piu recente al meno recente
	@Query(value="{ 'LogLevel' : ?0, 'applicationName' : ?1 , 'timestampInsert' : ?2, 'moduleName' : ?3, 'functionality' : ?4}")
	public Statistic findByFunctionality(String logLevel,String applicationName, String timeStampInsert, String moduleName, String functionality);
	
	@Query(value="{ 'LogLevel' : ?0, 'applicationName' : ?1, 'timestampInsert' : ?2,'moduleName' : ?3}", sort="{'errorNumber':-1}")
	public List<Statistic> findByModuleName(String logLevel,String applicationName,String timeStampInsert, String moduleName);
	
	@Query(value="{ 'LogLevel' : ?0, 'applicationName' : ?1, 'timestampInsert' : ?2}", sort="{'errorNumber':-1}")
	public List<Statistic> findByApplicationName(String logLevel,String applicationName, String timeStampInsert);
	
	@Query(value="{ 'LogLevel' : ?0, 'applicationName' : ?1, 'timestampInsert' : ?2, 'alert' : 'true'}")
	public List<Statistic> findApplicationAlert(String logLevel,String applicationName, String timeStampInsert);
	
	@Query(value="{ 'LogLevel' : ?0, 'applicationName' : ?1, 'timestampInsert' : ?2,'moduleName' : ?3 ,'alert' : 'true'}")
	public List<Statistic> findModuleAlert(String logLevel,String applicationName, String timeStampInsert,String moduleName);
}
