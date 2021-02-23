package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: CheckListOptionsDo
 *
 */

@Entity
@Table(name = "TM_NON_DISPTCH")
public class NonDispatchTaskDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	public NonDispatchTaskDo() {
		super();
	}
	private static final long serialVersionUID = -7341365853980611944L;	

	@Id
	@Column(name = "TASK_ID", length = 32)
	private String taskId = UUID.randomUUID().toString().replaceAll("-", "");	

	@Column(name = "DESCRIPTION", length = 1000)
	private String description;

	@Column(name = "ND_LOC", length = 200)
	private String location;

	@Column(name = "STATUS", length = 20)
	private String status;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "COMPLETED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedAt;

	@Column(name = "COMPLETED_BY", length = 100)
	private String completedBy;
	
	@Column(name = "LOC_TYPE", length = 20)
	private String locType;
	
	@Column(name = "USER_GROUP", length = 100)
	private String group; 

	public String getLocType() {
		return locType;
	}



	public void setLocType(String locType) {
		this.locType = locType;
	}



	public Date getCompletedAt() {
		return completedAt;
	}



	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}



	public String getCompletedBy() {
		return completedBy;
	}



	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}



	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getLocation() {
		return location;
	}



	public void setLocation(String location) {
		this.location = location;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Date getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}



	public String getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	@Override
	public Object getPrimaryKey() {
		return taskId;
	}



	public String getGroup() {
		return group;
	}



	public void setGroup(String group) {
		this.group = group;
	}



	@Override
	public String toString() {
		return "NonDispatchTaskDo [taskId=" + taskId + ", description=" + description + ", location=" + location
				+ ", status=" + status + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", completedAt="
				+ completedAt + ", completedBy=" + completedBy + ", locType=" + locType + ", group=" + group + "]";
	}




}
