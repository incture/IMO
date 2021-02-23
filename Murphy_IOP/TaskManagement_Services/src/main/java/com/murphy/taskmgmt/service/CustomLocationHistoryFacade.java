package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.CustomLocationHistoryDao;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskListDto;
import com.murphy.taskmgmt.dto.CustomLocationHistoryDto;
import com.murphy.taskmgmt.service.interfaces.CustomLocationHistoryFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("CustomLocationHistoryFacade")
public class CustomLocationHistoryFacade implements CustomLocationHistoryFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(CustomLocationHistoryFacade.class);

	public CustomLocationHistoryFacade() {
	}
	
	@Autowired
	private CustomLocationHistoryDao customLocationHistoryDao;

	@Override
	public CustomLocationHistoryDto getLocTaskHistory(String locationCode, String monthTime,int page, int page_size) {
		
		CustomLocationHistoryDto responseDto = new CustomLocationHistoryDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			responseDto = customLocationHistoryDao.getTaskHistory(locationCode, monthTime,page,page_size);
			if (!ServicesUtil.isEmpty(responseDto)) {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][CustomLocationHistoryFacade][getLocTaskHistory][error]" + e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

}
