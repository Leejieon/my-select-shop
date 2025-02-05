package com.sparta.myselectshop.domain.folder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sparta.myselectshop.domain.folder.dto.FolderResponseDto;
import com.sparta.myselectshop.domain.folder.entity.Folder;
import com.sparta.myselectshop.domain.folder.repository.FolderRepository;
import com.sparta.myselectshop.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FolderService {

	private final FolderRepository folderRepository;

	public void addFolders(List<String> folderNames, User user) {
		List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames);
		List<Folder> folderList = new ArrayList<>();

		for (String folderName : folderNames) {
			if (!isExistFolderName(folderName, existFolderList)) {
				folderList.add(new Folder(folderName, user));
			} else {
				throw new IllegalArgumentException("중복되는 폴더명이 존재합니다.");
			}
		}
		folderRepository.saveAll(folderList);
	}

	public List<FolderResponseDto> getFolders(User user) {
		List<Folder> folderList = folderRepository.findAllByUser(user);
		List<FolderResponseDto> responseDtoList = new ArrayList<>();

		for (Folder folder : folderList) {
			responseDtoList.add(new FolderResponseDto(folder));
		}
		return responseDtoList;
	}

	private Boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
		for (Folder existFolder : existFolderList) {
			if (folderName.equals(existFolder.getName())) {
				return true;
			}
		}
		return false;
	}
}
