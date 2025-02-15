package com.example.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.taskmanager.model") // ここを追加
@EnableJpaRepositories(basePackages = "com.example.taskmanager.repository") // ここを追加
public class TaskmanagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskmanagerApplication.class, args);
    }
}
