package com.sparta.myselectshop.domain.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sparta.myselectshop.domain.product.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.domain.product.dto.ProductRequestDto;
import com.sparta.myselectshop.domain.product.dto.ProductResponseDto;
import com.sparta.myselectshop.domain.product.entity.Product;
import com.sparta.myselectshop.domain.naver.dto.ItemDto;
import com.sparta.myselectshop.domain.product.repository.ProductRepository;
import com.sparta.myselectshop.domain.user.entity.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final static int MIN_MY_PRICE = 100;

	private final ProductRepository productRepository;

	public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
		Product product = productRepository.save(new Product(requestDto, user));
		return new ProductResponseDto(product);
	}

	@Transactional
	public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
		int myprice = requestDto.getMyprice();
		if (myprice < MIN_MY_PRICE) {
			throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해 주세요.");
		}

		Product product = productRepository.findById(id).orElseThrow(() ->
			new NullPointerException("해당 상품을 찾을 수 없습니다.")
		);

		product.update(requestDto);

		return new ProductResponseDto(product);
	}

	public List<ProductResponseDto> getProduct(User user) {
		List<Product> productList = productRepository.findAllByUser(user);
		List<ProductResponseDto> responseDtoList = new ArrayList<>();

		for (Product product : productList) {
			responseDtoList.add(new ProductResponseDto(product));
		}
		return responseDtoList;
	}

	@Transactional
	public void updateBySearch(Long id, ItemDto itemDto) {
		Product product = productRepository.findById(id).orElseThrow(
			() -> new NullPointerException("해당 상품은 존재하지 않습니다.")
		);
		product.updateByItemDto(itemDto);
	}

	public List<ProductResponseDto> getAllProducts() {
		List<Product> productList = productRepository.findAll();
		List<ProductResponseDto> responseDtoList = new ArrayList<>();

		for (Product product : productList) {
			responseDtoList.add(new ProductResponseDto(product));
		}
		return responseDtoList;
	}
}
