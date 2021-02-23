package com.murphy.taskmgmt.service.interfaces;


import java.util.List;

import com.murphy.integration.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.FracNotificationDto;

public interface FracNotificationFacadeLocal {

	void getFracHitDetails(String userName);
	ResponseMessage updateFracNotification(List<FracNotificationDto> dto);
	
}
