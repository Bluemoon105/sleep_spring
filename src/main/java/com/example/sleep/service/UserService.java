package com.example.sleep.service;

import com.example.sleep.model.User;          // 네 프로젝트의 User 엔티티 패키지에 맞춰라
import com.example.sleep.repository.UserRepository; // 이미 있는 UserRepository를 사용
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 컨트롤러/서비스에서 요구한 메서드명 그대로 유지
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    public int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // FastAPI가 기대하는 gender 정수 매핑 (M->1, 그 외->0)
    public int toGenderInt(String genderChar) {
        return "M".equalsIgnoreCase(genderChar) ? 0 : 1;
    }
}
