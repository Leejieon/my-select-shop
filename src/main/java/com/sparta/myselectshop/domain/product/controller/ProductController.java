package com.sparta.myselectshop.domain.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.myselectshop.domain.product.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.domain.product.dto.ProductRequestDto;
import com.sparta.myselectshop.domain.product.dto.ProductResponseDto;
import com.sparta.myselectshop.domain.product.service.ProductService;
import com.sparta.myselectshop.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

	private final ProductService productService;

	@PostMapping("/products")
	public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return productService.createProduct(requestDto, userDetails.getUser());
	}

	@PutMapping("/products/{id}")
	public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
		return productService.updateProduct(id, requestDto);
	}

	@GetMapping("/products")
	public Page<ProductResponseDto> getProducts(
		@RequestParam("page") int page,
		@RequestParam("size") int size,
		@RequestParam("sortBy") String sortBy,
		@RequestParam("isAsc") boolean isAsc,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return productService.getProduct(userDetails.getUser(), page - 1, size, sortBy, isAsc);
	}

	@GetMapping("/admin/products")
	public List<ProductResponseDto> getAllProducts() {
		return productService.getAllProducts();
	}

	@PostMapping("/products/{productId}/folder")
	public void addFolder(
		@PathVariable Long productId,
		@RequestParam Long folderId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		productService.addFolder(productId, folderId, userDetails.getUser());
	}
}
