package com.murphy.taskmgmt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.taskmgmt.dao.EnersightProveMonthlyDao;
import com.murphy.taskmgmt.service.interfaces.EnersightProveMonthlyLocal;

@Service("EnersightProveMonthlyFacade")
public class EnersightProveMonthlyFacade implements EnersightProveMonthlyLocal {

	// private static final Logger logger = LoggerFactory.getLogger(AlarmFeedFacade.class);

	public EnersightProveMonthlyFacade() {
	}

	private static final Logger logger = LoggerFactory.getLogger(EnersightProveMonthlyFacade.class);
	
	@Autowired
	private EnersightProveMonthlyDao enersightProveMonthlyDao;

	ResponseMessage message = null;


	@Override
	public UIResponseDto fetchProveDailyData() {

		// logger.info("[fetchProveDailyData] : INFO- Service Started");
		UIResponseDto uiResponseDto = new UIResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();

		try {
			uiResponseDto.setEnersightProveDailyDtoList(enersightProveMonthlyDao.fetchProveDailyData());
			responseMessage.setMessage("Successful");
			responseMessage.setStatus("SUCCESS");
			responseMessage.setStatusCode("0");

		} catch (Exception e) {
			logger.error("[fetchProveDailyData] : ERROR- Exception while interacting with database " + e);
			responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");

		} 
		uiResponseDto.setResponseMessage(responseMessage);
		return uiResponseDto;
	}

}
