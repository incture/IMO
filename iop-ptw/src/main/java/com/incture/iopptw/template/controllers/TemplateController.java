package com.incture.iopptw.template.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.template.dtos.CreateTemplateDto;
import com.incture.iopptw.template.dtos.TemplateDto;
import com.incture.iopptw.template.services.TemplateService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/template")
public class TemplateController {
	@Autowired
	private TemplateService templateService;

	@PostMapping("/create")
	public ResponseDto createTemplate(@RequestBody CreateTemplateDto createTemplateDto) {
		TemplateDto templateDto = new TemplateDto();
		templateDto.setName(createTemplateDto.getName());
		return templateService.saveTemplate(templateDto, createTemplateDto);
	}

	@GetMapping("/get-all")
	public ResponseDto getTemplateList() {
		return templateService.getAllTemplateList();

	}

	@GetMapping("/get")
	public ResponseDto getById(@RequestParam int tmpId) {
		return templateService.getTemplateById(tmpId);
	}

}
