package com.app.medibear.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_activities")
public class SleepData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private LocalDate date;
    private Double sleepHours;
    private Double caffeineMg;
    private Double alcoholConsumption;
    private Double physicalActivityHours;
    private Double predictedSleepQuality;
    private Double predictedFatigueScore;
    private String recommendedSleepRange;
    private String conditionLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 기본 생성자
    public SleepData() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getter / Setter 생략
}
