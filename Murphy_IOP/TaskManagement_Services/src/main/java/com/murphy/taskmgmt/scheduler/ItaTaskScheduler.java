package com.murphy.taskmgmt.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.murphy.integration.service.ItaFacadeService;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.ita.ITAGasBlowByActionDto;
import com.murphy.taskmgmt.ita.ITARulesServiceFacadeLocal;
import com.murphy.taskmgmt.ita.ITATaskActionDto;
import com.murphy.taskmgmt.service.interfaces.ItaFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Component
public class ItaTaskScheduler {

	private static final Logger logger = LoggerFactory.getLogger(ItaTaskScheduler.class);
	
	@Autowired
	ItaFacadeLocal itaFacade;
	
	@Autowired
	ITARulesServiceFacadeLocal itaRulesServiceFacadeLocal;
	
	com.murphy.integration.interfaces.ItaFacadeLocal itaFacadeLocal;
	
	@Autowired
	ConfigDao configDao;
	
	// 9:50 am daily UTC
	// 3:50 am daily CST
	// 3:20 pm daily IST
//	@Scheduled(cron = "0 50 03 ? * *",zone = "CST")
	public void callItaTask(){
		logger.error("[Murphy][ItaTaskScheduler][callItaTask][Scheduler] Schedule Running on "+new Date());
		
		int durationInDays;
		 int numberOfTasksCreated;
		 String Classification;
		 String subClassification;
		 String rootCause;
		 String typeOfTaskToBeCreated;
		 String taskClassificationITA;
		 String taskSubClassificationITA;
		 ResponseMessage responseMessage = new ResponseMessage();
		 
		 List<ITATaskActionDto> itaTaskActionDto = new ArrayList<ITATaskActionDto>();
		 String configuration = configDao.getConfigurationByRef("ITA_LEAK_POT_ENABLED");
         try {
     		if(configuration.equalsIgnoreCase("TRUE")){
				 itaTaskActionDto = (List<ITATaskActionDto>) itaRulesServiceFacadeLocal.getITARulesByType(MurphyConstant.ITATaskType);
				 if (!ServicesUtil.isEmpty(itaTaskActionDto)) {
					 for (ITATaskActionDto obj : itaTaskActionDto) {
			
					 durationInDays = obj.getDurationInDays();
					 numberOfTasksCreated = obj.getNumberOfTasksCreated();
					 Classification = obj.getClassification();
					 subClassification = obj.getSubClassification();
					 rootCause = obj.getRootCause();
					 typeOfTaskToBeCreated = obj.getTypeOfTaskToBeCreated();
					 taskClassificationITA = obj.getTaskClassificationITA();
					 taskSubClassificationITA = obj.getTaskSubClassificationITA();
				
					 responseMessage= itaFacade.checkTaskCount(durationInDays, numberOfTasksCreated, Classification, subClassification, rootCause,typeOfTaskToBeCreated,
						taskClassificationITA, taskSubClassificationITA);
					 }
				 }
     		}
         }
         catch(Exception e){
        	 logger.error("ItaTaskScheduler.callItaTask()" + e.getMessage());
         }
	}
	
	    // 1:30 pm daily UTC
	    // 7:30 am CST
		// 7:00 pm daily (next day) IST
		@Scheduled(cron = "0 30 07 ? * *",zone = "CST")
		public void callItaCarryOverTask(){
			logger.error("[Murphy][ItaTaskScheduler][callItaCarryOverTask][Scheduler] Schedule Running on "+new Date());
			String configuration = configDao.getConfigurationByRef("ITA_CARRYOVER_ENABLED");
			try{
				if(configuration.equalsIgnoreCase("TRUE")){
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage = itaFacade.createWaterOilCarryOverTask();
				}
			}
			 catch(Exception e){
	        	 logger.error("ItaTaskScheduler.callItaCarryOverTask()" + e.getMessage());
	         }
		}
		
		// 11:15 AM daily UTC
	    // 5:15 am CST
		// 4:45 pm daily IST
		@SuppressWarnings("unchecked")
		@Scheduled(cron = "0 15 5 ? * *",zone = "CST")
		public void callItaGasBlowBy() {
		logger.error("[Murphy][ItaTaskScheduler][callItaGasBlowBy][Scheduler] Schedule Running on " + new Date());
		String configuration = configDao.getConfigurationByRef("ITA_GASBLOWBY_ENABLED");
		try {
			if (configuration.equalsIgnoreCase("TRUE")) {
				ResponseMessage response = new ResponseMessage();
				itaFacadeLocal = new ItaFacadeService();

				String taskClassification = null, taskSubClassification = null, taskToBeCreated = null,
						oilMeterMerrickId = null, gasMeterMerrickId = null, meterName = null;

				Double dailyOilValue = 0.0, comparisonRatio = 0.0;

				List<ITAGasBlowByActionDto> itaGasBlowByActionDto = new ArrayList<ITAGasBlowByActionDto>();

				itaGasBlowByActionDto = (List<ITAGasBlowByActionDto>) itaRulesServiceFacadeLocal
						.getITARulesByType(MurphyConstant.ITAGasBlowBy);
				if (!ServicesUtil.isEmpty(itaGasBlowByActionDto)) {
					for (ITAGasBlowByActionDto obj : itaGasBlowByActionDto) {
						taskClassification = obj.getTaskClassification();
						taskSubClassification = obj.getTaskSubClassification();
						taskToBeCreated = obj.getTaskToBeCreated();
						oilMeterMerrickId = obj.getOilMeterMerrickId();
						gasMeterMerrickId = obj.getGasMeterMerrickId();
						meterName = obj.getMeterName();
						dailyOilValue = obj.getDailyOilValue();
						comparisonRatio = obj.getComparisonRatio();

					}
				}

				com.murphy.integration.dto.ResponseMessage responseMessage = itaFacadeLocal
						.getLocCodeListForGasBlowBy(oilMeterMerrickId, gasMeterMerrickId, meterName, dailyOilValue);

				if (!responseMessage.getStatus().equals(MurphyConstant.FAILURE)) {
					response = itaFacade.createGasBlowBy(responseMessage, taskClassification, taskSubClassification,
							taskToBeCreated, comparisonRatio);
				} else {
					response.setMessage(responseMessage.getMessage());
					response.setStatus(responseMessage.getStatus());
					response.setStatusCode(responseMessage.getStatusCode());
				}

				logger.error("RESPONSE MESSAGE FOR GASBLOWBY--> " + response.toString());

			}

		} catch (Exception e) {
			logger.error("ItaTaskScheduler.callItaGasBlowBy()" + e.getMessage());
			e.printStackTrace();
		}
	}

}
