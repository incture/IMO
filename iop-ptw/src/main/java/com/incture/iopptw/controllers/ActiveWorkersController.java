package com.incture.iopptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.iopptw.services.ActiveWorkersService;
import com.incture.iopptw.utils.ResponseDto;

@RestController
@RequestMapping("/getListOfActiveWorker")
public class ActiveWorkersController {
	@Autowired
	private ActiveWorkersService getActiveWorkersService;

	@GetMapping()
	public ResponseDto getDetails(@RequestParam(required = false) String muwi, @RequestParam String facility) {
		return getActiveWorkersService.getActiveWorkers(muwi, facility);
	}

}
