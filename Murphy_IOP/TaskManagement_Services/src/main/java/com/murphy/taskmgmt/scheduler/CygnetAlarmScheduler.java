package com.murphy.taskmgmt.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.murphy.taskmgmt.dao.CygnetAlarmFeedDao;

@Component
public class CygnetAlarmScheduler {

	private static final Logger logger = LoggerFactory.getLogger(CygnetDataPullSchedular.class);
	
	@Autowired
	private CygnetAlarmFeedDao cygnetAlarmFeedDao;

	
	/* Insert data to CYGNET_RECOM_DOWNTIME table */
/*	public void SetRecommendedDowntime(CygnetDowntimeCounterDo cygnetDowntimeCounterDo) {

		// logger.error("[Murphy][TaskManagement][CygnetAlarmScheduler][SetRecommendedDowntime][info][started]");

		try {

			CygnetRecomDowntimeDao cygnetRecomDowntimeDao = SpringContextBridge.services().getCygnetRecomDowntimeDao();

			if (cygnetDowntimeCounterDo.getAlarmSeverity().equals("1-Alarm")) {

				boolean isrecordExist = cygnetRecomDowntimeDao.load(cygnetDowntimeCounterDo.getPointId(),
						cygnetDowntimeCounterDo.getTimeStamp());
				// logger.error("[Murphy][TaskManagement][CygnetAlarmScheduler][SetRecommendedDowntime][info][1-Alarm]"+isrecordExist);
				if (!isrecordExist) {
					// logger.error("[Murphy][TaskManagement][CygnetAlarmScheduler][SetRecommendedDowntime][info][1-Alarm][insert]");
					CygnetRecomDowntimeDo cygnetRecomDowntimeDo = new CygnetRecomDowntimeDo();
					cygnetRecomDowntimeDo.setId(UUID.randomUUID().toString());
					cygnetRecomDowntimeDo.setPointId(cygnetDowntimeCounterDo.getPointId());
					cygnetRecomDowntimeDo.setStartTime(cygnetDowntimeCounterDo.getTimeStamp());
					cygnetRecomDowntimeDao.createRecomDowntime(cygnetRecomDowntimeDo);
					// logger.error("[Murphy][TaskManagement][CygnetAlarmScheduler][SetRecommendedDowntime][info][1-Alarm][insert][end]");
				}

			} else if (cygnetDowntimeCounterDo.getAlarmSeverity().equals("4-Normal")) {
				// logger.error("[Murphy][TaskManagement][CygnetAlarmScheduler][SetRecommendedDowntime][info][4-Normal]");
				cygnetRecomDowntimeDao.updateEndTime(cygnetDowntimeCounterDo.getPointId(),
						cygnetDowntimeCounterDo.getTimeStamp());
			}

		} catch (Exception e) {
			logger.error(
					"[Murphy][TaskManagement][CygnetAlarmScheduler][SetRecommendedDowntime][error]" + e.getMessage());
		}
	}*/

	/* Delete Older datas From Alarms2 */
	//@Scheduled(cron = "0 0 0 * * ?")
//	@Scheduled(cron = "0 0 18 * * ?",zone = "CST")
	public void DeleteOlderAlarmRecords() {
		
		String response = cygnetAlarmFeedDao.DeleteOlderAlarmRecords();
		logger.error("[Murphy][TaskManagement][CygnetAlarmScheduler][DeleteOlderRecords][response] " +response);
	}
	

}
