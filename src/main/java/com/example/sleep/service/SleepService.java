package com.example.sleep.service;

import com.example.sleep.dto.UserInputRequest;
import com.example.sleep.model.SleepData;   // 네 프로젝트 패키지 맞춰라
import com.example.sleep.model.User;       // 네 프로젝트 패키지 맞춰라
import com.example.sleep.repository.SleepRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class SleepService {

    private final SleepRepository sleepRepository;
    private final UserService userService;
    private final WebClient webClient;

    public SleepService(
            SleepRepository sleepRepository,
            UserService userService,
            @Value("${fastapi.base-url}") String fastApiBaseUrl
    ) {
        this.sleepRepository = sleepRepository;
        this.userService = userService;
        this.webClient = WebClient.builder()
                .baseUrl(fastApiBaseUrl)
                .build();
    }

    /**
     * 1차 저장: 프론트에서 받은 입력을 daily_activities에 우선 저장
     */
    public SleepData saveInitialRecord(UserInputRequest input) {
        SleepData record = new SleepData();
        record.setUserId(input.getUserId()); // SleepData가 int면 캐스팅
        record.setDate(LocalDate.now());
        record.setSleepHours(input.getSleepHours());
        record.setCaffeineMg(input.getCaffeineMg());
        record.setAlcoholConsumption(input.getAlcoholConsumption());
        record.setPhysicalActivityHours(input.getPhysicalActivityHours());
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        // 예측 필드는 아직 비워둔다
        return sleepRepository.save(record);
    }

    /**
     * FastAPI: 피로도 예측 호출 후 daily_activities의 관련 칼럼 업데이트
     * 업데이트되는 컬럼: predicted_sleep_quality, predicted_fatigue_score, condition_level
     */
    public SleepData updateFatiguePrediction(SleepData record) {
        // 사용자 기본정보 조회
        User user = userService.getUserById(record.getUserId().longValue());
        int age = userService.calculateAge(user.getBirthDate());
        int genderInt = userService.toGenderInt(user.getGender()); // "M"/"F" -> 1/0

        Map<String, Object> body = Map.of(
                "age", age,
                "gender", genderInt,
                "caffeine_mg", record.getCaffeineMg(),
                "sleep_hours", record.getSleepHours(),
                "physical_activity_hours", record.getPhysicalActivityHours(),
                "alcohol_consumption", record.getAlcoholConsumption()
        );

        Map<String, Object> resp = webClient.post()
                .uri("/sleep/predict-fatigue")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (resp == null) {
            throw new IllegalStateException("FastAPI returned null for predict-fatigue");
        }

        // FastAPI 응답 키 이름에 맞춰 매핑
        // 예: {predicted_sleep_quality: 3.0, predicted_fatigue_score: 33.33, condition_level: "보통"}
        if (resp.get("predicted_sleep_quality") != null) {
            record.setPredictedSleepQuality(((Number) resp.get("predicted_sleep_quality")).doubleValue());
        }
        if (resp.get("predicted_fatigue_score") != null) {
            record.setPredictedFatigueScore(((Number) resp.get("predicted_fatigue_score")).doubleValue());
        }
        if (resp.get("condition_level") != null) {
            record.setConditionLevel((String) resp.get("condition_level"));
        }

        record.setUpdatedAt(LocalDateTime.now());
        return sleepRepository.save(record);
    }

    /**
     * FastAPI: 개인 최적 수면시간 예측 호출 후 daily_activities의 recommended_sleep_range 업데이트
     * 업데이트되는 컬럼: recommended_sleep_range
     */
    public SleepData updateOptimalSleepRange(SleepData record) {
        User user = userService.getUserById(record.getUserId().longValue());
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

        Map<String, Object> resp = webClient.post()
                .uri("/sleep/recommend")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (resp == null) {
            throw new IllegalStateException("FastAPI returned null for predict-optimal-hours");
        }

        // FastAPI 응답 예: { recommended_sleep_range: "7.0 ~ 7.5 시간" }
        if (resp.get("recommended_sleep_range") != null) {
            record.setRecommendedSleepRange((String) resp.get("recommended_sleep_range"));
        }

        record.setUpdatedAt(LocalDateTime.now());
        return sleepRepository.save(record);
    }
}
