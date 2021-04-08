package com.incture.ptw.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ptw.dto.CreateRequestDto;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@GetMapping("/get")
	public String test()
	{
		return "Hello";
	}
	@PostMapping("/forDto")
	public void get(@RequestBody CreateRequestDto createRequestDto)
	{
		System.out.println(createRequestDto);
	}
}
