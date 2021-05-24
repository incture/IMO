package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.PermitDetailsService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/permitdetails")
public class PermitDetailsController {
	@Autowired
	private PermitDetailsService permitDetailsService;

	@GetMapping("/get")
	public ResponseDto downloadData(@RequestParam String permitNumber, @RequestParam String permitType) {
		return permitDetailsService.getPermitDetails(permitNumber, permitType);

	}

}
