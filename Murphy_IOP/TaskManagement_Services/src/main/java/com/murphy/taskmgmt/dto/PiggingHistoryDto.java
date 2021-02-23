package com.murphy.taskmgmt.dto;

import java.io.Serializable;
import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class PiggingHistoryDto  extends BaseDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String equipmentId;


	private Date lastCompletedOn;


	private Date dueDate;


	public String getEquipmentId() {
		return equipmentId;
	}


	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}


	public Date getLastCompletedOn() {
		return lastCompletedOn;
	}


	public void setLastCompletedOn(Date lastCompletedOn) {
		this.lastCompletedOn = lastCompletedOn;
	}


	public Date getDueDate() {
		return dueDate;
	}


	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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
