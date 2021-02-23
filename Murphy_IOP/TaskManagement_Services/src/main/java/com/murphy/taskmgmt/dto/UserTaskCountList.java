package com.murphy.taskmgmt.dto;

import java.util.List;

public class UserTaskCountList {

	private List<UserTaskCount> userTaskCountList;
	private ResponseMessage responseMessage;


	public List<UserTaskCount> getUserTaskCountList() {
		return userTaskCountList;
	}

	public void setUserTaskCountList(List<UserTaskCount> userTaskCountList) {
		this.userTaskCountList = userTaskCountList;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "UserTaskCountList [userTaskCountList=" + userTaskCountList + ", responseMessage=" + responseMessage
				+ "]";
	}

	
}
