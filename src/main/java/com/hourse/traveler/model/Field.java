package com.hourse.traveler.model;

import lombok.NonNull;

public interface Field {
	boolean contains(@NonNull Point point);
	boolean canMove(@NonNull Point point, @NonNull Step step);
}
