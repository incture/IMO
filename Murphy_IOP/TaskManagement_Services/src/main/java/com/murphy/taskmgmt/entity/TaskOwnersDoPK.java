package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TaskOwnersDoPK implements BaseDo, Serializable {

	/**
	 * 
	 */
	public TaskOwnersDoPK() {
		super();
	}

	private static final long serialVersionUID = -7858573206799766633L;

	@Column(name = "TASK_ID" , length = 32, nullable = false)
	private String taskId;

	@Column(name = "TASK_OWNER", length = 255, nullable = false)
	private String taskOwner;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		result = prime * result + ((taskOwner == null) ? 0 : taskOwner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskOwnersDoPK other = (TaskOwnersDoPK) obj;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		if (taskOwner == null) {
			if (other.taskOwner != null)
				return false;
		} else if (!taskOwner.equals(other.taskOwner))
			return false;
		return true;
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}

}
