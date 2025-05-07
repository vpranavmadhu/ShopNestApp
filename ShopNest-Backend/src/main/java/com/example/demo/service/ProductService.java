package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImage;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

	ProductRepository productRepository;
	ProductImageRepository productImageRepository;
	CategoryRepository categoryRepository;
	public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
		this.categoryRepository = categoryRepository;
	}
	
	
	public List<Product> getProductsByCategory(String categoryName) {
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<Category> categoryOp = categoryRepository.findByCategoryName(categoryName);
		if(categoryOp.isPresent()) {
			Category category = categoryOp.get();
			List<Product> products = productRepository.findByCategory_categoryId(category.getCategoryId());
			
			return products;
		} else {
			throw new RuntimeException("Category not found");
		}
		} else {
			return productRepository.findAll();
		}
	}
	
	public List<String> getProductImages(Integer productId) {
		
		List<ProductImage> productImages = productImageRepository.findByProduct_productId(productId);
		List<String> imageUrls = new ArrayList<>();
		for(ProductImage image : productImages) {
			imageUrls.add(image.getImageUrl());
		}
		
		return imageUrls;
	}
	
}
