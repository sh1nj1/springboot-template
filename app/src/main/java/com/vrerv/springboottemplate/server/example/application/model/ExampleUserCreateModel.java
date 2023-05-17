package com.vrerv.springboottemplate.server.example.application.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 예제사용자 생성 요청
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class ExampleUserCreateModel {

	@NotNull
	@Length(min = 2, max = 32)
	private String name;

	private List<ExampleUserContactModel> contacts;
}
