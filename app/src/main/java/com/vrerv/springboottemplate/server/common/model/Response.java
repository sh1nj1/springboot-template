package com.vrerv.springboottemplate.server.common.model;

import lombok.Builder;
import lombok.Data;

import org.springframework.http.HttpStatus;

@Data
@Builder
public class Response<T> {

    @Builder.Default
    private int status = HttpStatus.OK.value();
    private T payload;
}