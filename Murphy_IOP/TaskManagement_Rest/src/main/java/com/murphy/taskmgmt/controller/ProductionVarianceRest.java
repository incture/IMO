package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.dto.UIRequestDto;
import com.murphy.taskmgmt.dto.DOPVarianceResponseDto;
import com.murphy.taskmgmt.service.DGPFacade;
import com.murphy.taskmgmt.service.interfaces.VarianceFacadeLocal;


@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/variance", produces = "application/json")
public class ProductionVarianceRest {
	
	@Autowired
	VarianceFacadeLocal facadeLocal;
	
	@Autowired
	DGPFacade dgpFacade;
	
	
//	private static final Logger logger = LoggerFactory.getLogger(ProductionVarianceRest.class);

	
	//Dop Screen
	@RequestMapping(value = "/fetchVarianceData", method = RequestMethod.POST)
	public DOPVarianceResponseDto fetchVarianceData(@RequestBody UIRequestDto uiRequestDto) {
		
		return facadeLocal.fetchVarianceData(uiRequestDto);
	}
	
//  Not Consumed from UI	
	@RequestMapping(value = "/stageProveData", method = RequestMethod.GET)
	String createRecord() {
		
		return facadeLocal.createRecord();
	}
	
	//DGP & DOP Screen
	//Single Service to be consumed from UI
	@RequestMapping(value = "/fetchDOPDGPData", method = RequestMethod.POST)
	public DOPVarianceResponseDto fetchDGPDOPData(@RequestBody UIRequestDto uiRequestDto) {
		return dgpFacade.fetchDGPDOPData(uiRequestDto);
	}

}
