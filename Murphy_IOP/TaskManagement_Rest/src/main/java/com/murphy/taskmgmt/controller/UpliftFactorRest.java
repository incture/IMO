package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.UpliftFactorDto;
import com.murphy.taskmgmt.service.interfaces.UpliftFactorFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/upliftfactor", produces = "application/json")
public class UpliftFactorRest {

	@Autowired
	private UpliftFactorFacadeLocal upliftFactorFacadeLocal;
	
	@RequestMapping(value = "/dispatch", method = RequestMethod.GET)
	UpliftFactorDto getUpliftFactor(@RequestParam(value = "locationCode") String locCode,
			@RequestParam(value = "startDate", required = false) Long startDate){
		return upliftFactorFacadeLocal.getUpliftFactor(locCode, startDate);
	}

}
