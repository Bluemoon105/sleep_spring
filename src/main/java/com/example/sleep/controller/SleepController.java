package com.example.sleep.controller;

import com.example.sleep.dto.UserInputRequest;
import com.example.sleep.model.SleepData;
import com.example.sleep.service.SleepService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sleep")
public class SleepController {

    private final SleepService sleepService;

    public SleepController(SleepService sleepService) {
        this.sleepService = sleepService;
    }

    /** 활동량 입력 **/
    @PostMapping("/activities")
    public ResponseEntity<?> saveActivity(@RequestBody UserInputRequest input) {
        try {
            SleepData saved = sleepService.saveInitialRecord(input);
            sleepService.updateFatiguePrediction(saved);
            sleepService.updateOptimalSleepRange(saved);

            return ResponseEntity.ok(
                    new ApiResponse("데이터가 성공적으로 저장되었습니다.", saved)
            );

        } catch (IllegalStateException e) {
            // 오늘 기록 이미 존재
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));

        } catch (Exception e) {
            // 기타 에러
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("서버 오류: " + e.getMessage(), null));
        }
    }


    @GetMapping("/predict-fatigue")
    public Object predictFatigue(@RequestParam("memberNo") Long memberNo) {
        SleepData todayRecord = sleepService.findTodayRecord(memberNo, LocalDate.now());
        if (todayRecord == null) {
            return new ApiResponse("오늘 입력된 데이터가 없습니다.", null);
        }
        SleepData updated = sleepService.updateFatiguePrediction(todayRecord);
        return new ApiResponse("피로도 예측이 완료되었습니다.", updated);
    }

    @GetMapping("/predict-sleephours")
    public Object predictSleepHours(@RequestParam("memberNo") Long memberNo) {
        SleepData todayRecord = sleepService.findTodayRecord(memberNo, LocalDate.now());
        if (todayRecord == null) {
            return new ApiResponse("오늘 입력된 데이터가 없습니다.", null);
        }
        SleepData updated = sleepService.updateOptimalSleepRange(todayRecord);
        return new ApiResponse("수면 시간 예측이 완료되었습니다.", updated);
    }

    @GetMapping("/recent")
    public Object showRecent(@RequestParam("memberNo") Long memberNo) {
        List<SleepData> list = sleepService.getRecentSleepHours(memberNo);
        return new ApiResponse("최근 7일 데이터 조회 완료", list);
    }

    record ApiResponse(String message, Object data) {}
}
