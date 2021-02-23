package com.murphy.taskmgmt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.service.ItaFacadeService;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.ita.ITAGasBlowByActionDto;
import com.murphy.taskmgmt.ita.ITARulesServiceFacadeLocal;
import com.murphy.taskmgmt.ita.ITATaskActionDto;
import com.murphy.taskmgmt.service.interfaces.DopAutomationFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.ItaFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@RestController
@CrossOrigin()
@ComponentScan("com.murphy")
@RequestMapping(value = "/ita", produces = "application/json")
public class ItaRest {

	@Autowired
	ItaFacadeLocal itaFacade;
	
	com.murphy.integration.interfaces.ItaFacadeLocal itaFacadeLocal;

	@Autowired
	ITARulesServiceFacadeLocal itaRulesServiceFacadeLocal;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/task", method = RequestMethod.GET)
	public ResponseMessage createTask() throws ClientProtocolException, IOException{

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
		 boolean fail = false;

		 itaTaskActionDto = (List<ITATaskActionDto>) itaRulesServiceFacadeLocal.getITARulesByType("Task");
		 if (!ServicesUtil.isEmpty(itaTaskActionDto)) {
			 for (ITATaskActionDto obj : itaTaskActionDto) {
	
			 durationInDays = obj.getDurationInDays();
			 numberOfTasksCreated = obj.getNumberOfTasksCreated();
			 Classification = obj.getClassification();
			 subClassification = obj.getSubClassification();
			 typeOfTaskToBeCreated = obj.getTypeOfTaskToBeCreated();
			 taskClassificationITA = obj.getTaskClassificationITA();
			 taskSubClassificationITA = obj.getTaskSubClassificationITA();
			 rootCause = obj.getRootCause();
	
			 responseMessage =  itaFacade.checkTaskCount(durationInDays, numberOfTasksCreated, Classification, subClassification,rootCause,
					typeOfTaskToBeCreated, taskClassificationITA, taskSubClassificationITA);
				 if(responseMessage.getStatus().equalsIgnoreCase(MurphyConstant.FAILURE))
				 {
					 fail= true;
				 }
			 }
		 }
		 if(fail)
			 responseMessage.setStatus(MurphyConstant.FAILURE); 
		 return responseMessage;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/taskDop", method = RequestMethod.GET)
	public ResponseMessage createTaskDop()
	{
		 ResponseMessage responseMessage; 
		 int runTime = 20;
		 responseMessage = itaFacade.createDopTask(runTime);
		 return responseMessage;
		 
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/taskCarryOver", method = RequestMethod.GET)
	public ResponseMessage createWaterOilCarryOverTask()
	{
		 ResponseMessage responseMessage = itaFacade.createWaterOilCarryOverTask();
		 return responseMessage;
		 
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gasBlowBy", method = RequestMethod.GET)
	public ResponseMessage createGasBlowBy() throws ClientProtocolException, IOException
	{
		
		//ONLY FOR SIT/UAT
//		ResponseMessage response;
//		
//		
//		String taskClassification=null,taskSubClassification=null,taskToBeCreated=null,oilMeterMerrickId=null,gasMeterMerrickId=null,meterName=null;
//			
//		Double dailyOilValue = 0.0,comparisonRatio=0.0;
//			 	
//			 
//			
//			List<ITAGasBlowByActionDto> itaGasBlowByActionDto = new ArrayList<ITAGasBlowByActionDto>();
//			
//			
//			itaGasBlowByActionDto= (List<ITAGasBlowByActionDto>) itaRulesServiceFacadeLocal.getITARulesByType(MurphyConstant.ITAGasBlowBy);
//			 if (!ServicesUtil.isEmpty(itaGasBlowByActionDto)) {
//				 for (ITAGasBlowByActionDto obj : itaGasBlowByActionDto) {
//					 taskClassification = obj.getTaskClassification();
//					 taskSubClassification = obj.getTaskSubClassification();
//					 taskToBeCreated= obj.getTaskToBeCreated();
//					 oilMeterMerrickId = obj.getOilMeterMerrickId();
//					 gasMeterMerrickId = obj.getGasMeterMerrickId();
//					 meterName = obj.getMeterName();
//					 dailyOilValue=obj.getDailyOilValue();
//					 comparisonRatio=obj.getComparisonRatio();
//					
//				    // Call create task
////					 responseMessage = itaWaterOilDao.createItaWaterOil(tier,oilThreshold,waterThreshold,todayMap,yesterdayMap,
////							 daysAvgMap,classification, subClassification,taskType);		
//				 }
//		     }
//			
//		
//		
//		
//		
//		com.murphy.integration.dto.ResponseMessage responseMessage = new com.murphy.integration.dto.ResponseMessage();
//		
//		responseMessage.setData(oilMeterMerrickId+"#"+gasMeterMerrickId+"#"+meterName+"#"+dailyOilValue);
//		response=itaFacade.createGasBlowBy(responseMessage,taskClassification,taskSubClassification,taskToBeCreated,comparisonRatio);
//		
//		return response;
		
		
		//ACTUAL CODE
		ResponseMessage response=new ResponseMessage();
		
		itaFacadeLocal=new ItaFacadeService();
		
		 String taskClassification=null,taskSubClassification=null,taskToBeCreated=null,oilMeterMerrickId=null,gasMeterMerrickId=null,meterName=null;
		
		 Double dailyOilValue = 0.0,comparisonRatio=0.0;
		 	
		 
		
		List<ITAGasBlowByActionDto> itaGasBlowByActionDto = new ArrayList<ITAGasBlowByActionDto>();
		
		
		itaGasBlowByActionDto= (List<ITAGasBlowByActionDto>) itaRulesServiceFacadeLocal.getITARulesByType(MurphyConstant.ITAGasBlowBy);
		 if (!ServicesUtil.isEmpty(itaGasBlowByActionDto)) {
			 for (ITAGasBlowByActionDto obj : itaGasBlowByActionDto) {
				 taskClassification = obj.getTaskClassification();
				 taskSubClassification = obj.getTaskSubClassification();
				 taskToBeCreated= obj.getTaskToBeCreated();
				 oilMeterMerrickId = obj.getOilMeterMerrickId();
				 gasMeterMerrickId = obj.getGasMeterMerrickId();
				 meterName = obj.getMeterName();
				 dailyOilValue=obj.getDailyOilValue();
				 comparisonRatio=obj.getComparisonRatio();
	
			 }
		 }
		
		com.murphy.integration.dto.ResponseMessage responseMessage = itaFacadeLocal.getLocCodeListForGasBlowBy(oilMeterMerrickId,gasMeterMerrickId,meterName,dailyOilValue);
		
		if(!responseMessage.getStatus().equals(MurphyConstant.FAILURE))
		{
		response=itaFacade.createGasBlowBy(responseMessage,taskClassification,taskSubClassification,taskToBeCreated,comparisonRatio);
		return response;
		}
		else
		{
			response.setMessage(responseMessage.getMessage());
			response.setStatus(responseMessage.getStatus());
			response.setStatusCode(responseMessage.getStatusCode());
			return response;
		}
		 
		 
	}

	@RequestMapping(value = "/getITARulesByType", method = RequestMethod.GET)
	public List<?> getITARulesByType(@RequestParam("type") String type) throws ClientProtocolException, IOException {

		return itaRulesServiceFacadeLocal.getITARulesByType(type);
	}
	
	/*@RequestMapping(value = "/getITARulesForDOP", method = RequestMethod.GET)
	public List<ITADOPActionDto> getITARulesForDOP() throws ClientProtocolException, IOException {

		return invokeITARulesForDOP.getITADOPRulesByType("Task");
	}

	 @RequestMapping(value = "/getByDestination", method = RequestMethod.GET)
	public Map<String, String> getByDestination() throws ClientProtocolException, IOException {
		return invokeITARulesForTask.getByDestination();
	}
	
	@RequestMapping(value = "/getCanaryDataOil", method = RequestMethod.GET)
	public JSONObject getCanaryData() throws ClientProtocolException, IOException {

		String startTime = "2020-01-06 7:00:00";
	    String endTime = "2020-01-06 7:15:00";			
		return itaFacade.getCanaryDataforWaterOil(startTime,endTime);
	} */
}
