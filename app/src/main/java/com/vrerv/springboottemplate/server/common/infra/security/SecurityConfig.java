package com.vrerv.springboottemplate.server.common.infra.security;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrerv.springboottemplate.server.common.infra.error.FilterChainExceptionHandlingFilter;
import com.vrerv.springboottemplate.server.common.infra.error.GlobalErrorHandlingControllerAdvice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.security.cors.allowed-origins: http://localhost:*, http://192.168.0.*:*}")
    private List<String> corsAllowedOrigins;

	@Value("${app.security.white-listed-paths: /auth/**, /public/**, /error/**, /example/**, /client/api/**}")
	private List<String> whiteListedPaths;

	@Value("${app.security.docs-api-paths: /*/api-docs/**, /*/api-docs*, /swagger-ui.html, /swagger-ui/*}")
	private List<String> docsApiPaths;

	private final JwtTokenProvider jwtTokenProvider;
	private final UserTokenService userTokenService;
	private final GlobalErrorHandlingControllerAdvice handler;
	private final ObjectMapper om;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(docsApiPaths.toArray(String[]::new)).permitAll()
                // Login 관련 페이지 + Public API 호출은 비인증, 나머지는 인증 필요
                .antMatchers(whiteListedPaths.toArray(String[]::new)).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userTokenService), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new FilterChainExceptionHandlingFilter(handler, om), FilterSecurityInterceptor.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        RegexCorsConfiguration configuration = new RegexCorsConfiguration();
        configuration.setAllowedOriginRegex(corsAllowedOrigins);
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}