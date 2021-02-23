package com.murphy.taskmgmt.dto;

import java.util.List;

public class TaskNotificationDto {

	private List<NotificationDto> taskNotificationList;
	private int taskCount;
	
	
	public List<NotificationDto> getTaskNotificationList() {
		return taskNotificationList;
	}
	public void setTaskNotificationList(List<NotificationDto> taskNotificationList) {
		this.taskNotificationList = taskNotificationList;
	}
    
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	@Override
	public String toString() {
		return "TaskNotificationDto [taskNotificationList=" + taskNotificationList + ", taskCount=" + taskCount + "]";
	}
	
	
}
