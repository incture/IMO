package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class AlsInvestigationDto extends BaseDto{
	
	private String alsId;
	private String muwId;
	private Date date;
	private String reason;
	private String source;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAlsId() {
		return alsId;
	}
	public void setAlsId(String alsId) {
		this.alsId = alsId;
	}
	public String getMuwId() {
		return muwId;
	}
	public void setMuwId(String muwId) {
		this.muwId = muwId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Override
	public String toString() {
		return "AlsInvestigationDto [alsId=" + alsId + ", muwId=" + muwId + ", date=" + date + ", reason=" + reason
				+ ", source=" + source + "]";
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
