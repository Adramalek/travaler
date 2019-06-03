package com.hourse.traveler.service;

import com.hourse.traveler.model.Field;
import com.hourse.traveler.model.Point;

import lombok.NonNull;

public interface Traveler {
	long travel(@NonNull Field field, @NonNull Point start, @NonNull Point end);
}
