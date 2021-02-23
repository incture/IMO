package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.PiggingSchedulerDto;
import com.murphy.taskmgmt.dto.PiggingUIReqDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface PiggingSchedulerFacadeLocal {

	ResponseMessage insertToDB(PiggingUIReqDto dto); 
	ResponseMessage updateWorkOrder(PiggingSchedulerDto dto,String pigTaskType);
}
