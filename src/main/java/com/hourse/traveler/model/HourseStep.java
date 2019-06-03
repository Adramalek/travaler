package com.hourse.traveler.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HourseStep implements Step {
	UP_RIGHT(Step.of(1l, 2l)),
	RIGHT_UP(Step.of(2l, 1l)),
	RIGHT_DOWN(Step.of(2l, -1l)),
	DOWN_RIGHT(Step.of(1l, -2l)),
	DOWN_LEFT(Step.of(-1l, -2l)),
	LEFT_DOWN(Step.of(-2l, -1l)),
	LEFT_UP(Step.of(-2l, 1l)),
	UP_LEFT(Step.of(-1l, 2l));
	
	private final Step step;
	@Override
	public long alongX() {
		return this.step.alongX();
	}
	@Override
	public long alongY() {
		return this.step.alongY();
	}
	@Override
	public String toString() {
		return super.toString() + "(x=" + step.alongX() + ",y=" + step.alongY() + ")";
	}
	
}
