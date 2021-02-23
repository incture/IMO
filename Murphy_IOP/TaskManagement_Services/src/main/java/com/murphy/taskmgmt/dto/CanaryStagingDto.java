package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class CanaryStagingDto extends BaseDto {

	private String stagingId;
	private String muwiId;
	private String parameterType;
	private Date createdAt;
	private Double dataValue;
	private String createdAtInString;

	public String getStagingId() {
		return stagingId;
	}

	public void setStagingId(String stagingId) {
		this.stagingId = stagingId;
	}

	public String getMuwiId() {
		return muwiId;
	}

	public void setMuwiId(String muwiId) {
		this.muwiId = muwiId;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Double getDataValue() {
		return dataValue;
	}

	public void setDataValue(Double dataValue) {
		this.dataValue = dataValue;
	}

	public String getCreatedAtInString() {
		return createdAtInString;
	}

	public void setCreatedAtInString(String createdAtInString) {
		this.createdAtInString = createdAtInString;
	}

	@Override
	public Boolean getValidForUsage() {
		return true;
	}
	
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		return;
	}

	@Override
	public String toString() {
		return "CanaryStagingDto [stagingId=" + stagingId + ", muwiId=" + muwiId + ", parameterType=" + parameterType
				+ ", createdAt=" + createdAt + ", dataValue=" + dataValue + ", createdAtInString=" + createdAtInString
				+ "]";
	}
}