package com.murphy.geotab;

/**
 * @author INC00718
 *
 */
public class NearestUserDto {

	private String userId;
	private String firstName;
	private String lastName;
	private String emailId;
	private Double distanceFromLocation;
//	private Coordinates center;
	private Boolean isDriving;
	private Double unRoundedDistance;
	private String serialId;
	private int taskCount;
	private boolean taskAssignable;
	private String pId;
	private String blacklineId;
	private boolean isOnline;
	
	
	
	
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public String getBlacklineId() {
		return blacklineId;
	}
	public void setBlacklineId(String blacklineId) {
		this.blacklineId = blacklineId;
	}
	public Double getUnRoundedDistance() {
		return unRoundedDistance;
	}
	public void setUnRoundedDistance(Double unRoundedDistance) {
		this.unRoundedDistance = unRoundedDistance;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	/**
	 * @return the distanceFromLocation
	 */
	public Double getDistanceFromLocation() {
		return distanceFromLocation;
	}
	/**
	 * @param distanceFromLocation the distanceFromLocation to set
	 */
	public void setDistanceFromLocation(Double distanceFromLocation) {
		this.distanceFromLocation = distanceFromLocation;
	}
	
	public void setIsDriving(Boolean isDriving) {
		this.isDriving = isDriving;
	}
	
	public Boolean getIsDriving() {
		return this.isDriving;
	}
	
	
	
	@Override
	public String toString() {
		return "NearestUserDto [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId="
				+ emailId + ", distanceFromLocation=" + distanceFromLocation + ", isDriving=" + isDriving
				+ ", unRoundedDistance=" + unRoundedDistance + ", serialId=" + serialId + ", taskCount=" + taskCount
				+ ", taskAssignable=" + taskAssignable + ", pId=" + pId + ", blacklineId=" + blacklineId + ", isOnline="
				+ isOnline + "]";
	}
	public String getSerialId() {
		return serialId;
	}
	public void setSerialId(String serialId) {
		this.serialId = serialId;
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
	
}
