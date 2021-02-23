package com.murphy.taskmgmt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.DeviceDao;
import com.murphy.taskmgmt.dto.DeviceDto;
import com.murphy.taskmgmt.dto.DeviceListReponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.DeviceFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("deviceFacade")
public class DeviceFacade implements DeviceFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(DeviceFacade.class);

	@Autowired
	DeviceDao deviceDao;

	@Override
	public ResponseMessage createDeviceRecord(DeviceDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			if (!ServicesUtil.isEmpty(dto)) {
				deviceDao.createDeviceRecord(dto);
				responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.CREATED_SUCCESS);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
		} catch (

		Exception e) {
			logger.error("[Murphy][DeviceFacade][createDeviceRecord][error]" + e.getMessage());
		}
		return responseMessage;
	}

	@Override
	public DeviceListReponseDto getDeviceList() {
		DeviceListReponseDto deviceListReponseDto = new DeviceListReponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		deviceListReponseDto.setResponseMessage(responseMessage);
		try {
			deviceListReponseDto.setDeviceDtoList(deviceDao.getDeviceList());
			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][DeviceFacade][getDeviceList][error]" + e.getMessage());
		}
		return deviceListReponseDto;
	}

}
