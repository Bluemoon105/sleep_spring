package com.example.sleep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.sleep.model.SleepData;
import com.example.sleep.service.SleepService;

@RestController
@RequestMapping("/api/sleep")
@CrossOrigin(origins = "*")
public class SleepController {

    @Autowired
    private SleepService sleepService;

    @PostMapping("/predict-fatigue")
    public String predictFatigue(@RequestBody SleepData data) {
        return sleepService.predictFatigue(data);
    }

    @PostMapping("/recommend")
    public String recommendSleep(@RequestBody SleepData data) {
        return sleepService.recommendSleep(data);
    }
}
