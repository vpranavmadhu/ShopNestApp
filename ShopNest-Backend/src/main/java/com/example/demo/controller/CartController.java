package com.example.demo.controller;


import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
public class CartController {

	CartService cartService;
	UserRepository userRepository;
	
	public CartController(CartService cartService, UserRepository userRepository) {
		this.userRepository = userRepository;
		this.cartService = cartService;
	}
	@GetMapping("items/count")
	public ResponseEntity<Integer> getCartItemCount(@RequestParam String username) {
		
		User user = userRepository.findByUsername(username).orElseThrow(()-> new  IllegalArgumentException("User Not Found With Username : " + username));
		int count = cartService.getCartItemCount(user.getUserId());
		
		return ResponseEntity.ok(count);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Void> addToCart(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		int quantity = request.containsKey("quantity")?(int)request.get("quantity") : 1;
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User Not Found With Username: " + username));
		
		cartService.addToCart(user.getUserId(), productId, quantity);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	@GetMapping("/items")
	public ResponseEntity<Map<String, Object>> getCartItems(HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		
		Map<String, Object> cartItems = cartService.getCartItems(user.getUserId());
		
		return ResponseEntity.ok(cartItems);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Void> updateCartItemQuantity(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		int quantity = (int) request.get("quantity");
		System.out.println(username);
		System.out.println(productId);
		System.out.println(quantity);
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
		cartService.updateCartItemQuantity(user.getUserId(), productId, quantity);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteCartItem(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
		
		cartService.deleteCartItem(user.getUserId(), productId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		
		
	}
	
	@DeleteMapping("/deleteall")
	public ResponseEntity<Void> clearCartItems(@RequestBody Map<String, Object> request) {
		
		String username = (String) request.get("username");
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found "));
		
		cartService.clearAllCartItems(user.getUserId());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
