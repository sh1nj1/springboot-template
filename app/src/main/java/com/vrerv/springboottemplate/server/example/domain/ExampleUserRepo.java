package com.vrerv.springboottemplate.server.example.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ExampleUserRepo extends PagingAndSortingRepository<ExampleUser, Long> {
}
