package com.example.sleep.controller;

import com.example.sleep.dto.UserInputRequest;
import com.example.sleep.model.SleepData;  // 네 프로젝트 패키지 맞춰라
import com.example.sleep.service.SleepService;
import com.example.sleep.repository.SleepRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sleep")
public class SleepController {

    private final SleepService sleepService;
    private final SleepRepository sleepRepository;

    public SleepController(SleepService sleepService, SleepRepository sleepRepository) {
        this.sleepService = sleepService;
        this.sleepRepository = sleepRepository;
    }

    /**
     * 1) 1차 저장만 수행
     */
    @PostMapping("/activities")
    public ResponseEntity<SleepData> saveActivity(@RequestBody UserInputRequest input) {
        SleepData saved = sleepService.saveInitialRecord(input);
        return ResponseEntity.ok(saved);
    }

    /**
     * 2) 피로도 예측만 수행 (이미 저장된 레코드 ID 필요)
     */
    @PostMapping("/activities/{id}/predict-fatigue")
    public ResponseEntity<SleepData> predictFatigue(@PathVariable("id") Long id) {
        SleepData record = sleepRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + id));
        SleepData updated = sleepService.updateFatiguePrediction(record);
        return ResponseEntity.ok(updated);
    }

    /**
     * 3) 개인 최적 수면시간 예측만 수행 (이미 저장된 레코드 ID 필요)
     */
    @PostMapping("/activities/{id}/predict-sleephours")
    public ResponseEntity<SleepData> predictOptimal(@PathVariable ("id") Long id) {
        SleepData record = sleepRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + id));
        SleepData updated = sleepService.updateOptimalSleepRange(record);
        return ResponseEntity.ok(updated);
    }

    /**
     * 4) 한 번에: 1차 저장 -> 피로도 예측 -> 수면시간 예측
     *    프론트에서 한 번에 처리하고 싶을 때 사용
     */
    @PostMapping("/activities/full")
    public ResponseEntity<SleepData> saveAndPredict(@RequestBody UserInputRequest input) {
        SleepData saved = sleepService.saveInitialRecord(input);
        saved = sleepService.updateFatiguePrediction(saved);
        saved = sleepService.updateOptimalSleepRange(saved);
        return ResponseEntity.ok(saved);
    }
}
