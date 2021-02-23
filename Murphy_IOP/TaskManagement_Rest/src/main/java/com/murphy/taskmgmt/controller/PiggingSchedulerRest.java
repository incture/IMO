package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.PiggingSchedulerDto;
import com.murphy.taskmgmt.dto.PiggingUIReqDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.PiggingSchedulerFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/piggingWO", produces = "application/json")
public class PiggingSchedulerRest {

	@Autowired
	PiggingSchedulerFacadeLocal piggingSchedulerfacade;
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseMessage insertWOData(@RequestBody PiggingUIReqDto  dto) {
		return piggingSchedulerfacade.insertToDB(dto);
			
	}
	
//	@RequestMapping(value = "/update", method = RequestMethod.POST)
//	public ResponseMessage updateWorkOrder(@RequestBody PiggingSchedulerDto  dto) {
//		return piggingSchedulerfacade.updateWorkOrder(dto);
//			
//	}

}
