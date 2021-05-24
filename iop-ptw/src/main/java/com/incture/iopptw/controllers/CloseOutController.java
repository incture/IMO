package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.dtos.CloseOutReqDto;
import com.incture.iopptw.services.CloseOutService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping
public class CloseOutController {
	@Autowired
	private CloseOutService closeOutService;
	@PostMapping("/closeoutservice")
	public ResponseDto get(@RequestBody CloseOutReqDto closeOutReqDto){
		return closeOutService.closeOutService(closeOutReqDto);
	}
}
