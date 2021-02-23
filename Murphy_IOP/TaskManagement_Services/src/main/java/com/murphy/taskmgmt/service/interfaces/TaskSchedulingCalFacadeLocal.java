package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskSchedulingUpdateDto;

public interface TaskSchedulingCalFacadeLocal {

	ResponseMessage createTask(CustomTaskDto dto);

	ResponseMessage updateTaskDetailsOnShifting(TaskSchedulingUpdateDto dto);

}
