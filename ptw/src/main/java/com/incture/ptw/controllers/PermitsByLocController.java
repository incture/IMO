package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.services.PermitsByLocService;
import com.incture.ptw.util.ResponseDto;

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
