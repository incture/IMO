package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.GreetingResponseDto;
import com.murphy.taskmgmt.service.interfaces.GreetingFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/greeting", produces = "application/json")
public class GreetingRest {

	@Autowired
	private GreetingFacadeLocal greetingFacadeLocal;

	@RequestMapping(value = "/checkIfGreeted", method = RequestMethod.GET)
	GreetingResponseDto getUpliftFactor(@RequestParam(value = "userEmail") String userEmail) {
		return greetingFacadeLocal.checkIfGreeted(userEmail);
	}

}
