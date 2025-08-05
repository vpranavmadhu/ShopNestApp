package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImage;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class OrderService {
	
	OrderItemRepository orderItemRepository;
	ProductRepository productRepository;
	ProductImageRepository productImageRepository;
	
	public OrderService(OrderItemRepository orderItemRepository, ProductRepository productRepository, ProductImageRepository productImageRepository) {
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
	}
	
	public Map<String, Object> getOrdersForUser(User user) {
		
		 List<OrderItem> orderItems = orderItemRepository.findSuccessfulOrderItemsByUserId(user.getUserId());
		
		 Map<String, Object> response = new HashMap<>();
		 response.put("username", user.getUsername());
		 response.put("role", user.getRole().name());
		 
		 List<Map<String, Object>> products = new ArrayList<>();
		 for(OrderItem item : orderItems) {
			 
			 Product product = productRepository.findById(item.getProductId()).orElse(null);
			 if(product == null) {
				 continue;
			 }
			  List<ProductImage> images  = productImageRepository.findByProduct_productId(product.getProductId());
			  String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl(); 
			  
			  Map<String, Object> productDetails = new HashMap<>();
			  productDetails.put("order_id", item.getOrder().getOrderId());
			  productDetails.put("quantity", item.getQuantity());
			  productDetails.put("total_price", item.getTotalPrice());
			  productDetails.put("image_url", imageUrl);
			  productDetails.put("product_id", product.getProductId());
			  productDetails.put("name", product.getName());
			  productDetails.put("description", product.getDescription());
			  productDetails.put("price_per_unit", item.getPricePerUnit());
			  productDetails.put("orderedOn", item.getOrder().getUpdatedAt());
			  products.add(productDetails);
			  
		 }
		 
		 response.put("products", products);
		
		return response;
	}
	

}
