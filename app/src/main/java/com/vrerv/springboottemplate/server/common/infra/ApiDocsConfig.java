package com.vrerv.springboottemplate.server.common.infra;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "jwt", scheme = "bearer", bearerFormat = "JWT")
public class ApiDocsConfig {


	@Bean
	public OpenAPI customOpenAPI(@Value("${app.doc.title}") String docTitle,
			@Value("${app.doc.desc}") String docDesc,
			@Value("${app.doc.version}") String version) {

		return new OpenAPI()
				.info(new Info()
						.title(docTitle)
						.description(docDesc)
						.version(version)
				);
	}
}
