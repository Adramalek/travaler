package com.hourse.traveler.service;

import java.util.regex.Pattern;

import org.springframework.core.convert.converter.Converter;

import com.hourse.traveler.model.Point;

public enum PointConverter implements Converter<String, Point> {
	EXCEL_FORMAT {
		private final Pattern validRepresentationPattern = Pattern.compile("^([A-Z]+)([1-9]\\d*)$");
		private final char beforeA = 'A'-1;
		private final long base = 26l;
		
		@Override
		public Point convert(String representation) {
			if (representation.isBlank())
				throw new PointConversionException("Empty string.");
			var matcher = this.validRepresentationPattern.matcher(representation.trim());
			if (!matcher.find())
				throw new PointConversionException("Invalid point representation format: expected excel format.");
			var encodedX = matcher.group(1).toCharArray();
			var y = Long.parseLong(matcher.group(2));
			var order = 1l;
			var x = 0l;
			for (var i = encodedX.length-1; i >= 0; --i) {
				x += (encodedX[i] - this.beforeA) * order;
				order *= this.base;
			}
			return new Point(x, y);
		}
	};
}
