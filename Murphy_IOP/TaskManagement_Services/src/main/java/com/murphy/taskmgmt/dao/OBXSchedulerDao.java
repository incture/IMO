package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TimeZone;

import org.hibernate.Query;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.geotab.Coordinates;
import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.NearbyTaskDto;
import com.murphy.taskmgmt.dto.NonDispatchResponseDto;
import com.murphy.taskmgmt.dto.NonDispatchTaskDto;
import com.murphy.taskmgmt.dto.ObxOperatorWorkloadDetailsDto;
import com.murphy.taskmgmt.dto.ObxTaskAllocationDto;
import com.murphy.taskmgmt.dto.ObxTaskDto;
import com.murphy.taskmgmt.dto.ObxTaskIdEndTimeListDto;
import com.murphy.taskmgmt.dto.ObxTaskUpdateDto;
import com.murphy.taskmgmt.dto.ProcessEventsDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.dto.WellVisitDto;
import com.murphy.taskmgmt.entity.NonDispatchTaskDo;
import com.murphy.taskmgmt.entity.ProcessEventsDo;
import com.murphy.taskmgmt.entity.TaskEventsDo;
import com.murphy.taskmgmt.entity.TaskEventsDoPK;
import com.murphy.taskmgmt.entity.TaskOwnersDo;
import com.murphy.taskmgmt.entity.TaskOwnersDoPK;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;
import com.murphy.taskmgmt.entity.WellVisitDo;
import com.murphy.taskmgmt.entity.WellVisitDoPK;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ArcGISUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("OBXSchedulerDao")
public class OBXSchedulerDao extends BaseDao<WellVisitDo, WellVisitDto> {

	private static final Logger logger = LoggerFactory.getLogger(OBXSchedulerDao.class);

	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private TaskOwnersDao taskOwnersDao;

	@Autowired
	private ProcessEventsDao processDao;

	@Autowired
	private GeoTabDao geoTabDao;

	@Autowired
	private UserIDPMappingDao userMappingDao;

	@Autowired
	private ConfigDao configdao;

	@Autowired
	private CustomAttrInstancesDao attrtInstDao;

	@Autowired
	private NonDispatchTaskDao ndTaskDao;

	@Autowired
	private HierarchyDao hierarchyDao;
	
	@Autowired
	private ObxTaskDao ObxTaskDao;

	@Override
	protected WellVisitDo importDto(WellVisitDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		WellVisitDo entity = new WellVisitDo();
		entity.setWellVisitDoPK(new WellVisitDoPK());
		entity.setField(fromDto.getField());
		entity.getWellVisitDoPK().setDay(fromDto.getDay());
		entity.getWellVisitDoPK().setLocationCode(fromDto.getLocationCode());
		return entity;
	}

	@Override
	protected WellVisitDto exportDto(WellVisitDo entity) {
		WellVisitDto toDto = new WellVisitDto();
		toDto.setField(entity.getField());
		toDto.setDay(entity.getWellVisitDoPK().getDay());
		toDto.setLocationCode(entity.getWellVisitDoPK().getLocationCode());
		return toDto;
	}

	public String deleteAllWellVisitData() {
		String response = MurphyConstant.FAILURE;
		try{
			String query = "DELETE FROM TM_WELL_VISIT";
			Query q = this.getSession().createSQLQuery(query);
			Integer result = (Integer) q.executeUpdate();
			logger.error("[deleteAllWellVisitData] Number of Rows affected " + result);
			response = MurphyConstant.SUCCESS;
		}catch(Exception e){
			logger.error("[OBXSchedulerDao][deleteAllWellVisitData][Error] "+e.getMessage());
		}		
		return response;
	}

	// @SuppressWarnings("unchecked")
	// public List<String> getVisitMatrix(String field, int dayOfWeek) {
	// List<String> locationList = new ArrayList<String>();
	// String query = "SELECT LOCATION_CODE FROM TM_WELL_VISIT WHERE FIELD='" +
	// field + "' AND DAY=" + dayOfWeek;
	// Query q = this.getSession().createSQLQuery(query);
	// locationList = (List<String>) q.list();
	// return locationList;
	// }

	// @SuppressWarnings("unchecked")
	// public List<String> getVisitMatrix(List<String> fields, int dayOfWeek) {
	// String commaSeparatedFields="";
	// for(String field:fields){
	// commaSeparatedFields+=field+"','";
	// }
	// List<String> locationList = new ArrayList<String>();
	// String query = "SELECT LOCATION_CODE FROM TM_WELL_VISIT WHERE FIELD in('"
	// + commaSeparatedFields + "') AND DAY=" + dayOfWeek;
	// Query q = this.getSession().createSQLQuery(query);
	// locationList = (List<String>) q.list();
	// return locationList;
	// }

	@SuppressWarnings("unchecked")
	public List<ObxTaskDto> getVisitMatrixWithField(List<String> fields, int dayOfWeek) {
		String commaSeparatedFields = "";
		for (String field : fields) {
			commaSeparatedFields += field + "','";
		}
		List<ObxTaskDto> tasks = new ArrayList<ObxTaskDto>();
		try {
			String query = "SELECT WV.FIELD,WV.LOCATION_CODE,LC.LATITUDE,LC.LONGITUDE,WT.TIER,PL.LOCATION_TEXT FROM TM_WELL_VISIT WV "
					+ "JOIN PRODUCTION_LOCATION PL ON WV.LOCATION_CODE=PL.LOCATION_CODE "
					+ "JOIN LOCATION_COORDINATE LC ON LC.LOCATION_CODE=WV.LOCATION_CODE "
					+ "JOIN WELL_TIER WT ON WT.LOCATION_CODE=WV.LOCATION_CODE " + "WHERE WV.DAY=" + dayOfWeek
					+ " AND WV.FIELD in ('" + commaSeparatedFields + "')";
			Query q = this.getSession().createSQLQuery(query);
			List<Object[]> resultList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					ObxTaskDto task = new ObxTaskDto();
					task.setField((String) obj[0]);
					task.setLocationCode((String) obj[1]);
					task.setLatitude(((BigDecimal) obj[2]).doubleValue());
					task.setLongitude(((BigDecimal) obj[3]).doubleValue());
					task.setTier((String) obj[4]);
					task.setLocationText((String) obj[5]);
					tasks.add(task);
				}
			}
		} catch (Exception e) {
			logger.error("OBXSchedulerDao.getVisitMatrixWithField Exception " + e.getMessage());
		}
		return tasks;
	}

	@SuppressWarnings("unchecked")
	public List<NearbyTaskDto> getNearbyAssignedTask(double lat, double lon, String userId) {
		String query = "SELECT top 10 TE.TASK_ID, TE.DESCRIPTION, O.TASK_OWNER, P.LOCATION_TEXT, L.LONGITUDE, L.LATITUDE, "
				+ "((L.LONGITUDE - " + lon + ")*(L.LONGITUDE - " + lon + ")) + ((L.LATITUDE - " + lat
				+ ")*(L.LATITUDE - " + lat
				+ ")) as RELATIVE_DISTANCES , O.TASK_OWNER_DISP , TE.PROCESS_ID , PE.LOC_CODE ,A.INS_VALUE "
				+ "FROM TM_TASK_EVNTS TE, TM_TASK_OWNER O, TM_PROC_EVNTS PE, LOCATION_COORDINATE L, PRODUCTION_LOCATION P , TM_ATTR_INSTS A "
				+ "WHERE TE.STATUS = 'ASSIGNED' AND TE.ORIGIN = 'Dispatch' AND A.ATTR_TEMP_ID='123456' AND A.TASK_ID=TE.TASK_ID AND TE.TASK_ID = O.TASK_ID "
				+ "AND PE.PROCESS_ID = TE.PROCESS_ID AND PE.LOC_CODE = L.LOCATION_CODE AND L.LOCATION_CODE = P.LOCATION_CODE "
				+ " AND O.TASK_OWNER_EMAIL!='" + userId + "' " + "ORDER BY ((L.LONGITUDE - " + lon + ")*(L.LONGITUDE - "
				+ lon + ")) + ((L.LATITUDE - " + lat + ")*(L.LATITUDE - " + lat + "))";
		logger.error("OBXSchedulerDao.getNearbyAssignedTask(" + lat + ", " + lon + "):query=" + query);
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> list = q.list();
		List<NearbyTaskDto> returnList = new ArrayList<NearbyTaskDto>();
		for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
			Object[] objects = iterator.next();
			NearbyTaskDto nearbyTaskDto = new NearbyTaskDto();
			nearbyTaskDto.setTaskId((String) objects[0]);
			nearbyTaskDto.setTaskDescription((String) objects[1]);
			nearbyTaskDto.setAssignee((String) objects[2]);
			nearbyTaskDto.setLocationText((String) objects[3]);
			nearbyTaskDto.setToLongitude(objects[4] == null ? null : ((BigDecimal) objects[4]).doubleValue());
			nearbyTaskDto.setToLatitude(objects[5] == null ? null : ((BigDecimal) objects[5]).doubleValue());
			nearbyTaskDto.setAssigneeName(objects[7] == null ? null : (String) objects[7]);
			nearbyTaskDto.setProcessId(objects[8] == null ? null : (String) objects[8]);
			nearbyTaskDto.setLocationCode(objects[9] == null ? null : (String) objects[9]);
			nearbyTaskDto.setSubClassification(objects[10] == null ? null : (String) objects[10]);
			returnList.add(nearbyTaskDto);
		}
		// logger.error("OBXSchedulerDao.getNearbyAssignedTask():returnList;" +
		// returnList);
		return returnList;
	}

	public ResponseMessage updateOBXTaskUser(ObxTaskUpdateDto dto) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		String response = MurphyConstant.FAILURE;

		try {

			TaskEventsDoPK taskEventsDoPK = new TaskEventsDoPK();
			taskEventsDoPK.setTaskId(dto.getTaskId());
			taskEventsDoPK.setProcessId(dto.getProcessId());
			TaskEventsDo taskEventsDo = (TaskEventsDo) taskEventsDao.getSession().get(TaskEventsDo.class,
					taskEventsDoPK);
			TaskEventsDto taskEventDto = taskEventsDao.exportDto(taskEventsDo);

			TaskOwnersDoPK taskOwnersDoPK = new TaskOwnersDoPK();
			taskOwnersDoPK.setTaskId(dto.getTaskId());
			taskOwnersDoPK.setTaskOwner(dto.getDispatchTaskEmail());
			TaskOwnersDo taskOwnerEntity = (TaskOwnersDo) taskOwnersDao.getSession().get(TaskOwnersDo.class,
					taskOwnersDoPK);
			TaskOwnersDto taskOwnersDto = taskOwnersDao.exportDto(taskOwnerEntity);

			TaskOwnersDto taskOwnersDto2 = new TaskOwnersDto();
			String obxLocationCode = getObxLocationCode(dto.getObxTaskIdEndTimeList());

			taskOwnersDto2 = setNewTaskOwner(taskOwnersDto, taskOwnersDto2, dto.getAssignUserEmail(),
					dto.getObxTaskIdEndTimeList(), dto.getLocationCode(), obxLocationCode);

			List<TaskOwnersDto> taskOwnersList = new ArrayList<>();
			taskOwnersList.add(taskOwnersDto2);
			response = taskOwnersDao.updateOBXOwners(taskOwnersList, dto.getTaskId(), dto.getLocationCode(),
					taskEventDto.getOrigin());

			UserIDPMappingDo userDao = userMappingDao.getUserByEmail(dto.getAssignUserEmail());
			Boolean scheduler=false;
			if (MurphyConstant.SUCCESS.equals(response)) {
				response = taskEventsDao.updateTaskEventStatus(dto.getTaskId(), dto.getProcessId(),
						dto.getAssignUserEmail(), userDao.getUserFirstName() + " " + userDao.getUserLastName(),
						MurphyConstant.ASSIGN, dto.getUserUpdatedAt(), taskEventsDo.getDescription(),scheduler,null);
				ProcessEventsDo processDo = (ProcessEventsDo) processDao.getSession().get(ProcessEventsDo.class,
						dto.getProcessId());
				ProcessEventsDto processDto = processDao.exportDto(processDo);
				if (MurphyConstant.SUCCESS.equals(response)) {
					attrtInstDao.setAttrValueTo(dto.getTaskId(),
							userDao.getUserFirstName() + " " + userDao.getUserLastName(), "1234");
					processDto.setGroup(dto.getUserGroup());
					processDo.setStatus(MurphyConstant.ASSIGN);
					processDao.update(processDto);
					responseMessage.setMessage("Task Reassigned Successfully");
					responseMessage.setStatus(MurphyConstant.SUCCESS);
					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				}
			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][updateOBXTaskUser][error]" + e.getMessage());
		}

		return responseMessage;

	}

	private TaskOwnersDto setNewTaskOwner(TaskOwnersDto dto, TaskOwnersDto dto1, String email,
			List<ObxTaskIdEndTimeListDto> taskIdEndTimeList, String dispatchLocationCode, String obxLocationCode)
			throws Exception {

		try {
			dto1.setEstResolveTime(dto.getEstResolveTime());
			dto1.setCustomTime(dto.getCustomTime());
			dto1.setOwnerEmail(email);
			dto1.setTaskId(dto.getTaskId());
			dto1.setTaskOwner(email);
			dto1.setTier(dto.getTier());
			Double estimatedDriveTime = getEstDriveTime(dispatchLocationCode, obxLocationCode); 
			// estimated drive time for new dispatch task assign to OBX
			dto1.setEstDriveTime(estimatedDriveTime);
			dto1.setCustomTime(0.00);
			Double totalTime = dto.getEstResolveTime() + 0.00; 
			// total time = drive time + estimated resolve time
			if (estimatedDriveTime != -1) {
				totalTime = estimatedDriveTime + totalTime;
			}

			if (findStartTime(taskIdEndTimeList) != null) {
				dto1.setStartTime(findStartTime(taskIdEndTimeList));
				dto1.setEndTime(ServicesUtil.getDateWithInterval(findStartTime(taskIdEndTimeList), totalTime.intValue(),
						MurphyConstant.MINUTES));
			} else {
				Date d = new Date();
				dto1.setStartTime(d);
				dto1.setEndTime(ServicesUtil.getDateWithInterval(d, totalTime.intValue(), MurphyConstant.MINUTES));
			}

			UserIDPMappingDo userDao = userMappingDao.getUserByEmail(email);

			dto1.setTaskOwnerDisplayName(userDao.getUserFirstName() + " " + userDao.getUserLastName());
			dto1.setpId(userDao.getpId());
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][setNewTaskOwner][error]" + e.getMessage());
			throw e;
		}
		return dto1;
	}

	private Date findStartTime(List<ObxTaskIdEndTimeListDto> taskIdEndTimeList) {

		Date latest = null;

		try {
			if (!ServicesUtil.isEmpty(taskIdEndTimeList)) {

				ObxTaskIdEndTimeListDto dto = taskIdEndTimeList.get(0);
				latest = dto.getEndTime();

				for (int i = 1; i < taskIdEndTimeList.size(); i++) {

					ObxTaskIdEndTimeListDto dto1 = taskIdEndTimeList.get(1);
					if (dto1.getEndTime().after(latest)) {
						latest = dto1.getEndTime();
					}

				}
			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][findStartTime][error]" + e.getMessage());
		}
		return latest;

	}

	private Double getEstDriveTime(String dispatchLocationCode, String obxLocationCode) throws Exception {

		logger.error("[OBXSchedulerDao][getEstDriveTime][dispatchLocationCode]" + dispatchLocationCode);
		logger.error("[OBXSchedulerDao][getEstDriveTime][obxLocationCode]" + obxLocationCode);
		if (dispatchLocationCode != null && obxLocationCode != null) {
			Coordinates fromCoordinate = geoTabDao.getLatLongByLocationCode(obxLocationCode);
			Coordinates toCoordinate = geoTabDao.getLatLongByLocationCode(dispatchLocationCode);
			if (!ServicesUtil.isEmpty(fromCoordinate) && !ServicesUtil.isEmpty(toCoordinate)) {
				ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(fromCoordinate, toCoordinate);
				if (arcResponse.getResponseMessage().getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
					return arcResponse.getTotalDriveTime();
				}
			} else {
				logger.error("[OBXSchedulerDao][getEstDriveTime] Coordinates Not Found " + dispatchLocationCode + " "
						+ obxLocationCode);
				throw new IllegalArgumentException();
			}
		}
		return 0.00;
	}

	public String getObxLocationCode(List<ObxTaskIdEndTimeListDto> taskIdEndTimeList) {

		try {
			Date latest = findStartTime(taskIdEndTimeList);
			String taskId = "";
			for (ObxTaskIdEndTimeListDto obxTaskIdEndTimeListDto : taskIdEndTimeList) {

				if (obxTaskIdEndTimeListDto.getEndTime().equals(latest)) {
					taskId = obxTaskIdEndTimeListDto.getTaskId();
				}
			}

			String query = "SELECT PE.LOC_CODE FROM TM_PROC_EVNTS PE WHERE PE.PROCESS_ID ="
					+ "(select te.process_id from TM_TASK_EVNTS te where te.task_id = '" + taskId + "')";
			Object result = this.getSession().createSQLQuery(query).uniqueResult();
			if (!ServicesUtil.isEmpty(result)) {
				return (String) result;
			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][getObxLocationCode][error]" + e.getMessage());
		}
		return null;
	}

	// @SuppressWarnings("unchecked")
	// public List<WellVisitDto> getFieldTextAndCode(WellVisitDayMatrixDto
	// visitMatrix,int day) {
	//
	// List<WellVisitDto> wellVisitDtoList=new ArrayList<WellVisitDto>();
	// String queryString = "";
	// String
	// locations=ServicesUtil.getStringFromList(visitMatrix.getLocationCodes());
	// logger.error("locations"+locations);
	//// String loc=dto.getLocationCodes().get(0);
	//// for(int i=1;i<dto.getLocationCodes().size();i++){
	//// loc+="','"+dto.getLocationCodes().get(i);
	//// }
	// queryString = "select p4.location_text,p1.location_code from
	// production_location p1,"
	// + " production_location p2, production_location p3, production_location
	// p4 ,"
	// + "where p1.parent_code=p2.location_code and
	// p2.parent_code=p3.location_code and"
	// + " p3.parent_code=p4.location_code and ";
	// queryString = queryString + " p1.location_code in ("+locations+")";
	//
	// Query q = this.getSession().createSQLQuery(queryString);
	// List<Object[]> response = q.list();
	// if (!ServicesUtil.isEmpty(response)) {
	// WellVisitDto dto=new WellVisitDto();
	// for(Object[] obj:response){
	// dto=new WellVisitDto();
	// dto.setDay(day);
	// dto.setField((String)obj[0]);
	// dto.setLocationCode((String)obj[1]);
	// wellVisitDtoList.add(dto);
	// }
	// }
	// return wellVisitDtoList;
	//// }
	// }

	@SuppressWarnings("unchecked")
	public Map<String, Double> getToLocationCodeforBucket(String locationCode) {

		Map<String, Double> locationMap = new HashMap<String, Double>();

		try {

			// String query = "select to_location_code, road_drive_time from
			// LOCATION_DISTANCES where from_location_code='"
			String query = "select to_location_code, ROAD_DRIVE_TIME  from LOCATION_DISTANCES where from_location_code='"
					+ locationCode + "' UNION "
					+ " select from_location_code, ROAD_DRIVE_TIME  from LOCATION_DISTANCES where to_location_code='"
					+ locationCode + "'";
			// + "union select to_location_code, road_drive_time from
			// LOCATION_TEMP where to_location_code='"+locationCode+"'";

			List<Object[]> result = this.getSession().createSQLQuery(query).list();

			if (!ServicesUtil.isEmpty(result)) {

				for (Object[] obj : result) {
					Double driveTime = 0.0;

					String locationText = (String) obj[0];

					if (obj[1].getClass().getName().equals("java.lang.Double")) {

						driveTime = (Double) obj[1];

					} else if (obj[1].getClass().getName().equals("java.math.BigDecimal")) {

						driveTime = ((BigDecimal) obj[1]).doubleValue();

					}

					locationMap.put(locationText, driveTime);

				}
			}

		} catch (Exception e) {

		}

		return locationMap;

	}

	// Fetching database details for showing obx task allocation
	@SuppressWarnings("unchecked")
	public List<ObxTaskAllocationDto> getObxTaskAllocatedDetails(int dayOfWeek, String field, boolean isSpecificDay) {
		List<ObxTaskAllocationDto> allocatedTaskDtoList = new ArrayList<ObxTaskAllocationDto>();
		try {
			String query = "SELECT CLUSTER_NUMBER, LOCATION_TEXT, TIER, TASK_OWNER_EMAIL, ROLE, LOCATION_CODE, IS_OBX_USER, SEQUENCE_NUMBER, DRIVE_TIME, ESTIMATED_TASK_TIME,FIELD,DAY "
					+ "FROM OBX_TASK_ALLO WHERE ";
			if (isSpecificDay) {
				query = query + "DAY =" + dayOfWeek + " AND ";
			}
			query = query + "FIELD LIKE '" + field
					+ "' ORDER BY FIELD,DAY,CLUSTER_NUMBER,TASK_OWNER_EMAIL,SEQUENCE_NUMBER ASC";
			Query q = this.getSession().createSQLQuery(query);
			List<Object[]> resultList = (List<Object[]>) q.list();
			// Creating a map to store key:Email and Value:Full Name of the
			// owner so as not to run the query on repetition
			HashMap<String, String> ownerDetailMap = new HashMap<String, String>();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					ObxTaskAllocationDto dto = new ObxTaskAllocationDto();
					dto.setClusterdId((Integer) obj[0]);
					dto.setWell((String) obj[1]);
					dto.setTier((String) obj[2]);
					dto.setLocationCode((String) obj[5]);
					dto.setSequence((Integer) obj[7]);
					dto.setDriveTime(ServicesUtil.round((BigDecimal) obj[8], 3));
					dto.setTaskTime((BigDecimal) obj[9]);
					dto.setField((String) obj[10]);
					dto.setDay(MurphyConstant.DAY[((Integer) obj[11]) - 1]);
					// if(!ServicesUtil.isEmpty(obj[4])){
					// if(editableObxRole.equals((String)obj[4]))
					// dto.setIsEditable(true);
					// else
					// dto.setIsEditable(false);
					// }
					if (!ServicesUtil.isEmpty(obj[3])) {
						String nameQuery = "SELECT USER_FIRST_NAME, USER_LAST_NAME "
								+ "FROM TM_USER_IDP_MAPPING WHERE USER_EMAIL = '" + (String) obj[3] + "'";
						if (!(ownerDetailMap.containsKey((String) obj[3]))) {
							Query nameQ = this.getSession().createSQLQuery(nameQuery);
							List<Object[]> nameResultList = (List<Object[]>) nameQ.list();
							if (!ServicesUtil.isEmpty(nameResultList)) {
								for (Object[] nameObj : nameResultList) {
									ownerDetailMap.put((String) obj[3], (String) nameObj[0] + (String) nameObj[1]);
									// Checking if the selected operator is obx
									// or pro
									if (((String) obj[6]).equalsIgnoreCase(MurphyConstant.TRUE)) {
										dto.setObxOperator((String) nameObj[0] + (String) nameObj[1]);
										dto.setObxOperatorEmail((String) obj[3]);
									} else {
										dto.setProOperator((String) nameObj[0] + (String) nameObj[1]);
										dto.setProOperatorEmail((String) obj[3]);
									}
									// allocatedTaskDtoList.add(dto);
								}
							}
						} else {
							if (((String) obj[6]).equalsIgnoreCase(MurphyConstant.TRUE)) {
								dto.setObxOperator(ownerDetailMap.get((String) obj[3]));
								dto.setObxOperatorEmail((String) obj[3]);
							} else {
								dto.setProOperator(ownerDetailMap.get((String) obj[3]));
								dto.setProOperatorEmail((String) obj[3]);
							}
							// allocatedTaskDtoList.add(dto);
						}
					}
					allocatedTaskDtoList.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][getObxTaskAllocatedDetails][Exception] " + e.getMessage());
		}
		return allocatedTaskDtoList;
	}

	@SuppressWarnings("unchecked")
	public String getExsitingOwner(String locationCode, Integer day) {
		String exsitingOwnerEmail = "";
		String selectQuery = "SELECT  TASK_OWNER_EMAIL FROM OBX_TASK_ALLO WHERE LOCATION_CODE = '" + locationCode
				+ "' AND DAY = " + day;
		List<String> response = this.getSession().createSQLQuery(selectQuery).list();
		if (!ServicesUtil.isEmpty(response)) {
			exsitingOwnerEmail = response.get(0);
		}
		return exsitingOwnerEmail;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getExsitingOwnerList(List<String> locationCodeList, Integer day) {
		List<String> exsitingOwnerEmail = null;
		String commaSeparatedLocCodes = "";
		for (String locCode : locationCodeList) {
			commaSeparatedLocCodes += locCode + "','";
		}
		try{
			String selectQuery = "SELECT TASK_OWNER_EMAIL FROM OBX_TASK_ALLO WHERE LOCATION_CODE = '" + commaSeparatedLocCodes
					+ "' AND DAY = " + day;
			List<String> response = this.getSession().createSQLQuery(selectQuery).list();
			if (!ServicesUtil.isEmpty(response)) {
				exsitingOwnerEmail = response;
			}
		}catch(Exception e){
			logger.error("[OBXSchedulerDao][getExsitingOwnerList][Exception] " + e.getMessage());
		}
		return exsitingOwnerEmail;
	}

	// Fetching workload details corresponding obx operators
	@SuppressWarnings("unchecked")
	public ObxOperatorWorkloadDetailsDto getWorkloadDetails(String userEmail, Integer day, String firstName,
			String lastName, boolean isForWeek) {
		ObxOperatorWorkloadDetailsDto dto = new ObxOperatorWorkloadDetailsDto(firstName + " " + lastName);
		try {
			if (!ServicesUtil.isEmpty(userEmail)) {
				// Fetching the shift duration dynamically;
				String durationQuery = "SELECT CONFIG_DESC_VALUE FROM TM_CONFIG_VALUES WHERE CONFIG_ID ='"
						+ MurphyConstant.SHIFT_DURATION + "'";
				HashMap<String, String> workLoadMap = new HashMap<String, String>();
				Object durationQ = this.getSession().createSQLQuery(durationQuery).uniqueResult();
				Double duration = Double.parseDouble((String) durationQ);
				// Query to fetch sum of drive time and estimated time for the
				// user
				String query = "SELECT TASK_OWNER_EMAIL, SUM(DRIVE_TIME + ESTIMATED_TASK_TIME) AS WorkLoad, CLUSTER_NUMBER FROM OBX_TASK_ALLO "
						+ "WHERE TASK_OWNER_EMAIL = '" + userEmail + "'";
				if (!isForWeek) {
					query = query + " AND DAY = " + day;
				} else {
					duration = duration * MurphyConstant.NUMBER_OF_DAYS;
				}
				query = query + " GROUP BY TASK_OWNER_EMAIL, CLUSTER_NUMBER ";
				Query q = this.getSession().createSQLQuery(query);
				List<Object[]> resultList = (List<Object[]>) q.list();
				if (!ServicesUtil.isEmpty(resultList)) {
					Double workVal = (double) 0;
					String cluster = "";
					for (Object[] obj : resultList) {
						if (!(workLoadMap.containsKey((String) obj[0]))) {
							workLoadMap.put((String) obj[0], firstName + " " + lastName);
							BigDecimal bd = (BigDecimal) obj[1];
							Double d = bd.doubleValue();
							workVal = (double) Math.round(((d / 60) / duration) * 100);
							int clusterId = (Integer) obj[2];
							cluster = Integer.toString(clusterId);
						} else {
							BigDecimal bd = (BigDecimal) obj[1];
							Double d = bd.doubleValue();
							workVal += (double) Math.round(((d / 60) / duration) * 100);
							int clusterId = (Integer) obj[2];
							cluster = cluster + "," + Integer.toString(clusterId);
						}
					}
					dto.setClusterId(cluster);
					dto.setWorkLoad(workVal);
				}
			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][getWorkloadDetails][Exception] " + e.getMessage());
		}
		return dto;
	}

	// Updating the operator assigned to a task by ROC
	public String updateTaskForOperator(String taskOwnerEmail, String updatedByEmail, String locationCode, Integer day,
			String isObx) {
		try {
			if (!ServicesUtil.isEmpty(taskOwnerEmail) && !ServicesUtil.isEmpty(updatedByEmail)
					&& !ServicesUtil.isEmpty(locationCode) && !ServicesUtil.isEmpty(day)) {
				String updateQuery = "UPDATE OBX_TASK_ALLO SET TASK_OWNER_EMAIL = '" + taskOwnerEmail
						+ "' ,UPDATED_BY = '" + updatedByEmail + "' , IS_OBX_USER = '" + isObx
						+ "' WHERE LOCATION_CODE = '" + locationCode + "' AND DAY = " + day;
				Query q = this.getSession().createSQLQuery(updateQuery);
				q.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][updateTaskForOperatorDao][Exception] " + e.getMessage());
		}
		return MurphyConstant.SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public List<ObxTaskDto> getVisitMatrixWithFieldForDay(int dayOfWeek, List<String> field) { //, boolean takeTierC
		List<ObxTaskDto> tasks = new ArrayList<ObxTaskDto>();
		try {
			double taskResolveTime = Double
					.parseDouble(configdao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
			String fieldInString = com.murphy.integration.util.ServicesUtil.getStringFromList(field);
			String query = "SELECT WV.FIELD,WV.LOCATION_CODE,LC.LATITUDE,LC.LONGITUDE,WT.TIER,PL.LOCATION_TEXT FROM TM_WELL_VISIT WV "
					+ "JOIN PRODUCTION_LOCATION PL ON WV.LOCATION_CODE=PL.LOCATION_CODE "
					+ "JOIN LOCATION_COORDINATE LC ON LC.LOCATION_CODE=WV.LOCATION_CODE "
					+ "JOIN WELL_TIER WT ON WT.LOCATION_CODE=WV.LOCATION_CODE " + "WHERE WV.DAY=" + dayOfWeek
					+ " AND WV.FIELD IN (" + fieldInString + ")";
			/*if (takeTierC) {
				query = query + " AND WT.TIER IN ('" + MurphyConstant.TIER_C + "')";
			}
*/
			// logger.error("[getVisitMatrixWithFieldForDay][query] " +query);
			Query q = this.getSession().createSQLQuery(query);
			List<Object[]> resultList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					ObxTaskDto task = new ObxTaskDto();
					task.setLocationCode((String) obj[1]);
					task.setField((String) obj[0]);
					task.setOwnerEmail("");
					task.setLatitude(((BigDecimal) obj[2]).doubleValue());
					task.setLongitude(((BigDecimal) obj[3]).doubleValue());
					task.setTier((String) obj[4]);
					task.setLocationText((String) obj[5]);
					task.setDay(dayOfWeek);
					task.setClusterNumber(0);
					task.setDriveTime(0.0);
					task.setRole("");
					task.setUpdatedBy("SYSTEM");
					task.setEstimatedTaskTime(taskResolveTime);
					task.setSequenceNumber(0);
					tasks.add(task);
				}
			}
		} catch (Exception e) {
			logger.error("OBXSchedulerDao.getVisitMatrixWithField Exception " + e.getMessage());
		}
		return tasks;
	}

	public Double getLocationDistance(String fromlocationCode, String tolocationCode) {
		Double dist = 0.0;

		try {

			String query = "select ROAD_DRIVE_TIME  from LOCATION_DISTANCES where from_location_code='"
					+ fromlocationCode + "' and to_location_code='" + tolocationCode + "'";
			Query q = this.getSession().createSQLQuery(query);
			Object result = this.getSession().createSQLQuery(query).uniqueResult();

			if (!ServicesUtil.isEmpty(result)) {
				return ((BigDecimal) result).doubleValue();
			}

		} catch (Exception e) {
			logger.error("[ObxSchedulerDao][getLocationDistance][Exception] " + e.getMessage());
		}

		return dist;
	}

	// Checking flag if obx engine running or not for the excel download
	// feature.
	public String getObxEngineRunningFlag() {
		try {
			String query = "SELECT CONFIG_DESC_VALUE from TM_CONFIG_VALUES WHERE CONFIG_ID = 'OBX_ENGINE_RUNNING_FLAG'";
			Object result = this.getSession().createSQLQuery(query).uniqueResult();
			if (!ServicesUtil.isEmpty(result)) {
				return (String) result;
			}

		} catch (Exception e) {
			logger.error("[ObxSchedulerDao][getObxEngineRunningFlag][Exception] " + e.getMessage());
		}

		return null;
	}

	// Checking for the number of unassigned wells
	public BigInteger getUnassignedWellsCount(Integer dayOfWeek, String field, boolean isSpecificDay) {
		Integer wellsNum = 0;
		BigInteger unAssignedWellNum = BigInteger.valueOf((long) wellsNum);
		try {
			String query = "SELECT Count(*) from OBX_TASK_ALLO WHERE TASK_OWNER_EMAIL = '' AND ";
			if (isSpecificDay) {
				query = query + "DAY =" + dayOfWeek + " AND ";
			}
			query = query + "FIELD LIKE '" + field + "'";
			Object result = this.getSession().createSQLQuery(query).uniqueResult();
			if (!ServicesUtil.isEmpty(result)) {
				unAssignedWellNum = (BigInteger) result;
			}

		} catch (Exception e) {
			logger.error("[ObxSchedulerDao][getObxEngineRunningFlag][Exception] " + e.getMessage());
		}
		return unAssignedWellNum;
	}

	// For fetching location code of wells whose OBX task are not completed for
	// that day.
	@SuppressWarnings("unchecked")
	public List<String> getUnResolvedTaskLocation() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		List<String> locationCodes = null;
		try {
			String query = "SELECT pe.LOC_CODE" + " FROM TM_PROC_EVNTS AS pe INNER JOIN TM_TASK_EVNTS AS te"
					+ " ON pe.PROCESS_ID = te.PROCESS_ID INNER JOIN OBX_TASK_ALLO AS obx ON pe.LOC_CODE = obx.LOCATION_CODE"
					+ " WHERE pe.USER_GROUP = 'System User'	AND te.PARENT_ORIGIN = 'OBX' AND te.ORIGIN = 'Dispatch' AND te.TASK_TYPE = 'SYSTEM'"
					+ " AND pe.STATUS = 'REVOKED' AND To_Date(te.CREATED_AT)='" + sdf.format(cal.getTime()) + "'";

			Query q = this.getSession().createSQLQuery(query);
			locationCodes = (List<String>) q.list();
			logger.error("[ObxSchedulerDao][getUnResolvedTaskDetails][Query] " + q);
			if (!ServicesUtil.isEmpty(locationCodes)) {
				return locationCodes;
			}
		} catch (Exception e) {
			logger.error("[ObxSchedulerDao][getUnResolvedTaskDetails][Exception] " + e.getMessage());
		}
		return locationCodes;
	}

	// For fetching location codes of all wells of the remaining days
	@SuppressWarnings("unchecked")
	public List<String> getRestDaysLocationCode(List<Integer> restDays) {
		String day = Integer.toString(restDays.get(0));
		for (int i = 1; i < restDays.size(); i++) {
			day += "','" + Integer.toString(restDays.get(i));
		}
		try {
			String query = "SELECT LOCATION_CODE FROM TM_WELL_VISIT WHERE DAY IN ('" + day + "')";
			Query q = this.getSession().createSQLQuery(query);
			List<String> restLocCodes = (List<String>) q.list();
			// logger.error("[ObxSchedulerDao][getRestDaysLocationCode][Query] "
			// + q + " [UncompletedLocation] "+restLocCodes);
			if (!ServicesUtil.isEmpty(restLocCodes)) {
				return restLocCodes;
			}
		} catch (Exception e) {
			logger.error("[ObxSchedulerDao][getRestDaysLocationCode][Exception] " + e.getMessage());
		}
		return null;
	}

	// Fetching Non-dispatch task for particular location
	@SuppressWarnings("unchecked")
	public List<NonDispatchTaskDto> getNDTaskForOBX(String locCode) {
		List<NonDispatchTaskDto> responseList = null;
		try {
			String queryString = "select * from tm_non_disptch where status in ('" + MurphyConstant.NON_DISPATCH + "') "
					+ "and nd_loc = '" + locCode + "' order by created_at desc ";
			logger.error("[getNDTaskForOBX][Query] "+queryString);
			Query q = this.getSession().createSQLQuery(queryString.trim());
			List<NonDispatchTaskDo> resultList = (List<NonDispatchTaskDo>) q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				responseList = new ArrayList<NonDispatchTaskDto>();
				for (NonDispatchTaskDo entity : resultList) {
					responseList.add(ndTaskDao.exportDto(entity));
				}
			}
		} catch (Exception e) {
			logger.error("[ObxSchedulerDao][getNDTaskForOBX][Error]" + e.getMessage());
		}
		return responseList;
	}

	// Deleting rest days tier C wells in Well Visit and update the Well Visit
	// with new cluster of tier C wells
	public void deleteOrUpdateTierCWellVisit(List<Integer> restDays, int[] clustersCAssignmentsList,
			List<String> restDaysTierCWellsList, List<String> allDaysTierCLocCodes) {

		String loc = restDaysTierCWellsList.get(0);
		for (int i = 1; i < restDaysTierCWellsList.size(); i++) {
			loc += "','" + restDaysTierCWellsList.get(i);
		}
		try {
			// Deleting future days tierC matrix
			String deleteQuery = "DELETE FROM TM_WELL_VISIT WHERE LOCATION_CODE IN ('" + loc + "')";
			int delNum = this.getSession().createSQLQuery(deleteQuery.trim()).executeUpdate();
			logger.error("[ObxSchedulerDao][deleteOrUpdateTierCWellVisit][DeleteQuery] " + delNum);

			// Inserting updated cluster of tierC wells in well visit table
			HashSet<Integer> hs = new HashSet<Integer>();
			for (int i : clustersCAssignmentsList) {
				hs.add(i);
			}
			Collections.sort(restDays);
			int smallestDay = restDays.get(0);
			int result = 0;
			if ((hs.size() ^ restDays.size()) != 0) {
				logger.error("Not Same size");
			} else {
				int dayOfWeek = 0;
				for (int i = 0; i < clustersCAssignmentsList.length; i++) {
					List<String> locationCodes = new ArrayList<String>();
					locationCodes.add(allDaysTierCLocCodes.get(i));
					dayOfWeek = clustersCAssignmentsList[i] + smallestDay;
					String insertQuery = "INSERT INTO TM_WELL_VISIT (FIELD, DAY, LOCATION_CODE) " + "VALUES ('"
							+ hierarchyDao.getFieldText(locationCodes, MurphyConstant.WELL).getField().trim() + "', '"
							+ dayOfWeek + "','" + allDaysTierCLocCodes.get(i) + "')";
					 //logger.error("[ObxSchedulerDao][deleteOrUpdateTierCWellVisit][InsertQuery]" + insertQuery);
					result += this.getSession().createSQLQuery(insertQuery).executeUpdate();
				}
				logger.error("Result insert count " + result);
			}
		} catch (Exception e) {
			logger.error("[ObxSchedulerDao][deleteOrUpdateTierCWellVisit][Error]" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<ObxTaskAllocationDto> getObxTaskReport(String date) {
		List<ObxTaskAllocationDto> taskAllocatedList = new ArrayList<ObxTaskAllocationDto>();
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
		int dayNum = c.get(Calendar.DAY_OF_WEEK);
		List<String> findRestPrevDays = new ArrayList<String>();
		try {
			String weekDateTillPresent = getWeekByDate(date);
			findRestPrevDays = findRestDays();

			if (!ServicesUtil.isEmpty(findRestPrevDays.get(0))) {
				logger.error("[OBXSchedulerDao][getObxTaskReport][prevDays] " + findRestPrevDays.get(0));
				String queryForStatus = "SELECT TE.STATUS FROM TM_TASK_EVNTS TE JOIN TM_PROC_EVNTS PE ON "
						+ "PE.PROCESS_ID = TE.PROCESS_ID where pe.LOC_CODE in (select OA.location_code from OBX_TASK_ALLO OA where OA.TASK_OWNER_EMAIL<>'' AND DAY IN("
						+ findRestPrevDays.get(0) + ")) and PARENT_ORIGIN = 'OBX' " + "AND TASK_TYPE = 'SYSTEM' and"
						+ weekDateTillPresent + "";

				logger.error("[OBXSchedulerDao][getObxTaskReport][queryForStatus] " + queryForStatus);
				String ObxAllocationQuery = "SELECT OT.FIELD,OT.CLUSTER_NUMBER,OT.LOCATION_TEXT, OT.TIER, OT.TASK_OWNER_EMAIL, OT.SEQUENCE_NUMBER,"
						+ "OT.DRIVE_TIME,OT.IS_OBX_USER,OT.DAY,OT.LOCATION_CODE "
						+ "FROM OBX_TASK_ALLO OT WHERE OT.TASK_OWNER_EMAIL<>'' AND DAY IN ("
						+ findRestPrevDays.get(0) + ")";

				logger.error("[OBXSchedulerDao][getObxTaskReport][Query] " + ObxAllocationQuery);
				Query q = this.getSession().createSQLQuery(ObxAllocationQuery);
				List<Object[]> resultList = (List<Object[]>) q.list();
				if (!ServicesUtil.isEmpty(resultList)) {
					commonSetFunction(resultList, taskAllocatedList, queryForStatus, 1);
				}
				unAssginedAllocationDetails(taskAllocatedList, findRestPrevDays.get(0));

			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][getObxTaskReport][Exception] " + e.getMessage());
		}
		if (!ServicesUtil.isEmpty(findRestPrevDays.get(1))) {
			logger.error("[OBXSchedulerDao][getObxTaskReport][restDays] " + findRestPrevDays.get(1));
			taskAllocatedList = restDaysObxTaskDetailsReport(taskAllocatedList, findRestPrevDays.get(1), dayNum);
		}

		return taskAllocatedList;
	}

	@SuppressWarnings("unchecked")
	private List<ObxTaskAllocationDto> commonSetFunction(List<Object[]> resultList,
			List<ObxTaskAllocationDto> allocatedTaskDtoList, String queryForStatus, int j) {
		HashMap<String, String> ownerDetailMap = new HashMap<String, String>();
		List<String> statusList = null;
		int i=0;
		try{
			if(j==1){
				if(!ServicesUtil.isEmpty(getStatusbyQuery(queryForStatus))){
					statusList = getStatusbyQuery(queryForStatus);
				}else{
					logger.error("[commonSetFunction][Empty Status]");
				}
			}
			for (Object[] obj : resultList) {
				ObxTaskAllocationDto dto = new ObxTaskAllocationDto();
				dto.setField(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				dto.setClusterdId(ServicesUtil.isEmpty(obj[1]) ? null : (Integer) obj[1]);
				dto.setWell(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
				dto.setTier(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
				dto.setSequence(ServicesUtil.isEmpty(obj[5]) ? null : (Integer) obj[5]);
				dto.setDriveTime(ServicesUtil.round((BigDecimal) obj[6], 3));
				if(j==2){
					dto.setStatus("SCHEDULED");
				}else if(j==1){
		            dto.setStatus(statusList.get(i));
		            i++;
				}else if(j==3){
					dto.setStatus("UNASSGINED");
				}
				dto.setDay(findDayFromInteger((int) obj[8]));
				dto.setLocationCode(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
				if (!ServicesUtil.isEmpty(obj[4])) {
					String nameQuery = "SELECT USER_FIRST_NAME, USER_LAST_NAME "
							+ "FROM TM_USER_IDP_MAPPING WHERE USER_EMAIL = '" + (String) obj[4] + "'";
					if (!(ownerDetailMap.containsKey((String) obj[4]))) {
						Query nameQ = this.getSession().createSQLQuery(nameQuery);
						List<Object[]> nameResultList = (List<Object[]>)nameQ.list();
						if (!ServicesUtil.isEmpty(nameResultList)) {
							for (Object[] nameObj : nameResultList) {
								ownerDetailMap.put((String) obj[4], (String) nameObj[0] + (String) nameObj[1]);
								// Checking if the selected operator is obx or pro Operator
								if (((String) obj[7]).equalsIgnoreCase(MurphyConstant.TRUE)) {
									dto.setObxOperator((String) nameObj[0] + (String) nameObj[1]);
									dto.setObxOperatorEmail((String) obj[4]);
								} else {
									dto.setProOperator((String) nameObj[0] + (String) nameObj[1]);
									dto.setProOperatorEmail((String) obj[4]);
								}
							}
						}
					} else {
						if (((String) obj[7]).equalsIgnoreCase(MurphyConstant.TRUE)) {
							dto.setObxOperator(ownerDetailMap.get((String) obj[4]));
							dto.setObxOperatorEmail((String) obj[4]);
						} else {
							dto.setProOperator(ownerDetailMap.get((String) obj[4]));
							dto.setProOperatorEmail((String) obj[4]);
						}
					}
				}
				allocatedTaskDtoList.add(dto);
			}
		}catch(Exception e){
			logger.error("[OBXSchedulerDao][commonSetFunction][Exception] " + e.getMessage());
		}
		logger.error("[OBXSchedulerDao][commonSetFunction][allocatedTaskDtoList] "+allocatedTaskDtoList);
		return allocatedTaskDtoList;
	}


	
	@SuppressWarnings("unchecked")
	private List<ObxTaskAllocationDto> restDaysObxTaskDetailsReport(List<ObxTaskAllocationDto> allocatedTaskDtoList,
			String NextDays, int dayNum) {
		//List<ObxTaskAllocationDto> restDaysObxTaskAllocatedReport = null;
		try {
			if (dayNum + 1 < 6) {
				String query = "SELECT OT.FIELD,OT.CLUSTER_NUMBER,OT.LOCATION_TEXT, OT.TIER, OT.TASK_OWNER_EMAIL, OT.SEQUENCE_NUMBER,"
						+ "OT.DRIVE_TIME,OT.IS_OBX_USER,OT.DAY,OT.LOCATION_CODE "
						+ "FROM OBX_TASK_ALLO OT WHERE DAY IN (" + NextDays + ")";

				logger.error("[OBXSchedulerDao][allocatedTaskDtoList][Query] " + query);
				Query q = this.getSession().createSQLQuery(query);
				List<Object[]> resultList = q.list();
				if (!ServicesUtil.isEmpty(resultList)) {
					allocatedTaskDtoList = commonSetFunction(resultList, allocatedTaskDtoList,"",2);
					logger.error("[OBXSchedulerDao][restDaysObxTaskDetails][restDaysObxTaskAllocatedReport] "+allocatedTaskDtoList);
				}
			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][allocatedTaskDtoList][Exception] " + e.getMessage());
		}
		return allocatedTaskDtoList;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getStatusbyQuery(String queryForStatus) {
		List<String> statusList = new ArrayList<String>();
		try{
			Query q = this.getSession().createSQLQuery(queryForStatus);
			logger.error("[OBXSchedulerDao][getStatusbyQuery][queryForStatus] "+queryForStatus);
			List<String> resultList = (List<String>)q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (String obj : resultList) {
					statusList.add(obj);
				}
			}
			logger.error("[OBXSchedulerDao][getStatusbyQuery][statusList] "+statusList);
		} catch(Exception e){
			logger.error("[OBXSchedulerDao][getStatusbyQuery][Exception] "+e.getMessage());
		}
		return statusList;
	}


	private String findDayFromInteger(int j) {
		
		final String[] days = { "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		logger.error("findDayFromInteger day "+days[j]+ " DATE DAY "+j);
		return days[j];
	}

	private String getWeekByDate(String date) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM/dd");
		LocalDate localDate = formatter.parseLocalDate(date);
		int dayOfWeek = localDate.getDayOfWeek();

		String startWeekDate = "";
		String endWeekDate = "";
		try {
			List<String> startAndEnd = new LinkedList<String>();
			if (!ServicesUtil.isEmpty(dayOfWeek)) {

				if (dayOfWeek >= 1 && dayOfWeek <= 7) {

					if (dayOfWeek == 1) {
						startAndEnd = findStartAndEndOfWeek(date, dayOfWeek);
						startWeekDate = startAndEnd.get(0);
						endWeekDate = startAndEnd.get(1);
					} else if (dayOfWeek == 2) {
						startAndEnd = findStartAndEndOfWeek(date, dayOfWeek);
						startWeekDate = startAndEnd.get(0);
						endWeekDate = startAndEnd.get(1);
					} else if (dayOfWeek == 3) {
						startAndEnd = findStartAndEndOfWeek(date, dayOfWeek);
						startWeekDate = startAndEnd.get(0);
						endWeekDate = startAndEnd.get(1);
					} else if (dayOfWeek == 4) {
						startAndEnd = findStartAndEndOfWeek(date, dayOfWeek);
						startWeekDate = startAndEnd.get(0);
						endWeekDate = startAndEnd.get(1);
					} else if (dayOfWeek == 5) {
						startAndEnd = findStartAndEndOfWeek(date, dayOfWeek);
						startWeekDate = startAndEnd.get(0);
						endWeekDate = startAndEnd.get(1);
					} else if (dayOfWeek == 6) {
						startAndEnd = findStartAndEndOfWeek(date, dayOfWeek);
						startWeekDate = startAndEnd.get(0);
						endWeekDate = startAndEnd.get(1);
					} else if (dayOfWeek == 7) {
						startAndEnd = findStartAndEndOfWeek(date, dayOfWeek);
						startWeekDate = startAndEnd.get(0);
						endWeekDate = startAndEnd.get(1);
					}
				} else {
					logger.error("[OBXSchedulerDao][getWeekByDate][error][Enter the correct date]");
				}
			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][getWeekByDate][error]" + e.getMessage());
		}
		String week = " TO_DATE(TE.CREATED_AT) >='" + startWeekDate + "' AND TO_DATE(TE.CREATED_AT) <'" + endWeekDate + "'";
		return week;
	}

	// @SuppressWarnings("deprecation")
	private List<String> findStartAndEndOfWeek(String date, int dayOfWeek) {
		List<String> startAndEnd = new LinkedList<String>();
		try {

			if (dayOfWeek == 1) {
				startAndEnd.add(date);
				String end = getRequiredDate(date, +1);
				startAndEnd.add(end);
			} else if (dayOfWeek == 2) {
				// to find start of the week
				String start = getRequiredDate(date, -1);
				startAndEnd.add(start);
				// to find end of the week
				String end = getRequiredDate(date, +1);
				startAndEnd.add(end);
			} else if (dayOfWeek == 3) {
				String start = getRequiredDate(date, -2);
				startAndEnd.add(start);
				String end = getRequiredDate(date, +1);
				startAndEnd.add(end);
			} else if (dayOfWeek == 4) {
				String start = getRequiredDate(date, -3);
				startAndEnd.add(start);
				String end = getRequiredDate(date, +1);
				startAndEnd.add(end);
			} else if (dayOfWeek == 5) {
				String start = getRequiredDate(date, -4);
				startAndEnd.add(start);
				String end = getRequiredDate(date, +1);
				startAndEnd.add(end);
			} else if (dayOfWeek == 6) {
				String start = getRequiredDate(date, -5);
				startAndEnd.add(start);
				startAndEnd.add(date);
			} else if (dayOfWeek == 7) {
				String start = getRequiredDate(date, -6);
				startAndEnd.add(start);
				String end = getRequiredDate(date, -1);
				startAndEnd.add(end);
			}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][findStartAndEndOfWeek][error]" + e.getMessage());
		}
		return startAndEnd;
	}

	private String getRequiredDate(String date, int i) {
		try {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(date));
			cal.add(Calendar.DATE, i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			date = sdf.format(cal.getTime());
		} catch (Exception ex) {
			logger.error("[OBXSchedulerDao][getRequiredDate][Error] " + ex.getMessage());
		}
		return date;
	}
	
	private String nextDays(int dayNum) {
		String sample="";
		int num;
		for(int i=2;i<=5;i++){
			num=dayNum+i;
            if(i!=5)
			sample+="'"+num+"',";
            else
            	sample+="'"+num+"'";
		}
		return sample;
	}
	
	private String prevDays(int dayNum) {
		String sample = "";
		for (int i = 0; i < dayNum; i++) {
			int num = 2;
			num = num + i;
			if (i != dayNum - 1)
				sample += "'" + num + "',";
			else
				sample += "'" + num + "'";
		}
		 return sample;
	}
	
	@SuppressWarnings("unchecked")
	private List<ObxTaskAllocationDto> unAssginedAllocationDetails(List<ObxTaskAllocationDto> unAssginedAllocationList,
			String prevDays) {
		try {
			String unAssginedAllocationQuery = "SELECT OT.FIELD,OT.CLUSTER_NUMBER,OT.LOCATION_TEXT, OT.TIER, OT.TASK_OWNER_EMAIL, OT.SEQUENCE_NUMBER,"
					+ "OT.DRIVE_TIME,OT.IS_OBX_USER,OT.DAY,OT.LOCATION_CODE "
					+ "FROM OBX_TASK_ALLO OT WHERE OT.TASK_OWNER_EMAIL='' AND DAY IN (" + prevDays + ")";

				logger.error("[OBXSchedulerDao][unAssginedAllocationDetails][Query] " + unAssginedAllocationQuery);
				Query q = this.getSession().createSQLQuery(unAssginedAllocationQuery);
				List<Object[]> resultList = q.list();
				if (!ServicesUtil.isEmpty(resultList)) {
					unAssginedAllocationList = commonSetFunction(resultList, unAssginedAllocationList,"",3);
					logger.error("[OBXSchedulerDao][unAssginedAllocationDetails][unAssginedAllocationList] "+unAssginedAllocationList);
				}
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][allocatedTaskDtoList][Exception] " + e.getMessage());
		}
		return unAssginedAllocationList;
	}
	
	public List<String> findRestDays() {
		List<String> result = new ArrayList<String>();
		try {
			Date currentDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
			sdf.setTimeZone(TimeZone.getTimeZone("CST"));
			String dateCST = sdf.format(currentDate);

			SimpleDateFormat sdf2 = new SimpleDateFormat("dd:mm:yyyy:hh:mm:ss a");
			sdf2.setTimeZone(TimeZone.getTimeZone("CST"));
			String dateCST2 = sdf2.format(currentDate);

			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd:mm:yyyy:hh:mm:ss a");
			LocalDate localDate = formatter.parseLocalDate(dateCST2);
			int dayNum = localDate.getDayOfWeek();

			String checkTime = "05:30:00 AM";
			Date currentTime = null, obxTimeCheck = null;
			currentTime = sdf.parse(dateCST);
			obxTimeCheck = sdf.parse(checkTime);

			String restdays;
			String prevdays = null;
			if (currentTime.before(obxTimeCheck)) {
				prevdays = prevDays(dayNum, false);
				restdays = nextDays(dayNum, true);
				result.add(prevdays);
				result.add(restdays);
			} else {
				prevdays = prevDays(dayNum, true);
				restdays = nextDays(dayNum, false);
				result.add(prevdays);
				result.add(restdays);
			}
			logger.error("[OBXSchedulerDao][findRestDays][resultDays] " + result);

		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][findRestDays][Exception] " + e.getMessage());
		}
		return result;
	}
	
	public String nextDays(int dayNum, Boolean isPresentInclude) {
		String daysLeft = null;
		try {
			List<Integer> allDays = ObxTaskDao.getDays();
			logger.error("[OBXSchedulerDao][nextDays][allDays] " + allDays);
			List<Integer> restDays = new ArrayList<>();
			for (int day : allDays) {
				if (day > dayNum)
					restDays.add(day);
			}

			if (isPresentInclude) {
				restDays.add(dayNum);
			}

			if (!ServicesUtil.isEmpty(restDays.get(0))) {
				daysLeft = "'" + Integer.toString(restDays.get(0)) + "'";
				for (int i = 1; i < restDays.size(); i++) {
					daysLeft += ",'" + Integer.toString(restDays.get(i)) + "'";
				}
			}
			logger.error("[OBXSchedulerDao][nextDays][daysLeft] " + daysLeft);

		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][nextDays][exception] " + e.getMessage());
		}
		return daysLeft;
	}

	private String prevDays(int dayNum, Boolean isPresentInclude) {
		String daysLeft = null;
		try {
			List<Integer> allDays = ObxTaskDao.getDays();
			List<Integer> beforeDays = new ArrayList<>();
			for (int day : allDays) {
				if (day < dayNum)
					beforeDays.add(day);
			}
			if (isPresentInclude) {
				beforeDays.add(dayNum);
			}

			if (!ServicesUtil.isEmpty(beforeDays.get(0))) {
				daysLeft = "'" + Integer.toString(beforeDays.get(0)) + "'";
				for (int i = 1; i < beforeDays.size(); i++) {
					daysLeft += ",'" + Integer.toString(beforeDays.get(i)) + "'";
				}
			}
			logger.error("[OBXSchedulerDao][prevDays][daysLeft] " + daysLeft);
		} catch (Exception e) {
			logger.error("[OBXSchedulerDao][prevDays][Exception] " + e.getMessage());
		}

		return daysLeft;

	}
}