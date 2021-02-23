package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import com.murphy.integration.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.BypassNotificationDto;
import com.murphy.taskmgmt.dto.NotificationDto;

public interface NotificationFacadeLocal {

	void getNotificationList();
	void updatePwHopper(String userId,boolean isCanada,boolean isEFS);
	ResponseMessage updateBubbleAckDetails(List<NotificationDto> notifyDto);
	ResponseMessage updateSafetyAppStatusDetails(List<NotificationDto> bypassDtoList);
	ResponseMessage sendNotificationForFutureTask();	
	
}
