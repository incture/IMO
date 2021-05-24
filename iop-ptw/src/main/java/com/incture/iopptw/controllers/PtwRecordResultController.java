package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.PtwRecordResultService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping
public class PtwRecordResultController {
	@Autowired
	private PtwRecordResultService ptwRecordResultService;
	@GetMapping("/getPtwRecordResult")
	public ResponseDto getPtwRecordResult(@RequestParam String permitNumber) {
		return ptwRecordResultService.getPtwRecordResult(permitNumber);
	}
}
