package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.dto.UIRequestDto;
import com.murphy.taskmgmt.dto.CheckListResponseDto;
import com.murphy.taskmgmt.dto.PWHopperResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.PWHopperFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/pwhopper", produces = "application/json")
public class PWHopperRest {

	@Autowired
	PWHopperFacadeLocal facadeLocal;

	@RequestMapping(value = "/saveOrUpdateInvestInsts", method = RequestMethod.POST)
	ResponseMessage saveOrUpdateInvestInsts(@RequestBody CheckListResponseDto requestDto) {
		 return facadeLocal.saveOrUpdateInvestInsts(requestDto , true);
	}

	@RequestMapping(value = "/getCheckList", method = RequestMethod.GET)
	CheckListResponseDto getTaskDetails(@RequestParam(value = "userType", required = true) String userType,
			@RequestParam(value = "locationCode", required = false) String locationCode ,
			@RequestParam(value = "investigationId", required = false) String investigationId) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.getCheckList( userType, locationCode ,investigationId);
	}
	
	@RequestMapping(value = "/removeProactive", method = RequestMethod.GET)
	ResponseMessage saveOrUpdateInvestInsts(@RequestParam(value = "locationCode", required = true) String locationCode) {
		 return facadeLocal.removeProactive(locationCode);
	}

	
	@RequestMapping(value = "/getpwHopperList", method = RequestMethod.POST)
	PWHopperResponseDto getpwHopperList(@RequestBody UIRequestDto requestDto) {
		 return facadeLocal.getpwHopperList(requestDto);
	}
	
	@RequestMapping(value = "/setDataForPWHopperWell", method = RequestMethod.POST)
	String setDataForPWHopperWell() {
		 return facadeLocal.setDataForPWHopperWell();
	}


}
