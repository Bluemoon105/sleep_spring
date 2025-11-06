package com.example.sleep.repository;

import com.example.sleep.model.SleepData; // 네 프로젝트의 패키지명에 맞춰 유지
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepRepository extends JpaRepository<SleepData, Long> {
}
