package com.sparta.myselectshop.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String email;

	private boolean admin = false;
	private String adminToken = "";
}
