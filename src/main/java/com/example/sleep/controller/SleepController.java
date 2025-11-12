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

    @GetMapping("/form")
    public String showForm() {
        return "sleep/form";
    }

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

    @GetMapping("/predict-fatigue")
    public String predictFatigue(@RequestParam("userId") String userId, Model model) {
        SleepData todayRecord = sleepService.findTodayRecord(userId, LocalDate.now());
        if (todayRecord == null) {
            model.addAttribute("message", "오늘 입력된 데이터가 없습니다.");
            return "sleep/result";
        }
        SleepData updated = sleepService.updateFatiguePrediction(todayRecord);
        model.addAttribute("record", updated);
        model.addAttribute("message", "피로도 예측이 완료되었습니다.");
        return "sleep/result";
    }

    @GetMapping("/predict-sleephours")
    public String predictSleepHours(@RequestParam("userId") String userId, Model model) {
        SleepData todayRecord = sleepService.findTodayRecord(userId, LocalDate.now());
        if (todayRecord == null) {
            model.addAttribute("message", "오늘 입력된 데이터가 없습니다.");
            return "sleep/result";
        }
        SleepData updated = sleepService.updateOptimalSleepRange(todayRecord);
        model.addAttribute("record", updated);
        model.addAttribute("message", "수면 시간 예측이 완료되었습니다.");
        return "sleep/result";
    }

    @GetMapping("/recent")
    public String showRecent(@RequestParam("userId") String userId, Model model) {
        model.addAttribute("list", sleepService.getRecentSleepHours(userId));
        return "sleep/recent";
    }
}
