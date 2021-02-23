package com.murphy.taskmgmt.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Rashmendra.Sai
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphy.geotab.Coordinates;
import com.murphy.integration.util.ServicesUtil;
import com.murphy.taskmgmt.dao.ATSTaskAssignmentDao;
import com.murphy.taskmgmt.dao.AutoTaskSchedulingDao;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.ProductionLocationDao;
import com.murphy.taskmgmt.dao.StopTimeDao;
import com.murphy.taskmgmt.dto.ATSOpertaorDetailsDto;
import com.murphy.taskmgmt.dto.ATSTaskAssginmentDto;
import com.murphy.taskmgmt.dto.ATSTaskListDto;
import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.LocationDistancesDto;
import com.murphy.taskmgmt.dto.StopTimeDto;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.scheduler.CanaryStagingScheduler;
import com.murphy.taskmgmt.service.interfaces.AutoTaskSchedulingFacadeLocal;
import com.murphy.taskmgmt.util.ArcGISUtil;
import com.murphy.taskmgmt.util.GeoTabUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;

@Service("AutoTaskSchedulingFacade")
public class AutoTaskSchedulingFacade implements AutoTaskSchedulingFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(AutoTaskSchedulingFacade.class);

	@Autowired
	ConfigDao configDao;

	@Autowired
	AutoTaskSchedulingDao atsDao;

	@Autowired
	StopTimeDao stDao;

	@Autowired
	HierarchyDao hierarchyDao;

	@Autowired
	ATSTaskAssignmentDao atsTaskAssignmentDao;
	
	@Autowired
	CanaryStagingScheduler canaryStagingSched;
	
	@Autowired
	ProductionLocationDao productionLocationDao;

	// Key: Muwi, Value: Oil Prod
	private HashMap<String, Double> avg3HourMap;
	private HashMap<String, Double> avg3daysMap;

	private List<ATSOpertaorDetailsDto> opertaorDetailsList = new ArrayList<>();

	public AutoTaskSchedulingFacade() {
	}

	/**
	 * To fetch User Token
	 * 
	 * @return UserToken
	 */
	public String getUserToken() {
		try {
			ServicesUtil.unSetupSOCKS();
//			String username = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF);
			String username = configDao.getConfigurationByRef(MurphyConstant.CRT_CANARY_API_USERID_REF);
			String password = configDao.getConfigurationByRef(MurphyConstant.CRT_CANARY_API_PASSWORD_REF);
			String app = configDao.getConfigurationByRef(MurphyConstant.CRT_CANARY_APP);
			String crtCanaryAPIHost = configDao.getConfigurationByRef(MurphyConstant.CRT_CANARY_API_HOST);
			logger.error("crtCanaryAPIHost : "+crtCanaryAPIHost);
//			String password = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF);
			String userTokenPayload = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\","
					+ "\"timeZone\":\"" + MurphyConstant.CANARY_TIMEZONE + "\",\"application\":\""
					+ app + "\"}";
			String url = crtCanaryAPIHost + "api/v1/getUserToken";
			String httpMethod = "POST";
			logger.error("UserTokenPayLoad: "+userTokenPayload);
			JSONObject canaryResponseObject = RestUtil.callRest(url, userTokenPayload, httpMethod, username, password);
			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("userToken")) {
				logger.error("Canary Response Object for Token : "+canaryResponseObject);
				JSONParser parser = new JSONParser();
				org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser
						.parse(canaryResponseObject.toString());
				//logger.error("[getUserToken][User Token] : " + (String) json.get("userToken"));
				return (String) json.get("userToken");
			} else
				logger.error("[AutoTaskSchedulingFacade][getUserToken][Error]: User Token Not Generated");
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingFacade][getUserToken][error]" + e);
			logger.error("[AutoTaskSchedulingFacade][getUserToken][Message Error]" + e.getMessage());
		}
		return null;
	}

	/**
	 * To prepare Parameters for Canary API call.
	 * 
	 * @return
	 */
	public String serviceHitCanary() {
		String responseString = MurphyConstant.FAILURE;
		if (!ServicesUtil.isEmpty(configDao.getConfigurationByRef(MurphyConstant.ATSRunningFlag)) && configDao
				.getConfigurationByRef(MurphyConstant.ATSRunningFlag).equalsIgnoreCase(MurphyConstant.TRUE)) {
			String userToken = getUserToken();
			try {
				if (!ServicesUtil.isEmpty(userToken)) {
					logger.error("User Token Generated is : "+userToken);
					HashMap<String, ATSTaskListDto> taskMapping = new HashMap<>();
					List<String> taskIDList = new ArrayList<String>();
					List<String> taskIDExceptMuwi = new ArrayList<String>();
					// Final sortedTaskMapping Key: TaskID, Values:
					// ATSTaskListDto
					LinkedHashMap<String, ATSTaskListDto> sortedTaskMapping = new LinkedHashMap<String, ATSTaskListDto>();
					LinkedHashMap<String, ATSTaskListDto> taskMappingExceptMuwi = new LinkedHashMap<>();

					// Storing Details of task from ITA
					List<ATSTaskListDto> dtoList = atsDao.getTaskIdsforATSVOne();

					if(!ServicesUtil.isEmpty(dtoList)){
						// New list which have muwi but no duplicate Muwi for
						// different
						// taskID
						HashSet<String> muwiSet = new HashSet<>();
						for (ATSTaskListDto at : dtoList) {
							if (!ServicesUtil.isEmpty(at.getMuwiID())) {
								muwiSet.add(at.getMuwiID());
							}
						}

						// Considering only the one with MUWI ID for canary
						JSONArray jsonArray = new JSONArray();
						for (String muwi : muwiSet){
							jsonArray.put("MUWI_Prod." + muwi + "." + MurphyConstant.CANARY_PARAM_PV[0]);
						}
						logger.error("JSONARRAY : "+jsonArray);
						avg3daysMap = prepParamsForDayMapResponse(avg3HourMap, avg3daysMap, userToken, jsonArray);
						if (!ServicesUtil.isEmpty(avg3daysMap)) {
							avg3HourMap = prepPaaramForHourMapResponse(avg3HourMap, avg3daysMap, userToken, jsonArray);
						}
						logger.error("avg3daysMap Line 116 : " + avg3daysMap + " [avg3HourMap] : " + avg3HourMap);
//						if (!ServicesUtil.isEmpty(avg3HourMap)) {
							// Key: Muwi , Value : Decline rate
							HashMap<String, Long> riskCorresWell = new HashMap<>();
							
						if (!ServicesUtil.isEmpty(avg3HourMap)) {
							for (Entry<String, Double> day : avg3daysMap.entrySet()) {
								for (Entry<String, Double> hour : avg3HourMap.entrySet()) {
									if (day.getKey().equalsIgnoreCase(hour.getKey())) {
										// Risk BBl calculation for well
										riskCorresWell.put(day.getKey().split("\\.")[1],
												Math.round(((day.getValue() - hour.getValue()) * 100) / 100));
										break;
									}
								}
							}

						}
							Double sumRisk = 0d;
							

							// Inserting risk to the corresponding wells
							Set<String> riskCorresWellKeySet = riskCorresWell.keySet();
							
							for (ATSTaskListDto at : dtoList) {
								
								
								String riskBblMuwi=getRiskBblMuwi(riskCorresWellKeySet,at.getMuwiID());
								if(!ServicesUtil.isEmpty(riskBblMuwi)){
									at.setRiskBBL(riskCorresWell.get(riskBblMuwi));
									taskIDList.add(at.getTaskId());
									taskMapping.put(at.getTaskId(), at);
								}else{
									taskIDExceptMuwi.add(at.getTaskId());
									taskMappingExceptMuwi.put(at.getTaskId(), at);
								}
								
								
							}
							
							logger.error("dtoList line 195 " + dtoList);

							// sortedRiskMap Desc Order- Key : MUWI , Value : Risk
							LinkedHashMap<String, Long> sortedRiskMap = new LinkedHashMap<>();
							if(!ServicesUtil.isEmpty(riskCorresWell)&&riskCorresWell.size()>0){
								riskCorresWell.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
								.forEachOrdered(x -> sortedRiskMap.put(x.getKey(), x.getValue()));
								logger.error("SortedRiskMap Line 135 : " + sortedRiskMap);
								
								// Classification and SubClassification of tasks which
								// have
								// MuwiID
								LinkedHashMap<String, List<String>> tasksCategory = atsDao.getCategoryforTaskIds(taskIDList);
								
								// Sorted Whole Map Based on Risk BBL
								sortedTaskMapping = mappedTaskIDDetails(tasksCategory, sortedRiskMap, taskMapping,
										sortedTaskMapping);
								
								logger.error("sortedTaskMapping Line 210 : " + sortedTaskMapping);
							}
							
							// SortedTaskMapping contains all task with or without
							// muwi
							logger.error("taskMappingExceptMuwi.values "+taskIDExceptMuwi);
							if(!ServicesUtil.isEmpty(taskMappingExceptMuwi) && !ServicesUtil.isEmpty(taskIDExceptMuwi)){
								sortedTaskMapping.putAll(taskMappingExceptMuwi);
								// Category of tasks which do not have MuwiID or RiskBbl
								LinkedHashMap<String, List<String>> tasksCategoryWithoutMuwi = atsDao
										.getCategoryforTaskIds(taskIDExceptMuwi);
								for (Entry<String, List<String>> i : tasksCategoryWithoutMuwi.entrySet()) {
									for (Entry<String, ATSTaskListDto> j : taskMappingExceptMuwi.entrySet()) {
										if (i.getKey().equalsIgnoreCase(j.getKey())) {
											j.getValue().setClassification(i.getValue().get(0));
											j.getValue().setSubClassification(i.getValue().get(1));
											break;
										}
									}
								}
							}
							

							// Distance from respective Central Facility
							sortedTaskMapping = distanceFromFixedCF(sortedTaskMapping);

							logger.error("sortedTaskMapping Line 230 : " + sortedTaskMapping);

							// Employees present in morning shift based on
							// proficiency
							//opertaorDetailsList : List of Dto ; Conatins emailId,designation,DefaultstartTimeUTC,null loc Code,fieldDesignation
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
							calendar.set(Calendar.HOUR_OF_DAY, 5);
							calendar.set(Calendar.MINUTE, 00);
							calendar.set(Calendar.SECOND, 00);
							Map<String, List<String>> operatorToRouteMap=new HashMap<>();
							Map<String, List<String>> empByShift = stDao.getEmpByShift(calendar,operatorToRouteMap);
							logger.error("Operators Routes "+operatorToRouteMap);
							if (!ServicesUtil.isEmpty(empByShift)) {
								// CST :5am , UTC: 10am
								Calendar defaultStartTime = Calendar.getInstance();
								defaultStartTime.set(Calendar.HOUR_OF_DAY, 10);
								defaultStartTime.set(Calendar.MINUTE, 00);
								defaultStartTime.set(Calendar.SECOND, 00);
//								Date defaultStartInUTC = null;
								// Setting opertaorDetailsList DTO default Start
								// prevLoc= null;
								for (Entry<String, List<String>> k : empByShift.entrySet()) {
									for (String s : k.getValue()) {
										ATSOpertaorDetailsDto dto = new ATSOpertaorDetailsDto();
//										defaultStartInUTC = new Date();
										dto.setEmailID(s);
										dto.setDesignation(k.getKey());
										/*defaultStartInUTC = ServicesUtil.convertFromZoneToZone(null,
												defaultStartTime.getTime(), MurphyConstant.UTC_ZONE,
												MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
												MurphyConstant.DATE_DISPLAY_FORMAT);*/
										dto.setStartTimeForNextTask(defaultStartTime.getTime());
										dto.setUserFieldDesignation(atsDao.getFieldDesignationOfOperators(s));
										dto.setPrevTaskLocCode(null);
										dto.setRoutes(operatorToRouteMap.get(s));
										this.opertaorDetailsList.add(dto);
									}
								}
								logger.error("Intial before load check opertaorDetailsList : " + opertaorDetailsList);

								Map<String, List<String>> revisedEmpByShiftMap = atsDao.findLoadOfOperator(empByShift,
										opertaorDetailsList);
								logger.error("opertaorDetailsList after workload " + opertaorDetailsList);
								
//								LinkedHashMap<String, List<ATSOpertaorDetailsDto>> opAvailMap = atsDao.getAvailabilityOfOperator(empByShift);

								// For Assigning task to operators
								List<ATSTaskAssginmentDto> assignmentListDto = operatorStopTimeByClassification(
										opertaorDetailsList, sortedTaskMapping);
								if (!ServicesUtil.isEmpty(assignmentListDto)) {
									logger.error("assignmentListDto Line 202 : " + assignmentListDto);
									responseString = MurphyConstant.SUCCESS;
								}else{
									logger.error("AssignmentListDto is empty");
								}
								
								//assignTaskNew(opAvailMap,sortedTaskMapping);
								
							} else
								logger.error("[AutoTaskSchedulingFacade][No Operators present in the shift]");
//						} else
//							logger.error("[AutoTaskSchedulingFacade][No Response from Canary]");
					}else
						logger.error("[AutoTaskSchedulingFacade][ATSTaskListDto is empty]");
				} else
					logger.error("[AutoTaskSchedulingFacade][serviceHitCanary] : User Token is null");
			} catch (Exception e) {
				logger.error("[AutoTaskSchedulingFacade][serviceHitCanary][error] " + e);
				logger.error(" [AutoTaskSchedulingFacade][serviceHitCanary][Error Message ] " + e.getMessage());
			}finally{
				if(!ServicesUtil.isEmpty(userToken)){
					logger.error("Revoking User Token for ATS : "+userToken);
					canaryStagingSched.revokeUserToken(userToken);
				}else 
					logger.error("No User Token present to revoke");
			}
		}
		return responseString;
	}

	private String getRiskBblMuwi(Set<String> riskCorresWellKeySet, String muwiID) {

		if (ServicesUtil.isEmpty(muwiID) && ServicesUtil.isEmpty(riskCorresWellKeySet)) {
			return null;
		}
		if (ServicesUtil.isEmpty(muwiID) || ServicesUtil.isEmpty(riskCorresWellKeySet)) {
			return null;
		}

		for (String muwi : riskCorresWellKeySet) {

			if (muwiID.equalsIgnoreCase(muwi)) {
				return muwi;
			}

		}
		return null;
	}

	public HashMap<String, Double> canaryAPIServiceCall(String userToken, String startTime, String endTime,
			String aggregateInterval, JSONArray jsonArray) {
		HashMap<String, Double> muwiCorresValue = null;
		try {
			int maxSize = 10000;
			String continuation = null;
			String payload = "{\"userToken\":\"" + userToken + "\"" + ",\"tags\":" + jsonArray + ",\"startTime\":\""
					+ startTime + "\"" + ",\"endTime\":\"" + endTime + "\"" + ",\"aggregateName\":\""
					+ MurphyConstant.AGGR_NAME_AVG + "\"" + ",\"aggregateInterval\":\"" + aggregateInterval + "\""
					+ ",\"continuation\":" + continuation + ",\"maxSize\":" + maxSize + "}";

			logger.error("payload: " + payload);
			String url = configDao.getConfigurationByRef(MurphyConstant.CRT_CANARY_API_HOST) + "api/v1/getTagData";

			JSONObject responseATS = RestUtil.callRestforATS(url, payload, MurphyConstant.HTTP_METHOD_POST);

			if (!ServicesUtil.isEmpty(responseATS) && responseATS.toString().contains("data")) {
				JSONParser parser = new JSONParser();
				org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(responseATS.toString());

				JsonNode node = new ObjectMapper().readTree(json.toString());
				Iterator<String> itera = node.get("data").fieldNames();
				muwiCorresValue = new HashMap<>();
				while (itera.hasNext()) {
					String fieldName = itera.next();
					int arraySize=node.get("data").get(fieldName).size();
					if(!ServicesUtil.isEmpty(node.get("data").get(fieldName)) && arraySize > 0){
						if(!node.get("data").get(fieldName).get(0).get(1).isNull()) 
						muwiCorresValue.put(fieldName, node.get("data").get(fieldName).get(0).get(1).asDouble());
					}
				}
				// logger.error("muwiCorresValue: " + muwiCorresValue);
			} else
				logger.error(
						"[AutoTaskSchedulingFacade][CanaryAPIServiceCall][EmptyResponse]: Canary API Response empty");
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingFacade][CanaryAPIServiceCall][error]" + e.getMessage());
		}
		return muwiCorresValue;
	}

	// Mapping TaskID to details of it except for the one without Muwi
	public LinkedHashMap<String, ATSTaskListDto> mappedTaskIDDetails(HashMap<String, List<String>> tasksCategory,
			LinkedHashMap<String, Long> sortedRiskMap, HashMap<String, ATSTaskListDto> taskMapping,
			LinkedHashMap<String, ATSTaskListDto> sortedTaskMapping) {

		try {
			// For Storing risk BBL to corresponding taskId
			for (Entry<String, Long> i : sortedRiskMap.entrySet()) {
				for (Entry<String, ATSTaskListDto> j : taskMapping.entrySet()) {
					if (!ServicesUtil.isEmpty(j.getValue().getMuwiID())) {
						if (i.getKey().equalsIgnoreCase(j.getValue().getMuwiID())) {
							j.getValue().setRiskBBL(i.getValue());
							break;
						}
					}
				}
			}
			// For Storing Categories corres taskId
			for (Entry<String, List<String>> i : tasksCategory.entrySet()) {
				for (Entry<String, ATSTaskListDto> j : taskMapping.entrySet()) {
					if (i.getKey().equalsIgnoreCase(j.getKey())) {
						j.getValue().setClassification(i.getValue().get(0));
						j.getValue().setSubClassification(i.getValue().get(1));
						break;
					}
				}
			}

			// SOC: Descending Sorting Main HashMap based on RiskBBL and put in
			// new Map
			List<Map.Entry<String, ATSTaskListDto>> list = new LinkedList<Map.Entry<String, ATSTaskListDto>>(
					taskMapping.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, ATSTaskListDto>>() {
				public int compare(Map.Entry<String, ATSTaskListDto> o1, Map.Entry<String, ATSTaskListDto> o2) {
					return (o2.getValue().getRiskBBL()).compareTo(o1.getValue().getRiskBBL());
				}
			});
			for (Map.Entry<String, ATSTaskListDto> i : list) {
				sortedTaskMapping.put(i.getKey(), i.getValue());
			}
			// EOC

		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingFacade][mappedTaskIDDetails][error]" + e.getMessage());
		}
		return sortedTaskMapping;
	}

	// Preparing parameters for Canary and returning response from Canary
	public HashMap<String, Double> prepParamsForDayMapResponse(HashMap<String, Double> avg3HourMap,
			HashMap<String, Double> avg3daysMap, String userToken, JSONArray jsonArray) {
		Calendar currentStart = null;
		String startCurrentTimeCanaryFormat = null;
		String endTimeCanaryFormat = null;
		Calendar endDate = null;
		String aggregateInterval = null;
		try {
			// Actual Average per hour for last 3 day
			currentStart = Calendar.getInstance();
			startCurrentTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(currentStart.getTime(), null, "",
					MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_FOR_CANARY);
			endDate = Calendar.getInstance();
			endDate.add(Calendar.DATE, -3);
			endTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(endDate.getTime(), null, "",
					MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_FOR_CANARY);
			aggregateInterval = "3:00:00:00";
			// StartTimeCanary=2019-08-31T06:06:57 CST Time
			this.avg3daysMap = canaryAPIServiceCall(userToken, startCurrentTimeCanaryFormat, endTimeCanaryFormat,
					aggregateInterval, jsonArray);

		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingFacade][prepParamsAndCanaryResponse][error]" + e.getMessage());
		}
		return this.avg3daysMap;
	}

	public HashMap<String, Double> prepPaaramForHourMapResponse(HashMap<String, Double> avg3HourMap,
			HashMap<String, Double> avg3daysMap, String userToken, JSONArray jsonArray) {
		Calendar currentStart = null;
		String startCurrentTimeCanaryFormat = null;
		String endTimeCanaryFormat = null;
		String aggregateInterval = null;
		Calendar current = null;
		try {
			currentStart = Calendar.getInstance();
			current = Calendar.getInstance();
			// Actual Average for last 3 hours of the current day
			current.add(Calendar.HOUR, -3);
			startCurrentTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(currentStart.getTime(), null, "",
					MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_FOR_CANARY);
			endTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(current.getTime(), null, "",
					MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_FOR_CANARY);
			aggregateInterval = "0:03:00:00";
			this.avg3HourMap = canaryAPIServiceCall(userToken, startCurrentTimeCanaryFormat, endTimeCanaryFormat,
					aggregateInterval, jsonArray);

		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingFacade][prepPaaramForHourMapResponse][error]" + e.getMessage());
		}
		return this.avg3HourMap;
	}

//	private HashMap<String, ATSTaskListDto> euDistance(HashMap<String, ATSTaskListDto> sortedTaskMapping) {
//		Double distance = Double.MAX_VALUE;
//		Coordinates coord = null;
//		try {
//			for (Entry<String, ATSTaskListDto> j : sortedTaskMapping.entrySet()) {
//				coord = new Coordinates();
//				coord.setLongitude(j.getValue().getFacilityLongCoord() - j.getValue().getWellLongCoord());
//				coord.setLatitude(j.getValue().getFacilityLatCoord() - j.getValue().getWellLatCoord());
//				distance = Math.sqrt((coord.getLatitude()) * (coord.getLatitude())
//						+ (coord.getLongitude()) * (coord.getLongitude()));
//				j.getValue().setDistanceFromFacility(distance);
//			}
//
//		} catch (Exception e) {
//			logger.error("[AutoTaskSchedulingFacade][euDistance][Exception] " + e.getMessage());
//		}
//		return sortedTaskMapping;
//	}

	// For Fetching the road travel time from fixed location(Briggs North CF) to
	// respective location
	public LinkedHashMap<String, ATSTaskListDto> distanceFromFixedCF(
			LinkedHashMap<String, ATSTaskListDto> sortedTaskMapping) {
		LocationDistancesDto locationDistanceDto = null;
		Double roadDriveTime = 1d;
		try {
			for (Entry<String, ATSTaskListDto> j : sortedTaskMapping.entrySet()) {
				Coordinates toCoordinate = hierarchyDao.getCoordByCode(j.getValue().getLocationCode());
				Coordinates fromCoordinate = hierarchyDao.getCoordByCode(j.getValue().getFacilityLocCode());
				// Coordinates fromCoordinate =
				// hierarchyDao.getCoordByCode(j.getValue().getKoneWestFacilityCode());
				if (!ServicesUtil.isEmpty(fromCoordinate) && !ServicesUtil.isEmpty(toCoordinate)) {
					try {
						ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(fromCoordinate, toCoordinate);
						if(!ServicesUtil.isEmpty(arcResponse)){
							if (arcResponse.getResponseMessage().getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
								locationDistanceDto = new LocationDistancesDto();
								if(!ServicesUtil.isEmpty(arcResponse.getTotalDriveTime())){
									locationDistanceDto.setRoadDriveTime(arcResponse.getTotalDriveTime());
									j.getValue().setRoadDriveTimeFromFixedCF(
											ServicesUtil.isEmpty(locationDistanceDto.getRoadDriveTime()) ? null
													: locationDistanceDto.getRoadDriveTime());
								}else {
									locationDistanceDto.setRoadDriveTime(roadDriveTime);
									logger.error("Road Drive Time from ArcGISUtil is coming null. So RoadDriveTime set is : "+locationDistanceDto.getRoadDriveTime());
									j.getValue().setRoadDriveTimeFromFixedCF(
											ServicesUtil.isEmpty(locationDistanceDto.getRoadDriveTime()) ? null
													: locationDistanceDto.getRoadDriveTime());
								}
							}
						} else{
							logger.error("ArcGISUtil arcResponse is empty ");
						}
					} catch (Exception e) {
						logger.error("[distanceFromFixedCF][ Inner Exception] " + e.getMessage());
					}
				}
			}

		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingFacade][distanceFromFixedCF][Outer Exception] " + e.getMessage());
		}
		logger.error("[distanceFromFixedCF][SortedTaskMapping] " + sortedTaskMapping);
		return sortedTaskMapping;
	}

	// Fetching operator StopTime for task and Assigning task and
	// route to operator
	public List<ATSTaskAssginmentDto> operatorStopTimeByClassification(List<ATSOpertaorDetailsDto> opertaorDetailsList,
			HashMap<String, ATSTaskListDto> sortedTaskMapping) {

		Map<String, String> locationCodeToCFMap = new HashMap<>();
		
		List<ATSTaskAssginmentDto> taskAssignDtoList = null;
		try {
			if (!ServicesUtil.isEmpty(opertaorDetailsList) && !ServicesUtil.isEmpty(sortedTaskMapping)) {
				// emailSeqMap = email : sequence
				HashMap<String, Integer> emailSeqMap = new HashMap<>();
				// Task AssignmentDto List
				taskAssignDtoList = new ArrayList<>();
				// CST :10am , UTC: 3pm
				Calendar maxTaskAssignmentTime = Calendar.getInstance();
				maxTaskAssignmentTime.set(Calendar.HOUR_OF_DAY, 15);
				maxTaskAssignmentTime.set(Calendar.MINUTE, 00);
				maxTaskAssignmentTime.set(Calendar.SECOND, 00);

				for (Entry<String, ATSTaskListDto> atsTask : sortedTaskMapping.entrySet()) {
					ATSTaskAssginmentDto assignmentDto = new ATSTaskAssginmentDto();
					StopTimeDto stdto = atsDao.getStopTimeBasedOnClassification(atsTask.getValue().getClassification(),
							atsTask.getValue().getSubClassification());

					Double roadDriveTimeBetweenLoc = 0d;
					Date startTime = null;
					Date finishTime = null;
					Date prevFinishTime = null;
					int resolveTaskTime = 0;

					// li : {emailID,startTime,FinishTime,locCode,locText}
					List<Object> li = new ArrayList<>();
					// Boolean check if task can be assigned in permissible
					// time
					boolean withinPermisibleTime = false;
					
					
					String taskCfLocationCode=getCfLocationCodeByLocationCode(atsTask.getValue().getLocationCode(),locationCodeToCFMap);
					taskCfLocationCode=ServicesUtil.isEmpty(taskCfLocationCode)?null:taskCfLocationCode.trim();
					
					//Check Operator permissible by Location Code
					withinPermisibleTime = checkOperatorPemissibleByLocation(maxTaskAssignmentTime, atsTask, stdto,
							finishTime, prevFinishTime, resolveTaskTime, li, withinPermisibleTime,true,taskCfLocationCode);
					
					if(!withinPermisibleTime){
						withinPermisibleTime = checkOperatorPemissibleByLocation(maxTaskAssignmentTime, atsTask, stdto,
								finishTime, prevFinishTime, resolveTaskTime, li, withinPermisibleTime,false,taskCfLocationCode);
					}
					
					
					
					if (withinPermisibleTime) {
						List<ATSOpertaorDetailsDto> newOpertaorDetailsList = opertaorDetailsList;
						for (ATSOpertaorDetailsDto newOp : newOpertaorDetailsList) {
							if (newOp.getEmailID().equalsIgnoreCase((String) li.get(0))) {
								int index = newOpertaorDetailsList.indexOf(newOp);
								opertaorDetailsList.get(index).setStartTimeForNextTask((Date) li.get(2));
								opertaorDetailsList.get(index).setPrevTaskLocCode(atsTask.getValue().getLocationCode());

							}
						}
						if (emailSeqMap.containsKey(((String) li.get(0)).trim())) {
							int presentSeq = emailSeqMap.get((String) li.get(0));
							emailSeqMap.put(((String) li.get(0)).trim(), presentSeq + 1);
						} else {
							emailSeqMap.put(((String) li.get(0)).trim(), 1);
						}

						for (Entry<String, Integer> e : emailSeqMap.entrySet())
							assignmentDto.setSequence(e.getValue());

						// Assigning to Task Assignment dto
						assignmentDto.setTaskID(atsTask.getValue().getTaskId());
						assignmentDto.setEmailID((String) li.get(0));
						assignmentDto.setStartTimeOfTask((Date) li.get(1));
						assignmentDto.setEndTimeOfTask((Date) li.get(2));
						assignmentDto.setLocCode((String) li.get(3));
						assignmentDto.setLocText((String) li.get(4));
						assignmentDto.setWellTier(hierarchyDao.getTierByCode((String) li.get(3)));
						assignmentDto.setRisk((BigDecimal) li.get(5));
						assignmentDto.setStopTimeForTask((int) li.get(6));
						assignmentDto.setUserDesignation((String) li.get(7));
						assignmentDto.setRoadDriveTime((BigDecimal) li.get(8));
						assignmentDto.setHasAssigned("FALSE");

						taskAssignDtoList.add(assignmentDto);

						// Inserting in ATS_TASK_ASSIGNMENT table
						try {
							atsTaskAssignmentDao.insertTasks(assignmentDto);
						} catch (Exception e) {
							logger.error(
									"[AutoTaskSchedulingFacade][INSERTING DATA] Exception while inserting data in ATS_TASK_ASSIGNMENT "
											+ e.getMessage());
						}
					}else{
						logger.error("[AutoTaskSchedulingFacade][operatorStopTimeByClassification] Not with In Permissible");
					}
				}

			}
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingFacade][operatorStopTimeByClassification][Exception] " + e.getMessage());
		}
		return taskAssignDtoList;

	}

	private String getCfLocationCodeByLocationCode(String locationCode, Map<String, String> locationCodeToCFMap)
			throws NoResultFault, InvalidInputFault {

		if (!locationCodeToCFMap.containsKey(locationCode)) {
			locationCodeToCFMap.put(locationCode, productionLocationDao.getCFLocationCode(locationCode));
		}

		return locationCodeToCFMap.get(locationCode);
	}

	private boolean checkOperatorPemissibleByLocation(Calendar maxTaskAssignmentTime,
			Entry<String, ATSTaskListDto> atsTask, StopTimeDto stdto, Date finishTime, Date prevFinishTime,
			int resolveTaskTime, List<Object> li, boolean withinPermisibleTime, boolean isByLocation, String taskCfLocationCode) {

		for (ATSOpertaorDetailsDto op : this.opertaorDetailsList) {
			Double totalDuration = Double.MAX_VALUE;
			// if operator for same field or not
			if (!ServicesUtil.isEmpty(op.getUserFieldDesignation())) {
				logger.error("Operator designation ,task Field "+op.getUserFieldDesignation() + ","+atsTask.getValue().getFieldName());
				if (op.getUserFieldDesignation().contains(atsTask.getValue().getFieldName())) {

					if (isByLocation) {
						logger.error("Operator routes,taskCftext:"+op.getRoutes() +"#"+taskCfLocationCode);
						if (op.getRoutes().contains(taskCfLocationCode)) {

							withinPermisibleTime = checkOperatorIsPermissible(maxTaskAssignmentTime, atsTask, stdto,
									finishTime, prevFinishTime, resolveTaskTime, li, withinPermisibleTime, op,
									totalDuration);
						}
					} else {
						withinPermisibleTime = checkOperatorIsPermissible(maxTaskAssignmentTime, atsTask, stdto,
								finishTime, prevFinishTime, resolveTaskTime, li, withinPermisibleTime, op,
								totalDuration);
					}

				} else {
					logger.error(
							"[AutoTaskSchedulingFacade][Operator does not belong to that field] " + op.getEmailID());
					continue;
				}
			} else {
				logger.error("[AutoTaskSchedulingFacade][Operator does not have designations] " + op.getEmailID());
				continue;
			}
		}
		return withinPermisibleTime;
	}

	private boolean checkOperatorIsPermissible(Calendar maxTaskAssignmentTime, Entry<String, ATSTaskListDto> atsTask,
			StopTimeDto stdto, Date finishTime, Date prevFinishTime, int resolveTaskTime, List<Object> li,
			boolean withinPermisibleTime, ATSOpertaorDetailsDto op, Double totalDuration) {
		Double roadDriveTimeBetweenLoc=0d;
		Date startTime;
		if (ServicesUtil.isEmpty(op.getPrevTaskLocCode())) {
			// Drive time from Facility to that
			// location,
			roadDriveTimeBetweenLoc = atsTask.getValue().getRoadDriveTimeFromFixedCF();
			if (!ServicesUtil.isEmpty(roadDriveTimeBetweenLoc))
				startTime = DateUtils.addMinutes(op.getStartTimeForNextTask(),
						(int) Math.round(roadDriveTimeBetweenLoc));
			else {
				roadDriveTimeBetweenLoc = 1.0d;
				startTime = DateUtils.addMinutes(op.getStartTimeForNextTask(),
						(int) Math.round(roadDriveTimeBetweenLoc));
			}

		} else {
			// Drive time from prevLoc to new
			// taskLocation
			roadDriveTimeBetweenLoc = atsDao.getRoadDriveTime(op.getPrevTaskLocCode(),
					atsTask.getValue().getLocationCode());
			if (ServicesUtil.isEmpty(roadDriveTimeBetweenLoc))
				roadDriveTimeBetweenLoc = driveTimeBetweenLocations(op.getPrevTaskLocCode(),
						atsTask.getValue().getLocationCode());
			if (ServicesUtil.isEmpty(roadDriveTimeBetweenLoc))
				roadDriveTimeBetweenLoc = getCrowFlyDistanceTime(op.getPrevTaskLocCode(),
						atsTask.getValue().getLocationCode());
			if (ServicesUtil.isEmpty(roadDriveTimeBetweenLoc)) {
				roadDriveTimeBetweenLoc = 0d;
				if (((int) Math.round(roadDriveTimeBetweenLoc)) == 0)
					roadDriveTimeBetweenLoc = 1.0d;
			}
			startTime = DateUtils.addMinutes(op.getStartTimeForNextTask(),
					(int) Math.round(roadDriveTimeBetweenLoc));
		}

		if (startTime.before(maxTaskAssignmentTime.getTime())) {
			withinPermisibleTime = true;
			if (op.getDesignation().equalsIgnoreCase(MurphyConstant.PRO_A)) {
				// totalDuration(RT+DT) ;
				// finishTime =
				// (totalDuration(RT+DT) +
				// opStartTime)
				resolveTaskTime = stdto.getProA();
				totalDuration = totalDuration > stdto.getProA() + roadDriveTimeBetweenLoc
						? stdto.getProA() + roadDriveTimeBetweenLoc : totalDuration;
				finishTime = DateUtils.addMinutes(startTime,
						(int) Math.round(totalDuration));
			} else if (op.getDesignation().equalsIgnoreCase(MurphyConstant.PRO_B)) {
				resolveTaskTime = stdto.getProB();
				totalDuration = totalDuration > stdto.getProB() + roadDriveTimeBetweenLoc
						? stdto.getProB() + roadDriveTimeBetweenLoc : totalDuration;
				finishTime = DateUtils.addMinutes(startTime,
						(int) Math.round(totalDuration));
			} else if (op.getDesignation().equalsIgnoreCase(MurphyConstant.OBX)) {
				resolveTaskTime = stdto.getObx();
				totalDuration = totalDuration > stdto.getObx() + roadDriveTimeBetweenLoc
						? stdto.getObx() + roadDriveTimeBetweenLoc : totalDuration;
				finishTime = DateUtils.addMinutes(startTime,
						(int) Math.round(totalDuration));
			} else if (op.getDesignation().equalsIgnoreCase(MurphyConstant.SSE)) {
				resolveTaskTime = stdto.getSse();
				totalDuration = totalDuration > stdto.getSse() + roadDriveTimeBetweenLoc
						? stdto.getSse() + roadDriveTimeBetweenLoc : totalDuration;
				finishTime = DateUtils.addMinutes(startTime,
						(int) Math.round(totalDuration));
			}
			if (ServicesUtil.isEmpty(prevFinishTime)) {
				// If FT= 7:30am for the 1st
				// operator
				prevFinishTime = finishTime;
				li.clear();
				li.add(op.getEmailID());
				li.add(startTime);
				li.add(prevFinishTime);
				li.add(atsTask.getValue().getLocationCode());
				li.add(atsTask.getValue().getLocText());
				li.add(ServicesUtil.isEmpty(atsTask.getValue().getRiskBBL()) ? null
						: new BigDecimal(atsTask.getValue().getRiskBBL()));
				li.add(resolveTaskTime);
				li.add(op.getUserFieldDesignation());
				li.add(new BigDecimal(roadDriveTimeBetweenLoc));
			} else {
				// Now PFT = 8:30am; and FT =
				// 7:45am/8am/8:10am/
				if (finishTime.before(prevFinishTime)) {
					prevFinishTime = finishTime;
					li.clear();
					li.add(op.getEmailID());
					logger.error("Operator Email line515: " + op.getEmailID());
					li.add(startTime);
					li.add(finishTime);
					li.add(atsTask.getValue().getLocationCode());
					li.add(atsTask.getValue().getLocText());
					li.add(ServicesUtil.isEmpty(atsTask.getValue().getRiskBBL()) ? null
							: new BigDecimal(atsTask.getValue().getRiskBBL()));
					li.add(resolveTaskTime);
					li.add(op.getUserFieldDesignation());
					li.add(new BigDecimal(roadDriveTimeBetweenLoc));
				}
			}
		}
		return withinPermisibleTime;
	}

	public Double driveTimeBetweenLocations(String fromLocCode, String toLocCode) {
		LocationDistancesDto locationDistanceDto = null;
		Double driveTime = null;
		try {
			Coordinates toCoordinate = hierarchyDao.getCoordByCode(toLocCode);
			Coordinates fromCoordinate = hierarchyDao.getCoordByCode(fromLocCode);
			if (!ServicesUtil.isEmpty(fromCoordinate) && !ServicesUtil.isEmpty(toCoordinate)) {
				try {
					ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(fromCoordinate, toCoordinate);
					if (arcResponse.getResponseMessage().getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
						locationDistanceDto = new LocationDistancesDto();
						locationDistanceDto.setRoadDriveTime(arcResponse.getTotalDriveTime());
						driveTime = (ServicesUtil.isEmpty(locationDistanceDto.getRoadDriveTime()) ? null
								: locationDistanceDto.getRoadDriveTime());
					}
				} catch (Exception e) {
					logger.error("[driveTimeBetweenLocations][ Inner Exception] " + e.getMessage());
				}
			}

		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingFacade][driveTimeBetweenLocations][Outer Exception] " + e.getMessage());
		}
		logger.error("[driveTimeBetweenLocations]" + driveTime);
		return driveTime;
	}

	public Double getCrowFlyDistanceTime(String locationCodeOne, String locationCodeTwo) {
		Double totalDriveTime = null;
		try {
			Coordinates coordOne = null;
			Coordinates coordTwo = null;
			if (!ServicesUtil.isEmpty(locationCodeOne) && (!ServicesUtil.isEmpty(locationCodeTwo))) {
				coordOne = hierarchyDao.getCoordByCode(locationCodeOne);
				coordTwo = hierarchyDao.getCoordByCode(locationCodeTwo);
				if (!ServicesUtil.isEmpty(coordOne) && !ServicesUtil.isEmpty(coordTwo)) {
					Double distance = GeoTabUtil.getDistance(coordOne, coordTwo);
					totalDriveTime = (distance * 60) / 40;
				}
			}
		} catch (Exception e) {
			logger.error("[AutoTaskSchedulingFacade][getCrowFlyDistanceTime]" + e.getMessage());
		}
		logger.error("getCrowFlyDistanceTime totalDriveTime: "+totalDriveTime);
		return totalDriveTime;
	}
	
	
	// Fetching operator StopTime for task and Assigning task and
	/*	public List<ATSTaskAssginmentDto> assignTaskNew(LinkedHashMap<String, List<ATSOpertaorDetailsDto>> opAvail,
				HashMap<String, ATSTaskListDto> sortedTaskMapping) {

			List<ATSTaskAssginmentDto> taskAssignDtoList = null;
			try {
				if (!ServicesUtil.isEmpty(opAvail) && !ServicesUtil.isEmpty(sortedTaskMapping)) {
					// emailSeqMap = email : sequence
					HashMap<String, Integer> emailSeqMap = new HashMap<>();
					// Task AssignmentDto List
					taskAssignDtoList = new ArrayList<>();
					// CST :10am , UTC: 3pm
					Calendar maxTaskAssignmentTime = Calendar.getInstance();
					maxTaskAssignmentTime.set(Calendar.HOUR_OF_DAY, 15);
					maxTaskAssignmentTime.set(Calendar.MINUTE, 00);
					maxTaskAssignmentTime.set(Calendar.SECOND, 00);

					for (Entry<String, ATSTaskListDto> atsTask : sortedTaskMapping.entrySet()) {
						ATSTaskAssginmentDto assignmentDto = new ATSTaskAssginmentDto();
						StopTimeDto stdto = atsDao.getStopTimeBasedOnClassification(atsTask.getValue().getClassification(),
								atsTask.getValue().getSubClassification());

						Double roadDriveTimeBetweenLoc = 0d;
						Date startTime = null;
						Date finishTime = null;
						Date prevFinishTime = null;
						int resolveTaskTime = 0;

						// li : {emailID,startTime,FinishTime,locCode,locText}
						List<Object> li = new ArrayList<>();
						// Boolean check if task can be assigned in permissible
						// time
						boolean withinPermisibleTime = false;
						//opAvail - Key:Email, Value: List<ATSOpertaorDetailsDto>
						for(Entry<String, List<ATSOpertaorDetailsDto>> op : opAvail.entrySet()){
							Double totalDuration = Double.MAX_VALUE;
							// if operator for same field or not
							if(op.getValue().get(0).getUserFieldDesignation().contains(atsTask.getValue().getFieldName())){
								for(ATSOpertaorDetailsDto dt : op.getValue()){
										if(ServicesUtil.isEmpty(dt.getPrevTaskLocCode())){
											// Drive time from Facility to that location
											roadDriveTimeBetweenLoc = atsTask.getValue().getRoadDriveTimeFromFixedCF();
											startTime = DateUtils.addMinutes(op.getValue().get(0).getStartTimeForNextTask(), (int) Math.round(roadDriveTimeBetweenLoc));
										} else{
											// Drive time from prevLoc of operator to new taskLocation
											roadDriveTimeBetweenLoc = atsDao.getRoadDriveTime(dt.getPrevTaskLocCode(),atsTask.getValue().getLocationCode());
											if (ServicesUtil.isEmpty(roadDriveTimeBetweenLoc))
												roadDriveTimeBetweenLoc = driveTimeBetweenLocations(dt.getPrevTaskLocCode(),atsTask.getValue().getLocationCode());
											if (ServicesUtil.isEmpty(roadDriveTimeBetweenLoc))
												roadDriveTimeBetweenLoc = getCrowFlyDistanceTime(dt.getPrevTaskLocCode(),atsTask.getValue().getLocationCode());
											if (((int) Math.round(roadDriveTimeBetweenLoc)) == 0)
												roadDriveTimeBetweenLoc = 1.0d;
											startTime = DateUtils.addMinutes(dt.getStartTimeForNextTask(),
													(int) Math.round(roadDriveTimeBetweenLoc));
										}
										
										if(startTime.before(maxTaskAssignmentTime.getTime())) {
											withinPermisibleTime = true;
											if (dt.getDesignation().equalsIgnoreCase(MurphyConstant.PRO_A)) {
												// totalDuration(RT+DT) ;
												// finishTime = (totalDuration(RT+DT) +
												// opStartTime)
												resolveTaskTime = stdto.getProA();
												totalDuration = totalDuration > stdto.getProA() + roadDriveTimeBetweenLoc
														? stdto.getProA() + roadDriveTimeBetweenLoc : totalDuration;
												finishTime = DateUtils.addMinutes(startTime, (int) Math.round(totalDuration));
											} else if (dt.getDesignation().equalsIgnoreCase(MurphyConstant.PRO_B)) {
												resolveTaskTime = stdto.getProB();
												totalDuration = totalDuration > stdto.getProB() + roadDriveTimeBetweenLoc
														? stdto.getProB() + roadDriveTimeBetweenLoc : totalDuration;
												finishTime = DateUtils.addMinutes(startTime, (int) Math.round(totalDuration));
											} else if (dt.getDesignation().equalsIgnoreCase(MurphyConstant.OBX)) {
												resolveTaskTime = stdto.getObx();
												totalDuration = totalDuration > stdto.getObx() + roadDriveTimeBetweenLoc
														? stdto.getObx() + roadDriveTimeBetweenLoc : totalDuration;
												finishTime = DateUtils.addMinutes(startTime, (int) Math.round(totalDuration));
											} else if (dt.getDesignation().equalsIgnoreCase(MurphyConstant.SSE)) {
												resolveTaskTime = stdto.getSse();
												totalDuration = totalDuration > stdto.getSse() + roadDriveTimeBetweenLoc
														? stdto.getSse() + roadDriveTimeBetweenLoc : totalDuration;
												finishTime = DateUtils.addMinutes(startTime, (int) Math.round(totalDuration));
											}
											
											if(finishTime.before(dt.getEndTimeForTask())){
												if (ServicesUtil.isEmpty(prevFinishTime)) {
													// If FT= 7:30am for the 1st operator
													prevFinishTime = finishTime;
													li.clear();
													li.add(dt.getEmailID());
													li.add(startTime);
													li.add(prevFinishTime);
													li.add(atsTask.getValue().getLocationCode());
													li.add(atsTask.getValue().getLocText());
													li.add(ServicesUtil.isEmpty(atsTask.getValue().getRiskBBL()) ? null
															: new BigDecimal(atsTask.getValue().getRiskBBL()));
													li.add(resolveTaskTime);
													li.add(dt.getUserFieldDesignation());
													li.add(new BigDecimal(roadDriveTimeBetweenLoc));
												} else {
													// Now PFT = 8:30am; and FT =
													// 7:45am/8am/8:10am/
													if (finishTime.before(prevFinishTime)) {
														prevFinishTime = finishTime;
														li.clear();
														li.add(dt.getEmailID());
														logger.error("Operator Email line515: " + dt.getEmailID());
														li.add(startTime);
														li.add(finishTime);
														li.add(atsTask.getValue().getLocationCode());
														li.add(atsTask.getValue().getLocText());
														li.add(ServicesUtil.isEmpty(atsTask.getValue().getRiskBBL()) ? null
																: new BigDecimal(atsTask.getValue().getRiskBBL()));
														li.add(resolveTaskTime);
														li.add(dt.getUserFieldDesignation());
														li.add(new BigDecimal(roadDriveTimeBetweenLoc));
													}
												}
											}
										}
									}
								}else{
									logger.error("[AutoTaskSchedulingFacade][Operator does not belong to that field] ");
									continue;
								}
							}
						}
				}
			} catch (Exception e) {
				logger.error("[AutoTaskSchedulingFacade][operatorStopTimeByClassification][Exception] " + e.getMessage());
			}
			return taskAssignDtoList;

		}*/

	
}
