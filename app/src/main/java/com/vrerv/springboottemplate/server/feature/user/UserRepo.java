package com.vrerv.springboottemplate.server.feature.user;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepo extends PagingAndSortingRepository<User, Long> {

	Optional<User> findByAccount(String account);
}
