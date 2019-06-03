package com.hourse.traveler.model;

public interface Step {
	long alongX();
	long alongY();
	static Step of(long x, long y) {
		return new StepBasicImpl(x, y);
	}
}
