package com.sparta.myselectshop.domain.user.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sparta.myselectshop.domain.folder.dto.FolderResponseDto;
import com.sparta.myselectshop.domain.folder.entity.Folder;
import com.sparta.myselectshop.domain.folder.service.FolderService;
import com.sparta.myselectshop.domain.user.dto.SignupRequestDto;
import com.sparta.myselectshop.domain.user.entity.User;
import com.sparta.myselectshop.domain.user.service.UserService;
import com.sparta.myselectshop.mock.MockUser;
import com.sparta.myselectshop.security.UserDetailsImpl;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private FolderService folderService;

	private SignupRequestDto signupRequestDto;

	@BeforeEach
	void setUp() {
		signupRequestDto = new SignupRequestDto();
		signupRequestDto.setUsername("testname");
		signupRequestDto.setPassword("1234");
		signupRequestDto.setEmail("test@email.com");
	}

	@DisplayName("사용자는 이름, 비밀번호, 이메일을 입력해 회원가입할 수 있다.")
	@Test
	@MockUser
	void signup_success_test() throws Exception {
		// When
		mockMvc.perform(post("/api/user/signup")
				.with(csrf())
				.param("username", signupRequestDto.getUsername())
				.param("password", signupRequestDto.getPassword())
				.param("email", signupRequestDto.getEmail()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/api/user/login-page"));

		// Then
		verify(userService).signup(any(SignupRequestDto.class));
	}

	@DisplayName("회원가입 시 잘못된 입력값이 있다면 회원가입 페이지로 리다이렉트 된다.")
	@Test
	@MockUser
	void signup_invalid_filed_test() throws Exception {
		mockMvc.perform(post("/api/user/signup")
				.with(csrf())
				.param("username", "")
				.param("password", "short")
				.param("email", "invalid"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/api/user/signup"));
	}

	@DisplayName("사용자는 자신의 정보를 가져올 수 있다.")
	@Test
	@MockUser(username = "testUser")
	void get_user_info_test() throws Exception {
		// When & Then
		mockMvc.perform(get("/api/user-info"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("testUser"))
			.andExpect(jsonPath("$.admin").value(false));
	}

	@DisplayName("사용자는 자신의 폴더 정보를 가져올 수 있다.")
	@Test
	@MockUser
	void get_user_folder_test() throws Exception {
		// Given
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		User user = userDetails.getUser();

		List<FolderResponseDto> folders = Arrays.asList(
			new FolderResponseDto(new Folder("Folder1", user)),
			new FolderResponseDto(new Folder("Folder2", user))
		);

		// When
		when(folderService.getFolders(any())).thenReturn(folders);

		// Then
		mockMvc.perform(get("/api/user-folder"))
			.andExpect(status().isOk())
			.andExpect(view().name("index :: #fragment"))
			.andExpect(model().attribute("folders", folders));
	}
}