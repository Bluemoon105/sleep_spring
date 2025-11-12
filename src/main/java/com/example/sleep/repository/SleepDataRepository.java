package com.example.sleep.repository;

import com.example.sleep.model.SleepData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SleepDataRepository extends JpaRepository<SleepData, Long> {

    // 오늘 날짜 데이터가 존재하는지 확인
    boolean existsByUserIdAndDate(String userId, LocalDate date);

    // 특정 사용자 + 특정 날짜 데이터 조회
    Optional<SleepData> findByUserIdAndDate(String userId, LocalDate date);

    // 최근 7일 기록 (최신순)
    List<SleepData> findTop7ByUserIdOrderByDateDesc(String userId);
}

// 바뀐 db
//package com.example.sleep.repository;
//
//import com.example.sleep.model.SleepData;
//import com.example.sleep.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface SleepDataRepository extends JpaRepository<SleepData, Long> {
//
//    // 오늘 날짜 데이터 존재 여부
//    boolean existsByMemberAndDate(User member, LocalDate date);
//
//    // 특정 회원 + 날짜별 데이터 조회
//    Optional<SleepData> findByMemberAndDate(User member, LocalDate date);
//
//    // 최근 7일 기록 (최신순)
//    List<SleepData> findTop7ByMemberOrderByDateDesc(User member);
//}
