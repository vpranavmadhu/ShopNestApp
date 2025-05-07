package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("api/user")
public class UserProfileController {
	
	UserRepository userRepository;
	AuthService authService;
	public UserProfileController(UserRepository userRepository, AuthService authService) {
		this.userRepository  = userRepository;
		this.authService = authService;
	}
	
	
	
	@GetMapping("/profile")
	public ResponseEntity<Map<String, String>> profile(HttpServletRequest request) {

		try {
			User user = (User) request.getAttribute("authenticatedUser");
			
			

			Map<String, String> response = new HashMap<>();
			response.put("name", user.getUsername());
			response.put("email", user.getEmail());
			
			System.out.println("fetching profile done");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Something went Wrong"));
		}

	}
	
	@PutMapping("/update")
	public ResponseEntity<?> editProfile(HttpServletRequest request, @RequestBody Map<String, String> ModifiedUser) {
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			
			user.setUsername(ModifiedUser.get("username"));
			user.setEmail(ModifiedUser.get("email"));
			//saved modified user
			User modifiedUser = userRepository.save(user);
			
			
			System.out.println("update done");
			
			
			return ResponseEntity.ok(modifiedUser);
			
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "something went wrong"));
		}
	}

}
