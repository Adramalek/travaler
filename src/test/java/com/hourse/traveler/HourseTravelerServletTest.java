package com.hourse.traveler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.hourse.traveler.controller.HourseTravelerServlet;
import com.hourse.traveler.model.Point;
import com.hourse.traveler.model.RectangleField;
import com.hourse.traveler.service.PointConversionException;
import com.hourse.traveler.service.PointConverter;
import com.hourse.traveler.service.Traveler;

import lombok.extern.log4j.Log4j2;

@Log4j2
//@SpringBootTest
public class HourseTravelerServletTest {
	@ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
	
	private static Stream<Arguments> countShouldReturnNumber_Parameter() {
		return Stream.of(
				Arguments.of("10", "14", "B1", "A3", 10l, 14l, new Point(2l,1l), new Point(1l,3l), 1l),
				Arguments.of("10", "14", " B1 ", "B1", 10l, 14l, new Point(2l,1l), new Point(2l,1l), 0l),
				Arguments.of("10", "14", "Z1 ", "B1", 10l, 14l, new Point(26l,1l), new Point(2l,1l), -1l),
				Arguments.of("10", "14", " Z1", "Z10", 10l, 14l, new Point(26l,1l), new Point(26l,10l), -1l),
				Arguments.of("10", "14", "B1", "Z1", 10l, 14l, new Point(2l,1l), new Point(26l,1l), -1l));
	}
	
	private static Stream<Arguments> count_PointConversionException_ShouldReturnHttpStatusCode422_Parameters() {
		return Stream.of(
				Arguments.of(""),
				Arguments.of(" "),
				Arguments.of("A"),
				Arguments.of(" A "),
				Arguments.of("1"),
				Arguments.of(" 1 "),
				Arguments.of("1A"),
				Arguments.of(" 1A "));
	}
	
	private static Stream<Arguments> count_NumberFormatException_ShouldReturnHttpStatusCode422_Parameters() {
		return Stream.of(
				Arguments.of(""),
				Arguments.of(" "),
				Arguments.of("A"),
				Arguments.of("1-"),
				Arguments.of("-"));
	}
	
	private static Stream<Arguments> count_IllegalArgumentException_ShouldReturnHttpStatusCode422_Parameters() {
		return Stream.of(
				Arguments.of("0", "1"),
				Arguments.of("1", "0"));
	}
	
//	@ParameterizedTest(name="{index} => {0} {1} {2} {3}")
//	@MethodSource("countShouldReturnNumber_Parameter")
	public void countShouldReturnNumber(String width, String height, String start,
			String end, long mockWidth, long mockHeight,
			Point mockStartPoint, Point mockEndPoint, long expectedCount) throws ServletException, IOException {
		var request = mock(HttpServletRequest.class);
		var response = mock(HttpServletResponse.class);
		var traveler = mock(Traveler.class);
		var stringWriter = new StringWriter();
		var writer = new PrintWriter(stringWriter);
		var field = new RectangleField(new Point(1l,1l), new Point(mockWidth, mockHeight));
		
		when(request.getParameter("width")).thenReturn(width);
		when(request.getParameter("height")).thenReturn(height);
		when(request.getParameter("start")).thenReturn(start);
		when(request.getParameter("end")).thenReturn(end);
		when(request.getMethod()).thenReturn("GET");
		when(response.getWriter()).thenReturn(writer);
		when(response.getStatus()).thenReturn(200);
		when(traveler.travel(field, mockStartPoint, mockEndPoint)).thenReturn(expectedCount);
		
		new HourseTravelerServlet(traveler).service(request, response);
		
		stringWriter.flush();
		assertTrue(stringWriter.toString().contains(Long.toString(expectedCount)));
	}
	
//	@ParameterizedTest
//	@MethodSource("count_PointConversionException_ShouldReturnHttpStatusCode422_Parameters")
	public void count_PointConversionException_ShouldReturnHttpStatusCode422(String start) throws ServletException, IOException {
		var request = mock(HttpServletRequest.class);
		var response = mock(HttpServletResponse.class);
		var traveler = mock(Traveler.class);
		var stringWriter = new StringWriter();
		var writer = new PrintWriter(stringWriter);
		
		when(request.getParameter("width")).thenReturn("10");
		when(request.getParameter("height")).thenReturn("10");
		when(request.getParameter("start")).thenReturn(start);
		when(request.getMethod()).thenReturn("GET");
		when(response.getWriter()).thenReturn(writer);
		when(response.getStatus()).thenReturn(422);
		
		new HourseTravelerServlet(traveler).service(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("Invalid point representation format: expected excel format."));
		
	}
	
//	@ParameterizedTest
//	@MethodSource("count_NumberFormatException_ShouldReturnHttpStatusCode422_Parameters")
	public void count_NumberFormatException_ShouldReturnHttpStatusCode422(String width) throws ServletException, IOException {
		var request = mock(HttpServletRequest.class);
		var response = mock(HttpServletResponse.class);
		var traveler = mock(Traveler.class);
		var stringWriter = new StringWriter();
		var writer = new PrintWriter(stringWriter);
		
		when(request.getParameter("width")).thenReturn(width);
		when(request.getMethod()).thenReturn("GET");
		when(response.getWriter()).thenReturn(writer);
		when(response.getStatus()).thenReturn(422);
		
		new HourseTravelerServlet(traveler).service(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("Cannot convert from 'Strign' to 'Long'"));
	}
	
//	@ParameterizedTest
//	@MethodSource("count_IllegalArgumentException_ShouldReturnHttpStatusCode422_Parameters")
	public void count_IllegalArgumentException_ShouldReturnHttpStatusCode422(String width, String height) throws ServletException, IOException {
		var request = mock(HttpServletRequest.class);
		var response = mock(HttpServletResponse.class);
		var traveler = mock(Traveler.class);
		var stringWriter = new StringWriter();
		var writer = new PrintWriter(stringWriter);
		
		when(request.getParameter("width")).thenReturn(width);
		when(request.getParameter("height")).thenReturn(height);
		when(request.getMethod()).thenReturn("GET");
		when(response.getWriter()).thenReturn(writer);
		when(response.getStatus()).thenReturn(422);
		
		new HourseTravelerServlet(traveler).service(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("must be greater than 1."));
	}
}
