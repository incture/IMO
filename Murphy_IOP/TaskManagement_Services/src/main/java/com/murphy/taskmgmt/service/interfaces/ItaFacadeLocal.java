package com.murphy.taskmgmt.service.interfaces;

import org.json.JSONObject;

import com.murphy.taskmgmt.dto.ResponseMessage;

public interface ItaFacadeLocal {
	
	ResponseMessage checkTaskCount(int durationInDays,int numberOfTasksCreated,String Classification,String subClassification,String rootCause,
			String typeOfTaskToBeCreated,String taskClassificationITA,String taskSubClassificationITA);
	
	ResponseMessage createDopTask(int runTime);
	
	ResponseMessage createWaterOilCarryOverTask();
	
	JSONObject getCanaryData(String startTime, String endTime);
	
	JSONObject getCanaryDataforWaterOil(String startTime, String endTime, String[] canaryPraram,String aggregateName, String aggregateInterval);
	
	String getUserToken();
	
	ResponseMessage createGasBlowBy(com.murphy.integration.dto.ResponseMessage responseMessage, String taskClassification, String taskSubClassification, String taskToBeCreated, Double comparisonRatio);

}
