package com.hourse.traveler.controller;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hourse.traveler.model.ApiError;
import com.hourse.traveler.service.PointConversionException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		var cause = ex.getMostSpecificCause();
		if (cause instanceof PointConversionException) {
			return ResponseEntity.unprocessableEntity().body(ApiError.builder()
					.message(String.format("Cannot convert request parameter to required type '%s'",
							ex.getRequiredType().getTypeName()))
					.error(cause.getMessage())
					.build());
			}
		return super.handleTypeMismatch(ex, headers, status, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		var apiErrorBuilder = ApiError.builder().message(ex.getMessage());
		ex.getConstraintViolations().forEach(e -> apiErrorBuilder.error(e.getPropertyPath()+": "+e.getMessage()));
		return ResponseEntity.unprocessableEntity().body(apiErrorBuilder.build());
	}
	
}
