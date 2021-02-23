package com.murphy.taskmgmt.dto;

import java.util.List;

public class AlarmNotificationDto {

	private List<NotificationDto> alarmNotificationList;
	private int alarmCount;
	
	
	public List<NotificationDto> getAlarmNotificationList() {
		return alarmNotificationList;
	}
	public void setAlarmNotificationList(List<NotificationDto> alarmNotificationList) {
		this.alarmNotificationList = alarmNotificationList;
	}
	
	public int getAlarmCount() {
		return alarmCount;
	}
	public void setAlarmCount(int alarmCount) {
		this.alarmCount = alarmCount;
	}
	@Override
	public String toString() {
		return "AlarmNotificationDto [alarmNotificationList=" + alarmNotificationList + ", alarmCount=" + alarmCount
				+ "]";
	}
	
	
	
	
}
