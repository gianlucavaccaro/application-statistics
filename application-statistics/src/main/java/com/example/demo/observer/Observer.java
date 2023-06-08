package com.example.demo.observer;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;

import jakarta.annotation.PostConstruct;

@Service
public class Observer{

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	Analyzer analyzer;
	
	private ChangeStreamIterable<Document> watchControl;
	
	@PostConstruct
	public void observ() {
		List<Bson> pipe= Arrays.asList(Aggregates.match(Filters.in("operationType", "insert")));
		watchControl=mongoTemplate.getCollection("LogMessages").watch(pipe);
		analyzer.analyze(watchControl.cursor());
		
	}
	
	public MongoChangeStreamCursor<ChangeStreamDocument<Document>> getWatchControl() {
		return watchControl.cursor();
	}
	
}
