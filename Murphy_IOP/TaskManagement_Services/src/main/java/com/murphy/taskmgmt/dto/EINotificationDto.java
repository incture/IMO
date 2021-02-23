package com.murphy.taskmgmt.dto;

import java.util.List;

public class EINotificationDto {

	private List<NotificationDto> energyIsolationNotificationList;
	private int energyIsolationCount;
	
	
	
	public List<NotificationDto> getEnergyIsolationNotificationList() {
		return energyIsolationNotificationList;
	}
	public void setEnergyIsolationNotificationList(List<NotificationDto> energyIsolationNotificationList) {
		this.energyIsolationNotificationList = energyIsolationNotificationList;
	}
	public int getEnergyIsolationCount() {
		return energyIsolationCount;
	}
	public void setEnergyIsolationCount(int energyIsolationCount) {
		this.energyIsolationCount = energyIsolationCount;
	}
	@Override
	public String toString() {
		return "EINotificationDto [energyIsolationNotificationList=" + energyIsolationNotificationList
				+ ", energyIsolationCount=" + energyIsolationCount + "]";
	}
	
	
}
