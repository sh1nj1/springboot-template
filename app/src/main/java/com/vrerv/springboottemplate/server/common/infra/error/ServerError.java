package com.vrerv.springboottemplate.server.common.infra.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerError {
	/**
	 * tracking id for debugging
	 */
	private String trackingId;

	/**
	 * error code to identify error type
	 */
	private String code;

	/**
	 * user message for this error
	 */
	private String message;

	/**
	 * debug message for this error, you can include more detailed information for debugging, this should not be used for user message.
	 */
	private String debugMessage;
}
