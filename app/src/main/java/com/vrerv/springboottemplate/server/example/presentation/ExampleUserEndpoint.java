package com.vrerv.springboottemplate.server.example.presentation;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import com.vrerv.springboottemplate.server.example.application.ExampleUserService;
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserCreateModel;
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserResponseModel;
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserUpdateModel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Validated
@SecurityRequirements // override to be not required
@RestController
@RequestMapping("/example/example-users")
public class ExampleUserEndpoint {

	private final ExampleUserService service;

	/**
	 * List
	 * @return
	 */
	@GetMapping("/")
	@PageableAsQueryParam
	public Page<ExampleUserResponseModel> list(@Parameter(hidden = true) Pageable pageable) {

		return service.list(pageable);
	}

	/**
	 * Create new User
	 */
	@PostMapping("/")
	@ResponseBody
	public ExampleUserResponseModel create(@Valid @RequestBody ExampleUserCreateModel dto) {

		log.info("dto={}", dto);
		return service.create(dto);
	}

	/**
	 * 예제사용자를 업데이트한다.
	 * @param id 예제사용자의 아이디
	 * @param dto 업데이트할 사용자 데이터
	 */
	@PutMapping("/{id}")
	@ResponseBody
	public ExampleUserResponseModel update(@PathVariable Long id, @Valid @RequestBody ExampleUserUpdateModel dto) {

		return service.update(id, dto);
	}

	/**
	 * 예제사용자를 삭제한다.
	 * @param id 예제사용자의 아이디
	 */
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {

		service.delete(id);
	}

	/**
	 * 사용자 이름을 받아 사용자에 대한 인사말을 리턴한다.
	 *
	 * @param name 사용자 이름
	 * @return 사용자에 대한 인사말
	 */
	@GetMapping("/test/hello")
	@ResponseBody
	public String hello(@RequestParam String name) {

		return "Hello, " + name;
	}

	/**
	 * 사용자 이름을 받아 사용자에 대한 인사말을 리턴한다.
	 *
	 * @param userName 사용자 인사말과 사용자명
	 * @return
	 */
	@GetMapping("/test/hello2")
	@ResponseBody
	@ApiResponse(
			description = "사용자명과 인사말",
			content = {
					@Content(examples = @ExampleObject(name = "soonoh",
							value = """
									{
									  "greeting": "Hello",
									  "name": "Soonoh"
									}
									"""), schema = @Schema(implementation = UserName.class))
			}
	)
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "사용자명과 인사말",
			content = {
					@Content(examples = @ExampleObject(name = "soonoh",
							value = """
									{
									  "greeting": "Hello",
									  "name": "Soonoh"
									}
									"""), schema = @Schema(implementation = UserName.class))
			}
	)
	public UserName hello(@RequestBody UserName userName) {

		return userName;
	}

	/**
	 * 사용자 이름과 인사말
	 */
	@Data
	public static class UserName {

		/**
		 * 인사말
		 */
		private String greeting = "Hello";

		/**
		 * 이름
		 */
		private String name;
	}
}
