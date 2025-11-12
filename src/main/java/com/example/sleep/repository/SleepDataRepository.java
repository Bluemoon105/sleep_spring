package com.example.sleep.repository;

import com.example.sleep.model.SleepData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SleepDataRepository extends JpaRepository<SleepData, Long> {

    // 오늘 날짜 데이터 존재 여부 확인
    boolean existsByMemberNoAndDate(Long memberNo, LocalDate date);

    // 특정 회원 + 특정 날짜 데이터 조회
    Optional<SleepData> findByMemberNoAndDate(Long memberNo, LocalDate date);

    // 최근 7일 기록 (최신순)
    List<SleepData> findTop7ByMemberNoOrderByDateDesc(Long memberNo);
}
