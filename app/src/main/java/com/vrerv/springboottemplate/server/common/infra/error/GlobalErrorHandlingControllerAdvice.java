package com.vrerv.springboottemplate.server.common.infra.error;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.vrerv.springboottemplate.server.common.model.Response;
import com.vrerv.springboottemplate.server.feature.user.AuthenticationRejectedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

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

	private final Map<Class<?>, HttpStatus> statusMap = new HashMap<>();
	{
		statusMap.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
		statusMap.put(AuthenticationRejectedException.class, HttpStatus.UNAUTHORIZED);
		statusMap.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);
		statusMap.put(NoHandlerFoundException.class, HttpStatus.NOT_FOUND);
	}


	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Response<ServerError>> handleException(HttpServletRequest request, Locale locale, Exception ex) {

		Response<ServerError> response = resolveException(request, locale, ex);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	public Response<ServerError> resolveException(HttpServletRequest request, Locale locale, Exception ex) {

		Throwable e = ex;
		while (e instanceof AopWrappingException && e.getCause() != null) {
			e = e.getCause();
		}
		String errorId = UUID.randomUUID().toString();
		String messageCode = e.getClass().getSimpleName();
		String messageKey = ERROR_MESSAGE_PREFIX + messageCode;

		String m;
		HttpStatus status = statusMap.getOrDefault(e.getClass(), INTERNAL_SERVER_ERROR);
		try {
			m = ms.getMessage(messageKey, null, locale);
			Exception exceptionOrNull = debug ? ex: null;
			log.warn("handle error. errorId={}, messageKey={}, locale={}, remoteAddr={}, uri={}, message={}",
					errorId, messageKey, locale, request.getRemoteAddr(), request.getRequestURI(), ex.getMessage(), exceptionOrNull);
		} catch (NoSuchMessageException noMessageException) {
			messageCode = ERROR_MESSAGE_UNKNOWN;
			m = ms.getMessage(ERROR_MESSAGE_PREFIX + messageCode, null, locale);
			log.error("unexpected error. errorId={}, messageKey={}, locale={}, remoteAddr={}, uri={}",
					errorId, messageKey, locale, request.getRemoteAddr(), request.getRequestURI(), ex);
		}

		return Response.<ServerError>builder()
				.status(status.value())
				.payload(ServerError.builder()
						.trackingId(errorId)
						.code(messageCode)
						.message(m)
						.debugMessage(debug ? ex.getMessage() : null)
						.build())
				.build();
	}
}
