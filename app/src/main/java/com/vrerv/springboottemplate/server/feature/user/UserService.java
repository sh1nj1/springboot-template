package com.vrerv.springboottemplate.server.feature.user;

import javax.validation.Valid;

import com.vrerv.springboottemplate.server.base.service.UserDetailsServiceImpl;
import com.vrerv.springboottemplate.server.common.infra.security.UserTokenService;
import com.vrerv.springboottemplate.server.feature.user.model.LoginModel;
import com.vrerv.springboottemplate.server.feature.user.model.LoginResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional
@Service
public class UserService {

	private final UserTokenService userTokenService;
	private final UserDetailsServiceImpl userDetailsService;

	public LoginResponseModel auth(@Valid LoginModel dto) {

		UserDetails userDetails = userDetailsService.authenticate(dto.getAccount(), dto.getPassword());
		String token = userTokenService.createToken(dto.getAccount(), dto.getAccount());
		return LoginResponseModel.builder()
				.account(userDetails.getUsername())
				.accessToken(token)
				.build();
	}
}
