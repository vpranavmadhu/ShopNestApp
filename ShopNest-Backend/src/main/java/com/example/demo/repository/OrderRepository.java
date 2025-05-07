package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatus;

import jakarta.transaction.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>{

	@Modifying
	@Transactional
	@Query("DELETE FROM Order o WHERE o.userId = :userId")
	public void deleteByUserId(int userId);
	
	@Query("SELECT o FROM Order o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year and o.status = 'SUCCESS'")
	List<Order> findSuccessfulOrderByMonthAndYear(int month, int year);
	
	@Query("SELECT o FROM Order o WHERE DATE(o.createdAt) = :date AND o.status = 'SUCCESS'")
	List<Order> findSuccessfulOrderByDate(LocalDate date);
	
	@Query("SELECT o FROM Order o WHERE YEAR(o.createdAt) = :year AND o.status = 'SUCCESS'")
	List<Order> findSuccessfulOrderByYear(int year);
	
	@Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'SUCCESS'")
	BigDecimal calculateOverallBusiness();
	
	@Query("SELECT o FROM Order o WHERE o.status = :status")
	List<Order> findAllByStatus(OrderStatus status);

}
