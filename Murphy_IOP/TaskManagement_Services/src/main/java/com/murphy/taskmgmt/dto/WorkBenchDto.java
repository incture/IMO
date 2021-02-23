/**
 * 
 */
package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

/**
 * @author Kamlesh.Choubey
 *
 */
public class WorkBenchDto extends BaseDto{
	
	private String processId; 
	private String taskId;
	private String taskLocationText;
	private String taskSub;
	private String taskType;
	private Date createdOn;
	private String tier;
	private String taskSource;
	private String taskLocCode;
	private Boolean hasInquiry;
	private Boolean hasDispatch;
	private Boolean hasInvestigation;
	private String createdBy;
	private String locationType;
	private String status;
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the taskLocationText
	 */
	public String getTaskLocationText() {
		return taskLocationText;
	}
	/**
	 * @param taskLocationText the taskLocationText to set
	 */
	public void setTaskLocationText(String taskLocationText) {
		this.taskLocationText = taskLocationText;
	}
	/**
	 * @return the taskSub
	 */
	public String getTaskSub() {
		return taskSub;
	}
	/**
	 * @param taskSub the taskSub to set
	 */
	public void setTaskSub(String taskSub) {
		this.taskSub = taskSub;
	}
	/**
	 * @return the taskType
	 */
	public String getTaskType() {
		return taskType;
	}
	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the tier
	 */
	public String getTier() {
		return tier;
	}
	/**
	 * @param tier the tier to set
	 */
	public void setTier(String tier) {
		this.tier = tier;
	}
	/**
	 * @return the taskSource
	 */
	public String getTaskSource() {
		return taskSource;
	}
	/**
	 * @param taskSource the taskSource to set
	 */
	public void setTaskSource(String taskSource) {
		this.taskSource = taskSource;
	}
	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return processId;
	}
	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	/**
	 * @return the taksId
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @param taksId the taksId to set
	 */
	public void setTaskId(String taksId) {
		this.taskId = taksId;
	}
	/**
	 * @return the taskLoc
	 */
	public String getTaskLocCode() {
		return taskLocCode;
	}
	/**
	 * @param taskLoc the taskLoc to set
	 */
	public void setTaskLocCode(String taskLocCode) {
		this.taskLocCode = taskLocCode;
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
	/**
	 * @return the hasInquiry
	 */
	public Boolean getHasInquiry() {
		return hasInquiry;
	}
	/**
	 * @param hasInquiry the hasInquiry to set
	 */
	public void setHasInquiry(Boolean hasInquiry) {
		this.hasInquiry = hasInquiry;
	}
	/**
	 * @return the hasDispatch
	 */
	public Boolean getHasDispatch() {
		return hasDispatch;
	}
	/**
	 * @param hasDispatch the hasDispatch to set
	 */
	public void setHasDispatch(Boolean hasDispatch) {
		this.hasDispatch = hasDispatch;
	}
	/**
	 * @return the hasInvestigation
	 */
	public Boolean getHasInvestigation() {
		return hasInvestigation;
	}
	/**
	 * @param hasInvestigation the hasInvestigation to set
	 */
	public void setHasInvestigation(Boolean hasInvestigation) {
		this.hasInvestigation = hasInvestigation;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the locationType
	 */
	public String getLocationType() {
		return locationType;
	}
	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	
	
	

	
}
