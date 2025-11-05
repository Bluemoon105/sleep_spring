package com.example.sleep.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.sleep.model.SleepData;
import com.example.sleep.repository.SleepRepository;

import java.time.LocalDateTime;

@Service
public class SleepService {

    private final WebClient webClient;
    private final SleepRepository repository;

    public SleepService(@Value("${fastapi.base-url}") String baseUrl, SleepRepository repository) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.repository = repository;
    }

    public String predictFatigue(SleepData data) {
        String result = webClient.post()
                .uri("/sleep/predict-fatigue")
                .bodyValue(data)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        data.setUpdatedAt(LocalDateTime.now());
        repository.save(data);
        return result;
    }

    public String recommendSleep(SleepData data) {
        String result = webClient.post()
                .uri("/sleep/recommend")
                .bodyValue(data)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        data.setUpdatedAt(LocalDateTime.now());
        repository.save(data);
        return result;
    }
}
