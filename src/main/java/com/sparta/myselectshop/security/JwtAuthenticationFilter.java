package com.sparta.myselectshop.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.domain.user.dto.LoginRequestDto;
import com.sparta.myselectshop.domain.user.entity.UserRoleEnum;
import com.sparta.myselectshop.global.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/api/user/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					requestDto.getUsername(),
					requestDto.getPassword(),
					null
				)
			);
		} catch (IOException exception) {
			log.error(exception.getMessage());
			throw new RuntimeException(exception.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		String username = ((UserDetailsImpl)authResult.getPrincipal()).getUsername();
		UserRoleEnum role = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getRole();

		String token = jwtUtil.createToken(username, role);
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(401);
	}
}
