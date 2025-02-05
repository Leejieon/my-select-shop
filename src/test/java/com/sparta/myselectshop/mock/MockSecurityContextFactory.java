package com.sparta.myselectshop.mock;

import java.lang.annotation.Annotation;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.sparta.myselectshop.domain.user.entity.User;
import com.sparta.myselectshop.security.UserDetailsImpl;

public class MockSecurityContextFactory implements WithSecurityContextFactory<MockUser> {

	@Override
	public SecurityContext createSecurityContext(MockUser annotation) {
		User mockUser = new User(annotation.username(), annotation.password(), annotation.email(), annotation.role());
		mockUser.setId(annotation.id());

		UserDetailsImpl userDetails = new UserDetailsImpl(mockUser);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
			userDetails, "", userDetails.getAuthorities()
		);
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(token);
		return context;
	}
}
