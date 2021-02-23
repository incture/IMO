package com.murphy.taskmgmt.dto;

import java.util.List;

public class NearByTaskListDto {

	private List<NearbyTaskDto> nearByTasks;
	private ResponseMessage response;
	
	@Override
	public String toString() {
		return "NearByTaskListDto [nearByTasks=" + nearByTasks + ", response=" + response + "]";
	}
	public List<NearbyTaskDto> getNearByTasks() {
		return nearByTasks;
	}
	public void setNearByTasks(List<NearbyTaskDto> nearByTasks) {
		this.nearByTasks = nearByTasks;
	}
	public ResponseMessage getResponse() {
		return response;
	}
	public void setResponse(ResponseMessage response) {
		this.response = response;
	}
	
}
