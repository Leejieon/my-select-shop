package com.sparta.myselectshop.domain.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.myselectshop.domain.folder.entity.Folder;
import com.sparta.myselectshop.domain.folder.entity.ProductFolder;
import com.sparta.myselectshop.domain.folder.repository.FolderRepository;
import com.sparta.myselectshop.domain.folder.repository.ProductFolderRepository;
import com.sparta.myselectshop.domain.product.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.domain.product.dto.ProductRequestDto;
import com.sparta.myselectshop.domain.product.dto.ProductResponseDto;
import com.sparta.myselectshop.domain.product.entity.Product;
import com.sparta.myselectshop.domain.naver.dto.ItemDto;
import com.sparta.myselectshop.domain.product.repository.ProductRepository;
import com.sparta.myselectshop.domain.user.entity.User;
import com.sparta.myselectshop.domain.user.entity.UserRoleEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	public final static int MIN_MY_PRICE = 100;

	private final ProductRepository productRepository;
	private final FolderRepository folderRepository;
	private final ProductFolderRepository productFolderRepository;

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

	@Transactional(readOnly = true)
	public Page<ProductResponseDto> getProduct(User user, int page, int size, String sortBy, boolean isAsc) {
		// Paging
		Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(direction, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);

		UserRoleEnum role = user.getRole();

		Page<Product> productList;
		if (role == UserRoleEnum.USER) {
			productList = productRepository.findAllByUser(user, pageable);
		} else {
			productList = productRepository.findAll(pageable);
		}
		return productList.map(ProductResponseDto::new);
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

	public void addFolder(Long productId, Long folderId, User user) {
		// Get Product
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new NullPointerException("상품이 존재하지 않습니다."));

		// Get Folder
		Folder folder = folderRepository.findById(folderId)
			.orElseThrow(() -> new NullPointerException("해당 폴더가 존재하지 않습니다."));

		// Check user own
		if (!product.getUser().getId().equals(user.getId()) || !folder.getUser().getId().equals(user.getId())) {
			throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
		}

		// Check Duplication
		Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(product, folder);
		if (overlapFolder.isPresent()) {
			throw new IllegalArgumentException("중복된 폴더입니다.");
		}

		productFolderRepository.save(new ProductFolder(product, folder));
	}

	@Transactional(readOnly = true)
	public Page<ProductResponseDto> getProductsInFolder(Long folderId, int page, int size, String sortBy, boolean isAsc,
		User user) {
		Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(direction, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Product> products = productRepository.findAllByUserAndProductFolderList_FolderId(
			user, folderId, pageable);
		return products.map(ProductResponseDto::new);
	}
}
