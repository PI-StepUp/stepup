package com.pi.stepup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StepupApplication {

	public static void main(String[] args) {
		SpringApplication.run(StepupApplication.class, args);
	}

}
