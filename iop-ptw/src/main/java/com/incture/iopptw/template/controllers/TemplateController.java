package com.incture.iopptw.template.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.template.dtos.TemplateDto;
import com.incture.iopptw.template.services.TemplateService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/template")
public class TemplateController {
	@Autowired
	private TemplateService templateService;

	@PostMapping("/create")
	public ResponseDto createTemplate(@RequestBody TemplateDto templateDto) {
		return templateService.createTemplateService(templateDto);
	}

	@PostMapping("/save")
	public ResponseDto saveTemplate() {
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
