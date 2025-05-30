package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(originPatterns = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/auth")
public class AuthController {
	AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

		try {
			User user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
			String token = authService.generateToken(user);
			Cookie cookie = new Cookie("authToken", token);
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
			cookie.setPath("/");
			cookie.setMaxAge(3600);
			response.addCookie(cookie);

			response.addHeader("Set-Cookie",
					String.format("authToken=%s; HttpOnly; Path=/; Max-Age=3600; SameSite=None", token));

			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", "Login Successful");
			responseBody.put("username", user.getUsername());
			responseBody.put("role", user.getRole().name());

			return ResponseEntity.ok(responseBody);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error", e.getMessage()));
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
		try {

			User user = (User) request.getAttribute("authenticatedUser");

			authService.logout(user);

			// create an empty cookie
			Cookie cookie = new Cookie("authToken", null);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);

			response.addHeader("Set-Cookie",
					String.format("authToken=%s; HttpOnly; Path=/; Max-Age=0; SameSite=None", null));

			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", "Logout successful");
			System.out.println("logout performed");
			return ResponseEntity.ok(responseBody);

		} catch (RuntimeException e) {

			return ResponseEntity.status(500).body(Map.of("error", "Logout Failed"));
		}
	}

}
