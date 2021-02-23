package com.murphy.taskmgmt.dto;

import java.util.List;

public class TaskOwnersResponeDto{
	
	private List<GroupsUserDto> userList;
	private List<TaskOwnersDto> ownerList;
	private ResponseMessage responseMessage;

	public List<TaskOwnersDto> getOwnerList() {
		return ownerList;
	}
	public void setOwnerList(List<TaskOwnersDto> ownerList) {
		this.ownerList = ownerList;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "TaskOwnersResponeDto [userList=" + userList + ", ownerList=" + ownerList + ", responseMessage="
				+ responseMessage + "]";
	}
	public List<GroupsUserDto> getUserList() {
		return userList;
	}
	public void setUserList(List<GroupsUserDto> userList) {
		this.userList = userList;
	}

	
}
