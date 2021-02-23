package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class FracNotificationDto extends BaseDto {

	private String serialId;
	private long fracId;
	private String muwi;
	private Date acknowledgedAt;
	private String isAcknowledged;
	private String userGroup;
	private String userId;
//	private String isDispatched;
	private double maxTubePressure;
	private double maxCasePressure;
	private double activeTubePressure;
	private double activeCasePressure;
	private String wellStatus;
	private int fracCount;
	private List<FracAlertMessageDto> fracNotificationList;



	public long getFracId() {
		return fracId;
	}

	public void setFracId(long fracId) {
		this.fracId = fracId;
	}

	public String getMuwi() {
		return muwi;
	}

	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}

	public Date getAcknowledgedAt() {
		return acknowledgedAt;
	}

	public void setAcknowledgedAt(Date acknowledgedAt) {
		this.acknowledgedAt = acknowledgedAt;
	}

	public String getIsAcknowledged() {
		return isAcknowledged;
	}

	public void setIsAcknowledged(String isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

//	public String getIsDispatched() {
//		return isDispatched;
//	}
//
//	public void setIsDispatched(String isDispatched) {
//		this.isDispatched = isDispatched;
//	}

	public double getMaxTubePressure() {
		return maxTubePressure;
	}

	public void setMaxTubePressure(double maxTubePressure) {
		this.maxTubePressure = maxTubePressure;
	}

	public double getMaxCasePressure() {
		return maxCasePressure;
	}

	public void setMaxCasePressure(double maxCasePressure) {
		this.maxCasePressure = maxCasePressure;
	}

	public List<FracAlertMessageDto> getFracNotificationList() {
		return fracNotificationList;
	}

	public void setFracNotificationList(List<FracAlertMessageDto> fracNotificationList) {
		this.fracNotificationList = fracNotificationList;
	}
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}
	
	

	public String getWellStatus() {
		return wellStatus;
	}

	public void setWellStatus(String wellStatus) {
		this.wellStatus = wellStatus;
	}

	


	public double getActiveTubePressure() {
		return activeTubePressure;
	}

	public void setActiveTubePressure(double activeTubePressure) {
		this.activeTubePressure = activeTubePressure;
	}

	public double getActiveCasePressure() {
		return activeCasePressure;
	}

	public void setActiveCasePressure(double activeCasePressure) {
		this.activeCasePressure = activeCasePressure;
	}

	public int getFracCount() {
		return fracCount;
	}

	public void setFracCount(int fracCount) {
		this.fracCount = fracCount;
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

	@Override
	public String toString() {
		return "FracNotificationDto [serialId=" + serialId + ", fracId=" + fracId + ", muwi=" + muwi
				+ ", acknowledgedAt=" + acknowledgedAt + ", isAcknowledged=" + isAcknowledged + ", userGroup="
				+ userGroup + ", userId=" + userId + ", maxTubePressure=" + maxTubePressure + ", maxCasePressure="
				+ maxCasePressure + ", activeTubePressure=" + activeTubePressure + ", activeCasePressure="
				+ activeCasePressure + ", wellStatus=" + wellStatus + ", fracCount=" + fracCount
				+ ", fracNotificationList=" + fracNotificationList + "]";
	}
	

	
	
	
	
	
}
