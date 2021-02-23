package com.murphy.taskmgmt.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.dto.InvestigationHistoryDto;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.taskmgmt.dao.AlsInvestigationDao;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.AlsStagingFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("alsStagingFacade")
public class AlsStagingFacade implements AlsStagingFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(AlsStagingFacade.class);

	@Autowired
	private AlsInvestigationDao alsDao;

	@Override
	public ResponseMessage stageALSData(String muwId) {
		ResponseMessage message = new ResponseMessage();
		message.setMessage("ALS Staging " + MurphyConstant.CREATE_FAILURE);
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			String str = alsDao.createRecord(muwId);
			if (!ServicesUtil.isEmpty(str) && str.equals(MurphyConstant.SUCCESS)) {
				message.setMessage("ALS Staging " + MurphyConstant.CREATED_SUCCESS);
				message.setStatus(MurphyConstant.SUCCESS);
				message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("[Murphy][AlsStagingFacade][create][error]" + e.getMessage());
		}
		return message;
	}

	@Override
	public ResponseMessage removeALSData(String muwi) {
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Als Staging " + MurphyConstant.DELETE_FAILURE);
		message.setMessage(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		String result = alsDao.removeRecord(muwi);
		if (MurphyConstant.SUCCESS.equals(result)) {
			message.setMessage(MurphyConstant.DELETE_SUCCESS);
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		return message;
	}

	@Override
	public UIResponseDto getInvestigationDetails(String muwi)
	{
		UIResponseDto dto= new UIResponseDto();
		List<InvestigationHistoryDto> historyDto=null;
		com.murphy.integration.dto.ResponseMessage message = new com.murphy.integration.dto.ResponseMessage();
		message.setMessage("ALS Investigation " + MurphyConstant.READ_FAILURE);
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			historyDto = alsDao.getAlsInvestigation(muwi);

			if (!ServicesUtil.isEmpty(historyDto)) {
				message.setMessage(MurphyConstant.READ_SUCCESS);
				Collections.sort(historyDto);
				dto.setInvestigationHistoryDtoList(historyDto);
			}else{
				message.setMessage(MurphyConstant.NO_RESULT);
			}
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][AlsStaging][read][error]" + e.getMessage());
		}
		dto.setResponseMessage(message);
		return dto;
	}

}
