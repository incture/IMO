package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import com.murphy.taskmgmt.dto.AuditReportDto;
import com.murphy.taskmgmt.dto.NonDispatchResponseDto;
import com.murphy.taskmgmt.dto.NonDispatchTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskTypeResponseDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;

public interface NonDispatchFacadeLocal {

	ResponseMessage createTask(NonDispatchTaskDto dto);

	ResponseMessage deleteTask(UpdateRequestDto dto);

	NonDispatchResponseDto getAllTasks(String group,String location ,String locType);
	
	ResponseMessage updateTask(NonDispatchTaskDto dto);

	ResponseMessage completeTask(UpdateRequestDto dto);

	TaskTypeResponseDto getTaskTypes();

	List<AuditReportDto> getReport();
}
