package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class FlareDowntimeUpdateDto extends BaseDto{

	private FlareDowntimeDto dto;
	private Boolean isProCountUpdate;

	@Override
	public String toString() {
		return "FlareDowntimeUpdateDto [dto=" + dto + ", isProCountUpdate=" + isProCountUpdate + "]";
	}
	public FlareDowntimeDto getDto() {
		return dto;
	}
	public void setDto(FlareDowntimeDto dto) {
		this.dto = dto;
	}
	public Boolean getIsProCountUpdate() {
		return isProCountUpdate;
	}
	public void setIsProCountUpdate(Boolean isProCountUpdate) {
		this.isProCountUpdate = isProCountUpdate;
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
