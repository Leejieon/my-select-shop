package com.sparta.myselectshop.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sparta.myselectshop.domain.user.dto.SignupRequestDto;
import com.sparta.myselectshop.domain.user.entity.User;
import com.sparta.myselectshop.domain.user.entity.UserRoleEnum;
import com.sparta.myselectshop.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	private SignupRequestDto signupRequestDto;

	@BeforeEach
	void setUp() {
		signupRequestDto = new SignupRequestDto();
		signupRequestDto.setUsername("testname");
		signupRequestDto.setPassword("1234");
		signupRequestDto.setEmail("test@email.com");
	}

	@DisplayName("정상적인 사용자 회원 가입 테스트")
	@Test
	void signup_success_test() {
		// When
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

		// Then
		assertThatCode(() -> userService.signup(signupRequestDto))
				.doesNotThrowAnyException();
		verify(userRepository).save(any(User.class));
	}

	@DisplayName("중복된 사용자 이름을 사용하면 예외가 발생한다.")
	@Test
	void signup_duplication_username_test() {
		// When
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

		// Then
		assertThatThrownBy(() -> userService.signup(signupRequestDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("중복된 사용자가 존재합니다.");
	}

	@DisplayName("중복된 이메일을 사용하면 예외가 발생한다.")
	@Test
	void signup_duplication_email_test() {
		// When
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

		// Then
		assertThatThrownBy(() -> userService.signup(signupRequestDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("중복된 Email 입니다.");
	}

	@DisplayName("관리자는 유효한 ADMIN TOKEN 을 이용해 가입해야 한다.")
	@Test
	void signup_admin_with_valid_token_test() {
		// Given
		signupRequestDto.setAdmin(true);
		signupRequestDto.setAdminToken("AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC");

		// When
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

		// Then
		assertThatCode(() -> userService.signup(signupRequestDto))
			.doesNotThrowAnyException();
		verify(userRepository).save(argThat(user -> user.getRole() == UserRoleEnum.ADMIN));
	}

	@DisplayName("유효하지 않은 ADMIN TOKEN 을 사용하면 예외가 발생한다.")
	@Test
	void signup_admin_with_invalid_token_test() {
		// Given
		signupRequestDto.setAdmin(true);
		signupRequestDto.setAdminToken("invalidToken");

		// When
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

		// Then
		assertThatThrownBy(() -> userService.signup(signupRequestDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("관리자 암호가 틀려 등록이 불가능합니다.");
	}
}