package com.sparta.myselectshop.domain.folder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.myselectshop.domain.folder.entity.Folder;
import com.sparta.myselectshop.domain.user.entity.User;

public interface FolderRepository extends JpaRepository<Folder, Long> {
	List<Folder> findAllByUser(User user);

	List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);
}
