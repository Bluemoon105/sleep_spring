package com.example.sleep.mapper;

import com.example.sleep.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findById(Long id);
}
