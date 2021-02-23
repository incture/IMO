package com.murphy.taskmgmt.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.murphy.taskmgmt.service.TaskManagementFacade;
import com.murphy.taskmgmt.service.interfaces.WorkbenchFacadeLocal;
@Component
public class TaskManagementScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskManagementScheduler.class);
	
	@Autowired
	TaskManagementFacade taskManagementFacade;

	@Autowired
	WorkbenchFacadeLocal wrkBench;
	
	//@Scheduled(cron = "0 10 2 ? * TUE-SAT") 
//	@Scheduled(cron = "0 10 20 ? * MON-FRI",zone = "CST") 
	public void autoCloseTask() {
		logger.error("Auto Close Staus Updation Initiated");
		taskManagementFacade.autoCloseStatus();
		logger.error("Auto Close Staus Updation Completed");
	}
	
	// Running Cancelled task Scheduler in the same time for IOP Admin Console Changes
	//@Scheduled(cron = "0 0 4 * * MON") 
//	@Scheduled(cron = "0 0 22 * * SUN",zone = "CST") 
	public void autoCancelReturnedWorkBenchTask() {
		logger.error("Auto Cancel Returned Workbench Task Initialted");
		wrkBench.autoCancelObxTask();
		logger.error("uto Cancel Returned Workbench Completed");
	}

}
