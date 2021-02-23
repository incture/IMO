package com.murphy.taskmgmt.dto;

import java.util.Date;

public class DopDummyDto {

	private String muwi;
	private String locationCode;
	private String tier;
	private Double dataValue;
	private Date createdAt;
	private String paramType;
	ResponseMessage responseMessage;
	
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public String getMuwi() {
		return muwi;
	}
	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	public Double getDataValue() {
		return dataValue;
	}
	public void setDataValue(Double dataValue) {
		this.dataValue = dataValue;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	
	
	
}
