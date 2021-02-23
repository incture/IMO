package com.murphy.taskmgmt.dto;

import java.util.Date;

public class NDVTaskListDto {
	
	private String createdAtInString;
	private Long requestId;
	private Date createdAt;
	private String processId;
	private String taskId;
	private String wellName;
	private String field;
	private String varToFcstBOED;
	private String perVarToFcstBOED;
	private String mileStoneStep;
	private String status;
	

	public String getCreatedAtInString() {
		return createdAtInString;
	}
	public void setCreatedAtInString(String createdAtInString) {
		this.createdAtInString = createdAtInString;
	}
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getWellName() {
		return wellName;
	}
	public void setWellName(String wellName) {
		this.wellName = wellName;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getVarToFcstBOED() {
		return varToFcstBOED;
	}
	public void setVarToFcstBOED(String varToFcstBOED) {
		this.varToFcstBOED = varToFcstBOED;
	}
	public String getPerVarToFcstBOED() {
		return perVarToFcstBOED;
	}
	public void setPerVarToFcstBOED(String perVarToFcstBOED) {
		this.perVarToFcstBOED = perVarToFcstBOED;
	}
	public String getMileStoneStep() {
		return mileStoneStep;
	}
	public void setMileStoneStep(String mileStoneStep) {
		this.mileStoneStep = mileStoneStep;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "NDVTaskListDto [createdAtInString=" + createdAtInString + ", requestId=" + requestId + ", createdAt="
				+ createdAt + ", processId=" + processId + ", taskId=" + taskId + ", wellName=" + wellName + ", field="
				+ field + ", varToFcstBOED=" + varToFcstBOED + ", perVarToFcstBOED=" + perVarToFcstBOED
				+ ", mileStoneStep=" + mileStoneStep + ", status=" + status + "]";
	}

}
