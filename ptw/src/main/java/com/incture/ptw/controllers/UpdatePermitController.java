package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.dto.UpdatePermitRequestDto;
import com.incture.ptw.services.UpdatePermitService;
import com.incture.ptw.util.ResponseDto;

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
