package com.hourse.traveler.model;

import lombok.NonNull;
import lombok.ToString;

@ToString
public class RectangleField implements Field {
	private final Point leftBottomCorner;
	private final Point rightTopCorner;
	
	public RectangleField(@NonNull Point leftBottomCorner, @NonNull Point rightTopCorner) {
		super();
		this.validateCorners(leftBottomCorner, rightTopCorner);
		this.leftBottomCorner = leftBottomCorner;
		this.rightTopCorner = rightTopCorner;
	}

	@Override
	public boolean contains(@NonNull Point point) {
		return point.getX() >= this.leftBottomCorner.getX() &&
				point.getX() <= this.rightTopCorner.getX() &&
				point.getY() >= this.leftBottomCorner.getY() &&
				point.getY() <= this.rightTopCorner.getY();
	}
	
	@Override
	public boolean canMove(@NonNull Point point, @NonNull Step step) {
		return this.contains(point) && this.contains(point.moveBy(step));
	}
	
	private void validateCorners(Point leftBottomCorner, Point rightTopCorner) {
		if (leftBottomCorner.getX() > rightTopCorner.getX() || leftBottomCorner.getY() > rightTopCorner.getY())
			throw new IllegalArgumentException("'rightTopCorner' must have greater x and y coordinate values than 'leftBottomCorner'.");
	}
}
