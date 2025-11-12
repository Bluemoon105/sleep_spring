package com.example.sleep.service;

import com.example.sleep.dto.UserInputRequest;
import com.example.sleep.model.SleepData;
import com.example.sleep.model.User;
import com.example.sleep.repository.SleepDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SleepService {

    private final SleepDataRepository sleepDataRepository;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final String fastApiBaseUrl;

    public SleepService(
            SleepDataRepository sleepDataRepository,
            UserService userService,
            @Value("${fastapi.base-url}") String fastApiBaseUrl
    ) {
        this.sleepDataRepository = sleepDataRepository;
        this.userService = userService;
        this.restTemplate = new RestTemplate();
        this.fastApiBaseUrl = fastApiBaseUrl;
    }

    /** 1️⃣ 활동 데이터 저장 (하루 1회 제한) **/
    public SleepData saveInitialRecord(UserInputRequest input) {
        Long memberNo = input.getMemberNo();
        LocalDate today = LocalDate.now();

        boolean existsToday = sleepDataRepository.existsByMemberNoAndDate(memberNo, today);
        if (existsToday) {
            throw new IllegalStateException("오늘은 이미 활동량이 등록되었습니다.");
        }

        SleepData record = new SleepData();
        record.setMemberNo(memberNo);
        record.setDate(today);
        record.setSleepHours(input.getSleepHours());
        record.setCaffeineMg(input.getCaffeineMg());
        record.setAlcoholConsumption(input.getAlcoholConsumption());
        record.setPhysicalActivityHours(input.getPhysicalActivityHours());
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());

        return sleepDataRepository.save(record);
    }

    /** 2️⃣ 오늘 데이터 조회 **/
    public SleepData findTodayRecord(Long memberNo, LocalDate date) {
        return sleepDataRepository.findByMemberNoAndDate(memberNo, date).orElse(null);
    }

    /** 3️⃣ 피로도 예측 **/
    public SleepData updateFatiguePrediction(SleepData record) {
        User user = userService.getUserById(record.getMemberNo());
        int age = userService.calculateAge(user.getBirthDate());
        int genderInt = userService.toGenderInt(user.getGender());

        Map<String, Object> body = Map.of(
                "age", age,
                "gender", genderInt,
                "caffeine_mg", record.getCaffeineMg(),
                "sleep_hours", record.getSleepHours(),
                "physical_activity_hours", record.getPhysicalActivityHours(),
                "alcohol_consumption", record.getAlcoholConsumption()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                fastApiBaseUrl + "/sleep/predict-fatigue",
                request,
                Map.class
        );

        Map<String, Object> resp = response.getBody();
        if (resp == null) throw new IllegalStateException("FastAPI 응답이 비어 있습니다.");

        record.setPredictedSleepQuality(
                ((Number) resp.getOrDefault("predicted_sleep_quality", 0)).doubleValue()
        );
        record.setPredictedFatigueScore(
                ((Number) resp.getOrDefault("predicted_fatigue_score", 0)).doubleValue()
        );
        record.setConditionLevel((String) resp.getOrDefault("condition_level", "보통"));

        record.setUpdatedAt(LocalDateTime.now());
        return sleepDataRepository.save(record);
    }

    /** 4️⃣ 개인 최적 수면시간 예측 **/
    public SleepData updateOptimalSleepRange(SleepData record) {
        User user = userService.getUserById(record.getMemberNo());
        int age = userService.calculateAge(user.getBirthDate());
        int genderInt = userService.toGenderInt(user.getGender());

        Map<String, Object> body = Map.of(
                "age", age,
                "gender", genderInt,
                "caffeine_mg", record.getCaffeineMg(),
                "sleep_hours", record.getSleepHours(),
                "alcohol_consumption", record.getAlcoholConsumption(),
                "physical_activity_hours", record.getPhysicalActivityHours()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                fastApiBaseUrl + "/sleep/recommend",
                request,
                Map.class
        );

        Map<String, Object> resp = response.getBody();
        if (resp == null) throw new IllegalStateException("FastAPI 응답이 비어 있습니다.");

        record.setRecommendedSleepRange(
                (String) resp.getOrDefault("recommended_sleep_range", "7시간")
        );

        record.setUpdatedAt(LocalDateTime.now());
        return sleepDataRepository.save(record);
    }

    /** 5️⃣ 최근 7일 기록 **/
    public List<SleepData> getRecentSleepHours(Long memberNo) {
        List<SleepData> list = sleepDataRepository.findTop7ByMemberNoOrderByDateDesc(memberNo);
        Collections.reverse(list);
        return list;
    }
}
