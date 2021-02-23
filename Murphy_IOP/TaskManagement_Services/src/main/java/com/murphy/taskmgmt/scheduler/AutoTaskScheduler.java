package com.murphy.taskmgmt.scheduler;
/**
 * @author Rashmendra.Sai
 *
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.murphy.taskmgmt.dao.ATSTaskAssignmentDao;
import com.murphy.taskmgmt.service.AutoTaskSchedulingFacade;
import com.murphy.taskmgmt.util.MurphyConstant;


@Component
public class AutoTaskScheduler {

	private static final Logger logger = LoggerFactory.getLogger(AutoTaskScheduler.class);
	
	@Autowired
	AutoTaskSchedulingFacade atsfacade;
	
	@Autowired
	ATSTaskAssignmentDao atsDao;

	// 10:10 AM UTC
	// 4:10 AM CST
	// 3:40 PM IST
//	@Scheduled(cron = "0 10 04 ? * MON-FRI",zone = "CST")
	public void generateDataForATS() {
		String response = MurphyConstant.FAILURE;
		logger.error("Auto Task Scheduling Initiated");
		response = atsfacade.serviceHitCanary();
		if (response.equalsIgnoreCase(MurphyConstant.SUCCESS)){
			response = atsDao.fetchATSTaskandUpdateTable();
			if (response.equalsIgnoreCase(MurphyConstant.SUCCESS))
				response = atsDao.updateATSTaskAssignmentTableStatus();
		}
		if(response.equalsIgnoreCase(MurphyConstant.SUCCESS))
			logger.error("Auto Task Scheduling Successfull");
		else
			logger.error("Failure in running Auto Task Scheduling ");
	}
}
