package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.JsaDetailsService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/jsadetails")
public class JsaDetailsController {
	@Autowired
	private JsaDetailsService getJsaDetailsService;

	@GetMapping("/download")
	public ResponseDto downloadData() {
		return getJsaDetailsService.downloadDataService();

	}

}
