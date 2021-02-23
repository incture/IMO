package com.murphy.taskmgmt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.WorkBenchResponseDto;
import com.murphy.taskmgmt.dto.WrokBenchAudiLogDto;
import com.murphy.taskmgmt.service.interfaces.WorkbenchFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/workbench", produces = "application/json")
public class WorkBenchRest {
	
	@Autowired
	WorkbenchFacadeLocal facadeLocal;
	
	@RequestMapping(value = "/getTaskList", method = RequestMethod.GET)
	public WorkBenchResponseDto getTaskList(@RequestParam(value = "sortingOrder") String sortingOrder,
			@RequestParam(value = "sortObject") String sortObject , @RequestParam(value = "groupObject") String groupObject ,
					@RequestParam(value = "technicalRole") String technicalRole , @RequestParam(value = "locationCode") String locationCode ,
					@RequestParam(value = "locationType") String locationType , @RequestParam(value = "status") String status,
					@RequestParam(value = "isObx",required = false) String isObx) {
		return facadeLocal.getTaskList(sortingOrder, sortObject, groupObject , technicalRole, locationCode,locationType , status, isObx);

	}
	
	
	@RequestMapping(value = "/updateTaskStatus", method = RequestMethod.POST)
	public ResponseMessage updateTaskStatus(@RequestBody WrokBenchAudiLogDto dto){
		return facadeLocal.updateTaskStatus(dto);

	}
	
	@RequestMapping(value = "/autoCancelTaskStatus", method = RequestMethod.GET)
	public ResponseMessage autoCancelTaskStatus(){
		return facadeLocal.autoCancelObxTask();

	}

}
