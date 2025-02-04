package com.sparta.myselectshop.domain.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sparta.myselectshop.domain.user.dto.SignupRequestDto;
import com.sparta.myselectshop.domain.user.dto.UserInfoDto;
import com.sparta.myselectshop.domain.user.entity.UserRoleEnum;
import com.sparta.myselectshop.domain.user.service.UserService;
import com.sparta.myselectshop.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	@GetMapping("/user/login-page")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/user/signup")
	public String SignupPage() {
		return "signup";
	}

	@PostMapping("/user/signup")
	public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
		// Validation Exception Handling
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if (fieldErrors.size() > 0) {
			for (FieldError fieldError : fieldErrors) {
				log.info(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
			}
			return "redirect:/api/user/signup";
		}

		userService.signup(requestDto);

		return "redirect:/api/user/login-page";
	}

	@GetMapping("/user-info")
	@ResponseBody
	public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		String username = userDetails.getUsername();
		UserRoleEnum role = userDetails.getUser().getRole();
		boolean isAdmin = (role == UserRoleEnum.ADMIN);

		return new UserInfoDto(username, isAdmin);
	}
}
