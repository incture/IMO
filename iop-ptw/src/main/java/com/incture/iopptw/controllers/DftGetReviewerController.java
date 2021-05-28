package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.dtos.DftGetReviewerPayloadDto;
import com.incture.iopptw.services.DftGetReviewerService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping
public class DftGetReviewerController {
	@Autowired
	private DftGetReviewerService dftGetReviewerService;

	@PostMapping("/getreviewerbylocation")
	public ResponseDto getReviewerByLoc(@RequestBody DftGetReviewerPayloadDto d) {
		System.out.println(d);
		return dftGetReviewerService.getReviewerByLoc(d);
	}
}
