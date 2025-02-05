package com.sparta.myselectshop.domain.folder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.myselectshop.domain.folder.entity.Folder;
import com.sparta.myselectshop.domain.folder.entity.ProductFolder;
import com.sparta.myselectshop.domain.product.entity.Product;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
	Optional<ProductFolder> findByProductAndFolder(Product product, Folder folder);
}
