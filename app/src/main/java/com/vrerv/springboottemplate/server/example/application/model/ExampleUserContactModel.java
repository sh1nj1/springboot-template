package com.vrerv.springboottemplate.server.example.application.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.vrerv.springboottemplate.server.example.domain.ExampleContact.ExampleContactType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class ExampleUserContactModel {


	@NotBlank
	private String name;

	@NotNull
	private ExampleContactType contactType;

	@NotBlank
	private String value;
}
