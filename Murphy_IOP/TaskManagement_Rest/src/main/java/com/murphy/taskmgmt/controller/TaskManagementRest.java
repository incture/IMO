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

import com.murphy.taskmgmt.dao.ATSTaskAssignmentDao;
import com.murphy.taskmgmt.dto.AttrTempResponseDto;
import com.murphy.taskmgmt.dto.CheckForCreateTaskDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.FLSOPResponseDto;
import com.murphy.taskmgmt.dto.FieldAvailabilityResponseDto;
import com.murphy.taskmgmt.dto.IopTaskListDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.NDVTaskListResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskDetailResponseDto;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskListResponseDto;
import com.murphy.taskmgmt.dto.TaskTemplatesDto;
import com.murphy.taskmgmt.dto.UIResponseDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.dto.UserTaskCountList;
import com.murphy.taskmgmt.factory.TaskSchedulingFactory;
import com.murphy.taskmgmt.service.AutoTaskSchedulingFacade;
import com.murphy.taskmgmt.service.interfaces.TaskManagementFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingCalFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.TaskTemplateFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/tasks", produces = "application/json")
public class TaskManagementRest {

	@Autowired
	TaskManagementFacadeLocal facadeLocal;

	@Autowired
	TaskSchedulingCalFacadeLocal calFacadeLocal;

	@Autowired
	TaskTemplateFacadeLocal templateLocal;
	
	@Autowired
	AutoTaskSchedulingFacade atsf;
	
	@Autowired
	ATSTaskAssignmentDao atsD;
	
	@Autowired
	TaskSchedulingFactory calFactory;

	@RequestMapping(value = "/createTaskFromTemplate", method = RequestMethod.POST)
	ResponseMessage createTaskFromTemplate(@RequestBody TaskTemplatesDto dto) {
		// LOGGER.info("Inside delivery data creation");
		return templateLocal.createTaskFromTemplate(dto);
	}

	@RequestMapping(value = "/createTask", method = RequestMethod.POST)
	ResponseMessage createTask(@RequestBody CustomTaskDto dto) {
		// LOGGER.info("Inside delivery data creation");
		// return facadeLocal.createTask(dto);
		//return calFacadeLocal.createTask(dto);
		return calFactory.getSchedulingFacade(dto.getTaskEventDto().getLocationCode()).createTask(dto);
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	CustomTaskDto getTaskDetails(@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "userType", required = false) String userType,
			@RequestParam(value = "loggedInUser", required = false) String loggedInUser,
			@RequestParam(value = "loggedInUserGrp", required = false) String loggedInUserGrp,
			@RequestParam(value = "hopperuserType", required = false) String hopperuserType) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.getTaskDetails(taskId, userType, loggedInUser, loggedInUserGrp, hopperuserType);
	}

	@RequestMapping(value = "/getHeader", method = RequestMethod.GET)
	AttrTempResponseDto getCustomHeaders(@RequestParam(value = "taskTempId") String taskTempId,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "locType", required = false) String locType,
			@RequestParam(value = "classification", required = false) String classification,
			@RequestParam(value = "locationCode", required = false) String locationCode,
			@RequestParam(value = "compressor", required = false) String compressor,
			@RequestParam(value = "userType", required = false) String userType,
			@RequestParam(value = "duration", required = false) String duration) {
		// LOGGER.info("Inside delivery data creation");
		int durationInInt = 0;
		if (!ServicesUtil.isEmpty(duration)) {
			durationInInt = Integer.parseInt(duration);
		}
		return facadeLocal.getCustomHeaders(taskTempId, location, locType, classification, locationCode, compressor,
				userType, durationInInt);
	}

	@RequestMapping(value = "/getTasksByUser", method = RequestMethod.GET)
	TaskListResponseDto getTaskList(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "userType") String userType,
			@RequestParam(value = "group", required = false) String group,
			@RequestParam(value = "taskType", required = false) String taskType,
			@RequestParam(value = "origin", required = false) String origin,
			@RequestParam(value = "locationCode", required = false) String locationCode,
			@RequestParam(value = "isCreatedByMe", required = false) Boolean isCreatedByMe,
			@RequestParam(value = "device", required = false) String device,
			@RequestParam(value = "tier", required = false) String tier,
			@RequestParam(value = "isForToday", required = false) Boolean isForToday,
			@RequestParam(value = "classification", required = false) String classification,
			@RequestParam(value = "subClassification", required = false) String subClassification,
			@RequestParam(value = "country", required = false) String country) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.getTaskList(userId, userType, group, taskType, origin, locationCode, isCreatedByMe,
				device,tier,isForToday,classification,subClassification,country);
		
	}

	@RequestMapping(value = "/getTasksCount", method = RequestMethod.GET)
	UIResponseDto assignedTasksToUserCount(@RequestParam(value = "userId", required = false) String userId) {
		return facadeLocal.assignedTasksToUserCount(userId);
	}

	@RequestMapping(value = "/getUsersTasksCount", method = RequestMethod.GET)
	UserTaskCountList assignedTasksToUsersCount(@RequestParam(value = "pIDs", required = false) String pIdList) {
		return facadeLocal.assignedTasksToUsersCount(pIdList);
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	ResponseMessage changeStatus(@RequestBody UpdateRequestDto dto) {
		return facadeLocal.changeStatus(dto, "");
	}
	
	@RequestMapping(value = "/updateTask", method = RequestMethod.POST)
	ResponseMessage updateTask(@RequestBody CustomTaskDto dto) {
		// LOGGER.info("Inside delivery data creation");
		//return facadeLocal.updateTask(dto);
		return calFactory.getTaskManagementFacade(dto.getTaskEventDto().getLocationCode()).updateTask(dto);
	}

	@RequestMapping(value = "/getNDVTaskList", method = RequestMethod.GET)
	NDVTaskListResponseDto getNDVTaskList(@RequestParam(value = "role") String role,
			@RequestParam(value = "userType", required = false) String userType,
			@RequestParam(value = "loggedInUserType", required = false) String loggedInUserType) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.getNDVTaskList(role, userType, loggedInUserType);
	}

	@RequestMapping(value = "/completeProcess", method = RequestMethod.GET)
	ResponseMessage completeProcess(@RequestParam(value = "processId") String processId,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "origin", required = false) String origin) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.completeProcess(processId, userId, origin);
	}

	@RequestMapping(value = "/getTaskHistory", method = RequestMethod.GET)
	TaskListResponseDto getTaskHistory(@RequestParam(value = "locationCode") String locationCode,
			@RequestParam(value = "location", required = false) String location) {
		// LOGGER.info("Inside delivery data creation");
		return facadeLocal.getTaskHistory(locationCode, location);
	}

	@RequestMapping(value = "/getFLSOP", method = RequestMethod.GET)
	public FLSOPResponseDto getFLSOP(@RequestParam(value = "classification") String classification,
			@RequestParam(value = "subClassification") String subClassification) {
		return facadeLocal.getFLSOP(classification, subClassification);
	}

	// Enhancement to check single dispatch task per location
	@RequestMapping(value = "/checkForCreateTask", method = RequestMethod.GET)
	CheckForCreateTaskDto checkForExistingTask(@RequestParam(value = "locationCode") String locationCode) {
		return facadeLocal.checkForExistingTask(locationCode);
	}

	// AUTO CLOSE TASK CRETAED 14 DAYS AGO
	@RequestMapping(value = "/autoCloseTask", method = RequestMethod.GET)
	ResponseMessage autoCloseStatus() {
		return facadeLocal.autoCloseStatus();
	}

	// GET ALL TASKS FOR ADMIN
	@RequestMapping(value = "/getAllTasksForAdmin", method = RequestMethod.GET)
	TaskListResponseDto getTaskList(@RequestParam(value = "page") int page,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "taskType", required = false) String taskType,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "parentOrigin", required = false) String parentOrigin) {
		return facadeLocal.getAllTasksForAdmin(page, pageSize, taskType, status, parentOrigin);

	}

	// AutoClose Tasks For Admin
	@RequestMapping(value = "/autoCloseTaskForadmin", method = RequestMethod.POST)
	public ResponseMessage autoCloseTask(@RequestBody List<IopTaskListDto> taskId) {
		return facadeLocal.autoCloseTaskForAdmin(taskId);
	}
	
	//Get location(Well) details for IOP Admin
	@RequestMapping(value = "/getLocationMasterDetails", method = RequestMethod.GET)
	LocationHierarchyResponseDto getLocationMasterDetails(@RequestParam(value = "page") int page,
			@RequestParam(value = "page_size") int page_size ,
			@RequestParam(value="locationText")String locationText,
			@RequestParam(value="locationCode")String locationCode,
			@RequestParam(value="locationType")String locationType,
			@RequestParam(value="muwi")String muwi,
			@RequestParam(value="tier")String tier) {
		return facadeLocal.getLocationMasterDetails(page,page_size,locationText,locationCode,locationType,muwi,tier);
	}
	
	@RequestMapping(value = "/updateLocationMaster", method = RequestMethod.POST)
	ResponseMessage updateLocationMaster(@RequestBody List<LocationHierarchyDto> listLocationHierarchyDto) {
		return facadeLocal.updateLocationMaster(listLocationHierarchyDto);
	}

	@RequestMapping(value = "/taskActive", method = RequestMethod.GET)
	ResponseMessage getTaskStatus(@RequestParam(value = "userEmailId", required = false) String userEmailId,
			@RequestParam(value = "taskId", required = false) String taskId,
			@RequestParam(value = "processId", required = true) String processId,
			@RequestParam(value = "userGroup", required = true) String userGroup) {
		return facadeLocal.getTaskStatus(userEmailId, taskId, processId, userGroup);
	}
	
	@RequestMapping(value = "/getFieldWiseAvailability", method = RequestMethod.GET)
	public FieldAvailabilityResponseDto getFieldWiseAvailability(@RequestParam("technicalRole") String technicalRole) {
		return facadeLocal.getFieldWiseAvailability(technicalRole);
	}
	
	// For RETURN ALL functionality for all type of Dispatch Task
	@RequestMapping(value = "/updateAllReturnStatus", method = RequestMethod.POST)
	ResponseMessage returnAllStatus(@RequestBody List<UpdateRequestDto> dtoList) {
		return facadeLocal.returnAllStatus(dtoList, "");
	}
	
	@RequestMapping(value = "/getNextTask", method = RequestMethod.GET)
	TaskDetailResponseDto getNextTaskDetails(@RequestParam(value = "userId") String userId) {
		return facadeLocal.getNextTaskDetails(userId);
	}
	
	@RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
	public String sendMessageToRoc() {
			return "Inquiry Resolved";
	}
}
