package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.reports.PMCReportBaseDto;

public class IOPAdminReportResponseDto extends PMCReportBaseDto {
	TaskListResponseDto taskListResponseDto;

	public TaskListResponseDto getTaskListResponseDto() {
		return taskListResponseDto;
	}

	public void setTaskListResponseDto(TaskListResponseDto taskListResponseDto) {
		this.taskListResponseDto = taskListResponseDto;
	}
	

}
