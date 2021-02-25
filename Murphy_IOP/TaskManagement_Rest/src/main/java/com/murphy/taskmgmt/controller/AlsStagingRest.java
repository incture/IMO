package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.dto.UIResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.AlsStagingFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/alsStaging", produces = "application/json")
public class AlsStagingRest {

	@Autowired
	AlsStagingFacadeLocal facadeLocal;

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	ResponseMessage createAlsStaging(@RequestParam(value = "muwi") String muwi) {
		return facadeLocal.stageALSData(muwi);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	ResponseMessage deleteAls(@RequestParam(value = "muwi") String muwi) {
		return facadeLocal.removeALSData(muwi);
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	UIResponseDto readAlsStaging(@RequestParam(value = "uwiId") String uwiId) {
		return facadeLocal.getInvestigationDetails(uwiId);
	}

}
