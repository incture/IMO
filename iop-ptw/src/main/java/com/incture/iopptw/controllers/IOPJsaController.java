package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.IOPJsaService;
import com.incture.iopptw.services.IOPPtwService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping
public class IOPJsaController {
	@Autowired
	private IOPJsaService iopJsaService;
	@Autowired
	private IOPPtwService iopPtwService;
	@GetMapping("/getjsalist")
	public ResponseDto getDetails(@RequestParam String facilityOrSite, @RequestParam Integer isActive) {
		return iopJsaService.getJsaList(facilityOrSite, isActive);
	}
	@GetMapping("/getpermitlist")
	public ResponseDto getPermitDetails(@RequestParam int isCwp,@RequestParam int isHwp,@RequestParam int isCse,@RequestParam String location, @RequestParam int isActive) {
		return iopPtwService.getPtwList(isCwp, isHwp, isCse, location, isActive);
	}
}
