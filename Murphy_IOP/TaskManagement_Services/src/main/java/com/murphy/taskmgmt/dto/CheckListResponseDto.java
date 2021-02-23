package com.murphy.taskmgmt.dto;

import java.util.List;

public class CheckListResponseDto {
	
	private String investigationId;
	private String hopperId;
	private String locationCode;
	private String muwiId;
	private String userType;
	private String loggedInUser;
	private boolean isProactive;
	private List<CheckListDto> checkList;
	private ResponseMessage responseMessage;
	
	public String getInvestigationId() {
		return investigationId;
	}
	public void setInvestigationId(String investigationId) {
		this.investigationId = investigationId;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getMuwiId() {
		return muwiId;
	}
	public void setMuwiId(String muwiId) {
		this.muwiId = muwiId;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public List<CheckListDto> getCheckList() {
		return checkList;
	}
	public void setCheckList(List<CheckListDto> checkList) {
		this.checkList = checkList;
	}
	
	@Override
	public String toString() {
		return "CheckListResponseDto [investigationId=" + investigationId + ", locationCode=" + locationCode
				+ ", muwiId=" + muwiId + ", checkList=" + checkList + ", responseMessage=" + responseMessage + "]";
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public boolean getIsProactive() {
		return isProactive;
	}
	public void setIsProactive(boolean isProactive) {
		this.isProactive = isProactive;
	}
	public String getHopperId() {
		return hopperId;
	}
	public void setHopperId(String hopperId) {
		this.hopperId = hopperId;
	}
	public String getLoggedInUser() {
		return loggedInUser;
	}
	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	
	
}
