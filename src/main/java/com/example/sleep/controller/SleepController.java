package com.app.medibear.controller;

import com.app.medibear.dto.UserInputRequest;
import com.app.medibear.model.SleepData;  // 네 프로젝트 패키지 맞춰라
import com.app.medibear.service.SleepService;
import com.app.medibear.repository.SleepRepository;
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

   /** 활동량 입력(하루 1회 제한) **/
    @PostMapping("/activities")
    public ResponseEntity<?> saveActivity(@RequestBody UserInputRequest input) {
        try {
            SleepData saved = sleepService.saveInitialRecord(input);
            return ResponseEntity.ok(saved);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류: " + e.getMessage());
        }
    }
    /** 피로도 예측 - 오늘 날짜 데이터 자동 조회 **/
    @PostMapping("/activities/predict-fatigue")
    public ResponseEntity<?> predictFatigueToday(@RequestParam("userId") String userId) { // ✅ String으로 변경
        SleepData todayRecord = sleepService.findTodayRecord(userId, LocalDate.now());
        if (todayRecord == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("오늘 입력된 데이터가 없습니다.");
        }

        SleepData updated = sleepService.updateFatiguePrediction(todayRecord);
        return ResponseEntity.ok(updated);
    }

    /** 수면 시간 예측 - 오늘 날짜 데이터 자동 조회 **/
    @PostMapping("/activities/predict-sleephours")
    public ResponseEntity<?> predictSleepHoursToday(@RequestParam("userId") String userId) { // ✅ String으로 변경
        SleepData todayRecord = sleepService.findTodayRecord(userId, LocalDate.now());
        if (todayRecord == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("오늘 입력된 데이터가 없습니다.");
        }

        SleepData updated = sleepService.updateOptimalSleepRange(todayRecord);
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
