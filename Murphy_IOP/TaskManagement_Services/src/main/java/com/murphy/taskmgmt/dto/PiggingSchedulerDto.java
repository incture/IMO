package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class PiggingSchedulerDto extends BaseDto {

	private String workOrderNo;
	private String equipmentId;
	private String functionalLoc;
	private String flag;
	private String taskID;
	private Date time;
	private String pigLaunchStatus;
	private String pigRetrievalStatus;
	private String workOrderStatus;
	
	
	
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getFunctionalLoc() {
		return functionalLoc;
	}
	public void setFunctionalLoc(String functionalLoc) {
		this.functionalLoc = functionalLoc;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	
	public String getPigLaunchStatus() {
		return pigLaunchStatus;
	}
	public void setPigLaunchStatus(String pigLaunchStatus) {
		this.pigLaunchStatus = pigLaunchStatus;
	}
	public String getPigRetrievalStatus() {
		return pigRetrievalStatus;
	}
	public void setPigRetrievalStatus(String pigRetrievalStatus) {
		this.pigRetrievalStatus = pigRetrievalStatus;
	}
	public String getWorkOrderStatus() {
		return workOrderStatus;
	}
	public void setWorkOrderStatus(String workOrderStatus) {
		this.workOrderStatus = workOrderStatus;
	}
	@Override
	public String toString() {
		return "PiggingSchedulerDto [workOrderNo=" + workOrderNo + ", equipmentId=" + equipmentId + ", functionalLoc="
				+ functionalLoc + ", flag=" + flag + ", taskID=" + taskID + ", time=" + time + ", pigLaunchStatus="
				+ pigLaunchStatus + ", pigRetrievalStatus=" + pigRetrievalStatus + ", workOrderStatus="
				+ workOrderStatus + "]";
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
