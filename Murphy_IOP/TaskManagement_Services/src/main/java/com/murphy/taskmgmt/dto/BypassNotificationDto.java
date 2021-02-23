package com.murphy.taskmgmt.dto;

import java.util.List;

public class BypassNotificationDto {

	private List<NotificationDto> byPassLogNotificationList;
	private int byPassLogCount;
	private int shiftChangeAcceptedCount;
	
	public List<NotificationDto> getByPassLogNotificationList() {
		return byPassLogNotificationList;
	}
	public void setByPassLogNotificationList(List<NotificationDto> byPassLogNotificationList) {
		this.byPassLogNotificationList = byPassLogNotificationList;
	}
	public int getByPassLogCount() {
		return byPassLogCount;
	}
	public void setByPassLogCount(int byPassLogCount) {
		this.byPassLogCount = byPassLogCount;
	}
	
	public int getShiftChangeAcceptedCount() {
		return shiftChangeAcceptedCount;
	}
	public void setShiftChangeAcceptedCount(int shiftChangeAcceptedCount) {
		this.shiftChangeAcceptedCount = shiftChangeAcceptedCount;
	}
	@Override
	public String toString() {
		return "BypassNotificationDto [byPassLogNotificationList=" + byPassLogNotificationList + ", byPassLogCount="
				+ byPassLogCount + ", shiftChangeAcceptedCount=" + shiftChangeAcceptedCount + "]";
	}
	
	
}
