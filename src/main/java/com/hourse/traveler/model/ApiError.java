package com.hourse.traveler.model;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

@Getter
public class ApiError {
	private final String message;
	private final List<String> errors;
	
	@Builder
	public ApiError(@NonNull String message, @Singular List<String> errors) {
		super();
		this.message = message;
		this.errors = errors == null? List.of() : Collections.unmodifiableList(errors);
	}
}
