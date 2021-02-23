package com.murphy.taskmgmt.dto;

public class ObxAllocationUpdateDto {
	private String taskOwnerEmail;
	private String updatedByEmail;
	private String locationCode;
	private String selectedDay;
	private String isObxUser;
	/**
	 * @return the taskOwnerEmail
	 */
	public String getTaskOwnerEmail() {
		return taskOwnerEmail;
	}
	/**
	 * @param taskOwnerEmail the taskOwnerEmail to set
	 */
	public void setTaskOwnerEmail(String taskOwnerEmail) {
		this.taskOwnerEmail = taskOwnerEmail;
	}
	/**
	 * @return the updatedByEmail
	 */
	public String getUpdatedByEmail() {
		return updatedByEmail;
	}
	/**
	 * @param updatedByEmail the updatedByEmail to set
	 */
	public void setUpdatedByEmail(String updatedByEmail) {
		this.updatedByEmail = updatedByEmail;
	}
	/**
	 * @return the locationCode
	 */
	public String getLocationCode() {
		return locationCode;
	}
	/**
	 * @param locationCode the locationCode to set
	 */
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	/**
	 * @return the selectedDay
	 */
	public String getSelectedDay() {
		return selectedDay;
	}
	/**
	 * @param selectedDay the selectedDay to set
	 */
	public void setSelectedDay(String selectedDay) {
		this.selectedDay = selectedDay;
	}
	/**
	 * @return the isObxUser
	 */
	public String getIsObxUser() {
		return isObxUser;
	}
	/**
	 * @param isObxUser the isObxUser to set
	 */
	public void setIsObxUser(String isObxUser) {
		this.isObxUser = isObxUser;
	}
	
}
