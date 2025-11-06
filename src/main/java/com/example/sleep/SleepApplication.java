package com.example.sleep;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.sleep.mapper") // ✅ 추가
public class SleepApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleepApplication.class, args);
    }
}
