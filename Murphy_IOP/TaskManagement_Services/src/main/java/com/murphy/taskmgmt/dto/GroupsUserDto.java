package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class GroupsUserDto extends BaseDto {

	private String uniqueId;
	private String userId;
	private String userName;
	private String groupId;
	private String comment;
	private String firstName;
	private String lastName;
	private String pId;
/*
	// SOC added for OBX work division by competency
	private String obxDesignation;

	public String getObxDesignation() {
		return obxDesignation;
	}

	public void setObxDesignation(String obxDesignation) {
		this.obxDesignation = obxDesignation;
	}
	// EOC
*/
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "GroupsUserDto [uniqueId=" + uniqueId + ", userId=" + userId + ", userName=" + userName + ", groupId="
				+ groupId + ", comment=" + comment + ", firstName=" + firstName + ", lastName=" + lastName + ", pId="
				+ pId +"]";
				//+", obxDesignation="+ obxDesignation+ "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

}
