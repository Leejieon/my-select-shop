package com.sparta.myselectshop.domain.user.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.sparta.myselectshop.domain.user.entity.User;
import com.sparta.myselectshop.domain.user.entity.UserRoleEnum;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@DisplayName("사용자의 이름을 통해 사용자를 검색할 수 있다.")
	@Test
	void findUserByUsernameTest() {
		// Given
		User user = new User(
			"testname",
			"1234",
			"testname@email.com",
			UserRoleEnum.USER
		);
		userRepository.save(user);

		// When
		User rstUser = userRepository.findByUsername(user.getUsername()).orElse(null);

		// Then
		assertThat(rstUser).isNotNull()
			.extracting("username", "email", "role")
			.containsExactlyInAnyOrder("testname", "testname@email.com", UserRoleEnum.USER);
	}

	@DisplayName("사용자의 이메일을 통해 사용자를 검색할 수 있다.")
	@Test
	void findUserByUserEmailTest() {
		// Given
		User user = new User(
			"testname",
			"1234",
			"testname@email.com",
			UserRoleEnum.USER
		);
		userRepository.save(user);

		// When
		User rstUser = userRepository.findByEmail(user.getEmail()).orElse(null);

		// Then
		assertThat(rstUser).isNotNull()
			.extracting("username", "email", "role")
			.containsExactlyInAnyOrder("testname", "testname@email.com", UserRoleEnum.USER);
	}
}