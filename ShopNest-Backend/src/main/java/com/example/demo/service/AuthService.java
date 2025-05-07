package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.JWTToken;
import com.example.demo.entity.User;
import com.example.demo.repository.JWTTokenRepository;
import com.example.demo.repository.UserRepository;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
	
	private final Key SIGNING_KEY;
	UserRepository userRepository;
	JWTTokenRepository jwtTokenRepository;
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public AuthService(UserRepository userRepository, JWTTokenRepository jwtTokenRepository, @Value("${jwt.secret}") String jwtSecret) {
		this.userRepository = userRepository;
		this.jwtTokenRepository = jwtTokenRepository;
		this.SIGNING_KEY=Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
	
	
	public User authenticate(String username, String password) {
		Optional<User> exUser = userRepository.findByUsername(username);
		if(exUser.isPresent()) {
			User user = exUser.get();
			if(passwordEncoder.matches(password, user.getPassword())) {
				return user;
			} else {
				throw new RuntimeException("Invalid password user");
			}
			
		} else {
			throw new RuntimeException("Invalid username");
		}
	}
	
	public String generateToken(User user) {
		
		String token;
		LocalDateTime currentTime = LocalDateTime.now();
		JWTToken exToken = jwtTokenRepository.findByuser_id(user.getUserId());
		if(exToken!=null && currentTime.isBefore(exToken.getExpiresAt())) {
			token = exToken.getToken();
		}
		else {
			token = generateNewToken(user);
			if(exToken!=null) {
				jwtTokenRepository.delete(exToken);
			}
			saveToken(user, token);
		}
		System.out.println("tokn: " + token);
		return token;
	}
	
	public String generateNewToken(User user) {
		System.out.println("enetered new token");
		JwtBuilder builder = Jwts.builder();
		builder.setSubject(String.valueOf(user.getUserId()));
		builder.claim("role", user.getRole().name());
		builder.setIssuedAt(new Date());
		builder.setExpiration(new Date(System.currentTimeMillis()+3600000));
		builder.signWith(SIGNING_KEY);
		String token = builder.compact();
		System.out.println("generated new token");
		return token;
	}
	
	public void saveToken(User user, String Token) {
		System.out.println("enetered token save");
		JWTToken jwtToken = new JWTToken(user, Token, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
		jwtTokenRepository.save(jwtToken);
		System.out.println("saved");
		
	}
	
	public boolean validateToken(String token) {
		System.out.println("validating token");
		try {
			//parse and validate token
			Jwts.parserBuilder()
			.setSigningKey(SIGNING_KEY)
			.build()
			.parseClaimsJws(token);
			
			//check if token is present in DB and is not expired
			Optional<JWTToken> jwtToken = jwtTokenRepository.findByToken(token);
			if(jwtToken.isPresent()) {
				return jwtToken.get().getExpiresAt().isAfter(LocalDateTime.now());
			}
			
			return false;
			
		} catch (Exception e) {
			System.out.println("Token validation Failed " + e.getMessage());
			return false;
			
		}
	}
	
	public String extractUserId(String token) {
		return Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token).getBody().getSubject();
				
	}
	
	public void logout(User user) {
		jwtTokenRepository.deleteByUserId(user.getUserId());
	}
}
