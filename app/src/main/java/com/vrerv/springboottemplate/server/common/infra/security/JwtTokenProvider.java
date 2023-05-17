package com.vrerv.springboottemplate.server.common.infra.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {


	private static final String BEARER_PREFIX = "Bearer ";
    public static final String TOKEN_PROPERTY_NAME__TYPE = "type";
    public static final String TOKEN_PROPERTY_VALUE__TYPE_ANONYMOUS = "anonymous";
    private final UserDetailsService userDetailsService;

    @Value("${spring.jwt.secret:TEST_SECRET}")
    private String jwtSecretKey;

    @Value("${app.security.token.user-expire-days: 365}")
    private int userExpireDays = 365;
    @Value("${app.security.token.admin-expire-hours: 168}")
    private int adminExpireHours = 168;
    private long userExpireMillis = 1000L * 60 * 60 * 24 * userExpireDays;
    private long adminExpireMillis = 1000L * 60 * 60 * adminExpireHours;
    private String signingKey = null;

    @PostConstruct
    protected void init() {
        signingKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
    }

    public String createToken(String userId, String userName, List<String> roles, String type) {
        Date currentTime = new Date();
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("authority", roles);
        claims.put("userName", userName);
        claims.put(TOKEN_PROPERTY_NAME__TYPE, type);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(currentTime.getTime() +
                        (TOKEN_PROPERTY_VALUE__TYPE_ANONYMOUS.equals(type) ? userExpireMillis : adminExpireMillis)))
                .setIssuedAt(currentTime)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        if (isAnonymousToken(token)) {
            return new UsernamePasswordAuthenticationToken("anonymous", token, List.of(new SimpleGrantedAuthority("anonymous")));
        } else {
            UserDetails userDetails = userDetailsService.loadUserByUsername(this.extractUserId(token));
            return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        }
    }

    public String extractUserId(String token) {
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isAnonymousToken(String token) {
        return TOKEN_PROPERTY_VALUE__TYPE_ANONYMOUS.equals(extractExtraField(token, TOKEN_PROPERTY_NAME__TYPE));
    }

    public String extractExtraField(String token, String propertyName) {
        Object value = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().get(propertyName);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
            if (log.isInfoEnabled()) log.info("expiration={}", claims.getBody().getExpiration());
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
        	log.error("validationToken error", e);
            return false;
        }
    }

    public String getToken(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (token != null && token.startsWith(BEARER_PREFIX)) {
			return token.substring(BEARER_PREFIX.length());
		}
        return null;
    }

}
