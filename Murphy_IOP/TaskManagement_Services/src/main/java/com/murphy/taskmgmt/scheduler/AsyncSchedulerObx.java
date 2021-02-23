package com.murphy.taskmgmt.scheduler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.service.interfaces.ObxSchedulerFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;

@Component
public class AsyncSchedulerObx {
	
	private static final Logger logger = LoggerFactory.getLogger(AsyncSchedulerObx.class);
	
	@Autowired
	ObxSchedulerFacadeLocal obxFacade;
	
	@Autowired
	ConfigDao configDao;
	
	@Async("asyncExecutor")
	public void runObxEngine() throws InterruptedException{
		try{
		logger.error("Obx Engine start.."+System.currentTimeMillis());
		obxFacade.generateWellVisitMatrix_Field();
		obxFacade.CreateTaskAllocationField();
		logger.error("Obx Engine End.."+System.currentTimeMillis());
		Thread.sleep(1000L);
		}
		catch (Exception e) {
			logger.error("[AsyncSchedulerObx][runObxEngine] Exception "+e.getMessage());
		}
		finally {
			configDao.saveOrUpdateConfigByRef(MurphyConstant.OBX_ENGINE_RUNNING_FLAG,"false");
		}
	}
}
