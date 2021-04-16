package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.services.ApproveJsaService;
import com.incture.ptw.util.ResponseDto;

@RestController
@RequestMapping("/approvejsa")
public class ApproveJsaController {
	@Autowired
	private ApproveJsaService approveJsaService;
	
	@PostMapping()
	public ResponseDto approveJsa(String jsaPermitNumber, String status, String approvedBy){
		return approveJsaService.approveJsa(jsaPermitNumber, status, approvedBy);
	}

}
