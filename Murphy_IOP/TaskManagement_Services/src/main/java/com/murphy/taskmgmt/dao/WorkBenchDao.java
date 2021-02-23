package com.murphy.taskmgmt.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.AuditDto;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.WorkBenchDto;
import com.murphy.taskmgmt.dto.WrokBenchAudiLogDto;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

import edu.emory.mathcs.backport.java.util.Arrays;

@Repository("workBenchDao")
@Transactional
public class WorkBenchDao {

	private static final Logger logger = LoggerFactory.getLogger(WorkBenchDao.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditDao auditDao;

	@Autowired
	HierarchyDao hierarchyDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][WorkBenchDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<WorkBenchDto> getTaskList(String sortingOrder, String sortObject, String groupObject,
			String technicalRole, String locationCode, String locationType, String status, String isObx) {
		List<WorkBenchDto> taskList = new ArrayList<WorkBenchDto>();
		WorkBenchDto workBenchDto = null;
		String orderBy = "";

		try {
			if (!ServicesUtil.isEmpty(locationCode)) {
				Session session = this.getSession();
				String technicalRoles = ServicesUtil.getStringForInQuery(technicalRole);
				String locCodeArray[] = locationCode.split(",");
				List<String> locCodeList = new ArrayList<>();
				String fieldOfTechRolesQuery = "select field from TM_ROLE_MAPPING where technicalrole in ("
						+ technicalRoles.trim() + ")";
				List<String[]> fieldObjList = session.createSQLQuery(fieldOfTechRolesQuery).list();
				for (String locCode : locCodeArray) {
					if (fieldObjList.contains(locCode.trim().substring(0, 15))) {
						locCodeList.add(locCode.trim());
					}

				}
				// List<String> locCodeList = Arrays.asList();
				// String locationCodes =
				// ServicesUtil.getStringForInQuery(locationCode);
				String locationCodes = ServicesUtil
						.getStringFromList(hierarchyDao.getHierarchyLocCodes(locationType, locCodeList));

				if (!ServicesUtil.isEmpty(sortObject)) {
					if (sortObject.equalsIgnoreCase("date")) {
						orderBy = "te.created_at";
					} else if (sortObject.equalsIgnoreCase("tier")) {
						orderBy = "wt.tier";
					}
				}
				if (!ServicesUtil.isEmpty(groupObject)) {
					if (groupObject.equalsIgnoreCase("source")) {
						// todo
					} else if (groupObject.equalsIgnoreCase("task_type")) {
						orderBy = "te.origin";
					} else if (groupObject.equalsIgnoreCase("location")) {
						orderBy = "pe.loc_code";
					}

				}

				// Original code Commented
				/*String query = "select te.process_id, pe.loc_code , te.task_id , te.origin , te.tsk_subject , te.created_at , pe.started_by , pl.location_type , te.status "
						+ "from tm_task_evnts te inner join tm_proc_evnts pe on te.process_id = pe.process_id  inner join production_location pl "
						+ "on pl.location_code = pe.loc_code where te.parent_origin in ('" + MurphyConstant.P_ITA
						+ "' , '" + MurphyConstant.P_ITA_DOP + "') and te.status = '" + status.toUpperCase()
						+ "' and pe.loc_code in (" + locationCodes + ") order by " + orderBy + " " + sortingOrder;
				if (groupObject.equalsIgnoreCase("none")) {
					query = "select te.process_id, pe.loc_code , te.task_id , te.origin , te.tsk_subject , te.created_at , "
							+ "pe.started_by , pl.location_type , te.status from "
							+ "tm_task_evnts te inner join tm_proc_evnts pe on te.process_id = pe.process_id "
							+ " inner join production_location pl on pl.location_code = pe.loc_code where te.parent_origin in ('"
							+ MurphyConstant.P_ITA + "' , '" + MurphyConstant.P_ITA_DOP + "')  and te.status = '"
							+ status.toUpperCase() + "' and pe.loc_code in (" + locationCodes + ")";
				}*/

				// SOC 
				// Including OBX Returned Task in WorkBench
				
				
				String mainQuery = "select te.process_id, pe.loc_code , te.task_id , te.origin , te.tsk_subject , te.created_at , pe.started_by , pl.location_type , te.status "
						+ "from tm_task_evnts te inner join tm_proc_evnts pe on te.process_id = pe.process_id  inner join production_location pl "
						+ "on pl.location_code = pe.loc_code ";

				String originQuery = "";
				String groupOrderByQuery = "";

				if ((ServicesUtil.isEmpty(isObx) || isObx.equalsIgnoreCase("FALSE")) && !groupObject.equalsIgnoreCase("none")) {
					originQuery = "where te.parent_origin in ('" + MurphyConstant.P_ITA + "' , '"
							+ MurphyConstant.P_ITA_DOP + "') " + "and te.status = '" + status.toUpperCase()
							+ "' and pe.loc_code in (" + locationCodes + ") ";
					groupOrderByQuery = "order by " + orderBy + " " + sortingOrder;

				} else if ((ServicesUtil.isEmpty(isObx) || isObx.equalsIgnoreCase("FALSE")) && groupObject.equalsIgnoreCase("none")) {
					originQuery = "where te.parent_origin in ('" + MurphyConstant.P_ITA + "' , '"
							+ MurphyConstant.P_ITA_DOP + "') " + "and te.status = '" + status.toUpperCase()
							+ "' and pe.loc_code in (" + locationCodes + ") ";
				} else if (!ServicesUtil.isEmpty(isObx) && isObx.equalsIgnoreCase(MurphyConstant.TRUE)) {
					originQuery = "where te.parent_origin in ('" + MurphyConstant.OBX + "') and te.task_type = '"
							+ MurphyConstant.SYSTEM + "' and te.origin = '"+ MurphyConstant.DISPATCH_TASK + "' and te.status = '" + status.toUpperCase()
							+ "' and pe.loc_code in (" + locationCodes + ") ";
				}
				
				String query = mainQuery+ originQuery + groupOrderByQuery;
				
				logger.error("Workbench Final Query : "+query);
				
				// EOC

				List<Object[]> objects = session.createSQLQuery(query).list();
				for (Object[] obj : objects) {
					workBenchDto = new WorkBenchDto();
					workBenchDto.setProcessId((String) obj[0]);
					workBenchDto.setHasDispatch(false);
					workBenchDto.setHasInquiry(false);
					workBenchDto.setHasInvestigation(false);
					String locCode = (String) obj[1];
					String taskTypeCheckQuery = "select te.origin from tm_task_evnts te inner join tm_proc_evnts pe on te.process_id = pe.process_id where pe.loc_code = '"
							+ locCode + "' and te.status != '" + MurphyConstant.COMPLETE + "'";
					List<String> task_types = session.createSQLQuery(taskTypeCheckQuery).list();
					for (String task_type : task_types) {
						if (task_type.equalsIgnoreCase("Dispatch")) {
							workBenchDto.setHasDispatch(true);
						}
						if (task_type.equalsIgnoreCase("Inquiry")) {
							workBenchDto.setHasInquiry(true);
						}
						if (task_type.equalsIgnoreCase("Investigation")) {
							workBenchDto.setHasInvestigation(true);

						}
					}
					workBenchDto.setTaskLocCode(locCode);
					workBenchDto.setTaskId((String) obj[2]);
					workBenchDto.setTaskType((String) obj[3]);
					String taskSubject[] = ((String) obj[4]).split(" - ");
					workBenchDto.setTaskLocationText(taskSubject[0].trim());
					workBenchDto.setTaskSub(taskSubject[1].trim());
					workBenchDto.setCreatedOn((Date) obj[5]);
					String wellTierQuery = "select tier from WELL_TIER where location_code ='" + locCode + "'";
					String wellTier = (String) session.createSQLQuery(wellTierQuery).uniqueResult();
					if (!ServicesUtil.isEmpty(wellTier)) {
						workBenchDto.setTier(wellTier);
					} else {
						workBenchDto.setTier("");
					}

					workBenchDto.setCreatedBy((String) obj[6]);
					workBenchDto.setLocationType((String) obj[7]);
					workBenchDto.setStatus((String) obj[8]);
					taskList.add(workBenchDto);

				}

			}
		} catch (Exception ex) {
			logger.error("[WorkBenchDao][getTaskList]" + ex.getMessage());
		}

		return taskList;
	}

	public void updateTaskStatus(WrokBenchAudiLogDto dto) {

		try {
			Session session = this.getSession();
			String taskEvntUpdateQuery = "";
			if (dto.getAction().equalsIgnoreCase("Rejected")) {
				taskEvntUpdateQuery = "update tm_task_evnts set status = '" + MurphyConstant.REJECTED
						+ "' where task_id = '" + dto.getTaskId() + "'";
			} else if (dto.getAction().equalsIgnoreCase("Accepted")) {
				taskEvntUpdateQuery = "update tm_task_evnts set status = '" + MurphyConstant.NEW_TASK
						+ "' where task_id = '" + dto.getTaskId() + "'";
			}

			session.createSQLQuery(taskEvntUpdateQuery).executeUpdate();
			// String auditLogUpdateQuery= "";
			AuditDto auditDto = new AuditDto();
			auditDto.setAction(dto.getAction());
			auditDto.setActionBy(dto.getActionBy());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
			// dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = dateFormat.parse(dto.getCreatedAt());
			String createdAt = ServicesUtil.convertFromZoneToZoneString(null, dto.getCreatedAt(),
					MurphyConstant.CST_ZONE, MurphyConstant.UTC_ZONE, "yyyy-MM-dd hh:mm:ss aa",
					"yyyy-MM-dd hh:mm:ss aa");

			auditDto.setCreatedAt(dateFormat.parse(createdAt));
			auditDto.setTaskId(dto.getTaskId());
			auditDao.createInstance(auditDto);
			// if (dto.getAction().equalsIgnoreCase("Rejected")) {
			String auditLogId = UUID.randomUUID().toString().replaceAll("-", "");
			String workbenchAuditLogUpdateQuery = "insert into WORKBENCH_AUDIT_LOG values('" + auditLogId + "' , '"
					+ dto.getTaskId() + "' , '" + "" + "' , '" + "" + "' , '" + dto.getTaskType() + "' , '"
					+ dto.getTier() + "' , '" + dto.getLocationCode() + "' , '" + dto.getLocationType() + "' , '"
					+ dto.getSource() + "' , '" + dto.getAction() + "' , '" + dto.getActionBy() + "' , '"
					+ dto.getActionByEmail() + "' , '" + dto.getReason() + "' , '" + createdAt + "' )";
			session.createSQLQuery(workbenchAuditLogUpdateQuery).executeUpdate();
			// }

		} catch (Exception ex) {
			logger.error("[WorkBenchDao][updateTaskStatus]" + ex.getMessage());
		}

	}
	
	public String autoCancelObxTask() {
		String res= MurphyConstant.FAILURE;
		try {

			Date currentDate = new Date();
			Calendar c = Calendar.getInstance();
			//c.setTime(currentDate);
			Date dateCurrent = c.getTime();
			String dateCurrentInString = new SimpleDateFormat("yyyy-MM-dd").format(dateCurrent);
			
			final Calendar calNow = Calendar.getInstance();
			calNow.add(Calendar.DATE, -7);
			//calNow.setTime(currentDate);
			Date dateBefore = calNow.getTime();
			String dateBeforeInString = new SimpleDateFormat("yyyy-MM-dd").format(dateBefore);
			
			
			String queryTaskStatusUpdate = "UPDATE TM_TASK_EVNTS SET UPDATED_BY= '"+ MurphyConstant.SYSTEM + "' , "
					+ "COMPLETED_AT =  TO_TIMESTAMP('"+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
					+ "', 'yyyy-mm-dd hh24:mi:ss') , STATUS='" + MurphyConstant.CANCELLED
					+ "' WHERE TASK_ID IN ( SELECT TE.TASK_ID FROM TM_TASK_EVNTS TE JOIN "
					+ " TM_TASK_OWNER TO ON TE.TASK_ID=TO.TASK_ID"
					+ " JOIN TM_PROC_EVNTS PE ON PE.PROCESS_ID=TE.PROCESS_ID "
					+ " WHERE PARENT_ORIGIN='OBX' AND TASK_TYPE='SYSTEM' AND (CREATED_AT<TO_DATE('" + dateCurrentInString
					+ "','yyyy-MM-dd') AND CREATED_AT>=TO_DATE('" + dateBeforeInString
					+ "','yyyy-MM-dd')) and TE.STATUS in ('" + MurphyConstant.RETURN + "'))";
			
			logger.error("[WorkBenchDao][autoCancelObxTask][Task Query] " + queryTaskStatusUpdate);
			int result = this.getSession().createSQLQuery(queryTaskStatusUpdate).executeUpdate();
			logger.error("[WorkBenchDao][autoCancelObxTask][UpdateCount] " + result);
			
			autoCancelObxProcess();	
			res = MurphyConstant.SUCCESS;

		} catch (Exception e) {
			res= MurphyConstant.FAILURE;
			logger.error("[Murphy][WorkBenchDao][Exception][error]" + e.getMessage());
		}
		return res;
	}
	
	
	public void autoCancelObxProcess(){
		try{
			Date currentDate = new Date();
			Calendar c = Calendar.getInstance();
			//c.setTime(currentDate);
			Date dateCurrent = c.getTime();
			String dateCurrentInString = new SimpleDateFormat("yyyy-MM-dd").format(dateCurrent);
			
			final Calendar calNow = Calendar.getInstance();
			calNow.add(Calendar.DATE, -7);
			//calNow.setTime(currentDate);
			Date dateBefore = calNow.getTime();
			String dateBeforeInString = new SimpleDateFormat("yyyy-MM-dd").format(dateBefore);
			
			String queryStatusUpdate = "UPDATE TM_PROC_EVNTS SET COMPLETED_AT = "
					+ "TO_TIMESTAMP('"+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
					+ "', 'yyyy-mm-dd hh24:mi:ss') , STATUS='" + MurphyConstant.CANCELLED
					+ "' WHERE PROCESS_ID IN ( SELECT TE.PROCESS_ID FROM TM_TASK_EVNTS TE "
					+ " JOIN TM_TASK_OWNER TO ON TE.TASK_ID=TO.TASK_ID"
					+ " JOIN TM_PROC_EVNTS PE ON PE.PROCESS_ID=TE.PROCESS_ID "
					+ " WHERE TE.PARENT_ORIGIN='OBX' AND TE.TASK_TYPE='SYSTEM' AND (CREATED_AT<TO_DATE('" + dateCurrentInString
					+ "','yyyy-MM-dd') AND CREATED_AT>=TO_DATE('" + dateBeforeInString
					+ "','yyyy-MM-dd')) and TE.STATUS in ('" + MurphyConstant.RETURN + "'))";
			
			logger.error("[WorkBenchDao][autoCancelObxProcess][query] " + queryStatusUpdate);
			int result = this.getSession().createSQLQuery(queryStatusUpdate).executeUpdate();
			logger.error("[WorkBenchDao][autoCancelObxProcess][UpdateCount] " + result);
		}
		catch (Exception e) {
			logger.error("[WorkBenchDao][autoCancelObxProcess][Exception] " + e.getMessage());
		}
	}

}
