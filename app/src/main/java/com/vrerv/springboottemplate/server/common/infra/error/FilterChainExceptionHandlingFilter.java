package com.vrerv.springboottemplate.server.common.infra.error;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrerv.springboottemplate.server.common.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// with AOP, error occurs if you make this as @Component
@Slf4j
@RequiredArgsConstructor
public class FilterChainExceptionHandlingFilter extends GenericFilterBean {

    private final GlobalErrorHandlingControllerAdvice handler;
	private final ObjectMapper om;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			Response<ServerError> serverErrorResponse = handler.resolveException(httpRequest, request.getLocale(), e);
			httpResponse.setStatus(serverErrorResponse.getStatus());
			httpResponse.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
			httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
			httpResponse.setLocale(request.getLocale());
			response.getWriter().write(om.writeValueAsString(serverErrorResponse));
		}
	}
}