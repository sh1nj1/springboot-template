package com.vrerv.springboottemplate.server.common.infra.security;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * add @EntityListeners(PasswordEncodingEntityListener.class) to the Entity that extends PasswordEncodable
 */
@Slf4j
@NoArgsConstructor
@Component
public class PasswordEncodingEntityListener {

	public interface PasswordEncodable {
		String getPassword();
		void setPassword(String password);
	}

	private PasswordEncoder passwordEncoder;

	public PasswordEncodingEntityListener(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@PrePersist
	public void prePersist(PasswordEncodable user) {
		String password = user.getPassword();
		user.setPassword(passwordEncoder.encode(password));
	}

	@PreUpdate
	public void preUpdate(PasswordEncodable user) {
		String password = user.getPassword();
		if (!password.startsWith("$2a")) {
			user.setPassword(passwordEncoder.encode(password));
		}
	}
}