package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.CustomLocationHistoryDto;
import com.murphy.taskmgmt.dto.LocationHistoryRolledUpDto;
import com.murphy.taskmgmt.dto.TaskListDto;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("CustomLocationHistoryDao")
@Transactional

public class CustomLocationHistoryDao {

	public CustomLocationHistoryDao() {
	}

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private HierarchyDao hierarchyDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][BaseDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}

	private static final Logger logger = LoggerFactory.getLogger(CustomLocationHistoryDao.class);

	@SuppressWarnings("unchecked")
	public CustomLocationHistoryDto getTaskHistory(String locationCode, String monthTime, int page, int page_size) {

		CustomLocationHistoryDto cstDto = new CustomLocationHistoryDto();
		List<TaskListDto> responseDto = null;
		List<LocationHistoryRolledUpDto> statusCountList = null;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		if (monthTime.equalsIgnoreCase("one"))
			cal.add(Calendar.DATE, -30);
		else if (monthTime.equalsIgnoreCase("three"))
			cal.add(Calendar.DATE, -90);

		Query query = null;
		Integer countResult = 0;

		try {

			if (!ServicesUtil.isEmpty(locationCode)) {
				locationCode = ServicesUtil.getStringForInQuery(locationCode);
			}
			// Adding rootCauseDescription
			String rootCauseDescription = " select rt.description from tm_rootcause_insts rt where rt.task_Id = te.task_id "
					+ " and te.origin <> '"+ MurphyConstant.P_INVESTIGATION+"' and rt.created_At = (Select max(rtx.created_At)  from tm_rootcause_insts rtx where rtx.task_id = te.task_id )";
			
			String rootCause = " select rt.root_cause from tm_rootcause_insts rt where rt.task_Id = te.task_id "
					+ " and te.origin <> '"+ MurphyConstant.P_INVESTIGATION+"' and rt.created_At = (Select max(rtx.created_At)  from tm_rootcause_insts rtx where rtx.task_id = te.task_id)";

			String inProgressTimeQuery = " select max(at.created_at) from tm_audit_trail at where te.task_id = at.task_id "
					+ "group by at.task_id,at.action having at.action = 'IN PROGRESS'";
			String resolvedTimeQuery = " select max(at.created_at) from tm_audit_trail at "
					+ "where te.task_id = at.task_id group by at.task_id,at.action having at.action = 'RESOLVED'";

			String queryString = "Select te.TASK_ID AS TASK_ID,te.DESCRIPTION as TASK_DESCRIPTION,SUBSTR_AFTER(TSK_SUBJECT, '-') AS SUBJECT,"
					+ " te.STATUS AS STATUS ,te.PROCESS_ID AS PROCESS_ID ,pe.STARTED_BY_DISP AS STARTED_BY_DISP,"
					+ " pe.REQUEST_ID AS REQUEST_ID, te.CREATED_AT AS CT, "
					+ " ( SELECT max(i.INS_VALUE) FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID in ('NDO3','1234','INQ03') AND te.TASK_ID = i.TASK_ID  ) AS ASSIGNED_USER, "
					+ " te.parent_origin AS PARENT_ORIGIN ," + " (" + rootCause + " ) AS rootCause," + " (" + rootCauseDescription + " ) AS rootCauseDescription,"
					+ " pe.loc_code AS LOCATION_CODE, te.origin AS ORIGIN, te.task_type as TASK_TYPE, te.PREV_TASK as PREV_TASK,"
					+ " pe.USER_GROUP as USER_GROUP, te.updated_at AS UPDATED_AT, te.updated_by AS UPDATED_BY,"
					+ " te.USER_UPDATED_AT AS USER_UPDATED_AT, pe.EXTRA_ROLE AS EXTRA_ROLE," + " ("
					+ inProgressTimeQuery + " ) AS inProgressTime," + " (" + resolvedTimeQuery
					+ " ) AS resolvedTime, te.REFRENCE_NUM as referenceNum "
					+ " from TM_PROC_EVNTS pe left outer join  TM_TASK_EVNTS te on pe.PROCESS_ID = te.PROCESS_ID "
					+ " left outer join TM_TASK_OWNER tw on tw.TASK_ID = te.TASK_ID join tm_audit_trail at on te.task_id = at.task_id"
					+ "	where pe.LOC_CODE in (" + locationCode + ")" + " and" + " te.parent_origin IN ('"
					+ MurphyConstant.P_ALARM + "' ,'" + MurphyConstant.P_CUSTOM + "' ,'" + MurphyConstant.P_FRAC
					+ "' ,'" + MurphyConstant.P_INQUIRY + "' ,'" + MurphyConstant.P_INVESTIGATION + "' ,'" + MurphyConstant.P_ITA_DOP + "','"
					+ MurphyConstant.P_PIGGING + "' ,'" + MurphyConstant.P_VARIANCE + "','" + MurphyConstant.P_ITA + "','" + MurphyConstant.P_OBX
					+ "')" + " and te.status not in ('"+ MurphyConstant.DRAFT + "','" + MurphyConstant.NEW_TASK + "','" 
					+ MurphyConstant.REJECTED +"','" + MurphyConstant.CANCELLED +"')"
					+ "and to_date(te.created_at) >= '" + sdf.format(cal.getTime()) + "' ";

			String finalQuery = "select distinct TASK_ID, TASK_DESCRIPTION, SUBJECT,STATUS,PROCESS_ID,STARTED_BY_DISP,REQUEST_ID,"
					+ "CT,ASSIGNED_USER,PARENT_ORIGIN,rootCause,rootCauseDescription,LOCATION_CODE,ORIGIN,TASK_TYPE,PREV_TASK,USER_GROUP,"
					+ "UPDATED_AT, UPDATED_BY,USER_UPDATED_AT,EXTRA_ROLE, "
					+ "inProgressTime,resolvedTime, referenceNum from (";
			finalQuery = finalQuery + queryString + ") order by ct desc";

			String countQueryString = " SELECT COUNT(*) AS COUNT FROM " + "(" + finalQuery + ")";
			query = this.getSession().createSQLQuery(countQueryString);
			countResult = ((BigInteger) query.uniqueResult()).intValue();
			logger.error("[Murphy][CustomLocationHistoryDao][getTaskHistory][countResult]" + countResult);

			if (page > 0) {
				int first = (page - 1) * page_size;
				int last = page_size;
				finalQuery += " LIMIT " + last + " OFFSET " + first + "";
			}

			logger.error("[Murphy][CustomLocationHistoryDao][getTaskHistory][Query]" + finalQuery);
			Query q = this.getSession().createSQLQuery(finalQuery);

			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				TaskListDto dto = null;
				responseDto = new ArrayList<TaskListDto>();
				for (Object[] obj : response) {
					dto = new TaskListDto();
					
					dto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setDescription(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					dto.setClassification(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					String str[] = dto.getClassification().split("/",2);
					if (str != null) {
						dto.setTaskClassification(str[0]);
						if(str.length>1) {
							dto.setSubClassification(str[1]);
						} else {
							dto.setSubClassification(null);
						}
					}
					dto.setStatus(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setProcessId(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setCreatedBy(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					dto.setRequestId(ServicesUtil.isEmpty(obj[6]) ? null : ((BigInteger) obj[6]).longValue());
					dto.setCreatedAt(ServicesUtil.isEmpty(obj[7]) ? null
							: ServicesUtil.convertFromZoneToZone(null, obj[7], MurphyConstant.UTC_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setCreatedAtInString(ServicesUtil.isEmpty(obj[7]) ? null
							: ServicesUtil.convertFromZoneToZoneString(null, obj[7], MurphyConstant.UTC_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setTaskOwner(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
					dto.setParentOrigin(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
					dto.setRootCause(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
					dto.setRootCauseDescription(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
					if (dto.getStatus().equalsIgnoreCase(MurphyConstant.COMPLETE)
							|| dto.getStatus().equalsIgnoreCase(MurphyConstant.RESOLVE)) {
						String inPro = ServicesUtil.isEmpty(obj[21]) ? null
								: ServicesUtil.convertFromZoneToZoneString(null, obj[21], MurphyConstant.UTC_ZONE,
										MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.DATE_DISPLAY_FORMAT);
						if (inPro == null) {
							inPro = ServicesUtil.isEmpty(obj[7]) ? null
									: ServicesUtil.convertFromZoneToZoneString(null, obj[7], MurphyConstant.UTC_ZONE,
											MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
											MurphyConstant.DATE_DISPLAY_FORMAT);
						}
						String res = ServicesUtil.isEmpty(obj[22]) ? null
								: ServicesUtil.convertFromZoneToZoneString(null, obj[22], MurphyConstant.UTC_ZONE,
										MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.DATE_DISPLAY_FORMAT);

						String turnAround = null, turnAroundCal = null, turnAroundDisplay = null;
						if (inPro != null && res != null) {
							turnAroundCal = turnAroundCalculation(inPro, res);
							int start = turnAroundCal.indexOf(" ");
							turnAround = turnAroundCal.substring(0, start);
							if (turnAroundCal.length() > 12)
								turnAroundDisplay = turnAroundCal.substring(start + 1);

							turnAroundDisplay = turnAroundCal;
							dto.setTurnAroundTime(turnAroundDisplay);
							long totalTime = getTotalTime(dto.getTaskId());
							//long varianceTime = MurphyConstant.TOTAL_TIME - Long.parseLong(turnAround);
							long varianceTime = totalTime - Long.parseLong(turnAround);
							dto.setTurnAroundVariance((new Long(varianceTime).toString()) + " Minutes");
						}
					}
					dto.setLocationCode(ServicesUtil.isEmpty(obj[12]) ? null : (String) obj[12]);
					dto.setLocation(
							hierarchyDao.getLocationByLocCode(ServicesUtil.isEmpty(obj[12]) ? null : (String) obj[12]));
					dto.setOrigin(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
					dto.setTaskType(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
					dto.setPrev_task(ServicesUtil.isEmpty(obj[15]) ? null : (String) obj[15]);
					dto.setCreatedOnGroup(ServicesUtil.isEmpty(obj[16]) ? null : (String) obj[16]);
					dto.setUpdated_at(ServicesUtil.isEmpty(obj[17]) ? null
							: ServicesUtil.convertFromZoneToZone(null, obj[17], MurphyConstant.UTC_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setUpdated_by(ServicesUtil.isEmpty(obj[18]) ? null : (String) obj[18]);
					dto.setUser_updated_at(ServicesUtil.isEmpty(obj[19]) ? null
							: ServicesUtil.convertFromZoneToZone(null, obj[19], MurphyConstant.UTC_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setExtra_role(ServicesUtil.isEmpty(obj[20]) ? null : (String) obj[20]);
					dto.setTaskRefNum(ServicesUtil.isEmpty(obj[23]) ? null
							: (((String) obj[23]).substring(((String) obj[23]).length() - 6)));

					responseDto.add(dto);
				}
			}

			statusCountList = StatusCount(locationCode, monthTime);

			cstDto.setTaskList(responseDto);
			cstDto.setStatusCountList(statusCountList);
			cstDto.setPageCount(new BigDecimal(page_size));
			cstDto.setTotalCount(new BigDecimal(countResult));

		} catch (Exception e) {
			logger.error("[Murphy][CustomLocationHistoryDao][getTaskHistory][error]" + e.getMessage());
		}
		return cstDto;
	}

	public String turnAroundCalculation(String inProgTime, String inResolveTime) {
		// 03-Dec-18 10:57:07 AM
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		String time = null;
		Date dinProg;
		Date dResolved;
		try {
			dinProg = format.parse(inProgTime);
			dResolved = format.parse(inResolveTime);
			long diffMilli = dResolved.getTime() - dinProg.getTime();
			long diffMins = (diffMilli / 1000) / 60;
			String mins = new Long(diffMins).toString();
			time = mins + " " + "Minutes";

			if (diffMins > 1440L) {
				long diffDays = diffMilli / (24 * 60 * 60 * 1000);
				long diffMinutes = diffMilli / (60 * 1000) % 60;
				time = mins + " " + diffDays + " Day " + diffMinutes + " Minutes";
			}
		} catch (Exception e) {
			logger.error("[Murphy][CustomLocationHistoryDao][turnAroundCalculation][error]" + e.getMessage());
		}
		return time;
	}

	@SuppressWarnings("unchecked")
	public List<LocationHistoryRolledUpDto> StatusCount(String locationCode, String monthTime) {
		List<LocationHistoryRolledUpDto> statusList = new ArrayList<LocationHistoryRolledUpDto>();
		Query query = null;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		if (monthTime.equalsIgnoreCase("one"))
			cal.add(Calendar.DATE, -30);
		else if (monthTime.equalsIgnoreCase("three"))
			cal.add(Calendar.DATE, -90);

		try {
			// Query to fetch count of all the status
			String queryStringStatus = "select te.status,count(*) from TM_TASK_EVNTS te inner join TM_PROC_EVNTS pe"
					+ " on pe.PROCESS_ID = te.PROCESS_ID where pe.LOC_CODE in (" + locationCode + ")" + " and "
					+ " te.status not in ('"+ MurphyConstant.DRAFT + "','" + MurphyConstant.NEW_TASK +  "','" 
					+ MurphyConstant.REJECTED + "','" + MurphyConstant.CANCELLED + "')"
					+ " and to_date(te.created_at) >= '" + sdf.format(cal.getTime()) + "' group by te.status ";

			logger.error("[Murphy][CustomLocationHistoryDao][StatusCount][queryStringStatus]" + queryStringStatus);
			query = this.getSession().createSQLQuery(queryStringStatus);

			List<Object[]> response = (List<Object[]>) query.list();
			if (!ServicesUtil.isEmpty(response)) {
				LocationHistoryRolledUpDto dto = null;
				for (Object[] obj : response) {
					dto = new LocationHistoryRolledUpDto();
					dto.setStatus(ServicesUtil.isEmpty(obj[0]) ? null : ((String) obj[0]).toUpperCase());
					dto.setCount(ServicesUtil.isEmpty(obj[1]) ? null : new BigDecimal((BigInteger) obj[1]));
					statusList.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][CustomLocationHistoryDao][StatusCount][error]" + e.getMessage());
		}

		try {
			// Query to fetch count of task type (Origin)
			String queryStringType = "select te.origin,count(*) from TM_TASK_EVNTS te inner join TM_PROC_EVNTS pe"
					+ " on pe.PROCESS_ID = te.PROCESS_ID where pe.LOC_CODE in (" + locationCode + ")" + " and "
					+ " te.status not in ('"+ MurphyConstant.DRAFT + "','" + MurphyConstant.NEW_TASK + "','" 
					+ MurphyConstant.REJECTED + "','" + MurphyConstant.CANCELLED + "')"
					+ " and to_date(te.created_at) >= '" + sdf.format(cal.getTime()) + "' group by te.origin ";

			 logger.error("[Murphy][CustomLocationHistoryDao][StatusCountType][queryStringType]" + queryStringType);
			query = this.getSession().createSQLQuery(queryStringType);

			List<Object[]> response = (List<Object[]>) query.list();
			if (!ServicesUtil.isEmpty(response)) {
				LocationHistoryRolledUpDto dto = null;
				for (Object[] obj : response) {
					dto = new LocationHistoryRolledUpDto();
					dto.setStatus(ServicesUtil.isEmpty(obj[0]) ? null : ((String) obj[0]).toUpperCase());
					dto.setCount(ServicesUtil.isEmpty(obj[1]) ? null : new BigDecimal((BigInteger) obj[1]));
					statusList.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][CustomLocationHistoryDao][StatusCountType][error]" + e.getMessage());
		}
		return statusList;
	}
	
	public long getTotalTime(String taskId){
		long totalTime = 0;
		try{
		String queryString = "select distinct EST_RESOLVE_TIME from tm_task_owner where task_id = '"+ taskId +"'";
		logger.error("[Murphy][CustomLocationHistoryDao][getTotalTime][queryString]" + queryString);
		Object obj = this.getSession().createSQLQuery(queryString).uniqueResult();
		
		if(!ServicesUtil.isEmpty(obj))
			totalTime = ((Double)obj).longValue();
		logger.error("[Murphy][CustomLocationHistoryDao][getTotalTime][totalTime]" + totalTime);
		}
		catch (Exception e) {
			logger.error("[Murphy][CustomLocationHistoryDao][getTotalTime][error]" + e.getMessage());
		}
		
		return totalTime;
	}

}
