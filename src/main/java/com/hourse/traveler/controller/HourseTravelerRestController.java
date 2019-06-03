package com.hourse.traveler.controller;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hourse.traveler.model.RectangleField;
import com.hourse.traveler.model.Point;
import com.hourse.traveler.service.Traveler;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/hourse/rest")
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
@Validated
public class HourseTravelerRestController {
	private final Traveler traveler;
	
	@GetMapping("/count")
	public long count(@RequestParam("width") @Min(1) long width,
			@RequestParam("height") @Min(1) long height,
			@RequestParam("start") Point start,
			@RequestParam("end") Point end)
	{
		var field = new RectangleField(new Point(1l,1l), new Point(width, height));
		return this.traveler.travel(field, start, end);
	}
}
