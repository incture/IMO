package com.murphy.taskmgmt.websocket;

import com.murphy.taskmgmt.dto.AlarmNotificationDto;
import com.murphy.taskmgmt.dto.BypassNotificationDto;
import com.murphy.taskmgmt.dto.EINotificationDto;
import com.murphy.taskmgmt.dto.FracNotificationDto;
import com.murphy.taskmgmt.dto.PwHopperNotificationDto;
import com.murphy.taskmgmt.dto.TaskNotificationDto;

public class FracMessage {
    private String request;
    private String connectionMessage;
    private String userName;
    private int notifTotalCount;
    
    //parameters for accepted bypass log 
    private String shiftChnage;
    private String action;
    
    
    private FracNotificationDto fracNotificationDto;
    private TaskNotificationDto taskNotificationDto;
    private AlarmNotificationDto alarmNotificationDto;
    private PwHopperNotificationDto pwHopperNotificationDto;
    private BypassNotificationDto byPassLogNotificationDto;
    private EINotificationDto energyIsolationNotificationDto;
    
	

	

	public FracNotificationDto getFracNotificationDto() {
		return fracNotificationDto;
	}

	public void setFracNotificationDto(FracNotificationDto fracNotificationDto) {
		this.fracNotificationDto = fracNotificationDto;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	
	public String getConnectionMessage() {
		return connectionMessage;
	}

	public void setConnectionMessage(String connectionMessage) {
		this.connectionMessage = connectionMessage;
	}
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public TaskNotificationDto getTaskNotificationDto() {
		return taskNotificationDto;
	}

	public void setTaskNotificationDto(TaskNotificationDto taskNotificationDto) {
		this.taskNotificationDto = taskNotificationDto;
	}

	
	
	public AlarmNotificationDto getAlarmNotificationDto() {
		return alarmNotificationDto;
	}

	public void setAlarmNotificationDto(AlarmNotificationDto alarmNotificationDto) {
		this.alarmNotificationDto = alarmNotificationDto;
	}
	

	public PwHopperNotificationDto getPwHopperNotificationDto() {
		return pwHopperNotificationDto;
	}

	public void setPwHopperNotificationDto(PwHopperNotificationDto pwHopperNotificationDto) {
		this.pwHopperNotificationDto = pwHopperNotificationDto;
	}

	public int getNotifTotalCount() {
		return notifTotalCount;
	}

	public void setNotifTotalCount(int notifTotalCount) {
		this.notifTotalCount = notifTotalCount;
	}
	

	public BypassNotificationDto getByPassLogNotificationDto() {
		return byPassLogNotificationDto;
	}

	public void setByPassLogNotificationDto(BypassNotificationDto byPassLogNotificationDto) {
		this.byPassLogNotificationDto = byPassLogNotificationDto;
	}

	
	public EINotificationDto getEnergyIsolationNotificationDto() {
		return energyIsolationNotificationDto;
	}

	public void setEnergyIsolationNotificationDto(EINotificationDto energyIsolationNotificationDto) {
		this.energyIsolationNotificationDto = energyIsolationNotificationDto;
	}

	
	public String getShiftChnage() {
		return shiftChnage;
	}

	public void setShiftChnage(String shiftChnage) {
		this.shiftChnage = shiftChnage;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "FracMessage [request=" + request + ", connectionMessage=" + connectionMessage + ", userName=" + userName
				+ ", notifTotalCount=" + notifTotalCount + ", shiftChnage=" + shiftChnage + ", action=" + action
				+ ", fracNotificationDto=" + fracNotificationDto + ", taskNotificationDto=" + taskNotificationDto
				+ ", alarmNotificationDto=" + alarmNotificationDto + ", pwHopperNotificationDto="
				+ pwHopperNotificationDto + ", byPassLogNotificationDto=" + byPassLogNotificationDto
				+ ", energyIsolationNotificationDto=" + energyIsolationNotificationDto + "]";
	}

	
	
	
}
