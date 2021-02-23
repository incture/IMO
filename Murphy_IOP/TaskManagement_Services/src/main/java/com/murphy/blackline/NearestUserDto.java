package com.murphy.blackline;

public class NearestUserDto {

	private String userId;
	private String firstName;
	private String lastName;
	private String emailId;
	private Double distanceFromLocation;
//	private Coordinates center;
	private Boolean isOnline;
	@Override
	public String toString() {
		return "NearestUserDto [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId="
				+ emailId + ", distanceFromLocation=" + distanceFromLocation + ", isOnline=" + isOnline
				+ ", unRoundedDistance=" + unRoundedDistance + ", blacklineId=" + blacklineId + ", taskCount="
				+ taskCount + ", taskAssignable=" + taskAssignable + ", pId=" + pId + "]";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Double getDistanceFromLocation() {
		return distanceFromLocation;
	}
	public void setDistanceFromLocation(Double distanceFromLocation) {
		this.distanceFromLocation = distanceFromLocation;
	}
	public Boolean getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}
	public Double getUnRoundedDistance() {
		return unRoundedDistance;
	}
	public void setUnRoundedDistance(Double unRoundedDistance) {
		this.unRoundedDistance = unRoundedDistance;
	}
	public String getBlacklineId() {
		return blacklineId;
	}
	public void setBlacklineId(String blacklineId) {
		this.blacklineId = blacklineId;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	public boolean isTaskAssignable() {
		return taskAssignable;
	}
	public void setTaskAssignable(boolean taskAssignable) {
		this.taskAssignable = taskAssignable;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	private Double unRoundedDistance;
	private String blacklineId;
	private int taskCount;
	private boolean taskAssignable;
	private String pId;
}
