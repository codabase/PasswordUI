package com.codabase.controller;
  
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:com.codabase/config/user-beans.xml")
public class Application {
    public static void main(String[] args) {  
        SpringApplication.run(Application.class, args);
    }  
}  