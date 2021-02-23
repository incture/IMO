package com.murphy.taskmgmt.dto;

public class TaskSchedulingGraphDto {
	
	private String userName;
	private String taskSubject;
	private float timeInHrs ;
	private String priority;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTaskSubject() {
		return taskSubject;
	}
	public void setTaskSubject(String taskSubject) {
		this.taskSubject = taskSubject;
	}
	public float getTimeInHrs() {
		return timeInHrs;
	}
	public void setTimeInHrs(float timeInHrs) {
		this.timeInHrs = timeInHrs;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		return "TaskSchedulingGraphDto [userName=" + userName + ", taskSubject=" + taskSubject + ", timeInHrs="
				+ timeInHrs + ", priority=" + priority + "]";
	}
	
	
	
	
	
}
