package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.PermitsByLocService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/getpermitsbylocation")
public class PermitsByLocController {
	@Autowired
	private PermitsByLocService permitsByLocService;
	
	@GetMapping()
	public ResponseDto getPermitsbyLoc(@RequestParam(required = false) String muwi, @RequestParam String facility){
		return permitsByLocService.getPermitsByLoc(muwi, facility);
	}
	
}
