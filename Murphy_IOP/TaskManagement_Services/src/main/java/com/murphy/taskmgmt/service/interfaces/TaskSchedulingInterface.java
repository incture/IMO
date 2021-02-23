package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
/*
 * @author Prakash Kumar
 * Version 1.0
 * Since Sprint 6
 * */
public interface TaskSchedulingInterface {

	public ResponseMessage createTask(CustomTaskDto dto);
	
}
