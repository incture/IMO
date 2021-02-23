package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.StartTimeResponseDto;
import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
import com.murphy.taskmgmt.dto.TaskSchedulingResponseDto;
import com.murphy.taskmgmt.dto.TaskSchedulingUpdateDto;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingCalFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/scheduling", produces = "application/json")
public class TaskScheculingRest {

	@Autowired
	TaskSchedulingFacadeLocal facadeLocal;

	@Autowired
	TaskSchedulingCalFacadeLocal calFacadeLocal;

	@RequestMapping(value = "/updatePriority", method = RequestMethod.POST)
	public ResponseMessage updateTaskDetailsOnShifting(@RequestBody TaskSchedulingUpdateDto dto) {
		return calFacadeLocal.updateTaskDetailsOnShifting(dto);
		// return facadeLocal.updatePriority(dto);
	}

	@RequestMapping(value = "/getTasks", method = RequestMethod.GET)
	public TaskSchedulingResponseDto getTasks(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "orderBy", required = false) String orderBy,
			@RequestParam(value = "role") String role,
			@RequestParam(value = "timeZoneOffSet", required = false) int timeZoneOffSet,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {
		return facadeLocal.getDataForScheduling(userId, orderBy, role, timeZoneOffSet, fromDate,toDate);
	}

	@RequestMapping(value = "/getOwners", method = RequestMethod.GET)
	public TaskOwnersResponeDto getTaskOwnersbyId(@RequestParam(value = "taskId", required = false) String taskId,
			@RequestParam(value = "role", required = false) String role) {
		return facadeLocal.getTaskOwnersbyId(taskId, role);
	}

	@RequestMapping(value = "/intialUpdateOfPriority", method = RequestMethod.GET)
	public String intialUpdateOfPriority() {
		return facadeLocal.intialUpdateOfPriority();
	}

	@RequestMapping(value = "/getStartTimeForUser", method = RequestMethod.GET)
	public StartTimeResponseDto getStartTimeForUser(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "locationCode") String locationCode,
			@RequestParam(value = "classification", required = false) String classification,
			@RequestParam(value = "subClassification") String subClassification) {
		return facadeLocal.getStartTimeForUser(userId, locationCode, classification, subClassification);
	}
	
	
	// IOP Admin Console InProgress Task to ReAssign 
	@RequestMapping(value = "/reassignInAdminConsole", method = RequestMethod.POST)
	ResponseMessage reassignAdminConsole(@RequestBody CustomTaskDto dto) {
		return facadeLocal.reassignInAdminConsole(dto);
	}
	

}
