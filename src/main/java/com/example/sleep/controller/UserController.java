package com.example.sleep.controller;

import com.example.sleep.dto.PasswordChangeRequest;
import com.example.sleep.model.User;
import com.example.sleep.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/{memberNo")
	public ResponseEntity<?> getUser(@PathVariable Long memberNo) {
		User user = userService.getUserById(memberNo);
		return ResponseEntity.ok(user);
	}
	
	@PatchMapping("/password")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest req) {
		try {
			userService.changePassword(req);
			return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
	
	@DeleteMapping("/{memberNo")
	public ResponseEntity<?> deleteUser(@PathVariable Long memberNo) {
		userService.deleteUser(memberNo);
		return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
	}
}