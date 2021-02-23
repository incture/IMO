package com.murphy.taskmgmt.service;


import java.util.List;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.PiggingScheldulerDao;

import com.murphy.taskmgmt.dto.PiggingSchedulerDto;
import com.murphy.taskmgmt.dto.PiggingUIReqDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.PiggingSchedulerFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("PiggingSchedulerFacade")
public class PiggingSchedulerFacade implements PiggingSchedulerFacadeLocal{

	private static final Logger logger = LoggerFactory.getLogger(PiggingSchedulerFacade.class);

	@Autowired 
	PiggingScheldulerDao piggingDao;

	public ResponseMessage insertToDB(PiggingUIReqDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();	
		try{
		List<PiggingSchedulerDto> piggingDto=dto.getPiggingSchedulerDtoList();
			if(!ServicesUtil.isEmpty(piggingDto)){
				for(PiggingSchedulerDto woData:piggingDto){
					responseMessage=piggingDao.insertWoData(woData);

				}
			}
			else{
				logger.error("List is Empty");
			}
		}
		catch(Exception e){
			logger.error("Exception While Inserting WorkOrder Details to DB" + e.getMessage());
		}

		return responseMessage;

	}

	@SuppressWarnings("null")
	public ResponseMessage updateWorkOrder(PiggingSchedulerDto dto,String pigTaskType) {
		ResponseMessage responseMessage = new ResponseMessage();
		String response=null;
		responseMessage.setMessage("Exception while Updating WorkOrder");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			response=piggingDao.updateWorkOrder(dto,pigTaskType);
			System.err.println("Pigging UpdateWorkOrder"+response);
			if(response.equalsIgnoreCase("SUCCESS")){
				responseMessage.setMessage("WorkOrder Status Updated Successfully");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
		}
		catch(Exception e){
			logger.error("Exception while Updating WorkOrder status");
		}
		return responseMessage;

	}


}
