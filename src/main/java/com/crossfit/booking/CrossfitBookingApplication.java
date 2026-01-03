package com.crossfit.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CrossfitBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrossfitBookingApplication.class, args);
	}

}
