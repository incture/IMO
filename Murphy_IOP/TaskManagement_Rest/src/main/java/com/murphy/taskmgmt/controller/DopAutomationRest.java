package com.murphy.taskmgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.DOPResponseDto;
import com.murphy.taskmgmt.dto.DopDummyDto;
import com.murphy.taskmgmt.service.interfaces.DopAutomationFacadeLocal;
//these classes are no longer used

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/tasks", produces = "application/json")
public class DopAutomationRest {
	@Autowired
	DopAutomationFacadeLocal dopAutomationFacadeLocal;
	
	// GET Production Variance
	@RequestMapping(value = "/getDopVarianceData", method = RequestMethod.GET)
	public List<DopDummyDto> getVarianceData() {
		
		return dopAutomationFacadeLocal.fetchVarianceData();
	}
	
	@RequestMapping(value = "/getTaskId", method = RequestMethod.GET)
	public DOPResponseDto dgpQueryForOtherDetails(@RequestParam("locationCode")String locationCodesList)
	{
		return dopAutomationFacadeLocal.dgpQueryForOtherDetails(locationCodesList);
	}
//	
}
