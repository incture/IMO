package com.murphy.taskmgmt.service;


import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.taskmgmt.dao.FracNotificationDao;
import com.murphy.taskmgmt.dto.FracNotificationDto;
import com.murphy.taskmgmt.service.interfaces.FracNotificationFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;

@Service("fracNotificationFacade")
@Transactional
public class FracNotificationFacade implements FracNotificationFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(FracNotificationFacade.class);
	
	@Autowired
	FracNotificationDao fracNotificationDao;
	
	@Override
	public ResponseMessage updateFracNotification(List<FracNotificationDto> dto) {
		ResponseMessage message=new ResponseMessage();
		message.setMessage("SuccessFully Updated Notified Wells");
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		try{
		message=fracNotificationDao.updateFracNotification(dto);
		}
		catch(Exception e){
			logger.error("[FracNotificationFacade][updateFracNotification][error]"+e.getMessage());
		}
		return message;
	}

	@Override
	public void getFracHitDetails(String userName) {
     try{
    	 fracNotificationDao.getFracHitDetails(userName);
     }
     catch(Exception e){
    	 logger.error("[FracNotificationFacade][getFracHitDetails][error]"+e.getMessage());
     }
	}

	
    
		
		
		
		
}

