package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class EIShiftConfigDto extends BaseDto {
	private String id;
	private String shiftType;
	
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getShiftType() {
		return shiftType;
	}
	
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}

	@Override
	public String toString() {
		return "EIShiftConfigDto [id=" + id + ", shiftType=" + shiftType + "]";
	}
	
}
