package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.services.JsaByLocationService;
import com.incture.ptw.util.ResponseDto;

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
