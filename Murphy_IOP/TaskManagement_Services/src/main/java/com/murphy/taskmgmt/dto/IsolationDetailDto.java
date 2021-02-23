package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

import com.murphy.taskmgmt.dto.BaseDto;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class IsolationDetailDto extends BaseDto {
	private String id; //
	private String formId;
	private String description; //IsolationDetails
	private boolean isEIStored;
	private boolean isEquipTested;
	private boolean isDeleted;
	private Date isolationDate;
	private Date reinstatement;
	List<EiAttachmentDto> attachmentList;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEIStored() {
		return isEIStored;
	}

	public void setEIStored(boolean isEIStored) {
		this.isEIStored = isEIStored;
	}

	public boolean isEquipTested() {
		return isEquipTested;
	}

	public void setEquipTested(boolean isEquipTested) {
		this.isEquipTested = isEquipTested;
	}

	public Date getIsolationDate() {
		return isolationDate;
	}

	public void setIsolationDate(Date isolationDate) {
		this.isolationDate = isolationDate;
	}

	public Date getReinstatement() {
		return reinstatement;
	}

	public void setReinstatement(Date reinstatement) {
		this.reinstatement = reinstatement;
	}

	public List<EiAttachmentDto> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<EiAttachmentDto> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}

}
