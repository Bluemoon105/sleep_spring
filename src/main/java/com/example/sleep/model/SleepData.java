package com.example.sleep.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_activities_tb")  // ✅ 실제 테이블명 반영
public class SleepData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    /** 회원 번호 (FK) */
    @Column(name = "member_no", nullable = false)
    private Long memberNo;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "sleep_hours")
    private Double sleepHours;

    @Column(name = "caffeine_mg")
    private Double caffeineMg;

    @Column(name = "alcohol_consumption")
    private Double alcoholConsumption;

    @Column(name = "physical_activity_hours")
    private Double physicalActivityHours;

    @Column(name = "predicted_sleep_quality")
    private Double predictedSleepQuality;

    @Column(name = "predicted_fatigue_score")
    private Double predictedFatigueScore;

    @Column(name = "recommended_sleep_range")
    private String recommendedSleepRange;

    @Column(name = "condition_level")
    private String conditionLevel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public SleepData() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ Getter / Setter 전체 정의

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberNo() {
        return memberNo;
    }
    public void setMemberNo(Long memberNo) {
        this.memberNo = memberNo;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getSleepHours() {
        return sleepHours;
    }
    public void setSleepHours(Double sleepHours) {
        this.sleepHours = sleepHours;
    }

    public Double getCaffeineMg() {
        return caffeineMg;
    }
    public void setCaffeineMg(Double caffeineMg) {
        this.caffeineMg = caffeineMg;
    }

    public Double getAlcoholConsumption() {
        return alcoholConsumption;
    }
    public void setAlcoholConsumption(Double alcoholConsumption) {
        this.alcoholConsumption = alcoholConsumption;
    }

    public Double getPhysicalActivityHours() {
        return physicalActivityHours;
    }
    public void setPhysicalActivityHours(Double physicalActivityHours) {
        this.physicalActivityHours = physicalActivityHours;
    }

    public Double getPredictedSleepQuality() {
        return predictedSleepQuality;
    }
    public void setPredictedSleepQuality(Double predictedSleepQuality) {
        this.predictedSleepQuality = predictedSleepQuality;
    }

    public Double getPredictedFatigueScore() {
        return predictedFatigueScore;
    }
    public void setPredictedFatigueScore(Double predictedFatigueScore) {
        this.predictedFatigueScore = predictedFatigueScore;
    }

    public String getRecommendedSleepRange() {
        return recommendedSleepRange;
    }
    public void setRecommendedSleepRange(String recommendedSleepRange) {
        this.recommendedSleepRange = recommendedSleepRange;
    }

    public String getConditionLevel() {
        return conditionLevel;
    }
    public void setConditionLevel(String conditionLevel) {
        this.conditionLevel = conditionLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
