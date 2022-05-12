package com.mmtr.finance.parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinfocusApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinfocusApplication.class, args);
	}

}
