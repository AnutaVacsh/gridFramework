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

Сделать кастомную аннотацию над методом который вызываем в рабочей программе
Аннотация для самих этих классов вместо интерфейсов?

 */