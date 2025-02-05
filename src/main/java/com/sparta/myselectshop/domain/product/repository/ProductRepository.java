package com.sparta.myselectshop.domain.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.myselectshop.domain.product.entity.Product;
import com.sparta.myselectshop.domain.user.entity.User;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findAllByUser(User user);
}
