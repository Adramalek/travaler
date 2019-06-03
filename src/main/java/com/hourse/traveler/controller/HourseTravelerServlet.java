package com.hourse.traveler.controller;

import java.io.IOException;
import java.util.function.Predicate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import com.hourse.traveler.model.RectangleField;
import com.hourse.traveler.model.Point;
import com.hourse.traveler.service.PointConversionException;
import com.hourse.traveler.service.PointConverter;
import com.hourse.traveler.service.Traveler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HourseTravelerServlet extends HttpServlet {
	private static final long serialVersionUID = 6496855448290351207L;
	private final Traveler traveler;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {			
			var width = this.convertAndValidate(req, "width",
					source -> Long.parseLong(source),
					number -> number > 0,
					"Parameter 'width' must be greater than 1.");
			var height = this.convertAndValidate(req, "height",
					source -> Long.parseLong(source),
					number -> number > 0,
					"Parameter 'height' must be greater than 1.");
			var start = this.convertAndValidate(req, "start", PointConverter.EXCEL_FORMAT, null, null);
			var end = this.convertAndValidate(req, "end", PointConverter.EXCEL_FORMAT, null, null);
			var field = new RectangleField(new Point(1l,1l), new Point(width, height));
			var steps = this.traveler.travel(field, start, end);
			resp.setContentType("text/plain");
			resp.getWriter().write(Long.toString(steps));
			resp.setStatus(HttpStatus.OK.value());
		} catch (Exception ex) {
			this.handleException(resp, ex);
		}
	}
	
	private <T> T convertAndValidate(HttpServletRequest req,
			String parameterName,
			Converter<String, T> converter,
			@Nullable Predicate<T> validator,
			@Nullable String validationErrorMessage)
	{
		var parameter = converter.convert(req.getParameter(parameterName));
		if (!validator.test(parameter))
			throw new IllegalArgumentException(validationErrorMessage);
		return parameter;
	}
	
	private void handleException(HttpServletResponse resp, Exception ex) throws IOException {
		resp.reset();
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		if (ex instanceof PointConversionException || ex instanceof IllegalArgumentException)
			resp.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
		else if (ex instanceof NumberFormatException)
			resp.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), String.format("Cannot convert from 'Strign' to 'Long' for '%s'", ex.getMessage().toLowerCase()));
		else resp.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
			
	}
}
