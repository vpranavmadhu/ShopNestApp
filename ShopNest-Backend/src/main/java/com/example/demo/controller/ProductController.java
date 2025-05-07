package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(originPatterns = "http://localhost:5173", allowCredentials = "true")
public class ProductController {
	
	ProductService productService;
	
	public ProductController(ProductService productService) {
		this. productService =  productService;
	}
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getProducts(@RequestParam String category, HttpServletRequest request) {
		try {
			
			//authenticate the user using filter
			User authenticatedUser = (User)request.getAttribute("authenticatedUser");
			if(authenticatedUser == null) {
				return ResponseEntity.status(401).body(Map.of("error", "Unauthorized access"));
			}
			
			//fetch products based on category
			List<Product> products = productService.getProductsByCategory(category);
			
			
			//build response
			Map<String, Object> response = new HashMap<>();
			
			//Add user info
			Map<String, String> userInfo = new HashMap<>();
			userInfo.put("name", authenticatedUser.getUsername());
			userInfo.put("role", authenticatedUser.getRole().name());
			response.put("user", userInfo);
			
			//Add product details
			List<Map<String, Object>> productList = new ArrayList<>();
			for(Product product : products) {
				Map<String, Object> productDetails = new HashMap<>();
				productDetails.put("product_id", product.getProductId());
				productDetails.put("name", product.getName());
				productDetails.put("description", product.getDescription());
				productDetails.put("price", product.getPrice());
				productDetails.put("stock", product.getStock());
				
				//Fetch product images
				List<String> images = productService.getProductImages(product.getProductId());
				productDetails.put("images", images);
				productList.add(productDetails);
			}
			response.put("products", productList);
			
			return ResponseEntity.ok(response);
			
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
		}
	}
}
