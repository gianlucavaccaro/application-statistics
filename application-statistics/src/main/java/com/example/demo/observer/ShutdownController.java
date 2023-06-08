package com.example.demo.observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutdownController {

	@Autowired
	Observer obs;
	
	@GetMapping("/shutdown")
	public void shutdown() {
		System.out.println("CHIUDO IL CURSORE");
		obs.getWatchControl().close();
	}
}
