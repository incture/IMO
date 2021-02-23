package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class ShiftAuditLogDto extends BaseDto{

	private String shiftAuditId;
	private String resource;
	private String modifiedBy;
	private Date modifiedAt;
	private String currentShift;
	private String previousShift;
	private String currentBaseLoc;
	private String prevBaseLoc;
	private String currentBaseLocCode;
	private String shiftDay;
	
	
	public String getShiftAuditId() {
		return shiftAuditId;
	}
	public void setShiftAuditId(String shiftAuditId) {
		this.shiftAuditId = shiftAuditId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	public String getCurrentShift() {
		return currentShift;
	}
	public void setCurrentShift(String currentShift) {
		this.currentShift = currentShift;
	}
	public String getPreviousShift() {
		return previousShift;
	}
	public void setPreviousShift(String previousShift) {
		this.previousShift = previousShift;
	}
	public String getCurrentBaseLoc() {
		return currentBaseLoc;
	}
	public void setCurrentBaseLoc(String currentBaseLoc) {
		this.currentBaseLoc = currentBaseLoc;
	}
	public String getPrevBaseLoc() {
		return prevBaseLoc;
	}
	public void setPrevBaseLoc(String prevBaseLoc) {
		this.prevBaseLoc = prevBaseLoc;
	}
	
	public String getCurrentBaseLocCode() {
		return currentBaseLocCode;
	}
	public void setCurrentBaseLocCode(String currentBaseLocCode) {
		this.currentBaseLocCode = currentBaseLocCode;
	}
	
	
	public String getShiftDay() {
		return shiftDay;
	}
	public void setShiftDay(String shiftDay) {
		this.shiftDay = shiftDay;
	}
	@Override
	public String toString() {
		return "ShiftAuditLogDto [shiftAuditId=" + shiftAuditId + ", resource=" + resource + ", modifiedBy="
				+ modifiedBy + ", modifiedAt=" + modifiedAt + ", currentShift=" + currentShift + ", previousShift="
				+ previousShift + ", currentBaseLoc=" + currentBaseLoc + ", prevBaseLoc=" + prevBaseLoc
				+ ", currentBaseLocCode=" + currentBaseLocCode + ", shiftDay=" + shiftDay + "]";
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
