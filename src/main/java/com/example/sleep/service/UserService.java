package com.example.sleep.service;

import com.example.sleep.model.User;
import com.example.sleep.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long memberNo) {
        return userRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + memberNo));
    }

    public int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // ✅ enum 타입에 맞게 수정
    public int toGenderInt(User.Gender genderEnum) {
        if (genderEnum == null) return 0;
        return (genderEnum == User.Gender.M) ? 0 : 1;
    }
}
