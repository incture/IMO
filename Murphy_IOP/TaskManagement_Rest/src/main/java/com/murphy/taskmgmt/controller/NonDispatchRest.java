package com.murphy.taskmgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.AuditReportDto;
import com.murphy.taskmgmt.dto.NonDispatchResponseDto;
import com.murphy.taskmgmt.dto.NonDispatchTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskTypeResponseDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.service.interfaces.NonDispatchFacadeLocal;


@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/nonDispatch", produces = "application/json")
public class NonDispatchRest {

	@Autowired
	NonDispatchFacadeLocal facadeLocal;

	@RequestMapping(value = "/readByLocation", method = RequestMethod.GET)
	public NonDispatchResponseDto getAllData(@RequestParam(value ="group", required=false) String group,@RequestParam(value ="location", required=false ) String location,@RequestParam(value ="locType", required=false ) String locType) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.getAllTasks(group, location,locType);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseMessage createTask(@RequestBody NonDispatchTaskDto dto) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.createTask(dto);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMessage deleteTask(@RequestBody UpdateRequestDto dto) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.deleteTask(dto);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseMessage updateTask(@RequestBody NonDispatchTaskDto dto) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.updateTask(dto);
	}
	
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public ResponseMessage completeTask(@RequestBody UpdateRequestDto dto) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.completeTask(dto);
	}
	
	@RequestMapping(value = "/getTaskTypes", method = RequestMethod.GET)
	public TaskTypeResponseDto getTaskTypes() {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.getTaskTypes();
	}
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET)
	public List<AuditReportDto> getReport() {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.getReport();
	}
	
	
	
	
}

