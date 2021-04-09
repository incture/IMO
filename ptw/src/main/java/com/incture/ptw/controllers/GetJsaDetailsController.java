package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.services.GetJsaDetailsService;
@RestController
@RequestMapping("/jsadetails")
public class GetJsaDetailsController {
	@Autowired
	private GetJsaDetailsService getJsaDetailsService;

	
	@GetMapping("/download")
	public void downloadData() {
		getJsaDetailsService.downloadDataService();

	}

}
