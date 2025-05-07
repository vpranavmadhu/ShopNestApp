package com.example.demo.adminservices;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.JWTTokenRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;

@Service
public class AdminUserService {

	UserRepository userRepository;
	JWTTokenRepository jwtTokenRepository;
	OrderRepository orderRepository;
	OrderItemRepository orderItemRepository;

	public AdminUserService(UserRepository userRepository, JWTTokenRepository jwtTokenRepository,
			OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
		this.userRepository = userRepository;
		this.jwtTokenRepository = jwtTokenRepository;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
	}

	public User getUserById(int userId) {
		return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}

	public User modifyUser(Integer userId, String name, String email, String role) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

		if (name != null && !name.isEmpty()) {
			user.setUsername(name);
		}

		if (email != null && !email.isEmpty()) {
			user.setEmail(email);
		}

		if (role != null && !role.isEmpty()) {
			user.setRole(Role.valueOf(role));
		}

		jwtTokenRepository.deleteByUserId(user.getUserId());

		return userRepository.save(user);
	}

	public void deleteUser(int userId) {
		orderItemRepository.deleteByUserId(userId);
		orderRepository.deleteByUserId(userId);
		jwtTokenRepository.deleteByUserId(userId);
		userRepository.deleteById(userId);
	}
}
