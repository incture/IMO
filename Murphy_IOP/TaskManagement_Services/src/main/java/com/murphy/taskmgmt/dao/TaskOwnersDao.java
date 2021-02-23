package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.StartTimeResponseDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.dto.TaskSchedulingUpdateDto;
import com.murphy.taskmgmt.entity.TaskOwnersDo;
import com.murphy.taskmgmt.entity.TaskOwnersDoPK;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("TaskOwnersDao")
@javax.transaction.Transactional
public class TaskOwnersDao extends BaseDao<TaskOwnersDo, TaskOwnersDto> {

	@Autowired
	private CustomAttrInstancesDao attrDao;

	@Autowired
	private CustomAttrTemplateDao attrTempDao;

	@Autowired
	private GeoTabDao geoDao;

	@Autowired
	private UserIDPMappingDao userDao;

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(TaskOwnersDao.class);
	
	static DateFormat utcToStringFormatter = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss a");
	
	public TaskOwnersDao() {
	}
	
	@Override
	protected TaskOwnersDto exportDto(TaskOwnersDo entity) {
		TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
		taskOwnersDto.setTaskId(entity.getTaskOwnersDoPK().getTaskId());
		taskOwnersDto.setTaskOwner(entity.getTaskOwnersDoPK().getTaskOwner());
		// if (!ServicesUtil.isEmpty(entity.getIsProcessed()))
		// taskOwnersDto.setIsProcessed(entity.getIsProcessed());
		if (!ServicesUtil.isEmpty(entity.getTaskOwnerDisplayName()))
			taskOwnersDto.setTaskOwnerDisplayName(entity.getTaskOwnerDisplayName());
		if (!ServicesUtil.isEmpty(entity.getOwnerEmail()))
			taskOwnersDto.setOwnerEmail(entity.getOwnerEmail());
		// if (!ServicesUtil.isEmpty(entity.getIsSubstituted()))
		// taskOwnersDto.setIsSubstituted(entity.getIsSubstituted());
		if (!ServicesUtil.isEmpty(entity.getPriority()))
			taskOwnersDto.setPriority(entity.getPriority());
		if (!ServicesUtil.isEmpty(entity.getStartTime()))
			taskOwnersDto.setStartTime(entity.getStartTime());
		if (!ServicesUtil.isEmpty(entity.getEndTime()))
			taskOwnersDto.setEndTime(entity.getEndTime());
		if (!ServicesUtil.isEmpty(entity.getEstDriveTime()))
			taskOwnersDto.setEstDriveTime(entity.getEstDriveTime());
		if (!ServicesUtil.isEmpty(entity.getEstResolveTime()))
			taskOwnersDto.setEstResolveTime(entity.getEstResolveTime());
		if (!ServicesUtil.isEmpty(entity.getTier()))
			taskOwnersDto.setTier(entity.getTier());
		if (!ServicesUtil.isEmpty(entity.getCustomTime()))
			taskOwnersDto.setCustomTime(entity.getCustomTime());
		if (!ServicesUtil.isEmpty(entity.getpId()))
			taskOwnersDto.setpId(entity.getpId());

		return taskOwnersDto;
	}

	@Override
	protected TaskOwnersDo importDto(TaskOwnersDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		TaskOwnersDo entity = new TaskOwnersDo();
		entity.setTaskOwnersDoPK(new TaskOwnersDoPK());
		;
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			entity.getTaskOwnersDoPK().setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getTaskOwner()))
			entity.getTaskOwnersDoPK().setTaskOwner(fromDto.getTaskOwner());
		// if (!ServicesUtil.isEmpty(fromDto.getIsProcessed()))
		// entity.setIsProcessed(fromDto.getIsProcessed());
		if (!ServicesUtil.isEmpty(fromDto.getTaskOwnerDisplayName()))
			entity.setTaskOwnerDisplayName(fromDto.getTaskOwnerDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getOwnerEmail()))
			entity.setOwnerEmail(fromDto.getOwnerEmail());
		// if (!ServicesUtil.isEmpty(fromDto.getIsSubstituted()))
		// entity.setIsSubstituted(fromDto.getIsSubstituted());
		if (!ServicesUtil.isEmpty(fromDto.getPriority()))
			entity.setPriority(fromDto.getPriority());
		if (!ServicesUtil.isEmpty(fromDto.getStartTime()))
			entity.setStartTime(fromDto.getStartTime());
		if (!ServicesUtil.isEmpty(fromDto.getEndTime()))
			entity.setEndTime(fromDto.getEndTime());
		if (!ServicesUtil.isEmpty(fromDto.getEstDriveTime()))
			entity.setEstDriveTime(fromDto.getEstDriveTime());
		if (!ServicesUtil.isEmpty(fromDto.getEstResolveTime()))
			entity.setEstResolveTime(fromDto.getEstResolveTime());
		if (!ServicesUtil.isEmpty(fromDto.getTier()))
			entity.setTier(fromDto.getTier());
		if (!ServicesUtil.isEmpty(fromDto.getCustomTime()))
			entity.setCustomTime(fromDto.getCustomTime());
		if (!ServicesUtil.isEmpty(fromDto.getpId()))
			entity.setpId(fromDto.getpId());

		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<TaskOwnersDto> getTaskOwnersbyId(String taskInstanceId, String role) {

		String query = "SELECT distinct C.TASK_OWNER AS OWNER, C.TASK_OWNER_DISP AS OWNER_NAME from TM_TASK_OWNER C  ";
		if (!ServicesUtil.isEmpty(taskInstanceId)) {
			query = query + "WHERE  C.TASK_ID='" + taskInstanceId + "'";
		} else if (!ServicesUtil.isEmpty(role)) {
			role += ',' + MurphyConstant.OBX_CREATOR;
			role = ServicesUtil.getStringForInQuery(role);

			/*
			 * ---- This for the issue where user name is not coming if user has
			 * only resolved tasks.
			 * 
			 * query = query +
			 * " , TM_TASK_EVNTS te , TM_PROC_EVNTS pe WHERE  C.TASK_ID = te.TASK_ID and pe.PROCESS_ID = te.PROCESS_ID and pe.USER_GROUP in ("
			 * + role + ") " + "and te.status not in ('" +
			 * MurphyConstant.COMPLETE + "','" + MurphyConstant.RETURN + "','" +
			 * MurphyConstant.RESOLVE + "')"; ---
			 * 
			 */

			// 06Sep'18 - Changes for the issue where user name is not coming if
			// user has only resolved/completed tasks.

			query = query
					+ " , TM_TASK_EVNTS te , TM_PROC_EVNTS pe WHERE  C.TASK_ID = te.TASK_ID and pe.PROCESS_ID = te.PROCESS_ID and pe.USER_GROUP in ("
					+ role + ") " + "and te.status not in ('" + MurphyConstant.RETURN + "')";
		}
		Query q = this.getSession().createSQLQuery(query);
		// , "TaskOwnersListResult");
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			List<TaskOwnersDto> dtoList = new ArrayList<TaskOwnersDto>();
			for (Object[] obj : resultList) {
				TaskOwnersDto dto = new TaskOwnersDto();
				if (!ServicesUtil.isEmpty(obj[0]))
					dto.setTaskOwner((String) obj[0]);
				if (!ServicesUtil.isEmpty(obj[1]))
					dto.setTaskOwnerDisplayName((String) obj[1]);
				dtoList.add(dto);
			}

			return dtoList;
		}
		return null;
	}

	public String createOwner(TaskOwnersDto dto) {
		String reponse = MurphyConstant.FAILURE;
		try {
			create(dto);
			reponse = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][createOwner][error]" + e.getMessage());
		}
		return reponse;

	}

	public String updateOwners(List<TaskOwnersDto> owners, String taskId, String locationCode, String origin) {
		String respone = MurphyConstant.SUCCESS;
		List<String> existingOwners = getExistingOwners(taskId);
		String owner = "";
		try {
			if (!ServicesUtil.isEmpty(owners)) {
				for (TaskOwnersDto dto : owners) {
					dto.setTaskId(taskId);
					if (!ServicesUtil.isEmpty(existingOwners) && !existingOwners.contains(dto.getTaskOwner())) {
						dto = getOwnerDtoWithPriority(dto, locationCode, getResolveTimeByTaskId(dto.getTaskId()));
						if (!createOwner(dto).equals(MurphyConstant.SUCCESS)) {
							respone = MurphyConstant.FAILURE;
						}
					} else if (!ServicesUtil.isEmpty(existingOwners)) {
						existingOwners.remove(dto.getTaskOwner());
					} else {
						if (!createOwner(dto).equals(MurphyConstant.SUCCESS)) {
							respone = MurphyConstant.FAILURE;
						}
					}
					owner = owner + ", " + dto.getTaskOwnerDisplayName();
				}
				if (!ServicesUtil.isEmpty(owner)) {
					owner = owner.substring(1, owner.length());
				}
				if (!ServicesUtil.isEmpty(existingOwners)) {
					deleteNonExistingTasks(existingOwners, taskId);
				}
			}

			if (respone.equals(MurphyConstant.SUCCESS)) {
				if (origin.equals(MurphyConstant.INQUIRY)) {
					respone = attrDao.setAttrValueTo(taskId, owner, "INQ03");
				} else {
					respone = attrDao.setAttrValueTo(taskId, owner, "1234','NDO3");
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][updateOwners][error]" + e.getMessage());
		}
		return respone;
	}

	private float getResolveTimeByTaskId(String taskId) {

		String query = "SELECT DISTINCT(C.EST_RESOLVE_TIME) AS OWNER from TM_TASK_OWNER C  WHERE  C.TASK_ID='" + taskId
				+ "'";
		Query q = this.getSession().createSQLQuery(query);
		Double obj = (Double) q.uniqueResult();
		return (obj).floatValue();

	}

	@SuppressWarnings("unchecked")
	private List<String> getExistingOwners(String taskId) {

		String query = "SELECT C.TASK_OWNER AS OWNER from TM_TASK_OWNER C  WHERE  C.TASK_ID='" + taskId + "'";
		Query q = this.getSession().createSQLQuery(query);
		return (List<String>) q.list();
	}

	private String deleteNonExistingTasks(List<String> instanceList, String taskId) {
		String response = MurphyConstant.FAILURE;
		try {
			String listString = ServicesUtil.getStringFromList(instanceList);
			String query = "DELETE FROM TM_TASK_OWNER C where C.TASK_ID='" + taskId + "' and C.TASK_OWNER IN ("
					+ listString + ")";
			Query q = this.getSession().createSQLQuery(query);
			q.executeUpdate();
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][deleteNonExistingTasks][error] - " + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public TaskOwnersDto getLatestRecordOfUser(String userId) {
		// String query = "SELECT C from TaskOwnersDo C WHERE
		// C.taskOwnersDoPK.taskOwner='" + userId + "' and C.priority = (select
		// max(tx.priority) from TaskOwnersDo tx where
		// tx.taskOwnersDoPK.taskOwner='" +
		// userId + "' )";
		String query = "SELECT C.START_TIME,C.END_TIME,C.priority,C.task_id  from TM_TASK_OWNER C  WHERE  C.TASK_OWNER='"
				+ userId + "' and C.priority = (select max(tx.priority) from TM_TASK_OWNER tx where  tx.TASK_OWNER='"
				+ userId + "' and tx.priority <> 0  )";

		// System.err.println("[Murphy][getLatestRecordOfUser][query]"+query);
		TaskOwnersDto dto = null;
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> response = (List<Object[]>) q.list();

		if (!ServicesUtil.isEmpty(response)) {
			for (Object[] resObject : response) {
				dto = new TaskOwnersDto();
				dto.setEndTime(ServicesUtil.isEmpty(resObject[1]) ? null
						: ServicesUtil.convertFromZoneToZone(null, resObject[1], "", "", MurphyConstant.DATE_DB_FORMATE,
								MurphyConstant.DATE_DB_FORMATE));
				dto.setStartTime(ServicesUtil.isEmpty(resObject[0]) ? null
						: ServicesUtil.convertFromZoneToZone(null, resObject[0], "", "", MurphyConstant.DATE_DB_FORMATE,
								MurphyConstant.DATE_DB_FORMATE));
				dto.setPriority(ServicesUtil.isEmpty(resObject[2]) ? 0 : (Integer) resObject[2]);
				dto.setTaskId(ServicesUtil.isEmpty(resObject[3]) ? null : (String) resObject[3]);
			}

		}
		// System.err.println("[Murphy][getLatestRecordOfUser][response]"+dto);
		return dto;
	}

	/* Scheduling based on Date Start */

	public Date getEndTimeOfUsersTask(String userId, String taskId) {
		Date response = new Date();
		String query = "SELECT max(C.END_TIME) AS END_TIME from TM_TASK_OWNER C  WHERE  C.TASK_OWNER='" + userId + "'";
		if (!ServicesUtil.isEmpty(taskId)) {
			query = query + " and c.task_id ='" + taskId + "'";
		}
		Query q = this.getSession().createSQLQuery(query);
		Object resObject = (Object) q.uniqueResult();
		if (!ServicesUtil.isEmpty(response)) {
			response = ServicesUtil.convertFromZoneToZone(null, resObject, "", "", MurphyConstant.DATE_DB_FORMATE,
					MurphyConstant.DATE_DB_FORMATE);
		}
		return response;
	}

	//
	@SuppressWarnings("unused")
	public String updateStartEndTime(String userId, Date startTime, Date endTime, String taskId, double estDriveTime) {
		String response = MurphyConstant.FAILURE;
		try {
			String query = " UPDATE TM_TASK_OWNER SET START_TIME = TO_TIMESTAMP('"
					+ ServicesUtil.convertFromZoneToZoneString(startTime, null, "", "", "",
							MurphyConstant.DATE_DB_FORMATE_SD)
					+ "', 'yyyy-mm-dd hh24:mi:ss') , END_TIME = TO_TIMESTAMP('" + ServicesUtil
							.convertFromZoneToZoneString(endTime, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
					+ "', 'yyyy-mm-dd hh24:mi:ss') ";

			if (!ServicesUtil.isEmpty(estDriveTime) && estDriveTime != -20) {
				query = query + ", EST_DRIVE_TIME = " + estDriveTime + "";
			}
			query = query + " where TASK_OWNER='" + userId + "' and task_id = '" + taskId + "'";

			logger.error("[Murphy][TaskOwnersDao][updatePriority][query]  " + query);

			Query q = this.getSession().createSQLQuery(query);
			Integer result = (Integer) q.executeUpdate();
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][updatePriority][error]  " + e.getMessage());
		}
		return response;

	}
	//
	// @SuppressWarnings("unused")
	// public String updatePriorityWhenResolved(String taskId){
	// String response = MurphyConstant.FAILURE;
	// try{
	// String query = " UPDATE TM_TASK_OWNER st SET START_TIME = null , END_TIME
	// =
	// null where task_id = '"+taskId+"')";
	//
	// logger.error("[Murphy][TaskOwnersDao][updatePriorityWhenResolved][query]
	// " +
	// query);
	//
	// Query q = this.getSession().createSQLQuery(query);
	// Integer result = (Integer) q.executeUpdate();
	// response = MurphyConstant.SUCCESS;
	// }catch(Exception e){
	// logger.error("[Murphy][TaskOwnersDao][updatePriorityWhenResolved][error]
	// " +
	// e.getMessage());
	// }
	// return response;
	//
	// }

	/* Scheduling based on Priority End */

	@SuppressWarnings("unchecked")
	public String intialUpdateOfPriority() {
		String response = MurphyConstant.FAILURE;
		try {
			String query = "SELECT distinct C.TASK_OWNER AS OWNER from TM_TASK_OWNER C  ";
			Query q = this.getSession().createSQLQuery(query);
			List<String> ownerList = (List<String>) q.list();
			for (String owner : ownerList) {
				int count = 1;
				String taskQuery = "SELECT t.task_id, t.updated_at from TM_TASK_OWNER C, TM_TASK_EVNTS t where  t.task_id = c.task_id and t.status not in ('"
						+ MurphyConstant.COMPLETE + "','" + MurphyConstant.RETURN + "','" + MurphyConstant.RESOLVE
						+ "') and C.TASK_OWNER = '" + owner + "' order by t.created_at asc";
				Query qu = this.getSession().createSQLQuery(taskQuery);
				List<Object[]> taskList = (List<Object[]>) qu.list();
				for (Object[] taskId : taskList) {
					String updateQuery = " UPDATE TM_TASK_OWNER SET PRIORITY = " + count + " where task_id = '"
							+ taskId[0] + "' and task_owner = '" + owner + "'";
					Query que = this.getSession().createSQLQuery(updateQuery);
					que.executeUpdate();
					count++;
				}
			}
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][updatePriorityWhenResolved][error]  " + e.getMessage());
		}
		return response;
	}

	/* Scheduling based on Priority Start */

	public int getLatestPriorityOfUser(String userId) {

		String query = "SELECT max(C.PRIORITY) AS PRIORITY from TM_TASK_OWNER C  WHERE  C.TASK_OWNER='" + userId + "'";
		Query q = this.getSession().createSQLQuery(query);
		Integer response = (Integer) q.uniqueResult();
		if (ServicesUtil.isEmpty(response)) {
			response = 0;
		}
		return response + 1;
	}

	@SuppressWarnings("unused")
	public String updatePriority(String userId, int currentPosition, int newPosition, String taskId) {
		String response = MurphyConstant.FAILURE;
		try {
			String query = " UPDATE TM_TASK_OWNER SET PRIORITY = ( CASE WHEN (";

			if (newPosition > currentPosition) {
				query = query + "PRIORITY <= " + newPosition + " and  PRIORITY > " + currentPosition
						+ ") THEN PRIORITY -  1 ";
			} else {
				query = query + "PRIORITY >= " + newPosition + " and  PRIORITY < " + currentPosition
						+ ") THEN PRIORITY  +  1 ";
			}

			query = query + "WHEN PRIORITY = " + currentPosition + " THEN " + newPosition + "";

			query = query + " ELSE ( PRIORITY) END )  where TASK_OWNER='" + userId + "' ";

			// logger.error("[Murphy][TaskOwnersDao][updatePriority][query New]
			// " + query);

			Query q = this.getSession().createSQLQuery(query);
			Integer result = (Integer) q.executeUpdate();
			if (updateTimingsBasedOnPriority(userId, currentPosition, newPosition, taskId)
					.equals(MurphyConstant.SUCCESS)) {
				response = MurphyConstant.SUCCESS;
			}

		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][updatePriority][error]  " + e.getMessage());
		}
		return response;

	}

	@SuppressWarnings({ "unchecked" })
	public String updateTimingsBasedOnPriority(String userId, int currentPosition, int newPosition, String taskId) {
		String response = MurphyConstant.FAILURE;
		try {
			int lowerValue;
			if (currentPosition > newPosition) {
				lowerValue = newPosition - 1;
			} else {
				lowerValue = currentPosition - 1;
			}

			String getQuery = "Select tw.task_id as task_id, tw.PRIORITY as priority, tw.start_time as start_time ,tw.end_time as end_time,"
					// + "(SELECT max(i.INS_VALUE) FROM TM_ATTR_INSTS AS i WHERE
					// i.ATTR_TEMP_ID =
					// '123' AND te.TASK_ID = tw.TASK_ID ) AS LOCATION_CODE ,"
					+ "pe.LOC_CODE as LOC_CODE, "
					+ "(SELECT max(i.INS_VALUE) FROM TM_ATTR_INSTS AS i WHERE ATTR_TEMP_ID = '123456' AND te.TASK_ID = i.TASK_ID ) AS SUBCLASSIFICATION "
					+ " from TM_TASK_OWNER tw , tm_task_evnts te , tm_proc_evnts pe where te.task_id = tw.task_id  and te.process_id = pe.process_id and tw.PRIORITY >= "
					+ lowerValue + " and tw.task_owner = '" + userId + "' order by tw.PRIORITY ";

			Query q = this.getSession().createSQLQuery(getQuery);
			List<Object[]> priorityResult = (List<Object[]>) q.list();

			Date startDateOfCurrent = null, endDateOfCurrent = null, endDateOfPrev = null;
			float estResolveTime = 0;
			for (int i = 0; i < priorityResult.size() - 1; i++) {
				double driveTime = -20;
				Object[] prev = priorityResult.get(i);
				Object[] current = priorityResult.get(i + 1);
				int priority = (Integer) current[1];
				Date currentDate = new Date();
				currentDate = ServicesUtil.getDateWithInterval(currentDate, -330, MurphyConstant.MINUTES);

				startDateOfCurrent = ServicesUtil.convertFromZoneToZone(null, current[2], "", "",
						MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE);
				endDateOfCurrent = ServicesUtil.convertFromZoneToZone(null, current[3], "", "",
						MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE);
				endDateOfPrev = ServicesUtil.convertFromZoneToZone(null, prev[3], "", "",
						MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE);
				if ((priority == newPosition) || (priority == newPosition + 1)
						|| ((currentPosition > newPosition) && (priority == currentPosition + 1))
						|| ((currentPosition < newPosition) && (priority == currentPosition))) {

					String classification = attrTempDao.getTaskClassification((String) current[0]);
					estResolveTime = attrTempDao.getEstTimeForSubClass(classification, (String) current[5], userId);
					ArcGISResponseDto respo = geoDao.getRoadDistance((String) prev[4], (String) current[4], userId);
					driveTime = respo.getTotalDriveTime();
					Double totalTime = driveTime + estResolveTime;
					if (endDateOfPrev.before(currentDate)) {
						startDateOfCurrent = currentDate;
					} else {
						startDateOfCurrent = endDateOfPrev;
					}
					endDateOfCurrent = ServicesUtil.getDateWithInterval(startDateOfCurrent, totalTime.intValue(),
							MurphyConstant.MINUTES);

				} else {
					if (startDateOfCurrent != endDateOfPrev) {
						int difference = (int) (ServicesUtil.getDiffBtwnTwoDates(endDateOfPrev, startDateOfCurrent,
								MurphyConstant.MINUTES));
						// int difference = (int) ((endDateOfPrev.getTime() -
						// startDateOfCurrent.getTime())/60000);
						if (endDateOfPrev.before(currentDate)) {
							startDateOfCurrent = currentDate;
						} else {
							startDateOfCurrent = endDateOfPrev;
						}
						endDateOfCurrent = ServicesUtil.getDateWithInterval(endDateOfCurrent, difference,
								MurphyConstant.MINUTES);
					}
				}
				priorityResult.get(i + 1)[2] = ServicesUtil.convertFromZoneToZoneString(startDateOfCurrent, null, "",
						"", MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE);
				priorityResult.get(i + 1)[3] = ServicesUtil.convertFromZoneToZoneString(endDateOfCurrent, null, "", "",
						MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE);
				updateStartEndTime(userId, startDateOfCurrent, endDateOfCurrent, (String) current[0], driveTime);
			}
			response = MurphyConstant.SUCCESS;

		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][updatePriority][error]  " + e.getMessage());
		}
		return response;

	}

	@SuppressWarnings("unused")
	public String updatePriorityWhenResolved(String taskId) {
		String response = MurphyConstant.FAILURE;
		try {
			String query = " UPDATE TM_TASK_OWNER st SET PRIORITY = ( CASE "
					+ " WHEN (PRIORITY > (select max(o.PRIORITY) from TM_TASK_OWNER  o "
					+ " where o.task_owner = st.TASK_OWNER and o.task_id = '" + taskId + "')) THEN PRIORITY - 1 "
					+ " WHEN PRIORITY =  (select max(o.PRIORITY) from TM_TASK_OWNER  o"
					+ " where o.task_owner = st.TASK_OWNER and o.task_id = '" + taskId + "') THEN 0"
					+ " ELSE ( PRIORITY) END )  where TASK_OWNER in "
					+ " (select i.TASK_OWNER as TOWNER from TM_TASK_OWNER  i where i.task_id = '" + taskId + "')";

			logger.error("[Murphy][TaskOwnersDao][updatePriorityWhenResolved][query]  " + query);

			Query q = this.getSession().createSQLQuery(query);
			Integer result = (Integer) q.executeUpdate();
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][updatePriorityWhenResolved][error]  " + e.getMessage());
		}
		return response;

	}

	public TaskOwnersDto getOwnerDtoWithPriority(TaskOwnersDto ownerDto, String locationCode, float resolveTime) {
		TaskOwnersDto ownerLatestDto = getLatestRecordOfUser(ownerDto.getTaskOwner());
		Double totalTime = resolveTime + 0.00;
		Double driveTime = 0.00;
		int priority = 0;
		String prevLoc = "";
		Date latestDate = new Date();
		try{
			if (!ServicesUtil.isEmpty(ownerLatestDto)) {
				if (ownerLatestDto.getEndTime().getTime() > latestDate.getTime()) {
					latestDate = ownerLatestDto.getEndTime();
					prevLoc = getLocCodeFromTaskId(ownerLatestDto.getTaskId());
				}
				priority = ownerLatestDto.getPriority();
			}
			if (!locationCode.equals(prevLoc)) {
				ArcGISResponseDto respo = geoDao.getRoadDistance(prevLoc, locationCode, ownerDto.getTaskOwner());
				if(!ServicesUtil.isEmpty(respo) && !ServicesUtil.isEmpty(respo.getTotalDriveTime()))
					driveTime = respo.getTotalDriveTime();
				else
					driveTime = 0.00;
				// System.err.println("[Murphy][test in
				// create][prevLoc]"+prevLoc+"dto.getTaskEventDto().getLocationCode()"+locationCode+"resolveTime"+resolveTime+"[driveTime]"+driveTime);
			}
			if (driveTime != -1) {
				totalTime = driveTime + totalTime;
			}
			ownerDto.setEstDriveTime(driveTime);
			ownerDto.setEstResolveTime(resolveTime);
			ownerDto.setPriority(priority + 1);
			ownerDto.setStartTime(latestDate);
			ownerDto.setEndTime(ServicesUtil.getDateWithInterval(latestDate, totalTime.intValue(), MurphyConstant.MINUTES));
		}catch(Exception e){
			logger.error("[Murphy][TaskOwnersDao][getOwnerDtoWithPriority][error]  " + e);
		}
		return ownerDto;
	}

	public String getLocCodeFromTaskId(String taskId) {
		String updateQuery = "select pe.LOC_CODE from tm_task_evnts te , tm_proc_evnts pe where pe.process_id = te.process_id and te.task_id = '"
				+ taskId + "'";
		// System.err.println("[Murphy][TaskEventsDao][getLocCodeFromTaskId][updateQuery]"+updateQuery);
		Query q = this.getSession().createSQLQuery(updateQuery);
		return (String) q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public TaskOwnersDto getLastTaskOfUser(String userId) {
		String query = "SELECT tw.START_TIME,tw.END_TIME,tw.task_id  from TM_TASK_OWNER tw , tm_task_evnts te where te.task_id = tw.task_id  AND tw.TASK_OWNER='"
				+ userId
				+ "' AND te.STATUS NOT IN ('COMPLETED','RESOLVED') AND tw.END_TIME = (select max(tx.END_TIME) from TM_TASK_OWNER tx, tm_task_evnts tte where tte.task_id = tx.task_id  AND tx.TASK_OWNER='"
				+ userId + "' AND tte.STATUS NOT IN ('COMPLETED','RESOLVED') )";

		logger.error("[getLastTaskOfUser] : INFO- Query " + query);

		TaskOwnersDto dto = null;
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> response = (List<Object[]>) q.list();

		if (!ServicesUtil.isEmpty(response)) {
			for (Object[] resObject : response) {
				dto = new TaskOwnersDto();
				dto.setEndTime(ServicesUtil.isEmpty(resObject[1]) ? null
						: ServicesUtil.convertFromZoneToZone(null, resObject[1], "", "", MurphyConstant.DATE_DB_FORMATE,
								MurphyConstant.DATE_DB_FORMATE));
				dto.setStartTime(ServicesUtil.isEmpty(resObject[0]) ? null
						: ServicesUtil.convertFromZoneToZone(null, resObject[0], "", "", MurphyConstant.DATE_DB_FORMATE,
								MurphyConstant.DATE_DB_FORMATE));
				dto.setTaskId(ServicesUtil.isEmpty(resObject[2]) ? null : (String) resObject[2]);
			}

		}
		logger.error("[getLastTaskOfUser] : INFO lastDto " + dto);
		return dto;
	}

	@SuppressWarnings("unchecked")
	public String getTierFromWellTier(String locationCode) {
		String query = "SELECT TIER FROM WELL_TIER WHERE LOCATION_CODE = '" + locationCode + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<Object> response = (List<Object>) q.list();

		if (!ServicesUtil.isEmpty(response)) {

			logger.error("[getTierFromWellTier] : INFO TIER " + response.get(0));
			return (String) response.get(0);

		}
		return null;
	}

	@SuppressWarnings("unused")
	public String updateTaskDetailsOnShifting(TaskSchedulingUpdateDto dto) {

		String taskNextOldLocationTaskId = dto.getTaskNextOldLocationTaskId();
		String taskNextNewLocationTaskId = dto.getTaskNextNewLocationTaskId();
		String shiftedTaskTaskId = dto.getShiftedTaskTaskId();

		String response = MurphyConstant.FAILURE;

		if (!ServicesUtil.isEmpty(dto.getShiftedTaskTaskId())
				|| !ServicesUtil.isEmpty(dto.getTaskNextNewLocationTaskId())
				|| !ServicesUtil.isEmpty(dto.getTaskNextOldLocationTaskId()))
			try {
				// START Rescheduling task changes - 02 Dec 2019(Ankit)
				logger.error("dto.getShiftedTaskTaskId(): " + dto.toString());
				String taskIdTemp;
				if (!ServicesUtil.isEmpty(dto.getShiftedTaskTaskId())) {
					taskIdTemp = dto.getShiftedTaskTaskId();
				} else {
					taskIdTemp = dto.getTaskId();
				}
				// END Rescheduling task changes - 02 Dec 2019(Ankit)
				StringBuffer query = new StringBuffer("UPDATE TM_TASK_OWNER SET END_TIME = ( CASE ");
				if (!ServicesUtil.isEmpty(taskIdTemp)) {
					query.append("WHEN (TASK_ID = '" + taskIdTemp + "') THEN TO_TIMESTAMP('"
							+ ServicesUtil.convertFromZoneToZoneString(dto.getShiftedTaskEndDate(), null, "", "", "",
									MurphyConstant.DATE_DB_FORMATE_SD)
							+ "', 'yyyy-MM-dd HH24:mi:ss')");
				}
				if (!ServicesUtil.isEmpty(dto.getTaskNextNewLocationTaskId())) {
					query.append(
							"WHEN (TASK_ID = '" + dto.getTaskNextNewLocationTaskId() + "') THEN TO_TIMESTAMP('"
									+ ServicesUtil.convertFromZoneToZoneString(dto.getTaskNextNewLocationEndDate(),
											null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
									+ "', 'yyyy-MM-dd HH24:mi:ss')");
				}
				if (!ServicesUtil.isEmpty(dto.getTaskNextOldLocationTaskId())) {
					query.append(
							"WHEN (TASK_ID = '" + dto.getTaskNextOldLocationTaskId() + "') THEN TO_TIMESTAMP('"
									+ ServicesUtil.convertFromZoneToZoneString(dto.getTaskNextOldLocationEndDate(),
											null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
									+ "', 'yyyy-MM-dd HH24:mi:ss')");
				}
				query.append(" ELSE ( END_TIME) END )");

				query.append(",START_TIME = ( CASE ");
				
				
				
				if (!ServicesUtil.isEmpty(taskIdTemp)) {
					
					query.append("WHEN (TASK_ID = '" + taskIdTemp + "') THEN TO_TIMESTAMP('"
							+ ServicesUtil.convertFromZoneToZoneString(dto.getShiftedTaskStartDate(), null, "", "", "",
									MurphyConstant.DATE_DB_FORMATE_SD)
							+ "', 'yyyy-MM-dd HH24:mi:ss')");
				}

				query.append(" ELSE ( START_TIME) END )");

				query.append(",CUSTOM_TIME = ( CASE ");

				if (!ServicesUtil.isEmpty(taskIdTemp)) {
					query.append("WHEN (TASK_ID = '" + taskIdTemp + "') THEN "
							+ dto.getShiftedTaskCustomTime());
				}

				query.append(" ELSE ( CUSTOM_TIME) END )");

				if (!ServicesUtil.isEmpty(dto.getShiftedTaskDriveTime())
						|| !ServicesUtil.isEmpty(dto.getTaskNextNewLocationDriveTime())
						|| !ServicesUtil.isEmpty(dto.getTaskNextOldLocationDriveTime())) {
					query.append(",EST_DRIVE_TIME = ( CASE ");
				}

				if (!ServicesUtil.isEmpty(taskIdTemp)
						&& !ServicesUtil.isEmpty(dto.getShiftedTaskDriveTime())) {
					query.append(" WHEN (TASK_ID = '" + taskIdTemp + "') THEN "
							+ dto.getShiftedTaskDriveTime());
				}
				if (!ServicesUtil.isEmpty(dto.getTaskNextNewLocationTaskId())
						&& !ServicesUtil.isEmpty(dto.getTaskNextNewLocationDriveTime())) {
					query.append(" WHEN (TASK_ID = '" + dto.getTaskNextNewLocationTaskId() + "') THEN "
							+ dto.getTaskNextNewLocationDriveTime());
				}
				if (!ServicesUtil.isEmpty(dto.getTaskNextOldLocationTaskId())
						&& !ServicesUtil.isEmpty(dto.getTaskNextOldLocationDriveTime())) {
					query.append(" WHEN (TASK_ID = '" + dto.getTaskNextOldLocationTaskId() + "') THEN "
							+ dto.getTaskNextOldLocationDriveTime());
				}

				if (!ServicesUtil.isEmpty(dto.getShiftedTaskDriveTime())
						|| !ServicesUtil.isEmpty(dto.getTaskNextNewLocationDriveTime())
						|| !ServicesUtil.isEmpty(dto.getTaskNextOldLocationDriveTime())) {
					query.append(" ELSE (EST_DRIVE_TIME) END )");
				}

				query.append(" where TASK_OWNER ='" + dto.getTaskOwner() + "'" + " and task_id in ('"
						+ taskIdTemp + "','" + dto.getTaskNextNewLocationTaskId() + "','"
						+ dto.getTaskNextOldLocationTaskId() + "')");

				logger.error("[Murphy][TaskOwnersDao][updatePriority][query New] " + query);

				Query q = this.getSession().createSQLQuery(query.toString());
				Integer result = (Integer) q.executeUpdate();
				
//				Updating start time for future tasks
				logger.error("[Murphy][TaskOwnersDao][updatePriority] 01: " + dto.getShiftedTaskStartDate());
				try {
					String futureDate = utcToStringFormatter.format(dto.getShiftedTaskStartDate().getTime() + 
							TimeZone.getTimeZone(MurphyConstant.CST_ZONE).getOffset(dto.getShiftedTaskStartDate().getTime()));
					query = query.delete(0, query.length());
					query = query.append("UPDATE TM_ATTR_INSTS SET INS_VALUE = '" + futureDate + "' where "
							+ "TASK_ID = '" + taskIdTemp + "' AND ATTR_TEMP_ID = '123456789'");
					q = this.getSession().createSQLQuery(query.toString());
					result = (Integer) q.executeUpdate();
				} catch (Exception e) {
					logger.error("[Murphy][TaskOwnersDao][updatePriority][error] Not a future task " + e.toString());
				}
				// if (updateTimingsBasedOnPriority(userId, currentPosition,
				// newPosition, taskId)
				// .equals(MurphyConstant.SUCCESS)) {
				// response = MurphyConstant.SUCCESS;
				// }

			} catch (Exception e) {
				logger.error("[Murphy][TaskOwnersDao][updatePriority][error]  " + e.getMessage());
			}
		return response;

	}

	@SuppressWarnings("unchecked")
	public StartTimeResponseDto getNewStartOfUser(String userId, String locationCode, float resolveTimeInSecs, Double driveTimeFromOBX) {
		StartTimeResponseDto responseDto = null;
		Date response = new Date();
		String currentTimeInString = ServicesUtil.convertFromZoneToZoneString(response, "", "", "", "",
				MurphyConstant.DATE_IOS_FORMAT);
		try {
			String toGetMinStartTime = "(" + "Select min(next.start_time) from  tm_task_owner next "
					+ " left outer join  tm_task_evnts tx on tx.task_id = next.task_id where next.task_owner = '"
					+ userId + "' " + " and next.start_time > to_timestamp('" + currentTimeInString
					+ "' ,'yyyy-mm-dd hh24:mi:ss') " + " and tx.status <> '" + MurphyConstant.COMPLETE
					+ "'	and tx.origin = '" + MurphyConstant.DISPATCH_ORIGIN + "')";
			String toGetMinEndTime = "(" + "Select min(next.end_time) from  tm_task_owner next "
					+ " left outer join  tm_task_evnts tx on tx.task_id = next.task_id where next.task_owner = '"
					+ userId + "' " + " and next.end_time > to_timestamp('" + currentTimeInString
					+ "' ,'yyyy-mm-dd hh24:mi:ss') " + " and tx.status <> '" + MurphyConstant.COMPLETE
					+ "'	and tx.origin = '" + MurphyConstant.DISPATCH_ORIGIN + "')";

			String toGetTheStartENdTime = "(" + "Select min(m.start_time) from  tm_task_owner m "
					+ " left outer join  tm_task_evnts t on t.task_id = m.task_id where m.task_owner = '" + userId
					+ "' " + " and m.start_time =  " + toGetMinStartTime + " and  m.end_time = " + toGetMinEndTime + ""
					+ " and t.status <> '" + MurphyConstant.COMPLETE + "'	and t.origin = '"
					+ MurphyConstant.DISPATCH_ORIGIN + "')";

			String togetTheFirstTask = "select ''  as locCode , to_timestamp('" + currentTimeInString
					+ "' ,'yyyy-mm-dd hh24:mi:ss')  ,  " + " SECONDS_BETWEEN(  to_timestamp('" + currentTimeInString
					+ "' ,'yyyy-mm-dd hh24:mi:ss') " + " , " + toGetTheStartENdTime + " ) as secondsBtwn , "
					+ " cur.start_time as st from  tm_task_owner cur left outer join  tm_task_evnts te on te.task_id = cur.task_id "
					+ " left outer join  tm_proc_evnts pe on pe.process_id = te.process_id where pe.status <> 'COMPLETED'  "
					+ " and te.status not in ('COMPLETED' , 'RESOLVED') and te.origin = 'Dispatch'  "
					+ " and cur.task_owner = '" + userId + "' and cur.end_time > to_timestamp('" + currentTimeInString
					+ "' ,'yyyy-mm-dd hh24:mi:ss')  " + " and  " + " SECONDS_BETWEEN( to_timestamp('"
					+ currentTimeInString + "' ,'yyyy-mm-dd hh24:mi:ss') , " + toGetTheStartENdTime + " ) " + " >= "
					+ resolveTimeInSecs;

			String secondsBtwn = "SECONDS_BETWEEN( cur.end_time ,(Select min(next.start_time) from tm_task_owner next left outer join tm_task_evnts tx on tx.task_id = next.task_id  where next.task_owner = '"
					+ userId + "'" + " and next.end_time > cur.end_time" + " and tx.status <> '"
					+ MurphyConstant.COMPLETE + "'" + "	and tx.origin = '" + MurphyConstant.DISPATCH_ORIGIN + "'"
					+ ")) ";
			String maxEndTimeQuery = "  select max(maxend.end_time ) " + " from  tm_task_owner maxend "
					+ " left outer join  tm_task_evnts tenew on tenew.task_id = maxend.task_id "
					+ "  left outer join  tm_proc_evnts penew on penew.process_id = tenew.process_id  "
					+ " where penew.status <> '" + MurphyConstant.COMPLETE + "' " + " and tenew.status not in ('"
					+ MurphyConstant.COMPLETE + "' , '" + MurphyConstant.RESOLVE + "') " + "	and tenew.origin = '"
					+ MurphyConstant.DISPATCH_ORIGIN + "'" + " and maxend.task_owner = '" + userId + "'  "
					+ " and maxend.end_time >  to_timestamp('" + currentTimeInString + "' ,'yyyy-mm-dd hh24:mi:ss') ";

			String commonStartQuery = "select pe.loc_code ,cur.end_time , ";
			String commonMidQuery = " as secondsBtwn , cur.start_time as st" + " from tm_task_owner cur "
					+ " left outer join tm_task_evnts  te on te.task_id = cur.task_id "
					+ " left outer join tm_proc_evnts pe on pe.process_id = te.process_id  " + " where pe.status <> '"
					+ MurphyConstant.COMPLETE + "'" + " and te.status not in ('" + MurphyConstant.COMPLETE + "' , '"
					+ MurphyConstant.RESOLVE + "') " + "	and te.origin = '" + MurphyConstant.DISPATCH_ORIGIN + "'"
					+ " and cur.task_owner = '" + userId + "' " + " and cur.end_time > to_timestamp('"
					+ currentTimeInString + "' ,'yyyy-mm-dd hh24:mi:ss')  ";

			String query = togetTheFirstTask + " UNION " + commonStartQuery + secondsBtwn + commonMidQuery + " and "
					+ secondsBtwn + " >= " + resolveTimeInSecs + " UNION " + commonStartQuery + "-10.000"
					+ commonMidQuery + " and cur.end_time =  (" + maxEndTimeQuery + ")" + " order by st ";

			logger.error("[Murphy][TaskOwnersDao][getNewStartOfUser][query] " + query);

			Query q = this.getSession().createSQLQuery(query);
			
			//For first task of the day
			LocalTime midnight = LocalTime.MIDNIGHT;
			LocalDate today = LocalDate.now(ZoneId.of("UTC"));
			LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
			java.sql.Timestamp timestamp = Timestamp.valueOf(todayMidnight);
			
			String firstTaskOfDayQuery = "select  count(*) from tm_task_owner where start_time > ('" + timestamp 
					+ "') and start_time < to_timestamp('" + currentTimeInString
					+ "' ,'yyyy-mm-dd hh24:mi:ss') and task_owner = '" + userId + "'";
			
			Query firstTask = this.getSession().createSQLQuery(firstTaskOfDayQuery);
			Integer taskCount = ((BigInteger)firstTask.uniqueResult()).intValue();
			logger.error("[Murphy][TaskCount]" + taskCount);
			
			if(taskCount==0){
				//Printing FirstTaskOfDay Query
				logger.error("[Murphy][TaskOwnersDao][geStartTimeForTaskOfUser][query] " + firstTaskOfDayQuery);
				
				String locationCodeCentral = null;
				Double driveTime = 0.00;
				//count = 0;
				responseDto = new StartTimeResponseDto();
				if(locationCode.contains("CT00"))
					locationCodeCentral = MurphyConstant.CENTRAL_FACILITY_CATARINA;
				else if(locationCode.contains("KN00") || locationCode.contains("KS00"))
					locationCodeCentral = MurphyConstant.CENTRAL_FACILITY_KARNES;
				else
					locationCodeCentral = MurphyConstant.CENTRAL_FACILITY_TILDEN;
				
				logger.error("[locationCode] :"+locationCode + " [locationCodeCentral] "+locationCodeCentral);
				ArcGISResponseDto respo = geoDao.getRoadDistance(locationCodeCentral, locationCode, userId);
				if (!ServicesUtil.isEmpty(respo)) {
					if(!ServicesUtil.isEmpty(driveTimeFromOBX))
						driveTime = driveTimeFromOBX;
					else if (!ServicesUtil.isEmpty(respo.getTotalDriveTime()))
						driveTime = respo.getTotalDriveTime();
					else
						driveTime = 1.00;
					responseDto.setStartTime(response);
					responseDto.setDriveTime(driveTime);
				} else {
					logger.error("In Else [Respo is null from ArcGIS][DriveTime] : "+driveTime);
					response = ServicesUtil.convertFromZoneToZone(null, currentTimeInString, "", "",
							MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE);
					responseDto.setStartTime(response);
					responseDto.setDriveTime(-1.00);
				}
	
			}
			
			else{
			List<Object[]> responseList = (List<Object[]>) q.list();
			Double driveTime = 0.00;
			if (!ServicesUtil.isEmpty(responseList)) {
				responseDto = new StartTimeResponseDto();
				logger.error("in  of responseList" + responseList.size());
				String prevLoc = "";
				for (Object[] obj : responseList) {
					Double time = 0.0;
					if (obj[2].getClass().getName().equals("java.lang.Double")) {
						time = (Double) obj[2];
					} else if (obj[2].getClass().getName().equals("java.math.BigDecimal")) {
						time = ((BigDecimal) obj[2]).doubleValue();
					} else if (obj[2].getClass().getName().equals("java.math.BigInteger")) {
						time = ((BigInteger) obj[2]).doubleValue();
					} else {
						System.err.println("obj[2].getClass().getName()" + obj[2].getClass().getName());
					}

					prevLoc = (String) obj[0];

					if (!locationCode.equals(prevLoc)) {
						logger.error("[locationCode] :"+locationCode + " [prevLoc] "+prevLoc);
						ArcGISResponseDto respo = geoDao.getRoadDistance(prevLoc, locationCode, userId);
						if (!ServicesUtil.isEmpty(respo)) {
							if(!ServicesUtil.isEmpty(driveTimeFromOBX))
								driveTime = driveTimeFromOBX;
							else if (!ServicesUtil.isEmpty(respo.getTotalDriveTime()))
								driveTime = respo.getTotalDriveTime();
							else
								driveTime = 1.00;
							if (((driveTime == -1) || (((driveTime * 60) + resolveTimeInSecs) <= time)) || (time < 0)) {
								response = ServicesUtil.convertFromZoneToZone(null, obj[1], "", "",
										MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE);
								responseDto.setStartTime(response);
								responseDto.setDriveTime(driveTime);
								break;
							} else{
								logger.error("Drive time is -1 or time is less than 0 [DriveTime] : "+driveTime);
							}
						} else {
							logger.error("In Else [Respo is null from ArcGIS][DriveTime] : "+driveTime);
							response = ServicesUtil.convertFromZoneToZone(null, obj[1], "", "",
									MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE);
							responseDto.setStartTime(response);
							responseDto.setDriveTime(-1.00);
							break;
						}
					} else {
						response = ServicesUtil.convertFromZoneToZone(null, obj[1], "", "",
								MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE);
						logger.error("[Response][Else][Both Location matches] :"+response);
						responseDto.setStartTime(response);
						responseDto.setDriveTime(0.00);
						break;

					}
				}
			} else {
				responseDto = new StartTimeResponseDto();
				
				ArcGISResponseDto respo = geoDao.getRoadDistance("", locationCode, userId);
				response = ServicesUtil.convertFromZoneToZone(new Date(), "", "", "", MurphyConstant.DATE_DB_FORMATE,
						MurphyConstant.DATE_DB_FORMATE);

				if (!ServicesUtil.isEmpty(respo)) {
					logger.error("[ResponseList Null][respo] "+respo);
					if(!ServicesUtil.isEmpty(driveTimeFromOBX)){
						driveTime = driveTimeFromOBX;
						logger.error("In Geo Tab line 916 : "+driveTimeFromOBX);
					}
					else					
						driveTime = respo.getTotalDriveTime();
					
					responseDto.setStartTime(response);
					responseDto.setDriveTime(driveTime);
				} else{
					logger.error("Respo in line 881 is null/empty");
					driveTime = 1.00;
					responseDto.setStartTime(response);
					responseDto.setDriveTime(driveTime);
				}
			}
			}
			if (ServicesUtil.isEmpty(responseDto.getStartTime())) {
				responseDto.setStartTime(new Date());
			}
			if (ServicesUtil.isEmpty(responseDto.getDriveTime())) {
				responseDto.setDriveTime(0.00);
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][getNewStartOfUser][Exception] " + e);
			logger.error("[Murphy][TaskOwnersDao][getNewStartOfUser][Message Exception] " + e.getMessage());
		}
		logger.error("[Murphy][TaskOwnersDao][getNewStartOfUser][responseDto]" + responseDto);
		return responseDto;
		
	}
	/* Scheduling based on Priority End */

	@SuppressWarnings("unchecked")
	public String getGainTimeOfUser(String userId) { // 1.if no task present for
														// the user
		// 2.only one task which is inprogress - handled
		Date estimetedEndTime;
		long diff = 0;
		Date actualEndTime;
		long diffInMilliSeconds = 0;
		long diffMinutes = 0;
		long gainTimeInHours = 0;
		Date endTimeOfDay;
		String status;
		long lastTaskDiff = 0;
		Date currentTimeOfDay = null;
		String timeInString = "0.0";
		try {
			Date currentDate = new Date();
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			c.setTime(currentDate);
			int currentHour = c.get(Calendar.HOUR_OF_DAY);
			if (currentHour >= 7)
				c.add(Calendar.DATE, 1);
			Date currentTime = c.getTime();
			c.set(Calendar.HOUR_OF_DAY, 17);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Date endOfDay = c.getTime();
			c.set(Calendar.HOUR_OF_DAY, 7);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Date startOfDay = c.getTime();
			if (currentTime.before(endOfDay) && currentTime.after(startOfDay)) {
				String noTaskQuery = "SELECT COUNT(*) FROM TM_TASK_EVNTS TE JOIN TM_TASK_OWNER O ON  O.TASK_ID=TE.TASK_ID WHERE O.START_TIME>(SELECT CURRENT_DATE FROM DUMMY) AND O.END_TIME<(SELECT ADD_SECONDS((SELECT ADD_DAYS(CURRENT_DATE,1)FROM DUMMY),-1) FROM DUMMY) AND  O.TASK_OWNER='"
						+ userId + "'";
				Query taskCount = this.getSession().createSQLQuery(noTaskQuery);
				List<BigInteger> userTaskCount = (List<BigInteger>) taskCount.list();
				if (userTaskCount.get(0).equals(BigInteger.ZERO)) {
					String currentFreeTimeQuery = "SELECT ADD_SECONDS(ADD_DAYS(CURRENT_DATE,1),-1),CURRENT_TIMESTAMP FROM DUMMY;";
					List<Object[]> responseList = (List<Object[]>) this.getSession()
							.createSQLQuery(currentFreeTimeQuery).list();
					if (!ServicesUtil.isEmpty(responseList)) {
						Object[] obj = responseList.get(0);
						Date DayEndTime = (Date) obj[0];
						currentTimeOfDay = (Date) obj[1];
						diffInMilliSeconds = DayEndTime.getTime() - currentTimeOfDay.getTime();
					}
					diffMinutes = diffInMilliSeconds / (60 * 1000) % 60;
					gainTimeInHours = diffInMilliSeconds / (60 * 60 * 1000);
				} else {
					String query = "SELECT TO.END_TIME,TE.COMPLETED_AT FROM TM_TASK_OWNER TO JOIN TM_TASK_EVNTS TE ON TE.TASK_ID=TO.TASK_ID "
							+ "WHERE TE.STATUS IN ('" + MurphyConstant.COMPLETE + "'"
							// + ",'"+MurphyConstant.RESOLVE+"'"
							+ ")"
							// + "AND TE.PARENT_ORIGIN='"
							// + MurphyConstant.OBX + "'"
							+ " AND TE.COMPLETED_AT IS NOT NULL" + " AND TO.TASK_OWNER_EMAIL='" + userId + "'"
							+ " AND TO.START_TIME>(SELECT CURRENT_DATE FROM DUMMY)";
					// String query = "SELECT TO.END_TIME AS END_TIME, CASE WHEN
					// TE.STATUS='COMPLETED' THEN TE.COMPLETED_AT "
					// + " WHEN TE.STATUS='RESOLVED' THEN AU.CREATED_AT " + "
					// END AS COMPLETED_TIME "
					// + "FROM TM_TASK_OWNER TO " + " JOIN TM_TASK_EVNTS TE ON
					// TE.TASK_ID=TO.TASK_ID "
					// + " JOIN TM_AUDIT_TRAIL AU ON TE.TASK_ID=AU.TASK_ID AND
					// AU.ACTION='"
					// + MurphyConstant.RESOLVE + "'" + "WHERE
					// TO.TASK_OWNER_EMAIL='" + userId + "'" + " AND TE.STATUS
					// IN ('"
					// + MurphyConstant.COMPLETE + "','" +
					// MurphyConstant.RESOLVE + "')"
					// + " AND TO.START_TIME>(SELECT CURRENT_DATE FROM DUMMY)";
					// logger.error("tasks gain time query"+query);
					Query q = this.getSession().createSQLQuery(query);
					List<Object[]> responseList = (List<Object[]>) q.list();
					if (!ServicesUtil.isEmpty(responseList)) {
						for (Object[] obj : responseList) {
							estimetedEndTime = (Date) obj[0];
							actualEndTime = (Date) obj[1];
							// if(actualEndTime.before(estimetedEndTime)){
							// diff=estimetedEndTime.getTime()-actualEndTime.getTime();
							// }
							// else
							diff = estimetedEndTime.getTime() - actualEndTime.getTime();
							diffInMilliSeconds = diffInMilliSeconds + diff;
						}
					}
					String endGainTimeQuery = "SELECT END_TIME,COMPLETED_AT,DAYENDTIME,STATUS,(SELECT CURRENT_TIMESTAMP FROM DUMMY) FROM"
							+ "(SELECT ROW_NUMBER() OVER (PARTITION BY TASK_OWNER_EMAIL ORDER BY END_TIME DESC) AS RN"
							+ ",END_TIME,COMPLETED_AT,(SELECT ADD_SECONDS(actualTime,-1) FROM DUMMY) as DAYENDTIME,STATUS FROM(SELECT *,(SELECT ADD_DAYS(CURRENT_DATE,1) FROM DUMMY) AS actualTime "
							+ "FROM TM_TASK_OWNER TO JOIN TM_TASK_EVNTS TE ON TE.TASK_ID=TO.TASK_ID  WHERE TASK_OWNER_EMAIL='"
							+ userId + "')"
							+ "WHERE END_TIME<actualTime AND START_TIME>(SELECT CURRENT_DATE FROM DUMMY)) WHERE RN=1";
					// String endGainTimeQuery="SELECT
					// END_TIME,RESOLVED_TIME,DAYENDTIME,STATUS,(SELECT
					// CURRENT_TIMESTAMP FROM DUMMY),START_TIME FROM "
					// +"(SELECT ROW_NUMBER() OVER (PARTITION BY
					// TASK_OWNER_EMAIL ORDER BY END_TIME DESC) AS RN
					// ,START_TIME "
					// +",END_TIME,COMPLETED_AT,(SELECT
					// ADD_SECONDS(actualTime,-1) FROM DUMMY) as
					// DAYENDTIME,STATUS,RESOLVED_TIME "
					// +" FROM(SELECT *,(SELECT ADD_DAYS(CURRENT_DATE,1) FROM
					// DUMMY) AS actualTime,CASE WHEN
					// TE.STATUS='"+MurphyConstant.RESOLVE+"' THEN AU.CREATED_AT
					// ELSE TE.COMPLETED_AT END AS RESOLVED_TIME "
					// +"FROM TM_TASK_OWNER TO "+"JOIN TM_TASK_EVNTS TE ON
					// TE.TASK_ID=TO.TASK_ID "
					// +" JOIN TM_AUDIT_TRAIL AU ON TE.TASK_ID=AU.TASK_ID AND
					// AU.ACTION='"+MurphyConstant.RESOLVE+"' "
					// +" WHERE TASK_OWNER_EMAIL='"+userId+"')WHERE
					// END_TIME<actualTime AND START_TIME>(SELECT CURRENT_DATE
					// FROM DUMMY)) WHERE RN=1";
					// logger.error(" endGainTimeQuery "+endGainTimeQuery);
					Query gainTimeQuery = this.getSession().createSQLQuery(endGainTimeQuery);
					List<Object[]> totalGainResponse = (List<Object[]>) gainTimeQuery.list();
					if (!ServicesUtil.isEmpty(totalGainResponse)) {
						for (Object[] obj : totalGainResponse) {
							estimetedEndTime = (Date) obj[0];
							status = (String) obj[3];
							endTimeOfDay = (Date) obj[2];
							if (MurphyConstant.COMPLETE.equals(status) || MurphyConstant.RESOLVE.equals(status)) {
								actualEndTime = ServicesUtil.isEmpty(obj[1]) ? null : (Date) obj[1];
								currentTimeOfDay = (Date) obj[4];
								Date estimatedStartTime = (Date) obj[5];
								if (actualEndTime.after(currentTimeOfDay)) {
									lastTaskDiff = endTimeOfDay.getTime() - actualEndTime.getTime();
									diffInMilliSeconds = diffInMilliSeconds + lastTaskDiff;
								} else {
									lastTaskDiff = endTimeOfDay.getTime() - currentTimeOfDay.getTime();
									diffInMilliSeconds = diffInMilliSeconds + lastTaskDiff;
								}
							}
							// else {
							//// if(MurphyConstant.INPROGRESS.equals(status)){
							// lastTaskDiff =endTimeOfDay.getTime()-
							// estimetedEndTime.getTime() ;
							// lastTaskDiff+=diffInMilliSeconds;
							// }
						}
					}
					// logger.error("[TaskOwnerDao][getGainTimeOfUser]
					// diffInMilliSeconds at end of the day"+ diffInMilliSeconds
					// +" for user"+userId);
					diffMinutes = diffInMilliSeconds / (60 * 1000) % 60;
					diffMinutes = Math.abs(diffMinutes);
					gainTimeInHours = diffInMilliSeconds / (60 * 60 * 1000);
				}
				if (gainTimeInHours > 10) {
					diffInMilliSeconds = endOfDay.getTime() - currentTime.getTime();
					diffMinutes = diffInMilliSeconds / (60 * 1000) % 60;
					diffMinutes = Math.abs(diffMinutes);
					gainTimeInHours = diffInMilliSeconds / (60 * 60 * 1000);
					if (gainTimeInHours > 10) {
						gainTimeInHours = 10;
						diffMinutes = 0;
					}

				} else if (gainTimeInHours < -10) {
					diffInMilliSeconds = currentTime.getTime() - endOfDay.getTime();
					diffMinutes = diffInMilliSeconds / (60 * 1000) % 60;
					diffMinutes = Math.abs(diffMinutes);
					gainTimeInHours = diffInMilliSeconds / (60 * 60 * 1000);
					if (gainTimeInHours < -10) {
						gainTimeInHours = -10;
						diffMinutes = 0;
					}
				}
				timeInString = String.valueOf(gainTimeInHours) + "." + String.valueOf(diffMinutes);
			} else {
				timeInString = "0.0";
			}
		} catch (Exception e) {
			logger.error("[TaskOwnersDao][getGainTimeOfUser][Exception]" + e.getMessage());
		}
		return timeInString;
	}

	// public String updateOwnerGroup(List<CustomAttrTemplateDto> dto,String
	// taskId){
	// String ownerGroup=null,response=MurphyConstant.FAILURE;
	// for(int i=0;i<dto.size();i++){
	// CustomAttrTemplateDto ownerGroupCustomAttr=dto.get(i);
	// if(ownerGroupCustomAttr.getClItemId().equals("INQ02")){
	// ownerGroup=ownerGroupCustomAttr.getLabelValue();
	// }
	// response = attrDao.setAttrValueTo(taskId, ownerGroup, "INQ02");
	// }
	// return response;
	// }

	public String updateOBXOwners(List<TaskOwnersDto> owners, String taskId, String locationCode, String origin) {
		String respone = MurphyConstant.SUCCESS;
		Session session = null;
		Transaction tx = null;
		List<String> existingOwners = getExistingOwners(taskId);

		if (!ServicesUtil.isEmpty(existingOwners)) {
			deleteNonExistingTasks(existingOwners, taskId);
		}

		try {
			if (!ServicesUtil.isEmpty(owners)) {
				for (TaskOwnersDto dto : owners) {
					TaskOwnersDo entity = importDto(dto);
					if (!ServicesUtil.isEmpty(existingOwners)) {
						session = sessionFactory.openSession();
						tx = session.beginTransaction();
						session.persist(entity);
						tx.commit();
						/*
						 * if
						 * (!this.createOwner(dto).equals(MurphyConstant.SUCCESS
						 * )) { respone = MurphyConstant.FAILURE; }
						 */
					}

				}

			}

		} catch (Exception e) {
			tx.rollback();
			logger.error("[Murphy][TaskOwnersDao][updateOBXOwners][error]" + e.getMessage());
		} finally {
			try {
				if (session != null) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[fetchingMissingDataInfoFromDB] Exception While Closing Session " + e.getMessage());
			}
		}
		return respone;
	}

	public static void main(String[] args) {
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
		c.setTime(currentDate);
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		if (currentHour >= 7)
			c.add(Calendar.DATE, 1);
		Date currentTime = c.getTime();
		c.set(Calendar.HOUR_OF_DAY, 16);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date endOfDay = c.getTime();
		c.set(Calendar.HOUR_OF_DAY, 6);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date startOfDay = c.getTime();
		System.out.println(" currentTime " + currentTime + " endOfDay " + endOfDay + " startOfDay " + startOfDay
				+ " currentHour " + currentHour);
	}

	public String updateStatus(String status, String taskId) {
		String response = MurphyConstant.FAILURE;
		try {
			String query = " UPDATE TM_TASK_EVNTS st SET Status ='" + status + "' where st.task_id = '" + taskId + "'";
			logger.error("[Murphy][TaskOwnersDao][updatePriorityWhenResolved][query]  " + query);
			Query q = this.getSession().createSQLQuery(query);
			Integer result = (Integer) q.executeUpdate();
			logger.error(" result " + result);
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][updateStatus][error]  " + e.getMessage());
		}
		return response;

	}
	
	@SuppressWarnings("unchecked")
	public String updateOwnersStartTime(String taskId,String origin){
	String response = MurphyConstant.SUCCESS;
	Double resolveTime = 0.00;
	int priority = 0;
	String owner = null,ownerEmail = null;
	String newStartTime = null,newEndTime = null;
	Date new_start_date = null;
	
	try {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// fetch Task_owner for that task_id which needs follow up
		String QueryOwner = "SELECT TASK_OWNER_EMAIL,TASK_OWNER,EST_RESOLVE_TIME from TM_TASK_OWNER where TASK_ID = '" +taskId + "'" ;
		
		logger.error("[Murphy][TaskOwnersDao][updateOwnersStartTime][QueryOwner]  " + QueryOwner);
		Query query1 = this.getSession().createSQLQuery(QueryOwner);
		List<Object[]> responseOwner = (List<Object[]>) query1.list();
		if (!ServicesUtil.isEmpty(responseOwner)) {
			for (Object[] obj : responseOwner) {
				ownerEmail = ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
				owner = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
				resolveTime = ServicesUtil.isEmpty(obj[2]) ? 0 : (Double) obj[2];
			}
		}
		// Fetch owner's last task end_time and add 1 sec to get newStartTime
		String endDateQuery = "SELECT ADD_SECONDS (END_TIME,1),PRIORITY from TM_TASK_OWNER where TASK_OWNER_EMAIL = '"+ownerEmail + "'" 
                           +" order by END_TIME DESC LIMIT 1";
		
		logger.error("[Murphy][TaskOwnersDao][updateOwnersStartTime][endDateQuery]  " + endDateQuery);
		Query query2 = this.getSession().createSQLQuery(endDateQuery);
		List<Object[]> responseDate = (List<Object[]>) query2.list();
		if (!ServicesUtil.isEmpty(responseDate)) {
			for (Object[] obj : responseDate) {
				new_start_date = ServicesUtil.isEmpty(obj[0]) ? null : (Date) obj[0];
				priority = ServicesUtil.isEmpty(obj[1]) ? 0 : (int) obj[1];
			}
		}
		priority = priority + 1 ;
		// Update StartTime and EndTime of Follow-Up task
		newStartTime = sdf.format(new_start_date);
		Date start_time = sdf.parse(newStartTime);
		Date end_time = ServicesUtil.getDateWithInterval(start_time, resolveTime.intValue(), MurphyConstant.MINUTES);
		newEndTime = sdf.format(end_time);	
		
		String UpdateTime = "UPDATE TM_TASK_OWNER set PRIORITY = " +priority + ", START_TIME = TO_TIMESTAMP('"+newStartTime+"','yyyy-MM-dd HH24:mi:ss'),"
				          + " END_TIME = TO_TIMESTAMP('"+ newEndTime +"','yyyy-MM-dd HH24:mi:ss') where TASK_ID = '" + taskId + "'";
		
		logger.error("[Murphy][TaskOwnersDao][updateOwnersStartTime][UpdateTime]  " + UpdateTime);
		Query query3 = this.getSession().createSQLQuery(UpdateTime);
		int result = query3.executeUpdate();
		
		if (origin.equals(MurphyConstant.INQUIRY)) {
			response = attrDao.setAttrValueTo(taskId, owner, "INQ03");
		} else {
			response = attrDao.setAttrValueTo(taskId, owner, "1234','NDO3");
		}

	} catch (Exception e) {
		logger.error("[Murphy][TaskOwnersDao][updateOwnersStartTime][error]  " + e.getMessage());
		response = MurphyConstant.FAILURE;
	}
	return response;
 }

	// Update Revision of process_id if task comes for follow-up
	@SuppressWarnings("unchecked")
	public void updateRevisionOfProcess(String taskID){
		int revision = 0;
		String process_id = null;
		try {
			String getRevisionQuery = "SELECT pe.revision,pe.process_id from TM_PROC_EVNTS pe left outer join TM_TASK_EVNTS te on pe.PROCESS_ID = te.PROCESS_ID" 
		                              + " where te.task_id = '" + taskID + "'";
			logger.error("[Murphy][TaskOwnersDao][updateRevisionOfProcess][getRevisionQuery]  " + getRevisionQuery);
			Query revision_query = this.getSession().createSQLQuery(getRevisionQuery);
			List<Object[]> response = (List<Object[]>) revision_query.list();
			if (!ServicesUtil.isEmpty(response)) {
				for (Object[] obj : response) {
					revision = ServicesUtil.isEmpty(obj[0]) ? 0 : (int) obj[0];
					process_id = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
				}
			}
			revision = revision + 1;
			
			String queryUpdate = " UPDATE TM_PROC_EVNTS SET revision ='" + revision + "' where process_id = '" + process_id + "'";
			logger.error("[Murphy][TaskOwnersDao][updateRevisionOfProcess][queryUpdate]  " + queryUpdate);
			Query q = this.getSession().createSQLQuery(queryUpdate);
			int result = q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][TaskOwnersDao][updateRevisionOfProcess][error]  " + e.getMessage());
		}
	} 

}