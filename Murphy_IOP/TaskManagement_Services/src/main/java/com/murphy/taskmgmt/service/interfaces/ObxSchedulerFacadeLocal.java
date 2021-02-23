package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import com.murphy.geotab.UserLocationResponse;

import com.murphy.taskmgmt.dto.ConfigResponseListDto;
import com.murphy.taskmgmt.dto.GetObxTaskReportResponse;
import com.murphy.taskmgmt.dto.NearByTaskListDto;
import com.murphy.taskmgmt.dto.ObxAllocationResponseDto;
import com.murphy.taskmgmt.dto.ObxAllocationUpdateDto;
import com.murphy.taskmgmt.dto.ObxConfigValuesDto;
import com.murphy.taskmgmt.dto.ObxTaskAllocationDto;
import com.murphy.taskmgmt.dto.ObxTaskDto;
import com.murphy.taskmgmt.dto.ObxTaskUpdateDto;
import com.murphy.taskmgmt.dto.ObxWorkLoadDetailsResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
import com.murphy.taskmgmt.dto.WellVisitMatrixDto;

public interface ObxSchedulerFacadeLocal {

	String updateWellVisitMatrix(String locationCode, int day);

	String updateWellVisitMatrix(List<String> locationCodes, int day);

	void updateSequence(int day);

	void assignUsersToCluster(int dayOfWeek);

	void issueObxTasks();

	ResponseMessage createOBXTask(String userId, String location, Double driveTimeOBX);

	NearByTaskListDto getNearbyAssignedTask(double latitude, double longitude, String userId);

	UserLocationResponse getUserCurrentLocation(String userId);

	ResponseMessage updateOBXTask(ObxTaskUpdateDto dto);

	TaskOwnersResponeDto getOBXUsers(String roles);

	ResponseMessage updateUserSequence(int day, String userEmail);

	void generateWellVisitMatrix_Field();

	void CreateTaskAllocationField();

	void createClusters(int dayOfWeek, List<String> field, int numberOfClusters, String obxRole);

	void updateSequence(int day, int numberOfClusters, List<String> field);

	ResponseMessage runObxEngineTask(ObxConfigValuesDto dto);

	List<ConfigResponseListDto> getUpdatedConfig();

	ObxAllocationResponseDto getObxTaskAllocationDetails(String roles, String field, String selectedDay);

	ObxWorkLoadDetailsResponseDto getObxOperatorWorkDetails(String field, String selectedDay);

	ResponseMessage updateTaskForOperator(ObxAllocationUpdateDto dto);

	ResponseMessage getRunningObxFlag();

	void generateWellVisitMatrix();

	void updateConfig();

//	ResponseMessage assignClusterToUser(String day, String field, String clusterNumber, String userId);

	void revokeObxTasks();

//	void reAssignWells(List<String> field);
	
	void revokedTaskAllocation_Field();

	GetObxTaskReportResponse getObxTaskReport(String date);
	
	List<ObxTaskDto> getAllOBXAllo();

	void setDriveTime(List<ObxTaskDto> list);

	
	
}
