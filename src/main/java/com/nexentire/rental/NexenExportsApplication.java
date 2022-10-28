package com.nexentire.rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class NexenExportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexenExportsApplication.class, args);
	}

}
