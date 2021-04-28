package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.dto.CloseOutReqDto;
import com.incture.ptw.services.CloseOutService;
import com.incture.ptw.util.ResponseDto;

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
