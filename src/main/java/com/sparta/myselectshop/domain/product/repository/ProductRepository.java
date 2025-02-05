package com.sparta.myselectshop.domain.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.myselectshop.domain.product.entity.Product;
import com.sparta.myselectshop.domain.user.entity.User;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findAllByUser(User user, Pageable pageable);

	Page<Product> findAllByUserAndProductFolderList_FolderId(User user, Long folderId, Pageable pageable);
}
