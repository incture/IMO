package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: CheckListOptionsDo
 *
 */

@Entity
@Table(name = "TM_ND_MAPPING")
public class NDTaskMappingDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	public NDTaskMappingDo() {
		super();
	}
	private static final long serialVersionUID = -7341365853980611944L;	

	@Id
	@Column(name = "MAPPING_ID", length = 32)
	private String mappingId = UUID.randomUUID().toString().replaceAll("-", "");	

	@Column(name = "ND_TASK_ID", length = 32)
	private String ndTaskId;

	@Column(name = "TASK_ID", length = 32)
	private String taskId;

	@Override
	public String toString() {
		return "NDTaskMappinDo [mappingId=" + mappingId + ", ndTaskId=" + ndTaskId + ", taskId=" + taskId + ", status="
				+ status + "]";
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	public String getNdTaskId() {
		return ndTaskId;
	}

	public void setNdTaskId(String ndTaskId) {
		this.ndTaskId = ndTaskId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "STATUS", length = 20)
	private String status;

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return mappingId;
	}

	




}
