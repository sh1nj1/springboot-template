package com.vrerv.springboottemplate.server.feature.user;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationRejectedException extends AuthenticationException {
	public AuthenticationRejectedException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
