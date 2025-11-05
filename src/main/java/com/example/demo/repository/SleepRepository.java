package com.example.sleep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sleep.model.SleepData;

public interface SleepRepository extends JpaRepository<SleepData, Long> {
}
