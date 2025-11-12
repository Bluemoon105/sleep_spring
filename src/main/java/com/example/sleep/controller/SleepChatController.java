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

    /** 채팅 입력 폼 */
    @GetMapping("/form")
    public String chatForm() {
        return "chat/form";
    }

    /** 일반 대화 요청 */
    @PostMapping("/message")
    public String chat(@RequestParam("member_no") Long memberNo,
                       @RequestParam("message") String message,
                       Model model) {
        String response = llmService.chatGeneral(memberNo, message);
        model.addAttribute("memberNo", memberNo);
        model.addAttribute("message", message);
        model.addAttribute("response", response);
        return "chat/result";
    }

    /** 일간 리포트 */
    @GetMapping("/report/daily/{memberNo}")
    public String dailyReport(@PathVariable Long memberNo, Model model) {
        String report = llmService.getDailyReport(memberNo);
        model.addAttribute("report", report);
        return "chat/dailyReport";
    }

    /** 주간 리포트 */
    @GetMapping("/report/weekly/{memberNo}")
    public String weeklyReport(@PathVariable Long memberNo, Model model) {
        String report = llmService.getWeeklyReport(memberNo);
        model.addAttribute("report", report);
        return "chat/weeklyReport";
    }

    /** 대화 기록 */
    @GetMapping("/history/{memberNo}")
    public String history(@PathVariable Long memberNo, Model model) {
        model.addAttribute("history", llmService.getChatHistory(memberNo));
        return "chat/history";
    }
}
