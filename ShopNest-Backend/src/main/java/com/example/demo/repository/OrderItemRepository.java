package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.OrderItem;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
	List<OrderItem> findByOrderId(String orderId);

	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.userId = :userId AND oi.order.status = 'SUCCESS' ORDER BY oi.id DESC")
	List<OrderItem> findSuccessfulOrderItemsByUserId(int userId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM OrderItem oi WHERE oi.order.userId = :userId")
	public void deleteByUserId(int userId);

}
