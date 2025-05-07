package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.JWTToken;
import java.util.List;

@Repository
public interface JWTTokenRepository extends JpaRepository<JWTToken, Integer>{
	
	@Query("SELECT t FROM JWTToken t WHERE t.user.userId=:userId")
	public JWTToken findByuser_id(int userId);
	
	public Optional<JWTToken> findByToken(String token);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM JWTToken t WHERE t.user.userId = :userId")
	public void deleteByUserId(int userId);
}
