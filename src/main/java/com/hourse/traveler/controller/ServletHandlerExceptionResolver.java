package com.hourse.traveler.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.hourse.traveler.model.ApiError;
import com.hourse.traveler.service.PointConversionException;

@ControllerAdvice
public class ServletHandlerExceptionResolver implements HandlerExceptionResolver {
	
	@Override
	@ExceptionHandler({
		PointConversionException.class,
		NumberFormatException.class,
		IllegalArgumentException.class,
		Exception.class
	})
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			Exception ex) {
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		var model = new ModelAndView(new MappingJackson2JsonView());
		var apiError = ApiError.builder()
				.message(ex.getMessage())
				.build();
		if (ex instanceof PointConversionException ||
				ex instanceof NumberFormatException ||
				ex instanceof IllegalArgumentException)
		{
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			model.addObject("code", HttpStatus.UNPROCESSABLE_ENTITY.value());
			model.addObject("apiError", apiError);
		}
		else {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			model.addObject("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
			model.addObject("apiError", apiError);
		}
		return model;
	}

}
