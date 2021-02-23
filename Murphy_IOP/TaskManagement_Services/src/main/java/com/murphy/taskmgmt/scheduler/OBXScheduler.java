package com.murphy.taskmgmt.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.murphy.taskmgmt.service.interfaces.ObxSchedulerFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.WorkbenchFacadeLocal;

@Component
public class OBXScheduler {
	private static final Logger logger = LoggerFactory.getLogger(OBXScheduler.class);
	@Autowired
	ObxSchedulerFacadeLocal obx;
	
	// 4 AM UTC
	// 1 AM CST - 12.30
	// 12.30 PM IST
//	@Scheduled(cron = "0 0 22 * * SUN",zone = "CST")
	public void generateWellVisitMatrix() {
		logger.error("Well Visit Matrix Creation Initiated");
		obx.generateWellVisitMatrix_Field();
		logger.error("Well Visit Matrix Created Successfully");
	}

	// 4.30 AM UTC
	// 1.30 AM CST - 1 
	// 1 PM IST
//	@Scheduled(cron = "0 30 22 * * SUN",zone = "CST")
	public void CreateUserJobs() {
		logger.error("User Task Allocation Initiated");
		obx.updateConfig();
		obx.CreateTaskAllocationField();
		logger.error("User Task Allocation Initiated");
	}

	// 11.30 AM UTC
	// 5.30 AM CST 
	// 5 PM IST
//	@Scheduled(cron = "0 30 5 ? * MON-FRI",zone = "CST")
	public void creatObxTasks() {
		logger.error("User Job Creation Initiated");
		obx.issueObxTasks();
		logger.error("User Job Creation Completed");
	}

////	// 1 AM UTC
////	// 8 PM CST 
////	// 6.30 AM IST
//	@Scheduled(cron = "0 0 1 ? * TUE-SAT")
//	public void RevokeTasks() {
//		logger.error("Revoking tasks initiated");
//		obx.revokeObxTasks();
//		logger.error("Revoking tasks Completed");
//	}
//	
//	// 1.10 AM UTC
//	// 8.10 PM CST 
//	// 6.40 AM IST
//	@Scheduled(cron = "0 10 1 ? * TUE-SAT")
//	public void revokedTaskAllocation() {
//		logger.error("revokedTaskAllocation initiated");
//		obx.revokedTaskAllocation_Field();
//		logger.error("revokedTaskAllocation Completed");
//	}
}
