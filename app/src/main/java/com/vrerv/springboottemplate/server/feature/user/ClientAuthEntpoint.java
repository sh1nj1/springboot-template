package com.vrerv.springboottemplate.server.feature.user;

import javax.validation.Valid;

import com.vrerv.springboottemplate.server.feature.user.model.LoginModel;
import com.vrerv.springboottemplate.server.feature.user.model.LoginResponseModel;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Validated
@SecurityRequirements // override to be not required
@RestController
@RequestMapping("/client/api/auth")
public class ClientAuthEntpoint {

	private final UserService service;

	/**
	 * Do login
	 */
	@PostMapping("/login")
	@ResponseBody
	public LoginResponseModel auth(@Valid @RequestBody LoginModel dto) {

		return service.auth(dto);
	}
}
