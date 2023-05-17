package com.vrerv.springboottemplate.server.example.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ExampleContactRepo extends PagingAndSortingRepository<ExampleContact, Long> {
}
