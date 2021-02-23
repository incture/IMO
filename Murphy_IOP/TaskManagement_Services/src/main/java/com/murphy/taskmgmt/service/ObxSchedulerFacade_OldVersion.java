//package com.murphy.taskmgmt.service;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.StringJoiner;
//import java.util.TimeZone;
//import java.util.stream.Stream;
//
//import org.netlib.util.doubleW;
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
//import com.murphy.taskmgmt.dao.ObxTaskDao;
//import com.murphy.taskmgmt.dao.TaskOwnersDao;
//import com.murphy.taskmgmt.dao.UserIDPMappingDao;
//import com.murphy.taskmgmt.dto.ArcGISBestSequesnceDto;
//import com.murphy.taskmgmt.dto.ArcGISBestSequesnceListDto;
//import com.murphy.taskmgmt.dto.ArcGISResponseDto;
//import com.murphy.taskmgmt.dto.AttrTempResponseDto;
//import com.murphy.taskmgmt.dto.ClusterDto;
//import com.murphy.taskmgmt.dto.CollaborationDto;
//import com.murphy.taskmgmt.dto.ConfigDto;
//import com.murphy.taskmgmt.dto.ConfigResponseListDto;
//import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
//import com.murphy.taskmgmt.dto.CustomTaskDto;
//import com.murphy.taskmgmt.dto.FLSOPResponseDto;
//import com.murphy.taskmgmt.dto.FieldResponseDto;
//import com.murphy.taskmgmt.dto.GetObxTaskReportResponse;
//import com.murphy.taskmgmt.dto.GroupsUserDto;
//import com.murphy.taskmgmt.dto.LocationHierarchyDto;
//import com.murphy.taskmgmt.dto.NearByTaskListDto;
//import com.murphy.taskmgmt.dto.NearbyTaskDto;
//import com.murphy.taskmgmt.dto.ObxAllocationResponseDto;
//import com.murphy.taskmgmt.dto.ObxAllocationUpdateDto;
//import com.murphy.taskmgmt.dto.ObxConfigValuesDto;
//import com.murphy.taskmgmt.dto.ObxOperatorWorkloadDetailsDto;
//import com.murphy.taskmgmt.dto.ObxTaskAllocationDto;
//import com.murphy.taskmgmt.dto.ObxTaskDto;
//import com.murphy.taskmgmt.dto.ObxTaskUpdateDto;
//import com.murphy.taskmgmt.dto.ObxWorkLoadDetailsResponseDto;
//import com.murphy.taskmgmt.dto.ResponseMessage;
//import com.murphy.taskmgmt.dto.TaskEventsDto;
//import com.murphy.taskmgmt.dto.TaskListDto;
//import com.murphy.taskmgmt.dto.TaskOwnersDto;
//import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
//import com.murphy.taskmgmt.dto.WellVisitDayMatrixDto;
//import com.murphy.taskmgmt.dto.WellVisitDto;
//import com.murphy.taskmgmt.dto.WellVisitMatrixDto;
//import com.murphy.taskmgmt.entity.Node;
//import com.murphy.taskmgmt.entity.UserIDPMappingDo;
//import com.murphy.taskmgmt.scheduler.AsyncSchedulerObx;
//import com.murphy.taskmgmt.service.interfaces.ObxSchedulerFacadeLocal;
//import com.murphy.taskmgmt.service.interfaces.TaskManagementFacadeLocal;
//import com.murphy.taskmgmt.service.interfaces.TaskSchedulingCalFacadeLocal;
//import com.murphy.taskmgmt.util.ArcGISUtil;
//import com.murphy.taskmgmt.util.GeoTabUtil;
//import com.murphy.taskmgmt.util.MurphyConstant;
//import com.murphy.taskmgmt.util.ServicesUtil;
//
//import weka.classifiers.lazy.IBk;
//import weka.clusterers.ClusterEvaluation;
//import weka.clusterers.FilteredClusterer;
//import weka.clusterers.MakeDensityBasedClusterer;
//import weka.clusterers.SimpleKMeans;
//import weka.core.Attribute;
//import weka.core.DenseInstance;
//import weka.core.EuclideanDistance;
//import weka.core.Instance;
//import weka.core.Instances;
//import weka.filters.Filter;
//
//@Service("ObxSchedulerFacade")
//public class ObxSchedulerFacade_OldVersion implements ObxSchedulerFacadeLocal {
//
//	private static final Logger logger = LoggerFactory.getLogger(ObxSchedulerFacade_OldVersion.class);
//
//	@Autowired
//	private HierarchyDao hierarchyDao;
//
//	@Autowired
//	private TaskOwnersDao ownersDao;
//
//	@Autowired
//	private CustomAttrTemplateDao attrTempDao;
//
//	@Autowired
//	private ConfigDao configDao;
//
//	@Autowired
//	LocationDistancesDao distanceDao;
//
//	@Autowired
//	OBXSchedulerDao obxDao;
//
//	@Autowired
//	UserIDPMappingDao userIdpMappingDao;
//
//	@Autowired
//	ObxTaskDao obxTaskAllocationDao;
//
//	@Autowired
//	UserIDPMappingDao userMappingDao;
//
//	@Autowired
//	TaskSchedulingCalFacadeLocal taskSchedulingDao;
//
//	@Autowired
//	TaskManagementFacadeLocal taskManagementFacade;
//
//	@Autowired
//	AsyncSchedulerObx obxEngine;
//
//	private WellVisitMatrixDto initializeWellVisit(Integer days) {
//		WellVisitMatrixDto weekVisitMatrix = new WellVisitMatrixDto();
//		weekVisitMatrix.setDayMatrixList(new ArrayList<WellVisitDayMatrixDto>());
//		for (int i = 0; i < days; i++) {
//			WellVisitDayMatrixDto dayMatrix = new WellVisitDayMatrixDto();
//			dayMatrix.setWorkLoad(0.0);
//			dayMatrix.setLocationCodes(new ArrayList<String>());
//			weekVisitMatrix.getDayMatrixList().add(dayMatrix);
//		}
//		return weekVisitMatrix;
//	}
//
//	private WellVisitMatrixDto getWellVisitMatrixWithTierA(List<String> list, WellVisitMatrixDto weekVisitMatrix) {
//		for (String locationCode : list) {
//			for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i++) {
//				weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes().add(locationCode);
//				updateWellVisitMatrix(locationCode, i + 2);
//			}
//		}
//		return weekVisitMatrix;
//	}
//
//	private WellVisitMatrixDto getWellVisitMatrixWithTierB(WellVisitMatrixDto weekVisitMatrix,
//			List<String> tierBLocations) {
//		List<String> clsuterOne = new ArrayList<>();
//		List<String> clusterTwo = new ArrayList<>();
//		try {
//			getTierBClusters(tierBLocations, clsuterOne, clusterTwo);
//		} catch (Exception e) {
//			logger.error(
//					"[ObxSchedulerFacade][getWellVisitMatrixWithTierB] getTierBClusters Exception " + e.getMessage());
//		}
//		// Monday & Thursday firstBatch of locations
//		for (int i = 0; i < MurphyConstant.NUMBER_OF_DAYS; i += 3) {
//			weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes().addAll(clsuterOne);
//			updateWellVisitMatrix(clsuterOne, i + 2);
//		}
//		// Tuesday & Friday secondBatch of locations
//		for (int i = 1; i < MurphyConstant.NUMBER_OF_DAYS; i += 3) {
//			weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes().addAll(clusterTwo);
//			updateWellVisitMatrix(clusterTwo, i + 2);
//		}
//		return weekVisitMatrix;
//	}
//
//	private void getTierBClusters(List<String> tierBLocations, List<String> clsuterOne, List<String> clusterTwo)
//			throws Exception {
//		try {
//			double estimatedTaskTime = Double
//					.parseDouble(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
//			List<LocationHierarchyDto> locations = hierarchyDao.getCoordWithLocationCode(tierBLocations);
//			List<Coordinates> coordsList = new ArrayList<Coordinates>();
//			for (LocationHierarchyDto dto : locations) {
//				Coordinates coordinate = new Coordinates(dto.getLatValue(), dto.getLongValue());
//				coordsList.add(coordinate);
//			}
//			int[] clustersAssignmentsList = divideClusters(2, coordsList, estimatedTaskTime).getAssignments();
//			for (int i = 0; i < clustersAssignmentsList.length; i++) {
//				int clusterAssignment = clustersAssignmentsList[i];
//				if (clusterAssignment == 0) {
//					clsuterOne.add(locations.get(i).getLocation());
//				} else if (clusterAssignment == 1) {
//					clusterTwo.add(locations.get(i).getLocation());
//				}
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][getTierBClusters] Exception " + e.getMessage());
//			throw e;
//		}
//	}
//
//	public ClusterDto divideClusters(int noOfClusters, List<Coordinates> coordinates, double estimatedTaskTime)
//			throws Exception {
//		ClusterDto response = new ClusterDto();
//		try {
//			// number of clusters
//			String[] options = weka.core.Utils.splitOptions("-I 1000 -init 0");
//			// logger.error("coordinates"+coordinates);
//			Instances dataSet = prepareDataSetForWeka(coordinates, estimatedTaskTime);
//			// logger.error("dataSet"+dataSet);
//			SimpleKMeans kmeans = new SimpleKMeans();
//			kmeans.setSeed(10);
//			kmeans.setOptions(options);
//			kmeans.setNumClusters(noOfClusters);
//			kmeans.setPreserveInstancesOrder(true);
//			kmeans.buildClusterer(dataSet);
//
//			ClusterEvaluation eval = new ClusterEvaluation();
//			eval.setClusterer(kmeans);
//
//			// this array returns the cluster number for each instance
//			// the array has as many elements as the number of instances in
//			// dataset
//			response.setAssignments(kmeans.getAssignments());
//			response.setCenters(kmeans.getClusterCentroids());
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][divideClusters] Exception " + e.getMessage());
//			throw e;
//		}
//		return response;
//	}
//
//	private Instances prepareDataSetForWeka(List<Coordinates> locations, double stopTime) {
//		String Longitude = "LONGITUDE";
//		String Latitude = "LATITUDE";
//		String WELL_STOP = "WELL_STOP";
//		Instances instances = null;
//
//		try {
//			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
//			attributes.add(new Attribute(Longitude));
//			attributes.add(new Attribute(Latitude));
//			attributes.add(new Attribute(WELL_STOP));
//
//			instances = new Instances("Coordinates", attributes, 0);
//
//			for (Coordinates location : locations) {
//				Instance inst = new DenseInstance(attributes.size());
//				inst.setValue(0, location.getLongitude());
//				inst.setValue(1, location.getLatitude());
//				inst.setValue(2, stopTime);
//				instances.add(inst);
//			}
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerFacade][prepareData]Exception While Preparing data" + e.getMessage());
//		}
//		return instances;
//	}
//
//	private void tierCClusterDivision(WellVisitMatrixDto weekVisitMatrix, List<String> tierCWellsList) {
//		try {
//			List<LocationHierarchyDto> locations = hierarchyDao.getCoordWithLocationCode(tierCWellsList);
//			List<Coordinates> coordsList = new ArrayList<Coordinates>();
//			for (LocationHierarchyDto dto : locations) {
//				Coordinates coordinate = new Coordinates(dto.getLatValue(), dto.getLongValue());
//				coordsList.add(coordinate);
//			}
//			double estimatedTaskTime = Double
//					.parseDouble(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
//			ClusterDto clusters = divideClusters(MurphyConstant.NUMBER_OF_DAYS, coordsList, estimatedTaskTime);
//			int[] clustersAssignmentsList = clusters.getAssignments();
//			Instances[] dayCenters = new Instances[MurphyConstant.NUMBER_OF_DAYS];
//			for (int i = 0; i < weekVisitMatrix.getDayMatrixList().size(); i++) {
//				List<Coordinates> coords = hierarchyDao
//						.getCoordsByCodes(weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes());
//				dayCenters[i] = divideClusters(1, coords, estimatedTaskTime).getCenters();
//			}
//			int[] dayCluster = mapDayWithCluster(clusters.getCenters(), dayCenters, coordsList);
//			// logger.error("dayCluster"+Arrays.toString(dayCluster));
//			for (int i = 0; i < clustersAssignmentsList.length; i++) {
//				weekVisitMatrix.getDayMatrixList().get(dayCluster[clustersAssignmentsList[i]]).getLocationCodes()
//						.add(locations.get(i).getLocation());
//				updateWellVisitMatrix(locations.get(i).getLocation(), dayCluster[clustersAssignmentsList[i]] + 2);
//			}
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerFacade][tierCClusterDivision]Exception " + e.getMessage());
//		}
//	}
//
//	private int[] mapDayWithCluster(Instances centers, Instances[] dayCenters, List<Coordinates> coords) {
//		int[] response = new int[MurphyConstant.NUMBER_OF_DAYS];
//		try {
//			int clusterChoosen = 0;
//			boolean[] selected = new boolean[centers.numInstances()];
//			for (int i = 0; i < dayCenters.length; i++) {
//				Double distance = Double.MAX_VALUE;
//				Double prevDistance = Double.MAX_VALUE;
//				for (int j = 0; j < centers.numInstances(); j++) {
//					if (!selected[j]) {
//						distance = euDistance(dayCenters[i].instance(0), centers.instance(j));
//						if (distance < prevDistance) {
//							prevDistance = distance;
//							clusterChoosen = j;
//						}
//					}
//				}
//				response[i] = clusterChoosen;
//				selected[clusterChoosen] = true;
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][mapDayWithCluster] Exception " + e.getMessage());
//		}
//		return response;
//	}
//
//	// public ClusterDto divideClustersFarthestFirst(int noOfClusters,
//	// List<Coordinates> coordinates) throws Exception {
//	// ClusterDto response=new ClusterDto();
//	// try {
//	// // number of clusters
//	//// String[] options = weka.core.Utils.splitOptions("-N 3 -S 1");
//	// Instances dataSet = prepareDataSetForWeka(coordinates);
//	// String[] options = weka.core.Utils.splitOptions("-I 1000 -init 0");
//	// SimpleKMeans kmeans = new SimpleKMeans();
//	// kmeans.setSeed(10);
//	// kmeans.setOptions(options);
//	// kmeans.setNumClusters(noOfClusters);
//	// kmeans.setPreserveInstancesOrder(true);
//	// MakeDensityBasedClusterer densityBasedCluster=new
//	// MakeDensityBasedClusterer();
//	// densityBasedCluster.setClusterer(kmeans);
//	// densityBasedCluster.setMinStdDev(1.0E-6);
//	// densityBasedCluster.buildClusterer(dataSet);
//	// FilteredClusterer filteredCluster=new FilteredClusterer();
//	// Filter filter=new weka.filters.AllFilter();
//	// filteredCluster.setFilter(filter);
//	// filteredCluster.setClusterer(kmeans);
//	// filteredCluster.buildClusterer(dataSet);
//	//// FarthestFirst farthestFirst=new FarthestFirst();
//	//// farthestFirst.setNumClusters(noOfClusters);
//	//// Instances dataSet = prepareDataSetForWeka(coordinates);
//	//// SimpleKMeans kmeans = new SimpleKMeans();
//	//// farthestFirst.setSeed(10);
//	//// farthestFirst.buildClusterer(dataSet);
//	//// kmeans.setOptions(options);
//	//// kmeans.setNumClusters(noOfClusters);
//	//// kmeans.setPreserveInstancesOrder(true);
//	//// farthestFirst.buildClusterer(dataSet);
//	//
//	//// ClusterEvaluation eval = new ClusterEvaluation();
//	//// eval.setClusterer(farthestFirst);
//	//
//	// // this array returns the cluster number for each instance
//	// // the array has as many elements as the number of instances in
//	// // dataset
//	//
//	// int i=0;
//	// int[] assignments=new int[dataSet.size()];
//	// for(Instance instance:dataSet){
//	//// java.lang.System.out.printf("Instance %d - > cluster %d \n", i,
//	// clusternum);
//	// assignments[i]=densityBasedCluster.clusterInstance(instance);
//	// i++;
//	// }
//	// response.setAssignments(assignments);
//	// response.setCenters(kmeans.getClusterCentroids());
//	// } catch (Exception e) {
//	// logger.error("[ObxSchedulerFacade][divideClusters] Exception " +
//	// e.getMessage());
//	// throw e;
//	// }
//	// return response;
//	// }
//
//	private void getWellVisitMatrixWithTierC(WellVisitMatrixDto weekVisitMatrix, List<String> tierCWellsList) {
//		List<String> unAssignedWells = new ArrayList<String>();
//		HashMap<Integer, Double> estimatedWorkLoad = new HashMap<Integer, Double>();
//		Double estimatedResolveTime = Double
//				.parseDouble(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
//		Double dayCapacity = Integer.parseInt(configDao.getConfigurationByRef(MurphyConstant.NUM_OBX_OPERATOR_EFS))
//				* Double.parseDouble(configDao.getConfigurationByRef(MurphyConstant.SHIFT_DURATION));
//		dayCapacity = dayCapacity * 60;
//		// TODO: change with sequence finding with location_Distances table
//		calculateWorkLoadOfMatrix(weekVisitMatrix, estimatedResolveTime);
//		for (String tierCWell : tierCWellsList) {
//			try {
//				estimatedWorkLoad = getEstimatedWorkLoad(weekVisitMatrix, tierCWell, estimatedResolveTime);
//				assignOnMinDeltaDay(estimatedWorkLoad, dayCapacity, unAssignedWells, weekVisitMatrix, tierCWell, true);
//			} catch (Exception e) {
//				logger.error("[ObxSchedulerFacade][getWellVisitMatrixWithTierC] Exception " + e.getMessage());
//			}
//		}
//		if (!ServicesUtil.isEmpty(unAssignedWells)) {
//			boolean isAlldayCapacityExceeded = isAllDayCapacityExceeded(weekVisitMatrix, dayCapacity);
//			for (String leftOutWell : unAssignedWells) {
//				try {
//					estimatedWorkLoad = getEstimatedWorkLoad(weekVisitMatrix, leftOutWell, estimatedResolveTime);
//					if (!isAlldayCapacityExceeded) {
//						assignOnLessWorkloadDay(weekVisitMatrix, leftOutWell, estimatedWorkLoad, dayCapacity);
//						isAlldayCapacityExceeded = isAllDayCapacityExceeded(weekVisitMatrix, dayCapacity);
//					} else {
//						assignOnMinDeltaDay(estimatedWorkLoad, dayCapacity, unAssignedWells, weekVisitMatrix,
//								leftOutWell, false);
//					}
//				} catch (Exception e) {
//					logger.error("[ObxSchedulerFacade][getWellVisitMatrixWithTierC] Exception " + e.getMessage());
//				}
//			}
//		}
//	}
//
//	// for days without breaching workload
//	private void assignOnMinDeltaDay(HashMap<Integer, Double> estimatedWorkLoad, Double dayCapacity,
//			List<String> unAssignedWells, WellVisitMatrixDto weekVisitMatrix, String location,
//			boolean considerDayCapacity) {
//		int minDeltaDay = 0;
//		double deltaValue = Double.MAX_VALUE;
//		double deltaValueOfTheDay = Double.MAX_VALUE;
//		for (int i = 0; i < estimatedWorkLoad.size(); i++) {
//			deltaValueOfTheDay = estimatedWorkLoad.get(i) - weekVisitMatrix.getDayMatrixList().get(i).getWorkLoad();
//			if (deltaValue > deltaValueOfTheDay) {
//				minDeltaDay = i;
//				deltaValue = minDeltaDay;
//			}
//		}
//		Double workLoadOnMinDeltaDay = weekVisitMatrix.getDayMatrixList().get(minDeltaDay).getWorkLoad();
//		if (considerDayCapacity) {
//			for (int i = 0; i < estimatedWorkLoad.size(); i++) {
//				deltaValueOfTheDay = estimatedWorkLoad.get(i) - weekVisitMatrix.getDayMatrixList().get(i).getWorkLoad();
//				if (deltaValue > deltaValueOfTheDay) {
//					minDeltaDay = i;
//					deltaValue = minDeltaDay;
//				}
//			}
//			if (workLoadOnMinDeltaDay >= dayCapacity) {
//				unAssignedWells.add(location);
//			} else {
//				weekVisitMatrix.getDayMatrixList().get(minDeltaDay).getLocationCodes().add(location);
//				weekVisitMatrix.getDayMatrixList().get(minDeltaDay).setWorkLoad(estimatedWorkLoad.get(minDeltaDay));
//				updateWellVisitMatrix(location, minDeltaDay + 2);
//			}
//		} else {
//			// int size=Integer.MAX_VALUE;
//			// for(int i=0;i<weekVisitMatrix.getDayMatrixList().size();i++){
//			// if(size>weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes().size()){
//			// size=weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes().size();
//			// minDeltaDay=i;
//			// }
//			// }
//			weekVisitMatrix.getDayMatrixList().get(minDeltaDay).getLocationCodes().add(location);
//			weekVisitMatrix.getDayMatrixList().get(minDeltaDay).setWorkLoad(estimatedWorkLoad.get(minDeltaDay));
//			updateWellVisitMatrix(location, minDeltaDay + 2);
//		}
//	}
//
//	// if SomeDays Workload is breached then assign to the day with workload
//	// left and min difference in workload
//	private void assignOnLessWorkloadDay(WellVisitMatrixDto weekVisitMatrix, String location,
//			HashMap<Integer, Double> estimatedWorkLoad, Double dayCapacity) {
//		int minDeltaDay = 0;
//		double deltaValue = Double.MAX_VALUE;
//		double deltaValueOfTheDay = Double.MAX_VALUE;
//		for (int i = 0; i < estimatedWorkLoad.size(); i++) {
//			deltaValueOfTheDay = estimatedWorkLoad.get(i) - weekVisitMatrix.getDayMatrixList().get(i).getWorkLoad();
//			if (deltaValue > deltaValueOfTheDay && (dayCapacity > estimatedWorkLoad.get(i))) {
//				minDeltaDay = i;
//				deltaValue = deltaValueOfTheDay;
//			}
//		}
//		weekVisitMatrix.getDayMatrixList().get(minDeltaDay).getLocationCodes().add(location);
//		weekVisitMatrix.getDayMatrixList().get(minDeltaDay).setWorkLoad(estimatedWorkLoad.get(minDeltaDay));
//		updateWellVisitMatrix(location, minDeltaDay + 2);
//	}
//
//	// to check if all day capacity is exceeded
//	private boolean isAllDayCapacityExceeded(WellVisitMatrixDto weekVisitMatrix, double dayCapacity) {
//		boolean isCapacityExceededForAll = true;
//		boolean isDayOverLoaded = false;
//		for (int i = 0; i < weekVisitMatrix.getDayMatrixList().size(); i++) {
//			isDayOverLoaded = false;
//			if (weekVisitMatrix.getDayMatrixList().get(i).getWorkLoad() >= dayCapacity) {
//				isDayOverLoaded = true;
//				// logger.error("[ObxSchedulerFacade][isAllDayCapacityExceeded]
//				// Capacity Exceeded on Day " + i
//				// + " Capacity " +
//				// weekVisitMatrix.getDayMatrixList().get(i).getWorkLoad());
//			}
//			isCapacityExceededForAll = isCapacityExceededForAll && isDayOverLoaded;
//		}
//		return isCapacityExceededForAll;
//	}
//
//	private HashMap<Integer, Double> getEstimatedWorkLoad(WellVisitMatrixDto weekVisitMatrix, String tierCWell,
//			Double estimatedResolveTime) {
//		HashMap<Integer, Double> estimatedWorkLoad = new HashMap<Integer, Double>();
//		try {
//			Map<Integer, Double> distanceMap = getIndexDistanceMap(weekVisitMatrix, tierCWell);
//			Double workLoad = Double.MAX_VALUE;
//			for (Entry<Integer, Double> dayDistance : distanceMap.entrySet()) {
//				workLoad = weekVisitMatrix.getDayMatrixList().get(dayDistance.getKey()).getWorkLoad()
//						+ (dayDistance.getValue() / 60) + estimatedResolveTime;
//				estimatedWorkLoad.put(dayDistance.getKey(), workLoad);
//				workLoad = Double.MAX_VALUE;
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][getEstimatedWorkLoad] Exception While Calculating Workload "
//					+ e.getMessage());
//		}
//		return estimatedWorkLoad;
//	}
//
//	private void calculateWorkLoadOfMatrix(WellVisitMatrixDto weekVisitMatrix, double estimatedResolveTime) {
//		try {
//			for (int i = 0; i < weekVisitMatrix.getDayMatrixList().size(); i++) {
//				List<Coordinates> coordsList = hierarchyDao
//						.getCoordsByCodes(weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes());
//				if (!ServicesUtil.isEmpty(coordsList) && coordsList.size() >= 2) {
//					// TODO: change with sequence finding with
//					// location_Distances table
//					ArcGISBestSequesnceListDto bestRoute = ArcGISUtil.getRoadDistance_BestRoute(coordsList,
//							MurphyConstant.ARCGIS_SERVICE_BEST_ROUTE_PAYLOAD);
//					// Cumulative time will come in Seconds
//					// Convert to Minutes
//					double cumulativeTimeInMinute = bestRoute.getCumulativeDriveTime() / 60;
//					double workLoad = cumulativeTimeInMinute
//							+ (weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes().size()
//									* estimatedResolveTime);
//					weekVisitMatrix.getDayMatrixList().get(i).setWorkLoad(workLoad);
//				} else {
//					logger.error(
//							"[ObxSchedulerFacade][calculateWorkLoadOfMatrix] Matrix Coordinates not found or less than 2 "
//									+ weekVisitMatrix.getDayMatrixList().get(i).getLocationCodes() + " day " + i);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][calculateWorkLoadOfMatrix] Exception " + e.getMessage());
//		}
//	}
//
//	// private void calculateWorkLoadOfMatrix(WellVisitMatrixDto
//	// weekVisitMatrix) {
//	// Double workLoad = 0.0;
//	// Double taskEstimatedTime = (double)
//	// attrTempDao.getEstTimeForSubClass(MurphyConstant.OBX_SUB_CLASSIFICATION);
//	// for (int i = 0; i < weekVisitMatrix.getDayMatrixList().size(); i++) {
//	// workLoad = 0.0;
//	// WellVisitDayMatrixDto dayMatrix =
//	// weekVisitMatrix.getDayMatrixList().get(i);
//	// for (int j = 0; j < dayMatrix.getLocationCodes().size() - 1; j++) {
//	// LocationDistancesDto distanceDto =
//	// distanceDao.getDistance(dayMatrix.getLocationCodes().get(j),
//	// dayMatrix.getLocationCodes().get(j + 1));
//	// Double distance = 0.0;
//	// if
//	// (distanceDto.getResponseMessage().getStatus().equals(MurphyConstant.SUCCESS))
//	// {
//	// distance = distanceDto.getRoadTotalTime();
//	// distance = distance / 60;
//	// }
//	// workLoad += distance;
//	// workLoad += taskEstimatedTime;
//	// }
//	// dayMatrix.setWorkLoad(workLoad);
//	// }
//	// }
//
//	@Override
//	public void generateWellVisitMatrix() {
//		try {
//			obxDao.deleteAllWellVisitData();
//
//			HashMap<String, List<String>> wellsWithTier = hierarchyDao.getAllWellsLocationCodeAndTier();
//			// for the fields
//			WellVisitMatrixDto wellVisitMatrix = initializeWellVisit(MurphyConstant.NUMBER_OF_DAYS);
//			if (wellsWithTier.containsKey(MurphyConstant.TIER_A)) {
//				try {
//					getWellVisitMatrixWithTierA(wellsWithTier.get(MurphyConstant.TIER_A), wellVisitMatrix);
//					logger.error("[ObxSchedulerFacade][generateWellVisitMatrix] Well Visit Completed For Tier A");
//				} catch (Exception e) {
//					logger.error(
//							"[ObxSchedulerFacade][generateWellVisitMatrix] Exception While building Well Visit for Tier A"
//									+ e.getMessage());
//				}
//			}
//			if (wellsWithTier.containsKey(MurphyConstant.TIER_B)) {
//				try {
//					getWellVisitMatrixWithTierB(wellVisitMatrix, wellsWithTier.get(MurphyConstant.TIER_B));
//					logger.error("[ObxSchedulerFacade][generateWellVisitMatrix] Well Visit Completed For Tier B");
//				} catch (Exception e) {
//					logger.error(
//							"[ObxSchedulerFacade][generateWellVisitMatrix] Exception While building Well Visit for Tier B"
//									+ e.getMessage());
//				}
//			}
//			if (wellsWithTier.containsKey(MurphyConstant.TIER_C)) {
//				try {
//					getWellVisitMatrixWithTierC(wellVisitMatrix, wellsWithTier.get(MurphyConstant.TIER_C));
//					// tierCClusterDivision(wellVisitMatrix,
//					// wellsWithTier.get(MurphyConstant.TIER_C));
//					logger.error("[ObxSchedulerFacade][generateWellVisitMatrix] Well Visit Completed For Tier C");
//				} catch (Exception e) {
//					logger.error(
//							"[ObxSchedulerFacade][generateWellVisitMatrix] Exception While building Well Visit for Tier C"
//									+ e.getMessage());
//				}
//			}
//			// updateWellVisitMatrix(wellVisitMatrix);
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][generateWellVisitMatrix] Exception While building Well Visit"
//					+ e.getMessage());
//		}
//	}
//
//	@Override
//	public String updateWellVisitMatrix(String locationCode, int day) {
//		String response = MurphyConstant.FAILURE;
//		WellVisitDto dto = new WellVisitDto();
//		dto.setDay(day);
//		dto.setLocationCode(locationCode);
//		List<String> locationCodes = new ArrayList<String>();
//		locationCodes.add(locationCode);
//		try {
//			dto.setField(hierarchyDao.getFieldText(locationCodes, MurphyConstant.WELL).getField());
//			obxDao.create(dto);
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][updateWellVisitMatrix] Exception " + e.getMessage());
//		}
//		response = MurphyConstant.SUCCESS;
//		return response;
//	}
//
//	@Override
//	public String updateWellVisitMatrix(List<String> locationCodes, int day) {
//		String response = MurphyConstant.FAILURE;
//		for (String locationCode : locationCodes) {
//			WellVisitDto dto = new WellVisitDto();
//			dto.setDay(day);
//			dto.setLocationCode(locationCode);
//			List<String> locations = new ArrayList<String>();
//			locations.add(locationCode);
//			try {
//				dto.setField(hierarchyDao.getFieldText(locations, MurphyConstant.WELL).getField());
//				obxDao.create(dto);
//			} catch (Exception e) {
//				logger.error("[ObxSchedulerFacade][updateWellVisitMatrix] Exception " + e.getMessage());
//			}
//			response = MurphyConstant.SUCCESS;
//		}
//		return response;
//	}
//
//	// Task Creation
//
//	// @Override
//	// public void CreateTaskAllocation(){
//	// obxTaskAllocationDao.deleteJobs();
//	// try{
//	// List<Integer> days=obxTaskAllocationDao.getDays();
//	// for(int day:days){
//	// try{
//	// createClusters(day);
//	// updateSequence(day);
//	// assignUsersToCluster(day);
//	// } catch (Exception e) {
//	// logger.error("[ObxSchedulerFacade][assignUsersToCluster] Exception
//	// "+e.getMessage()+"for day"+day);
//	// }
//	// }
//	// }
//	// catch(Exception e){
//	// logger.error("[ObxSchedulerFacade][CreateTaskAllocation] Exception
//	// "+e.getMessage());
//	// }
//	// }
//
//	// @Override
//	// public void createClusters(int dayOfWeek) {
//	// try {
//	// List<ObxTaskDto> dayTasks =
//	// obxDao.getVisitMatrixWithFieldForDay(dayOfWeek);
//	// HashMap<String,String> fieldAndObxRoleMap=new HashMap<String,String>();
//	// int numberOfClusters = Integer
//	// .parseInt(configDao.getConfigurationByRef(MurphyConstant.NUM_OBX_OPERATOR_EFS));
//	// double estimatedTaskTime = Double
//	// .parseDouble(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
//	// List<Coordinates> coordinates = new ArrayList<Coordinates>();
//	// for (ObxTaskDto task : dayTasks) {
//	// coordinates.add(new Coordinates(task.getLatitude(),
//	// task.getLongitude()));
//	// }
//	// int[] assignments = divideClusters(numberOfClusters, coordinates);
//	// int i = 0;
//	// for (int clusterNumber : assignments) {
//	// ObxTaskDto task = dayTasks.get(i);
//	// task.setClusterNumber(clusterNumber + 1);
//	// task.setEstimatedTaskTime(estimatedTaskTime);
//	// String role="";
//	// if(fieldAndObxRoleMap.containsKey(task.getField())){
//	// role=fieldAndObxRoleMap.get(task.getField());
//	// }
//	// else
//	// {
//	// if(ServicesUtil.isEmpty(task.getField())){
//	// logger.error("field is empty for "+task.getLocationCode()+" on
//	// "+task.getDay());
//	// }
//	// else{
//	// role=userIdpMappingDao.getOBXRoles(task.getField(),
//	// MurphyConstant.OBX_BUSINESS_ROLE).get(0);
//	// fieldAndObxRoleMap.put(task.getField(), role);}
//	// }
//	// task.setRole(role);
//	// task.setIsObxOperator("false");
//	// try {
//	// obxTaskAllocationDao.create(task);
//	// }
//	// catch (Exception e) {
//	// logger.error("[ObxSchedulerFacade][createClusters] Exception while
//	// inserting " + e.getMessage() + " for "+dayTasks.get(i));
//	// }
//	// i++;
//	// }
//	// } catch (Exception e) {
//	// logger.error("[ObxSchedulerFacade][createClusters] Exception " +
//	// e.getMessage());
//	// }
//	// }
//
//	@Override
//	public void updateSequence(int day) {
//		int numberOfClusters = Integer.parseInt(configDao.getConfigurationByRef(MurphyConstant.NUM_OBX_OPERATOR_EFS));
//		for (int i = 1; i <= numberOfClusters; i++) {
//			List<ObxTaskDto> obxTasksOfCluster = obxTaskAllocationDao.getTasksOfCluster(i, day);
//			List<String> locationCodes = new ArrayList<String>();
//			for (ObxTaskDto task : obxTasksOfCluster) {
//				locationCodes.add(task.getLocationCode());
//			}
//			// updateSequenceAndDriveTime(obxTasksOfCluster);
//			// obxTaskAllocationDao.updateTasks(obxTasksOfCluster);
//			updateSequenceAndDriveTimeWithStagedData(obxTasksOfCluster);
//		}
//	}
//
//	public void updateSequenceAndDriveTime(List<ObxTaskDto> obxTasks) {
//		List<String> locationList = new ArrayList<>();
//		for (ObxTaskDto tasks : obxTasks) {
//			locationList.add(tasks.getLocationCode());
//		}
//		String coordInString = configDao.getConfigurationByRef(obxTasks.get(0).getRole());
//		List<String> coords = Arrays.asList(coordInString.split(","));
//		Coordinates startingPoint = new Coordinates();
//		startingPoint.setLatitude(Double.parseDouble(coords.get(0)));
//		startingPoint.setLongitude(Double.parseDouble(coords.get(1)));
//		List<Coordinates> coordList = new ArrayList<Coordinates>();
//		coordList.add(startingPoint);
//		for (ObxTaskDto task : obxTasks) {
//			Coordinates taskCoord = new Coordinates(task.getLatitude(), task.getLongitude());
//			coordList.add(taskCoord);
//		}
//		try {
//			// TODO: change with sequence finding with location_Distances table
//			ArcGISBestSequesnceListDto bestRoute = new ArcGISBestSequesnceListDto();
//			try {
//				bestRoute = ArcGISUtil.getRoadDistance_BestRoute(coordList,
//						MurphyConstant.ARCGIS_SERVICE_BEST_ROUTE_PAYLOAD_WITH_FIRSTSTOP);
//			} catch (Exception e) {
//				logger.error(
//						"[ObxSchedulerFacade][findSequence] getRoadDistance_BestRoute Exception " + e.getMessage());
//			}
//			if (MurphyConstant.SUCCESS.equals(bestRoute.getResponseMessage().getStatus())) {
//				for (int i = 1; i < bestRoute.getBestSequenceList().size(); i++) {
//					ArcGISBestSequesnceDto dto = bestRoute.getBestSequenceList().get(i);
//					int taskIndex = dto.getLocationId() - 2;
//					obxTasks.get(taskIndex).setSequenceNumber(dto.getSequenceNumber() - 1);
//					obxTasks.get(taskIndex).setDriveTime(dto.getTotalDriveTime() / 60);
//				}
//			}
//			updateDriveTime(obxTasks);
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][findSequence] Exception " + e.getMessage());
//			throw e;
//		}
//	}
//
//	public void updateSequenceAndDriveTimeWithStagedData(List<ObxTaskDto> obxTasks) {
//		String coordInString = configDao.getConfigurationByRef(obxTasks.get(0).getRole());
//		List<String> coords = Arrays.asList(coordInString.split(","));
//		ObxTaskDto centralFacility = new ObxTaskDto();
//		centralFacility.setLatitude(Double.parseDouble(coords.get(0)));
//		centralFacility.setLongitude(Double.parseDouble(coords.get(1)));
//		List<ObxTaskDto> taskListWithDepot = new ArrayList<>();
//		taskListWithDepot.add(centralFacility);
//		taskListWithDepot.addAll(obxTasks);
//		try {
//			List<String> bestRoute;
//			// TODO: change with sequence finding with location_Distances table
//			// ArcGISBestSequesnceListDto bestRoute = new
//			// ArcGISBestSequesnceListDto();
//			bestRoute = obxTaskSequence(taskListWithDepot);
//			logger.error("bestRoute" + bestRoute);
//			// updateDriveTime(obxTasks);
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][findSequence] Exception " + e.getMessage());
//			throw e;
//		}
//	}
//
//	private void updateDriveTime(List<ObxTaskDto> tasks) {
//		try {
//			Collections.sort(tasks, new Comparator<ObxTaskDto>() {
//				@Override
//				public int compare(ObxTaskDto o1, ObxTaskDto o2) {
//					return o1.getSequenceNumber() - o2.getSequenceNumber();
//				}
//			});
//			for (int i = tasks.size() - 1; i > 0; i--) {
//				double driveTime = tasks.get(i).getDriveTime() - tasks.get(i - 1).getDriveTime();
//				tasks.get(i).setDriveTime(driveTime);
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][updateDriveTime] Exception " + e.getMessage());
//			throw e;
//		}
//	}
//
//	@Override
//	public void assignUsersToCluster(int dayOfWeek) {
//		try {
//			double workLoad = Double.parseDouble(configDao.getConfigurationByRef(MurphyConstant.SHIFT_DURATION));
//			double taskEstimatedTime = Double
//					.parseDouble(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
//			workLoad = workLoad * 60;
//			StringJoiner joiner = new StringJoiner(",");
//			List<String> allFields = hierarchyDao.getAllFields();
//			for (String field : allFields)
//				joiner.add(field);
//			String fields = joiner.toString();
//			List<String> obxRoles = userIdpMappingDao.getOBXRoles(fields, MurphyConstant.OBX_BUSINESS_ROLE);
//			for (String role : obxRoles) {
//				HashMap<Integer, List<ObxTaskDto>> clusterTaskMap = obxTaskAllocationDao.getTasksOfRoleAndDay(role,
//						dayOfWeek);
//				List<String> roleList = new ArrayList<String>();
//				roleList.add(role);
//				List<GroupsUserDto> users = userIdpMappingDao.getUsersBasedOnRole(roleList);
//				int i = 0;
//				for (Entry<Integer, List<ObxTaskDto>> entry : clusterTaskMap.entrySet()) {
//					if (i < users.size()) {
//						String userID = users.get(i).getUserId();
//						double userWorkload = 0.0;
//						for (ObxTaskDto task : entry.getValue()) {
//							userWorkload = userWorkload + ((task.getDriveTime()) + taskEstimatedTime);
//							if (userWorkload < workLoad && role.equals(task.getRole())) {
//								task.setOwnerEmail(userID);
//								try {
//									// obxTaskAllocationDao.update(task);
//									obxTaskAllocationDao.updateOwner(task.getDay(), task.getLocationCode(), userID,
//											true);
//								} catch (Exception e) {
//									logger.error(
//											"[ObxSchedulerFacade][assignUsersToCluster] Exception " + e.getMessage());
//								}
//							} else if (userWorkload >= workLoad || (!role.equals(task.getRole()))) {
//								break;
//							}
//
//						}
//					}
//					i++;
//				}
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][assignUsersToCluster] Exception " + e.getMessage());
//		}
//	}
//
//	public Map<Integer, Double> getIndexDistanceMap(WellVisitMatrixDto allBucket, String locationCode) {
//		Map<String, Double> locationCordinateMap = obxDao.getToLocationCodeforBucket(locationCode);
//		Map<Integer, Double> indexDistMap = new HashMap<Integer, Double>();
//		int i = 0;
//		for (WellVisitDayMatrixDto list : allBucket.getDayMatrixList()) {
//			Double mindist = Double.MAX_VALUE;
//			for (String well : list.getLocationCodes()) {
//				if (locationCordinateMap.containsKey(well)) {
//					if (locationCordinateMap.get(well) < mindist) {
//						mindist = locationCordinateMap.get(well);
//
//					}
//				}
//			}
//			if (mindist != Double.MAX_VALUE) {
//				indexDistMap.put(i, mindist);
//			} else {
//				indexDistMap.put(i, Double.MAX_VALUE);
//			}
//			i++;
//		}
//		return indexDistMap;
//	}
//
//	@Override
//	public void issueObxTasks() {
//		Calendar calendar = Calendar.getInstance();
//		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//		try {
//			HashMap<String, List<ObxTaskDto>> userTasksMap = obxTaskAllocationDao.getTasksOfDayForUSers(dayOfWeek);
//			for (Entry<String, List<ObxTaskDto>> userTask : userTasksMap.entrySet()) {
//				Collections.sort(userTask.getValue(), new Comparator<ObxTaskDto>() {
//					@Override
//					public int compare(ObxTaskDto o1, ObxTaskDto o2) {
//						return o1.getSequenceNumber() - o2.getSequenceNumber();
//					}
//				});
//				for (ObxTaskDto task : userTask.getValue()) {
//					createOBXTask(userTask.getKey(), task.getLocationCode());
//				}
//			}
//			logger.error("[ObxSchedulerFacade][issueObxTasks] Tasks Creation Completed ");
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][issueObxTasks] Exception " + e.getMessage());
//		}
//	}
//
//	@Override
//	public ResponseMessage createOBXTask(String userId, String location) {
//		ResponseMessage response = new ResponseMessage();
//		CustomTaskDto customTaskDto = new CustomTaskDto();
//		TaskEventsDto taskEventsDto = new TaskEventsDto();
//		try {
//			TaskOwnersDto owner = new TaskOwnersDto();
//			owner.setEstResolveTime(
//					Double.parseDouble(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS)));
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
//			String locationText = hierarchyDao.getLocationByLocCode(location);
//			attributeValues.put("123", locationText);
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
//			// Adding description
//			taskEventsDto.setDescription("OBX Task : " + locationText);
//
//			customTaskDto.setTaskEventDto(taskEventsDto);
//			FLSOPResponseDto flsop = taskManagementFacade.getFLSOP(MurphyConstant.OBX,
//					MurphyConstant.OBX_SUB_CLASSIFICATION);
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
//			// Addition of Non Dispatch task for that location
//			customTaskDto.setNdTaskList(obxDao.getNDTaskForOBX(locationText));
//			response = taskSchedulingDao.createTask(customTaskDto);
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacadeDummy][createOBXTask][Error]" + e.getMessage());
//		}
//		return response;
//
//	}
//
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
//	public TaskOwnersResponeDto getOBXUsers(String roles) {
//		TaskOwnersResponeDto response = new TaskOwnersResponeDto();
//		ResponseMessage responseMsg = new ResponseMessage();
//		responseMsg.setMessage("Failed to Fetch OBX users");
//		responseMsg.setStatus(MurphyConstant.FAILURE);
//		responseMsg.setStatusCode(MurphyConstant.CODE_FAILURE);
//		List<TaskOwnersDto> ownersList = new ArrayList<TaskOwnersDto>();
//		TaskOwnersDto owner = null;
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
//	// To update run and save Config values OBX task scheduler
//	public ResponseMessage runObxEngineTask(ObxConfigValuesDto dto) {
//		ResponseMessage response = new ResponseMessage();
//		response.setStatus(MurphyConstant.FAILURE);
//		response.setStatusCode(MurphyConstant.CODE_FAILURE);
//		ObxConfigValuesDto obxResponseDto = new ObxConfigValuesDto();
//		try {
//			if (!ServicesUtil.isEmpty(dto)) {
//				// Checking if the OBX engine flag is already running
//				// if not the updating the configuration value else sending
//				// message to UI.
//				if (!(configDao.getConfigurationByRef(MurphyConstant.OBX_ENGINE_RUNNING_FLAG)
//						.equalsIgnoreCase(MurphyConstant.TRUE))) {
//					/*
//					 * for (ConfigDto configDto : dto.getConfigDto()) response =
//					 * configDao.saveOrUpdateConfigByRef(configDto.getConfigId()
//					 * , configDto.getConfigValue());
//					 */
//					configDao.saveOrUpdateConfigByRef(MurphyConstant.OBX_ENGINE_UPDATED_BY,
//							dto.getObxEngineUpdatedBy());
//					configDao.saveOrUpdateConfigByRef(MurphyConstant.LAST_UPDATED_DATE_TIME,
//							dto.getLastUpdatedDateTime());
//					configDao.saveOrUpdateConfigByRef(MurphyConstant.SHIFT_DURATION, dto.getShiftDuration());
//					configDao.saveOrUpdateConfigByRef(MurphyConstant.UPLOAD_WORKFACTOR_PERCENT,
//							dto.getUploadWorkFactor());
//					configDao.saveOrUpdateConfigByRef(MurphyConstant.DURATION_STOP_BY_WELLS,
//							dto.getDurationStopWells());
//					// res[5] =
//					// configDao.saveOrUpdateConfigByRef(MurphyConstant.NUM_OBX_OPERATOR_EFS,
//					// dto.getNumObxOperator());
//					response = configDao.saveOrUpdateConfigByRef(MurphyConstant.OBX_ENGINE_RUNNING_FLAG,
//							dto.getObxEngineRunningFlag());
//
//					if (response.getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
//						obxEngine.runObxEngine();
//					}
//					response.setStatus(MurphyConstant.SUCCESS);
//					response.setStatusCode(MurphyConstant.CODE_SUCCESS);
//					response.setMessage("OBX Engine is Running");
//					obxResponseDto.setResponseMessage(response);
//
//				} else {
//					response.setMessage("Please wait as OBX Engine is already running!");
//				}
//			}
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerFacade][runObxEngineTask] Exception " + e.getMessage());
//			configDao.saveOrUpdateConfigByRef(MurphyConstant.OBX_ENGINE_RUNNING_FLAG, "false");
//		}
//		return response;
//	}
//
//	// Fetching the updated config values
//	public List<ConfigResponseListDto> getUpdatedConfig() {
//		List<ConfigDto> configDtoUpdatedList = new ArrayList<>();
//		List<ConfigResponseListDto> configDtoResponseList = new ArrayList<>();
//		ConfigResponseListDto conDto = new ConfigResponseListDto();
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
//		responseMessage.setStatus(MurphyConstant.FAILURE);
//		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		try {
//			String[] obxConfigConst = new String[] { MurphyConstant.DURATION_STOP_BY_WELLS,
//					MurphyConstant.SHIFT_DURATION, MurphyConstant.UPLOAD_WORKFACTOR_PERCENT,
//					MurphyConstant.LAST_UPDATED_DATE_TIME, MurphyConstant.OBX_ENGINE_RUNNING_FLAG,
//					MurphyConstant.OBX_ENGINE_UPDATED_BY };
//			ConfigDto[] updatedConfigDto = new ConfigDto[obxConfigConst.length];
//			for (int i = 0; i < obxConfigConst.length; i++) {
//				updatedConfigDto[i] = new ConfigDto();
//				updatedConfigDto[i].setConfigId(obxConfigConst[i]);
//				if (obxConfigConst[i].equals(MurphyConstant.LAST_UPDATED_DATE_TIME)) {
//					if (ServicesUtil.isEmpty(configDao.getConfigurationByRef(MurphyConstant.LAST_UPDATED_DATE_TIME))) {
//						updatedConfigDto[i].setConfigValue(ServicesUtil.convertFromZoneToZoneString(new Date(), null,
//								MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
//								MurphyConstant.DATE_DB_FORMATE));
//					} else {
//						updatedConfigDto[i].setConfigValue(configDao.getConfigurationByRef(obxConfigConst[i]));
//					}
//				} else {
//					updatedConfigDto[i].setConfigValue(configDao.getConfigurationByRef(obxConfigConst[i]));
//				}
//				configDtoUpdatedList.add(updatedConfigDto[i]);
//			}
//
//			if (ServicesUtil.isEmpty(configDtoUpdatedList)) {
//				responseMessage.setMessage(MurphyConstant.NO_RESULT);
//			} else {
//				HashMap<String, String> configResponseMap = new HashMap<>();
//				for (ConfigDto s : configDtoUpdatedList) {
//					configResponseMap.put(s.getConfigId(), s.getConfigValue());
//				}
//				conDto.setResponseConfigMap(configResponseMap);
//				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
//			}
//			responseMessage.setStatus(MurphyConstant.SUCCESS);
//			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//			conDto.setResponseMessage(responseMessage);
//			configDtoResponseList.add(conDto);
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerFacade][getUpdatedConfig] Exception " + e.getMessage());
//		}
//		return configDtoResponseList;
//	}
//
//	// Fetching the task allocated table for the field
//	public ObxAllocationResponseDto getObxTaskAllocationDetails(String role, String field, String selectedDay) {
//
//		List<ObxTaskAllocationDto> obxDtoUpdatedList = null;
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
//		responseMessage.setStatus(MurphyConstant.FAILURE);
//		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		ObxAllocationResponseDto responseDto = new ObxAllocationResponseDto();
//		boolean isSpecificDay = true;
//		try {
//			List<GroupsUserDto> obxGrpUserDtos = new ArrayList<GroupsUserDto>();
//			Calendar currentDate = Calendar.getInstance(); // set this up
//															// however you need
//															// it.
//			int day = currentDate.get(Calendar.DAY_OF_WEEK);
//			if (MurphyConstant.ALL.equalsIgnoreCase(field)) {
//				field = "%";
//				StringJoiner joiner = new StringJoiner(",");
//				List<String> allFields = hierarchyDao.getAllFields();
//				for (String fields : allFields)
//					joiner.add(fields);
//				String fieldsInString = joiner.toString();
//				obxGrpUserDtos = userIdpMappingDao.getUsersBasedOnPOTRole(MurphyConstant.OBX_BUSINESS_ROLE,
//						fieldsInString);
//			} else {
//				field = field + "%";
//				obxGrpUserDtos = userIdpMappingDao.getUsersBasedOnRole(MurphyConstant.OBX_BUSINESS_ROLE, field);
//			}
//			if (MurphyConstant.ALL.equalsIgnoreCase(selectedDay)) {
//				isSpecificDay = false;
//			} else
//				day = getDayOfWeek(selectedDay);
//
//			obxDtoUpdatedList = obxDao.getObxTaskAllocatedDetails(day, field, isSpecificDay);
//
//			// Getting the whole operator list
//			List<String> roleList = new ArrayList<>();
//			roleList.add(MurphyConstant.USER_TYPE_FIELD);
//			List<GroupsUserDto> proGrpUserDtos = new ArrayList<GroupsUserDto>();
//			proGrpUserDtos = userIdpMappingDao.getUsersBasedOnRole(roleList);
//			Collections.sort(proGrpUserDtos, new NameComparator());
//			responseDto.setProOperatorList(proGrpUserDtos);
//
//			/*
//			 * StringJoiner joiner=new StringJoiner(","); List<String>
//			 * allFields=hierarchyDao.getAllFields(); for(String
//			 * fields:allFields) joiner.add(fields); String
//			 * newFields=joiner.toString(); List<String>
//			 * obxRoles=userIdpMappingDao.getOBXRoles(newFields,MurphyConstant.
//			 * OBX_BUSINESS_ROLE);
//			 */
//
//			// Getting the unassigned wells
//			BigInteger unAssignedWells = obxDao.getUnassignedWellsCount(day, field, isSpecificDay);
//
//			Collections.sort(obxGrpUserDtos, new NameComparator());
//			responseDto.setObxOperatorList(obxGrpUserDtos);
//
//			if (ServicesUtil.isEmpty(obxDtoUpdatedList)) {
//				responseMessage.setMessage(MurphyConstant.NO_RESULT);
//			} else {
//				responseDto.setUnAssignedWellsNum(unAssignedWells);
//				responseDto.setObxList(obxDtoUpdatedList);
//				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
//			}
//			responseMessage.setStatus(MurphyConstant.SUCCESS);
//			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//			responseDto.setResponseMessage(responseMessage);
//			// }
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerFacade][getObxTaskAllocationDetails] Exception " + e.getMessage());
//		}
//		return responseDto;
//	}
//
//	// Fetching the workload details of the operators
//	public ObxWorkLoadDetailsResponseDto getObxOperatorWorkDetails(String field, String selectedDay) {
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
//		responseMessage.setStatus(MurphyConstant.FAILURE);
//		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		List<ObxOperatorWorkloadDetailsDto> obxWorkLoadDtoResponseList = new ArrayList<ObxOperatorWorkloadDetailsDto>();
//		ObxWorkLoadDetailsResponseDto responseDto = new ObxWorkLoadDetailsResponseDto();
//		ObxOperatorWorkloadDetailsDto obxWorkLoadDto = new ObxOperatorWorkloadDetailsDto();
//		try {
//			List<GroupsUserDto> users = new ArrayList<>();
//			Calendar myDate = Calendar.getInstance(); // set this up however you
//														// need it.
//			int day = myDate.get(Calendar.DAY_OF_WEEK);
//			boolean isForWeek = true;
//			if (!ServicesUtil.isEmpty(field) && !ServicesUtil.isEmpty(selectedDay)) {
//				if (MurphyConstant.ALL.equalsIgnoreCase(field)) {
//					StringJoiner joiner = new StringJoiner(",");
//					List<String> allFields = hierarchyDao.getAllFields();
//					for (String fields : allFields)
//						joiner.add(fields);
//					field = joiner.toString();
//					users = userIdpMappingDao.getUsersBasedOnPOTRole(MurphyConstant.OBX_BUSINESS_ROLE, field);
//				} else {
//					field = field + "%";
//					users = userIdpMappingDao.getUsersBasedOnRole(MurphyConstant.OBX_BUSINESS_ROLE, field);
//				}
//				if (!MurphyConstant.ALL.equalsIgnoreCase(selectedDay)) {
//					day = getDayOfWeek(selectedDay);
//					isForWeek = false;
//				}
//				if (!ServicesUtil.isEmpty(users)) {
//					Collections.sort(users, new NameComparator());
//					for (GroupsUserDto user : users) {
//						obxWorkLoadDto = obxDao.getWorkloadDetails(user.getUserId(), day, user.getFirstName(),
//								user.getLastName(), isForWeek);
//						// obxWorkLoadDtoResponseList.addAll(obxWorkLoadDtoList);
//						obxWorkLoadDtoResponseList.add(obxWorkLoadDto);
//					}
//					if (ServicesUtil.isEmpty(obxWorkLoadDtoResponseList)) {
//						responseMessage.setMessage(MurphyConstant.NO_RESULT);
//					} else {
//						responseDto.setObxWorkLoadList(obxWorkLoadDtoResponseList);
//						responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
//					}
//					responseMessage.setStatus(MurphyConstant.SUCCESS);
//					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//					responseDto.setResponseMessage(responseMessage);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerFacade][getObxOperatorWorkDetails] Exception " + e.getMessage());
//		}
//		return responseDto;
//	}
//
//	// Converting day of the week and getting corresponding integer value
//	private int getDayOfWeek(String day) {
//		assert day != null;
//		final String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
//		for (int i = 0; i < days.length; i++)
//			if (days[i].equalsIgnoreCase(day))
//				return i + 1;
//		return -1;
//	}
//
//	@Override
//	public ResponseMessage updateUserSequence(int day, String userEmail) {
//		ResponseMessage response = new ResponseMessage();
//		try {
//			response.setMessage("Update Success");
//			response.setStatus(MurphyConstant.SUCCESS);
//			response.setStatusCode(MurphyConstant.CODE_SUCCESS);
//			List<ObxTaskDto> obxTasksOfCluster = obxTaskAllocationDao.getTasksOfUser(day, userEmail);
//			List<String> locationCodes = new ArrayList<String>();
//			for (ObxTaskDto task : obxTasksOfCluster) {
//				locationCodes.add(task.getLocationCode());
//			}
//			// TODO : replace with finding Sequence
//			updateSequenceAndDriveTime(obxTasksOfCluster);
//			obxTaskAllocationDao.updateTasks(obxTasksOfCluster);
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerFacade][updateUserSequence] Exception " + e.getMessage());
//			response.setMessage("Update Failure");
//			response.setStatus(MurphyConstant.FAILURE);
//			response.setStatusCode(MurphyConstant.CODE_FAILURE);
//		}
//		return response;
//	}
//
//	// Updating the DB and Fetching the updated workload details for the
//	// operators.
//	public ResponseMessage updateTaskForOperator(ObxAllocationUpdateDto dto) {
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
//		responseMessage.setStatus(MurphyConstant.FAILURE);
//		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		try {
//			Date currentDate = new Date();
//			Calendar c = Calendar.getInstance();
//			c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
//			c.setTime(currentDate);
//			int currentHour = c.get(Calendar.HOUR_OF_DAY);
//			if (currentHour >= 7)
//				c.add(Calendar.DATE, 1);
//			Date currentTime = c.getTime();
//			c.set(Calendar.HOUR_OF_DAY, 5);
//			c.set(Calendar.MINUTE, 30);
//			c.set(Calendar.SECOND, 0);
//			c.set(Calendar.MILLISECOND, 0);
//			Date endOfDay = c.getTime();
//			// if(currentTime.before(endOfDay)){
//			if (!ServicesUtil.isEmpty(dto.getTaskOwnerEmail()) && !ServicesUtil.isEmpty(dto.getUpdatedByEmail())
//					&& !ServicesUtil.isEmpty(dto.getSelectedDay()) && !ServicesUtil.isEmpty(dto.getLocationCode())) {
//				int day = getDayOfWeek(dto.getSelectedDay());
//				String exsitingOwner = obxDao.getExsitingOwner(dto.getLocationCode(), day);
//				String response = obxDao.updateTaskForOperator(dto.getTaskOwnerEmail(), dto.getUpdatedByEmail(),
//						dto.getLocationCode(), day, dto.getIsObxUser());
//				updateUserSequence(day, dto.getTaskOwnerEmail());
//				if (!ServicesUtil.isEmpty(exsitingOwner)) {
//					updateUserSequence(day, exsitingOwner);
//				}
//				if (response.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
//					responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
//					responseMessage.setStatus(MurphyConstant.SUCCESS);
//					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//				}
//			}
//			// }
//			// else{
//			// responseMessage.setMessage(MurphyConstant.TIME_EXCEEDED);
//			// }
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerFacade][updateTaskForOperator] Exception " + e.getMessage());
//		}
//		return responseMessage;
//	}
//
//	@Override
//	public void generateWellVisitMatrix_Field() {
//		try {
//			obxDao.deleteAllWellVisitData();
//			HashMap<String, List<String>> wellsWithTier = null;
//			// for the fields
//			WellVisitMatrixDto wellVisitMatrix = new WellVisitMatrixDto();
//			// List<String> tildenFields =new ArrayList<>();
//			// tildenFields.addAll(Arrays.asList(MurphyConstant.TILDEN_FIELDS));
//			// List<String> karnesFields=new ArrayList<>();
//			// karnesFields.addAll(Arrays.asList(MurphyConstant.KARNES_FIELDS));
//			List<String> catarinaFields = new ArrayList<>();
//			catarinaFields.addAll(Arrays.asList(MurphyConstant.CATARINA_FIELDS));
//			List<String> wellLocations = new ArrayList<>();
//			List<List<String>> fieldsList = new ArrayList<>();
//			// fieldsList.add(tildenFields);
//			// fieldsList.add(karnesFields);
//			fieldsList.add(catarinaFields);
//			for (List<String> field : fieldsList) {
//				wellVisitMatrix = initializeWellVisit(MurphyConstant.NUMBER_OF_DAYS);
//				// wellVisitMatrix = initializeWellVisit();
//				wellLocations = hierarchyDao.getLocCodeByLocationTypeAndCode(MurphyConstant.FIELD, field);
//				wellsWithTier = hierarchyDao.getTierMap(wellLocations);
//				if (wellsWithTier.containsKey(MurphyConstant.TIER_A)) {
//					try {
//						getWellVisitMatrixWithTierA(wellsWithTier.get(MurphyConstant.TIER_A), wellVisitMatrix);
//					} catch (Exception e) {
//						logger.error(
//								"[ObxSchedulerFacade][generateWellVisitMatrix] Exception While building Well Visit for Tier A"
//										+ e.getMessage());
//					}
//				}
//				if (wellsWithTier.containsKey(MurphyConstant.TIER_B)) {
//					try {
//						getWellVisitMatrixWithTierB(wellVisitMatrix, wellsWithTier.get(MurphyConstant.TIER_B));
//					} catch (Exception e) {
//						logger.error(
//								"[ObxSchedulerFacade][generateWellVisitMatrix] Exception While building Well Visit for Tier B"
//										+ e.getMessage());
//					}
//				}
//				if (wellsWithTier.containsKey(MurphyConstant.TIER_C)) {
//					try {
//						tierCClusterDivision(wellVisitMatrix, wellsWithTier.get(MurphyConstant.TIER_C));
//					} catch (Exception e) {
//						logger.error(
//								"[ObxSchedulerFacade][generateWellVisitMatrix] Exception While building Well Visit for Tier C"
//										+ e.getMessage());
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][generateWellVisitMatrix] Exception While building Well Visit"
//					+ e.getMessage());
//		}
//	}
//
//	@Override
//	public void CreateTaskAllocationField() {
//		obxTaskAllocationDao.deleteJobs();
//		try {
//			List<Integer> days = obxTaskAllocationDao.getDays();
//			// get fields
//			List<GroupsUserDto> users = new ArrayList<GroupsUserDto>();
//			HashMap<String, ArrayList<String>> obxRoleAndFieldMap = getUniqueFields();
//			boolean takeTierC = false;
//			taskAllocationFieldCreation(days, users, obxRoleAndFieldMap, takeTierC);
//
//			// Commented and Created a new function
//			// taskAllocationFieldCreation()
//			/*
//			 * for (int day : days) { for (Entry<String, ArrayList<String>>
//			 * roleAndFieldList : obxRoleAndFieldMap.entrySet()) { users =
//			 * userIdpMappingDao.getUsersBasedOnRole(Arrays.asList(
//			 * roleAndFieldList.getKey())); try { createClusters(day,
//			 * roleAndFieldList.getValue(), users.size(),
//			 * roleAndFieldList.getKey()); updateSequence(day, users.size(),
//			 * roleAndFieldList.getValue()); } catch (Exception e) { logger.
//			 * error("[ObxSchedulerFacade][CreateTaskAllocation] Exception " +
//			 * e.getMessage() + "for " + day + " roleAndFieldList " +
//			 * roleAndFieldList); } } try { assignUsersToCluster(day); } catch
//			 * (Exception e) { logger.
//			 * error("[ObxSchedulerFacade][CreateTaskAllocation] Exception while assignUsersToCluster "
//			 * + e.getMessage()); } }
//			 */
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][CreateTaskAllocation] Exception " + e.getMessage());
//		}
//		// INC0080580 Fix : SOC
//		finally {
//			configDao.saveOrUpdateConfigByRef(MurphyConstant.OBX_ENGINE_RUNNING_FLAG, "false");
//		}
//		// INC0080580 Fix : EOC
//	}
//
//	private HashMap<String, ArrayList<String>> getUniqueFields() {
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
//	public void createClusters(int dayOfWeek, List<String> field, int numberOfClusters, String obxRole,
//			boolean takeTierC) {
//		try {
//			List<ObxTaskDto> dayTasks = obxDao.getVisitMatrixWithFieldForDay(dayOfWeek, field, takeTierC);
//			HashMap<String, String> fieldAndObxRoleMap = new HashMap<String, String>();
//			// int numberOfClusters = Integer
//			// .parseInt(configDao.getConfigurationByRef(MurphyConstant.NUM_OBX_OPERATOR_EFS));
//			double estimatedTaskTime = Double
//					.parseDouble(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
//			List<Coordinates> coordinates = new ArrayList<Coordinates>();
//			for (ObxTaskDto task : dayTasks) {
//				coordinates.add(new Coordinates(task.getLatitude(), task.getLongitude()));
//			}
//
//			String coordInString = configDao.getConfigurationByRef(obxRole);
//			List<String> coords = Arrays.asList(coordInString.split(","));
//			Coordinates startingPoint = new Coordinates();
//			startingPoint.setLatitude(Double.parseDouble(coords.get(0)));
//			startingPoint.setLongitude(Double.parseDouble(coords.get(1)));
//
//			// int[] assignments = divideClusters(numberOfClusters,
//			// coordinates).getAssignments();
//
//			logger.error("[ObxSchedulerFacade][createClusters] coordinates " + coordinates);
//			ClusterDto clusters = divideClusters(numberOfClusters, coordinates, estimatedTaskTime);
//			int[] assignments = balancedKMeans(clusters.getCenters(), coordinates, numberOfClusters, startingPoint)
//					.getAssignments();
//			// int[] assignments = balancedKMeansExp(clusters.getCenters(),
//			// coordinates, numberOfClusters,startingPoint).getAssignments();
//			// int[] assignments =clusters.getAssignments();
//			int i = 0;
//			for (int clusterNumber : assignments) {
//				ObxTaskDto task = dayTasks.get(i);
//				task.setClusterNumber(clusterNumber + 1);
//				task.setEstimatedTaskTime(estimatedTaskTime);
//				task.setIsObxOperator("false");
//				String role = "";
//				task.setStatus("SCHEDULED");
//				if (fieldAndObxRoleMap.containsKey(task.getField())) {
//					role = fieldAndObxRoleMap.get(task.getField());
//				} else {
//					if (ServicesUtil.isEmpty(task.getField())) {
//						logger.error("field is empty for " + task.getLocationCode() + " on " + task.getDay());
//					} else {
//						role = userIdpMappingDao.getOBXRoles(task.getField(), MurphyConstant.OBX_BUSINESS_ROLE).get(0);
//						fieldAndObxRoleMap.put(task.getField(), role);
//					}
//				}
//				task.setRole(role);
//				try {
//					obxTaskAllocationDao.create(task);
//				} catch (Exception e) {
//					logger.error("[ObxSchedulerFacade][createClusters] Exception while inserting " + e.getMessage()
//							+ " for " + dayTasks.get(i));
//				}
//				i++;
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][createClusters] Exception " + e.getMessage());
//		}
//	}
//
//	@Override
//	public void updateSequence(int day, int numberOfClusters, List<String> field) {
//		// int numberOfClusters =
//		// Integer.parseInt(configDao.getConfigurationByRef(MurphyConstant.NUM_OBX_OPERATOR_EFS));
//		try {
//			for (int i = 1; i <= numberOfClusters; i++) {
//				List<ObxTaskDto> obxTasksOfCluster = obxTaskAllocationDao.getTasksOfCluster(i, day, field);
//				List<String> locationCodes = new ArrayList<String>();
//				for (ObxTaskDto task : obxTasksOfCluster) {
//					locationCodes.add(task.getLocationCode());
//				}
//				// TODO : replace with finding Sequence
//				updateSequenceAndDriveTime(obxTasksOfCluster);
//				obxTaskAllocationDao.updateTasks(obxTasksOfCluster);
//				// updateSequenceAndDriveTimeWithStagedData(obxTasksOfCluster);
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][updateSequence] Exception " + e.getMessage());
//		}
//	}
//
//	// Sorting alphabetically in terms of first name
//	static class NameComparator implements Comparator<GroupsUserDto> {
//		@Override
//		public int compare(GroupsUserDto g1, GroupsUserDto g2) {
//			return g1.getFirstName().compareToIgnoreCase(g2.getFirstName());
//		}
//	}
//
//	// Sorting alphabetically in terms of first name
//	static class WorkLoadSortComparator implements Comparator<ObxOperatorWorkloadDetailsDto> {
//		@Override
//		public int compare(ObxOperatorWorkloadDetailsDto g1, ObxOperatorWorkloadDetailsDto g2) {
//			return g1.getObxOperatorfullName().compareToIgnoreCase(g2.getObxOperatorfullName());
//		}
//	}
//
//	public List<String> obxTaskSequence(List<ObxTaskDto> locationList) {
//
//		List<String> shotestpath = new ArrayList<String>();
//
//		try {
//
//			int NoOfVehicles = 1;
//			int VehicleCap = 0;
//
//			int NoOfWells = locationList.size();
//
//			// Initialise
//			ObxTaskDto Depot = (ObxTaskDto) locationList.get(0);
//
//			Node[] Nodes = new Node[NoOfWells + 1];
//			Node depot = new Node(Depot.getLatitude(), Depot.getLongitude());
//			// double lat =0.0, log =0.0 ;
//			Nodes[0] = depot;
//			int i = 1;
//			for (ObxTaskDto obx : locationList) {
//
//				Nodes[i] = new Node(i, obx.getLatitude(), obx.getLongitude(), obx.getLocationCode());
//				System.out.println(obx.getLongitude() + "," + obx.getLatitude() + ";");
//				i++;
//
//			}
//
//			double[][] distanceMatrix = new double[NoOfWells + 1][NoOfWells + 1];
//
//			for (int k = 1; k <= NoOfWells; k++) {
//				for (int j = k + 1; j <= NoOfWells; j++) // The table is
//															// summetric to the
//															// first diagonal
//				{ // Use this to compute distances in O(n/2)
//					// System.out.println("Yo code"+Nodes[k].Code+"-->Form
//					// code"+ Nodes[j].Code);
//					// System.out.println("To lat"+Nodes[k].Node_X+"to
//					// long"+Nodes[k].Node_X+"from lat"+ Nodes[j].Node_X+"from
//					// lan "+Nodes[j].Node_Y);
//					double distance = obxDao.getLocationDistance(Nodes[k].Code, Nodes[j].Code);
//					distanceMatrix[k][j] = distance;
//					distanceMatrix[j][k] = distance;
//				}
//			}
//
//			OBXSolution s = new OBXSolution(NoOfWells, NoOfVehicles, VehicleCap);
//
//			s.GreedySolution(Nodes, distanceMatrix);
//
//			shotestpath = s.SolutionData();
//
//		} catch (Exception e) {
//
//		}
//		return shotestpath;
//	}
//
//	// Checking flag if Obx engine running or not for the excel download
//	// feature.
//	public ResponseMessage getRunningObxFlag() {
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
//		responseMessage.setStatus(MurphyConstant.FAILURE);
//		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		try {
//			String response = obxDao.getObxEngineRunningFlag();
//			if (!ServicesUtil.isEmpty(response)) {
//				responseMessage.setMessage(response);
//				responseMessage.setStatus(MurphyConstant.SUCCESS);
//				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][getRunningObxFlag] [Exception] " + e.getMessage());
//		}
//		return responseMessage;
//	}
//
//	public ClusterDto balancedKMeans(Instances clusterCentroids, List<Coordinates> coords, int numberOfClusters,
//			Coordinates centralFacility) {
//		ClusterDto response = new ClusterDto();
//		int[] assignments = new int[coords.size()];
//		double estimatedTaskTime = Double
//				.parseDouble(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
//
//		double workLoad = Double.parseDouble(configDao.getConfigurationByRef(MurphyConstant.SHIFT_DURATION));
//		workLoad = workLoad * 60;
//		logger.error("workLoad" + workLoad);
//		Instances instances = prepareDataSetForWeka(coords, estimatedTaskTime);
//		int[] noOfWells = new int[numberOfClusters];
//		double[] daysWorkload = new double[numberOfClusters];
//		try {
//			boolean isAssigned[] = new boolean[coords.size()];
//			boolean done = false;
//			int i = 0;
//			int wellSelected = 0;
//			while (!done) {
//				// for(int i=0;i<clusterCentroids.numInstances();i++){
//				double distance = Double.MAX_VALUE;
//				double prevDistance = Double.MAX_VALUE;
//				for (int j = 0; j < instances.size(); j++) {
//					if (!isAssigned[j]) {
//						distance = euDistance(instances.get(j), clusterCentroids.get(i));
//						// logger.error(" euDistance
//						// "+euDistance(instances.get(j),
//						// clusterCentroids.get(i))+" Weka Distance "+
//						// distance);
//						if (distance < prevDistance) {
//							prevDistance = distance;
//							wellSelected = j;
//						}
//						// if(distance<prevDistance){
//						// prevDistance=distance;
//						// wellSelected=j;
//						// }
//					}
//				}
//				// Instance newCenter = new DenseInstance(2);
//				// centralFacility.setLongitude((centralFacility.getLongitude()+coords.get(wellSelected).getLongitude())/2);
//				// centralFacility.setLatitude((centralFacility.getLongitude()+coords.get(wellSelected).getLatitude())/2);
//				// newCenter.setValue(0, centralFacility.getLongitude());
//				// newCenter.setValue(1, centralFacility.getLatitude());
//				// double
//				// distanceFromRevicedCenter=euDistance(clusterCentroids.get(i),
//				// instances.get(wellSelected));
//				// Instance newCenter = new DenseInstance(2);
//				// Double
//				// longitude=(clusterCentroids.get(i).value(0)+coords.get(wellSelected).getLongitude())/2;
//				// Double
//				// latitude=(clusterCentroids.get(i).value(1)+coords.get(wellSelected).getLatitude())/2;
//				// newCenter.setValue(0, longitude);s
//				// newCenter.setValue(1, latitude);
//				// clusterCentroids.set(i, newCenter);
//				assignments[wellSelected] = i;
//				isAssigned[wellSelected] = true;
//				daysWorkload[i] += prevDistance + estimatedTaskTime;
//				noOfWells[i]++;
//				// clusterCentroids.set(i, newCenter);
//				// wellSelected=0;
//				if (daysWorkload[i] >= workLoad) {
//					if (daysWorkload[i] > workLoad)
//						isAssigned[wellSelected] = false;
//					i++;
//					if (i == clusterCentroids.numInstances()) {
//						done = true;
//					}
//				}
//				wellSelected = 0;
//			}
//			response.setAssignments(assignments);
//			response.setCenters(clusterCentroids);
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][balancedKMeans] [Exception] " + e.getMessage());
//		}
//		return response;
//	}
//
//	private double euDistance(Instance point1, Instance point2) {
//		Double distance = Double.MAX_VALUE;
//		Coordinates coord = new Coordinates();
//		try {
//			coord.setLongitude(point1.value(0) - point2.value(0));
//			coord.setLatitude(point1.value(1) - point2.value(1));
//			distance = Math.sqrt(
//					(coord.getLatitude()) * (coord.getLatitude()) + (coord.getLongitude()) * (coord.getLongitude()));
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][euDistance] [Exception] " + e.getMessage());
//		}
//		return distance;
//	}
//
//	public ClusterDto balancedKMeansExp(Instances clusterCentroids, List<Coordinates> coords, int numberOfClusters,
//			Coordinates centralFacility) {
//		ClusterDto response = new ClusterDto();
//		int[] assignments = new int[coords.size()];
//		double estimatedTaskTime = Double
//				.parseDouble(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
//
//		double workLoad = Double.parseDouble(configDao.getConfigurationByRef(MurphyConstant.SHIFT_DURATION));
//		workLoad = workLoad * 60;
//		Instances instances = prepareDataSetForWeka(coords, estimatedTaskTime);
//		double[] daysWorkload = new double[numberOfClusters];
//		try {
//			boolean isAssigned[] = new boolean[coords.size()];
//			for (int k = 0; k < instances.size();) {
//				double distance = Double.MAX_VALUE;
//				double prevDistance = Double.MAX_VALUE;
//				int clusterSelected = 0;
//				for (int j = 0; j < clusterCentroids.numInstances(); j++) {
//					if (!isAssigned[k] && daysWorkload[j] >= workLoad) {
//						distance = euDistance(instances.get(k), clusterCentroids.get(j));
//						if (distance < prevDistance) {
//							prevDistance = distance;
//							clusterSelected = j;
//						}
//					}
//				}
//				assignments[k] = clusterSelected;
//				daysWorkload[clusterSelected] += prevDistance + estimatedTaskTime;
//				if (daysWorkload[clusterSelected] > workLoad) {
//					isAssigned[k] = false;
//				} else {
//					isAssigned[k] = true;
//					k++;
//				}
//			}
//			response.setAssignments(assignments);
//			response.setCenters(clusterCentroids);
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][balancedKMeansExp] [Exception] " + e.getMessage());
//		}
//		return response;
//	}
//
//	// INC0080580 Fix : SOC
//	@Override
//	public void updateConfig() {
//		try {
//			configDao.saveOrUpdateConfigByRef(MurphyConstant.OBX_ENGINE_RUNNING_FLAG, "true");
//			configDao.saveOrUpdateConfigByRef(MurphyConstant.OBX_ENGINE_UPDATED_BY, MurphyConstant.SYSTEM);
//			configDao.saveOrUpdateConfigByRef(MurphyConstant.LAST_UPDATED_DATE_TIME,
//					ServicesUtil.convertFromZoneToZoneString(new Date(), null, MurphyConstant.UTC_ZONE,
//							MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE));
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][updateConfig] [Exception] " + e.getMessage());
//		}
//
//	}
//	// INC0080580 Fix : EOC
//
//	// @Override
//	// public void reAssignWells(List<String> field){
//	// try{
//	// double workLoad =
//	// Double.parseDouble(configDao.getConfigurationByRef(MurphyConstant.SHIFT_DURATION));
//	// workLoad = workLoad * 60;
//	// for(int i=2;i<=6;i++){
//	// List<ObxTaskDto>
//	// unAssignedWells=obxTaskAllocationDao.getUnassignedWells(i,field);
//	// if(unAssignedWells.size()>0){
//	// List<ObxOperatorWorkloadDetailsDto>
//	// usersWithLessWorkload=obxTaskAllocationDao.getUsersWithLessWorkload(i,
//	// field);
//	// double unAssignedWorkLoad=0.0;
//	// int unAssignedWellIndex=0;
//	// int lessWorkloadUserIndex=0;
//	// double leastWorkLoad=0.0;
//	// for(int j=0;j<usersWithLessWorkload.size();j++){
//	// ObxOperatorWorkloadDetailsDto userDetail=usersWithLessWorkload.get(j);
//	// if(userDetail.getObxOperatorfullName().equals("")){
//	// unAssignedWorkLoad=userDetail.getWorkLoad();
//	// unAssignedWellIndex=j;
//	// if(j==0){
//	// lessWorkloadUserIndex++;
//	// }
//	// }
//	// else if(lessWorkloadUserIndex==j){
//	// leastWorkLoad=userDetail.getWorkLoad();
//	// }
//	// }
//	// if(leastWorkLoad+unAssignedWorkLoad<=workLoad){
//	// for(ObxTaskDto unassigned:unAssignedWells){
//	// obxTaskAllocationDao.updateOwner(i, unassigned.getLocationCode(),
//	// usersWithLessWorkload.get(lessWorkloadUserIndex).getObxOperatorfullName(),
//	// true);
//	// }
//	// updateUserSequence(i,
//	// usersWithLessWorkload.get(lessWorkloadUserIndex).getObxOperatorfullName());
//	// }
//	// else{
//	// double userWorkLoad=0.0;
//	// lessWorkloadUserIndex=0;
//	// boolean isupdated=false;
//	// for(ObxTaskDto unassigned:unAssignedWells){
//	// if(lessWorkloadUserIndex!=unAssignedWellIndex){
//	// userWorkLoad=usersWithLessWorkload.get(lessWorkloadUserIndex).getWorkLoad()+unassigned.getDriveTime()+unassigned.getEstimatedTaskTime();
//	// usersWithLessWorkload.get(lessWorkloadUserIndex).setWorkLoad(userWorkLoad);
//	// if(userWorkLoad<workLoad){
//	// obxTaskAllocationDao.updateOwner(i, unassigned.getLocationCode(),
//	// usersWithLessWorkload.get(lessWorkloadUserIndex).getObxOperatorfullName(),
//	// true);
//	// isupdated=true;
//	// }
//	// else if(userWorkLoad==workLoad){
//	// obxTaskAllocationDao.updateOwner(i, unassigned.getLocationCode(),
//	// usersWithLessWorkload.get(lessWorkloadUserIndex).getObxOperatorfullName(),
//	// true);
//	// updateUserSequence(i,
//	// usersWithLessWorkload.get(lessWorkloadUserIndex).getObxOperatorfullName());
//	// userWorkLoad=0.0;
//	// lessWorkloadUserIndex++;
//	// }
//	// else{
//	// userWorkLoad=0.0;
//	// lessWorkloadUserIndex++;
//	// if(isupdated)
//	// updateUserSequence(i,
//	// usersWithLessWorkload.get(lessWorkloadUserIndex).getObxOperatorfullName());
//	// }
//	//
//	// if(lessWorkloadUserIndex>=usersWithLessWorkload.size()){
//	// logger.error("[ObxSchedulerFacade][reAssignWells] All users are assigned
//	// with Maximum Workload ");
//	// break;
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	//
//	// }catch (Exception e) {
//	// logger.error("[ObxSchedulerFacade][reAssignWells] [Exception] " +
//	// e.getMessage());
//	// }
//	// }
//
//	// Creating cluster and Updating Sequence
//	public void taskAllocationFieldCreation(List<Integer> days, List<GroupsUserDto> users,
//			HashMap<String, ArrayList<String>> obxRoleAndFieldMap, boolean takeTierC) {
//		for (int day : days) {
//			for (Entry<String, ArrayList<String>> roleAndFieldList : obxRoleAndFieldMap.entrySet()) {
//				users = userIdpMappingDao.getUsersBasedOnRole(Arrays.asList(roleAndFieldList.getKey()));
//				try {
//					createClusters(day, roleAndFieldList.getValue(), users.size(), roleAndFieldList.getKey(),
//							takeTierC);
//					updateSequence(day, users.size(), roleAndFieldList.getValue());
//				} catch (Exception e) {
//					logger.error("[ObxSchedulerFacade][CreateTaskAllocation][taskAllocationFieldCreation] Exception "
//							+ e.getMessage() + "for " + day + " roleAndFieldList " + roleAndFieldList);
//				}
//			}
//			try {
//				assignUsersToCluster(day);
//			} catch (Exception e) {
//				logger.error(
//						"[ObxSchedulerFacade][CreateTaskAllocation][taskAllocationFieldCreation] Exception while assignUsersToCluster "
//								+ e.getMessage());
//			}
//		}
//	}
//
//	@Override
//	public void revokeObxTasks() {
//		try {
//			obxTaskAllocationDao.revokeObxTasks();
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][revokeObxTasks] [Exception] " + e.getMessage());
//		}
//	}
//
//	// For Incorporating revoked task and creating new task allocation
//	@Override
//	public void revokedTaskAllocation_Field() {
//		try {
//			List<Integer> days = obxTaskAllocationDao.getDays();
//			// get fields
//			List<GroupsUserDto> users = new ArrayList<GroupsUserDto>();
//			// For Revoked Task to create cluster again
//			List<String> unresolvedLocCodes = obxDao.getUnResolvedTaskLocation();
//			if (!ServicesUtil.isEmpty(unresolvedLocCodes)) {
//				HashMap<String, List<String>> unresolvedTierWellMap = hierarchyDao.getTierMap(unresolvedLocCodes);
//				List<String> revokedTierCWellsList = unresolvedTierWellMap.get(MurphyConstant.TIER_C);
//				Calendar calendar = Calendar.getInstance();
//				calendar.add(Calendar.DATE, -1);
//				int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
//				List<Integer> restDays = new ArrayList<>();
//				for (int day : days) {
//					if (day > currentDay)
//						restDays.add(day);
//				}
//				if (!ServicesUtil.isEmpty(restDays)) {
//					List<String> restLocCodes = obxDao.getRestDaysLocationCode(restDays);
//					if (!ServicesUtil.isEmpty(restLocCodes)) {
//						HashMap<String, List<String>> tierWellMap = hierarchyDao.getTierMap(restLocCodes);
//						List<String> restDaysTierCWellsList = tierWellMap.get(MurphyConstant.TIER_C);
//						if (!ServicesUtil.isEmpty(restDaysTierCWellsList)) {
//							boolean takeTierC = true;
//							List<String> allDaysTierCLocCodes = new ArrayList<>();
//							Stream.of(revokedTierCWellsList, restDaysTierCWellsList)
//									.forEach(allDaysTierCLocCodes::addAll);
//							 logger.error("revokedTierCWellsList " +
//							 revokedTierCWellsList.size() + " restDaysTierCWellsList"
//							 + restDaysTierCWellsList.size() + " allDaysTierCLocCodes" + allDaysTierCLocCodes.size());
//							List<LocationHierarchyDto> tierCLocDto = hierarchyDao
//									.getCoordWithLocationCode(allDaysTierCLocCodes);
//							List<Coordinates> coordsList = new ArrayList<Coordinates>();
//							for (LocationHierarchyDto dto : tierCLocDto) {
//								Coordinates coordinate = new Coordinates(dto.getLatValue(), dto.getLongValue());
//								coordsList.add(coordinate);
//							}
//							double estimatedTaskTime = Double.parseDouble(
//									configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
//							ClusterDto clusters = divideClusters(restDays.size(), coordsList, estimatedTaskTime);
//							int[] clustersCAssignmentsList = clusters.getAssignments();
//
//							obxDao.deleteOrUpdateTierCWellVisit(restDays, clustersCAssignmentsList,
//									restDaysTierCWellsList, allDaysTierCLocCodes);
//
//							FieldResponseDto dto = hierarchyDao.getFieldText(allDaysTierCLocCodes, MurphyConstant.WELL);
//							HashMap<String, ArrayList<String>> obxRoleAndRestFieldMap = userIdpMappingDao
//									.getOBXRolesWithFieldMap(dto.getField(), MurphyConstant.OBX_BUSINESS_ROLE);
//							taskAllocationFieldCreation(restDays, users, obxRoleAndRestFieldMap, takeTierC);
//						}
//					}
//				}
//			} else {
//				logger.error("[ObxSchedulerFacade][revokedTaskAllocation_Field][No Unresolved task]");
//			}
//		} catch (Exception e) {
//			logger.error("[ObxSchedulerFacade][revokedTaskAllocation_Field] Exception " + e.getMessage());
//		}
//		// INC0080580 Fix : SOC
//		finally {
//			configDao.saveOrUpdateConfigByRef(MurphyConstant.OBX_ENGINE_RUNNING_FLAG, "false");
//		}
//		// INC0080580 Fix : EOC
//	}
//
//	@Override
//	public GetObxTaskReportResponse getObxTaskReport(String date) {
//		GetObxTaskReportResponse getObxTaskReportResponse = new GetObxTaskReportResponse();
//		ResponseMessage responseMessage = new ResponseMessage();
//		List<ObxTaskAllocationDto> ObxTaskAllocationDto;
//		try {
//			ObxTaskAllocationDto = obxDao.getObxTaskReport(date);
//			if (!ServicesUtil.isEmpty(ObxTaskAllocationDto)) {
//				responseMessage.setMessage("Data fetched successfully");
//				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//				responseMessage.setStatus(MurphyConstant.SUCCESS);
//				getObxTaskReportResponse.setObxTaskAllocationDto(ObxTaskAllocationDto);
//
//			} else {
//				responseMessage.setMessage("No data found for the requested date");
//				responseMessage.setStatus(MurphyConstant.SUCCESS);
//				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//			}
//		} catch (Exception e) {
//			logger.error("[Murphy][ObxSchedulerFacade][getObxTaskReport][error]" + e.getMessage());
//			responseMessage.setMessage("Entered date is not correct");
//			responseMessage.setStatus(MurphyConstant.FAILURE);
//			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		}
//		getObxTaskReportResponse.setResponseMessage(responseMessage);
//		return getObxTaskReportResponse;
//	}
//
//}