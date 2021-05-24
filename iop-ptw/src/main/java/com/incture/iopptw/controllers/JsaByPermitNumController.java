package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.JsaByPermitNumService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/getjsabypermitnumber")
public class JsaByPermitNumController {
	@Autowired
	private JsaByPermitNumService getJsaByPermitNumService;

	@GetMapping()
	public ResponseDto getDetails(@RequestParam String permitNumber) {
		return getJsaByPermitNumService.getJsaByPermitNum(permitNumber);
	}

}
