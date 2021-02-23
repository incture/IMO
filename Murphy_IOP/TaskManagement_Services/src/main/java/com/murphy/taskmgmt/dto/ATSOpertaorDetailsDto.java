package com.murphy.taskmgmt.dto;

/**
 * @author Rashmendra.Sai
 *
 */
import java.util.Date;
import java.util.List;

public class ATSOpertaorDetailsDto {

	private String emailID;
	private Double startTimeInMins;
	private String designation;
	private String taskOwnerName;
	private Date startTimeForNextTask;
	private String prevTaskLocCode;
	private String userFieldDesignation;
	private Date endTimeForTask;
	private List<String> routes;
	
	
	
	public List<String> getRoutes() {
		return routes;
	}
	public void setRoutes(List<String> routes) {
		this.routes = routes;
	}
	public Date getEndTimeForTask() {
		return endTimeForTask;
	}
	public void setEndTimeForTask(Date endTimeForTask) {
		this.endTimeForTask = endTimeForTask;
	}
	public String getUserFieldDesignation() {
		return userFieldDesignation;
	}
	public void setUserFieldDesignation(String userFieldDesignation) {
		this.userFieldDesignation = userFieldDesignation;
	}
	public String getEmailID() {
		return emailID;
	}
	public Double getStartTimeInMins() {
		return startTimeInMins;
	}
	public void setStartTimeInMins(Double startTimeInMins) {
		this.startTimeInMins = startTimeInMins;
	}
	
	public Date getStartTimeForNextTask() {
		return startTimeForNextTask;
	}
	public void setStartTimeForNextTask(Date startTimeForNextTask) {
		this.startTimeForNextTask = startTimeForNextTask;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getTaskOwnerName() {
		return taskOwnerName;
	}
	public void setTaskOwnerName(String taskOwnerName) {
		this.taskOwnerName = taskOwnerName;
	}
	public String getPrevTaskLocCode() {
		return prevTaskLocCode;
	}
	public void setPrevTaskLocCode(String prevTaskLocCode) {
		this.prevTaskLocCode = prevTaskLocCode;
	}
	@Override
	public String toString() {
		return "ATSOpertaorDetailsDto [emailID=" + emailID + ", startTimeInMins=" + startTimeInMins + ", designation="
				+ designation + ", taskOwnerName=" + taskOwnerName + ", startTimeForNextTask=" + startTimeForNextTask
				+ ", prevTaskLocCode=" + prevTaskLocCode + ", userFieldDesignation=" + userFieldDesignation
				+ ", endTimeForTask=" + endTimeForTask + "]";
	}
	
	
}
