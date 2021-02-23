package com.murphy.taskmgmt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.scheduler.CanaryStagingScheduler;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/canary", produces = "application/json")
public class CanaryStagingRest {
	
	private static final Logger logger = LoggerFactory.getLogger(CanaryStagingRest.class);

	@Autowired
	private CanaryStagingScheduler canaryStagingScheduler;

	@RequestMapping(value = "/stageData", method = RequestMethod.GET)
	public void stageData() {
		System.err.println("CanaryStagingRest.stageData()");
		canaryStagingScheduler.stageData();
	}

	@RequestMapping(value = "/clearAndCopyData", method = RequestMethod.GET)
	public int clearAndCopyData(String endTimeDBFormat) {
		System.err.println("CanaryStagingRest.clearAndCopyData(" + endTimeDBFormat + ")");
		try {
			return canaryStagingScheduler.stageData(endTimeDBFormat);
		} catch (Exception e) {
			System.err.println("CanaryStagingRest.clearAndCopyData(" + endTimeDBFormat + ")" + e.getMessage());
		}
		return -1;
	}
	
	@RequestMapping(value = "/clearAndCopyDataTemp", method = RequestMethod.GET)
	public int clearAndCopyData(String startTimeDBFormat, String endTimeDBFormat) {
		logger.error("CanaryStagingRest.clearAndCopyDataTemp endTimeDBFormat" + endTimeDBFormat + "start : " + startTimeDBFormat);
		try {
			 return canaryStagingScheduler.stagedatatemp(startTimeDBFormat,endTimeDBFormat);
		} catch (Exception e) {
			System.err.println("CanaryStagingRest.clearAndCopyDataTemp(" + endTimeDBFormat + ")" + e.getMessage());
			logger.error("CanaryStagingRest.clearAndCopyDatatemp(" + endTimeDBFormat + ")" + e.getMessage());
		}
		return -1;
	}

	@RequestMapping(value = "/stageDataForDOP", method = RequestMethod.GET)
	public void stageDataForDOP() {
		System.err.println("CanaryStagingRest.stageDataForDOP()");
		canaryStagingScheduler.stageDataForDOP();
	}

	@RequestMapping(value = "/clearAndCopyDataForDOP", method = RequestMethod.GET)
	public int clearAndCopyDataForDOP(@RequestParam String endTimeDBFormat) {
		System.err.println("CanaryStagingRest.clearAndCopyData(" + endTimeDBFormat + ")");
		try {
			//original code
			//return canaryStagingScheduler.stageDataForDOP(endTimeDBFormat);
			
			//added as part of dop query performance issue fix
			return canaryStagingScheduler.stagingDataForDOPDGPThroughAPICall(endTimeDBFormat);
		} catch (Exception e) {
			System.err.println("CanaryStagingRest.clearAndCopyDataForDOP(" + endTimeDBFormat + ")" + e.getMessage());
		}
		return -1;
	}

	@RequestMapping(value = "/prepParamsForDOP", method = RequestMethod.GET)
	public String prepParamsForDOP(String endTimeDBFormat) {
		System.err.println("CanaryStagingRest.prepParamsForDOP(" + endTimeDBFormat + ")");
		return canaryStagingScheduler.prepParamsForDOP(endTimeDBFormat).toString();
	}

	
	@RequestMapping(value = "/getUserToken", method = RequestMethod.GET)
	public String getUserToken() {
		System.err.println("CanaryStagingRest.getUserToken()");
		return canaryStagingScheduler.getUserToken();
	}
	
	@RequestMapping(value = "/revokeUserToken", method = RequestMethod.GET)
	public String revokeUserToken(String userToken) {
		System.err.println("CanaryStagingRest.revokeUserToken(" + userToken + ")");
		canaryStagingScheduler.revokeUserToken(userToken);
		return "Invoked";
	}
}