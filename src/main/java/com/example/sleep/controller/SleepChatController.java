package com.example.sleep.controller;

import com.example.sleep.service.SleepLLMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class SleepChatController {

    private final SleepLLMService llmService;

    public SleepChatController(SleepLLMService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/message")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, Object> body) {
        Long memberNo = Long.valueOf(body.get("memberNo").toString());
        String message = (String) body.get("message");

        String response = llmService.chatGeneral(memberNo, message);
        return ResponseEntity.ok(Map.of("response", response));
    }

    @GetMapping("/report/daily/{memberNo}")
    public ResponseEntity<Map<String, Object>> dailyReport(@PathVariable("memberNo") Long memberNo) {
        String report = llmService.getDailyReport(memberNo);
        return ResponseEntity.ok(Map.of("report", report));
    }

    @GetMapping("/report/weekly/{memberNo}")
    public ResponseEntity<Map<String, Object>> weeklyReport(@PathVariable("memberNo") Long memberNo) {
        String report = llmService.getWeeklyReport(memberNo);
        return ResponseEntity.ok(Map.of("report", report));
    }

    @GetMapping("/history/{memberNo}")
    public ResponseEntity<Map<String, Object>> history(@PathVariable("memberNo") Long memberNo) {
        Map<String, Object> history = llmService.getChatHistory(memberNo);
        return ResponseEntity.ok(history);
    }
}
