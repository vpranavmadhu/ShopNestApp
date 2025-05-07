package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImage;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CartService {
	
	CartRepository cartRepository;
	UserRepository userRepository;
	ProductRepository productRepository;
	ProductImageRepository productImageRepository;
	
	public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, ProductImageRepository productImageRepository) {
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
	}
	
	public int getCartItemCount(int userId) {
		return cartRepository.countTotalItems(userId);
	}
	
	public void addToCart(int userId, int productId, int quantity) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User Not Found With ID: " + userId));
		Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product Not FOund With ID: " + productId));
		
		//fetch cartItem with userID and productId to check if already userId with productId entry exists
		Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(userId, productId);
		if(existingItem.isPresent()) {
			//update the quantity of existing cart Item with the product
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			cartRepository.save(cartItem);
			
		}
		else {
			//create new cart item with userId and new product and save
			CartItem newItem = new CartItem( user, product, quantity);
			cartRepository.save(newItem);
		}
	}
	
	public Map<String, Object> getCartItems(int userId) {
		List<CartItem> cartItems = cartRepository.findCartItemsWithProductDetails(userId);
		
		Map<String, Object> response = new HashMap<>();
		
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User Not Found With ID: " + userId));
		
		response.put("username", user.getUsername());
		response.put("role", user.getRole().name());
		
		List<Map<String, Object>> products = new ArrayList<>();
		
		int overallTotalPrice = 0;
		
		
		for(CartItem cartItem: cartItems) {
			Map<String, Object> productDetails = new HashMap<>();
			Product product = cartItem.getProduct();
			
			List<ProductImage> productImages = productImageRepository.findByProduct_productId(product.getProductId());
			String imageUrls = null;
			if(productImages!=null && !productImages.isEmpty()) {
				imageUrls = productImages.get(0).getImageUrl();
			} else {
				imageUrls = "default-image-url";
				
			}
			productDetails.put("product_id", product.getProductId());
				productDetails.put("image_url", imageUrls);
				productDetails.put("name", product.getName());
				productDetails.put("description", product.getDescription());
				productDetails.put("price_per_unit", product.getPrice());
				productDetails.put("quantity", cartItem.getQuantity());
				productDetails.put("total_price", cartItem.getQuantity()*product.getPrice().doubleValue());
				
				products.add(productDetails);
				
			overallTotalPrice += cartItem.getQuantity()*product.getPrice().doubleValue();
			
		}
		Map<String, Object> cart = new HashMap<>();
		cart.put("products", products);
		cart.put("overall_total_price", overallTotalPrice);
		
		response.put("cart", cart);
		
		return response;
	}
	
	public void updateCartItemQuantity(int userId, int productId, int quantity) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
		Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
		
		Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(userId, productId);
		
		if(existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			if(quantity == 0) {
				deleteCartItem(userId, productId);
			} else {
				cartItem.setQuantity(quantity);
				cartRepository.save(cartItem);
			}
		}
	}
	
	public void deleteCartItem(int userId, int productId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
		Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
		
		cartRepository.deleteCartItem(userId, productId);
	}
	
	public void clearAllCartItems(int userId) {
		cartRepository.deleteAllCartItemsByUser(userId);
	}

}
