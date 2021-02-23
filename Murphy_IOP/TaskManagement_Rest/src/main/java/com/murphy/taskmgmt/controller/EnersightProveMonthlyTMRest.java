package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.dto.UIResponseDto;
import com.murphy.taskmgmt.service.interfaces.EnersightProveMonthlyLocal;



@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/proveReportTM", produces = "application/json")
public class EnersightProveMonthlyTMRest {
	
	@Autowired
	EnersightProveMonthlyLocal enersightProveMonthlyLocal;
	

	@RequestMapping(value = "/fetchProveDailyData", method = RequestMethod.GET)
	public UIResponseDto fetchProveDailyData(){

		return enersightProveMonthlyLocal.fetchProveDailyData();
	}
	

}
