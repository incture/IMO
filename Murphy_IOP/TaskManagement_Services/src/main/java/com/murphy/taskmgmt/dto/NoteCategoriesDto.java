package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class NoteCategoriesDto extends BaseDto{
	
	private String noteCategoryId;
	private String noteCategoryDescription;


	public String getNoteCategoryId() {
		return noteCategoryId;
	}

	public void setNoteCategoryId(String noteCategoryId) {
		this.noteCategoryId = noteCategoryId;
	}

	public String getNoteCategoryDescription() {
		return noteCategoryDescription;
	}

	public void setNoteCategoryDescription(String noteCategoryDescription) {
		this.noteCategoryDescription = noteCategoryDescription;
	}

	@Override
	public String toString() {
		return "NoteCategoriesDto [noteCategoryId=" + noteCategoryId + ", noteCategoryDescription="
				+ noteCategoryDescription + "]";
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
