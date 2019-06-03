package com.hourse.traveler.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class PointConversionException extends RuntimeException {
	private static final long serialVersionUID = 2841471154820713087L;

	public PointConversionException() {
		super();
	}

	public PointConversionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PointConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public PointConversionException(String message) {
		super(message);
	}

	public PointConversionException(Throwable cause) {
		super(cause);
	}
}
