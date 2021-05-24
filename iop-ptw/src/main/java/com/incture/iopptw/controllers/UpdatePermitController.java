package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.dtos.UpdatePermitRequestDto;
import com.incture.iopptw.services.UpdatePermitService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping
public class UpdatePermitController {
	
	@Autowired
	private UpdatePermitService updatePermitService;
	@PostMapping("/updatepermitservice")
	public ResponseDto get(@RequestBody UpdatePermitRequestDto updatePermitRequestDto){
		return updatePermitService.updatePermitDetails(updatePermitRequestDto);
	}

}
