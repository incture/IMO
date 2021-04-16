package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.dto.ApprovePermitDto;
import com.incture.ptw.services.ApprovePermitService;
import com.incture.ptw.util.ResponseDto;

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
