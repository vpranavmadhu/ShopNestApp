package com.example.demo.adminservices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatus;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class AdminBusinessService {
	OrderRepository orderRepository;
	OrderItemRepository orderItemRepository;
	ProductRepository productRepository;

	public AdminBusinessService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
	}

	public Map<String, Object> calculateMonthlyBusiness(int month, int year) {

		if (year < 2000 || year > 2025) {
			throw new IllegalArgumentException("Invalid year");
		}

		List<Order> successfulOrders = orderRepository.findSuccessfulOrderByMonthAndYear(month, year);

		double totalBusiness = 0.0;
		Map<String, Integer> categorySales = new HashMap<>();

		for (Order order : successfulOrders) {
			totalBusiness += order.getTotalAmount().doubleValue();

			List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
			for (OrderItem item : orderItems) {
				String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
				categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("totalBusiness", totalBusiness);
		response.put("categorySales", categorySales);

		return response;

	}

	public Map<String, Object> calculateDailyBusiness(LocalDate date) {
		if (date == null) {
			throw new IllegalArgumentException("Invalid date as date cannot be null");
		}

		List<Order> successfulOrders = orderRepository.findSuccessfulOrderByDate(date);

		double totalBusiness = 0.0;
		Map<String, Integer> categorySales = new HashMap<>();

		for (Order order : successfulOrders) {
			totalBusiness += order.getTotalAmount().doubleValue();

			List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
			for (OrderItem item : orderItems) {
				String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
				categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("totalBusiness", totalBusiness);
		response.put("categorySales", categorySales);

		return response;

	}

	public Map<String, Object> calculateYearlyBusiness(int year) {

		if (year < 2000 || year > 2025) {
			throw new IllegalArgumentException("Invalid year: " + year);
		}

		List<Order> successfulOrders = orderRepository.findSuccessfulOrderByYear(year);

		double totalBusiness = 0.0;
		Map<String, Integer> categorySales = new HashMap<>();

		for (Order order : successfulOrders) {
			totalBusiness += order.getTotalAmount().doubleValue();

			List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
			for (OrderItem item : orderItems) {
				String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
				categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("totalBusiness", totalBusiness);
		response.put("categorySales", categorySales);

		return response;

	}

	public Map<String, Object> calculateOverallBusiness() {
		
		System.out.println("reached inside service");

		BigDecimal totalOverallBusiness = orderRepository.calculateOverallBusiness();
		System.out.println("totalocerall" + totalOverallBusiness);
		List<Order> successfulOrders = orderRepository.findAllByStatus(OrderStatus.SUCCESS);
		System.out.println("succ order: " + successfulOrders);

		Map<String, Integer> categorySales = new HashMap<>();

		for (Order order : successfulOrders) {
			List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
			for (OrderItem item : orderItems) {
				String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
				categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("totalBusiness", totalOverallBusiness.doubleValue());
		response.put("categorySales", categorySales);
		System.out.println("res in ser: " + response);
		return response;

	}
}
