package com.example.sleep;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.sleep.mapper")
@SpringBootApplication
public class SleepApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleepApplication.class, args);
    }
}
