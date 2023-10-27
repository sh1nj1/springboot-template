package com.vrerv.springboottemplate.server.common.infra.error;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Wrapping method parameters and exception for debugging, this is only used by AOP and adding args.
 */
@Slf4j
@AllArgsConstructor
@Getter
public class AopWrappingException extends RuntimeException {

	private List<Object> args;

	public AopWrappingException(List<Object> args, Throwable t) {
		super(t);
		this.args = args;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + ", args=" + args;
	}
}
