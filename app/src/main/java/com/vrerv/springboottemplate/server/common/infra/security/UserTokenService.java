package com.vrerv.springboottemplate.server.common.infra.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserTokenService {

    @Value("${app.security.token.verify-unload: true}")
    private boolean verifyUnload = true;
    private final JwtTokenProvider jwtTokenProvider;

    // multi-instance 사용시, redis 나 다른 공유 캐쉬를 사용해야 함.
    private Set<String> userTokenSet = new HashSet<>();

    public boolean isValidToken(String token) {
        if (verifyUnload) {
            return userTokenSet.contains(token);
        }
        return true;
    }

    public boolean invalidateToken(String token) {

        return userTokenSet.remove(token);
    }

    public String createToken(String userId, String userName) {

        return this.createToken(userId, userName, Collections.emptyList(), null);
    }

    public String createToken(String userId, String userName, List<String> roles, String type) {

        String token = jwtTokenProvider.createToken(userId, userName, roles, type);
        userTokenSet.add(token);

        return token;
    }
}
