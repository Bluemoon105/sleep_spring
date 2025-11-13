package com.example.sleep.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
	private Long memberNo;
	private String oldPassword;
	private String newPassword;
}