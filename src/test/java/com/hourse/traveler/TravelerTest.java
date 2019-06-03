package com.hourse.traveler;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.hourse.traveler.model.Field;
import com.hourse.traveler.model.Point;
import com.hourse.traveler.model.RectangleField;
import com.hourse.traveler.service.HourseTraveler;
import com.hourse.traveler.service.Traveler;

@SpringBootTest
public class TravelerTest {
	private final Traveler hourseTraveler = new HourseTraveler();
	@ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
	
	private static Stream<Arguments> hourseTravelerTravelThrowNullPointerExceptionParams() {
		var field = new RectangleField(new Point(0l,0l), new Point(10l,10l));
		var start = new Point(1l,1l);
		var end = new Point(9l,9l);
		return Stream.of(
				Arguments.of(null, null, null),
				Arguments.of(field, null, null),
				Arguments.of(null, start, null),
				Arguments.of(null, null, end),
				Arguments.of(field, start, null),
				Arguments.of(field, null, end),
				Arguments.of(null, start, end));
	}
	
	private static Stream<Arguments> hourseTravelerTravelParams() {
		var field = new RectangleField(new Point(1l,1l), new Point(10l,14l));
		return Stream.of(
				Arguments.of(field, new Point(2l,1l), new Point(1l,3l), 1l),
				Arguments.of(field, new Point(-2l,1l), new Point(1l,3l), -1l),
				Arguments.of(field, new Point(2l,1l), new Point(1l,-3l), -1l));
	}
	
	@ParameterizedTest
	@MethodSource("hourseTravelerTravelThrowNullPointerExceptionParams")
	public void hourseTravelerTravelThrowNullPointerException(Field field, Point start, Point end) {
		assertThrows(NullPointerException.class, () -> {
			this.hourseTraveler.travel(field, start, end);
		});
	}
	
	@ParameterizedTest
	@MethodSource("hourseTravelerTravelParams")
	public void hourseTravelerTravel(Field field, Point start, Point end, long expected) {
		assertEquals(expected, this.hourseTraveler.travel(field, start, end));
	}
}
