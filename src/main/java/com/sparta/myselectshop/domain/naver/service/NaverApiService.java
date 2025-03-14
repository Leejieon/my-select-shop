package com.sparta.myselectshop.domain.naver.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sparta.myselectshop.domain.naver.dto.ItemDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "NAVER API")
@Service
public class NaverApiService {

	private final RestTemplate restTemplate;

	@Value("${api.open.naver.client-id}")
	private String clientId;

	@Value("${api.open.naver.client-secret}")
	private String clientSecret;

	public NaverApiService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	public List<ItemDto> searchItems(String query) {
		// Request URL
		URI uri = UriComponentsBuilder
			.fromUriString("https://openapi.naver.com")
			.path("/v1/search/shop.json")
			.queryParam("display", 15)
			.queryParam("query", query)
			.encode()
			.build()
			.toUri();
		log.info("uri = " + uri);

		RequestEntity<Void> requestEntity = RequestEntity
			.get(uri)
			.header("X-Naver-Client-Id", clientId)
			.header("X-Naver-Client-Secret", clientSecret)
			.build();

		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

		log.info("NAVER API Status Code : " + responseEntity.getStatusCode());

		return fromJSONtoItems(responseEntity.getBody());
	}

	public List<ItemDto> fromJSONtoItems(String responseEntity) {
		JSONObject jsonObject = new JSONObject(responseEntity);
		JSONArray items = jsonObject.getJSONArray("items");
		List<ItemDto> itemDtoList = new ArrayList<>();

		for (Object item : items) {
			ItemDto itemDto = new ItemDto((JSONObject)item);
			itemDtoList.add(itemDto);
		}
		return itemDtoList;
	}
}
