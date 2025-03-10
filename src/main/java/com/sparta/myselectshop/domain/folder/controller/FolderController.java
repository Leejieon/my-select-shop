package com.sparta.myselectshop.domain.folder.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.myselectshop.domain.folder.dto.FolderRequestDto;
import com.sparta.myselectshop.domain.folder.dto.FolderResponseDto;
import com.sparta.myselectshop.domain.folder.service.FolderService;
import com.sparta.myselectshop.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FolderController {

	private final FolderService folderService;

	@PostMapping("/folders")
	public void addFolders(@RequestBody FolderRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<String> folderNames = requestDto.getFolderNames();
		folderService.addFolders(folderNames, userDetails.getUser());
	}

	@GetMapping("/folders")
	public List<FolderResponseDto> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return folderService.getFolders(userDetails.getUser());
	}
}
