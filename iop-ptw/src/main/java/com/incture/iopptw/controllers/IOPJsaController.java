package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.IOPJsaService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/getjsalist")
public class IOPJsaController {
	@Autowired
	private IOPJsaService iopJsaService;
	
	@GetMapping()
	public ResponseDto getDetails(@RequestParam String facilityOrSite, @RequestParam Integer isActive) {
		return iopJsaService.getJsaList(facilityOrSite, isActive);
	}
}
