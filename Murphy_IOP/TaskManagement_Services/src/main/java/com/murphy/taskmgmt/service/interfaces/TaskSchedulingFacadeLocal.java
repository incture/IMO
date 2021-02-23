package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.StartTimeResponseDto;
import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
import com.murphy.taskmgmt.dto.TaskSchedulingResponseDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;

public interface TaskSchedulingFacadeLocal {

	ResponseMessage updatePriority(UpdateRequestDto dto);

	String intialUpdateOfPriority();

	TaskSchedulingResponseDto getDataForScheduling(String userId, String orderBy, String role, int timeZoneOffSet, String fromDate,String toDate);

	TaskOwnersResponeDto getTaskOwnersbyId(String taskId, String role);

	StartTimeResponseDto getStartTimeForUser(String userId, String locationCode, String classification,
			String subClassification);

	ResponseMessage reassignInAdminConsole(CustomTaskDto dto);

	
}
