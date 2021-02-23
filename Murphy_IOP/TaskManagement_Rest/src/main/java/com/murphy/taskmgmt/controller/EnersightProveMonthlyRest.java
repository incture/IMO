package com.murphy.taskmgmt.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.dto.PROVEUIResponseDto;
import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.interfaces.EnersightProveMonthlyLocal;
import com.murphy.integration.service.EnersightProveDaily;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/proveReport", produces = "application/json")
public class EnersightProveMonthlyRest {

	// TODO Why is enersightProveMonthlyLocal not injected? Is this code tested?
	EnersightProveMonthlyLocal enersightProveMonthlyLocal;

	@RequestMapping(value = "/fetchProveVarianceData", method = RequestMethod.POST)
	public PROVEUIResponseDto fetchProveData(@RequestBody UIRequestDto uiRequestDto) {
//		System.err.println("[fetchProveData] : INFO- uiRequestDto " + uiRequestDto);
		return new EnersightProveDaily().fetchProveData(uiRequestDto);
	}

	
	//Not Consumed from UI
	@RequestMapping(value = "/fetchProveDailyData", method = RequestMethod.GET)
	public UIResponseDto fetchProveDailyData() {
		// System.err.println("[fetchProveDailyData] :
		// EnersightProveMonthlyRest");
		return new EnersightProveDaily().fetchProveDailyData();
	}

	//Not Consumed from UI
	@RequestMapping(value = "/fetchFracData", method = RequestMethod.GET)
	public Map<String, String> fetchFracData() {
		Set<String> uwiSet = new HashSet<String>();
		uwiSet.add("9264201300000000PLR0013H1");
		uwiSet.add("9264201300000000SCD0004H2");
		return new EnersightProveDaily().fetchFracData(uwiSet);

	}

	//Not Consumed from UI
	@RequestMapping(value = "/fetchPigData", method = RequestMethod.GET)
	public Map<String, String> fetchPigData() {
		Set<String> uwiSet = new HashSet<String>();
		uwiSet.add("9264201300000000PLR0013H1");
		uwiSet.add("9264201300000000SCD0004H2");
		return new EnersightProveDaily().fetchPiggingValue(uwiSet);
	}
}