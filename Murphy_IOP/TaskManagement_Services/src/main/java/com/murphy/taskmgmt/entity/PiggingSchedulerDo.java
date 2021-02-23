package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "PIGGING_SCHEDULER")
public class PiggingSchedulerDo implements BaseDo, Serializable {

	@Id
	@Column(name = "WORKORDER_NO", length = 32)
	private String workOrderNo;
	
	@Column(name = "EQUIPMENT_ID", length = 50)
	private String equipmentId;
	
	@Column(name = "FUNCTIONAL_LOC", length = 80)
	private String functionalLoc;
	
	@Column(name = "FLAG", length = 32)
	private String flag;
	
	@Column(name = "TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@Column(name = "PIG_LAUNCH_STATUS", length = 30)
	private String pigLaunchStatus;
	
	@Column(name = "PIG_RETRIEVAL_STATUS", length = 30)
	private String pigRetrievalStatus;
	
	@Column(name = "WORKORDER_STATUS", length = 50)
	private String workOrderStatus;
	
	
	
	
	

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


	public Date getTime() {
		return time;
	}



	public void setTime(Date time) {
		this.time = time;
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
		return "PiggingSchedulerDo [workOrderNo=" + workOrderNo + ", equipmentId=" + equipmentId + ", functionalLoc="
				+ functionalLoc + ", flag=" + flag + ", time=" + time + ", pigLaunchStatus=" + pigLaunchStatus
				+ ", pigRetrievalStatus=" + pigRetrievalStatus + ", workOrderStatus=" + workOrderStatus + "]";
	}


	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
