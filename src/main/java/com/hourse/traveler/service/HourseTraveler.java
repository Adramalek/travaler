package com.hourse.traveler.service;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.hourse.traveler.model.Field;
import com.hourse.traveler.model.HourseStep;
import com.hourse.traveler.model.Point;

import lombok.NonNull;

public class HourseTraveler implements Traveler {
	private Field field;
	private Point currentPosition;
	private Point goalPosition;
	
	@Override
	public long travel(@NonNull Field field, @NonNull Point start, @NonNull Point end) {
		if (!field.contains(start) || !field.contains(end))
			return -1l;
		if (start.squareDistanceTo(end) == 0l)
			return 0l;
		this.field = field;
		this.currentPosition = start;
		this.goalPosition = end;
		var steps = 0l;
		while (this.currentPosition.squareDistanceTo(this.goalPosition) != 0l) {
			this.currentPosition = this.findNextPoint();
			++steps;
		}
		return steps;
	}
	
	private Point findNextPoint() {
		var five = Long.valueOf(5);
		return Arrays.stream(HourseStep.values())
				.filter(step -> this.field.canMove(this.currentPosition, step))
				.map(step -> this.currentPosition.moveBy(step))
				.collect(Collectors.groupingBy(point -> point.squareDistanceTo(this.goalPosition)))
				.entrySet().stream()
				.min((entry1, entry2) -> {
					if (five.compareTo(entry1.getKey()) == 0)
						return -1;
					if (five.compareTo(entry2.getKey()) == 0)
						return 1;
					var distanceDifference = entry1.getKey() - entry2.getKey();
					return (int) (distanceDifference / Math.abs(distanceDifference));
				})
				.flatMap(entry -> entry.getValue()
						.stream()
						.max((point1, point2) -> {
							var point1StepsCount = Arrays.stream(HourseStep.values())
									.filter(step -> this.field.canMove(point1, step))
									.count();
							var point2StepsCount = Arrays.stream(HourseStep.values())
									.filter(step -> this.field.canMove(point2, step))
									.count();
							return (int) (point1StepsCount - point2StepsCount);
						}))
				.get();
	}
}
