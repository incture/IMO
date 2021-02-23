package com.murphy.taskmgmt.scheduler;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.PiggingFacadeLocal;

@Component
public class PiggingTaskCreationSchedular {

	
	
	@Autowired
	PiggingFacadeLocal piggingFacade;
	
	
	private static final Logger logger = LoggerFactory.getLogger(PiggingTaskCreationSchedular.class);
	
	//@Scheduled(cron = "0 48 9 1/1 * ?")
//	@Scheduled(cron = "0 48 3 1/1 * ?",zone = "CST")
	public void createTaskForPigging(){
	
		logger.error("[Murphy][TaskManagement][PiggingTaskCreationSchedular][createTaskForPigging][response] before Running");
		ResponseMessage res= piggingFacade.UpdatePiggingHistory(new Date());
//		 System.err.println("[Murphy][TaskManagement][PiggingTaskCreationSchedular][createTaskForPigging][response] " +res);
		logger.error("[Murphy][TaskManagement][PiggingTaskCreationSchedular][createTaskForPigging][response] " +res);
		 
	}
}
