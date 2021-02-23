package com.murphy.taskmgmt.service.interfaces;

import java.util.List;
import java.util.Map;

import com.murphy.taskmgmt.dto.AttrTempResponseDto;
import com.murphy.taskmgmt.dto.CheckForCreateTaskDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.FLSOPResponseDto;
import com.murphy.taskmgmt.dto.FieldOperatorAvailabilityDto;
import com.murphy.taskmgmt.dto.FieldAvailabilityResponseDto;
import com.murphy.taskmgmt.dto.IopTaskListDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.NDVTaskListResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskDetailResponseDto;
import com.murphy.taskmgmt.dto.TaskListResponseDto;
import com.murphy.taskmgmt.dto.UIResponseDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.dto.UserTaskCountList;

public interface TaskManagementFacadeLocal {

	ResponseMessage changeStatus(UpdateRequestDto dto, String descritpion);

	ResponseMessage updateTask(CustomTaskDto dto);

	NDVTaskListResponseDto getNDVTaskList(String userRole, String userType, String loggedInUserType);

	TaskListResponseDto getTaskList(String userId, String userType, String group, String taskType, String origin,
			String locationCode, Boolean isCreatedByMe,String device, String tier, Boolean isForToday, 
			String classification, String subClassification,String country);

	UIResponseDto assignedTasksToUserCount(String userId);

	TaskListResponseDto getTaskHistory(String locationCode, String location);

	FLSOPResponseDto getFLSOP(String classification, String subClassification);

	ResponseMessage completeProcess(String processId, String userId, String origin);

	CustomTaskDto getTaskDetails(String taskId, String userType, String loggedInUser, String loggedInUserGrp,
			String hopperuserType);

	UserTaskCountList assignedTasksToUsersCount(String pIdList);

	AttrTempResponseDto getCustomHeaders(String taskTempId, String location, String locationType, String classification,
			String locationCode, String compressorName, String userType, int duration);

	CheckForCreateTaskDto checkForExistingTask(String locationCode);

	TaskListResponseDto getAllTasksForAdmin(int page, int pageSize, String taskType, String status, String parentOrigin);

	ResponseMessage autoCloseTaskForAdmin(List<IopTaskListDto> taskId);

	ResponseMessage getTaskStatus(String userEmailId, String taskId, String processId, String userGroup);

	ResponseMessage autoCloseStatus();
	public FieldAvailabilityResponseDto getFieldWiseAvailability(String technicalRole);

	ResponseMessage returnAllStatus(List<UpdateRequestDto> dtoList, String string);
	
	LocationHierarchyResponseDto getLocationMasterDetails(int page, int page_size,String locationText,String locationCode,String locationType,String muwi,String tier);
	
	TaskDetailResponseDto getNextTaskDetails(String userId);
	
	ResponseMessage updateLocationMaster(List<LocationHierarchyDto> listLocationHierarchyDto);

}
