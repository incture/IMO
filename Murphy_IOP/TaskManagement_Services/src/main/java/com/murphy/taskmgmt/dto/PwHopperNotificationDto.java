package com.murphy.taskmgmt.dto;

import java.util.List;

public class PwHopperNotificationDto {

	private List<NotificationDto> pwHopperNotificationList ;
	private int hopperCount;
	
	public List<NotificationDto> getPwHopperNotificationList() {
		return pwHopperNotificationList;
	}
	public void setPwHopperNotificationList(List<NotificationDto> pwHopperNotificationList) {
		this.pwHopperNotificationList = pwHopperNotificationList;
	}
	
	public int getHopperCount() {
		return hopperCount;
	}
	public void setHopperCount(int hopperCount) {
		this.hopperCount = hopperCount;
	}
	@Override
	public String toString() {
		return "PwHopperNotificationDto [pwHopperNotificationList=" + pwHopperNotificationList + ", hopperCount="
				+ hopperCount + "]";
	}
	
	
}
