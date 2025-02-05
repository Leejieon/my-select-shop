package com.sparta.myselectshop.mock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

import com.sparta.myselectshop.domain.user.entity.UserRoleEnum;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface MockUser {

	long id() default 1L;
	String username() default "mockUser";
	String password() default "password";
	String email() default "mock@email.com";
	UserRoleEnum role() default UserRoleEnum.USER;
}
