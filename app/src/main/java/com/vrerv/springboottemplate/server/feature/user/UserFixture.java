package com.vrerv.springboottemplate.server.feature.user;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class UserFixture {

	private final UserRepo userRepo;

	@PostConstruct
	public void init() {

		userRepo.findByAccount("1234").orElseGet(() -> {
			User user = User.builder()
					.account("1234")
					.password("1234")
					.build();
			return userRepo.save(user);
		});
	}
}
