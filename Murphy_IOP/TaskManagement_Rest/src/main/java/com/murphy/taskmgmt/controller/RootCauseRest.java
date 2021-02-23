package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.RootCauseResponseDto;
import com.murphy.taskmgmt.service.interfaces.RootCauseFacadeLocal;


@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/rootCause", produces = "application/json" )
public class RootCauseRest {

	@Autowired
	RootCauseFacadeLocal facadeLocal;

	@RequestMapping(value = "/getByStatus", method = RequestMethod.GET)
	public RootCauseResponseDto getRootCause(@RequestParam("taskId") String taskId,@RequestParam("status") String status ,@RequestParam(value ="origin" , required=false) String origin) {
		return facadeLocal.getRootCauseByTaskId(taskId, status , origin);
	}

	@RequestMapping(value = "/getByStatusNew", method = RequestMethod.GET)
	public RootCauseResponseDto getRootCauseInArray(@RequestParam("taskId") String taskId,@RequestParam("status") String status ,@RequestParam(value ="origin" , required=false) String origin) {
		return facadeLocal.getRootCauseByTaskIdInArray(taskId, status, origin);
	}

	
}

