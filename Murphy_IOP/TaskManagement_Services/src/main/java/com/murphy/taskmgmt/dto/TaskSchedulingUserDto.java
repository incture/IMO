package com.murphy.taskmgmt.dto;

import java.util.List;

public class TaskSchedulingUserDto {


	private String taskOwner;
	private String name;
	private float totalWorkTime;
	private float availableTime;
	private String shift;
	private List<TaskSchedulingDto> appointments;
	private String pId;
	
	
	
	
	
	public String getpId() {
		return pId;
	}




	public void setpId(String pId) {
		this.pId = pId;
	}




	public float getAvailableTime() {
		return availableTime;
	}




	public void setAvailableTime(float availableTime) {
		this.availableTime = availableTime;
	}




	public String getShift() {
		return shift;
	}




	public void setShift(String shift) {
		this.shift = shift;
	}




	public String getTaskOwner() {
		return taskOwner;
	}




	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public List<TaskSchedulingDto> getAppointments() {
		return appointments;
	}




	public void setAppointments(List<TaskSchedulingDto> appointments) {
		this.appointments = appointments;
	}




	@Override
	public String toString() {
		return "TaskSchedulingUserDto [taskOwner=" + taskOwner + ", name=" + name + ", appointments=" + appointments
				+ "]";
	}




	public float getTotalWorkTime() {
		return totalWorkTime;
	}




	public void setTotalWorkTime(float totalWorkTime) {
		this.totalWorkTime = totalWorkTime;
	}



}
