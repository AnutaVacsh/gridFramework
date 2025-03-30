package ru.vaschenko.DistributionNode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DistributionNodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributionNodeApplication.class, args);
	}

}