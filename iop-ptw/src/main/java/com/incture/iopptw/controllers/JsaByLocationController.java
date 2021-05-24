package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.JsaByLocationService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/getjsabylocation")
public class JsaByLocationController {
	@Autowired
	private JsaByLocationService jsaByLocationService;

	@GetMapping()
	public ResponseDto getDetails(@RequestParam(required = false) String muwi, @RequestParam String facility) {
		return jsaByLocationService.getJsaByLocation(muwi, facility);
	}

}
