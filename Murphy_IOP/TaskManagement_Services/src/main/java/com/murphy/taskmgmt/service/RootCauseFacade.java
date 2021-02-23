package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.CustomAttrInstancesDao;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RootCauseDto;
import com.murphy.taskmgmt.dto.RootCauseResponseDto;
import com.murphy.taskmgmt.service.interfaces.RootCauseFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("rootCauseFacade")
public class RootCauseFacade implements RootCauseFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(RootCauseFacade.class);

	public RootCauseFacade() {
	}

	@Autowired
	private CustomAttrInstancesDao instDao;

	@Override
	public RootCauseResponseDto getRootCauseByTaskId(String taskId, String status, String origin) {
		ResponseMessage responseMessage = new ResponseMessage();
		RootCauseResponseDto responseDto = new RootCauseResponseDto();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			List<RootCauseDto> responseList = null;
			if (!MurphyConstant.INVESTIGATON.equals(origin) || ServicesUtil.isEmpty(origin))
				responseList = instDao.getRootCause(taskId, status, origin);
			else
				responseList = instDao.getRootCauseInArray(taskId, status, origin);

			if (ServicesUtil.isEmpty(responseList)) {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			} else {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				responseDto.setRootCauseList(responseList);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][RootCauseFacade][getRootCauseByTaskId][error]" + e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

	@Override
	public RootCauseResponseDto getRootCauseByTaskIdInArray(String taskId, String status, String origin) {
		ResponseMessage responseMessage = new ResponseMessage();
		RootCauseResponseDto responseDto = new RootCauseResponseDto();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			List<RootCauseDto> responseList = instDao.getRootCauseInArray(taskId, status, origin);
			if (ServicesUtil.isEmpty(responseList)) {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			} else {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				responseDto.setRootCauseList(responseList);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][RootCauseFacade][getRootCauseByTaskId][error]" + e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}
}
