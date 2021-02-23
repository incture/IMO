package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dto.AuditReportDto;
import com.murphy.taskmgmt.dto.AuditReportResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.AuditFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;



@Service("auditFacade")
public class AuditFacade implements AuditFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(AuditFacade.class);

	public AuditFacade() {
	}

	@Autowired
	private AuditDao auditDao;

	@Override 
	public AuditReportResponseDto getReport(){
		

		AuditReportResponseDto response = new AuditReportResponseDto();
		ResponseMessage message = new ResponseMessage();
		message.setMessage(MurphyConstant.READ_FAILURE);
		message.setMessage(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			List<AuditReportDto> responseDto = auditDao.getReport();
			if (ServicesUtil.isEmpty(responseDto)) {
				message.setMessage(MurphyConstant.NO_RESULT);
			} else {
				message.setMessage(MurphyConstant.READ_SUCCESS);
				response.setTasks(responseDto);
			}
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}catch(Exception e){
			logger.error("[Murphy][LocationHierarchy][getHierarchy][error]"+e.getMessage());
		}
		response.setResponseMessage(message);
		return response;
	}

}

