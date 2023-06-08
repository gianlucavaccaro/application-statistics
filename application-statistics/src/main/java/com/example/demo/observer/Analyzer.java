package com.example.demo.observer;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;

@Service
public class Analyzer {
	
	private static final Logger log= Logger.getLogger("Analyzer");
	
	private Statistic st;
	
	@Autowired 
	MyRepository repo;
	
	@Autowired
	EvaluateAlert alert;
	
	public void analyze(MongoChangeStreamCursor<ChangeStreamDocument<Document>> cursor) {
		while(cursor.hasNext()) {
			ChangeStreamDocument<Document> doc=cursor.next();
			if(doc.getFullDocument().getString("logLevel").equals("ERROR") || doc.getFullDocument().getString("logLevel").equals("WARNING"))
				createStatistic(doc.getFullDocument());
			else
				log.severe("Il Log che sto analizzando non è un errore nè un warning");
		}
	}
	
	private void createStatistic(Document fullDocument) {
		st= new Statistic();
		st.setApplicationName(fullDocument.getString("applicationName"));
		st.setModuleName(fullDocument.getString("moduleName"));
		st.setFunctionality(fullDocument.getString("functionality"));
		st.setLogLevel(fullDocument.getString("logLevel"));
		st.setTimestampInsert(LocalDate.now().toString());
		
		if(StringUtils.isEmpty(fullDocument.getString("functionality"))) {
			log.info("cerco nel modulo");
			findLastModuleErrorNumber(st.getApplicationName(),st.getModuleName());
		}else {
			log.info("cerco tra le funzionalita");
			findLastFunctionalityErrorNumber(st.getApplicationName(),st.getModuleName(),st.getFunctionality());
		}
			
	}

	//questo metodo controlla se esiste una statistica, per la funzionalita, di oggi e la aggiorna, 
		//se non esistono statistiche di oggi la crea
	private void findLastFunctionalityErrorNumber(String appName, String module, String functionality) {
		Statistic ErroreFunctionality = repo.findByFunctionality(st.getLogLevel(),appName,LocalDate.now().toString(),module,functionality);	
		
		if(ErroreFunctionality!=null) { 
			log.info("faccio l'update");
			ErroreFunctionality.setErrorNumber(ErroreFunctionality.getErrorNumber()+1);
			repo.save(ErroreFunctionality);
			
			ErroreFunctionality.setAlert(alert.evaluateAlert(ErroreFunctionality));
			repo.save(ErroreFunctionality);
		}else {
			log.info("non esistono errori per questa funzionalita");
			st.setErrorNumber(1);
			repo.save(st);
			
			st.setAlert(alert.evaluateAlert(st));
			repo.save(st);
		}
	}
	
	
	//questo metodo controlla se esiste una statistica, per il modulo, di oggi e la aggiorna, 
		// se non esistono statistiche di oggi la crea e gli pone come error number 1, perche è il primo errore di oggi senza funzionalita
	private void findLastModuleErrorNumber(String applicationName, String moduleName) {
		List<Statistic> listaErroriModulo = repo.findByModuleName(st.getLogLevel(),applicationName,LocalDate.now().toString(),moduleName);
		Statistic ErroreModuloSenzaFunzionalita = repo.findByFunctionality(st.getLogLevel(),applicationName,LocalDate.now().toString(),moduleName,null);
		if(ErroreModuloSenzaFunzionalita != null){
			log.info("Da aggiornare: " + ErroreModuloSenzaFunzionalita.getErrorNumber());
			ErroreModuloSenzaFunzionalita.setErrorNumber(listaErroriModulo.get(0).getErrorNumber()+1);
			repo.save(ErroreModuloSenzaFunzionalita);
			
			ErroreModuloSenzaFunzionalita.setAlert(alert.evaluateAlert(ErroreModuloSenzaFunzionalita));
			repo.save(ErroreModuloSenzaFunzionalita);
		}else {
			log.info("non esistono statistiche per questo modulo senza funzionalita");
			st.setErrorNumber(1);
			repo.save(st);
			
			st.setAlert(alert.evaluateAlert(st));
			repo.save(st);
		}
	}
	
}
