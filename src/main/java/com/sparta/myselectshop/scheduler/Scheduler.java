package com.sparta.myselectshop.scheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sparta.myselectshop.domain.product.entity.Product;
import com.sparta.myselectshop.domain.naver.dto.ItemDto;
import com.sparta.myselectshop.domain.naver.service.NaverApiService;
import com.sparta.myselectshop.domain.product.repository.ProductRepository;
import com.sparta.myselectshop.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Scheduler")
@Component
@RequiredArgsConstructor
public class Scheduler {

	private final NaverApiService naverApiService;
	private final ProductService productService;
	private final ProductRepository productRepository;

	// order : sec, min, hour, day, month, week
	@Scheduled(cron = "0 0 1 * * *") // every 1 AM
	public void updatePrice() throws InterruptedException {
		log.info("가격 업데이트 실행");

		List<Product> productList = productRepository.findAll();
		for (Product product : productList) {
			// search one product per every 1sec
			TimeUnit.SECONDS.sleep(1);

			String title = product.getTitle();
			List<ItemDto> itemDtoList = naverApiService.searchItems(title);

			if (itemDtoList.size() > 0) {
				ItemDto itemDto = itemDtoList.get(0);
				Long id = product.getId();
				try {
					productService.updateBySearch(id, itemDto);
				} catch (Exception e) {
					log.error(id + " : " + e.getMessage());
				}
			}
		}
	}

}
