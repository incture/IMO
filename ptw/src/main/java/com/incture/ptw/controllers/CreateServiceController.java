package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.dto.CreateRequestDto;
import com.incture.ptw.services.CreateService;
import com.incture.ptw.util.ResponseDto;

@RestController
@RequestMapping
public class CreateServiceController {
	@Autowired
	private CreateService createService;
	@PostMapping("/createservice")
	public ResponseDto get(@RequestBody CreateRequestDto createRequestDto)
	{
		return createService.createService(createRequestDto);
	}
}
