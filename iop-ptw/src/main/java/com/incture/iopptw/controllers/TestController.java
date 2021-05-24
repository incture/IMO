package com.incture.iopptw.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.entities.TestDo;
import com.incture.iopptw.services.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	private TestService testService;
	@GetMapping
	public String test()
	{
		return "Hello";
	}
	@GetMapping("/get")
	public List<TestDo> get()
	{
		return testService.getAllData();
	}
}
