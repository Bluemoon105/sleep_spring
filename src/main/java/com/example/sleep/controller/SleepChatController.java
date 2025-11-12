package com.example.sleep.controller;

import com.example.sleep.service.SleepLLMService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class SleepChatController {

    private final SleepLLMService llmService;

    public SleepChatController(SleepLLMService llmService) {
        this.llmService = llmService;
    }

    @GetMapping("/form")
    public String chatForm() {
        return "chat/form";
    }

    @PostMapping("/message")
    public String chat(@RequestParam("user_id") String userId,
                       @RequestParam("message") String message,
                       Model model) {
        String response = llmService.chatGeneral(userId, message);
        model.addAttribute("userId", userId);
        model.addAttribute("message", message);
        model.addAttribute("response", response);
        return "chat/result";
    }

    @GetMapping("/report/daily/{userId}")
    public String dailyReport(@PathVariable String userId, Model model) {
        String report = llmService.getDailyReport(userId);
        model.addAttribute("report", report);
        return "chat/dailyReport";
    }

    @GetMapping("/report/weekly/{userId}")
    public String weeklyReport(@PathVariable String userId, Model model) {
        String report = llmService.getWeeklyReport(userId);
        model.addAttribute("report", report);
        return "chat/weeklyReport";
    }

    @GetMapping("/history/{userId}")
    public String history(@PathVariable String userId, Model model) {
        model.addAttribute("history", llmService.getChatHistory(userId));
        return "chat/history";
    }
}
