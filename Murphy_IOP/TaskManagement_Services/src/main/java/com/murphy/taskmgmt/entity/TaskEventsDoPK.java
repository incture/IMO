package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TaskEventsDoPK implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -363219593432656662L;

	public TaskEventsDoPK() {
		super();
	}

	@Column(name = "TASK_ID", length = 32, nullable = false)
	private String taskId;

	@Column(name = "PROCESS_ID", length = 32, nullable = false)
	private String processId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		result = prime * result + ((processId == null) ? 0 : processId.hashCode());
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
		TaskEventsDoPK other = (TaskEventsDoPK) obj;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		return true;
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}

	@Override
	public String toString() {
		return "TaskEventsDoPK [taskId=" + taskId + ", processId=" + processId + "]";
	}

}
