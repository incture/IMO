package com.incture.iopptw.template.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/template")
public class TemplateController {

	@PostMapping("/create")
	public ResponseDto createTemplate() {
		return null;
	}

	@GetMapping("/get-all")
	public ResponseDto getTemplateList() {
		return null;

	}

	@GetMapping("/get")
	public ResponseDto getById(@RequestParam int id) {
		return null;
	}
}
