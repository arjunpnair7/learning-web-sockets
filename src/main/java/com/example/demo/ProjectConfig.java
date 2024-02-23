package com.example.demo;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example")
@EnableFeignClients(basePackages = "com.example")
public class ProjectConfig {
}
