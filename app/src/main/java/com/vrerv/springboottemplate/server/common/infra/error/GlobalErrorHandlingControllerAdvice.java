package com.vrerv.springboottemplate.server.common.infra.error;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.vrerv.springboottemplate.server.common.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalErrorHandlingControllerAdvice {

	private static final String ERROR_MESSAGE_PREFIX = "error.";
	private static final String ERROR_MESSAGE_UNKNOWN = "unknown";

	private final MessageSource ms;
	@Value("${app.debug}")
	private boolean debug;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Response<ServerError> handleException(HttpServletRequest request, Locale locale, Exception ex) {

		Throwable e = ex;
		while (e instanceof AopWrappingException && e.getCause() != null) {
			e = e.getCause();
		}
		String errorId = UUID.randomUUID().toString();
		String messageCode = e.getClass().getSimpleName();
		String messageKey = ERROR_MESSAGE_PREFIX + messageCode;
		log.error("unexpected error. errorId={}, messageKey={}, locale={}, remoteAddr={}, uri={}",
				errorId, messageKey, locale, request.getRemoteAddr(), request.getRequestURI(), ex);

		String m;
		try {
			m = ms.getMessage(messageKey, null, locale);
		} catch (NoSuchMessageException noMessageException) {
			messageCode = ERROR_MESSAGE_UNKNOWN;
			m = ms.getMessage(ERROR_MESSAGE_PREFIX + messageCode, null, locale);
		}

		return Response.<ServerError>builder()
				.status(INTERNAL_SERVER_ERROR.value())
				.payload(ServerError.builder()
						.trackingId(errorId)
						.code(messageCode)
						.message(m)
						.debugMessage(debug ? ex.getMessage() : null)
						.build())
				.build();
	}
}
