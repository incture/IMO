package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.dtos.ApprovePermitDto;
import com.incture.iopptw.services.ApprovePermitService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/approvepermit")
public class ApprovePermitController {
	@Autowired
	private ApprovePermitService approvePermitService;

	@PostMapping()
	public ResponseDto approveJsa(@RequestBody ApprovePermitDto approvePermitDto) {
		return approvePermitService.approvePermit(approvePermitDto);
	}

}
