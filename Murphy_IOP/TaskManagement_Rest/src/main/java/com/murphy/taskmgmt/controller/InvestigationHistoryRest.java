package com.murphy.taskmgmt.controller;

import javax.ws.rs.QueryParam;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.interfaces.InvestigationHistoryLocal;
import com.murphy.integration.service.InvestigationHistoryService;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/investigationHistory", produces = "application/json")
public class InvestigationHistoryRest {
	
	InvestigationHistoryLocal investigationHistoryLocal;

	@RequestMapping(value = "/fetchInvestigationData", method = RequestMethod.GET)
	public UIResponseDto fetchInvestigationData(@QueryParam("uwiId") String uwiId) {
		investigationHistoryLocal = new InvestigationHistoryService();
		return investigationHistoryLocal.fetchInvestigationData(uwiId);
	}
	

}
