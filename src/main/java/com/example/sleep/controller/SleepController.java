package com.example.sleep.controller;

import com.example.sleep.dto.UserInputRequest;
import com.example.sleep.model.SleepData;
import com.example.sleep.service.SleepService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/sleep")
public class SleepController {

    private final SleepService sleepService;

    public SleepController(SleepService sleepService) {
        this.sleepService = sleepService;
    }

    /** 활동 데이터 입력 폼 */
    @GetMapping("/form")
    public String showForm() {
        return "sleep/form";
    }

    /** 활동량 저장 */
    @PostMapping("/activities")
    public String saveActivity(@ModelAttribute UserInputRequest input, Model model) {
        try {
            SleepData saved = sleepService.saveInitialRecord(input);
            sleepService.updateFatiguePrediction(saved);
            sleepService.updateOptimalSleepRange(saved);
            model.addAttribute("message", "데이터가 성공적으로 저장되었습니다.");
            model.addAttribute("record", saved);
        } catch (IllegalStateException e) {
            model.addAttribute("message", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("message", "서버 오류: " + e.getMessage());
        }
        return "sleep/result";
    }

    /** 피로도 예측 */
    @GetMapping("/predict-fatigue")
    public String predictFatigue(@RequestParam("member_no") Long memberNo, Model model) {
        SleepData todayRecord = sleepService.findTodayRecord(memberNo, LocalDate.now());
        if (todayRecord == null) {
            model.addAttribute("message", "오늘 입력된 데이터가 없습니다.");
            return "sleep/result";
        }
        SleepData updated = sleepService.updateFatiguePrediction(todayRecord);
        model.addAttribute("record", updated);
        model.addAttribute("message", "피로도 예측이 완료되었습니다.");
        return "sleep/result";
    }

    /** 최적 수면시간 예측 */
    @GetMapping("/predict-sleephours")
    public String predictSleepHours(@RequestParam("member_no") Long memberNo, Model model) {
        SleepData todayRecord = sleepService.findTodayRecord(memberNo, LocalDate.now());
        if (todayRecord == null) {
            model.addAttribute("message", "오늘 입력된 데이터가 없습니다.");
            return "sleep/result";
        }
        SleepData updated = sleepService.updateOptimalSleepRange(todayRecord);
        model.addAttribute("record", updated);
        model.addAttribute("message", "수면 시간 예측이 완료되었습니다.");
        return "sleep/result";
    }

    /** 최근 7일 기록 조회 */
    @GetMapping("/recent")
    public String showRecent(@RequestParam("member_no") Long memberNo, Model model) {
        model.addAttribute("list", sleepService.getRecentSleepHours(memberNo));
        return "sleep/recent";
    }
}
