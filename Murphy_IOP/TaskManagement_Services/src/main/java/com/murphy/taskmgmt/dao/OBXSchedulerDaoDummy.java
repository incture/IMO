package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.geotab.Coordinates;
import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.NearbyTaskDto;
import com.murphy.taskmgmt.dto.ObxTaskDto;
import com.murphy.taskmgmt.dto.ObxTaskIdEndTimeListDto;
import com.murphy.taskmgmt.dto.ObxTaskUpdateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.dto.WellVisitDto;
import com.murphy.taskmgmt.dto.WellVisitDtoDummy;
import com.murphy.taskmgmt.entity.TaskEventsDo;
import com.murphy.taskmgmt.entity.TaskEventsDoPK;
import com.murphy.taskmgmt.entity.TaskOwnersDo;
import com.murphy.taskmgmt.entity.TaskOwnersDoPK;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;
import com.murphy.taskmgmt.entity.WellVisitDo;
import com.murphy.taskmgmt.entity.WellVisitDoDummy;
import com.murphy.taskmgmt.entity.WellVisitDoPK;
import com.murphy.taskmgmt.entity.WellVisitDoPKDummy;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ArcGISUtil;
import com.murphy.taskmgmt.util.GeoTabUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil ;
//import com.sap.db.util.StringUtil;

@Repository("OBXSchedulerDaoDummy")
public class OBXSchedulerDaoDummy extends BaseDao<WellVisitDoDummy, WellVisitDtoDummy> {

	private static final Logger logger = LoggerFactory.getLogger(OBXSchedulerDaoDummy.class);
	
	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private TaskOwnersDao taskOwnersDao;
	
	@Autowired
	private GeoTabDao geoTabDao;
	
	@Autowired
	private UserIDPMappingDao userMappingDao;

	@Override
	protected WellVisitDoDummy importDto(WellVisitDtoDummy fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		WellVisitDoDummy entity = new WellVisitDoDummy();
		entity.setWellVisitDoPK(new WellVisitDoPKDummy());
		entity.setField(fromDto.getField());
		entity.getWellVisitDoPK().setDay(fromDto.getDay());
		entity.getWellVisitDoPK().setLocationCode(fromDto.getLocationCode());
		return entity;
	}

	@Override
	protected WellVisitDtoDummy exportDto(WellVisitDoDummy entity) {
		WellVisitDtoDummy toDto = new WellVisitDtoDummy();
		toDto.setField(entity.getField());
		toDto.setDay(entity.getWellVisitDoPK().getDay());
		toDto.setLocationCode(entity.getWellVisitDoPK().getLocationCode());
		return toDto;
	}

	public String deleteAllWellVisitData() {
		String response = MurphyConstant.FAILURE;
		String query = "DELETE FROM TM_WELL_VISIT_DUMMY";
		Query q = this.getSession().createSQLQuery(query);
		Integer result = (Integer) q.executeUpdate();
		logger.error("[deleteAllWellVisitData] Number of Rows affected " + result);
		response = MurphyConstant.SUCCESS;
		return response;
	}

//	@SuppressWarnings("unchecked")
//	public List<String> getVisitMatrix(String field, int dayOfWeek) {
//		List<String> locationList = new ArrayList<String>();
//		String query = "SELECT LOCATION_CODE FROM TM_WELL_VISIT WHERE FIELD='" + field + "' AND DAY=" + dayOfWeek;
//		Query q = this.getSession().createSQLQuery(query);
//		locationList = (List<String>) q.list();
//		return locationList;
//	}
	
	@SuppressWarnings("unchecked")
	public List<String> getVisitMatrix(List<String> fields, int dayOfWeek) {
		String commaSeparatedFields="";
		for(String field:fields){
			commaSeparatedFields+=field+"','";
		}
		List<String> locationList = new ArrayList<String>();
		String query = "SELECT LOCATION_CODE FROM TM_WELL_VISIT_DUMMY WHERE FIELD in('" + commaSeparatedFields + "') AND DAY=" + dayOfWeek;
		Query q = this.getSession().createSQLQuery(query);
		locationList = (List<String>) q.list();
		return locationList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ObxTaskDto> getVisitMatrixWithField(List<String> fields, int dayOfWeek) {
		String commaSeparatedFields = "";
		for (String field : fields) {
			commaSeparatedFields += field + "','";
		}
		List<ObxTaskDto> tasks = new ArrayList<ObxTaskDto>();
		try{
		String query = "SELECT WV.FIELD,WV.LOCATION_CODE,LC.LATITUDE,LC.LONGITUDE,WT.TIER,PL.LOCATION_TEXT FROM TM_WELL_VISIT_DUMMY WV "
				+ "JOIN PRODUCTION_LOCATION PL ON WV.LOCATION_CODE=PL.LOCATION_CODE "
				+ "JOIN LOCATION_COORDINATE LC ON LC.LOCATION_CODE=WV.LOCATION_CODE "
				+ "JOIN WELL_TIER WT ON WT.LOCATION_CODE=WV.LOCATION_CODE " + "WHERE WV.DAY=" + dayOfWeek
				+ " AND WV.FIELD in ('" + commaSeparatedFields + "')";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = (List<Object[]>) q.list();
		if(!ServicesUtil.isEmpty(resultList)){
			for(Object[] obj:resultList){
				ObxTaskDto task=new ObxTaskDto();
				task.setField((String) obj[0]);
				task.setLocationCode((String)obj[1]);
				task.setLatitude(((BigDecimal)obj[2]).doubleValue());
				task.setLongitude(((BigDecimal)obj[3]).doubleValue());
				task.setTier((String)obj[4]);
				task.setLocationText((String) obj[5]);
				tasks.add(task);
			}
		}}
		catch (Exception e) {
			logger.error("OBXSchedulerDao.getVisitMatrixWithField Exception "+e.getMessage());
		}
		return tasks;
	}

//	@SuppressWarnings("unchecked")
//	public List<NearbyTaskDto> getNearbyAssignedTask(double lat, double lon,String userId) {
//		String query = "SELECT top 10 TE.TASK_ID, TE.DESCRIPTION, O.TASK_OWNER, P.LOCATION_TEXT, L.LONGITUDE, L.LATITUDE, "
//				+ "((L.LONGITUDE - " + lon + ")*(L.LONGITUDE - " + lon + ")) + ((L.LATITUDE - " + lat
//				+ ")*(L.LATITUDE - " + lat + ")) as RELATIVE_DISTANCES , O.TASK_OWNER_DISP , TE.PROCESS_ID , PE.LOC_CODE ,A.INS_VALUE "
//				+ "FROM TM_TASK_EVNTS TE, TM_TASK_OWNER O, TM_PROC_EVNTS PE, LOCATION_COORDINATE L, PRODUCTION_LOCATION P , TM_ATTR_INSTS A "
//				+ "WHERE TE.STATUS = 'ASSIGNED' AND TE.ORIGIN = 'Dispatch' AND A.ATTR_TEMP_ID='123456' AND A.TASK_ID=TE.TASK_ID AND TE.TASK_ID = O.TASK_ID "
//				+ "AND PE.PROCESS_ID = TE.PROCESS_ID AND PE.LOC_CODE = L.LOCATION_CODE AND L.LOCATION_CODE = P.LOCATION_CODE "
//				+" AND O.TASK_OWNER_EMAIL!='"+userId+"' "
//				+ "ORDER BY ((L.LONGITUDE - " + lon + ")*(L.LONGITUDE - " + lon + ")) + ((L.LATITUDE - " + lat
//				+ ")*(L.LATITUDE - " + lat + "))";
//		 logger.error("OBXSchedulerDao.getNearbyAssignedTask(" + lat + ", " + lon + "):query=" + query);
//		Query q = this.getSession().createSQLQuery(query);
//		List<Object[]> list = q.list();
//		List<NearbyTaskDto> returnList = new ArrayList<NearbyTaskDto>();
//		for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
//			Object[] objects = iterator.next();
//			NearbyTaskDto nearbyTaskDto = new NearbyTaskDto();
//			nearbyTaskDto.setTaskId((String) objects[0]);
//			nearbyTaskDto.setTaskDescription((String) objects[1]);
//			nearbyTaskDto.setAssignee((String) objects[2]);
//			nearbyTaskDto.setLocationText((String) objects[3]);
//			nearbyTaskDto.setToLongitude(objects[4] == null ? null : ((BigDecimal) objects[4]).doubleValue());
//			nearbyTaskDto.setToLatitude(objects[5] == null ? null : ((BigDecimal) objects[5]).doubleValue());
//			nearbyTaskDto.setAssigneeName(objects[7] == null ? null : (String) objects[7]);
//			nearbyTaskDto.setProcessId(objects[8] == null ? null : (String) objects[8]);
//			nearbyTaskDto.setLocationCode(objects[9] == null ? null : (String) objects[9]);
//			nearbyTaskDto.setSubClassification(objects[10] == null ? null : (String) objects[10]);
//			returnList.add(nearbyTaskDto);
//		}
////		logger.error("OBXSchedulerDao.getNearbyAssignedTask():returnList;" + returnList);
//		return returnList;
//	}
//	
//public ResponseMessage updateOBXTaskUser(ObxTaskUpdateDto dto) {
//	
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
//		responseMessage.setStatus(MurphyConstant.FAILURE);
//		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
//		String response = MurphyConstant.FAILURE;
//		
//		try {
//						
//			TaskEventsDoPK taskEventsDoPK = new TaskEventsDoPK();
//			taskEventsDoPK.setTaskId(dto.getTaskId());
//			taskEventsDoPK.setProcessId(dto.getProcessId());					
//			TaskEventsDo taskEventsDo = (TaskEventsDo)taskEventsDao.getSession().get(TaskEventsDo.class, taskEventsDoPK);
//			TaskEventsDto taskEventDto = taskEventsDao.exportDto(taskEventsDo);
//			
//			TaskOwnersDoPK taskOwnersDoPK = new TaskOwnersDoPK();
//			taskOwnersDoPK.setTaskId(dto.getTaskId());
//			taskOwnersDoPK.setTaskOwner(dto.getDispatchTaskEmail());
//			TaskOwnersDo taskOwnerEntity = (TaskOwnersDo)taskOwnersDao.getSession().get(TaskOwnersDo.class, taskOwnersDoPK);
//			TaskOwnersDto taskOwnersDto = taskOwnersDao.exportDto(taskOwnerEntity);
//			
//			
//			TaskOwnersDto taskOwnersDto2 = new TaskOwnersDto();
//			String obxLocationCode = getObxLocationCode(dto.getObxTaskIdEndTimeList());
//			
//			taskOwnersDto2 = setNewTaskOwner(taskOwnersDto, taskOwnersDto2,dto.getAssignUserEmail(),dto.getObxTaskIdEndTimeList(),dto.getLocationCode(),obxLocationCode);
//			
//			List<TaskOwnersDto> taskOwnersList = new ArrayList<>();
//			taskOwnersList.add(taskOwnersDto2);
//			response = taskOwnersDao.updateOBXOwners(taskOwnersList, dto.getTaskId(), dto.getLocationCode(), taskEventDto.getOrigin());
//			
//			UserIDPMappingDo userDao = userMappingDao.getUserByEmail(dto.getAssignUserEmail());
//			
//			if (MurphyConstant.SUCCESS.equals(response)) {
//				response = taskEventsDao.updateTaskEventStatus(dto.getTaskId(), dto.getProcessId(), dto.getAssignUserEmail(), userDao.getUserFirstName()+" "+userDao.getUserLastName(), MurphyConstant.ASSIGN, dto.getUserUpdatedAt(), taskEventsDo.getDescription());
//				if (MurphyConstant.SUCCESS.equals(response)){
//					
//					responseMessage.setMessage("Task Reassigned Successfully");
//					responseMessage.setStatus(MurphyConstant.SUCCESS);
//					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerDao][updateOBXTaskUser][error]"+e.getMessage());
//		}
//		
//		return responseMessage;
//				
//	}
	
//	private TaskOwnersDto setNewTaskOwner(TaskOwnersDto dto, TaskOwnersDto dto1, String email, List<ObxTaskIdEndTimeListDto> taskIdEndTimeList, String dispatchLocationCode, String obxLocationCode)
//	{
//		
//		try{
//			dto1.setEstResolveTime(dto.getEstResolveTime());
//			dto1.setCustomTime(dto.getCustomTime());
//			dto1.setOwnerEmail(email);
//			dto1.setTaskId(dto.getTaskId());
//			dto1.setTaskOwner(email);
//			dto1.setTier(dto.getTier());
//			Double estimatedDriveTime = getEstDriveTime(dispatchLocationCode, obxLocationCode); //  estimated drive time for new dispatch task assign to OBX
//			dto1.setEstDriveTime(estimatedDriveTime);
//			dto1.setCustomTime(0.00);
//			Double totalTime = dto.getEstResolveTime() + 0.00; // total time = drive time + estimated resolve time
//			if (estimatedDriveTime != -1) {
//				totalTime = estimatedDriveTime + totalTime;
//			}
//			
//			if (findStartTime(taskIdEndTimeList) != null) {
//				dto1.setStartTime(findStartTime(taskIdEndTimeList));
//				dto1.setEndTime(ServicesUtil.getDateWithInterval(findStartTime(taskIdEndTimeList), totalTime.intValue(), MurphyConstant.MINUTES));
//			} else {
//				Date d = new Date();
//				dto1.setStartTime(d);
//				dto1.setEndTime(ServicesUtil.getDateWithInterval(d, totalTime.intValue(), MurphyConstant.MINUTES));
//			}
//			
//			UserIDPMappingDo userDao = userMappingDao.getUserByEmail(email);
//			
//			dto1.setTaskOwnerDisplayName(userDao.getUserFirstName()+" "+userDao.getUserLastName());
//			dto1.setpId(userDao.getpId());
//		}catch(Exception e){
//			logger.error("[OBXSchedulerDao][setNewTaskOwner][error]"+e.getMessage());
//		}
//		return dto1;	
//	}
	
//	private Date findStartTime(List<ObxTaskIdEndTimeListDto> taskIdEndTimeList) {
//
//		Date latest = null;
//
//		try {
//			if (!ServicesUtil.isEmpty(taskIdEndTimeList)) {
//
//				ObxTaskIdEndTimeListDto dto = taskIdEndTimeList.get(0);
//				latest = dto.getEndTime();
//
//				for (int i = 1; i < taskIdEndTimeList.size(); i++) {
//
//					ObxTaskIdEndTimeListDto dto1 = taskIdEndTimeList.get(1);
//					if (dto1.getEndTime().after(latest)) {
//						latest = dto1.getEndTime();
//					}
//
//				}
//			}
//		} catch (Exception e) {
//			logger.error("[OBXSchedulerDao][findStartTime][error]"+e.getMessage());
//		}
//		return latest;
//
//	}
	
//	private Double getEstDriveTime(String dispatchLocationCode, String obxLocationCode) {
//		
//		logger.error("[OBXSchedulerDao][getEstDriveTime][dispatchLocationCode]"+dispatchLocationCode);
//		logger.error("[OBXSchedulerDao][getEstDriveTime][obxLocationCode]"+obxLocationCode);
//		if(dispatchLocationCode != null && obxLocationCode != null){
//			Coordinates fromCoordinate = geoTabDao.getLatLongByLocationCode(obxLocationCode);
//			Coordinates toCoordinate = geoTabDao.getLatLongByLocationCode(dispatchLocationCode);
//			ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(fromCoordinate, toCoordinate);		
//			if (arcResponse.getResponseMessage().getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
//				return arcResponse.getTotalDriveTime();
//			}
//		}
//		return 0.00;
//	}
	
//	public String getObxLocationCode(List<ObxTaskIdEndTimeListDto> taskIdEndTimeList) {
//		
//		try{
//			Date latest = findStartTime(taskIdEndTimeList);
//			String taskId = "";
//			for (ObxTaskIdEndTimeListDto obxTaskIdEndTimeListDto : taskIdEndTimeList) {
//				
//				if(obxTaskIdEndTimeListDto.getEndTime().equals(latest)){
//					taskId = obxTaskIdEndTimeListDto.getTaskId();
//				}
//			}
//			
//			String query = "SELECT PE.LOC_CODE FROM TM_PROC_EVNTS PE WHERE PE.PROCESS_ID ="
//	                       +"(select te.process_id from TM_TASK_EVNTS te where te.task_id = '"+taskId+"')";
//			Object result = this.getSession().createSQLQuery(query).uniqueResult();
//			if(!ServicesUtil.isEmpty(result)) {
//				return (String) result;
//			}
//		}catch(Exception e ){
//			logger.error("[OBXSchedulerDao][getObxLocationCode][error]"+e.getMessage());
//		}
//		return null;
//	}
}