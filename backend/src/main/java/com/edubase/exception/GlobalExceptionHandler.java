package com.edubase.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
		return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> fields = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(err -> fields.put(err.getField(), err.getDefaultMessage()));

		String message = fields.isEmpty()
				? "Dados inválidos"
				: fields.entrySet().stream()
						.map(e -> e.getKey() + ": " + e.getValue())
						.collect(Collectors.joining("; "));

		return build(HttpStatus.BAD_REQUEST, message, fields);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, String> fields = new LinkedHashMap<>();
		ex.getConstraintViolations().forEach(v -> {
			String path = v.getPropertyPath() != null ? v.getPropertyPath().toString() : "campo";
			fields.put(path, v.getMessage());
		});
		String message = fields.isEmpty() ? ex.getMessage() : String.join("; ", fields.values());
		return build(HttpStatus.BAD_REQUEST, message, fields);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException ex) {
		String detail = "JSON inválido ou valor de enum não reconhecido";
		Throwable cause = ex.getMostSpecificCause();
		if (cause != null && cause.getMessage() != null && !cause.getMessage().isBlank()) {
			detail = detail + ": " + cause.getMessage();
		}
		return build(HttpStatus.BAD_REQUEST, detail, null);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
		return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), null);
	}

	private ResponseEntity<Map<String, Object>> build(
			HttpStatus status,
			String message,
			Map<String, String> fields) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		if (fields != null && !fields.isEmpty()) {
			body.put("fields", fields);
		}
		return ResponseEntity.status(status).body(body);
	}
}
