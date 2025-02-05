package com.sparta.myselectshop.domain.product.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.myselectshop.domain.folder.entity.ProductFolder;
import com.sparta.myselectshop.domain.product.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.domain.product.dto.ProductRequestDto;
import com.sparta.myselectshop.domain.user.entity.User;
import com.sparta.myselectshop.global.Timestamped;
import com.sparta.myselectshop.domain.naver.dto.ItemDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product")
@NoArgsConstructor
public class Product extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String image;

	@Column(nullable = false)
	private String link;

	@Column(nullable = false)
	private int lprice;

	@Column(nullable = false)
	private int myprice;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "product")
	private List<ProductFolder> productFolderList = new ArrayList<>();


	public Product(ProductRequestDto requestDto, User user) {
		this.title = requestDto.getTitle();
		this.image = requestDto.getImage();
		this.link = requestDto.getLink();
		this.lprice = requestDto.getLprice();
		this.user = user;
	}

	public void update(ProductMypriceRequestDto requestDto) {
		this.myprice = requestDto.getMyprice();
	}

	public void updateByItemDto(ItemDto itemDto) {
		this.lprice = itemDto.getLprice();
	}
}
