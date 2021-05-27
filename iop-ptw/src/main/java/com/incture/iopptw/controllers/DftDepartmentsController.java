package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.DftDepartmentsService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping
public class DftDepartmentsController {
	@Autowired
	private DftDepartmentsService dftDepartmentsService;
	
	@GetMapping("/getalldepartment")
	public ResponseDto getAllDepartments(){
		return dftDepartmentsService.getAllDepartments();
	}
}
