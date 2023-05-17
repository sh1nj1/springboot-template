package com.vrerv.springboottemplate.server.example.application.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 예제사용자 응답 모델
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class ExampleUserResponseModel {

	private Long id;

	private String name;

	private List<ExampleUserContactModel> contacts;
}
