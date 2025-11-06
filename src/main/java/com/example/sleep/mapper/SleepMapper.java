package com.example.sleep.mapper;

import com.example.sleep.model.SleepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface SleepMapper {
    Optional<SleepData> findById(@Param("id") Long id);
    void insert(SleepData data);
    void updateFatigue(SleepData data);
    void updateOptimal(SleepData data);
}
