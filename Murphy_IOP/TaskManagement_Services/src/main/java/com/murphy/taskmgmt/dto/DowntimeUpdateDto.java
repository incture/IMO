package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class DowntimeUpdateDto extends BaseDto{
	
private DowntimeCapturedDto dto;
private Boolean isProCountUpdate;
private String countryCode;

@Override
public String toString() {
	return "DowntimeUpdateDto [dto=" + dto + ", isProCountUpdate=" + isProCountUpdate + ", countryCode=" + countryCode
			+ "]";
}
public DowntimeCapturedDto getDto() {
	return dto;
}
public void setDto(DowntimeCapturedDto dto) {
	this.dto = dto;
}
public Boolean getIsProCountUpdate() {
	return isProCountUpdate;
}
public void setIsProCountUpdate(Boolean isProCountUpdate) {
	this.isProCountUpdate = isProCountUpdate;
}


public String getCountryCode() {
	return countryCode;
}
public void setCountryCode(String countryCode) {
	this.countryCode = countryCode;
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
