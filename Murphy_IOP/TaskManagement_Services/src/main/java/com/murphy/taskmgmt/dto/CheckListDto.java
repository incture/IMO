package com.murphy.taskmgmt.dto;

import java.util.List;

public class CheckListDto {
	
	
	private List<CheckListItemDto> checkList;
	private String userType;
	
	
	public List<CheckListItemDto> getCheckList() {
		return checkList;
	}
	public void setCheckList(List<CheckListItemDto> checkList) {
		this.checkList = checkList;
	}
	
	@Override
	public String toString() {
		return "CheckListDto [checkList=" + checkList + ", userType=" + userType + "]";
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

	
	
}
