package com.example.sleep.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sleep_predictions")
public class SleepPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ userId를 문자열로 변경
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "recommended_sleep_hours")
    private Double recommendedSleepHours;

    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 기본 생성자
    public SleepPrediction() {
        this.createdAt = LocalDateTime.now();
    }

    // ----- Getter & Setter -----
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getRecommendedSleepHours() {
        return recommendedSleepHours;
    }
    public void setRecommendedSleepHours(Double recommendedSleepHours) {
        this.recommendedSleepHours = recommendedSleepHours;
    }

    public Double getConfidence() {
        return confidence;
    }
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
