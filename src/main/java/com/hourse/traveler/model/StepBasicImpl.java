package com.hourse.traveler.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StepBasicImpl implements Step {
	private final long x;
	private final long y;
	
	public long alongX() {
		// TODO Auto-generated method stub
		return this.x;
	}

	public long alongY() {
		// TODO Auto-generated method stub
		return this.y;
	}

}
