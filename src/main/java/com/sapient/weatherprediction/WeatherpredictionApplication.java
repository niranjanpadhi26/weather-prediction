package com.sapient.weatherprediction;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@CircuitBreaker(name = "weatherService", fallbackMethod = "fallback")
public class WeatherpredictionApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherpredictionApplication.class, args);
	}

}
