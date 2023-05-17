package com.vrerv.springboottemplate.server.common.infra.security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;

public class RegexCorsConfiguration extends CorsConfiguration {

	private List<String> allowedOriginsRegexes = new ArrayList<>();

	/**
	 * Check the origin of the request against the configured allowed origins.
	 * @param requestOrigin the origin to check
	 * @return the origin to use for the response, possibly {@code null} which
	 * means the request origin is not allowed
	 */
	public String checkOrigin(String requestOrigin) {
		if (!StringUtils.hasText(requestOrigin)) {
			return null;
		}

		if (this.allowedOriginsRegexes.isEmpty()) {
			return null;
		}

		if (this.allowedOriginsRegexes.contains(ALL)) {
			if (getAllowCredentials() != Boolean.TRUE) {
				return ALL;
			}
			else {
				return requestOrigin;
			}
		}

		for (String allowedOriginRegex : this.allowedOriginsRegexes) {
			if (createMatcher(requestOrigin, allowedOriginRegex).matches()) {
				return requestOrigin;
			}
		}

		return null;
	}

	public void setAllowedOriginRegex(List<String> allowedOriginsRegexes) {
		this.allowedOriginsRegexes = allowedOriginsRegexes;
	}

	private Matcher createMatcher(String origin, String allowedOrigin) {
		String regex = this.parseAllowedWildcardOriginToRegex(allowedOrigin);
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(origin);
	}

	private String parseAllowedWildcardOriginToRegex(String allowedOrigin) {
		String regex = allowedOrigin.replace(".", "\\.");
		return regex.replace("*", ".*");
	}
}