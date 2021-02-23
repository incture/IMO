//package com.murphy.taskmgmt.service;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.UUID;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.murphy.geotab.Coordinates;
//import com.murphy.geotab.UserLocationResponse;
//import com.murphy.taskmgmt.dao.ConfigDao;
//import com.murphy.taskmgmt.dao.CustomAttrTemplateDao;
//import com.murphy.taskmgmt.dao.HierarchyDao;
//import com.murphy.taskmgmt.dao.LocationDistancesDao;
//import com.murphy.taskmgmt.dao.OBXSchedulerDao;
//import com.murphy.taskmgmt.dao.OBXSchedulerDaoDummy;
//import com.murphy.taskmgmt.dao.ObxTaskDao;
//import com.murphy.taskmgmt.dao.ObxTaskDaoDummy;
//import com.murphy.taskmgmt.dao.TaskOwnersDao;
//import com.murphy.taskmgmt.dao.UserIDPMappingDao;
//import com.murphy.taskmgmt.dto.ArcGISBestSequesnceDto;
//import com.murphy.taskmgmt.dto.ArcGISBestSequesnceListDto;
//import com.murphy.taskmgmt.dto.ArcGISResponseDto;
//import com.murphy.taskmgmt.dto.AttrTempResponseDto;
//import com.murphy.taskmgmt.dto.CollaborationDto;
//import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
//import com.murphy.taskmgmt.dto.CustomTaskDto;
//import com.murphy.taskmgmt.dto.FLSOPResponseDto;
//import com.murphy.taskmgmt.dto.GroupsUserDto;
//import com.murphy.taskmgmt.dto.LocationDistancesDto;
//import com.murphy.taskmgmt.dto.NearByTaskListDto;
//import com.murphy.taskmgmt.dto.NearbyTaskDto;
//import com.murphy.taskmgmt.dto.ObxTaskDto;
//import com.murphy.taskmgmt.dto.ObxTaskDtoDummy;
//import com.murphy.taskmgmt.dto.ObxTaskUpdateDto;
//import com.murphy.taskmgmt.dto.ResponseMessage;
//import com.murphy.taskmgmt.dto.TaskEventsDto;
//import com.murphy.taskmgmt.dto.TaskOwnersDto;
//import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
//import com.murphy.taskmgmt.dto.WellVisitDayMatrixDto;
//import com.murphy.taskmgmt.dto.WellVisitDto;
//import com.murphy.taskmgmt.dto.WellVisitDtoDummy;
//import com.murphy.taskmgmt.dto.WellVisitMatrixDto;
//import com.murphy.taskmgmt.entity.UserIDPMappingDo;
//import com.murphy.taskmgmt.service.interfaces.ObxSchedulerFacadeLocal;
//import com.murphy.taskmgmt.service.interfaces.ObxSchedulerFacadeLocalDummy;
//import com.murphy.taskmgmt.service.interfaces.TaskManagementFacadeLocal;
//import com.murphy.taskmgmt.util.ArcGISUtil;
//import com.murphy.taskmgmt.util.GeoTabUtil;
//import com.murphy.taskmgmt.util.MurphyConstant;
//import com.murphy.taskmgmt.util.ServicesUtil;
//
//import weka.clusterers.ClusterEvaluation;
//import weka.clusterers.SimpleKMeans;
//import weka.core.Attribute;
//import weka.core.DenseInstance;
//import weka.core.Instance;
//import weka.core.Instances;
//
//@Service("ObxSchedulerFacadeDummyDummy")
//public class ObxSchedulerFacadeDummy implements ObxSchedulerFacadeLocalDummy {
//
//	private static final Logger logger = LoggerFactory.getLogger(ObxSchedulerFacadeDummy.class);
//
//	@Autowired
//	HierarchyDao hierarchyDao;
//
//	@Autowired
//	OBXSchedulerDao obxDao;
//	
//	@Autowired
//	OBXSchedulerDaoDummy obxDaoDummy;
//
//	@Autowired
//	UserIDPMappingDao userIdpMappingDao;
//
//	@Autowired
//	TaskSchedulingCalFacade taskSchedulingDao;
//
//	@Autowired
//	TaskManagementFacade taskMgmtDao;
//
//	@Autowired
//	TaskManagementFacadeLocal taskManagementFacade;
//
//	@Autowired
//	ConfigDao configDao;
//
//	@Autowired
//	LocationDistancesDao distanceDao;
//
//	@Autowired
//	private CustomAttrTemplateDao attrTempDao;
//
//	@Autowired
//	private TaskOwnersDao ownersDao;
//	
//	@Autowired
//	ObxTaskDao obxTaskDao;
//	
//	@Autowired
//	ObxTaskDaoDummy obxTaskDaoDummy;
//
//	private WellVisitMatrixDto getWellVisitMatrixTierA(List<String> list) {
//		// initialization
//		WellVisitMatrixDto weekVisitMatrix = new WellVisitMatrixDto();
//		weekVisitMatrix.setDayMatrixList(new ArrayList<WellVisitDayMatrixDto>());
//		for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i++) {
//			WellVisitDayMatrixDto dayMatrix = new WellVisitDayMatrixDto();
//			dayMatrix.setWorkLoad(0.0);
//			dayMatrix.setLocationCodes(new ArrayList<String>());
//			weekVisitMatrix.getDayMatrixList().add(dayMatrix);
//		}
//		for (String locationCode : list) {
//			for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i++) {
//				weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes().add(locationCode);
//			}
//		}
//		return weekVisitMatrix;
//	}
//
//	private WellVisitMatrixDto getWellVisitMatrixTierB(List<String> firstBatch, List<String> secondBatch,
//			WellVisitMatrixDto weekVisitMatrix, boolean hasTierAWells) {
//		if (!hasTierAWells) {
//			weekVisitMatrix = new WellVisitMatrixDto();
//			weekVisitMatrix.setDayMatrixList(new ArrayList<WellVisitDayMatrixDto>());
//			WellVisitDayMatrixDto dayMatrix = null;
//			for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i++) {
//				dayMatrix = new WellVisitDayMatrixDto();
//				dayMatrix.setWorkLoad(0.0);
//				dayMatrix.setLocationCodes(new ArrayList<String>());
//				weekVisitMatrix.getDayMatrixList().add(dayMatrix);
//			}
//		}
//		// Monday & Thursday firstBatch of locations
//		for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i += 3) {
//			weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes().addAll(firstBatch);
//		}
//		// Tuesday & Friday secondBatch of locations
//		for (int i = 1; i < MurphyConstant.NUMBER_OF_DAYS; i += 3) {
//			weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes().addAll(secondBatch);
//		}
//		return weekVisitMatrix;
//	}
//
//	private WellVisitMatrixDto getWellVisitMatrixTierC(List<String> list, WellVisitMatrixDto weekVisitMatrix,
//			boolean hasOnlyTierC, boolean hasTierB) {
//		Double WorkLoadOnMinDay = Double.MAX_VALUE;
//		int minWorkLoadDay = 0;
//		HashMap<Integer, Double> estimatedWorkLoad = null;
//		Double estimatedResolveTime=(double) attrTempDao.getEstTimeForSubClass(MurphyConstant.OBX_SUB_CLASSIFICATION);
//		Double nearestDistance = 0.0;
//		if (!hasOnlyTierC) {
//			weekVisitMatrix = new WellVisitMatrixDto();
//			weekVisitMatrix.setDayMatrixList(new ArrayList<WellVisitDayMatrixDto>());
//			for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i++) {
//				WellVisitDayMatrixDto dayMatrix = new WellVisitDayMatrixDto();
//				dayMatrix.setWorkLoad(0.0);
//				dayMatrix.setLocationCodes(new ArrayList<String>());
//				weekVisitMatrix.getDayMatrixList().add(dayMatrix);
//				estimatedWorkLoad = copy(weekVisitMatrix.getDayMatrixList());
//			}
//		} else {
//			calculateWorkLoadOfMatrix(weekVisitMatrix);
//			estimatedWorkLoad = copy(weekVisitMatrix.getDayMatrixList());
//		}
//		for (String location : list) {
//			minWorkLoadDay = 0;
//			WorkLoadOnMinDay = Double.MAX_VALUE;
//			// Double WorkLoad=0.0;
//			nearestDistance = 0.0;
//			for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i++) {
//				nearestDistance = getNearestDistance(weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes(),
//						location);
//				nearestDistance += estimatedResolveTime;
//				estimatedWorkLoad.put(i, estimatedWorkLoad.get(i) + nearestDistance);
//			}
//			for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i++) {
//				if (estimatedWorkLoad.get(i) < WorkLoadOnMinDay) {
//					WorkLoadOnMinDay = estimatedWorkLoad.get(i);
//					minWorkLoadDay = i;
//				}
//			}
//			weekVisitMatrix.getDayMatrixList().get(minWorkLoadDay).getLocationCodes().add(location);
//			weekVisitMatrix.getDayMatrixList().get(minWorkLoadDay).setWorkLoad(WorkLoadOnMinDay);
//			// logger.error("weekVisitMatrix.getDayMatrixList().get(minWorkLoadDay)"+weekVisitMatrix.getDayMatrixList().get(minWorkLoadDay));
//			estimatedWorkLoad = copy(weekVisitMatrix.getDayMatrixList());
//		}
//		return weekVisitMatrix;
//	}
//
//	private HashMap<Integer, Double> copy(List<WellVisitDayMatrixDto> dayMatrixList) {
//		// TODO Auto-generated method stub
//		HashMap<Integer, Double> copy = new HashMap<Integer, Double>();
//		for (int i = 0; i < dayMatrixList.size(); i++) {
//			copy.put(i, dayMatrixList.get(i).getWorkLoad());
//		}
//		return copy;
//	}
//
//	private void calculateWorkLoadOfMatrix(WellVisitMatrixDto weekVisitMatrix) {
//		Double workLoad = 0.0;
//		Double taskEstimatedTime = (double) attrTempDao.getEstTimeForSubClass(MurphyConstant.OBX_SUB_CLASSIFICATION);
//		for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i++) {
//			workLoad = 0.0;
//			WellVisitDayMatrixDto dayMatrix = weekVisitMatrix.getDayMatrixList().get(i);
//			for (int j = 0; j < dayMatrix.getLocationCodes().size() - 1; j++) {
//				LocationDistancesDto distanceDto = distanceDao.getDistance(dayMatrix.getLocationCodes().get(j),
//						dayMatrix.getLocationCodes().get(j + 1));
//				Double distance = 0.0;
//				if (distanceDto.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS)) {
//					distance = distanceDto.getRoadTotalTime();
//				} else {
//					Coordinates fromCoordinate = hierarchyDao.getCoordByCode(dayMatrix.getLocationCodes().get(j));
//					Coordinates toCoordinate = hierarchyDao.getCoordByCode(dayMatrix.getLocationCodes().get(j + 1));
//					ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(fromCoordinate, toCoordinate);
//					if (arcResponse.getResponseMessage().getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
//						distance = arcResponse.getTotalDriveTime();
//					}
//				}
//				workLoad += distance;
//				workLoad += taskEstimatedTime;
//			}
//			dayMatrix.setWorkLoad(workLoad);
//		}
//	}
//
//	// private int findDayWithLessWorkLoad(WellVisitMatrixDto visitMatrix) {
//	// int dayWithMinWorkLoad = 0;
//	// List<Coordinates> coordList = null;
//	// Double MinWorkLoad = Double.MAX_VALUE;
//	// for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i++) {
//	// if (visitMatrix.getDayMatrixList().get(i).getLocationCodes().size() > 1)
//	// {
//	// coordList =
//	// hierarchyDao.getCoordsByCodes(visitMatrix.getDayMatrixList().get(i).getLocationCodes());
//	// ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(coordList);
//	// if
//	// (arcResponse.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS))
//	// {
//	// Double MinWorkLoad1 = arcResponse.getTotalDriveTime();
//	// visitMatrix.getDayMatrixList().get(i).setWorkLoad(MinWorkLoad1);
//	// if (MinWorkLoad > MinWorkLoad1) {
//	// MinWorkLoad = MinWorkLoad1;
//	// dayWithMinWorkLoad = i;
//	// }
//	//
//	// } else {
//	// logger.error("Error While calling getRoadDistance in
//	// findDayWithLessWorkLoad"
//	// + arcResponse.getResponseMessage().getMessage() + " for locations " +
//	// coordList + " on day "
//	// + i);
//	// }
//	// } else {
//	// dayWithMinWorkLoad = i;
//	// visitMatrix.getDayMatrixList().get(i).setWorkLoad(0.0);
//	// break;
//	// }
//	// }
//	// return dayWithMinWorkLoad;
//	// }
//
//	private HashMap<String, List<String>> getLocationsOfFields() {
//		HashMap<String, List<String>> response = new HashMap<>();
//		HashMap<String, String> fieldLocations = hierarchyDao.getLocationByLocType(MurphyConstant.FIELD);
//		for (Map.Entry<String, String> entry : fieldLocations.entrySet()) {
//			response.put(entry.getKey(), hierarchyDao.getLocCodeByLocationTypeAndCode(MurphyConstant.FIELD,
//					Arrays.asList(entry.getValue())));
//		}
//		return response;
//	}
//
//	@Override
//	public void generateWellVisitMatrix() {
//		obxDaoDummy.deleteAllWellVisitData();
//		HashMap<String, List<String>> locationMap = getLocationsOfFields();
//		for (Map.Entry<String, List<String>> entry : locationMap.entrySet()) {
//			WellVisitMatrixDto fieldMatrix = buildWellVisitMatrix_Field(entry);
//			try {
//				if (fieldMatrix.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS)) {
//					logger.error("[ObxSchedulerFacadeDummy][generateWellVisitMatrix] Matrix SuccessFully build For "
//							+ entry.getKey());
//					updateWellVisitMatrix(fieldMatrix, entry.getKey());
//				}
//			} catch (Exception e) {
//				logger.error("[ObxSchedulerFacadeDummy][generateWellVisitMatrix][error] " + e.getMessage()
//						+ " While Building Matrix for " + entry.getKey());
//			}
//		}
//	}
//
//	@Override
//	public String updateWellVisitMatrix(WellVisitMatrixDto fieldMatrix, String field) {
//		WellVisitDtoDummy dto = null;
//		String response = MurphyConstant.FAILURE;
//		if (!ServicesUtil.isEmpty(fieldMatrix.getDayMatrixList())) {
//			try {
//				for (int i = 0; i < fieldMatrix.getDayMatrixList().size(); i++) {
//					WellVisitDayMatrixDto dayMatrix = fieldMatrix.getDayMatrixList().get(i);
//					// to Match day in util.day
//					int day = i + 2;
//					for (int j = 0; j < dayMatrix.getLocationCodes().size(); j++) {
//						dto = new WellVisitDtoDummy();
//						dto.setDay(day);
//						dto.setField(field);
//						dto.setLocationCode(dayMatrix.getLocationCodes().get(j));
//						obxDaoDummy.create(dto);
//					}
//				}
//				response = MurphyConstant.SUCCESS;
//			} catch (Exception e) {
//				logger.error("Error While Storing Matrix" + e.getMessage());
//			}
//		}
//
//		return response;
//	}
//
//	private WellVisitMatrixDto buildWellVisitMatrix_Field(Map.Entry<String, List<String>> entry) {
//		WellVisitMatrixDto visitMatrix = new WellVisitMatrixDto();
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setMessage("Error While Building Well Visit Matrix");
//		responseMessage.setStatus(MurphyConstant.FAILURE);
//		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		boolean hasTierAWells = false, hasTierBWells = false;
//		try {
//			HashMap<String, List<String>> FieldLocCodeWithTier = divideLocationsByTier(entry.getValue());
//
//			if (FieldLocCodeWithTier.containsKey(MurphyConstant.TIER_A)) {
//				visitMatrix = getWellVisitMatrixTierA(FieldLocCodeWithTier.get("Tier A"));
//				hasTierAWells = true;
//			}
//			if (FieldLocCodeWithTier.containsKey(MurphyConstant.TIER_B)) {
//				hasTierBWells = true;
//				List<List<String>> tierBDividedList = new ArrayList<List<String>>();
//				if (FieldLocCodeWithTier.get(MurphyConstant.TIER_B).size() > 2) {
//					tierBDividedList = divideTierBWells(FieldLocCodeWithTier.get(MurphyConstant.TIER_B));
//				} else {
//					tierBDividedList.add(Arrays.asList(FieldLocCodeWithTier.get(MurphyConstant.TIER_B).get(0)));
//					if (FieldLocCodeWithTier.get(MurphyConstant.TIER_B).size() == 2)
//						tierBDividedList.add(Arrays.asList(FieldLocCodeWithTier.get(MurphyConstant.TIER_B).get(1)));
//					else
//						tierBDividedList.add(new ArrayList<>());
//				}
//				if (!ServicesUtil.isEmpty(tierBDividedList)) {
//					visitMatrix = getWellVisitMatrixTierB(tierBDividedList.get(0), tierBDividedList.get(1), visitMatrix,
//							hasTierAWells);
//				} else {
//					logger.error("Error While Dividing Tier B Wells");
//				}
//			}
//			if (FieldLocCodeWithTier.containsKey(MurphyConstant.TIER_C)) {
//				visitMatrix = getWellVisitMatrixTierC(FieldLocCodeWithTier.get(MurphyConstant.TIER_C), visitMatrix,
//						hasTierAWells || hasTierBWells, hasTierBWells);
//			}
//			responseMessage.setMessage("Well Visit Matrix Build SuccessFully");
//			responseMessage.setStatus(MurphyConstant.SUCCESS);
//			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacadeDummy][buildWellVisitMatrix_Field][error]" + e.getMessage());
//		}
//		visitMatrix.setResponseMessage(responseMessage);
//		return visitMatrix;
//
//	}
//
//	private List<List<String>> divideTierBWells(List<String> locationList) {
//		List<Coordinates> coordList = hierarchyDao.getCoordsByCodes(locationList);
//		List<List<String>> locationSets = null;
//		try {
//			ArcGISBestSequesnceListDto bestRoute = ArcGISUtil.getRoadDistance_BestRoute(coordList,MurphyConstant.ARCGIS_SERVICE_BEST_ROUTE_PAYLOAD_WITH_FIRSTSTOP);
//			if (bestRoute.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS)) {
//				Double[] timeArray = new Double[bestRoute.getBestSequenceList().size()];
//				for (int i = 0; i < bestRoute.getBestSequenceList().size(); i++) {
//					timeArray[i] = bestRoute.getBestSequenceList().get(i).getTotalDriveTime();
//				}
//				List<List<String>> indexSets = ServicesUtil.divideIn2EqualSums(timeArray);
//				locationSets = getLocationsByIndex(indexSets, locationList);
//			} else {
//				logger.error("Error While calling getRoadDistance_BestRoute for" + coordList);
//			}
//		} catch (Exception e) {
//			logger.error("Error While dividing tier B wells " + e.getMessage());
//		}
//		return locationSets;
//	}
//
//	private List<List<String>> getLocationsByIndex(List<List<String>> indexSets, List<String> locationList) {
//		List<List<String>> locationSets = new ArrayList<List<String>>();
//		List<String> locationSet1 = new ArrayList<String>();
//		List<String> locationSet2 = new ArrayList<String>();
//		for (int i = 0; i < indexSets.get(0).size(); i++) {
//			if (!ServicesUtil.isEmpty(indexSets.get(0).get(i))) {
//				locationSet1.add(locationList.get(Integer.parseInt(indexSets.get(0).get(i))));
//			}
//		}
//		for (int i = 0; i < indexSets.get(1).size(); i++) {
//			if (!ServicesUtil.isEmpty(indexSets.get(1).get(i))) {
//				locationSet2.add(locationList.get(Integer.parseInt(indexSets.get(1).get(i))));
//			}
//		}
//		locationSets.add(locationSet1);
//		locationSets.add(locationSet2);
//		return locationSets;
//	}
//
//	public HashMap<String, List<String>> divideLocationsByTier(List<String> locationList) {
//		HashMap<String, List<String>> response = new HashMap<>();
//		response = hierarchyDao.getTierMap(locationList);
//		return response;
//	}
//
//	
//	public HashMap<String, List<String>> CreateUserJobs() {
//		Calendar calendar = Calendar.getInstance();
//		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//		HashMap<String, String> fieldLocations = hierarchyDao.getLocationByLocType(MurphyConstant.FIELD);
//		List<String> usersList = null;
//		HashMap<String, List<String>> jobList = null;
//		List<String> locations = null;
//		int i = 0;
//		String fields = "";
//		for (Entry<String, String> entry : fieldLocations.entrySet()) {
//			if (i == 0) {
//				fields = entry.getKey();
//				i++;
//			} else
//				fields = fields + "," + entry.getKey();
//		}
//		HashMap<String, ArrayList<String>> OBXRoles = userIdpMappingDao.getOBXRolesWithFieldMap(fields,
//				MurphyConstant.OBX_BUSINESS_ROLE);
//		List<String> roleList = null;
//		List<GroupsUserDto> roleUsersList = null;
//		for (Entry<String, ArrayList<String>> entry : OBXRoles.entrySet()) {
//			usersList = new ArrayList<String>();
//			roleList = new ArrayList<>();
//			locations = new ArrayList<String>();
//			roleList.add(entry.getKey());
//			roleUsersList = userIdpMappingDao.getUsersBasedOnRole(roleList);
//			if (!ServicesUtil.isEmpty(roleUsersList)) {
//				for (GroupsUserDto user : roleUsersList) {
//					usersList.add(user.getUserId());
//				}
//				locations = obxDaoDummy.getVisitMatrix(entry.getValue(), dayOfWeek);
//				if (!ServicesUtil.isEmpty(locations)) {
//					jobList = assignUsersJobs(usersList, locations);
//					sortBasedOnRoute(jobList);
//					ArrayList<ObxTaskDto> tasks=new ArrayList<ObxTaskDto>();
//					try{
//					tasks=obxTaskDao.getObxTaskDtoFromLocationCode(jobList,entry.getValue().get(0));
//					obxTaskDaoDummy.insertTasks(tasks);
//					}
//					catch (Exception e) {
//						logger.error("Error while inserting to dummy table");
//					}
////					insertTasks(jobList);
//					CreateTasks(jobList);
//				} 
//				else {
//					logger.error("[ObxSchedulerFacadeDummy][CreateUserJobs] Job is not Present for " + entry.getValue()
//							+ " on day " + dayOfWeek);
//				}
//			} else {
//				logger.error("[ObxSchedulerFacadeDummy][CreateUserJobs] User List is Empty for" + entry.getKey());
//			}
//			logger.error("[ObxSchedulerFacadeDummy][CreateUserJobs] Job Creation Completed For " + entry.getKey());
//		}
//		logger.error("[ObxSchedulerFacadeDummy][CreateUserJobs] Job Creation Completed");
//		return jobList;
//	}
//
//
//	@Override
//	public ResponseMessage updateTaskSequence(String obxRole){
//		ResponseMessage response=new ResponseMessage();
//		response.setStatus(MurphyConstant.FAILURE);
//		response.setStatusCode(MurphyConstant.CODE_FAILURE);
//		try{
//			List<String> OBXRoles=new ArrayList<>();
//			OBXRoles.add(obxRole);
//			List<GroupsUserDto> obxUsersList = userIdpMappingDao.getUsersBasedOnRole(OBXRoles);
//			for(GroupsUserDto user:obxUsersList){
//				try{
//				List<ObxTaskDtoDummy> userTasks=obxTaskDaoDummy.getTasksOfUser(user.getUserId());
//					findSequence(userTasks, obxRole);
//					obxTaskDaoDummy.updateTasks(userTasks);
//				}
//				catch (Exception e) {
//					logger.error("[ObxSchedulerFacadeDummy][updateTaskSequence] Exception "+e.getMessage()+" while finding Sequence for "+user.getUserId());
//				}
//			}
//		}
//		catch (Exception e) {
//			logger.error("[ObxSchedulerFacadeDummy][updateTaskSequence] Exception "+e.getMessage());
//		}
//		return response;
//	}
//	
//	public List<ObxTaskDtoDummy> findSequence(List<ObxTaskDtoDummy> obxTasks,String role) {
//		List<String> locationList=new ArrayList<>();
//		for(ObxTaskDtoDummy tasks:obxTasks){
//			locationList.add(tasks.getLocationCode());
//		}
////		StringJoiner joiner=new StringJoiner("','");
////		for(String field:fields)
////			joiner.add(field+"_STARTING_POINT");
////		String commaSeperatedField=joiner.toString();
//		String coordInString=configDao.getConfigurationByRef(role);
//		List<String> coords=Arrays.asList(coordInString.split(","));
//		Coordinates startingPoint=new Coordinates();
//		startingPoint.setLatitude(Double.parseDouble(coords.get(0)));
//		startingPoint.setLongitude(Double.parseDouble(coords.get(1)));
//		List<Coordinates> coordList = new ArrayList<Coordinates>();
//		coordList.add(startingPoint);
//		for(ObxTaskDtoDummy task:obxTasks){
//			Coordinates taskCoord=new Coordinates(task.getLatitude(),task.getLongitude());
//			coordList.add(taskCoord);
//		}
//		try {
////			List<Coordinates> coordListWithCentralFacility=new ArrayList<>();
////			coordListWithCentralFacility.add(startingPoint);
////			coordListWithCentralFacility.addAll(coordList);
//			ArcGISBestSequesnceListDto bestRoute = ArcGISUtil.getRoadDistance_BestRoute(coordList,MurphyConstant.ARCGIS_SERVICE_BEST_ROUTE_PAYLOAD_WITH_FIRSTSTOP);
//			if(MurphyConstant.SUCCESS.equals(bestRoute.getResponseMessage().getStatus())){
//				logger.error("best Sequence "+bestRoute.getBestSequenceList());
//				for(int i=1;i<bestRoute.getBestSequenceList().size();i++){
//					ArcGISBestSequesnceDto dto = bestRoute.getBestSequenceList().get(i);
//					int taskIndex=dto.getLocationId()-2;
//					obxTasks.get(taskIndex).setSequenceNumber(dto.getSequenceNumber()-1);
//					obxTasks.get(taskIndex).setDriveTime(dto.getTotalDriveTime());
//				}
//			}
//			obxTasks=updateDriveTime(obxTasks);
//		}
//		catch (Exception e) {
//			logger.error("[ObxSchedulerFacadeDummy][findSequence] Exception "+e.getMessage());
//			throw e;
//		}
//		return obxTasks;
//	}
//
//	private void CreateTasks(HashMap<String, List<String>> jobList) {
//		for (Entry<String, List<String>> User : jobList.entrySet()) {
//			for (String Location : User.getValue()) {
//				ResponseMessage response = createOBXTask(User.getKey(), Location);
//				if (response.getStatus().equals(MurphyConstant.FAILURE)) {
//					logger.error("[ObxSchedulerFacadeDummy][CreateTasks] Task Creation Failed For " + User.getKey()
//							+ " for the Location " + Location);
//				}
//			}
//		}
//
//	}
//
//	@Override
//	public ResponseMessage createOBXTask(String userId, String location) {
//		ResponseMessage response = new ResponseMessage();
//		CustomTaskDto customTaskDto = new CustomTaskDto();
//		TaskEventsDto taskEventsDto = new TaskEventsDto();
//		try {
//			TaskOwnersDto owner = new TaskOwnersDto();
//			owner.setEstResolveTime(attrTempDao.getEstTimeForSubClass(MurphyConstant.OBX_SUB_CLASSIFICATION));
//			owner.setTaskOwner(userId);
//			owner.setOwnerEmail(userId);
//			UserIDPMappingDo user = userIdpMappingDao.getUserByEmail(userId);
//			owner.setpId(user.getpId());
//			owner.setTaskOwnerDisplayName(user.getUserFirstName() + " " + user.getUserLastName());
//			List<CustomAttrTemplateDto> customAttrList = new ArrayList<>();
//			// fetching task custom Attributes
//			AttrTempResponseDto taskHeaders = taskManagementFacade.getCustomHeaders("123", null, MurphyConstant.WELL,
//					null, location, null, null, 0);
//			customAttrList = taskHeaders.getCustomAttr();
//			HashMap<String, String> attributeValues = new HashMap<String, String>();
//			attributeValues.put("123", hierarchyDao.getLocationByLocCode(location));
//			attributeValues.put("12345", MurphyConstant.OBX);
//			attributeValues.put("123456", MurphyConstant.OBX_SUB_CLASSIFICATION);
//			attributeValues.put("1234567", "NEW");
//			attributeValues.put("12345678", null);
//			attributeValues.put("1234", null);
//			for (CustomAttrTemplateDto customAttribute : customAttrList) {
//				customAttribute.setLabelValue(attributeValues.get(customAttribute.getClItemId()));
//			}
//			customTaskDto.setCustomAttr(customAttrList);
//
//			List<TaskOwnersDto> taskOwners = new ArrayList<TaskOwnersDto>();
//			taskOwners.add(owner);
//			taskEventsDto.setOwners(taskOwners);
//
//			taskEventsDto.setCreatedBy(MurphyConstant.OBX_CREATOR);
//			taskEventsDto.setCreatedByDisplay(MurphyConstant.OBX_CREATOR);
//			taskEventsDto.setLocationCode(location);
//			taskEventsDto.setOrigin(MurphyConstant.DISPATCH_ORIGIN);
//			taskEventsDto.setParentOrigin(MurphyConstant.OBX);
//			taskEventsDto.setStatus(MurphyConstant.ASSIGN);
//			taskEventsDto.setSubClassification(MurphyConstant.OBX_SUB_CLASSIFICATION);
//			taskEventsDto.setSubject(MurphyConstant.OBX + "/" + MurphyConstant.OBX_SUB_CLASSIFICATION);
//			taskEventsDto.setGroup(MurphyConstant.OBX_CREATOR);
//			taskEventsDto.setTaskType("SYSTEM");
//
//			customTaskDto.setTaskEventDto(taskEventsDto);
//			FLSOPResponseDto flsop = taskMgmtDao.getFLSOP(MurphyConstant.OBX, MurphyConstant.OBX_SUB_CLASSIFICATION);
//			CollaborationDto commentsDto = new CollaborationDto();
//			if (flsop.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS)) {
//				commentsDto.setCreatedAtDisplay(ServicesUtil.convertFromZoneToZoneString(new Date(), null,
//						MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
//						MurphyConstant.DATE_DB_FORMATE));
//				String message = flsop.getFlsop();
//				String replacedString = message.replaceAll("\\\\n", "\n");
//				commentsDto.setMessage(replacedString);
//				commentsDto.setUserDisplayName(MurphyConstant.OBX_CREATOR);
//				commentsDto.setUserId(MurphyConstant.OBX_CREATOR);
//			}
//			List<CollaborationDto> collaborationList = new ArrayList<CollaborationDto>();
//			collaborationList.add(commentsDto);
//			customTaskDto.setCollabrationDtos(collaborationList);
//			response = taskSchedulingDao.createTask(customTaskDto);
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacadeDummy][createOBXTask][Error]" + e.getMessage());
//		}
//		return response;
//
//	}
//
//	private HashMap<String, List<String>> assignUsersJobs(List<String> usersList, List<String> locations) {
//		HashMap<String, List<String>> usersJobs = new HashMap<String, List<String>>();
//		HashMap<String, Integer> workLoad = new HashMap<String, Integer>();
//		if (usersList.size() > 1) {
//			initializeAllocationMatrix(usersList, locations, usersJobs, workLoad);
//			generateJobMatrix(usersList, locations, usersJobs, workLoad);
//		} else {
//			usersJobs.put(usersList.get(0), locations);
//		}
//		return usersJobs;
//	}
//
//	private void generateJobMatrix(List<String> usersList, List<String> locations,
//			HashMap<String, List<String>> usersJobs, HashMap<String, Integer> workLoad) {
//		Double taskEstimatedTime = (double) attrTempDao.getEstTimeForSubClass(MurphyConstant.OBX_SUB_CLASSIFICATION);
//		int dayWithMinEstimatedWorkLoad = Integer.MAX_VALUE;
//		String assignee = usersList.get(0);
//		int distance = 0;
//		HashMap<String, Integer> estimatedWorkLoad = copy(workLoad);
//		for (int j = 0; j < locations.size(); j++) {
//			dayWithMinEstimatedWorkLoad = Integer.MAX_VALUE;
//			for (Entry<String, List<String>> entry : usersJobs.entrySet()) {
//				distance = getNearestDistance(entry.getValue(), locations.get(j)).intValue();
//				// estimatedWorkLoad[k] += distance;
//				estimatedWorkLoad.put(entry.getKey(),
//						(estimatedWorkLoad.get(entry.getKey()) + distance + taskEstimatedTime.intValue()));
//			}
//			for (Entry<String, Integer> entry : estimatedWorkLoad.entrySet()) {
//				if (dayWithMinEstimatedWorkLoad > entry.getValue()) {
//					assignee = entry.getKey();
//					dayWithMinEstimatedWorkLoad = entry.getValue();
//				}
//			}
//			// workLoad[assignee] = estimatedWorkLoad[assignee];
//			workLoad.put(assignee, estimatedWorkLoad.get(assignee));
//			usersJobs.get(assignee).add(locations.get(j));
//			estimatedWorkLoad = copy(workLoad);
//		}
//
//	}
//
//	private Double getNearestDistance(List<String> list, String location) {
//		Double minDistance = Double.MAX_VALUE;
//		Double actualDistance = 0.0;
//		if (list.size() < 1) {
//			minDistance = 0.0;
//		}
//		Coordinates toCoordinate = hierarchyDao.getCoordByCode(location);
//		for (int i = 0; i < list.size(); i++) {
//			actualDistance = 0.0;
//			LocationDistancesDto distanceDto = distanceDao.getDistance(list.get(i), location);
//			if (distanceDto.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS)) {
//				actualDistance = distanceDto.getRoadTotalTime();
//			} else {
//				Coordinates fromCoordinate = hierarchyDao.getCoordByCode(list.get(i));
//				ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(fromCoordinate, toCoordinate);
//				if (arcResponse.getResponseMessage().getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
//					actualDistance = arcResponse.getTotalDriveTime();
//				}
//			}
//			if (actualDistance < minDistance) {
//				minDistance = actualDistance;
//			}
//		}
//		return minDistance;
//	}
//
//	private void initializeAllocationMatrix(List<String> usersList, List<String> locations,
//			HashMap<String, List<String>> usersJobs, HashMap<String, Integer> workLoad) {
//		int minDistance = Integer.MAX_VALUE;
//		int nearestWell = 0;
//		int actualDistance = 0;
//		UserIDPMappingDo user = null;
//		Double taskEstimatedTime = (double) attrTempDao.getEstTimeForSubClass(MurphyConstant.OBX_SUB_CLASSIFICATION);
//		for (int i = 0; i < usersList.size(); i++) {
//			ArrayList<String> userJobMatrix = new ArrayList<>();
//			for (int j = 0; j < locations.size(); j++) {
//				user = userIdpMappingDao.getUserByEmail(usersList.get(i));
//				nearestWell = j;
//				minDistance = 0;
//				if (!ServicesUtil.isEmpty(user.getSerialId())) {
//					Coordinates userLocation = GeoTabUtil.getLocBySerialId(user.getSerialId());
//					try {
//						if (!ServicesUtil.isEmpty(userLocation)) {
//
//							ArcGISResponseDto roadDistance = ArcGISUtil.getRoadDistance(userLocation,
//									hierarchyDao.getCoordByCode(locations.get(j)));
//							if (roadDistance.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS)) {
//								actualDistance = roadDistance.getTotalTime().intValue();
//								if (actualDistance < minDistance) {
//									nearestWell = j;
//									minDistance = actualDistance;
//								}
//							}
//							// else {
//							// nearestWell = j;
//							// minDistance = 0;
//							// }
//						}
//						// else {
//						// nearestWell = j;
//						// minDistance = 0;
//						// }
//					} catch (Exception e) {
//						logger.error("[ObxSchedulerFacadeDummy][initializeAllocationMatrix]Exception " + e.getMessage());
//					}
//				}
//			}
//			userJobMatrix.add(locations.get(nearestWell));
//			usersJobs.put(usersList.get(i), userJobMatrix);
//			// workLoad[i] = minDistance;
//			workLoad.put(usersList.get(i), minDistance + (taskEstimatedTime.intValue()));
//			locations.remove(nearestWell);
//		}
//	}
//
//	public static HashMap<String, Integer> copy(HashMap<String, Integer> original) {
//		HashMap<String, Integer> copy = new HashMap<String, Integer>();
//		for (Map.Entry<String, Integer> entry : original.entrySet()) {
//			copy.put(entry.getKey(), entry.getValue());
//		}
//		return copy;
//	}
//	
//   //will return distance adjacencyMatrix for the given wells and distance is been considered as 1 milliSecond if distance between two wells is 0 
//	private Double[][] generateAdjacencyMatrix(List<String> locationCodes) {
//		Double[][] adjacencyMatrix = new Double[locationCodes.size()][locationCodes.size()];
//		Double distance = 0.0;
//		for (int i = 0; i < locationCodes.size(); i++) {
//			for (int j = i; j < locationCodes.size(); j++) {
//				distance = 1.1;
//				if (i != j) {
//					LocationDistancesDto distanceDto = distanceDao.getDistance(locationCodes.get(i),
//							locationCodes.get(j));
//					if (distanceDto.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS)) {
//						distance = distanceDto.getRoadTotalTime();
//
//						// converting to milliSeconds to avoid 0.0 < seconds problem
//						distance = distance * 1000;
//						distance += 1.000000000001; // Adding a factor of 1.000000000001 to manage 0 Sec wells
//						// if(distance<=1){
//						// distance=1.000000000001;
//						// }
//					} else {
//						Coordinates fromCoordinate = hierarchyDao.getCoordByCode(locationCodes.get(i));
//						Coordinates toCoordinate = hierarchyDao.getCoordByCode(locationCodes.get(j));
//						ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(fromCoordinate, toCoordinate);
//						if (arcResponse.getResponseMessage().getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
//							distance = arcResponse.getTotalDriveTime();
//							// converting to milliSeconds to avoid distance>0.0 seconds problem
//							distance = distance * 1000;
//							distance += 1.000000000001;// Adding a factor of 1.000000000001 to manage 0 Sec wells
//							// if(distance<=1){
//							// distance=1.000000000001;
//							// }
//						} else {
//							logger.error("[generateAdjacencyMatrix][getRoadDistance] is not found for "
//									+ locationCodes.get(i) + "" + locationCodes.get(j));
//						}
//					}
//				} else if (i == j) {
//					distance = 0.0;
//				}
//				adjacencyMatrix[i][j] = distance;
//				adjacencyMatrix[j][i] = distance;
//			}
//			// logger.error("[generateAdjacencyMatrix]"+Arrays.toString(adjacencyMatrix[i]));
//		}
//		return adjacencyMatrix;
//	}
//
//	private void sortBasedOnRoute(HashMap<String, List<String>> userJobMap) {
//		for (Entry<String, List<String>> entry : userJobMap.entrySet()) {
//			List<String> locationList = new ArrayList<>();
//			locationList.addAll(entry.getValue());
//			// List<Coordinates> coordList =
//			// hierarchyDao.getCoordsByCodes(locationList);
//			Double[][] adjacencyMatrix = generateAdjacencyMatrix(locationList);
//			List<Integer> shortestRoute = ServicesUtil.getShortestRoute(adjacencyMatrix);
//			List<String> shortRouteLocationList = new ArrayList<String>();
//			for (int i = 0; i < shortestRoute.size(); i++) {
//				shortRouteLocationList.add(locationList.get(shortestRoute.get(i)));
//			}
//			if (shortRouteLocationList.size() != locationList.size()) {
//				try {
//					// logger.error("Failed to find Shortest path in the
//					// LocationList");
//					locationList.removeAll(shortRouteLocationList);
//					// logger.error(" missing locations "+locationList);
//					if (!ServicesUtil.isEmpty(locationList))
//						shortRouteLocationList.addAll(locationList);
//				} catch (Exception e) {
//					logger.error("[ObxSchedulerFacadeDummy][sortBasedOnRoute][Exception while sorting]" + e.getMessage());
//				}
//
//			}
//			entry.setValue(shortRouteLocationList);
//		}
//	}
//
//	/***
//	 * Finds Nearby Assigned Task for a location
//	 * <ol>
//	 * <li>Retrieve</li>
//	 * <li></li>
//	 * <li>Returns TaskID(hidden in screen), Task Description, Location (Well
//	 * Name), Est. Completion Time, Est. Travel Time, Total Duration,
//	 * Assignee(Task Owner)</li>
//	 * </ol>
//	 */
//	@Override
//	public NearByTaskListDto getNearbyAssignedTask(double latitude, double longitude, String userId) {
//		// System.err.println("ObxSchedulerFacadeDummy.getNearbyAssignedTask(" +
//		// latitude + ", " + longitude + ")");
//		NearByTaskListDto response = new NearByTaskListDto();
//		Double gainTimeOfUser = Double.parseDouble(ownersDao.getGainTimeOfUser(userId));
//		// for Hour to minute conversion
//		gainTimeOfUser = gainTimeOfUser * 60;
//		List<NearbyTaskDto> list = new ArrayList<>();
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setStatus(MurphyConstant.FAILURE);
//		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		if (latitude == 0.0 || longitude == 0.0) {
//			responseMessage.setMessage("User Location Not Found");
//		} else {
//			list = obxDao.getNearbyAssignedTask(latitude, longitude, userId);
//			for (Iterator<NearbyTaskDto> iterator = list.iterator(); iterator.hasNext();) {
//				NearbyTaskDto nearbyTaskDto = iterator.next();
//				Double estimatedTravelTime = getRoadDistance(latitude, longitude, nearbyTaskDto);
//				float estimatedCompletionTime = attrTempDao.getEstTimeForSubClass(nearbyTaskDto.getSubClassification());
//
//				// ServicesUtil.isEmpty(obj[12]) ? 0 : (Math.round((Double)
//				// obj[12]))
//				if (estimatedTravelTime != null) {
//					double requireTimeforTask = estimatedCompletionTime + estimatedTravelTime;
//					if (gainTimeOfUser >= (requireTimeforTask)) {
//						nearbyTaskDto.setEstimatedTravelTime(Math.round(estimatedTravelTime));
//						nearbyTaskDto.setEstimatedCompletionTime(estimatedCompletionTime);
//						// nearbyTaskDto.setEstimatedTotalDuration(estimatedCompletionTime
//						// + estimatedTravelTime);
//						nearbyTaskDto.setEstimatedTotalDuration(
//								nearbyTaskDto.getEstimatedTravelTime() + nearbyTaskDto.getEstimatedCompletionTime());
//					} else {
//						iterator.remove();
//					}
//				} else {
//					nearbyTaskDto.setEstimatedTotalDuration(nearbyTaskDto.getEstimatedCompletionTime());
//				}
//			}
//			responseMessage.setStatus(MurphyConstant.SUCCESS);
//			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//			responseMessage.setMessage("Task Fetched SuccessFully");
//		}
//		response.setNearByTasks(list);
//		response.setResponse(responseMessage);
//		return response;
//	}
//
//	private Double getRoadDistance(double fromLatitude, double fromLongitude, NearbyTaskDto nearbyTaskDto) {
//		// System.err.println("ObxSchedulerFacadeDummy.setRoadDistance("+fromLatitude+",
//		// "+fromLongitude+", "+nearbyTaskDto+")");
//		Coordinates fromCoordinate = new Coordinates(fromLatitude, fromLongitude);
//		Coordinates toCoordinate = new Coordinates(nearbyTaskDto.getToLatitude(), nearbyTaskDto.getToLongitude());
//		ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(fromCoordinate, toCoordinate);
//		if (arcResponse.getResponseMessage().getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
//			return arcResponse.getTotalDriveTime();
//		}
//		return null;
//	}
//
//	@Override
//	public TaskOwnersResponeDto getOBXUsers(String roles) {
//		TaskOwnersResponeDto response = new TaskOwnersResponeDto();
//		ResponseMessage responseMsg = new ResponseMessage();
//		responseMsg.setMessage("Failed to Fetch OBX users");
//		responseMsg.setStatus(MurphyConstant.FAILURE);
//		responseMsg.setStatusCode(MurphyConstant.CODE_FAILURE);
//		List<TaskOwnersDto> ownersList = new ArrayList<TaskOwnersDto>();
//		TaskOwnersDto owner = null;
//		// FieldResponseDto field = new FieldResponseDto();
//		// List<String>
//		// locationList=userIdpMappingDao.getFieldFromTechRole(roles);
//		// logger.error("locationList"+locationList);
//		// field = locDao.getFieldText(locationList, MurphyConstant.FIELD);
//		// HashMap<String, ArrayList<String>> OBXRoles =
//		// userIdpMappingDao.getOBXRoles(field.getField(),
//		// MurphyConstant.OBX_BUSINESS_ROLE);
//		// List<String> obxRoles = new ArrayList<String>();
//		// for (Entry<String, ArrayList<String>> entry : OBXRoles.entrySet()) {
//		// obxRoles.add(entry.getKey());
//		// }
//		List<GroupsUserDto> users = userIdpMappingDao.getUsersBasedOnPOTRole(MurphyConstant.OBX_BUSINESS_ROLE, roles);
//		if (!ServicesUtil.isEmpty(users)) {
//			for (GroupsUserDto userDto : users) {
//				owner = new TaskOwnersDto();
//				owner.setOwnerEmail(userDto.getUserId());
//				owner.setTaskOwnerDisplayName(userDto.getUserName());
//				owner.setGainTime(ownersDao.getGainTimeOfUser(userDto.getUserId()));
//				ownersList.add(owner);
//			}
//		}
//		responseMsg.setMessage("Users Fetched Successfully");
//		responseMsg.setStatus(MurphyConstant.SUCCESS);
//		responseMsg.setStatusCode(MurphyConstant.CODE_SUCCESS);
//		response.setResponseMessage(responseMsg);
//		response.setOwnerList(ownersList);
//		return response;
//	}
//
//	@Override
//	public UserLocationResponse getUserCurrentLocation(String userId) {
//		UserIDPMappingDo user = userIdpMappingDao.getUserByEmail(userId);
//		ResponseMessage responseMsg = new ResponseMessage();
//		responseMsg.setStatus(MurphyConstant.SUCCESS);
//		responseMsg.setMessage("location fetched successfully");
//		responseMsg.setStatusCode(MurphyConstant.CODE_SUCCESS);
//		UserLocationResponse userLocation = new UserLocationResponse();
//		Coordinates coords = new Coordinates(0.0, 0.0);
//		if (!ServicesUtil.isEmpty(user.getSerialId())) {
//			Coordinates geoResponse = GeoTabUtil.getLocBySerialId(user.getSerialId());
//			if (!ServicesUtil.isEmpty(geoResponse)) {
//				coords = geoResponse;
//			}
//		}
//		userLocation.setCoordinates(coords);
//		userLocation.setResponse(responseMsg);
//		return userLocation;
//	}
//
//	@Override
//	public ResponseMessage updateOBXTask(ObxTaskUpdateDto dto) {
//
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
//		responseMessage.setStatus(MurphyConstant.FAILURE);
//		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		if (dto != null) {
//			return obxDao.updateOBXTaskUser(dto);
//		}
//		return responseMessage;
//	}
//	
//	@Override
//	public HashMap<String, List<String>> createClusters() {
//		Calendar calendar = Calendar.getInstance();
//		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//		List<String> roleList = null;
//		List<GroupsUserDto> roleUsersList = null;
//		List<String> usersList = null;
//		HashMap<String, List<String>> jobList = null;
//		List<ObxTaskDto> obxTasks=null;
//		HashMap<String, ArrayList<String>> OBXRoles = prepareRoleAndFieldMap();
//		obxTaskDao.deleteJobs();
//		for (Entry<String, ArrayList<String>> entry : OBXRoles.entrySet()) {
//			try{
//			usersList = new ArrayList<String>();
//			roleList = new ArrayList<>();
////			locations = new ArrayList<String>();
//			obxTasks=new ArrayList<ObxTaskDto>();
//			roleList.add(entry.getKey());
//			roleUsersList = userIdpMappingDao.getUsersBasedOnRole(roleList);
//			if (!ServicesUtil.isEmpty(roleUsersList)) {
//				for (GroupsUserDto user : roleUsersList) {
//					usersList.add(user.getUserId());
//				}
//				obxTasks=obxDao.getVisitMatrixWithField(entry.getValue(), dayOfWeek);
//				if (!ServicesUtil.isEmpty(obxTasks)) {
//					divideClusters(obxTasks,usersList);
//					obxTaskDao.insertTasks(obxTasks);
//					updateTaskSequence(entry.getKey());
//					}
//				else {
//					logger.error("[ObxSchedulerFacadeDummy][createClusters] Job is not Present for " + entry.getValue());
//				}
//				}
//			 else {
//				logger.error("[ObxSchedulerFacadeDummy][createClusters] User List is Empty for" + entry.getKey());
//			}
//			logger.error("[ObxSchedulerFacadeDummy][createClusters] Job Creation Completed For " + entry.getKey());
//			}
//			catch (Exception e) {
//				logger.error("[ObxSchedulerFacadeDummy][createClusters] Exception " + e.getMessage() + " for "+entry.getKey());
//			}
//		}
//		logger.error("[ObxSchedulerFacadeDummy][createClusters] Job Creation Completed");
//		return jobList;
//	}
//	
//	@Override
//	public HashMap<String, ArrayList<String>> prepareRoleAndFieldMap(){
//		HashMap<String, String> fieldLocations = hierarchyDao.getLocationByLocType(MurphyConstant.FIELD);
//		int i = 0;
//		String fields = "";
//		for (Entry<String, String> entry : fieldLocations.entrySet()) {
//			if (i == 0) {
//				fields = entry.getKey();
//				i++;
//			} else
//				fields = fields + "," + entry.getKey();
//		}
//		HashMap<String, ArrayList<String>> OBXRoles = userIdpMappingDao.getOBXRolesWithFieldMap(fields,
//				MurphyConstant.OBX_BUSINESS_ROLE);
//		return OBXRoles;
//	}
//	
//	@Override
//	public void divideClusters(List<ObxTaskDto> tasks,List<String> usersList) throws Exception{
//	    try {
//	    	//number of clusters
//	    	 int n = usersList.size();
//	        String[] options = weka.core.Utils.splitOptions("-I 1000 -init 0");
//	        Instances dataSet = prepareDataSetForWeka(tasks);
//	        SimpleKMeans kmeans = new SimpleKMeans();
//	        kmeans.setSeed(10);
//	        kmeans.setOptions(options);
//	        kmeans.setNumClusters(n);
//	        kmeans.setPreserveInstancesOrder(true);
//	        kmeans.buildClusterer(dataSet);
//
//	        ClusterEvaluation eval = new ClusterEvaluation();
//	        eval.setClusterer(kmeans);
//
//	        //this array returns the cluster number for each instance
//	        //the array has as many elements as the number of instances in dataset
//	        int[] assignments = kmeans.getAssignments();
//
//	        int i = 0;
//	        for(int clusternum : assignments){
////	            java.lang.System.out.printf("Instance %d - > cluster %d \n", i, clusternum);
//	        	tasks.get(i).setOwnerEmail(usersList.get(clusternum));
//	        	tasks.get(i).setDriveTime(0.0);
//	        	tasks.get(i).setSequenceNumber(0);
////	            logger.error(" operator :"+usersList.get(clusternum)+" Location : "+tasks.get(i));
//	            i++;
//	        }
//
//	    } catch (Exception e) {
//	    	logger.error("Error On KMeans Analysis Exception : " +e.getMessage());
//	    	throw e;
//	    }
//	
//	}
//	
//	private Instances prepareDataSetForWeka(List<ObxTaskDto> tasks){
//		String Longitude="LONGITUDE";
//		String Latitude="LATITUDE";
//		Instances instances =null;
//		try{
//		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
//		attributes.add(new Attribute(Longitude));
//		attributes.add(new Attribute(Latitude));
//
//		instances = new Instances("Coordinates", attributes , 0);
////		dataRaw.setClassIndex(dataRaw.numAttributes() - 1); // Assuming z (z on lastindex) as classindex 
//
//		for(ObxTaskDto task:tasks){
//			Instance inst=new DenseInstance(2);
//			inst.setValue(0, task.getLongitude());
//			inst.setValue(1, task.getLatitude());
//			instances.add(inst);
//		}
////		logger.error("[ObxSchedulerFacadeDummy][prepareData] instances "+instances);
//		}
//		catch(Exception e){
//			logger.error("[ObxSchedulerFacadeDummy][prepareData]Exception While Preparing data"+e.getMessage());
//		}
//		return instances;
//	}
//
//	private List<ObxTaskDtoDummy> updateDriveTime(List<ObxTaskDtoDummy> tasks){
//		try{
//		Collections.sort(tasks,new Comparator<ObxTaskDtoDummy>() {
//			@Override
//			public int compare(ObxTaskDtoDummy o1, ObxTaskDtoDummy o2) {
//				// TODO Auto-generated method stub
//				return o1.getSequenceNumber()-o2.getSequenceNumber();
//			}
//		});
////		logger.error("tasks after sorting"+tasks);
//		for(int i=tasks.size()-1;i>0;i--){
////			logger.error("tasks.get(i).getDriveTime()"+tasks.get(i).getDriveTime());
////			logger.error("tasks.get(i).getDriveTime()"+tasks.get(i-1).getDriveTime());
//			double driveTime=tasks.get(i).getDriveTime()-tasks.get(i-1).getDriveTime();
//			tasks.get(i).setDriveTime(driveTime);
//		}}
//		catch(Exception e){
//			logger.error("[ObxSchedulerFacadeDummy][updateDriveTime] Exception "+ e.getMessage());
//			throw e;
//		}
//		
//		logger.error("tasks  while returning" + tasks);
//		return tasks;
//	}
//	// //Remove
//	// @Override
//	// public Double getDistance(String userId, String locationCode) {
//	// // TODO Auto-generated method stub
//	// Double actualDistance =null;
//	// UserIDPMappingDo user = userIdpMappingDao.getUserByEmail(userId);
//	// if (!ServicesUtil.isEmpty(user.getSerialId())) {
//	// Coordinates userLocation =
//	// GeoTabUtil.getLocBySerialId(user.getSerialId());
//	// try{
//	// if (!ServicesUtil.isEmpty(userLocation)) {
//	// ArcGISResponseDto roadDistance = ArcGISUtil
//	// .getRoadDistance(userLocation,
//	// hierarchyDao.getCoordByCode(locationCode));
//	// if(roadDistance.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS)){
//	// actualDistance = roadDistance.getTotalTime();
//	// }
//	// }}
//	// catch (Exception e) {
//	// logger.error("[initializeAllocationMatrix]Exception " +e.getMessage());
//	// }
//	// }
//	//
//	// return actualDistance;
//	// }
//}