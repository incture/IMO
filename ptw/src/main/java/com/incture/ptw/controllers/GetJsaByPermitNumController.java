package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.services.GetJsaByPermitNumService;
import com.incture.ptw.util.ResponseDto;

@RestController
@RequestMapping("/getjsabypermitnumber")
public class GetJsaByPermitNumController {
	@Autowired
	private GetJsaByPermitNumService getJsaByPermitNumService;
	public ResponseDto getDetails(@RequestParam String permitNumber) {
		return getJsaByPermitNumService.getJsaByPermitNum(permitNumber);
	}
	
}
