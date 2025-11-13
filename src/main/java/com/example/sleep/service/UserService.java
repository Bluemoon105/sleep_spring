package com.example.sleep.service;

import com.example.sleep.dto.PasswordChangeRequest;
import com.example.sleep.dto.SignUpRequest;
import com.example.sleep.model.User;
import com.example.sleep.repository.SleepDataRepository;
import com.example.sleep.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SleepDataRepository sleepDataRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
    					SleepDataRepository sleepDataRepository
    		) {
        this.userRepository = userRepository;
        this.sleepDataRepository = sleepDataRepository;  
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    public User createUser(SignUpRequest req) {
    	
    	if (userRepository.existsByEmail(req.getEmail())) {
    		throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    	}
    	
    	if (req.getPassword() == null || req.getPassword().length() <8 ) {
    		throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
    	}
    	
    	User user = new User();
    	user.setEmail(req.getEmail());
    	user.setPassword(req.getPassword());
    	user.setGender(req.getGender());
    	user.setName(req.getName());
    	user.setBirthDate(LocalDate.parse(req.getBirthDate()));
    	
    	return userRepository.save(user);
    }

    public User getUserById(Long memberNo) {
        return userRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + memberNo));
    }
    
    public void changePassword(PasswordChangeRequest req) {
    	User user = getUserById(req.getMemberNo());
    	
    	if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
    		throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
    	}
    	
    	if (req.getNewPassword() ==null || req.getNewPassword().length() <8 ) {
    		throw new IllegalArgumentException("새 비밀번호는 8자 이상이어야 합니다.");
    	}
    	
    	user.setPassword(passwordEncoder.encode(req.getNewPassword()));
    	userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long memberNo) {
    	User user = getUserById(memberNo);
    	
    	sleepDataRepository.deleteByMemberNo(memberNo);
    	
    	userRepository.delete(user);
    }

    public int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public int toGenderInt(User.Gender genderEnum) {
        if (genderEnum == null) return 0;
        return (genderEnum == User.Gender.M) ? 0 : 1;
    }
    
}
