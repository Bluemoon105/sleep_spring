package com.example.sleep.controller;

import com.example.sleep.dto.SignUpRequest;
import com.example.sleep.model.User;
import com.example.sleep.service.UserService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
	
	private final UserService userService;
	
	public AuthController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest req) {
		try {
			User saved = userService.createUser(req);
			return ResponseEntity.ok(saved);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
}