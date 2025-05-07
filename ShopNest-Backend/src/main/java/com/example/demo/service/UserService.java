package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	UserRepository userRepository;
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User register(User user) throws RuntimeException {
		if(userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("user name Already Taken");
		}
		if(userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email Already Registered");
		}
		 user.setPassword(passwordEncoder.encode(user.getPassword()));
		 
		 return userRepository.save(user);
	}
		 
		 
	
	
}
