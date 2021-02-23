package com.murphy.taskmgmt.service.interfaces;



import java.util.Date;

import com.murphy.taskmgmt.dto.CustomTaskDto;

import com.murphy.taskmgmt.dto.ResponseMessage;

public interface PiggingFacadeLocal {
  double calculatePiggingTime(String eqipmentId);
  
  ResponseMessage createTask(String equipmentId,String worOrderNo);
  

CustomTaskDto getRetrivePayload(CustomTaskDto dto);

ResponseMessage UpdatePiggingHistory(Date now);


}
