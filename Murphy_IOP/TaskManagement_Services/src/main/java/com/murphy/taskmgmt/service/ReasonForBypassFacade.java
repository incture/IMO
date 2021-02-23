package com.murphy.taskmgmt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.ReasonForBypassDao;
import com.murphy.taskmgmt.dto.ReasonForBypassDto;
import com.murphy.taskmgmt.dto.ReasonForBypassResponseListDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.ReasonForBypassFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("reasonForBypassFacade")
public class ReasonForBypassFacade implements ReasonForBypassFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(ReasonForBypassFacade.class);

	@Autowired
	ReasonForBypassDao reasonForBypassDao;

	@Override
	public ResponseMessage createReasonForBypass(ReasonForBypassDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			if (!ServicesUtil.isEmpty(dto)) {
				reasonForBypassDao.createReasonForBypass(dto);
				responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.CREATED_SUCCESS);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
		} catch (

		Exception e) {
			logger.error("[Murphy][ReasonForBypassFacade][createReasonForBypass][error]" + e.getMessage());
		}
		return responseMessage;
	}

	@Override
	public ReasonForBypassResponseListDto getReasonForBypassList() {
		ReasonForBypassResponseListDto reasonForBypassResponseListDto = new ReasonForBypassResponseListDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		reasonForBypassResponseListDto.setResponseMessage(responseMessage);
		try {
			reasonForBypassResponseListDto.setReasonForBypassDtoList(reasonForBypassDao.getReasonForBypassList());
			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][ReasonForBypassFacade][getReasonForBypassList][error]" + e.getMessage());
		}
		return reasonForBypassResponseListDto;
	}

}
