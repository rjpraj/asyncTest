package com.rajesh.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:/applicationContext.xml")
public class AsyncProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncProcessingApplication.class, args);
	}

}
