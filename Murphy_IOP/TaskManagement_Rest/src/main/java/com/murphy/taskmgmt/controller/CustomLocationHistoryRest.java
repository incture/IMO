package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.CustomLocationHistoryDto;
import com.murphy.taskmgmt.service.interfaces.CustomLocationHistoryFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/locationHistory", produces = "application/json")
public class CustomLocationHistoryRest {
	
	@Autowired
	CustomLocationHistoryFacadeLocal facadeLocal;
	
	
	@RequestMapping(value = "/getLocTaskHistory", method = RequestMethod.GET)
	CustomLocationHistoryDto getLocTaskHistory(@RequestParam(value = "locationCode") String locationCode,
			@RequestParam(value = "monthTime", required = false) String monthTime,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "page_size") int page_size) {
		return facadeLocal.getLocTaskHistory(locationCode, monthTime,page,page_size);
	}

}
