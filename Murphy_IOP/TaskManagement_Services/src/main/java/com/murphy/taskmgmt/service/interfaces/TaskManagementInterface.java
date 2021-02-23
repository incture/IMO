package com.murphy.taskmgmt.service.interfaces;

/*
 * @author Prakash Kumar
 * Version 1.0
 * Since Sprint 6
 * */
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface TaskManagementInterface {

	public ResponseMessage updateTask(CustomTaskDto dto);
}
