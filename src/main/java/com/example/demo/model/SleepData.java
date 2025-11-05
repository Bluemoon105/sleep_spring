package com.example.sleep.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int userId;
    private Double sleepHours;
    private Double physicalActivityHours;
    private Double caffeineMg;
    private Double alcoholConsumption;
    private Double predictedSleepQuality;
    private Double predictedFatigueScore;
    private String recommendedSleepRange;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt;
}
