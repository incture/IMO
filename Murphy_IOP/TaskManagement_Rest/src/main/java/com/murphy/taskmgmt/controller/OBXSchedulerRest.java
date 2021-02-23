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

import com.murphy.geotab.UserLocationResponse;
import com.murphy.taskmgmt.dto.ConfigResponseListDto;
import com.murphy.taskmgmt.dto.GetObxTaskReportResponse;
import com.murphy.taskmgmt.dto.NearByTaskListDto;
import com.murphy.taskmgmt.dto.ObxAllocationResponseDto;
import com.murphy.taskmgmt.dto.ObxAllocationUpdateDto;
import com.murphy.taskmgmt.dto.ObxConfigValuesDto;
import com.murphy.taskmgmt.dto.ObxTaskUpdateDto;
import com.murphy.taskmgmt.dto.ObxWorkLoadDetailsResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
import com.murphy.taskmgmt.service.interfaces.ObxSchedulerFacadeLocal;
import com.murphy.taskmgmt.util.ArcGISUtil;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/OBXScheduler", produces = "application/json")
public class OBXSchedulerRest {

	@Autowired
	ObxSchedulerFacadeLocal obx;
	
	@Autowired
	ArcGISUtil arc;
	
	@RequestMapping(value="/generateWellVisitMatrix_Field", method = RequestMethod.GET)
	public void generateWellVisitMatrix_Field(){
		System.err.println("Entering obx.generateWellVisitMatrix()");
		obx.generateWellVisitMatrix_Field();
	}
	
	@RequestMapping(value="/CreateOBXTask", method = RequestMethod.GET)
	public ResponseMessage CreateOBXTask(@RequestParam(value="userId" , required=true)String userId,
			@RequestParam(value="location" , required=true)String location){
		System.err.println("Entering obx.CreateOBXTask()");
		return obx.createOBXTask(userId, location,null);
	}
	
	@RequestMapping(value = "/nearbyAssignedTask", method = RequestMethod.GET)
	public NearByTaskListDto getNearbyAssignedTask(@RequestParam(value = "latitude", required = false) double latitude,
			@RequestParam(value = "longitude", required = false) double longitude,
			@RequestParam(value = "userId", required = true) String userId) {
		return obx.getNearbyAssignedTask(latitude, longitude,userId);
	}
	
	@RequestMapping(value = "/getUserLocation", method = RequestMethod.GET)
	public UserLocationResponse getUserCurrentLocation(@RequestParam(value = "userId", required = true) String userId) {
		return obx.getUserCurrentLocation(userId);
	}
	
	@RequestMapping(value = "/getOBXUsers", method = RequestMethod.GET)
	public TaskOwnersResponeDto getUsers(@RequestParam(value = "roles", required = true) String roles) {
		return obx.getOBXUsers(roles);
	}
	
	@RequestMapping(value = "/updateOBXTask", method = RequestMethod.POST)
	ResponseMessage updateTask(@RequestBody ObxTaskUpdateDto dto) {
		return obx.updateOBXTask(dto);
	}
	
	@RequestMapping(value="/assignUsersToCluster",method = RequestMethod.GET)
	public void assignUsersToCluster(@RequestParam(value = "day", required = true) int day){
		obx.assignUsersToCluster(day);
	}
	
	
	@RequestMapping(value="/issueObxTasks",method = RequestMethod.GET)
	public void issueObxTasks(){
		obx.issueObxTasks();
	}

	//Running the OBX Engine for updation of Config Values and running the schedulers
	@RequestMapping(value = "/runObxEngineTask", method = RequestMethod.POST)
	public ResponseMessage runObxEngine(@RequestBody ObxConfigValuesDto obxDto) {
		return obx.runObxEngineTask(obxDto);
	}
	
	//Fetching of the updated configuration values 
	@RequestMapping(value = "/getConfigDetails", method = RequestMethod.GET)
	public List<ConfigResponseListDto> getUpdatedConfigValue() {
		return obx.getUpdatedConfig();
	}
	
	//Fetching of the OBX Task allocated details based on roles 
	@RequestMapping(value = "/getObxTaskAllocated", method = RequestMethod.GET)
	public ObxAllocationResponseDto getObxTaskAllocationDetails(@RequestParam(value = "roles", required = true) String roles, @RequestParam(value = "field", required = true) String field
			,@RequestParam(value = "selectedDay", required = true) String selectedDay) {
		return obx.getObxTaskAllocationDetails(roles, field, selectedDay);
	}
	
	//Fetching workload for the users
	@RequestMapping(value = "/getObxWorkload", method = RequestMethod.GET)
	public ObxWorkLoadDetailsResponseDto getObxWorkloadDetails(@RequestParam(value = "field", required = true) String field, @RequestParam(value = "selectedDay", required = true) String selectedDay) {
		return obx.getObxOperatorWorkDetails(field, selectedDay);
	}
	
	//Updating the DB and Fetching the updated workload details for the operators.
	@RequestMapping(value = "/updateTaskForOperator", method = RequestMethod.POST)
	public ResponseMessage updatedTaskForOperator(@RequestBody ObxAllocationUpdateDto dto) {
		return obx.updateTaskForOperator(dto);
	}
	
	//Generating task allocation in OBX Task Allocation table
	@RequestMapping(value="/generateTaskAllocationField",method = RequestMethod.GET)
	public void CreateTaskAllocation_Field(){
		obx.CreateTaskAllocationField();
	}
	
	//Checking flag if obx engine running or not for the excel download feature.
	@RequestMapping(value="/checkObxRunningFlag",method = RequestMethod.GET)
	public ResponseMessage obxRunFlag(){
		return obx.getRunningObxFlag();
	}
	
	@RequestMapping(value="/mailForOBX", method = RequestMethod.GET)
	public void mailForOBX(){
		
	arc.mailDriveTimeOutliers();
	}
	
	/*//Updating the uncompleted task as revoked 
	@RequestMapping(value="/revokeTasks",method = RequestMethod.GET)
	public void revokeObxTasks(){
		obx.revokeObxTasks();
	}
	
	//For reallocating revoked task
	@RequestMapping(value="/generateRevokedTaskAllocationField",method = RequestMethod.GET)
	public void createRevokedTaskAllocation_Field(){
		obx.revokedTaskAllocation_Field();
	}
	
	//For generating OBX Report 	
	@RequestMapping(value = "/getObxTaskReport", method = RequestMethod.GET)
	public GetObxTaskReportResponse getObxTaskReport(@RequestParam(value = "date", required = true) String date) {
		return obx.getObxTaskReport(date);
	}*/
		
}
