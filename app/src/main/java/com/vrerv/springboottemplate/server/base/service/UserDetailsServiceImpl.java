package com.vrerv.springboottemplate.server.base.service;

import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import com.vrerv.springboottemplate.server.feature.user.AuthenticationRejectedException;
import com.vrerv.springboottemplate.server.feature.user.User;
import com.vrerv.springboottemplate.server.feature.user.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepo repo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repo.findByAccount(username).orElseThrow(() -> new UsernameNotFoundException("Username not found. username: " + username));
		return new UserDetails() {

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return Collections.emptyList();
			}

			@Override
			public String getPassword() {
				return user.getPassword();
			}

			@Override
			public String getUsername() {
				return user.getAccount();
			}

			@Override
			public boolean isAccountNonExpired() {
				return false;
			}

			@Override
			public boolean isAccountNonLocked() {
				return false;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return false;
			}

			@Override
			public boolean isEnabled() {
				return true;
			}
		};
	}

	public UserDetails authenticate(String userId, String password) throws AuthenticationRejectedException {
		try {
			UserDetails user = loadUserByUsername(userId);
			boolean matchedPassword = passwordEncoder.matches(password, user.getPassword());
			if(!matchedPassword) {
				throw new AuthenticationRejectedException("Failed to authenticate.", null);
			}
			return user;
		} catch (UsernameNotFoundException e) {
			throw new AuthenticationRejectedException("Failed to authenticate.", e);
		}
	}
}
