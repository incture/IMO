package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.DeviceDto;
import com.murphy.taskmgmt.dto.DeviceListReponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface DeviceFacadeLocal {
	
	ResponseMessage createDeviceRecord(DeviceDto dto);
	DeviceListReponseDto getDeviceList();

}
