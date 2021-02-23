package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.dto.BaseDto;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class ActivityLogDto extends BaseDto {
	private String id;
	private String formId;
	private String permIssueName;
	private String permIssueLoginName;
	private String isApproved;	// True/False/Null values to UI.
	private Date permIssueTime;
	private String type;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getPermIssueName() {
		return permIssueName;
	}

	public void setPermIssueName(String permIssueName) {
		this.permIssueName = permIssueName;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public Date getPermIssueTime() {
		return permIssueTime;
	}

	public void setPermIssueTime(Date permIssueTime) {
		this.permIssueTime = permIssueTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPermIssueLoginName() {
		return permIssueLoginName;
	}

	public void setPermIssueLoginName(String permIssueLoginName) {
		this.permIssueLoginName = permIssueLoginName;
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}

}
