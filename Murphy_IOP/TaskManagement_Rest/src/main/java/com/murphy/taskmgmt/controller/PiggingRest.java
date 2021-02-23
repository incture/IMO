package com.murphy.taskmgmt.controller;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.PiggingWorkOrderCreationDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.PiggingFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/pigging", produces = "application/json")
public class PiggingRest {

	@Autowired
	PiggingFacadeLocal piggingfacade;
	

	@RequestMapping(value="/task",method=RequestMethod.GET)
	public ResponseMessage createTask(@RequestParam(value = "equipmentId") String equipmentId,@RequestParam(value = "workorderNo") String workorderNo){
		
		return piggingfacade.createTask(equipmentId,workorderNo);
	}
	@RequestMapping(value="hciPayload", method=RequestMethod.GET)
	public PiggingWorkOrderCreationDto getPayload(){
		
		PiggingWorkOrderCreationDto dto= new PiggingWorkOrderCreationDto();
		dto.setEquipment("000000000020468012");
		dto.setFunctionalLocation("MUR-US-EFS-CT00-BNGS");
		dto.setOrder("PM02");
		dto.setMnWkCtr("UONOP_01");
		dto.setPlannerGroup("P05/PS01");
		dto.setPmActType("PIG");
		dto.setOperation("EFS Routing Pigging");
		dto.setWkCtr("UONOP_01");
		dto.setActType("WOOPS");
		
		return dto;
	}
	@RequestMapping(value="createPayload", method=RequestMethod.POST)
	public CustomTaskDto getCreatePayload(@RequestBody CustomTaskDto customTaskDto){
		return piggingfacade.getRetrivePayload(customTaskDto);
	}
	@RequestMapping(value="/pigTime",method=RequestMethod.GET)
	public Double createTasks(@RequestParam(value = "equipmentId") String equipmentId){
		    
		double val= piggingfacade.calculatePiggingTime(equipmentId);
		return val;
	}
	@RequestMapping(value="/taskCreation",method=RequestMethod.GET)
	public ResponseMessage createTasks(){
		
		return piggingfacade.UpdatePiggingHistory(new Date());
	}
}
