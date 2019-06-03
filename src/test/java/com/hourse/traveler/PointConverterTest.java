package com.hourse.traveler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.hourse.traveler.model.Point;
import com.hourse.traveler.service.PointConversionException;
import com.hourse.traveler.service.PointConverter;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@SpringBootTest
@RunWith(JUnitParamsRunner.class)
public class PointConverterTest {
	
	private Object[] pointConverterShoulldSucceedParameters() {
		var params = new ArrayList<Object[]>();
		int letterOffset = 'Z'-'A';
		while (letterOffset >= 0) {
			params.add(new Object[] { String.format("%c%d", 'A'+letterOffset, 1), new Point(1l+letterOffset, 1l) });
		}
		params.add(new Object[] { "AA1", new Point(27l,1l) });
		params.add(new Object[] { "AAA1", new Point(703l,1l) });
		return params.toArray();
	}
	
	@Test(expected=PointConversionException.class)
	@Parameters({"", "AA", "11", " ", " AA", " 11", " AA ", " 11 "})
	public void pointConverterShouldThrowPointConversionException(String source) {
		PointConverter.EXCEL_FORMAT.convert(source);
	}
	
	@Test
	@Parameters(method="pointConverterShoulldSucceedParameters")
	public void pointConverterShoulldSucceed(String source, Point expected) {
		assertEquals(PointConverter.EXCEL_FORMAT.convert(source), expected);
	}
}
