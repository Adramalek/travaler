package com.hourse.traveler.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class Point {
	private final long x;
	private final long y;
	
	public Point moveBy(@NonNull Step step) {
		return new Point(this.x + step.alongX(), this.y + step.alongY());
	}
	
	public long squareDistanceTo(@NonNull Point point) {
		var x = point.x - this.x;
		var y = point.y - this.y;
		return x*x + y*y;
	}
}
