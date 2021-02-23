package com.murphy.taskmgmt.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphy.integration.dto.GasBlowByDto;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.ItaTaskDao;
import com.murphy.taskmgmt.dao.ItaWaterOilDao;
import com.murphy.taskmgmt.dao.ProductionVarianceDao;
import com.murphy.taskmgmt.dao.WellMuwiDao;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.ita.ITADOPActionDto;
import com.murphy.taskmgmt.ita.ITARulesServiceFacadeLocal;
import com.murphy.taskmgmt.ita.ITAWaterOilActionDto;
import com.murphy.taskmgmt.scheduler.CanaryStagingScheduler;
import com.murphy.taskmgmt.service.interfaces.ItaFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("ItaFacade")
@Transactional
public class ItaFacade implements ItaFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(ItaFacade.class);

	@Autowired
	private ItaTaskDao itaTaskDao;

	@Autowired
	private ProductionVarianceDao productionVarianceDao;

	@Autowired
	private ConfigDao configDao;

	@Autowired
	private WellMuwiDao wellMuwiDao;
	
	@Autowired
	ITARulesServiceFacadeLocal itaRulesServiceFacadeLocal;
	
	@Autowired
	CanaryStagingScheduler canaryStagingScheduler;
	
	@Autowired
	ItaWaterOilDao itaWaterOilDao;
	
	HashMap<String, String> muwiSTSSVMap = null;
	HashMap<String, Double> todayMap = null;
	HashMap<String, Double> yesterdayMap = null;
	HashMap<String, Double> daysAvgMap = null;

	// Calculation of already created task and then create new task : ITA -> TASK
	@Override
	public ResponseMessage checkTaskCount(int durationInDays, int numberOfTasksCreated, String Classification,
			String subClassification, String rootCause, String typeOfTaskToBeCreated, String taskClassificationITA,
			String taskSubClassificationITA) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage = itaTaskDao.checkTaskCount(durationInDays, numberOfTasksCreated, Classification,
				subClassification, rootCause,typeOfTaskToBeCreated, taskClassificationITA, taskSubClassificationITA);

		return responseMessage;
	}

	// Create task for ITA -> DOP
	@SuppressWarnings("unchecked")
	public ResponseMessage createDopTask(int runtime) {

		List<ITADOPActionDto> itaDOPActionDto = new ArrayList<ITADOPActionDto>();
		// Rules values
		String Tier = null, taskType = null, STSSV_Rules = null;
		String classification = null, subClassification = null;
		double barrels, deviation;
		int toTime, fromTime, i=0;
		String startDate=null, endDate = null;
		
		ResponseMessage responseMessage=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// Fetch Data from rules and Call create task for DOP
			itaDOPActionDto = (List<ITADOPActionDto>) itaRulesServiceFacadeLocal.getITARulesByType(MurphyConstant.ITADOPType);
			 if (!ServicesUtil.isEmpty(itaDOPActionDto)) {
				 for (ITADOPActionDto obj : itaDOPActionDto) {
					 Tier = obj.getTierLevel();
					 barrels = obj.getActualBO();
					 classification= obj.getTaskClassification();
					 subClassification = obj.getTaskSubClassification();
					 STSSV_Rules = obj.getSsvStatus();
					 deviation = obj.getPercentageDeviation();
					 taskType = obj.getTypeOfTaskToBeCreated();
					 toTime = obj.getTo();
					 fromTime = obj.getFrom();
					 
					 Date start = new Date();
					 
					 if(toTime == runtime) {
						 if(i < 1){
							 startDate = sdf.format(start) +" "+fromTime+ ":00:00";
							 endDate = sdf.format(start)+" "+toTime+ ":00:00";
							// Fetch Canary Data and create HashMap of muwi,STSSV
						    JSONObject jsonObject= getCanaryData(startDate,endDate);
							createHashMapCanary(jsonObject);
							i++;
						 }
					    // Call create task
						responseMessage = productionVarianceDao.createItaTaskDop(endDate, Tier, barrels,
						           classification, subClassification, STSSV_Rules.trim(), deviation, taskType,muwiSTSSVMap);
					 }
				 }
		     }
		}catch (Exception e) {
			logger.error("[ItaFacade][createDopTask][error] : " + e.getMessage());
		}
		finally{
			if(!ServicesUtil.isEmpty(getUserToken()))
			{
				canaryStagingScheduler.revokeUserToken(getUserToken());
				logger.error("User token revoked");
			}
			else
				logger.error("User token is null");
		}
		return responseMessage;
	}
	
	

	// Get user token for connecting to Canary
	public String getUserToken() {
		try {

			String username = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF);
			String password = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF);
			String userTokenPayload = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\","
					+ "\"timeZone\":\"" + MurphyConstant.CANARY_TIMEZONE + "\",\"application\":\""
					+ MurphyConstant.CANARY_APP + "\"}";

			String url = MurphyConstant.CANARY_API_HOST + "api/v1/getUserToken";
			String httpMethod = "POST";
			JSONObject canaryResponseObject = RestUtil.callRest(url, userTokenPayload, httpMethod, username, password);
			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("userToken")) {
				JSONParser parser = new JSONParser();
				org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser
						.parse(canaryResponseObject.toString());

				return (String) json.get("userToken");
			} else
				logger.error("[ItaFacade][getUserToken][Error]: User Token Not Generated");
		} catch (Exception e) {
			logger.error("[ItaFacade][getUserToken][error]" + e.getMessage());
		}
		return null;
	}

	// Fetch Data for canary
	public JSONObject getCanaryData(String startTime, String endTime) {
		
		JSONObject responseATS = null;
		try{
		Set<String> wellMuwiList = wellMuwiDao.getMuwi();
		JSONArray jsonArray = new JSONArray();
		for (String wellMuwi : wellMuwiList) {
			jsonArray.put("MUWI_Prod." + wellMuwi + "." + MurphyConstant.ssvCanaryTag);

		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf1.parse(startTime);
		Date endDate = sdf1.parse(endTime);
		
		String  startCurrentTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(startDate, null, "",
		    "", "", MurphyConstant.DATEFORMAT_FOR_CANARY);
		 String endTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(endDate, null, "",
		    "", "", MurphyConstant.DATEFORMAT_FOR_CANARY);
		 
		 com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
		 
		String payload = "{\"userToken\":\"" + getUserToken() + "\"" + ",\"tags\":" + jsonArray + ",\"startTime\":\""
				+ startCurrentTimeCanaryFormat + "\"" + ",\"endTime\":\"" + endTimeCanaryFormat + "\"" + ",\"aggregateInterval\":\"" + "0:01:00:00" + "\""
				+ ",\"continuation\":" + null
				+ ",\"maxSize\":" + 10000 + "}";
		logger.error("ItaFacade PAYLOAD:"+": "+payload);
		String url = MurphyConstant.CANARY_API_HOST + "api/v1/getTagData";
		responseATS = RestUtil.callRestforATS(url, payload, MurphyConstant.HTTP_METHOD_POST);
		logger.error("ItaFacade json obejct: "+responseATS);
		}
		catch(Exception e){
			logger.error("[ItaFacade][getCanaryData]][error]" + e.getMessage());
		}
		return responseATS;
	}
	
	public HashMap<String, String> createHashMapCanary(JSONObject jsonObject){
		try {
			JsonNode node;
			if (!ServicesUtil.isEmpty(jsonObject) && jsonObject.toString().contains("data")) {
				JSONParser parser = new JSONParser();
				org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(jsonObject.toString());
				node = new ObjectMapper().readTree(json.toString());
				logger.error("[ItaFacade][createHashMapCanary][Entered here]");
				
			    Iterator<String> fieldNamesIterator = node.get("data").fieldNames();
			    muwiSTSSVMap = new HashMap<>();
		        while (fieldNamesIterator.hasNext()) {
				    String fieldName = fieldNamesIterator.next();
				    String wellMuwi = fieldName.split("\\.")[1];
				    String STSSV = null;
				    int arraySize=node.get("data").get(fieldName).size();
				    if(!ServicesUtil.isEmpty(node.get("data").get(fieldName)) && arraySize>0){
				    	// Get STSSV data from Canary
				    	STSSV = node.get("data").get(fieldName).get(0).get(1).asText();
				    	// When more than one value is coming, check if all data is same
				    	for(int i=1; i< arraySize; i++){
				    		String STSSV_Check = node.get("data").get(fieldName).get(i).get(1).asText();
				    		// If different status are coming in consider it as "Not Applicable" for task creation
				    		if(!STSSV_Check.equalsIgnoreCase(STSSV)){
				    			STSSV = "Not Applicable";
				    			break;
				    		}
				    	}
				    }
				    else{
				    	// Considering [] as as "Applicable" for task creation
				    	STSSV = "Applicable";
				    }
				    muwiSTSSVMap.put(wellMuwi, STSSV.trim());
		        }
			}else
				logger.error("JSON Object Returned from Canary is Empty");
		}
		catch(Exception e){
			logger.error("[ItaFacade][createHashMapCanary][error]" + e.getMessage());
		}
		return muwiSTSSVMap;
	}
	
    // Create task for ITA -> WaterOilCarryOverTask
		@SuppressWarnings("unchecked")
		public ResponseMessage createWaterOilCarryOverTask() {

			List<ITAWaterOilActionDto> itaWaterOilActionDto = new ArrayList<ITAWaterOilActionDto>();
			// Rules values
			String taskType = null, tier = null;
			String classification = null, subClassification = null;
			double oilThreshold, waterThreshold;
			String startTimeToday=null, endTimeToday = null, startTimeYesterday= null, endTimeYesterday = null;
			String startTime7daysAvg = null, endTime7daysAvg = null;
				
			ResponseMessage responseMessage=null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {			
				 // Fetch Canary Data and create HashMap of (muwi+type),typeValue for today
				 String[] canaryParam = {"QTOILD","QTH2OD"};
				 Calendar calendar = Calendar.getInstance();
				 calendar.add(Calendar.DATE, -1);
				 startTimeToday = sdf.format(calendar.getTime()) +" "+"07:00:00";
				 endTimeToday = sdf.format(calendar.getTime())+" "+"07:15:00";
//				 startTimeToday = "2020-04-15 02:00:00";
//				 endTimeToday = "2020-04-15 02:15:00"; 

				 JSONObject jsonObjectToday= getCanaryDataforWaterOil(startTimeToday,endTimeToday,canaryParam,"Maximum","0:00:15:00");
				 this.todayMap = createHashMapforCarryOver(jsonObjectToday);
				
				 // Fetch Canary Data and create HashMap of (muwi+type),typeValue for yesterday
				 Calendar cal = Calendar.getInstance();
				 cal.add(Calendar.DATE, -2);
				 startTimeYesterday = sdf.format(cal.getTime()) +" "+"07:00:00";
				 endTimeYesterday = sdf.format(cal.getTime())+" "+"07:15:00";
//				 startTimeYesterday = "2020-04-14 02:00:00";
//				 endTimeYesterday = "2020-04-14 02:15:00";
				 
				 JSONObject jsonObjectYesterday= getCanaryDataforWaterOil(startTimeYesterday,endTimeYesterday,canaryParam,"Maximum","0:00:15:00");
				 this.yesterdayMap = createHashMapforCarryOver(jsonObjectYesterday);
				 
				 // Fetch Canary Data and create HashMap of (muwi+type),typeValue for last 7 day average of oil
				 String [] canaryParamAvg = {"QTOILD"};
				 Calendar cal1 = Calendar.getInstance();
				 cal1.add(Calendar.DATE, -8);
				 startTime7daysAvg = startTimeToday;
				 endTime7daysAvg = sdf.format(cal1.getTime())+" "+"07:00:00";
				 //endTime7daysAvg = "2020-04-08 02:00:00";
				 
				 JSONObject jsonObject7daysAvg = getCanaryDataforWaterOil(startTime7daysAvg,endTime7daysAvg,canaryParamAvg,"Average","7:00:00:00");
				 this.daysAvgMap = createHashMapforCarryOver(jsonObject7daysAvg);
				 
				// Fetch Data from rules and Call create task for CarryOver
				itaWaterOilActionDto = (List<ITAWaterOilActionDto>) itaRulesServiceFacadeLocal.getITARulesByType(MurphyConstant.ITAWaterOilCarryOver);
				 if (!ServicesUtil.isEmpty(itaWaterOilActionDto)) {
					 for (ITAWaterOilActionDto obj : itaWaterOilActionDto) {
						 oilThreshold = obj.getOilThreshold();
						 waterThreshold = obj.getWaterThreshold();
						 classification= obj.getTaskClassification();
						 subClassification = obj.getTaskSubClassification();
						 taskType = obj.getTaskToBeCreated();
						 tier = obj.getTier();
						
					    // Call create task
						 responseMessage = itaWaterOilDao.createItaWaterOil(tier,oilThreshold,waterThreshold,todayMap,yesterdayMap,
								 daysAvgMap,classification, subClassification,taskType);		
					 }
			     }
			}catch (Exception e) {
				logger.error("[ItaFacade][createWaterOilCarryOverTask][error] : " + e.getMessage());
			}
			finally{
				if(!ServicesUtil.isEmpty(getUserToken()))
				{
					canaryStagingScheduler.revokeUserToken(getUserToken());
					logger.error("User token revoked");
				}
				else
					logger.error("User token is null");
			}
			return responseMessage;
		}
		
		// Fetch Data from canary
		public JSONObject getCanaryDataforWaterOil(String startTime, String endTime, String[] canaryParam, 
				String aggregateName, String aggregateInterval) {
			
			JSONObject responseATS = null;
			try{
			//Set<String> wellMuwiList = new HashSet<String>();
			//wellMuwiList.add("9264212700000000KON0162H1");	
			Set<String> wellMuwiList = wellMuwiDao.getMuwi();			
			JSONArray jsonArray = new JSONArray();
			for (String wellMuwi : wellMuwiList) {
				for (String param : canaryParam) {
					jsonArray.put("MUWI_Prod." + wellMuwi + "." + param);
				}
			}
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = sdf1.parse(startTime);
			Date endDate = sdf1.parse(endTime);
			
			String  startCurrentTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(startDate, null,"",
			   "", "", MurphyConstant.DATEFORMAT_FOR_CANARY);
			 String endTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(endDate, null,"",
			    "", "", MurphyConstant.DATEFORMAT_FOR_CANARY);
			 
			 com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			 
			String payload = "{\"userToken\":\"" + getUserToken() + "\"" + ",\"tags\":" + jsonArray + ",\"startTime\":\""
					+ startCurrentTimeCanaryFormat + "\"" + ",\"endTime\":\"" + endTimeCanaryFormat + "\"" + ",\"aggregateName\":\"" + aggregateName + "\""
					+ ",\"aggregateInterval\":\"" + aggregateInterval + "\"" + ",\"continuation\":" + null
					+ ",\"maxSize\":" + 10000 + "}";
			logger.error("ItaFacade PAYLOAD [getCanaryDataforWaterOil]:"+": "+payload);
			String url = MurphyConstant.CANARY_API_HOST + "api/v1/getTagData";
			responseATS = RestUtil.callRestforATS(url, payload, MurphyConstant.HTTP_METHOD_POST);
			logger.error("[getCanaryDataforWaterOil] ItaFacade json obejct : "+responseATS);
			}
			catch(Exception e){
				logger.error("[ItaFacade][getCanaryDataforWaterOil]][error]" + e.getMessage());
			}
			return responseATS;
		}
		
		public HashMap<String, Double> createHashMapforCarryOver(JSONObject jsonObject){
			HashMap<String, Double> map = null;
			try {
				JsonNode node;
				if (!ServicesUtil.isEmpty(jsonObject) && jsonObject.toString().contains("data")) {
					JSONParser parser = new JSONParser();
					org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(jsonObject.toString());
					node = new ObjectMapper().readTree(json.toString());
					
				    Iterator<String> fieldNamesIterator = node.get("data").fieldNames();
				    map = new HashMap<>();
			        while (fieldNamesIterator.hasNext()) {
					    String fieldName = fieldNamesIterator.next();
					    String muwi_tag = fieldName.split("\\.")[1] + "_" + fieldName.split("\\.")[2];
					    Double barrels = null;
					    int arraySize=node.get("data").get(fieldName).size();
					    if(!ServicesUtil.isEmpty(node.get("data").get(fieldName)) && arraySize > 0){
					    	// Get barrels of oil/water data from Canary
					    	if(!node.get("data").get(fieldName).get(0).get(1).isNull()){
						    	barrels = node.get("data").get(fieldName).get(0).get(1).asDouble();
						    	map.put(muwi_tag, barrels);
					    	}
					    }
			        }
				}else
					logger.error("[createHashMapforCarryOver][JSON Object Returned from Canary is Empty]");
			}
			catch(Exception e){
				logger.error("[ItaFacade][createHashMapforCarryOver][error]" + e.getMessage());
			}
			return map;
		}
		
		@SuppressWarnings("unchecked")
		@Override
	public ResponseMessage createGasBlowBy(com.murphy.integration.dto.ResponseMessage responseMessage,
			String taskClassification, String taskSubClassification, String taskToBeCreated, Double comparisonRatio) {
		ResponseMessage response = new ResponseMessage();
		String description = "This task is created for Gas BlowBy";
		Map<String, String> locCodeMuwiMap = new HashMap<>();
		List<String> gasBlowByMuwiList = new ArrayList<>();
		// Map<String, String> configMap;
		GasBlowByDto blowByDto;
		Map<Integer, GasBlowByDto> oilGasMap = (Map<Integer, GasBlowByDto>) responseMessage.getData();
		try {

			if (!ServicesUtil.isEmpty(oilGasMap)) {

				// Checking for Gas blow by
				for (Map.Entry<Integer, GasBlowByDto> map : oilGasMap.entrySet()) {
					blowByDto = map.getValue();

					if (!ServicesUtil.isEmpty(blowByDto.getDailyOilGasRatio())) {

						if ((blowByDto.getYearlyOilGasRatio() / blowByDto.getDailyOilGasRatio()) < comparisonRatio) {

							logger.error("DailyOil-" + blowByDto.getEstOilVol() + "||DailyGas-"
									+ blowByDto.getEstGasVol() + "||YearlyOil-" + blowByDto.getTotEstOilVol()
									+ "||YearlyGas-" + blowByDto.getTotEstGasVol() + "||AvgYearlyGas "
									+ blowByDto.getAvgYearlyGas() + "||DailyOGRatio-" + blowByDto.getDailyOilGasRatio()
									+ "||YearlyOGRatio-" + blowByDto.getYearlyOilGasRatio());

							gasBlowByMuwiList.add(blowByDto.getMuwiId());
						}

					}

				}

				logger.error("Count for gasblowbys---->" + gasBlowByMuwiList.size());

				if (!gasBlowByMuwiList.isEmpty()) {
					// Getting location_code for gasblowby muwis//
					locCodeMuwiMap = itaWaterOilDao.getLocCode(gasBlowByMuwiList);
					List<String> locCodeList = locCodeMuwiMap.keySet().stream().collect(Collectors.toList());

					// Creating task for each gasblowby
					for (int i = 0; i < locCodeList.size(); i++) {
						if (!ServicesUtil.isEmpty(locCodeList.get(i))) {
							response = itaWaterOilDao.createTask(locCodeList.get(i), taskClassification,
									taskSubClassification, taskToBeCreated, description);
						} else {
							logger.error("No loc code found for muwi " + locCodeMuwiMap.get(locCodeList.get(i)));
						}
					}
					response.setMessage("GasBlowby tasks created");
					response.setStatus(MurphyConstant.SUCCESS);
					response.setStatusCode(MurphyConstant.CODE_SUCCESS);

				} else {
					response.setMessage("No GasBlowbys have occured");
					response.setStatus(MurphyConstant.SUCCESS);
					response.setStatusCode(MurphyConstant.CODE_SUCCESS);
				}

			} else {
				response.setMessage("No GasBlowbys have occured");
				response.setStatus(MurphyConstant.SUCCESS);
				response.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}

		} catch (Exception e) {

			logger.error("[ItaFacade][createGasBlowBy] [error] " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
}


