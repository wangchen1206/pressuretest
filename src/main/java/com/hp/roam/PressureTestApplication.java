package com.hp.roam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PressureTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PressureTestApplication.class, args);
	}
	
}
