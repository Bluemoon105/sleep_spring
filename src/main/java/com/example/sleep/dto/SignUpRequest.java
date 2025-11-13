package com.example.sleep.dto;

import com.example.sleep.model.User.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
	
	private String email;
	private String password;
	private Gender gender;
	private String name;
	private String birthDate;
}