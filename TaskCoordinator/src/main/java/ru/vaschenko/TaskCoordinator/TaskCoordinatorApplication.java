package ru.vaschenko.TaskCoordinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TaskCoordinatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskCoordinatorApplication.class, args);
	}
}
/*
Валидация
Сделать ещё более гибко, чтоб контроллеры тоже могли подменяться
 */