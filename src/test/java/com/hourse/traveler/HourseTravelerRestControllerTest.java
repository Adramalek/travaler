package com.hourse.traveler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import com.hourse.traveler.controller.HourseTravelerRestController;
import com.hourse.traveler.model.Point;
import com.hourse.traveler.model.RectangleField;
import com.hourse.traveler.service.PointConversionException;
import com.hourse.traveler.service.PointConverter;
import com.hourse.traveler.service.Traveler;

import junitparams.Parameters;

//@RunWith(SpringRunner.class)
//@WebMvcTest(HourseTravelerRestController.class)
public class HourseTravelerRestControllerTest {
	private static final String PATH_FORMAT = "/rest/count?width=%s&height=%s&start=%s&end=%s";
	@ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
	@Autowired
	private MockMvc mockMvc;

	private static Stream<Arguments> countShouldReturnNumber_Parameters() {
		return Stream.of(
				Arguments.of("10", "14", "B1", "A3", 10l, 14l, new Point(2l,1l), new Point(1l,3l), 1l),
				Arguments.of("10", "14", " B1 ", "B1", 10l, 14l, new Point(2l,1l), new Point(2l,1l), 0l),
				Arguments.of("10", "14", "Z1 ", "B1", 10l, 14l, new Point(26l,1l), new Point(2l,1l), -1l),
				Arguments.of("10", "14", " Z1", "Z10", 10l, 14l, new Point(26l,1l), new Point(26l,10l), -1l),
				Arguments.of("10", "14", "B1", "Z1", 10l, 14l, new Point(2l,1l), new Point(26l,1l), -1l));
	}
	
	private static Stream<Arguments> count_PointConversionException_ShouldReturnHttpStatusCode422_Parameters() {
		return Stream.of(
				Arguments.of("10", "14", "BA", "A3"),
				Arguments.of("10", "14", "11", "B1"),
				Arguments.of("10", "14", " ", "Z1"),
				Arguments.of("10", "14", "%", "ZZ"),
				Arguments.of("10", "14", "", "Z1"));
	}
	
	private static Stream<Arguments> count_NumberFormatException_ShouldReturnHttpStatusCode422() {
		return Stream.of(
				Arguments.of(" ", "1", "A1", "B1"),
				Arguments.of("%", "1", "A1", "B1"),
				Arguments.of("A", "1", "A1", "B1"),
				Arguments.of("", "1", "A1", "B1"),
				Arguments.of("-", "1", "A1", "B1"),
				Arguments.of("1-", "1", "A1", "B1"),
				Arguments.of("1+", "1", "A1", "B1"),
				Arguments.of("1A", "1", "A1", "B1"));
	}
	
	private static Stream<Arguments> count_ValidationException_ShouldReturnHttpStatusCode422() {
		return Stream.of(
				Arguments.of("-1", "1", "A1", "B1"),
				Arguments.of("1", "-1", "A1", "B1"),
				Arguments.of("0", "1", "A1", "B1"),
				Arguments.of("1", "0", "A1", "B1"));
	}
	
//	@ParameterizedTest
//	@MethodSource("countShouldReturnNumber_Parameters")
	public void countShouldReturnNumber(String width, String height, String start,
			String end, long mockWidth, long mockHeight,
			Point mockStartPoint, Point mockEndPoint, long expectedCount) throws Exception {
		var traveler = mock(Traveler.class);
		when(traveler.travel(new RectangleField(new Point(1l,1l), new Point(mockWidth, mockHeight)),
				mockStartPoint, mockEndPoint))
			.thenReturn(expectedCount);
		this.mockMvc.perform(get(String.format(PATH_FORMAT, width, height, start, end)))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.TEXT_PLAIN))
					.andExpect(content().string(Long.toString(expectedCount)));
	}
	
//	@ParameterizedTest
//	@MethodSource("count_PointConversionException_ShouldReturnHttpStatusCode422_Parameters")
	public void count_PointConversionException_ShouldReturnHttpStatusCode422(String width, String height, String start, String end) throws Exception {
		this.mockMvc.perform(get(String.format(PATH_FORMAT, width, height, start, end)))
					.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
	}
	
//	@ParameterizedTest
//	@MethodSource("count_NumberFormatException_ShouldReturnHttpStatusCode422")
	public void count_NumberFormatException_ShouldReturnHttpStatusCode422(String width, String height, String start, String end) throws Exception {
		this.mockMvc.perform(get(String.format(PATH_FORMAT, width, height, start, end)))
				    .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
	}
	
//	@ParameterizedTest
//	@MethodSource("count_ValidationException_ShouldReturnHttpStatusCode422")
	public void count_ValidationException_ShouldReturnHttpStatusCode422(String width, String height, String start, String end) throws Exception {
		this.mockMvc.perform(get(String.format(PATH_FORMAT, width, height, start, end)))
				    .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
	}
}
