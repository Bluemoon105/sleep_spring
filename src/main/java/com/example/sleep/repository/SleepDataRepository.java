package com.example.sleep.repository;

import com.example.sleep.model.SleepData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepDataRepository extends JpaRepository<SleepData, Long> {
}
