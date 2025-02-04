package com.sparta.myselectshop.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.myselectshop.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
