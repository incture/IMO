package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.geotab.Coordinates;
import com.murphy.integration.util.ApplicationConstant;
import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.FieldAvailabilityDto;
import com.murphy.taskmgmt.dto.FieldAvailabilityResponseDto;
import com.murphy.taskmgmt.dto.FieldOperatorAvailabilityDto;
import com.murphy.taskmgmt.dto.IopTaskListDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.NDVTaskListDto;
import com.murphy.taskmgmt.dto.ProcessEventsDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskListDto;
import com.murphy.taskmgmt.dto.TaskListResponseDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.dto.TaskSchedulingDto;
import com.murphy.taskmgmt.dto.TaskSchedulingGraphDto;
import com.murphy.taskmgmt.dto.TaskSchedulingResponseDto;
import com.murphy.taskmgmt.dto.TaskSchedulingUserDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.dto.UserTaskCount;
import com.murphy.taskmgmt.entity.ProcessEventsDo;
import com.murphy.taskmgmt.entity.TaskEventsDo;
import com.murphy.taskmgmt.entity.TaskEventsDoPK;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.GeoTabUtil;
import com.murphy.taskmgmt.util.MailAlertUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.SequenceGenerator;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("TaskEventsDao")
@Transactional
public class TaskEventsDao extends BaseDao<TaskEventsDo, TaskEventsDto> {

	private static final Logger logger = LoggerFactory.getLogger(TaskEventsDao.class);

	public TaskEventsDao() {
	}

	@Autowired
	private ProcessEventsDao processEventsDao;

	@Autowired
	private NDTaskMappingDao mappingDao;

	@Autowired
	private TaskOwnersDao ownerDao;

	@Autowired
	private HierarchyDao locDao;

	@Autowired
	private ConfigDao configDao;

	@Autowired
	private ShiftRegisterDao shiftRegisterDao;

	@Autowired
	private UserIDPMappingDao userMappingDao;

	@Autowired
	private AuditDao auditDao;

	@Override
	protected TaskEventsDto exportDto(TaskEventsDo entity) {
		TaskEventsDto taskEventsDto = new TaskEventsDto();
		taskEventsDto.setTaskId(entity.getTaskEventsDoPK().getTaskId());
		taskEventsDto.setProcessId(entity.getTaskEventsDoPK().getProcessId());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			taskEventsDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getName()))
			taskEventsDto.setName(entity.getName());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			taskEventsDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			taskEventsDto.setStatus(entity.getStatus());
		if (!ServicesUtil.isEmpty(entity.getCurrentProcessor()))
			taskEventsDto.setCurrentProcessor(entity.getCurrentProcessor());
		// if (!ServicesUtil.isEmpty(entity.getPriority()))
		// taskEventsDto.setPriority(entity.getPriority());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			taskEventsDto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getCompletedAt()))
			taskEventsDto.setCompletedAt(entity.getCompletedAt());
		if (!ServicesUtil.isEmpty(entity.getCompletionDeadLine()))
			taskEventsDto.setCompletionDeadLine(entity.getCompletionDeadLine());
		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			taskEventsDto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getTaskMode()))
			taskEventsDto.setTaskMode(entity.getTaskMode());
		if (!ServicesUtil.isEmpty(entity.getStatusFlag()))
			taskEventsDto.setStatusFlag(entity.getStatusFlag());
		if (!ServicesUtil.isEmpty(entity.getCurrentProcessorDisplayName()))
			taskEventsDto.setCurrentProcessorDisplayName(entity.getCurrentProcessorDisplayName());
		if (!ServicesUtil.isEmpty(entity.getTaskType()))
			taskEventsDto.setTaskType(entity.getTaskType());
		if (!ServicesUtil.isEmpty(entity.getForwardedBy()))
			taskEventsDto.setForwardedBy(entity.getForwardedBy());
		if (!ServicesUtil.isEmpty(entity.getForwardedAt()))
			taskEventsDto.setForwardedAt(entity.getForwardedAt());
		if (!ServicesUtil.isEmpty(entity.getOrigin()))
			taskEventsDto.setOrigin(entity.getOrigin());
		if (!ServicesUtil.isEmpty(entity.getUrl()))
			taskEventsDto.setDetailUrl(entity.getUrl());
		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			taskEventsDto.setUpdatedAt(entity.getUpdatedAt());
		if (!ServicesUtil.isEmpty(entity.getUpdatedBy()))
			taskEventsDto.setUpdatedBy(entity.getUpdatedBy());
		if (!ServicesUtil.isEmpty(entity.getPrevTask()))
			taskEventsDto.setPrevTask(entity.getPrevTask());
		if (!ServicesUtil.isEmpty(entity.getParentOrigin()))
			taskEventsDto.setParentOrigin(entity.getParentOrigin());
		if (!ServicesUtil.isEmpty(entity.getTaskRefNum())) 
			taskEventsDto.setTaskRefNum(entity.getTaskRefNum());
		if (!ServicesUtil.isEmpty(entity.getNotificationFlag())) 
			taskEventsDto.setNotificationFlag(entity.getNotificationFlag());
		return taskEventsDto;
	}

	@Override
	protected TaskEventsDo importDto(TaskEventsDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		TaskEventsDo entity = new TaskEventsDo();
		entity.setTaskEventsDoPK(new TaskEventsDoPK());
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			entity.getTaskEventsDoPK().setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getProcessId()))
			entity.getTaskEventsDoPK().setProcessId(fromDto.getProcessId());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getCurrentProcessor()))
			entity.setCurrentProcessor(fromDto.getCurrentProcessor());
		// if (!ServicesUtil.isEmpty(fromDto.getPriority()))
		// entity.setPriority(fromDto.getPriority());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getCompletedAt()))
			entity.setCompletedAt(fromDto.getCompletedAt());
		if (!ServicesUtil.isEmpty(fromDto.getCompletionDeadLine()))
			entity.setCompletionDeadLine(fromDto.getCompletionDeadLine());
		if (!ServicesUtil.isEmpty(fromDto.getCurrentProcessorDisplayName()))
			entity.setCurrentProcessorDisplayName(fromDto.getCurrentProcessorDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			entity.setDescription(fromDto.getDescription());
		if (!ServicesUtil.isEmpty(fromDto.getName()))
			entity.setName(fromDto.getName());
		if (!ServicesUtil.isEmpty(fromDto.getSubject()))
			entity.setSubject(fromDto.getSubject());
		if (!ServicesUtil.isEmpty(fromDto.getProcessName()))
			entity.setProcessName(fromDto.getProcessName());
		if (!ServicesUtil.isEmpty(fromDto.getTaskMode()))
			entity.setTaskMode(fromDto.getTaskMode());
		if (!ServicesUtil.isEmpty(fromDto.getStatusFlag()))
			entity.setStatusFlag(fromDto.getStatusFlag());
		if (!ServicesUtil.isEmpty(fromDto.getTaskType()))
			entity.setTaskType(fromDto.getTaskType());
		if (!ServicesUtil.isEmpty(fromDto.getForwardedBy()))
			entity.setForwardedBy(fromDto.getForwardedBy());
		if (!ServicesUtil.isEmpty(fromDto.getForwardedAt()))
			entity.setForwardedAt(fromDto.getForwardedAt());
		if (!ServicesUtil.isEmpty(fromDto.getOrigin()))
			entity.setOrigin(fromDto.getOrigin());
		if (!ServicesUtil.isEmpty(fromDto.getDetailUrl()))
			entity.setUrl(fromDto.getDetailUrl());
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedAt()))
			entity.setUpdatedAt(fromDto.getUpdatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedBy()))
			entity.setUpdatedBy(fromDto.getUpdatedBy());

		/* for Inquiry task */
		if (!ServicesUtil.isEmpty(fromDto.getParentOrigin())) {
			entity.setParentOrigin(fromDto.getParentOrigin());
		}
		if (!ServicesUtil.isEmpty(fromDto.getPrevTask())) {
			entity.setPrevTask(fromDto.getPrevTask());
		}
		if (!ServicesUtil.isEmpty(fromDto.getTaskRefNum())) {
			entity.setTaskRefNum(fromDto.getTaskRefNum());
		}
		if (!ServicesUtil.isEmpty(fromDto.isNotificationFlag()))
			entity.setNotificationFlag(fromDto.isNotificationFlag());

		return entity;
	}

	@SuppressWarnings("unchecked")
	public TaskEventsDto getTaskDetails(String taskId, String userType) {
		TaskEventsDto returnDto = null;
		try {
			String queryString = "";
			queryString = "select * from (Select te.TASK_ID ,te.PROCESS_ID,te.CREATED_AT,te.DESCRIPTION,te.STATUS,te.tsk_subject,pe.STARTED_BY_DISP,tw.TASK_OWNER, "
					+ "tw.TASK_OWNER_DISP,te.origin ,pe.loc_code,pe.PROCESS_TYPE,tw.p_id , pe.user_group , te.PARENT_ORIGIN , te.PREV_TASK, te.TASK_TYPE, "
					+ "te.UPDATED_AT, null,te.REFRENCE_NUM,tw.start_time,te.cur_proc,te.cur_proc_disp from TM_TASK_EVNTS te join TM_PROC_EVNTS pe on te.PROCESS_ID = pe.PROCESS_ID "
					+ "left outer join TM_TASK_OWNER tw on te.TASK_ID = tw.TASK_ID where te.TASK_ID = '" + taskId + "' "
					+ "and pe.loc_code not in (select LOCATION_CODE from WELL_TIER wt) "
					+ "UNION Select te.TASK_ID ,te.PROCESS_ID,te.CREATED_AT,te.DESCRIPTION,te.STATUS,te.tsk_subject,pe.STARTED_BY_DISP,tw.TASK_OWNER, "
					+ "tw.TASK_OWNER_DISP,te.origin ,pe.loc_code,pe.PROCESS_TYPE,tw.p_id , pe.user_group , te.PARENT_ORIGIN , te.PREV_TASK, te.TASK_TYPE,te.UPDATED_AT, "
					+ "wt.TIER,te.REFRENCE_NUM,tw.start_time,te.cur_proc,te.cur_proc_disp from WELL_TIER wt, TM_TASK_EVNTS te join TM_PROC_EVNTS pe on te.PROCESS_ID = pe.PROCESS_ID left outer join "
					+ "TM_TASK_OWNER tw on te.TASK_ID = tw.TASK_ID where wt.LOCATION_CODE = pe.loc_code AND te.TASK_ID = '"
					+ taskId + "')";
			Query q = this.getSession().createSQLQuery(queryString);

			System.err.println("[Murphy][TaskEventsDao][getTaskDetails][queryString]" + queryString);

			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				returnDto = new TaskEventsDto();
				TaskOwnersDto ownerDto = null;
				int i = 0;
				for (Object[] obj : response) {
					ownerDto = new TaskOwnersDto();
					if (i == 0) {
						returnDto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						returnDto.setProcessId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						returnDto.setCreatedAt(ServicesUtil.isEmpty(obj[2]) ? null : (Date) obj[2]);
						/*
						 * returnDto.setCreatedAtInString(ServicesUtil.isEmpty(
						 * obj[2]) ? null :
						 * ServicesUtil.convertFromZoneToZoneString(null,
						 * obj[2], MurphyConstant.UTC_ZONE,
						 * MurphyConstant.UTC_ZONE,
						 * MurphyConstant.DATE_DB_FORMATE,
						 * MurphyConstant.DATE_DISPLAY_FORMAT));
						 */
						// Send in epoch
						Date created_atInString = returnDto.getCreatedAt();
						returnDto.setCreatedAtInString(ServicesUtil.isEmpty(created_atInString) ? null
								: String.valueOf(created_atInString.getTime()));

						returnDto.setDescription(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
						returnDto.setStatus(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
						returnDto.setSubject(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
						returnDto.setCreatedByDisplay(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
						returnDto.setOrigin(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
						returnDto.setLocationCode(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
						returnDto.setLocationType(ServicesUtil.isEmpty(returnDto.getLocationCode()) ? null
								: locDao.getLocationtypeByLocCode(returnDto.getLocationCode()));
						returnDto.setTaskMode(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
						returnDto.setGroup(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
						returnDto.setParentOrigin(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
						returnDto.setPrevTask(ServicesUtil.isEmpty(obj[15]) ? null : (String) obj[15]);
						returnDto.setTaskType(ServicesUtil.isEmpty(obj[16]) ? null : (String) obj[16]);
						returnDto.setUpdatedAt(ServicesUtil.isEmpty(obj[17]) ? null : (Date) obj[17]);
						returnDto.setTier(ServicesUtil.isEmpty(obj[18]) ? null : (String) obj[18]);
						returnDto.setTaskRefNum(ServicesUtil.isEmpty(obj[19]) ? null
								: (((String) obj[19]).substring(((String) obj[19]).length() - 6)));
						/*
						 * returnDto.setStart_time(ServicesUtil.isEmpty(obj[20])
						 * ? null :
						 * ServicesUtil.convertFromZoneToZoneString(null,
						 * obj[20], MurphyConstant.UTC_ZONE,
						 * MurphyConstant.UTC_ZONE,
						 * MurphyConstant.DATE_DB_FORMATE,
						 * MurphyConstant.DATE_DISPLAY_FORMAT));
						 */
						// Send in epoch
						Date start_time = ServicesUtil.isEmpty(obj[20]) ? null : (Date) obj[20];
						returnDto.setStart_time(
								ServicesUtil.isEmpty(start_time) ? null : String.valueOf(start_time.getTime()));

						returnDto.setHasDispatchTask(MurphyConstant.INQUIRY.equals(returnDto.getOrigin())
								? checkIfDispatchExists(returnDto.getProcessId()) : false);
						returnDto.setOwners(new ArrayList<TaskOwnersDto>());

						// Start-CHG0037344-Inquiry to a field seat.
						returnDto.setCurrentProcessor(ServicesUtil.isEmpty(obj[21]) ? null : obj[21].toString());
						returnDto.setCurrentProcessorDisplayName(
								ServicesUtil.isEmpty(obj[22]) ? null : obj[22].toString());
						// End-CHG0037344-Inquiry to a field seat.

						i++;
					}
					ownerDto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					ownerDto.setTaskOwner(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
					ownerDto.setTaskOwnerDisplayName(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
					ownerDto.setpId(ServicesUtil.isEmpty(obj[12]) ? null : (String) obj[12]);

					returnDto.getOwners().add(ownerDto);
				}
			}
			logger.error("[Murphy][TaskEventsDao][getTaskDetails][returnDto ]" + returnDto);
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][getTaskDetails][error]" + e.getMessage());
		}
		return returnDto;
	}

	@SuppressWarnings("unchecked")
	private boolean checkIfDispatchExists(String processId) {
		String queryString = "";
		try {
			queryString = "Select te.TASK_ID from TM_TASK_EVNTS te  where te.PARENT_ORIGIN = '" + MurphyConstant.INQUIRY
					+ "'" + " and origin = '" + MurphyConstant.DISPATCH_ORIGIN + "' and te.status not in ( '"
					+ MurphyConstant.COMPLETE + "','" + MurphyConstant.RESOLVE + "')  and te.prev_task='" + processId
					+ "'";
			Query q = this.getSession().createSQLQuery(queryString);
			// logger.error("[Murphy][TaskEventsDao][checkIfDispatchExists][queryString]"+queryString);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response) && response.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][checkIfDispatchExists][error]" + e.getMessage() + "\n[queryString]"
					+ queryString);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public TaskListResponseDto getAllTasks(String userId, String userType, String group, String taskType, String origin,
			String location, Boolean isCreatedByMe, String device, String tier, Boolean isForToday,
			String classification, String subClassification, String country) {
		List<TaskListDto> responseDto = new ArrayList<TaskListDto>();
		// List<TaskListDto> responseMobileDto = new ArrayList<TaskListDto>();
		TaskListResponseDto respCustomTaskDto = new TaskListResponseDto();
		try {
			String commonQueryStartString = "", assignerTasksQuery = "", recieverTasksQuery = "",
					commonQueryMidString = "", queryString = "", mobilequeryString = "", bypass_ei_id_queryString = "";

			String latestCommentQuery = "(SELECT max(i.CREATED_AT) FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID)";
			commonQueryStartString = "Select te.TASK_ID AS TASK_ID,te.TSK_SUBJECT AS SUBJECT," + latestCommentQuery
					+ " AS CREATED_AT,"
					+ " ( SELECT i.MESSAGE FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID and created_at = "
					+ latestCommentQuery + ") AS MESSAGE, "
					+ " (SELECT i.USER_DISPLAY_NAME FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID and created_at = "
					+ latestCommentQuery + ") AS USER_DISPLAY_NAME,"
					+ "	te.STATUS AS STATUS ,te.PROCESS_ID AS PROCESS_ID ,pe.STARTED_BY_DISP AS STARTED_BY_DISP,pe.REQUEST_ID AS REQUEST_ID,"
					+ " te.CREATED_AT AS CT,"
					+ " ( SELECT max(i.INS_VALUE) FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID in ('NDO3','1234','INQ03') AND te.TASK_ID = i.TASK_ID  ) AS ASSIGNED_USER,"
					+ " te.UPDATED_AT AS UPDATED_AT ,te.CREATED_AT AS TSK_CREATED_AT ,"
					+ "( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID in('123','INQ01') AND te.TASK_ID = i.TASK_ID  ) AS LOCATION,"
					+ " te.origin As ORIGIN  ,"
					+ "pe.LOC_CODE as loc_code ,pe.STARTED_BY as started_by , pe.user_group as user_group,te.parent_origin as parent_origin,"
					+ " pe.extra_role as extra_role," + " te.REFRENCE_NUM as referenceNum ";
			// Changes for Cross field task and filter
			// pe.extra_role as extra_role

			commonQueryMidString = " from  TM_PROC_EVNTS pe left outer join  TM_TASK_EVNTS te on pe.PROCESS_ID = te.PROCESS_ID ";

			bypass_ei_id_queryString = "te.cur_proc as CURRENT_OWNER,te.cur_proc_disp AS CURRENT_OWNER_DISP, (SELECT max(j.FORM_ID) from EI_FORM j where "
					+ "pe.loc_code = j.location_id and j.status = 'IN PROGRESS' ) AS EI_FORMID,(SELECT max(sbh.SSD_BYPASS_ID) "
					+ "from SSD_BYPASS_HEADER sbh where sbh.location_code = pe.loc_code and sbh.bypass_status = 'IN PROGRESS') "
					+ "AS BYPASS_ID ";

			recieverTasksQuery = commonQueryStartString + " , tw.start_time as st,wt.TIER as tier, te.description, "
					+ bypass_ei_id_queryString + "" + commonQueryMidString + "";
			assignerTasksQuery = commonQueryStartString + " , null  as st, null as tier, te.description, "
					+ bypass_ei_id_queryString + "" + commonQueryMidString
					+ " where te.STATUS not in ('COMPLETED', 'REJECTED','CANCELLED') ";
			mobilequeryString = commonQueryStartString + " , tw.start_time as st,wt.TIER as tier, te.description, "
					+ bypass_ei_id_queryString + "" + commonQueryMidString + "";

			if (!ServicesUtil.isEmpty(location)) {
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -20);
				String locaArr[] = location.split(",");
				String locLikeQuery = " pe.LOC_CODE LIKE '" + locaArr[0] + "%'";
				for (int i = 1; i < locaArr.length; i++) {
					locLikeQuery += " or pe.LOC_CODE LIKE '" + locaArr[i] + "%'";
				}
				assignerTasksQuery = assignerTasksQuery + "and (" + locLikeQuery + ") and TO_DATE(te.CREATED_AT) >='"
						+ sdf.format(cal.getTime()) + "'";
				// SOC : Since Investigation task were showing in ROC as no USER
				// GROUP was there in query for selected location codes
				if (!ServicesUtil.isEmpty(group)) {
					group = ServicesUtil.getStringForInQuery(group);
					// Changes for Cross field task and filter
					assignerTasksQuery = assignerTasksQuery + " and (pe.USER_GROUP in(" + group + "))";
				}

				// EOC

			} else {
				if (!ServicesUtil.isEmpty(group)) {
					group = ServicesUtil.getStringForInQuery(group);
					// Changes for Cross field task and filter
					assignerTasksQuery = assignerTasksQuery + " and (pe.USER_GROUP in(" + group + "))";
				}
			}

			recieverTasksQuery = recieverTasksQuery
					+ "  left outer join WELL_TIER wt on wt.LOCATION_CODE = pe.LOC_CODE left outer join TM_TASK_OWNER tw on tw.TASK_ID = te.TASK_ID where te.STATUS IN ('"
					+ MurphyConstant.INPROGRESS + "','" + MurphyConstant.ASSIGN + "') and tw.TASK_OWNER ='" + userId
					+ "'";
			mobilequeryString = mobilequeryString
					+ "  left outer join WELL_TIER wt on wt.LOCATION_CODE = pe.LOC_CODE left outer join TM_TASK_OWNER tw on tw.TASK_ID = te.TASK_ID where te.STATUS IN ('"
					+ MurphyConstant.INPROGRESS + "','" + MurphyConstant.ASSIGN + "') and tw.TASK_OWNER ='" + userId
					+ "'";

			String addCreatedByMe = "";

			if (!ServicesUtil.isEmpty(isCreatedByMe)) {
				if (isCreatedByMe) {
					// recieverTasksQuery += " or pe.started_by ='" + userId +
					// "'";

					addCreatedByMe = " union "
							+ "Select te.TASK_ID AS TASK_ID,te.TSK_SUBJECT AS SUBJECT,(SELECT max(i.CREATED_AT) FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null "
							+ " AND te.TASK_ID = i.TASK_ID) AS CREATED_AT, ( SELECT i.MESSAGE FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID "
							+ " and created_at = (SELECT max(i.CREATED_AT) FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID)) AS MESSAGE, "
							+ " (SELECT i.USER_DISPLAY_NAME FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID and created_at = (SELECT max(i.CREATED_AT) "
							+ " FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID)) AS USER_DISPLAY_NAME, te.STATUS AS STATUS ,te.PROCESS_ID AS PROCESS_ID , "
							+ " pe.STARTED_BY_DISP AS STARTED_BY_DISP,pe.REQUEST_ID AS REQUEST_ID, te.CREATED_AT AS CT, ( SELECT max(i.INS_VALUE) FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID "
							+ " in ('NDO3','1234','INQ03') AND te.TASK_ID = i.TASK_ID  ) AS ASSIGNED_USER, te.UPDATED_AT AS UPDATED_AT ,te.CREATED_AT AS TSK_CREATED_AT , "
							+ " ( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID in('123','INQ01') AND te.TASK_ID = i.TASK_ID  ) AS LOCATION, te.origin As ORIGIN  , "
							+ " pe.LOC_CODE as loc_code ,pe.STARTED_BY as started_by , pe.user_group as user_group,te.parent_origin as parent_origin, pe.extra_role as extra_role, "
							+ " te.REFRENCE_NUM as referenceNum  , tw.start_time as st,wt.TIER as tier, te.description,te.cur_proc as CURRENT_OWNER,te.cur_proc_disp AS CURRENT_OWNER_DISP, "
							+ "(SELECT max(j.FORM_ID) from EI_FORM j where pe.loc_code = j.location_id and j.status = 'IN PROGRESS' ) AS EI_FORMID,"
							+ "(SELECT max(sbh.SSD_BYPASS_ID) from SSD_BYPASS_HEADER sbh where sbh.location_code = pe.loc_code and sbh.bypass_status = 'IN PROGRESS') "
							+ "AS BYPASS_ID from  TM_PROC_EVNTS pe left outer join  TM_TASK_EVNTS te "
							+ " on pe.PROCESS_ID = te.PROCESS_ID   left outer join WELL_TIER wt on wt.LOCATION_CODE = pe.LOC_CODE left outer join TM_TASK_OWNER tw on tw.TASK_ID = te.TASK_ID "
							+ " where te.STATUS not in ('COMPLETED', 'REJECTED','CANCELLED') " + " and  pe.started_by ='" + userId
							+ "' and te.TASK_TYPE  in ( 'Human', 'SYSTEM')";
				} else
					recieverTasksQuery += " and tw.TASK_OWNER ='" + userId + "'";
			} else {
				recieverTasksQuery += " and tw.TASK_OWNER ='" + userId + "'";
			}

			if (!ServicesUtil.isEmpty(taskType)) {
				recieverTasksQuery = recieverTasksQuery + " and te.TASK_TYPE  = '" + taskType + "'";
				assignerTasksQuery = assignerTasksQuery + " and te.TASK_TYPE  = '" + taskType + "'";
				mobilequeryString = mobilequeryString + " and te.TASK_TYPE  = '" + taskType + "'";
			} else {
				recieverTasksQuery = recieverTasksQuery + " and te.TASK_TYPE  in ( '" + MurphyConstant.HUMAN + "', '"
						+ MurphyConstant.SYSTEM + "')";
				assignerTasksQuery = assignerTasksQuery + " and te.TASK_TYPE in ( '" + MurphyConstant.HUMAN + "', '"
						+ MurphyConstant.SYSTEM + "')";
				mobilequeryString = mobilequeryString + " and te.TASK_TYPE in ( '" + MurphyConstant.HUMAN + "', '"
						+ MurphyConstant.SYSTEM + "')";
			}
			queryString = "Select distinct TASK_ID , SUBJECT ,CREATED_AT , MESSAGE , USER_DISPLAY_NAME , STATUS , PROCESS_ID , STARTED_BY_DISP , REQUEST_ID , CT ,   "
					+ "ASSIGNED_USER , UPDATED_AT , TSK_CREATED_AT , LOCATION , ORIGIN , loc_code , started_by , user_group,parent_origin, EXTRA_ROLE, referenceNum,st,tier  "
					+ ", description,CURRENT_OWNER,CURRENT_OWNER_DISP,EI_FORMID,BYPASS_ID from ( ";

			if (MurphyConstant.USER_TYPE_ALS.equals(userType) || MurphyConstant.FIELD.equalsIgnoreCase(userType)) {
				if (!ServicesUtil.isEmpty(origin)) {
					recieverTasksQuery = recieverTasksQuery + " and te.origin in ('" + origin + "')";
					mobilequeryString = mobilequeryString + " and te.origin in ('" + origin + "')";
				}
				queryString = recieverTasksQuery + " ORDER BY 21 asc ";
			} else {
				if (!ServicesUtil.isEmpty(isCreatedByMe)) {
					if (isCreatedByMe) {
						queryString = queryString + assignerTasksQuery + " UNION " + recieverTasksQuery + addCreatedByMe
								+ " ) ORDER BY 12 desc ";

					} else {
						queryString = queryString + assignerTasksQuery + " UNION " + recieverTasksQuery
								+ " ) ORDER BY 12 desc ";
					}
				} else {
					queryString = queryString + assignerTasksQuery + " UNION " + recieverTasksQuery
							+ " ) ORDER BY 12 desc ";
				}
			}
			if (!ServicesUtil.isEmpty(device) && device.equalsIgnoreCase("Mobile")) {
				String shift_end_time = shiftRegisterDao.getShiftEndTimingsbyEmp(userId,country);
				// To get the tasks according to well tier
				if (!ServicesUtil.isEmpty(tier) && tier.equalsIgnoreCase("A"))
					mobilequeryString = mobilequeryString + " and wt.TIER = 'Tier " + tier.toUpperCase() + "'";
				else if (!ServicesUtil.isEmpty(tier) && tier.equalsIgnoreCase("B"))
					mobilequeryString = mobilequeryString + " and wt.TIER = 'Tier " + tier.toUpperCase() + "'";
				else if (!ServicesUtil.isEmpty(tier) && tier.equalsIgnoreCase("C"))
					mobilequeryString = mobilequeryString + " and wt.TIER = 'Tier " + tier.toUpperCase() + "'";

				if (origin.equalsIgnoreCase("Investigation"))
					queryString = mobilequeryString + " ORDER BY 21 asc";
				else {
					// To get the task for today
					if (!ServicesUtil.isEmpty(isForToday) && isForToday) {
						String shift_start_time = shiftRegisterDao.getShiftStartTimingsbyEmp(userId);
						mobilequeryString = mobilequeryString + " and to_timestamp(tw.start_time) >= '" + shift_start_time
								+ "'";
					}
					queryString = mobilequeryString + " and to_timestamp(tw.start_time) <= '" + shift_end_time
							+ "' ORDER BY 21 asc";
				}
			}
			logger.error("[Murphy][TaskEventsDo][getAllTasks][query] " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> responseList = q.list();
			if (!ServicesUtil.isEmpty(responseList)) {
				TaskListDto dto = null;
				// respCustomTaskDto = new TaskListResponseDto();
				Set<String> locationFilterList = new HashSet<String>();
				Set<String> taskTypeFilterList = new HashSet<String>();
				Set<String> assignedToFilterList = new HashSet<String>();
				Set<String> taskClassficationFilterList = new HashSet<String>();
				Set<String> subClassficationFilterList = new HashSet<String>();
				Set<String> issueClassficationFilterList = new HashSet<String>();
				Set<String> createdByFilterList = new HashSet<String>();
				List<String> taskIdList = new ArrayList<>();
				for (Object[] obj : responseList) {

					if (!taskIdList.contains(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0])) {

						dto = new TaskListDto();
						dto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						dto.setDescription(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						if (!ServicesUtil.isEmpty(obj[3])) {
							/*
							 * dto.setCommentedAt(ServicesUtil.isEmpty(obj[2]) ?
							 * null :
							 * ServicesUtil.convertFromZoneToZoneString(null,
							 * obj[2], MurphyConstant.UTC_ZONE,
							 * MurphyConstant.UTC_ZONE,
							 * MurphyConstant.DATE_DB_FORMATE,
							 * MurphyConstant.DATE_DISPLAY_FORMAT));
							 */
							// Send in epoch
							Date commentedAt = ServicesUtil.isEmpty(obj[2]) ? null : (Date) obj[2];
							dto.setCommentedAt(
									ServicesUtil.isEmpty(commentedAt) ? null : String.valueOf(commentedAt.getTime()));

							dto.setLatestComment(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
							dto.setCommentedByDisplay(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
						}
						dto.setStatus(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
						dto.setProcessId(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
						dto.setCreatedBy(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
						dto.setRequestId(ServicesUtil.isEmpty(obj[8]) ? null : ((BigInteger) obj[8]).longValue());
						dto.setTaskOwner(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
						/*
						 * dto.setCreatedAtInString(ServicesUtil.isEmpty(obj[12]
						 * ) ? null :
						 * ServicesUtil.convertFromZoneToZoneString(null,
						 * obj[12], MurphyConstant.UTC_ZONE,
						 * MurphyConstant.UTC_ZONE,
						 * MurphyConstant.DATE_DB_FORMATE,
						 * MurphyConstant.DATE_DISPLAY_FORMAT));
						 */
						// Send in epoch
						Date created_at = ServicesUtil.isEmpty(obj[12]) ? null : (Date) obj[12];
						dto.setCreatedAtInString(
								ServicesUtil.isEmpty(created_at) ? null : String.valueOf(created_at.getTime()));

						if (ServicesUtil.isEmpty(obj[13])) {
							if (!ServicesUtil.isEmpty(obj[15])) {
								String locCode = (String) obj[15];
								String locText = locDao.getLocationByLocCode(locCode);
								dto.setLocation(ServicesUtil.isEmpty(locText) ? null : locText);
								locationFilterList.add(dto.getLocation());
							}
						} else {
							dto.setLocation(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
							locationFilterList.add(dto.getLocation());
						}
						dto.setOrigin(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
						taskTypeFilterList.add(dto.getOrigin());
						dto.setLocationCode(ServicesUtil.isEmpty(obj[15]) ? null : (String) obj[15]);
						dto.setMuwiId(
								ServicesUtil.isEmpty(obj[15]) ? null : locDao.getMuwiByLocationCode((String) obj[15]));
						dto.setCreatedByEmailId(ServicesUtil.isEmpty(obj[16]) ? null : (String) obj[16]);
						dto.setCreatorGroupId(ServicesUtil.isEmpty(obj[17]) ? null : (String) obj[17]);
						dto.setParentOrigin(ServicesUtil.isEmpty(obj[18]) ? null : (String) obj[18]);
						dto.setCreatedOnGroup(null);
						dto.setTaskRefNum(ServicesUtil.isEmpty(obj[20]) ? null
								: (((String) obj[20]).substring(((String) obj[20]).length() - 6)));
						/*
						 * dto.setStart_time(ServicesUtil.isEmpty(obj[21]) ?
						 * null : ServicesUtil.convertFromZoneToZoneString(null,
						 * obj[21], MurphyConstant.UTC_ZONE,
						 * MurphyConstant.UTC_ZONE,
						 * MurphyConstant.DATE_DB_FORMATE,
						 * MurphyConstant.DATE_DISPLAY_FORMAT));
						 */
						// Send in epoch
						Date epoch_start_time = ServicesUtil.isEmpty(obj[21]) ? null : (Date) obj[21];
						dto.setStart_time(ServicesUtil.isEmpty(epoch_start_time) ? null
								: String.valueOf(epoch_start_time.getTime()));

						dto.setTier(ServicesUtil.isEmpty(obj[22]) ? null : (String) obj[22]);
						dto.setTaskDescription(ServicesUtil.isEmpty(obj[23]) ? null : (String) obj[23]);
						dto.setHasEnergyIsolation(ServicesUtil.isEmpty(obj[26]) ? false : true);
						dto.setHasBypass(ServicesUtil.isEmpty(obj[27]) ? false : true);
						dto.setHasDispatch(checkIfDispatchExists(dto.getProcessId()));
						if (!ServicesUtil.isEmpty(dto.getOrigin())) {
							dto = getAttrValues(dto);
							if (!ServicesUtil.isEmpty(dto.getTaskClassification())) {
								if (!ServicesUtil.isEmpty(classification)
										&& !(dto.getTaskClassification().equalsIgnoreCase(classification))) {
									continue;// To filter the classification
								}
								taskClassficationFilterList.add(dto.getTaskClassification());
							}
							if (!ServicesUtil.isEmpty(dto.getSubClassification())) {
								if (!ServicesUtil.isEmpty(subClassification)
										&& !(dto.getSubClassification().equalsIgnoreCase(subClassification))) {
									continue;// To filter the subclassification
								}
								subClassficationFilterList.add(dto.getSubClassification());

							}
							if (!ServicesUtil.isEmpty(dto.getIssueClassification()))
								issueClassficationFilterList.add(dto.getIssueClassification());
						}
						// Start-CHG0037344-Inquiry to a field seat.

						if (!ServicesUtil.isEmpty(dto.getCreatorGroupId()) && !ServicesUtil.isEmpty(group)
								&& !ServicesUtil.isEmpty(dto.getOrigin())
								&& !ServicesUtil.isEmpty(dto.getParentOrigin())
								&& dto.getOrigin().equals(MurphyConstant.INQUIRY)
								&& dto.getParentOrigin().equals(MurphyConstant.INQUIRY)
								&& group.contains(dto.getCreatorGroupId()) && !ServicesUtil.isEmpty(obj[24])
								&& dto.getTaskOwner().split(",").length > 1
								&& (dto.getStatus().equals(MurphyConstant.INPROGRESS)
										|| dto.getStatus().equals(MurphyConstant.RESOLVE))) {
							dto.setTaskOwner(ServicesUtil.isEmpty(obj[25]) ? null : obj[25].toString());
						}

						if (!ServicesUtil.isEmpty(dto.getCreatorGroupId()) && !ServicesUtil.isEmpty(group)
								&& !ServicesUtil.isEmpty(dto.getOrigin())
								&& !ServicesUtil.isEmpty(dto.getParentOrigin())
								&& dto.getOrigin().equals(MurphyConstant.INQUIRY)
								&& dto.getParentOrigin().equals(MurphyConstant.INQUIRY)
								&& group.contains(dto.getCreatorGroupId()) && !ServicesUtil.isEmpty(obj[24])
								&& (dto.getStatus().equals(MurphyConstant.ASSIGN)))

						{
							dto.setTaskOwner(ServicesUtil.isEmpty(obj[24]) ? null : obj[24].toString());
						}

						// End-CHG0037344-Inquiry to a field seat.
						responseDto.add(dto);
						taskIdList.add((String) obj[0]);

					}

				}
				// To update the taskClassificationFilterList based on
				// SubClassification
				if (!ServicesUtil.isEmpty(subClassification)) {
					taskClassficationFilterList = new HashSet<String>();
					for (TaskListDto tempDto : responseDto) {
						if (!ServicesUtil.isEmpty(tempDto.getTaskClassification())) {
							taskClassficationFilterList.add(tempDto.getTaskClassification());
						}
					}
				}

				respCustomTaskDto.setTaskList(responseDto);
				respCustomTaskDto.setAssignedToFilterList(assignedToFilterList);
				respCustomTaskDto.setCreatedByFilterList(createdByFilterList);
				respCustomTaskDto.setIssueClassficationFilterList(issueClassficationFilterList);
				respCustomTaskDto.setLocationFilterList(locationFilterList);
				respCustomTaskDto.setSubClassficationFilterList(subClassficationFilterList);
				respCustomTaskDto.setTaskClassficationFilterList(taskClassficationFilterList);
				respCustomTaskDto.setTaskTypeFilterList(taskTypeFilterList);
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][getAllTasks][error]" + e.getMessage());
		}
		return respCustomTaskDto;
	}

	public long getLatestKey(String table, String column) {

		// String queryString = "Select max( cls."+column+") from
		// "+o.getClass().getSimpleName()+" cls ";
		String queryString = "Select max( cls." + column + ") from " + table + " cls ";
		Query q = this.getSession().createSQLQuery(queryString);
		BigInteger current = (BigInteger) q.uniqueResult();
		SequenceGenerator sg = new SequenceGenerator();
		return sg.getNextKey(current);
	}

	public String updateTaskEventStatus(String taskId, String processId, String user, String userDisplay, String status,
			Date userUpdatedAt, String description, Boolean schedulerRun, String isGroupTask) {
		String response = MurphyConstant.FAILURE;
		String updatedStatus = null;
		String taskCreator = null;
		String taskCreatorName = null;
		Boolean isResolved = false;
		String mailStatus = null;
		MailAlertUtil alertUtil = new MailAlertUtil();
		try {
			String queryString = "Select te from TaskEventsDo te where te.taskEventsDoPK.taskId = '" + taskId + "'";
			Query q = this.getSession().createQuery(queryString);
			TaskEventsDo updateDo = (TaskEventsDo) q.uniqueResult();
			if (!ServicesUtil.isEmpty(updateDo)) {
				if (!ServicesUtil.isEmpty(description)) {
					updateDo.setDescription(description);
				}
				if (!ServicesUtil.isEmpty(status)) {

					if (MurphyConstant.RESOLVE.equals(status)) {
						updateDo.setCurrentProcessor(user);
						updateDo.setCurrentProcessorDisplayName(userDisplay);
						mappingDao.changeStatusWhenResolved(updateDo.getTaskEventsDoPK().getTaskId());
						// ownerDao.updatePriorityWhenResolved(updateDo.getTaskEventsDoPK().getTaskId());
						if (MurphyConstant.INVESTIGATON.equals(updateDo.getOrigin())) {
							updateDo.setCompletedAt(new Date());
						}

						// SOC : When multiple dispatch created on single
						// inquiry task
						List<String> parentList = getParentTaskIDForCurrent(taskId);
						if (!ServicesUtil.isEmpty(parentList)) {
							updateDo.setParentOrigin(ServicesUtil.isEmpty(parentList.get(0))
									? updateDo.getParentOrigin() : parentList.get(0));
							updateDo.setPrevTask(ServicesUtil.isEmpty(parentList.get(1)) ? updateDo.getPrevTask()
									: parentList.get(1));
						}
						// EOC

						if (MurphyConstant.INQUIRY.equals(updateDo.getParentOrigin())
								&& MurphyConstant.DISPATCH_ORIGIN.equals(updateDo.getOrigin())) {
							// updateDo.setCompletedAt(new Date());
							updateInquiryStatus(updateDo.getPrevTask(), status);
							taskCreator = getTaskCreator(updateDo.getTaskEventsDoPK().getProcessId());
							// auditDao.createInstance( status ,taskCreator,
							// updateDo.getPrevTask());
							String inqTaskId = getLatestTaskForProcess(updateDo.getPrevTask());
							auditDao.createInstance(status, taskCreator, inqTaskId);
							triggerMailNotification(MurphyConstant.RESOLVE, updateDo.getPrevTask(), inqTaskId,
									MurphyConstant.INQUIRY, MurphyConstant.DISPATCH_ORIGIN);
						}
					}
					if (MurphyConstant.COMPLETE.equals(status)) {
						if (!MurphyConstant.INVESTIGATON.equals(updateDo.getOrigin())) {
							updateDo.setCompletedAt(new Date());
						}
						ProcessEventsDo processDo = new ProcessEventsDo();
						processDo.setProcessId(updateDo.getTaskEventsDoPK().getProcessId());
						processDo.setStatus(status);
						processEventsDao.updateProcessStatusToComp(processDo);
						mappingDao.changeStatusWhenComplete(updateDo.getTaskEventsDoPK().getTaskId(),
								updateDo.getStatus());

						logger.error("UpdateDo Line 612 : " + updateDo.toString());
						// SOC : When multiple dispatch created on single
						// inquiry task
						List<String> parentList = getParentTaskIDForCurrent(taskId);
						if (!ServicesUtil.isEmpty(parentList)) {
							logger.error("parentList 616 : " + parentList.toString());
							updateDo.setParentOrigin(ServicesUtil.isEmpty(parentList.get(0))
									? updateDo.getParentOrigin() : parentList.get(0));
							updateDo.setPrevTask(ServicesUtil.isEmpty(parentList.get(1)) ? updateDo.getPrevTask()
									: parentList.get(1));
						}
						// EOC

						if (MurphyConstant.INQUIRY.equals(updateDo.getParentOrigin())
								&& MurphyConstant.DISPATCH_ORIGIN.equals(updateDo.getOrigin())) {
							// updateDo.setCompletedAt(new Date());
							updateInquiryStatus(updateDo.getPrevTask(), status);
							ProcessEventsDo inqProcessDo = new ProcessEventsDo();
							inqProcessDo.setProcessId(updateDo.getPrevTask());
							inqProcessDo.setStatus(status);
							// logger.error("[Murphy][TaskEventsDao][updateProcessInstance]before
							// initate with " + processDo);
							processEventsDao.updateProcessStatusToComp(inqProcessDo);
							taskCreator = getTaskCreator(updateDo.getTaskEventsDoPK().getProcessId());
							auditDao.createInstance(status, taskCreator, updateDo.getPrevTask());
							String inqTaskId = getLatestTaskForProcess(updateDo.getPrevTask());
							auditDao.createInstance(status, taskCreator, inqTaskId);
							// triggerMailNotification(MurphyConstant.RESOLVE,updateDo.getPrevTask(),inqTaskId,MurphyConstant.INQUIRY,MurphyConstant.DISPATCH);
						}
						// SOC: Auto Close of Dispatch task
						Date createdAt = updateDo.getCreatedAt();
						Date currentDate = new Date();
						long diff = currentDate.getTime() - createdAt.getTime();
						long diffDays = diff / (24 * 60 * 60 * 1000);
						// Added for OBX Cancelled task too.
						if (diffDays >= 14 && schedulerRun) {
							if ((MurphyConstant.CUSTOM.equals(updateDo.getParentOrigin())
									|| MurphyConstant.ALARM.equals(updateDo.getParentOrigin())
									|| (MurphyConstant.OBX.equalsIgnoreCase(updateDo.getParentOrigin())
											&& updateDo.getStatus().equalsIgnoreCase(MurphyConstant.CANCELLED)))
									&& MurphyConstant.DISPATCH_TASK.equals(updateDo.getOrigin())) {
								ProcessEventsDo processEventsDo = new ProcessEventsDo();
								processEventsDo.setProcessId(updateDo.getTaskEventsDoPK().getProcessId());
								processEventsDo.setStatus(MurphyConstant.COMPLETE);
								processEventsDao.updateProcessStatusToComplete(processEventsDo);
								updatedStatus = updateAutoChangeStatus(updateDo.getTaskEventsDoPK().getTaskId(),
										status);
								taskCreator = getTaskCreator(updateDo.getTaskEventsDoPK().getProcessId());
								auditDao.createInstance(updateDo.getStatus(), taskCreator, updateDo.getPrevTask());
								String inqTaskId = getLatestTaskForProcess(updateDo.getPrevTask());
								auditDao.createInstance(updateDo.getStatus(), taskCreator, inqTaskId);
							}
						}
						// EOC: Auto Close of Dispatch task
					}

					if (!ServicesUtil.isEmpty(updatedStatus)) {
						if (updatedStatus.equals(MurphyConstant.SUCCESS)) {
							updateDo.setStatus(MurphyConstant.COMPLETE);
						}
					}

					updateDo.setStatus(status);

					if (MurphyConstant.REASSIGN.equals(status)) {
						updateDo.setStatus(MurphyConstant.ASSIGN);
					}
				}
				updateDo.setUpdatedAt(new Date());
				updateDo.setUpdatedBy(userDisplay);

				// Start-CHG0037344-Inquiry to a field seat.

				// String userGrp =
				// getUserGroup(updateDo.getTaskEventsDoPK().getProcessId());
				if (updateDo.getParentOrigin().equals("Inquiry") && updateDo.getOrigin().equals("Inquiry")
						&& status.equals(MurphyConstant.INPROGRESS)) {
					updateDo.setCurrentProcessor(user);
					updateDo.setCurrentProcessorDisplayName(userDisplay);
				}

				if (!ServicesUtil.isEmpty(isGroupTask) && updateDo.getParentOrigin().equals("Inquiry")
						&& updateDo.getOrigin().equals("Inquiry") && status.equals(MurphyConstant.ASSIGN)) {
					if (isGroupTask.split(",")[0].equalsIgnoreCase("true"))
						updateDo.setCurrentProcessor(isGroupTask.split(",")[1]);
					else if (isGroupTask.split(",")[0].equalsIgnoreCase("null")
							|| isGroupTask.split(",")[0].equalsIgnoreCase("false"))
						updateDo.setCurrentProcessor(null);
				}

				// End-CHG0037344-Inquiry to a field seat.

				if (!ServicesUtil.isEmpty(userUpdatedAt)) {

					updateDo.setUserUpdatedAt(userUpdatedAt);
				}
				merge(updateDo);
				response = MurphyConstant.SUCCESS;
				triggerMailNotification(status, updateDo.getTaskEventsDoPK().getProcessId(),
						updateDo.getTaskEventsDoPK().getTaskId(), updateDo.getParentOrigin(), updateDo.getOrigin());

			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][updateTaskEventStatus][error]" + e.getMessage());
		}
		return response;
	}

	// SOC: To get the parent task id of any task
	@SuppressWarnings({ "unchecked" })
	private List<String> getParentTaskIDForCurrent(String taskId) {
		List<String> list = new ArrayList<>();
		String parentTaskId = "";
		try {
			String query = " SELECT PREV_TASK FROM TM_TASK_EVNTS where TASK_ID = '" + taskId + "'";
			Query q = this.getSession().createSQLQuery(query);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {

				String tempParentTaskId = response.get(0);
				while (!ServicesUtil.isEmpty(tempParentTaskId)) {
					String sq = " SELECT PREV_TASK FROM TM_TASK_EVNTS where TASK_ID = '" + tempParentTaskId + "'";
					Query q1 = this.getSession().createSQLQuery(sq);
					List<String> res = (List<String>) q1.list();
					if (!ServicesUtil.isEmpty(res)) {
						tempParentTaskId = res.get(0);
						if (!ServicesUtil.isEmpty(tempParentTaskId))
							parentTaskId = tempParentTaskId;
					} else
						break;
				}
				if (!ServicesUtil.isEmpty(parentTaskId)) {
					logger.error("[parentTaskId][getParentTaskIDForCurrent][Process_ID] : " + parentTaskId);
					String parentQuery = " SELECT PARENT_ORIGIN FROM TM_TASK_EVNTS where PROCESS_ID = '" + parentTaskId
							+ "'";
					Query pQ = this.getSession().createSQLQuery(parentQuery);
					List<String> response2 = (List<String>) pQ.list();
					if (!ServicesUtil.isEmpty(response2)) {
						if (!ServicesUtil.isEmpty(response2.get(0))
								&& response2.get(0).equalsIgnoreCase(MurphyConstant.INQUIRY)) {
							list.add(response2.get(0));
							list.add(parentTaskId);
						}
					}
				} else
					logger.error("No recurring parentTaskId for the current task Id : " + taskId);
			} else
				logger.error("No prev task Id for the current task Id : " + taskId);
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][getParentTaskIDForCurrent][error]" + e.getMessage());
			e.printStackTrace();
		}
		logger.error("list[getParentTaskIDForCurrent] : " + list.toString());
		return list;
	}
	// EOC

	public void triggerMailNotification(String status, String processId, String taskId, String parentOrigin,
			String origin) {
		MailAlertUtil alertUtil = new MailAlertUtil();
		Boolean isResolved = false;
		String taskCreatorName = null;
		String mailStatus = null;
		try {
			if (status.equalsIgnoreCase(MurphyConstant.RESOLVE) || status.equalsIgnoreCase(MurphyConstant.COMPLETE)) {

				if (status.equalsIgnoreCase(MurphyConstant.COMPLETE)) {
					isResolved = isInquiryResolved(taskId, status);
				}
				if (isResolved == false && (status.equalsIgnoreCase(MurphyConstant.RESOLVE)
						|| status.equalsIgnoreCase(MurphyConstant.COMPLETE))) {
					if ((MurphyConstant.INQUIRY.equals(parentOrigin) && MurphyConstant.INQUIRY.equals(origin))
							|| ((MurphyConstant.DISPATCH_ORIGIN.equalsIgnoreCase(origin)
									&& MurphyConstant.INQUIRY.equalsIgnoreCase(parentOrigin)))) {
						ProcessEventsDto procDto = getTaskCreatorDetails(processId);
						if (!ServicesUtil.isEmpty(procDto.getLocationCode())
								&& !ServicesUtil.isEmpty(procDto.getStartedBy())
								&& !ServicesUtil.isEmpty(procDto.getGroup())) {
							String wellName = locDao.getLocationByLocCode(procDto.getLocationCode());
							taskCreatorName = getTaskCreatorName(procDto.getStartedBy(), null);
							// Triggering Mail Alert to Engineer
							if (!ServicesUtil.isEmpty(taskCreatorName)) {
								com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
								String moduleLink = ApplicationConstant.INQUIRY_APP_LINK + taskId + "&?processId="
										+ processId;
								// mailStatus = alertUtil.sendMailAlert(
								// "swathi.vs@incture.com,swetha.k@incture.com,sergio_rivera@murphyoilcorp.com,"
								// + procDto.getStartedBy(),
								// "Inquiry Resolved - " + wellName,
								// MurphyConstant.EMAIL_RSL_CONTENT_INQUIRY,
								// taskCreatorName, moduleLink);

								mailStatus = alertUtil.sendMailAlert(procDto.getStartedBy(),
										"Inquiry Resolved - " + wellName, MurphyConstant.EMAIL_RSL_CONTENT_INQUIRY,
										taskCreatorName, moduleLink, null);

								if (mailStatus.equalsIgnoreCase(MurphyConstant.FAILURE)) {
									alertUtil.sendMailAlert("swathisathiyanarayanan@gmail.com", "Mail Not send",
											"Mail Not send for ID" + procDto.getStartedBy(), "Swathi", null, null);

								}
							}
						}

					}

				}
			}
		} catch (Exception e) {
			logger.error("Exception while Triggering Mail Alert" + e.getMessage());
		}

	}

	private Boolean isInquiryResolved(String taskId, String status) {
		BigInteger count = null;
		try {
			String selectQuery = "SELECT COUNT(*) FROM TM_AUDIT_TRAIL WHERE TASK_ID='" + taskId
					+ "' AND ACTION IN('RESOLVED')";
			Query q = this.getSession().createSQLQuery(selectQuery);
			List<BigInteger> response = (List<BigInteger>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				count = response.get(0);

				if (count.equals(BigInteger.ONE)) {
					return true;
				} else {
					return false;
				}
			}

		} catch (Exception e) {
			logger.error("Exception while Getting Inquiry Audit Info" + e.getMessage());
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	private ProcessEventsDto getTaskCreatorDetails(String processId) {
		ProcessEventsDto procEvntsDto = null;
		try {
			String selectQuery = "SELECT STARTED_BY,USER_GROUP,LOC_CODE,STATUS FROM TM_PROC_EVNTS WHERE PROCESS_ID='"
					+ processId + "'";

			Query q = this.getSession().createSQLQuery(selectQuery);

			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				for (Object[] obj : response) {
					procEvntsDto = new ProcessEventsDto();
					procEvntsDto.setStartedBy(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					procEvntsDto.setGroup(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					procEvntsDto.setLocationCode(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					procEvntsDto.setStatus(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
				}
			}
		} catch (Exception ex) {
			logger.error("Exception While Fetching Process Details" + ex.getMessage());
		}
		return procEvntsDto;
	}

	// Auto Close of Dispatch task for more than 14 days of Parent Origin :
	// Custom, Alarms
	public String updateAutoChangeStatus(String taskId, String status) {
		String updateQuery = "update tm_task_evnts set status='" + MurphyConstant.COMPLETE + "' ,UPDATED_BY= '"
				+ MurphyConstant.SYSTEM + "' , COMPLETED_AT =  TO_TIMESTAMP('"
				+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
				+ "', 'yyyy-mm-dd hh24:mi:ss')  where task_id ='" + taskId + "' AND PARENT_ORIGIN IN ('"
				+ MurphyConstant.ALARM + "','" + MurphyConstant.CUSTOM + "')";

		Query q = this.getSession().createSQLQuery(updateQuery);
		logger.error("[Murphy][TaskEventsDao][updateAutoChangeStatus][error]" + updateQuery);
		Integer result = q.executeUpdate();
		logger.error("[Murphy][TaskEventsDao][updateAutoChangeStatus][result]" + result);
		if (result > 0)
			return MurphyConstant.SUCCESS;
		else
			return MurphyConstant.NO_RECORD;

	}

	// @SuppressWarnings("unchecked")
	// public TaskSchedulingResponseDto getDataForSchedulingMano(String userId,
	// String orderBy, String role , int timeZoneOffSet) {
	// TaskSchedulingResponseDto responseDto = new TaskSchedulingResponseDto();
	// ResponseMessage response = new ResponseMessage();
	// response.setMessage(MurphyConstant.READ_FAILURE);
	// response.setStatus(MurphyConstant.FAILURE);
	// response.setStatusCode(MurphyConstant.CODE_FAILURE);
	// if (!ServicesUtil.isEmpty(userId)) {
	// userId= ServicesUtil.getStringForInQuery(userId);
	// } else {
	// userId = getStringFromList(ownerDao.getTaskOwnersbyId(null, role));
	// }
	// try {
	// if (ServicesUtil.isEmpty(userId)) {
	// response.setMessage(MurphyConstant.NO_RESULT);
	// } else {
	// if(ServicesUtil.isEmpty(orderBy)){
	// orderBy ="ASC";
	// }
	// List<TaskSchedulingUserDto> returnList = null;
	// List<TaskSchedulingGraphDto> graphList = null;
	// String query = "Select CREATED_AT, PRIORITY,TASK_OWNER,
	// LOC,CLASSIFICATION ,SUBCLASSIFICATION ,"
	// + " RESOLVE_TIME ,"
	// + " TASK_OWNER_DISP , SUBJECT , START_TIME,END_TIME,TASK_ID,DRIVE_TIME
	// from ("
	// + "SELECT te.CREATED_AT AS CREATED_AT,tw.PRIORITY AS PRIORITY,
	// tw.TASK_OWNER AS TASK_OWNER,"
	// + "(SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE ATTR_TEMP_ID = '123'
	// AND te.TASK_ID = i.TASK_ID ) AS LOC, "
	// + "(SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE ATTR_TEMP_ID =
	// '12345' AND te.TASK_ID = i.TASK_ID) AS CLASSIFICATION,"
	// + "(SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE ATTR_TEMP_ID =
	// '123456' AND te.TASK_ID = i.TASK_ID ) AS SUBCLASSIFICATION,
	// tw.TASK_OWNER_DISP AS TASK_OWNER_DISP ,"
	// + " te.TSK_SUBJECT AS SUBJECT ,tw.START_TIME AS START_TIME, tw.END_TIME
	// AS END_TIME ,te.TASK_ID AS TASK_ID , tw.EST_RESOLVE_TIME AS RESOLVE_TIME
	// , tw.EST_DRIVE_TIME AS DRIVE_TIME"
	// + " FROM TM_TASK_EVNTS AS te LEFT OUTER JOIN TM_TASK_OWNER AS tw ON
	// te.TASK_ID = tw.TASK_ID where te.STATUS not in ('"
	// + MurphyConstant.COMPLETE + "','" + MurphyConstant.RETURN + "','" +
	// MurphyConstant.RESOLVE
	// + "') and te.origin <> '"+MurphyConstant.INVESTIGATON+"' )" + " where
	// TASK_OWNER in (" + userId + ")";
	// query = query + " order by TASK_OWNER " + orderBy + ",PRIORITY asc";
	//
	//
	// //
	// System.err.println("[Murphy][TaskEventsDao][getDataForScheduling][query]"+query);
	// Query q = this.getSession().createSQLQuery(query);
	// List<Object[]> result = (List<Object[]>) q.list();
	//
	// if (!ServicesUtil.isEmpty(result)) {
	// Map<String , Integer> locationMap = new HashMap<String , Integer>();
	// returnList = new ArrayList<TaskSchedulingUserDto>();
	// graphList = new ArrayList<TaskSchedulingGraphDto>();
	// TaskSchedulingDto dto = null;
	// TaskSchedulingGraphDto graphDto = null;
	// TaskSchedulingUserDto userDto = null;
	// for (Object[] obj : result) {
	// dto = new TaskSchedulingDto();
	// graphDto = new TaskSchedulingGraphDto();
	// dto.setCreatedAtDisplay(ServicesUtil.isEmpty(obj[0]) ? null
	// : ServicesUtil.convertFromZoneToZoneString(null, obj[0],
	// MurphyConstant.UTC_ZONE,
	// MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
	// MurphyConstant.DATE_DISPLAY_FORMAT));
	// dto.setPriority(ServicesUtil.isEmpty(obj[1]) ? 0 : (Integer) obj[1]);
	// dto.setLocation(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
	// dto.setClassification(ServicesUtil.isEmpty(obj[4]) ? null : (String)
	// obj[4]);
	// dto.setSubClassification(ServicesUtil.isEmpty(obj[5]) ? null : (String)
	// obj[5]);
	//
	// dto.setEstResolveTime(
	// ServicesUtil.isEmpty(obj[6]) ? 0
	// : ((Double) obj[6]).floatValue());
	// dto.setTravelTime(ServicesUtil.isEmpty(obj[12]) ? 0
	// : (Math.round((Double) obj[12])));
	// dto.setTotalEstTime(ServicesUtil.isEmpty(dto.getTravelTime()) ||
	// (dto.getTravelTime() == -1)? dto.getEstResolveTime()
	// : dto.getEstResolveTime() + dto.getTravelTime());
	//
	//
	// dto.setStartTime(ServicesUtil.isEmpty(obj[9]) ? null
	// : ServicesUtil.convertFromZoneToZone(null, obj[9],
	// MurphyConstant.UTC_ZONE,
	// MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
	// MurphyConstant.DATE_DISPLAY_FORMAT));
	// dto.setStartTimeInString(ServicesUtil.isEmpty(obj[9]) ? null
	// : ServicesUtil.convertFromZoneToZoneString(null, obj[9],
	// MurphyConstant.UTC_ZONE,
	// MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
	// MurphyConstant.DATE_DISPLAY_FORMAT));
	// dto.setEndTime(ServicesUtil.isEmpty(obj[10]) ? null
	// : ServicesUtil.convertFromZoneToZone(null, obj[10],
	// MurphyConstant.UTC_ZONE,
	// MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
	// MurphyConstant.DATE_DISPLAY_FORMAT));
	// dto.setEndTimeInString(ServicesUtil.isEmpty(obj[10]) ? null
	// : ServicesUtil.convertFromZoneToZoneString(null, obj[10],
	// MurphyConstant.UTC_ZONE,
	// MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
	// MurphyConstant.DATE_DISPLAY_FORMAT));
	// dto.setTaskId(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
	//
	// graphDto.setTimeInHrs(dto.getTotalEstTime());
	// graphDto.setUserName(ServicesUtil.isEmpty(obj[7]) ? null :(String)
	// obj[7]);
	// graphDto.setTaskSubject(ServicesUtil.isEmpty(obj[8]) ? null : (String)
	// obj[8]);
	// graphList.add(graphDto);
	//
	// if(locationMap.containsKey((String) obj[2])){
	// returnList.get(locationMap.get((String)
	// obj[2])).getAppointments().add(dto);
	// userDto.setTotalWorkTime(dto.getTotalEstTime()+userDto.getTotalWorkTime());
	// if(userDto.getAvailableTime() > 0){
	// String taskPosition =
	// ServicesUtil.checkIfTimeExistsInInterval(dto.getStartTime(),new Date()
	// ,dto.getEndTime());
	// if(MurphyConstant.AFTER.equals(taskPosition)){
	// // System.err.println("In AFTER");
	// userDto.setAvailableTime(userDto.getAvailableTime() -
	// dto.getEstResolveTime());
	// }else if(MurphyConstant.CURRENT.equals(taskPosition) &&
	// ServicesUtil.isEmpty(dto.getEndTime())){
	// // System.err.println("In CURRENT");
	// userDto.setAvailableTime(userDto.getAvailableTime() -
	// ServicesUtil.getDiffBtwnTwoDates(dto.getEndTime(),new Date(),
	// MurphyConstant.MINUTES));
	// }
	// }else{
	// userDto.setAvailableTime(0);
	// }
	// }else{
	// userDto = new TaskSchedulingUserDto();
	// userDto.setTaskOwner(ServicesUtil.isEmpty(obj[2]) ? null : (String)
	// obj[2]);
	// userDto.setName(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
	// userDto.setAppointments(new ArrayList<TaskSchedulingDto>());
	// userDto.getAppointments().add(dto);
	//
	// if(ServicesUtil.checkIfTimeExistsInInterval(ServicesUtil.convertFromZoneToZone(null,
	// "07:00:00", MurphyConstant.CST_ZONE,
	// MurphyConstant.CST_ZONE, MurphyConstant.HRSMINSEC_FORMAT,
	// MurphyConstant.HRSMINSEC_FORMAT),
	// ServicesUtil.convertFromZoneToZone(null, obj[0], MurphyConstant.UTC_ZONE,
	// MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
	// MurphyConstant.HRSMINSEC_FORMAT),ServicesUtil.convertFromZoneToZone(null,
	// "19:00:00", MurphyConstant.CST_ZONE,
	// MurphyConstant.CST_ZONE, MurphyConstant.HRSMINSEC_FORMAT,
	// MurphyConstant.HRSMINSEC_FORMAT)).equals(MurphyConstant.CURRENT)){
	// userDto.setShift("7am - 7pm");
	// userDto.setAvailableTime(ServicesUtil.fetchAvailableTime(19 ,
	// timeZoneOffSet));
	// }else{
	// userDto.setShift("7pm - 7am");
	// userDto.setAvailableTime(ServicesUtil.fetchAvailableTime(7 ,
	// timeZoneOffSet));
	// }
	// System.err.println("setAvailableTime" + userDto.getAvailableTime());
	// userDto.setTotalWorkTime(dto.getTotalEstTime());
	// locationMap.put( (String) obj[2], returnList.size());
	// returnList.add(userDto);
	// }
	// }
	//
	// responseDto.setTaskList(returnList);
	// responseDto.setGraphDtos(graphList);
	// response.setMessage(MurphyConstant.READ_SUCCESS);
	// }else{
	// response.setMessage(MurphyConstant.NO_RESULT);
	// }
	// }
	// response.setStatus(MurphyConstant.SUCCESS);
	// response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	//
	// } catch (Exception e) {
	// logger.error("[Murphy][TaskEventsDao][getDataForScheduling][error] [e]"
	// +e+"e.getMessage" +e.getMessage());
	// e.printStackTrace();
	// }
	//
	// responseDto.setResponseMessage(response);
	// return responseDto;
	// }

	@SuppressWarnings("unchecked")
	public TaskSchedulingResponseDto getDataForScheduling(String userId, String orderBy, String role,
			int timeZoneOffSet, String fromDate, String toDate) {
		TaskSchedulingResponseDto responseDto = new TaskSchedulingResponseDto();
		ResponseMessage response = new ResponseMessage();
		response.setMessage(MurphyConstant.READ_FAILURE);
		response.setStatus(MurphyConstant.FAILURE);
		response.setStatusCode(MurphyConstant.CODE_FAILURE);
		if (!ServicesUtil.isEmpty(userId)) {
			userId = ServicesUtil.getStringForInQuery(userId);
		} else {
			userId = getStringFromList(ownerDao.getTaskOwnersbyId(null, role));
		}
		try {
			if (ServicesUtil.isEmpty(userId)) {
				response.setMessage(MurphyConstant.NO_RESULT);
			} else {
				if (ServicesUtil.isEmpty(orderBy)) {
					orderBy = "ASC";
				}
				List<TaskSchedulingUserDto> returnList = null;
				List<TaskSchedulingGraphDto> graphList = null;
				String date = fromDate + "','" + toDate;

				String newQuery = "Select CREATED_AT, PRIORITY,TASK_OWNER, LOC,CLASSIFICATION ,SUBCLASSIFICATION , RESOLVE_TIME ,"
						+ " TASK_OWNER_DISP , SUBJECT , START_TIME,END_TIME,TASK_ID,DRIVE_TIME ,STATUS,TIER,CUSTOM,PID ,resolvedTime ,inProgrssTime,completed from ("
						+ "SELECT te.CREATED_AT AS CREATED_AT,tw.PRIORITY AS PRIORITY, tw.TASK_OWNER AS TASK_OWNER,"
						+ "(SELECT max(i.INS_VALUE)  FROM TM_ATTR_INSTS AS i WHERE ATTR_TEMP_ID = '123' AND te.TASK_ID = i.TASK_ID ) AS LOC, "
						+ "(SELECT max(i.INS_VALUE)  FROM TM_ATTR_INSTS AS i WHERE ATTR_TEMP_ID = '12345' AND te.TASK_ID = i.TASK_ID) AS CLASSIFICATION,"
						+ "(SELECT max(i.INS_VALUE) FROM TM_ATTR_INSTS AS i WHERE ATTR_TEMP_ID = '123456' AND te.TASK_ID = i.TASK_ID ) AS SUBCLASSIFICATION, "
						+ "tw.TASK_OWNER_DISP AS TASK_OWNER_DISP ,"
						+ " te.TSK_SUBJECT AS SUBJECT ,tw.START_TIME AS START_TIME, tw.END_TIME AS END_TIME"
						+ " ,te.TASK_ID AS TASK_ID , tw.EST_RESOLVE_TIME AS RESOLVE_TIME , tw.EST_DRIVE_TIME AS DRIVE_TIME ,"
						+ " te.status as STATUS , tw.TIER AS TIER,tw.Custom_time AS CUSTOM,tw.p_id AS PID , "
						+ " (Select max(created_At) from tm_audit_trail au where au.task_id = te.task_id  and au.action = '"
						+ MurphyConstant.RESOLVE + "') as resolvedTime , "
						+ " (Select max(created_At) from tm_audit_trail au where au.task_id = te.task_id  and au.action = '"
						+ MurphyConstant.INPROGRESS + "' )as inProgrssTime, te.completed_at as completed "
						+ " FROM TM_TASK_EVNTS AS te  LEFT OUTER JOIN  TM_TASK_OWNER AS tw ON te.TASK_ID = tw.TASK_ID "
						+ " where ";

				if (checkRequiredDate(fromDate)) {
					String allStatusQuery = "te.STATUS not in ('" + MurphyConstant.RETURN + "','"
							+ MurphyConstant.REJECTED + "', '" + MurphyConstant.REVOKED + "','"
							+ MurphyConstant.CANCELLED + "') and te.origin not in ('" + MurphyConstant.INVESTIGATON
							+ "','" + MurphyConstant.INQUIRY + "') AND (to_date(te.COMPLETED_AT) in ('" + date
							+ "') OR to_date(start_time) in ('" + date + "')))" + " where TASK_OWNER in (" + userId
							+ ") and start_time is not null ";
					newQuery = newQuery + allStatusQuery + " order by TASK_OWNER " + orderBy + ",start_time,tier asc";
				} else {
					String inProgAssignedQuery = " (te.STATUS not in ('" + MurphyConstant.RETURN + "','"
							+ MurphyConstant.REJECTED + "', '" + MurphyConstant.REVOKED + "','"
							+ MurphyConstant.COMPLETE + "','" + MurphyConstant.CANCELLED + "') OR te.STATUS IN ('"
							+ MurphyConstant.INPROGRESS + "','" + MurphyConstant.ASSIGN + "'"
							+ ")) and te.origin not in ('" + MurphyConstant.INVESTIGATON + "','"
							+ MurphyConstant.INQUIRY + "') AND (to_date(start_time) in ('" + date + "')))"
							+ " where TASK_OWNER in (" + userId + ") and start_time is not null ";
					newQuery = newQuery + inProgAssignedQuery + " order by TASK_OWNER " + orderBy
							+ ",start_time,tier asc";
				}
				logger.error("[Murphy][TaskEventsDao][getDataForScheduling][query]" + newQuery);
				Query q = this.getSession().createSQLQuery(newQuery);
				List<Object[]> result = (List<Object[]>) q.list();

				if (!ServicesUtil.isEmpty(result)) {
					Map<String, Integer> locationMap = new HashMap<String, Integer>();
					returnList = new ArrayList<TaskSchedulingUserDto>();
					graphList = new ArrayList<TaskSchedulingGraphDto>();
					TaskSchedulingDto dto = null;
					TaskSchedulingGraphDto graphDto = null;
					TaskSchedulingUserDto userDto = null;
					for (Object[] obj : result) {
						dto = new TaskSchedulingDto();
						graphDto = new TaskSchedulingGraphDto();
						dto.setCreatedAtDisplay(ServicesUtil.isEmpty(obj[0]) ? null
								: ServicesUtil.convertFromZoneToZoneString(null, obj[0], MurphyConstant.UTC_ZONE,
										MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.DATE_DISPLAY_FORMAT));
						// dto.setPriority(ServicesUtil.isEmpty(obj[1]) ? 0 :
						// (Integer) obj[1]);
						dto.setLocation(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
						dto.setClassification(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
						dto.setSubClassification(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);

						dto.setEstResolveTime(ServicesUtil.isEmpty(obj[6]) ? 0 : ((Double) obj[6]).floatValue());
						dto.setTravelTime(ServicesUtil.isEmpty(obj[12]) ? 0 : (Math.round((Double) obj[12])));

						dto.setExpectedStartTime(ServicesUtil.isEmpty(obj[9]) ? null
								: ServicesUtil.convertFromZoneToZone(null, obj[9], MurphyConstant.UTC_ZONE,
										MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.DATE_DISPLAY_FORMAT));
						dto.setExpectedStartTimeInString(ServicesUtil.isEmpty(obj[9]) ? null
								: ServicesUtil.convertFromZoneToZoneString(null, obj[9], MurphyConstant.UTC_ZONE,
										MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.DATE_DISPLAY_FORMAT));
						dto.setExpectedEndTime(ServicesUtil.isEmpty(obj[10]) ? null
								: ServicesUtil.convertFromZoneToZone(null, obj[10], MurphyConstant.UTC_ZONE,
										MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.DATE_DISPLAY_FORMAT));
						dto.setExpectedEndTimeInString(ServicesUtil.isEmpty(obj[10]) ? null
								: ServicesUtil.convertFromZoneToZoneString(null, obj[10], MurphyConstant.UTC_ZONE,
										MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.DATE_DISPLAY_FORMAT));
						dto.setTaskId(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
						dto.setProcessId(
								ServicesUtil.isEmpty(dto.getTaskId()) ? null : getProcessIdFromTask(dto.getTaskId()));

						dto.setStatus(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
						dto.setTier(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
						dto.setCustomOffset(ServicesUtil.isEmpty(obj[15]) ? 0 : ((Double) obj[15]).floatValue());

						if (MurphyConstant.COMPLETE.equals(dto.getStatus())) {

							if (!ServicesUtil.isEmpty(obj[17])) {

								dto.setEndTime(ServicesUtil.isEmpty(obj[17]) ? null
										: ServicesUtil.convertFromZoneToZone(null, obj[17], MurphyConstant.UTC_ZONE,
												MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
												MurphyConstant.DATE_DISPLAY_FORMAT));
								dto.setEndTimeInString(ServicesUtil.isEmpty(obj[17]) ? null
										: ServicesUtil.convertFromZoneToZoneString(null, obj[17],
												MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,
												MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
							} else {
								dto.setEndTime(ServicesUtil.isEmpty(obj[19]) ? null
										: ServicesUtil.convertFromZoneToZone(null, obj[19], MurphyConstant.UTC_ZONE,
												MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
												MurphyConstant.DATE_DISPLAY_FORMAT));
								dto.setEndTimeInString(ServicesUtil.isEmpty(obj[19]) ? null
										: ServicesUtil.convertFromZoneToZoneString(null, obj[19],
												MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,
												MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
							}

							if (!ServicesUtil.isEmpty(obj[18])) {
								dto.setStartTime(ServicesUtil.isEmpty(obj[18]) ? null
										: ServicesUtil.convertFromZoneToZone(null, obj[18], MurphyConstant.UTC_ZONE,
												MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
												MurphyConstant.DATE_DISPLAY_FORMAT));
								dto.setStartTimeInString(ServicesUtil.isEmpty(obj[18]) ? null
										: ServicesUtil.convertFromZoneToZoneString(null, obj[18],
												MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,
												MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
							} else {
								dto.setStartTime(ServicesUtil.convertFromZoneToZone(
										ServicesUtil.getDateWithInterval(dto.getEndTime(),
												-((int) dto.getEstResolveTime()), MurphyConstant.MINUTES),
										"", MurphyConstant.UTC_ZONE, MurphyConstant.UTC_ZONE,
										MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
								dto.setStartTimeInString(ServicesUtil.convertFromZoneToZoneString(
										ServicesUtil.getDateWithInterval(dto.getEndTime(),
												-((int) dto.getEstResolveTime()), MurphyConstant.MINUTES),
										"", MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,
										MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
								System.err.println("testnew" + dto.getEndTime() + "endtime"
										+ (-((int) dto.getEstResolveTime())) + "estResovletime"
										+ ServicesUtil.getDateWithInterval(dto.getEndTime(),
												-((int) dto.getEstResolveTime()), MurphyConstant.MINUTES)
										+ "------" + dto.getEndTime());
							}

						} else {
							dto.setStartTime(ServicesUtil.isEmpty(obj[9]) ? null
									: ServicesUtil.convertFromZoneToZone(null, obj[9], MurphyConstant.UTC_ZONE,
											MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
											MurphyConstant.DATE_DISPLAY_FORMAT));
							dto.setStartTimeInString(ServicesUtil.isEmpty(obj[9]) ? null
									: ServicesUtil.convertFromZoneToZoneString(null, obj[9], MurphyConstant.UTC_ZONE,
											MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
											MurphyConstant.DATE_DISPLAY_FORMAT));
							dto.setEndTime(ServicesUtil.isEmpty(obj[10]) ? null
									: ServicesUtil.convertFromZoneToZone(null, obj[10], MurphyConstant.UTC_ZONE,
											MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
											MurphyConstant.DATE_DISPLAY_FORMAT));
							dto.setEndTimeInString(ServicesUtil.isEmpty(obj[10]) ? null
									: ServicesUtil.convertFromZoneToZoneString(null, obj[10], MurphyConstant.UTC_ZONE,
											MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
											MurphyConstant.DATE_DISPLAY_FORMAT));
						}
						dto.setTotalEstTime(ServicesUtil.isEmpty(dto.getTravelTime()) || (dto.getTravelTime() == -1)
								? (ServicesUtil.isEmpty(dto.getCustomOffset()) ? dto.getEstResolveTime()
										: dto.getEstResolveTime() + dto.getCustomOffset())
								: ServicesUtil.isEmpty(dto.getCustomOffset())
										? dto.getEstResolveTime() + dto.getTravelTime()
										: dto.getEstResolveTime() + dto.getTravelTime() + dto.getCustomOffset());

						graphDto.setTimeInHrs(dto.getTotalEstTime());
						graphDto.setUserName(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
						graphDto.setTaskSubject(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
						graphList.add(graphDto);

						if (locationMap.containsKey((String) obj[2])) {
							returnList.get(locationMap.get((String) obj[2])).getAppointments().add(dto);
							userDto.setTotalWorkTime(dto.getTotalEstTime() + userDto.getTotalWorkTime());
							if (userDto.getAvailableTime() > 0) {
								String taskPosition = ServicesUtil.checkIfTimeExistsInInterval(dto.getStartTime(),
										new Date(), dto.getEndTime());
								if (MurphyConstant.AFTER.equals(taskPosition)) {
									// System.err.println("In AFTER");
									userDto.setAvailableTime(userDto.getAvailableTime() - dto.getEstResolveTime());
								} else if (MurphyConstant.CURRENT.equals(taskPosition)
										&& ServicesUtil.isEmpty(dto.getEndTime())) {
									// System.err.println("In CURRENT");
									userDto.setAvailableTime(userDto.getAvailableTime() - ServicesUtil
											.getDiffBtwnTwoDates(dto.getEndTime(), new Date(), MurphyConstant.MINUTES));
								}
							} else {
								userDto.setAvailableTime(0);
							}
						} else {
							userDto = new TaskSchedulingUserDto();
							userDto.setTaskOwner(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
							userDto.setName(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
							userDto.setAppointments(new ArrayList<TaskSchedulingDto>());
							userDto.setpId(ServicesUtil.isEmpty(obj[16]) ? null : (String) obj[16]);

							userDto.getAppointments().add(dto);

							if (ServicesUtil
									.checkIfTimeExistsInInterval(
											ServicesUtil.convertFromZoneToZone(null, "07:00:00",
													MurphyConstant.CST_ZONE, MurphyConstant.CST_ZONE,
													MurphyConstant.HRSMINSEC_FORMAT, MurphyConstant.HRSMINSEC_FORMAT),
											ServicesUtil.convertFromZoneToZone(null, obj[0],
													MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,
													MurphyConstant.DATE_DB_FORMATE, MurphyConstant.HRSMINSEC_FORMAT),
											ServicesUtil.convertFromZoneToZone(null, "19:00:00",
													MurphyConstant.CST_ZONE, MurphyConstant.CST_ZONE,
													MurphyConstant.HRSMINSEC_FORMAT, MurphyConstant.HRSMINSEC_FORMAT))
									.equals(MurphyConstant.CURRENT)) {
								userDto.setShift("7am - 7pm");
								userDto.setAvailableTime(ServicesUtil.fetchAvailableTime(19, timeZoneOffSet));
							} else {
								userDto.setShift("7pm - 7am");
								userDto.setAvailableTime(ServicesUtil.fetchAvailableTime(7, timeZoneOffSet));
							}
							// System.err.println("setAvailableTime" +
							// userDto.getAvailableTime());
							userDto.setTotalWorkTime(dto.getTotalEstTime());
							locationMap.put((String) obj[2], returnList.size());
							returnList.add(userDto);
						}
					}

					responseDto.setTaskList(returnList);
					// responseDto.setGraphDtos(graphList);
					response.setMessage(MurphyConstant.READ_SUCCESS);
				} else {
					response.setMessage(MurphyConstant.NO_RESULT);
				}
			}
			response.setStatus(MurphyConstant.SUCCESS);
			response.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (Exception e) {
			logger.error(
					"[Murphy][TaskEventsDao][getDataForScheduling][error] [e]" + e + "e.getMessage" + e.getMessage());
			e.printStackTrace();
		}

		responseDto.setResponseMessage(response);
		return responseDto;

	}

	public static String getStringFromList(List<TaskOwnersDto> stringList) {
		if (!ServicesUtil.isEmpty(stringList)) {
			String returnString = "";
			for (TaskOwnersDto st : stringList) {
				returnString = returnString + "'" + st.getTaskOwner().trim() + "',";
			}
			return returnString.substring(0, returnString.length() - 1);
		} else
			return null;
	}

	@SuppressWarnings("unchecked")
	public String getProcessIdFromTask(String taskId) {
		try {
			String queryString = "select distinct(te1.process_id) from tm_task_evnts te1 where te1.task_id='" + taskId
					+ "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			System.err.println("[Murphy][LocationHierarchyDao][getMuwiByLocation][error]" + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<NDVTaskListDto> getAllNDVTasks(String userRole, String userType) {

		List<NDVTaskListDto> listDtos = null;

		if (!ServicesUtil.isEmpty(userRole)) {
			List<String> userTypeArray = new ArrayList<String>();
			if (!ServicesUtil.isEmpty(userType) && userType.contains(",")) {
				String[] types = userType.split(",");
				for (String role : types) {
					userTypeArray.add(role);
				}
			} else {
				userTypeArray.add(userType);
			}
			if (userType.contains("ENG")) {
				userType = userType.replace("ENG", "ENGINEERING");
			}
			String resolvedQuery = "", assignedQuery = "";
			String queryString = " SELECT distinct pe.PROCESS_ID AS PROCESS_ID, pe.REQUEST_ID AS REQUEST_ID,pe.STARTED_AT AS STARTED_AT, "
					+ "( SELECT i.INS_VALUE  FROM TM_ATTR_INSTS AS i  WHERE ATTR_TEMP_ID = 'NDV1' AND pe.PROCESS_ID = i.TASK_ID  ) AS WELLNAME, "
					+ "( SELECT i.INS_VALUE  FROM TM_ATTR_INSTS AS i  WHERE ATTR_TEMP_ID = 'NDV5' AND pe.PROCESS_ID = i.TASK_ID  ) AS FIELD ,"
					+ "( SELECT i.INS_VALUE  FROM TM_ATTR_INSTS AS i  WHERE ATTR_TEMP_ID = 'NDV6' AND pe.PROCESS_ID = i.TASK_ID  ) AS VARTOFRCTBOED ,"
					+ "( SELECT i.INS_VALUE  FROM TM_ATTR_INSTS AS i  WHERE ATTR_TEMP_ID = 'NDV7' AND pe.PROCESS_ID = i.TASK_ID  ) AS PERVARTOFRCTBOED ,"
					+ " ins.ins_value as  step , "
					// + "(select max(inst.ins_value) from tm_attr_insts inst
					// where te.task_id = inst.task_id and inst.ATTR_TEMP_ID =
					// 'NDO2' ) as step ,"
					+ "  te.STATUS AS STATUS ,te.STATUS as task_status FROM TM_TASK_EVNTS te,  TM_PROC_EVNTS AS  pe , tm_attr_insts ins  where pe.status = '"
					+ MurphyConstant.INPROGRESS + "' and pe.process_id = te.process_id " + " and te.origin = '"
					+ MurphyConstant.INVESTIGATON + "'  and pe.user_group in ("
					+ ServicesUtil.getStringForInQuery(userRole) + ") and ins.task_id = te.task_id "
					+ " and ins.ATTR_TEMP_ID = 'NDO2' " + " and te.status in (";

			assignedQuery = queryString + "'" + MurphyConstant.ASSIGN + "','" + MurphyConstant.INPROGRESS
					+ "' ) and ins.ins_value in (" + ServicesUtil.getStringForInQuery(userType) + " ) ";

			// String taskOwnerList = "";

			// List<GroupsUserDto> users =
			// userMappingDao.getUsersBasedOnPOTRole(userRole, userType);
			// if (!ServicesUtil.isEmpty(users)) {
			// for (GroupsUserDto user : users) {
			// taskOwnerList += "'" + user.getUserId() + "',";
			// }
			// }
			// groupDao.getAllUserIdOfGroup(userRole,userTypeArray);
			// if (!ServicesUtil.isEmpty(taskOwnerList)) {
			// taskOwnerList = taskOwnerList.substring(0, taskOwnerList.length()
			// - 1);
			// alsEngQuery = alsEngQuery + " and tw.task_owner in (" +
			// taskOwnerList + ")";
			// }

			if (userTypeArray.contains(MurphyConstant.USER_TYPE_POT)) {
				resolvedQuery = queryString + "'" + MurphyConstant.RESOLVE + "')";
				queryString = resolvedQuery + " UNION " + assignedQuery;

				// if (userTypeArray.contains(MurphyConstant.USER_TYPE_ALS) ||
				// userTypeArray.contains(MurphyConstant.USER_TYPE_ENG)) {
				// queryString = queryString + " UNION " + assignedQuery;
				// }
			} else {
				queryString = assignedQuery;
			}

			System.err.println("[Murphy][TaskEventsDao][getAllNDVTasks][queryString]" + queryString + "[userRole]"
					+ userRole + "[userType]" + userType + "[userTypeArray]" + userTypeArray);
			Query q = this.getSession().createSQLQuery(queryString);

			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				listDtos = new ArrayList<NDVTaskListDto>();
				NDVTaskListDto dto = null;
				for (Object[] obj : response) {
					dto = new NDVTaskListDto();
					dto.setProcessId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setRequestId(ServicesUtil.isEmpty(obj[1]) ? null : ((BigInteger) obj[1]).longValue());
					dto.setCreatedAt(ServicesUtil.isEmpty(obj[2]) ? null
							: ServicesUtil.convertFromZoneToZone(null, obj[2], MurphyConstant.UTC_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setCreatedAtInString(ServicesUtil.isEmpty(obj[2]) ? null
							: ServicesUtil.convertFromZoneToZoneString(null, obj[2], MurphyConstant.UTC_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));
					dto.setWellName(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setField(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setVarToFcstBOED(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					dto.setPerVarToFcstBOED(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
					dto.setStatus(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
					if (!ServicesUtil.isEmpty(obj[9]) && (MurphyConstant.RESOLVE.equals((String) obj[9]))) {
						dto.setMileStoneStep(MurphyConstant.USER_TYPE_POT);
						dto.setStatus(MurphyConstant.INPROGRESS);
					} else {
						dto.setMileStoneStep(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
					}
					listDtos.add(dto);
				}

			}
		}
		return listDtos;
	}

	public int getTaskCount(String userType, String userRole) {
		if (userType.equals(MurphyConstant.USER_TYPE_ENG)) {
			userType = "ENGINEERING";
		}
		String queryString = "SELECT count(distinct(pe.process_id)) FROM TM_TASK_EVNTS AS te, TM_PROC_EVNTS AS pe, tm_attr_insts inst  WHERE "
				+ " pe.process_id = te.process_id and te.task_id = inst.task_id and inst.ATTR_TEMP_ID = 'NDO2'"
				+ " AND te.origin = '" + MurphyConstant.INVESTIGATON + "' AND pe.user_group in ("
				+ ServicesUtil.getStringForInQuery(userRole) + ") ";
		if (MurphyConstant.USER_TYPE_POT.equals(userType)) {

			queryString = queryString + " and (( te.status IN  ('" + MurphyConstant.INPROGRESS + "', '"
					+ MurphyConstant.ASSIGN + "') " + " and inst.ins_value  = '" + userType + "') or (te.status IN ('"
					+ MurphyConstant.RESOLVE + "')))";
			// queryString = queryString + " ('" + MurphyConstant.RESOLVE + "')
			// ";
		} else {
			queryString = queryString + " and te.status IN  ('" + MurphyConstant.INPROGRESS + "', '"
					+ MurphyConstant.ASSIGN + "') " + " and inst.ins_value  = '" + userType + "'";
		}

		// System.err.println("[Murphy][TaskEventsDao][getTaskCount][queryString]"+queryString);
		Query q = this.getSession().createSQLQuery(queryString);
		return ((BigInteger) q.uniqueResult()).intValue();

	}

	public String updateTaskStatusResolveToComplete(String processId) {
		String updateQuery = "update tm_task_evnts set status='" + MurphyConstant.COMPLETE
				+ "' , COMPLETED_AT =  TO_TIMESTAMP('"
				+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
				+ "', 'yyyy-mm-dd hh24:mi:ss')  where process_id='" + processId + "'" + " and status='"
				+ MurphyConstant.RESOLVE + "'";

		Query q = this.getSession().createSQLQuery(updateQuery);
		// System.err.println("[Murphy][TaskEventsDao][updateTaskStatusResolveToComplete][updateQuery]"
		// + updateQuery);

		Integer result = q.executeUpdate();
		if (result > 0)
			return MurphyConstant.SUCCESS;
		else
			return MurphyConstant.NO_RECORD;

	}

	public String updateTaskStatusToComplete(String processId) {
		String updateQuery = "update tm_task_evnts set status='" + MurphyConstant.COMPLETE
				+ "' , COMPLETED_AT =  TO_TIMESTAMP('"
				+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
				+ "', 'yyyy-mm-dd hh24:mi:ss')  where process_id='" + processId + "'";

		Query q = this.getSession().createSQLQuery(updateQuery);
		// System.err.println("[Murphy][TaskEventsDao][updateTaskStatusResolveToComplete][updateQuery]"
		// + updateQuery);

		Integer result = q.executeUpdate();
		if (result > 0)
			return MurphyConstant.SUCCESS;
		else
			return MurphyConstant.NO_RECORD;

	}

	public int assignedTasksToUserCount(String userId) {
		String updateQuery = "SELECT COUNT(te.TASK_ID) FROM  TM_TASK_EVNTS te left outer join TM_TASK_OWNER tw on tw.TASK_ID = te.TASK_ID where te.STATUS IN ('"
				+ MurphyConstant.ASSIGN + "') " + "and tw.TASK_OWNER ='" + userId + "'  AND te.ORIGIN IN('"
				+ MurphyConstant.DISPATCH_ORIGIN + "')";

		Query q = this.getSession().createSQLQuery(updateQuery);
		// System.err.println("[Murphy][TaskEventsDao][updateTaskStatusResolveToComplete][updateQuery]"
		// + updateQuery);

		BigInteger result = (BigInteger) q.uniqueResult();
		return result.intValue();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<UserTaskCount> assignedTasksToUsersCount(List<String> pIdList) {
		Set<String> pIdListSet = new HashSet<String>();
		ArrayList<UserTaskCount> responseDto = new ArrayList<UserTaskCount>();
		for (String pId : pIdList)
			pIdListSet.add(pId);
		String userIdListQuery = com.murphy.integration.util.ServicesUtil.getQueryString(pIdListSet, "tu.P_ID",
				new ArrayList<String>());
		String selectQuery = "SELECT COUNT(te.TASK_ID),tu.USER_LOGIN_NAME from TM_TASK_OWNER tw "
				+ "inner join TM_USER_IDP_MAPPING tu on tu.P_ID = tw.P_ID"
				+ " left outer join TM_TASK_EVNTS te  on tw.TASK_ID = te.TASK_ID  where te.STATUS IN ('"
				+ MurphyConstant.ASSIGN + "') " + "and " + userIdListQuery + "  AND te.ORIGIN IN('"
				+ MurphyConstant.DISPATCH_ORIGIN + "') GROUP BY tu.USER_LOGIN_NAME";

		Query q = this.getSession().createSQLQuery(selectQuery);
		// System.err.println("[Murphy][TaskEventsDao][updateTaskStatusResolveToComplete][updateQuery]"
		// + updateQuery);

		List<Object[]> response = (List<Object[]>) q.list();
		if (!ServicesUtil.isEmpty(response)) {
			responseDto = new ArrayList<UserTaskCount>();
			UserTaskCount uiResponseDto = null;
			for (Object[] obj : response) {
				uiResponseDto = new UserTaskCount();
				uiResponseDto.setUserName(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				BigInteger taskCount = ServicesUtil.isEmpty(obj[0]) ? BigInteger.ZERO : (BigInteger) obj[0];
				uiResponseDto.setCount(taskCount.intValue());
				responseDto.add(uiResponseDto);
			}
		}
		return responseDto;
	}

	@SuppressWarnings("unchecked")
	public List<TaskListDto> getLatestTasksForLoc(String locationCode, String location) {
		List<TaskListDto> responseDto = null;
		try {
			String rootCause = "select max(rt.root_cause) from tm_rootcause_insts rt where rt.task_Id = te.task_id and rt.action = te.status "
					+ " and rt.created_At = (Select max(rtx.created_At)  from tm_rootcause_insts rtx where rtx.task_id = te.task_id  and rtx.action = te.status )";
			String queryString = "Select te.TASK_ID AS TASK_ID,te.TSK_SUBJECT AS SUBJECT,"
					+ "te.STATUS AS STATUS ,te.PROCESS_ID AS PROCESS_ID ,pe.STARTED_BY_DISP AS STARTED_BY_DISP"
					+ ",pe.REQUEST_ID AS REQUEST_ID, te.CREATED_AT AS CT, "
					+ " ( SELECT max(i.INS_VALUE) FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID in ('NDO3','1234') AND te.TASK_ID = i.TASK_ID  ) AS ASSIGNED_USER, "
					+ " te.parent_origin As ORIGIN ," + " (" + rootCause + " ) as rootCause"
					+ " from  TM_PROC_EVNTS pe left outer join  TM_TASK_EVNTS te on pe.PROCESS_ID = te.PROCESS_ID "
					+ " left outer join TM_TASK_OWNER tw on tw.TASK_ID = te.TASK_ID where pe.LOC_CODE = '"
					+ locationCode + "' " + " and te.origin = '" + MurphyConstant.DISPATCH_ORIGIN + "' "
					+ "and te.status not in ('" + MurphyConstant.DRAFT + "','" + MurphyConstant.NEW_TASK + "','"
					+ MurphyConstant.REJECTED + "','" + MurphyConstant.CANCELLED + "')"
					+ " order by te.created_at desc limit 10";

			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				TaskListDto dto = null;
				responseDto = new ArrayList<TaskListDto>();
				for (Object[] obj : response) {
					dto = new TaskListDto();
					dto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setDescription(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					dto.setStatus(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					dto.setProcessId(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setCreatedBy(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setRequestId(ServicesUtil.isEmpty(obj[5]) ? null : ((BigInteger) obj[5]).longValue());

					dto.setCreatedAt(ServicesUtil.isEmpty(obj[6]) ? null
							: ServicesUtil.convertFromZoneToZone(null, obj[6], MurphyConstant.UTC_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT));

					Date createdAtInString = ServicesUtil.isEmpty(obj[6]) ? null : (Date) obj[6];
					dto.setCreatedAtInString(ServicesUtil.isEmpty(createdAtInString) ? null
							: String.valueOf(createdAtInString.getTime()));

					/*
					 * dto.setCreatedAtInString(ServicesUtil.isEmpty(obj[6]) ?
					 * null : ServicesUtil.convertFromZoneToZoneString(null,
					 * obj[6], MurphyConstant.UTC_ZONE, MurphyConstant.IST_ZONE,
					 * MurphyConstant.DATE_DB_FORMATE,
					 * MurphyConstant.DATE_DISPLAY_FORMAT));
					 */

					dto.setTaskOwner(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
					dto.setOrigin(ServicesUtil.isEmpty(obj[8]) ? null
							: MurphyConstant.VARIANCE.equals((String) obj[8]) ? "DOP" : (String) obj[8]);
					dto.setRootCause(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);

					responseDto.add(dto);
				}
			}
		} catch (Exception e) {
			System.err.println("[Murphy][TaskEventsDao][getMuwiByLocation][error]" + e.getMessage());
		}
		return responseDto;
	}

	@SuppressWarnings("unchecked")
	public String getFLSOPforSubClassification(String classification, String subClassification) {
		String responseString = "";
		try {
			String queryString = "select flsop from tm_flsop where CLASSIFICATION = '" + classification
					+ "' and SUB_CLASSIFICATION = '" + subClassification + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				responseString = response.get(0);
			}
		} catch (Exception e) {
			System.err.println("[Murphy][TaskEventsDao][getFLSOPforSubClassification][error]" + e.getMessage());
		}
		return responseString;
	}

	public String updateInquiryStatus(String processId, String status) {
		String updateQuery = "update tm_task_evnts set status='" + MurphyConstant.RESOLVE
				+ "' , COMPLETED_AT =  TO_TIMESTAMP('"
				+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
				+ "', 'yyyy-mm-dd hh24:mi:ss')  where process_id='" + processId + "' and ORIGIN='"
				+ MurphyConstant.INQUIRY + "'";

		Query q = this.getSession().createSQLQuery(updateQuery);
		System.err.println("[Murphy][TaskEventsDao][updateTaskStatusResolveToComplete][updateQuery]" + updateQuery);

		Integer result = q.executeUpdate();
		if (result > 0)
			return MurphyConstant.SUCCESS;
		else
			return MurphyConstant.NO_RECORD;

	}

	@SuppressWarnings("unchecked")
	public String getTaskCreator(String processId) {
		String responseString = "";
		String selectQuery = "SELECT STARTED_BY FROM TM_PROC_EVNTS WHERE PROCESS_ID='" + processId + "'";

		Query q = this.getSession().createSQLQuery(selectQuery);
		// System.err.println("[Murphy][TaskEventsDao][getTaskCreator][Query]" +
		// updateQuery);

		List<String> response = (List<String>) q.list();
		if (!ServicesUtil.isEmpty(response)) {
			responseString = response.get(0);
		}
		return responseString;
	}

	@SuppressWarnings("unchecked")
	public String getTaskCreatorName(String userId, String userRole) {
		String responseString = "";
		String selectQuery = "";
		try {
			if (userRole == null) {
				selectQuery = "SELECT USER_FIRST_NAME,USER_LAST_NAME FROM TM_USER_IDP_MAPPING WHERE USER_EMAIL='"
						+ userId + "' AND USER_ROLE IN (" + MurphyConstant.EMAIL_USERS + ")";
			} else {
				selectQuery = "SELECT USER_FIRST_NAME,USER_LAST_NAME FROM TM_USER_IDP_MAPPING WHERE USER_EMAIL='"
						+ userId + "' AND USER_ROLE IN (" + userRole + ")";
			}

			Query q = this.getSession().createSQLQuery(selectQuery);
			System.err.println("[Murphy][TaskEventsDao][getTaskCreator][Query]" + selectQuery);

			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				for (Object[] obj : response) {
					String frstName = (String) obj[0];
					String lastName = (String) obj[1];
					responseString = frstName + " " + lastName;
				}
			}
		} catch (Exception ex) {
			logger.error("Exception while Fetching User Details" + ex.getMessage());
		}
		return responseString;
	}

	@SuppressWarnings("unchecked")
	public String getTasksForProcess(String processId) {
		String commaSeparatedTaskId = "";
		try {
			String selectQuery = "SELECT TASK_ID FROM TM_TASK_EVNTS WHERE PROCESS_ID IN ('" + processId + "')";
			Query q = this.getSession().createSQLQuery(selectQuery);
			logger.error("[Murphy][TaskEventsDao][getTaskForProcess][Quey]" + selectQuery);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				StringJoiner joiner = new StringJoiner("','");
				for (String pId : response)
					joiner.add(pId);
				commaSeparatedTaskId = joiner.toString();
				logger.error("[Murphy][TaskEventsDao][getTasksForProcess][response]" + response
						+ " [commaSeparatedTaskId] " + commaSeparatedTaskId);
				// return response;
			}

		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][getTasksForProcess][error]" + e.getMessage());
		}
		return commaSeparatedTaskId;
	}

	@SuppressWarnings("unchecked")
	public String getLatestTaskForProcess(String processId) {
		String taskId = null;
		String selectQuery = "SELECT TASKID FROM(Select te.task_id As TASKID,ROW_NUMBER() OVER (PARTITION BY te.PROCESS_ID ORDER BY TE.CREATED_AT DESC) As RN from TM_TASK_EVNTS te where te.Process_Id='"
				+ processId + "' )WHERe RN=1";
		Query q = this.getSession().createSQLQuery(selectQuery);
		// System.err.println("[Murphy][TaskEventsDao][getTaskForProcess][Quey]"
		// + updateQuery);
		List<String> response = (List<String>) q.list();
		if (!ServicesUtil.isEmpty(response)) {
			taskId = response.get(0);
		}
		return taskId;
	}

	public ResponseMessage updateTaskForPigging(TaskEventsDto dto) {
		ResponseMessage res = new ResponseMessage();
		res.setStatus(MurphyConstant.FAILURE);
		try {

			// String hql="update ProcessEventsDo pe set pe.startedBy =
			// :createdBy,pe.startedByDisplayName= :startedByDisplayName where
			// pe.processId= :processId";
			String qry = "UPDATE TM_PROC_EVNTS SET STARTED_BY='" + dto.getCreatedBy() + "',STARTED_BY_DISP='"
					+ dto.getCreatedByDisplay() + "' WHERE PROCESS_ID='" + dto.getProcessId() + "'";
			// Query query = getSession().createQuery(hql);
			Query query = this.getSession().createSQLQuery(qry);
			// query.setParameter("createdBy",dto.getCreatedBy());
			// query.setParameter("startedByDisplayName",dto.getCreatedByDisplay());
			// query.setParameter("processId", dto.getProcessId());
			query.executeUpdate();
			res.setStatus(MurphyConstant.SUCCESS);

		} catch (Exception e) {
			System.err.println("[Murphy][TaskEventsDao][updateTaskForPigging][error]" + e.getMessage());
		}
		return res;
	}

	// Adding for incident INC0077951
	@SuppressWarnings("unchecked")
	public String processIdHavingPrevTask(String processId) {
		String commaSeparatedId = "";
		List<String> response = null;
		try {
			String queryString = "select te.PROCESS_ID from TM_TASK_EVNTS te where te.PREV_TASK IN ('" + processId
					+ "') " + "AND te.PARENT_ORIGIN = '" + MurphyConstant.INQUIRY + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			response = q.list();
			if (!ServicesUtil.isEmpty(response)) {
				StringJoiner joiner = new StringJoiner("','");
				for (String pId : response)
					joiner.add(pId);
				commaSeparatedId = joiner.toString();
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][processIdHavingPrevTask][error]" + e.getMessage());
		}
		return commaSeparatedId;
	}

	// Enhancement to check single task per location
	@SuppressWarnings("unchecked")
	public List<TaskListDto> checkForLocation(String locationCode) {
		List<TaskListDto> taskListDto = null;
		TaskListDto dto = null;
		try {
			String commonQueryStartString = "", assignerTasksQuery = "", commonQueryMidString = "", queryString = "";

			String latestCommentQuery = "(SELECT max(i.CREATED_AT) FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID)";
			commonQueryStartString = "Select te.TASK_ID AS TASK_ID,te.TSK_SUBJECT AS SUBJECT," + latestCommentQuery
					+ " AS CREATED_AT,"
					+ " ( SELECT i.MESSAGE FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID and created_at = "
					+ latestCommentQuery + ") AS MESSAGE, "
					+ " (SELECT i.USER_DISPLAY_NAME FROM TM_COLLABORATION AS i WHERE i.MESSAGE is not null AND te.TASK_ID = i.TASK_ID and created_at = "
					+ latestCommentQuery + ") AS USER_DISPLAY_NAME,"
					+ "	te.STATUS AS STATUS ,te.PROCESS_ID AS PROCESS_ID ,pe.STARTED_BY_DISP AS STARTED_BY_DISP,pe.REQUEST_ID AS REQUEST_ID,"
					+ " te.CREATED_AT AS CT,"
					+ " ( SELECT max(i.INS_VALUE) FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID in ('NDO3','1234','INQ03') AND te.TASK_ID = i.TASK_ID  ) AS ASSIGNED_USER,"
					+ " te.UPDATED_AT AS UPDATED_AT ,te.CREATED_AT AS TSK_CREATED_AT ,"
					+ "( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID in('123','INQ01') AND te.TASK_ID = i.TASK_ID  ) AS LOCATION,"
					+ " te.origin As ORIGIN , pe.LOC_CODE as loc_code ,pe.STARTED_BY as started_by, "
					+ " pe.user_group as user_group,te.parent_origin as parent_origin, pe.extra_role as extra_role ";

			commonQueryMidString = " from TM_PROC_EVNTS pe left outer join  TM_TASK_EVNTS te on pe.PROCESS_ID = te.PROCESS_ID ";
			assignerTasksQuery = commonQueryStartString + " , null  as st " + commonQueryMidString
					+ " where te.STATUS not in ('COMPLETED', 'REJECTED','CANCELLED') AND te.ORIGIN NOT IN ('Inquiry','Investigation') AND te.PARENT_ORIGIN NOT IN ('OBX','Pigging') AND"
					+ " pe.LOC_CODE ='" + locationCode.trim() + "'";

			queryString = "Select distinct TASK_ID , SUBJECT ,CREATED_AT , MESSAGE , USER_DISPLAY_NAME , STATUS , PROCESS_ID , STARTED_BY_DISP , REQUEST_ID , CT ,   "
					+ "ASSIGNED_USER , UPDATED_AT , TSK_CREATED_AT , LOCATION , ORIGIN , loc_code , started_by , user_group,parent_origin, EXTRA_ROLE  "
					+ "from ( ";

			queryString = queryString + assignerTasksQuery + " ) ORDER BY 12 desc ";

			logger.error("[TaskEventsDao][checkForLocation][queryString] " + queryString);

			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> result = q.list();
			if (!ServicesUtil.isEmpty(result)) {
				taskListDto = new ArrayList<TaskListDto>();
				for (Object[] obj : result) {
					dto = new TaskListDto();
					taskListDto.add(taskDetails(obj, dto));
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][checkForLocation][error] " + e.getMessage());
		}
		return taskListDto;
	}

	// Changes created for single dispatch per location
	private TaskListDto taskDetails(Object[] obj, TaskListDto dto) {
		dto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
		dto.setDescription(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
		if (!ServicesUtil.isEmpty(obj[3])) {
			Date commentedAt = ServicesUtil.isEmpty(obj[2]) ? null : (Date) obj[2];
			dto.setCommentedAt(ServicesUtil.isEmpty(commentedAt) ? null : String.valueOf(commentedAt.getTime()));
			dto.setLatestComment(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
			dto.setCommentedByDisplay(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
		}
		dto.setStatus(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
		dto.setProcessId(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
		dto.setCreatedBy(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
		dto.setRequestId(ServicesUtil.isEmpty(obj[8]) ? null : ((BigInteger) obj[8]).longValue());
		dto.setTaskOwner(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);

		Date createdAtinString = ServicesUtil.isEmpty(obj[12]) ? null : (Date) obj[12];
		dto.setCreatedAtInString(
				ServicesUtil.isEmpty(createdAtinString) ? null : String.valueOf(createdAtinString.getTime()));
		dto.setCreatedAt(ServicesUtil.isEmpty(createdAtinString) ? null : createdAtinString);

		dto.setLocation(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
		dto.setOrigin(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
		dto.setLocationCode(ServicesUtil.isEmpty(obj[15]) ? null : (String) obj[15]);
		dto.setMuwiId(ServicesUtil.isEmpty(obj[15]) ? null : locDao.getMuwiByLocationCode((String) obj[15]));
		dto.setCreatedByEmailId(ServicesUtil.isEmpty(obj[16]) ? null : (String) obj[16]);
		dto.setCreatorGroupId(ServicesUtil.isEmpty(obj[17]) ? null : (String) obj[17]);
		dto.setParentOrigin(ServicesUtil.isEmpty(obj[18]) ? null : (String) obj[18]);
		dto.setCreatedOnGroup(ServicesUtil.isEmpty(obj[19]) ? null : (String) obj[19]);
		dto.setHasDispatch(checkIfDispatchExists(dto.getProcessId()));

		return dto;
	}

	// Fetching field location code for Filter location selected
	@SuppressWarnings("unchecked")
	public String fieldFilterTool(String locText) {
		String locCode = "";
		try {
			String queryString = "select CONFIG_DESC_VALUE from tm_config_values where CONFIG_ID in ('"
					+ locText.toUpperCase().trim() + "')";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> loc = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(loc)) {
				locCode = loc.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][fieldFilterTool][error]" + e.getMessage());
		}
		return locCode;
	}

	@SuppressWarnings("unchecked")
	public String autoChangeStatus() {
		String response = null;
		try {
			String query = "SELECT TASK_ID,PROCESS_ID,STATUS FROM TM_TASK_EVNTS WHERE ORIGIN='"
					+ MurphyConstant.DISPATCH_TASK
					+ "'AND PARENT_ORIGIN IN('Alarm','Custom') AND PARENT_ORIGIN NOT IN('Variance')" + "AND STATUS IN('"
					+ MurphyConstant.RESOLVE + "')"
					+ "AND To_Date(CREATED_AT)<=(SELECT ADD_DAYS(CURRENT_DATE,-14)from dummy);";
			Query q = this.getSession().createSQLQuery(query);
			logger.error("[Murphy][TaskEventsDao][autoChangeStatus][query]" + query);
			List<Object[]> responseList = q.list();

			// For Fetching the Cancelled WorkBench OBX system generated task
			String Obxquery = "SELECT TASK_ID,PROCESS_ID,STATUS FROM TM_TASK_EVNTS WHERE ORIGIN='"
					+ MurphyConstant.DISPATCH_TASK
					+ "'AND PARENT_ORIGIN IN('OBX') AND PARENT_ORIGIN NOT IN('Variance') AND TASK_TYPE='SYSTEM'"
					+ "AND STATUS IN('" + MurphyConstant.CANCELLED + "')"
					+ "AND To_Date(CREATED_AT)<=(SELECT ADD_DAYS(CURRENT_DATE,-14)from dummy);";
			Query obxQ = this.getSession().createSQLQuery(Obxquery);
			logger.error("[Murphy][TaskEventsDao][OBX Auto Change][Obxquery]" + Obxquery);
			List<Object[]> responseList1 = obxQ.list();

			// Adding both the list
			responseList.addAll(responseList1);

			Date currentDdate = new Date();
			Boolean isSchdeulerRun = true;
			if (!ServicesUtil.isEmpty(responseList)) {
				UpdateRequestDto dto = null;
				for (Object[] obj : responseList) {
					dto = new UpdateRequestDto();
					dto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setProcessId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					dto.setStatus(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);

					response = updateTaskEventStatus(dto.getTaskId(), dto.getProcessId(), "System", "System",
							MurphyConstant.COMPLETE, currentDdate, "", isSchdeulerRun, null);
				}

			}
			response = MurphyConstant.SUCCESS;

		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][autoChangeStatus][error]" + e.getMessage());
		}
		return response;
	}

	// GET ALL TASKS FOR IOP ADMIN
	@SuppressWarnings("unchecked")
	public TaskListResponseDto getAllTasksForAdmin(int page, int pageSize, String taskType, String status,
			String parentOrigin) {
		List<TaskListDto> responseListDto = new ArrayList<TaskListDto>();
		TaskListResponseDto taskListResponseDto = new TaskListResponseDto();
		try {
			int countResult = 0;
			String inProgressTimeQuery = " select max(at.created_at) from tm_audit_trail at where te.task_id = at.task_id "
					+ "group by at.task_id,at.action having at.action = 'IN PROGRESS'";
			String resolvedTimeQuery = " select max(at.created_at) from tm_audit_trail at "
					+ "where te.task_id = at.task_id group by at.task_id,at.action having at.action = 'RESOLVED'";

			String rootCause = "select max(rt.root_cause) from tm_rootcause_insts rt where rt.task_Id = te.task_id and rt.action = te.status "
					+ " and rt.created_At = (Select max(rtx.created_At)  from tm_rootcause_insts rtx where rtx.task_id = te.task_id  and rtx.action = te.status )";
			String latestUpdateTime = "select  Max(at.created_at) from tm_audit_trail at  "
					+ " where te.task_id = at.task_id " + " group by te.task_id";

			String commonQuery = "select te.task_id as TASK_ID, te.created_at as DATE_TIME ,te.status as STATUS ,te.task_type as TASK_TYPE, SUBSTR_AFTER(te.TSK_SUBJECT, '-') as CLASSIFICATION ,"
					+ "  pe.loc_code as LOCATION_CODE , taskOwn.TASK_OWNER_DISP as ASSIGNED_TO," + "(" + rootCause
					+ ") as ROOT_CAUSE , te.process_id as PROCESS_ID ,"
					+ " wm.muwi as MUWI_ID ,  te.ORIGIN as ORIGIN , " + "(" + inProgressTimeQuery
					+ " ) AS inProgressTime," + " (" + resolvedTimeQuery + " ) AS resolvedTime , "
					+ "te.parent_origin as PARENT_ORIGIN , " + " (" + latestUpdateTime + " ) AS LATEST_UPDATED_TIME"
					+ " FROM tm_task_evnts te  " + " left join tm_proc_evnts pe " + " on te.process_id = pe.process_id "
					+ " left join tm_task_owner taskOwn " + " on te.TASK_ID = taskOwn.TASK_ID "
					+ " left join well_muwi wm " + "on pe.LOC_CODE = wm.LOCATION_CODE "
					+ " WHERE te.STATUS NOT IN('COMPLETED', 'REVOKED', 'DRAFT' , 'NEW') ";

			String queryString = "";
			if (ServicesUtil.isEmpty(taskType) && ServicesUtil.isEmpty(status) && ServicesUtil.isEmpty(parentOrigin)) {
				queryString = commonQuery;
				logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][Get All Tasks]" + queryString);

			} else if (ServicesUtil.isEmpty(taskType) && ServicesUtil.isEmpty(parentOrigin)) {
				queryString = commonQuery + " and  te.status= '" + status + "'";
				logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][Get All Tasks with explicit Status]"
						+ queryString);

			} else if (ServicesUtil.isEmpty(status) && ServicesUtil.isEmpty(parentOrigin)) {
				if (taskType.equalsIgnoreCase("Dispatch")) {
					queryString = commonQuery + " and te.origin= '" + taskType
							+ "' and te.parent_origin in ( 'Custom' , 'Inquiry', 'Investigation')";
				} else {

					queryString = commonQuery + " and te.origin= '" + taskType + "'";
				}
				logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][Get All Tasks with explicit Origin/tasktype]"
						+ queryString);

			} else if (ServicesUtil.isEmpty(status) && ServicesUtil.isEmpty(taskType)) {
				queryString = commonQuery + " and te.parent_origin = '" + parentOrigin + "'";
				logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][Get All Tasks with explicit Parent origin]"
						+ queryString);

			} else if (ServicesUtil.isEmpty(status)) {

				if (taskType.equalsIgnoreCase("Dispatch") && parentOrigin.equalsIgnoreCase("ITA")) {
					queryString = commonQuery + " and  te.origin= '" + taskType + "' " + " and te.parent_origin like '"
							+ parentOrigin + "%" + "'";
					logger.error(
							"[Murphy][TaskEventsDao][getAllTasksForAdmin][Get All Tasks with Origin and Parent origin ITA]"
									+ queryString);
				}

				else {
					queryString = commonQuery + " and  te.origin= '" + taskType + "'  and te.parent_origin = '"
							+ parentOrigin + "'";
					logger.error(
							"[Murphy][TaskEventsDao][getAllTasksForAdmin][Get All Tasks with Origin and Parent origin]"
									+ queryString);
				}
			} else if (ServicesUtil.isEmpty(parentOrigin)) {

				if (taskType.equalsIgnoreCase("Dispatch")) {
					queryString = commonQuery + " and te.origin= '" + taskType
							+ "' and te.parent_origin in ( 'Custom' , 'Inquiry', 'Investigation') and te.status = '"
							+ status + "'";
				} else {
					queryString = commonQuery + " and  te.origin= '" + taskType + "'  and te.status = '" + status + "'";
				}

				logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][Get All Tasks with Origin and status]"
						+ queryString);

			}

			else {
				if (parentOrigin.equalsIgnoreCase("ITA")) {
					queryString = commonQuery + " and  te.origin='" + taskType + "' " + "and te.status= '" + status
							+ "' and te.parent_origin  like '" + parentOrigin + "%" + "'";
					logger.error(
							"[Murphy][TaskEventsDao][getAllTasksForAdmin][Get All Tasks with explicit everything and ITA]"
									+ queryString);
				} else {
					queryString = commonQuery + " and  te.origin='" + taskType + "' and te.status= '" + status
							+ "' and te.parent_origin='" + parentOrigin + "'";
					logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][Get All Tasks with explicit everything]"
							+ queryString);
				}

			}
			String finalQuery = "select distinct TASK_ID,DATE_TIME,STATUS,TASK_TYPE,CLASSIFICATION,LOCATION_CODE,"
					+ "ASSIGNED_TO, ROOT_CAUSE,PROCESS_ID, MUWI_ID, ORIGIN,inProgressTime,resolvedTime , PARENT_ORIGIN , LATEST_UPDATED_TIME FROM ("
					+ queryString + ") order by DATE_TIME desc";

			String countQueryString = " SELECT COUNT(*)  FROM " + "(" + finalQuery + ")";
			Query query = this.getSession().createSQLQuery(countQueryString);
			logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][countQueryString]" + query);
			countResult = ((BigInteger) query.uniqueResult()).intValue();
			logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][CountResult]" + countResult);

			if (page > 0) {
				int first = (page - 1) * pageSize;
				int last = pageSize;
				finalQuery += " LIMIT " + last + " OFFSET " + first + "";
			}

			Query executeQuery = this.getSession().createSQLQuery(finalQuery);
			logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][finalQuery]" + executeQuery);

			List<Object[]> responseList = executeQuery.list();

			String taskStatus = null;
			if (!ServicesUtil.isEmpty(responseList)) {
				TaskListDto dto = null;
				for (Object[] obj : responseList) {

					dto = new TaskListDto();

					dto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);

					/*
					 * dto.setCreatedAtInString(ServicesUtil.isEmpty(obj[1]) ?
					 * null : ServicesUtil.convertFromZoneToZoneString(null,
					 * obj[1], MurphyConstant.UTC_ZONE, MurphyConstant.UTC_ZONE,
					 * MurphyConstant.DATE_DB_FORMATE,
					 * MurphyConstant.DATE_DISPLAY_FORMAT));
					 */
					Date createdAtString = ServicesUtil.isEmpty(obj[1]) ? null : (Date) obj[1];
					dto.setCreatedAtInString(
							ServicesUtil.isEmpty(createdAtString) ? null : String.valueOf(createdAtString.getTime()));

					dto.setStatus(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);

					taskStatus = dto.getStatus();

					dto.setTaskType(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);

					dto.setClassification(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);

					dto.setLocationCode(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					if (!ServicesUtil.isEmpty(dto.getLocationCode())) {
						dto.setLocation(locDao.getLocationByLocCode(dto.getLocationCode()));
					}

					dto.setTaskOwner(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);

					if (taskStatus.equals(MurphyConstant.RESOLVE)) {
						dto.setRootCause(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);

						// Do not change these to Epoch
						String inPro = ServicesUtil.isEmpty(obj[11]) ? null
								: ServicesUtil.convertFromZoneToZoneString(null, obj[11], MurphyConstant.UTC_ZONE,
										MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.DATE_DISPLAY_FORMAT);
						String res = ServicesUtil.isEmpty(obj[12]) ? null
								: ServicesUtil.convertFromZoneToZoneString(null, obj[12], MurphyConstant.UTC_ZONE,
										MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.DATE_DISPLAY_FORMAT);
						String turnAround = turnAroundCalculation(inPro, res);

						if (!ServicesUtil.isEmpty(turnAround)) {
							String turnAroundMinutes = turnAround + " Minutes";
							dto.setTurnAroundTime(turnAroundMinutes);

							// Need to take mins dynamically
							long varianceTime = 20L - Long.parseLong(turnAround);
							dto.setTurnAroundVariance(new Long(varianceTime).toString());

						}
					}
					dto.setProcessId(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
					dto.setMuwiId(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
					dto.setOrigin(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
					dto.setParentOrigin(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
					// Do not change these to Epoch
					String latestUpdatedTime = ServicesUtil.isEmpty(obj[14]) ? null
							: ServicesUtil.convertFromZoneToZoneString(null, obj[14], MurphyConstant.UTC_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
									MurphyConstant.DATE_DISPLAY_FORMAT);
					logger.error("Date from database : " + latestUpdatedTime);
					String toBeDecidedTime = toBeDecidedTimeCalculation(latestUpdatedTime);
					dto.setTaskTimeDifference(toBeDecidedTime);
					responseListDto.add(dto);
				}
			}
			taskListResponseDto.setTaskList(responseListDto);

			taskListResponseDto.setPageCount(pageSize);
			taskListResponseDto.setTotalCount(new BigDecimal(countResult));
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][autoChangeStatus][error]" + e.getMessage());
		}

		return taskListResponseDto;
	}

	// CALCULATION OF TASK TIME DIFFERENCE
	public String toBeDecidedTimeCalculation(String latestUpdatedTime) {
		// String p = "Thu Jun 20 2019 06:27:54 GMT+0000 (UTC)";
		// 03-Dec-18 10:57:07 AM
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		String time = null;

		try {

			Calendar c = Calendar.getInstance();
			String currentTime = c.getTime().toString();
			Date currTime = sdf.parse(currentTime);
			String convertedTime = format.format(currTime);
			Date d1 = format.parse(convertedTime);
			String dateFromDb = latestUpdatedTime;
			Date d2 = format.parse(dateFromDb);

			// in milliseconds
			long diff = d1.getTime() - d2.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			String days = new Long(diffDays).toString();
			String hours = new Long(diffHours).toString();
			String mins = new Long(diffMinutes).toString();
			String seconds = new Long(diffSeconds).toString();
			time = days + " days " + hours + " hrs " + mins + " mins " + seconds + " secs ";

		} catch (Exception e) {
			logger.error("[Murphy][task time difference][task time diff calculation][error]" + e.getMessage());
		}
		return time;
	}

	// CALCULATE TURN AROUND TIME
	public String turnAroundCalculation(String inProgTime, String inResolveTime) {
		// String p = "Thu Jun 20 2019 06:27:54 GMT+0000 (UTC)";
		// 03-Dec-18 10:57:07 AM
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		String time = null;
		Date dinProg;
		try {
			dinProg = format.parse(inProgTime);
			Date dResolved = format.parse(inResolveTime);
			long diffMilli = dResolved.getTime() - dinProg.getTime();
			long diffMins = (diffMilli / 1000) / 60;
			String mins = new Long(diffMins).toString();
			time = mins;
			logger.error("[turnAroundCalculation][Time]" + time);
		} catch (Exception e) {
			logger.error("[Murphy][CustomLocationHistoryDao][turnAroundCalculation][error]" + e.getMessage());
		}
		return time;
	}

	// AUTO CLOSE STATUS FOR IOP ADMIN CONSOLE
	@SuppressWarnings("unchecked")
	public String autoChangeStatusForAdmin(List<IopTaskListDto> taskIds) {
		String response = null;
		String commaSeperatedTaskIds = "";
		for (IopTaskListDto t : taskIds) {
			String i = t.getTaskId();
			commaSeperatedTaskIds += i + "','";
		}
		Date currentDdate = new Date();
		try {

			String queryString = "select te.task_id as TASK_ID , te.status as STATUS from tm_task_evnts te where task_id  in  ('"
					+ commaSeperatedTaskIds + "')";

			Query executeQuery = this.getSession().createSQLQuery(queryString);
			logger.error("[Murphy][TaskEventsDao][autoChangeStatus][queryString]" + executeQuery);
			List<Object[]> responseList = executeQuery.list();
			if (!ServicesUtil.isEmpty(responseList)) {
				TaskListDto dto = null;
				for (Object[] obj : responseList) {
					dto = new TaskListDto();
					dto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setStatus(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					response = updateTaskEventStatusForAdmin(dto.getTaskId(), dto.getProcessId(), "IOP_Admin",
							"IOP_Admin", MurphyConstant.COMPLETE, currentDdate, "");

				}
			}
			response = MurphyConstant.SUCCESS;

		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][autoChangeStatus][error]" + e.getMessage());
		}
		return response;
	}

	// AUTO CLOSE OF TASKS FOR IOP ADMIN( DB UPDATE)
	private String updateTaskEventStatusForAdmin(String taskId, String processId, String user, String userDisplay,
			String status, Date userUpdatedAt, String description) {
		String response = MurphyConstant.FAILURE;
		try {
			String queryString = "Select te from TaskEventsDo te where te.taskEventsDoPK.taskId in( '" + taskId + "')";
			Query q = this.getSession().createQuery(queryString);
			TaskEventsDo updateDo = (TaskEventsDo) q.uniqueResult();
			String updatedStatus = null;
			if (!ServicesUtil.isEmpty(updateDo)) {
				if (!ServicesUtil.isEmpty(description)) {
					updateDo.setDescription(description);
				}
				if (!ServicesUtil.isEmpty(status)) {
					if (MurphyConstant.COMPLETE.equals(status)) {

						if (MurphyConstant.ASSIGN.equals(updateDo.getStatus())
								|| MurphyConstant.RETURN.equals(updateDo.getStatus())
								|| MurphyConstant.INPROGRESS.equals(updateDo.getStatus())
								|| MurphyConstant.NEW_TASK.equals(updateDo.getStatus())
								|| MurphyConstant.RESOLVE.equals(updateDo.getStatus())) {
							ProcessEventsDo processEventsDo = new ProcessEventsDo();
							processEventsDo.setProcessId(updateDo.getTaskEventsDoPK().getProcessId());
							processEventsDo.setStatus(MurphyConstant.COMPLETE);
							processEventsDao.updateProcessStatusToComplete(processEventsDo);
							updatedStatus = updateAutoChangeStatusForAdmin(updateDo.getTaskEventsDoPK().getTaskId(),
									status);
							String taskCreator = getTaskCreator(updateDo.getTaskEventsDoPK().getProcessId());
							auditDao.createInstance(updateDo.getStatus(), taskCreator, updateDo.getPrevTask());
							String inqTaskId = getLatestTaskForProcess(updateDo.getPrevTask());
							auditDao.createInstance(updateDo.getStatus(), taskCreator, inqTaskId);

						}
					}

					if (!ServicesUtil.isEmpty(updatedStatus)) {
						if (updatedStatus.equals(MurphyConstant.SUCCESS)) {
							updateDo.setStatus(MurphyConstant.COMPLETE);
						}
					}

					updateDo.setStatus(status);
					updateDo.setUpdatedAt(new Date());
					updateDo.setUpdatedBy(userDisplay);

					if (!ServicesUtil.isEmpty(userUpdatedAt)) {
						updateDo.setUserUpdatedAt(userUpdatedAt);
					}
					merge(updateDo);
				}
			}
			response = MurphyConstant.SUCCESS;

		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][updateTaskEventStatus][error]" + e.getMessage());
		}
		return response;
	}

	// UPDATE DB FOR AUTO CLOSE STATUS, FOR IOP ADMIN
	public String updateAutoChangeStatusForAdmin(String taskId, String status) {
		String updateQuery = "update tm_task_evnts set status='" + MurphyConstant.COMPLETE + "' ,UPDATED_BY= '"
				+ MurphyConstant.IOP_ADMIN + "' , COMPLETED_AT =  TO_TIMESTAMP('"
				+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "", MurphyConstant.DATE_DB_FORMATE_SD)
				+ "', 'yyyy-mm-dd hh24:mi:ss')  where task_id ='" + taskId + "' AND STATUS NOT IN ('"
				+ MurphyConstant.COMPLETE + "','" + MurphyConstant.REVOKED + "')";

		Query q = this.getSession().createSQLQuery(updateQuery);
		logger.error("[Murphy][TaskEventsDao][updateAutoChangeStatusForAdmin][error]" + updateQuery);
		Integer result = q.executeUpdate();
		if (result > 0)
			return MurphyConstant.SUCCESS;
		else
			return MurphyConstant.NO_RECORD;

	}

	// check for User Authorization and Task status
	public TaskOwnersDto getTaskStatus(String userEmailId, String taskId, String processId, String userGroup) {
		TaskOwnersDto dto = new TaskOwnersDto();
		String taskOwnerId = null;
		try {
			ProcessEventsDto procDto = getTaskCreatorDetails(processId);
			String status = procDto.getStatus();
			if (!ServicesUtil.isEmpty(status) && !MurphyConstant.COMPLETE.equalsIgnoreCase(status)) {
				boolean isFound = userGroup.indexOf(procDto.getGroup()) != -1 ? true : false;
				if (isFound == true) {
					dto.setTaskStatus(MurphyConstant.ACTIVE);
				} else {
					dto.setTaskStatus("User Not Authorized ");
				}

			} else {
				dto.setTaskStatus(MurphyConstant.COMPLETE);
			}

		} catch (Exception ex) {
			logger.error("Exception while Fetching Task Details" + ex.getMessage());
		}
		return dto;

	}

	private Boolean checkRequiredDate(String date) {
		Boolean within5days = false;
		try {

			final Calendar cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(date));
			final Calendar calNow = Calendar.getInstance();
			calNow.add(Calendar.DATE, -6);
			if (cal.after(calNow)) {
				within5days = true;
			} else {
				within5days = false;
			}
		} catch (Exception ex) {
			logger.error("[TaskEventsDao][checkRequiredDate][Error] " + ex.getMessage());
		}
		return within5days;
	}

	public TaskListDto getAttrValues(TaskListDto taskDto) {
		List<CustomAttrTemplateDto> cusList = null;
		try {
			if (!ServicesUtil.isEmpty(taskDto.getOrigin())) {
				switch (taskDto.getOrigin()) {
				case MurphyConstant.INQUIRY:
					cusList = getCustomAttrIntancesforInq(taskDto.getTaskId() + "','" + taskDto.getProcessId(),
							MurphyConstant.TEMP_ID_INQUIRY_OBS, taskDto.getOrigin());
					break;
				case MurphyConstant.INVESTIGATON:
					cusList = getCustomAttrIntancesforInq(taskDto.getTaskId() + "','" + taskDto.getProcessId(),
							MurphyConstant.TEMP_ID_OBSERVATION, taskDto.getOrigin());
					break;
				case MurphyConstant.DISPATCH_ORIGIN:
					cusList = getCustomAttrIntancesforInq(taskDto.getTaskId(), null, taskDto.getOrigin());
					break;
				}
				if (!ServicesUtil.isEmpty(cusList))
					for (CustomAttrTemplateDto c : cusList) {
						if (!ServicesUtil.isEmpty(c.getLabel()) && !ServicesUtil.isEmpty(c.getLabelValue())
								&& c.getLabel().equalsIgnoreCase("Issue Classification"))
							taskDto.setIssueClassification(c.getLabelValue());
						if (!ServicesUtil.isEmpty(c.getLabel()) && !ServicesUtil.isEmpty(c.getLabelValue())
								&& c.getLabel().equalsIgnoreCase("Task Classification"))
							taskDto.setTaskClassification(c.getLabelValue());
						if (!ServicesUtil.isEmpty(c.getLabel()) && !ServicesUtil.isEmpty(c.getLabelValue())
								&& c.getLabel().equalsIgnoreCase("Sub Classification"))
							taskDto.setSubClassification(c.getLabelValue());
					}
			} else
				logger.error("[Murphy][TaskEventsDao][getAttrValues][Task Origin Empty]");
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][getAttrValues][error]" + e.getMessage());
		}
		return taskDto;
	}

	// Filter for task Classficatin and sub- classification
	@SuppressWarnings("unchecked")
	public List<CustomAttrTemplateDto> getCustomAttrIntancesforInq(String taskId, String taskTempId, String origin) {
		String queryString = "Select temp.LABEL,ins.INS_VALUE" + " from TM_ATTR_INSTS ins"
				+ " inner join TM_ATTR_TEMP temp on temp.ATTR_ID = ins.ATTR_TEMP_ID " + "where ins.TASK_ID in( '"
				+ taskId + "') ";

		if (!ServicesUtil.isEmpty(taskTempId) && origin.equalsIgnoreCase(MurphyConstant.INQUIRY)) {
			queryString = queryString + "and temp.TASK_TEMP_ID in ('" + taskTempId + "')";
		} else if (!ServicesUtil.isEmpty(taskTempId) && origin.equalsIgnoreCase(MurphyConstant.INVESTIGATON)) {
			queryString = queryString + "and temp.TASK_TEMP_ID in ('" + taskTempId + "')";
		}

		// logger.error("[Murphy][TaskEventsDao][getAttrIntancesforInq][queryString]"
		// + queryString);
		CustomAttrTemplateDto dto = null;
		List<CustomAttrTemplateDto> cusList = new ArrayList<>();
		try {
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					dto = new CustomAttrTemplateDto();
					dto.setLabel((ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]));
					dto.setLabelValue((ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]));
					cusList.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][getAttrIntancesforInq][error]" + e.getMessage());
		}
		return cusList;
	}

	public TaskEventsDto getTaskDetailsForProcessId(String processId) {
		TaskEventsDto evntsDto = new TaskEventsDto();
		try {
			String fetchQuery = "SELECT TASK_ID,PARENT_ORIGIN,ORIGIN FROM TM_TASK_EVNTS WHERE PROCESS_ID='" + processId
					+ "' AND " + "ORIGIN='" + MurphyConstant.INQUIRY + "' AND PARENT_ORIGIN='" + MurphyConstant.INQUIRY
					+ "'";
			Query q = this.getSession().createSQLQuery(fetchQuery);

			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {

				Object[] obj = response.get(0);

				evntsDto = new TaskEventsDto();
				evntsDto.setTaskId((ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]));
				evntsDto.setParentOrigin((ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]));
				evntsDto.setOrigin((ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]));

			}

			return evntsDto;
		} catch (Exception e) {
			logger.error("Exception while fetching Task Details" + e.getMessage());
		}
		return evntsDto;

	}

	public String updateStatus(String taskId, String processId, String status) {
		String response = MurphyConstant.FAILURE;
		try {
			// Update query for status from DRAFT to ASSIGN
			String updateQuery = "update tm_task_evnts set status='" + MurphyConstant.ASSIGN + "'where task_id ='"
					+ taskId + "' AND PARENT_ORIGIN IN ('" + MurphyConstant.P_ITA + "','" + MurphyConstant.P_ITA_DOP
					+ "')";

			Query q = this.getSession().createSQLQuery(updateQuery);
			logger.error("[Murphy][TaskEventsDao][updateStatus][updateQuery]" + updateQuery);
			Integer result = q.executeUpdate();
			logger.error("[Murphy][TaskEventsDao][updateStatus][result]" + result);
			if (result > 0)
				response = MurphyConstant.SUCCESS;

			String taskCreator = getTaskCreator(processId);
			auditDao.createInstance(MurphyConstant.ASSIGN, taskCreator, taskId);
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][updateStatus][error] " + e.getMessage());
		}
		return response;
	}

	public String updateCreatedAt(String taskId) {
		String created_At = null;
		String response = null;
		try {
			Date today = new Date();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			created_At = sdf.format(today);

			String updateQuery = "update tm_task_evnts set created_at = TO_TIMESTAMP('" + created_At
					+ "','yyyy-MM-dd HH24:mi:ss'), updated_at = " + "TO_TIMESTAMP('" + created_At
					+ "','yyyy-MM-dd HH24:mi:ss') " + "where task_id ='" + taskId + "' " + "AND PARENT_ORIGIN IN ('"
					+ MurphyConstant.P_ITA + "','" + MurphyConstant.P_ITA_DOP + "')";

			Query q = this.getSession().createSQLQuery(updateQuery);
			logger.error("[Murphy][TaskEventsDao][updateCreatedAt][updateQuery]" + updateQuery);
			Integer result = q.executeUpdate();
			logger.error("[Murphy][TaskEventsDao][updateCreatedAt][result]" + result);

			if (result > 0)
				response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][updateCreatedAt][error] " + e.getMessage());
		}

		return response;
	}

	/*
	 * public HashMap<String, String> fieldWiseAvailability(String
	 * technicalRole) { HashMap<String, String> hm = new HashMap<>(); try {
	 * String fieldOfTechRolesQuery =
	 * "select field from TM_ROLE_MAPPING where technicalrole in (" +
	 * technicalRole.trim() + ")"; List<String> fieldObjList =
	 * this.getSession().createSQLQuery(fieldOfTechRolesQuery).list(); for
	 * (String locCode : fieldObjList) { String query =
	 * "select sum(tmto.est_drive_time) + sum(tmto.est_resolve_time) as totalOccupiedTime "
	 * +
	 * "from tm_task_evnts te inner join tm_proc_evnts pe on te.process_id = pe.process_id "
	 * + "inner join tm_task_owner tmto on tmto.task_id = te.task_id " +
	 * "where pe.loc_code like '" + locCode + "'"; List<Object[]> objs =
	 * this.getSession().createSQLQuery(fieldOfTechRolesQuery).list(); //
	 * hm.put(locCode, availability) } } catch (Exception e) {
	 * logger.error("Exception while fetching fieldWiseAvailability " +
	 * e.getMessage()); } return hm;
	 * 
	 * }
	 */

	public FieldAvailabilityResponseDto fieldWiseAvailability(String technicalRole) {
		FieldAvailabilityResponseDto responseDto = new FieldAvailabilityResponseDto();
		List<FieldAvailabilityDto> fieldAvailabilityDtosList = new ArrayList<>();
		DecimalFormat format = new DecimalFormat("##.0");
		double cumulativePercent = 0;

		// current time in CST
		Calendar cCurrent = Calendar.getInstance();
		cCurrent.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
		int currentHour = cCurrent.get(Calendar.HOUR_OF_DAY);
		int currentMinute = cCurrent.get(Calendar.MINUTE);
		logger.error("[Murphy][TaskEventsDao][fieldWiseAvailability][cCurrent][hour]" + currentHour + "[minute] "
				+ currentMinute);
		String shift = checkShift(currentHour, currentMinute);

		StringBuffer shiftQuery = new StringBuffer(
				"Select CONFIG_ID,CONFIG_DESC_VALUE from TM_CONFIG_VALUES where CONFIG_ID in ");
		int shiftStartHour = 0;
		int shiftStartMin = 0;
		int shiftEndHour = 0;
		int shiftEndMin = 0;

		if (shift.equals("1")) {
			// day shift
			shiftQuery.append("('DAY_SHIFT_START','DAY_SHIFT_END')");
			shiftStartHour = 10;
			shiftStartMin = 0;
			shiftEndHour = 22;
			shiftEndMin = 0;
		} else {
			// night shift
			shiftStartHour = 22;
			shiftStartMin = 0;
			shiftEndHour = 10;
			shiftEndMin = 0;
			shiftQuery.append("('NIGHT_SHIFT_START','NIGHT_SHIFT_END')");
		}

		logger.error("[Murphy][TaskEventsDao][fieldWiseAvailability][shiftQuery]" + shiftQuery.toString());

		List<Object[]> shiftObjectList = this.getSession().createSQLQuery(shiftQuery.toString()).list();

		for (Object[] obj : shiftObjectList) {
			if (obj[0].toString().contains("SHIFT_START") && !ServicesUtil.isEmpty((String) obj[1])) {
				String startTime[] = obj[1].toString().split("\\.");
				shiftStartHour = Integer.parseInt(startTime[0]);
				if (startTime.length > 1) {
					shiftStartMin = Integer.parseInt(startTime[1]);
				}

			} else if (obj[0].toString().contains("SHIFT_END") && !ServicesUtil.isEmpty((String) obj[1])) {
				String endTime[] = obj[1].toString().split("\\.");
				shiftEndHour = Integer.parseInt(endTime[0]);
				if (endTime.length > 1) {
					shiftEndMin = Integer.parseInt(endTime[1]);
				}
			}
		}

		DateFormat sdf = new SimpleDateFormat(MurphyConstant.DATE_DB_FORMATE_SD);
		sdf.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
		Date currentTimeInDate = cal.getTime();
		String currentTime = sdf.format(cal.getTime());

		int currentHourInUTC = cal.get(Calendar.HOUR_OF_DAY);
		Calendar cStart = Calendar.getInstance();
		cStart.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
		cStart.get(Calendar.DATE);
		cStart.set(Calendar.HOUR_OF_DAY, shiftStartHour);
		cStart.set(Calendar.MINUTE, shiftStartMin);
		cStart.set(Calendar.SECOND, 0);
		if (shift.equals("2") && currentHourInUTC < 12) {
			cStart.add(Calendar.DATE, -1);
		}

		Date shiftStartTime = cStart.getTime();
		String dayStart = sdf.format(cStart.getTime());

		Calendar cEnd = Calendar.getInstance();
		cEnd.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
		cEnd.get(Calendar.DATE);
		cEnd.set(Calendar.HOUR_OF_DAY, shiftEndHour);
		cEnd.set(Calendar.MINUTE, shiftEndMin);
		cEnd.set(Calendar.SECOND, 0);
		if (shift.equals("2") && currentHourInUTC > 12) {
			cEnd.add(Calendar.DATE, 1);
		}

		Date shiftEndTime = cEnd.getTime();
		String dayEnd = sdf.format(shiftEndTime);

		double remTimeLeftInMins = TimeUnit.MILLISECONDS
				.toMinutes(shiftEndTime.getTime() - currentTimeInDate.getTime());

		// roles to fetch operators eg :PRO_CATARINA or OBX_CATARINA operators
		String busRolAndFieByTechRoles = "Select  BUSINESSEROLE, FIELD,TECHNICALROLE from TM_ROLE_MAPPING where  technicalrole in ("
				+ ServicesUtil.getStringFromList(Arrays.asList(technicalRole.split("\\,"))) + ")";

		List<Object[]> fieldObjList = this.getSession().createSQLQuery(busRolAndFieByTechRoles).list();
		Map<String, List<FieldOperatorAvailabilityDto>> fieldToOperatorMap = null;
		Map<String, List<String>> roleToFieldMap = null;
		Map<String, Double> operatorToTaskTimeMap = new HashMap<>();
		if (!ServicesUtil.isEmpty(fieldObjList)) {
			fieldToOperatorMap = new HashMap<>();
			roleToFieldMap = new HashMap<>();
			for (Object[] objects : fieldObjList) {
				if (!ServicesUtil.isEmpty(objects[0]) && !ServicesUtil.isEmpty(objects[1])) {
					String businessRole = objects[0].toString();
					if (businessRole.contains(MurphyConstant.Catarina)) {

						businessRole = MurphyConstant.Catarina.toUpperCase();
					}
					if (businessRole.contains(MurphyConstant.Karnes)) {
						businessRole = MurphyConstant.Karnes.toUpperCase();
					}
					if (businessRole.contains(MurphyConstant.Tilden)) {
						businessRole = MurphyConstant.Tilden.toUpperCase();
					}

					if (roleToFieldMap.containsKey(businessRole)) {
						roleToFieldMap.get(businessRole).add((String) objects[1]);
					} else {
						roleToFieldMap.put(businessRole, new ArrayList<>());
						roleToFieldMap.get(businessRole).add((String) objects[1]);
					}

					if (!fieldToOperatorMap.containsKey(businessRole)) {
						fieldToOperatorMap.put(businessRole, new ArrayList<>());
					}

				}
			}
		}

		StringBuffer roleInQuery = new StringBuffer("");
		Set<String> fieldKeys = fieldToOperatorMap.keySet();
		for (String userRole : fieldKeys) {
			String proRole = "PRO_" + userRole;
			String obxRole = "OBX_" + userRole;
			roleInQuery = roleInQuery.append("USER_ROLE ").append("like '%").append(proRole).append("%'").append(" or ")
					.append("USER_ROLE").append(" like '%").append(obxRole).append("%'").append(" or ");

		}
		roleInQuery.delete(roleInQuery.lastIndexOf("or"), roleInQuery.length());
		logger.error("[Murphy][TaskEventsDao][fieldWiseAvailability][roleInQuery]" + roleInQuery);
		// fetch operators only in that shift

		List<String> empByShift = shiftRegisterDao.getEmpByShift();
		boolean proOrOBXOperatorInShift = false;
		StringBuffer employessInShiftQuery = new StringBuffer("USER_EMAIL in (");
		if (!ServicesUtil.isEmpty(empByShift)) {
			for (String employee : empByShift) {
				proOrOBXOperatorInShift = true;
				employessInShiftQuery.append("'").append(employee).append("'").append(",");
			}

		}
		employessInShiftQuery.deleteCharAt(employessInShiftQuery.length() - 1);
		employessInShiftQuery.append(")");
		logger.error("[Murphy][TaskEventsDao][fieldWiseAvailability][employessInShiftQuery]" + employessInShiftQuery);

		// fetch all PRO AND OBX operators present in the shift
		if (proOrOBXOperatorInShift && fieldKeys.size() > 0) {
			String operatorsQuery = "Select USER_FIRST_NAME,USER_LAST_NAME, USER_EMAIL,USER_ROLE FROM TM_USER_IDP_MAPPING where "
					+ "(" + roleInQuery + ")";
			// + employessInShiftQuery + " )" + " and (" + roleInQuery + ")";

			logger.error("[Murphy][TaskEventsDao][fieldWiseAvailability][operatorsQuery]" + operatorsQuery);
			List<Object[]> operatorsObjList = this.getSession().createSQLQuery(operatorsQuery).list();
			for (Object[] operatorObjects : operatorsObjList) {
				for (String employee : empByShift) {
					if (employee.equalsIgnoreCase((String) operatorObjects[2])) {
						FieldOperatorAvailabilityDto operatorAvailabilityDto = new FieldOperatorAvailabilityDto();
						operatorAvailabilityDto.setFirstName(
								ServicesUtil.isEmpty(operatorObjects[0]) ? "" : (String) operatorObjects[0]);
						operatorAvailabilityDto.setLastName(
								ServicesUtil.isEmpty(operatorObjects[1]) ? "" : (String) operatorObjects[1]);
						operatorAvailabilityDto.setEmailId((String) operatorObjects[2]);
						operatorAvailabilityDto.setAvailablePercentage("100%");
						// Assign operators to corresponding fields
						String userRole[] = operatorObjects[3].toString().split(",");
						for (String role : userRole) {
							for (String field : fieldKeys) {
								if (role.contains(field)) {
									if (role.contains("PRO")) {
										operatorAvailabilityDto.setOperatorType("PRO");
									} else {
										operatorAvailabilityDto.setOperatorType("OBX");
									}
									if (!fieldToOperatorMap.get(field).contains(operatorAvailabilityDto)) {
										fieldToOperatorMap.get(field).add(operatorAvailabilityDto);
									}
								}
							}

						}
						operatorToTaskTimeMap.put(operatorAvailabilityDto.getEmailId(), 0.0);
					}
				}
			}
		}
		int noOfOperatorsCumulative = 0;
		double cumulativeRemTimeLeftInMins = 0;
		for (String role : roleToFieldMap.keySet()) {
			double remTimeLeftInMinsForAllOperators = 0;
			double fieldAvailabilityPercentage = 0;
			int noOfOperators = 0;
			double noOfMinsAvaForAllOps = 0;
			FieldAvailabilityDto fieldAvailabilityDto = new FieldAvailabilityDto();
			fieldAvailabilityDto.setField(role);
			fieldAvailabilityDto.setNoOfOperators(noOfOperators);

			if (proOrOBXOperatorInShift && !ServicesUtil.isEmpty(fieldToOperatorMap)
					&& !ServicesUtil.isEmpty(roleToFieldMap) && roleToFieldMap.get(role).size() > 0
					&& fieldToOperatorMap.get(role).size() > 0) {
				List<FieldOperatorAvailabilityDto> operatorListByField = fieldToOperatorMap.get(role);
				StringBuffer locCodeBuffer = new StringBuffer("(");
				for (String locationCode : roleToFieldMap.get(role)) {
					locCodeBuffer.append(" PE.LOC_CODE like ").append("'").append(locationCode).append("%").append("'")
							.append(" or ");
				}
				locCodeBuffer.delete(locCodeBuffer.lastIndexOf("or"), locCodeBuffer.length() - 1);
				locCodeBuffer.append(")");

				StringBuffer operatorsInQuery = new StringBuffer("(");
				for (FieldOperatorAvailabilityDto operatorDto : operatorListByField) {
					operatorsInQuery.append("'").append(operatorDto.getEmailId()).append("'").append(",");
				}
				operatorsInQuery.deleteCharAt(operatorsInQuery.length() - 1);
				operatorsInQuery.append(")");

				// fetch total task time for all the operators
				String taskQuery = "Select TOW.TASK_OWNER_EMAIL ,(SUM(TOW.EST_RESOLVE_TIME)+SUM(TOW.EST_DRIVE_TIME)) as totalTaskTime  "
						+ "from TM_PROC_EVNTS PE inner join TM_TASK_EVNTS TE on PE.PROCESS_ID=TE.PROCESS_ID "
						+ " inner join TM_TASK_OWNER TOW on TE.TASK_ID=TOW.TASK_ID " + " where " + locCodeBuffer
						+ " and TE.STATUS not in ('COMPLETED','RESOLVED')" + " and TOW.TASK_OWNER_EMAIL in "
						+ operatorsInQuery + " and TOW.END_TIME >TO_TIMESTAMP ('" + currentTime
						+ "','yyyy-MM-dd HH24:mi:ss') " + " and TOW.START_TIME >=TO_TIMESTAMP ('" + dayStart
						+ "','yyyy-MM-dd HH24:mi:ss') " + "and TOW.END_TIME<=TO_TIMESTAMP ('" + dayEnd
						+ "','yyyy-MM-dd HH24:mi:ss')" + "group by TOW.TASK_OWNER_EMAIL";

				logger.error("[Murphy][TaskEventsDao][fieldWiseAvailability][taskQuery]" + taskQuery);
				List<Object[]> objs = this.getSession().createSQLQuery(taskQuery).list();

				// calculate the total task time
				for (Object[] object : objs) {
					operatorToTaskTimeMap.put((String) object[0], (Double) object[1]);

				}

				for (FieldOperatorAvailabilityDto operatorDto : operatorListByField) {
					double noOfMinutesAvaiForSinOp = 0;
					if (remTimeLeftInMins > 0
							&& (remTimeLeftInMins > operatorToTaskTimeMap.get(operatorDto.getEmailId()))) {
						noOfMinutesAvaiForSinOp = remTimeLeftInMins
								- operatorToTaskTimeMap.get(operatorDto.getEmailId());
						double opAvaPercentage = (noOfMinutesAvaiForSinOp / remTimeLeftInMins) * 100;
						if (opAvaPercentage > 100) {
							opAvaPercentage = 100;
						}
						if (opAvaPercentage % 1 == 0) {
							operatorDto.setAvailablePercent(Math.round(opAvaPercentage));
							operatorDto.setAvailablePercentage(Math.round(opAvaPercentage) + "%");
						} else {
							if (Double.parseDouble(format.format(opAvaPercentage)) % 1 == 0) {
								operatorDto.setAvailablePercent(
										Math.round(Double.parseDouble(format.format(opAvaPercentage))));
								operatorDto.setAvailablePercentage(
										Math.round(Double.parseDouble(format.format(opAvaPercentage))) + "%");
							} else {

								operatorDto.setAvailablePercent(Double.parseDouble(format.format(opAvaPercentage)));
								operatorDto.setAvailablePercentage(format.format(opAvaPercentage) + "%");
							}
						}
						// operatorDto.setAvailablePercentage(opAvaPercentage +
						// "%");
						// totTimorAllOpsInMins = totTimorAllOpsInMins +
						// operatorToTaskTimeMap.get(operatorDto.getEmailId());

					} else {
						operatorDto.setAvailablePercent(0);
						operatorDto.setAvailablePercentage("0 %");
					}

					noOfMinsAvaForAllOps = noOfMinsAvaForAllOps + noOfMinutesAvaiForSinOp;
				}
				fieldAvailabilityDto.setOperatorAvailabilityList(operatorListByField);
				noOfOperators = fieldToOperatorMap.get(role).size();
				remTimeLeftInMinsForAllOperators = noOfOperators * remTimeLeftInMins;
			}

			if (remTimeLeftInMinsForAllOperators > 0) {
				fieldAvailabilityPercentage = (noOfMinsAvaForAllOps / remTimeLeftInMinsForAllOperators) * 100;
			} else {
				fieldAvailabilityPercentage = 0;
			}
			if (fieldAvailabilityPercentage > 100) {
				fieldAvailabilityPercentage = 100;
			}

			if (fieldAvailabilityPercentage % 1 == 0) {
				fieldAvailabilityDto.setFieldAvailablePercent(Math.round(fieldAvailabilityPercentage));
				fieldAvailabilityDto.setFieldAvailabilityPercentage(Math.round(fieldAvailabilityPercentage) + "%");
			} else {
				if (Double.parseDouble(format.format(fieldAvailabilityPercentage)) % 1 == 0) {
					fieldAvailabilityDto.setFieldAvailablePercent(
							Math.round(Double.parseDouble(format.format(fieldAvailabilityPercentage))));
					fieldAvailabilityDto.setFieldAvailabilityPercentage(
							Math.round(Double.parseDouble(format.format(fieldAvailabilityPercentage))) + "%");

				} else {
					fieldAvailabilityDto
							.setFieldAvailablePercent(Double.parseDouble(format.format(fieldAvailabilityPercentage)));
					fieldAvailabilityDto
							.setFieldAvailabilityPercentage(format.format(fieldAvailabilityPercentage) + "%");
				}
			}

			fieldAvailabilityDto.setNoOfOperators(noOfOperators);
			fieldAvailabilityDtosList.add(fieldAvailabilityDto);
			noOfOperatorsCumulative = noOfOperatorsCumulative + noOfOperators;
			cumulativeRemTimeLeftInMins = cumulativeRemTimeLeftInMins + noOfMinsAvaForAllOps;

		}

		if ((remTimeLeftInMins * noOfOperatorsCumulative) > 0) {
			cumulativePercent = ((cumulativeRemTimeLeftInMins) / (remTimeLeftInMins * noOfOperatorsCumulative)) * 100;
		}
		if (cumulativePercent > 100) {
			cumulativePercent = 100;
		}

		responseDto.setFieldAvailabilityDtoList(fieldAvailabilityDtosList);
		if (cumulativePercent % 1 == 0) {
			responseDto.setCumulativePercentage(Math.round(cumulativePercent));
			responseDto.setCumulativePercent(Math.round(cumulativePercent) + "%");

		} else {
			if (Double.parseDouble(format.format(cumulativePercent)) % 1 == 0) {
				responseDto.setCumulativePercentage(Math.round(Double.parseDouble(format.format(cumulativePercent))));
				responseDto
						.setCumulativePercent(Math.round(Double.parseDouble(format.format(cumulativePercent))) + "%");
			} else {
				responseDto.setCumulativePercent(format.format(cumulativePercent) + "%");
				responseDto.setCumulativePercentage(Double.parseDouble(format.format(cumulativePercent)));
			}
		}

		return responseDto;

	}

	private String checkShift(int hour, int min) {
		if (5 <= hour && hour < 17) {
			return "1";
		} else {
			return "2";
		}
	}

	public String updateStatusIopAdminConsole(TaskEventsDto dto) {
		String res = MurphyConstant.FAILURE;
		try {

			String updateTaskQuery = "update tm_task_evnts set status='" + MurphyConstant.CANCELLED + "' ,UPDATED_BY= '"
					+ MurphyConstant.IOP_ADMIN + "' , COMPLETED_AT =  TO_TIMESTAMP('"
					+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "",
							MurphyConstant.DATE_DB_FORMATE_SD)
					+ "', 'yyyy-mm-dd hh24:mi:ss')  where task_id ='" + dto.getTaskId() + "' AND STATUS IN ('"
					+ MurphyConstant.INPROGRESS + "')";
			Query query = this.getSession().createSQLQuery(updateTaskQuery);
			int result1 = query.executeUpdate();
			if (result1 > 0) {
				String updateProcQuery = "update tm_proc_evnts set status='" + MurphyConstant.CANCELLED
						+ "', COMPLETED_AT =  TO_TIMESTAMP('"
						+ ServicesUtil.convertFromZoneToZoneString(null, null, "", "", "",
								MurphyConstant.DATE_DB_FORMATE_SD)
						+ "', 'yyyy-mm-dd hh24:mi:ss')  where PROCESS_ID ='" + dto.getProcessId() + "'";
				Query procQuery = this.getSession().createSQLQuery(updateProcQuery);
				int result2 = procQuery.executeUpdate();
				if (result2 > 0 && result1 > 0)
					res = MurphyConstant.SUCCESS;
			}

		} catch (Exception e) {
			System.err.println("[Murphy][TaskEventsDao][updateStatusIopAdminConsole][error]" + e.getMessage());
		}
		return res;
	}

	public LocationHierarchyResponseDto getLocationMasterDetails(int page, int page_size, String locationText,
			String locationCode, String locationType, String muwi, String tier) {
		LocationHierarchyResponseDto locationHierarchyReponseDto = new LocationHierarchyResponseDto();
		List<LocationHierarchyDto> listDto = new ArrayList<LocationHierarchyDto>();
		Integer countResult = 0;
		String fetchQuery = "";
		  String whereQuery ="";
          String whereQuery1 ="";
          String whereQuery2 ="";
          String whereQuery3 ="";
          String whereQuery4 ="";
          String whereQuery5 ="";
		try {
			 fetchQuery = "SELECT pl.location_code,pl.location_text,pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI"
			 + " FROM PRODUCTION_LOCATION pl "
			 + "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code =lc.location_code "
			 + "LEFT OUTER JOIN WELL_TIER wt ON pl.location_code =wt.location_code "
			 + "LEFT OUTER JOIN WELL_MUWI wm ON pl.location_code = wm.location_code ";
			
			String toUpperCaseLocationText = locationText.toUpperCase();
			String value = "";
			if (!ServicesUtil.isEmpty(muwi))
				value = ServicesUtil.getCountryCodeByMuwi(muwi);
			
			whereQuery ="WHERE ";
			
			StringBuilder whereQueryBuffer = new StringBuilder(whereQuery);
			
			
			//filter for muwi
            if(!ServicesUtil.isEmpty(muwi))
            {
            	if (!ServicesUtil.isEmpty(value) && value.equalsIgnoreCase(MurphyConstant.CA_CODE)) {
    				muwi = muwi.toUpperCase();
    				whereQuery1 =" UPPER(wm.muwi) like '%"+muwi+"%' ";
    			}
            	else{
            		whereQuery1 =" wm.muwi like '%"+muwi+"%' ";
            	}
            	
            }
            
            //filter for tier
            if(!ServicesUtil.isEmpty(tier) && !tier.equalsIgnoreCase("All"))
            {
            	whereQuery2=" UPPER(wt.tier) like '%"+tier.toUpperCase()+"%' ";
            }
            if(ServicesUtil.isEmpty(tier))
            {
            	whereQuery2=" (wt.tier is Null or wt.tier = '') ";
            }
            
           
            //filter for location text
            if(!ServicesUtil.isEmpty(locationText))
            {
            	whereQuery3 =" UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' ";
            }
            
            //filter for location type
            if(!ServicesUtil.isEmpty(locationType) && !locationType.equalsIgnoreCase("All"))
            {
            	if(locationType.equalsIgnoreCase("Well"))
            	{
            		whereQuery4 =" pl.location_type = 'Well' ";
            	}
            	else
            	{
            		whereQuery4 =" pl.location_type like '%"+locationType+"%' ";
            	}
            
            }
            if(!ServicesUtil.isEmpty(locationType) && locationType.equalsIgnoreCase("All"))
            {
            	whereQuery4 =" (pl.location_type = 'Well' or pl.location_type like '%Facility%' or pl.location_type like '%Central Facility%') ";
            }
            
            //filter for location code
            if(!ServicesUtil.isEmpty(locationCode))
            {
            	whereQuery5 =" UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' ";
            }
            
            //building where clause for fetchQuery
            whereQueryBuffer.append(ServicesUtil.isEmpty(whereQuery1) ? "" : " "+whereQuery1 + " and") ;
            
            whereQueryBuffer.append(ServicesUtil.isEmpty(whereQuery2) ? "" : " "+whereQuery2 +" and" );
            
            whereQueryBuffer.append(ServicesUtil.isEmpty(whereQuery3) ? "" : " "+whereQuery3 + " and" );
            
            whereQueryBuffer.append(ServicesUtil.isEmpty(whereQuery4) ? "" : " "+whereQuery4 +" and" ) ;
            
            whereQueryBuffer.append(ServicesUtil.isEmpty(whereQuery5) ? "" : " "+whereQuery5 + " ") ;
             
            boolean iswhereQueryEndingWithAnd=whereQueryBuffer.toString().endsWith("and");
            if(iswhereQueryEndingWithAnd)
            {
            	System.err.println("value" +(whereQueryBuffer.lastIndexOf("and")));
            	whereQueryBuffer.delete(whereQueryBuffer.lastIndexOf("and"),whereQueryBuffer.length());
            }
            
            fetchQuery = fetchQuery + whereQueryBuffer;
            

//			String toUpperCaseLocationText = locationText.toUpperCase();
//			String value = "";
//			if (!ServicesUtil.isEmpty(muwi))
//				value = ServicesUtil.getCountryCodeByMuwi(muwi);
//			if (!ServicesUtil.isEmpty(value) && value.equalsIgnoreCase(MurphyConstant.CA_CODE)) {
//				muwi = muwi.toUpperCase();
//			}
//			if (locationType.equalsIgnoreCase("All") && tier.equalsIgnoreCase("All")) {
//
//				if(!ServicesUtil.isEmpty(muwi))
//				{
//				fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//						+ "FROM PRODUCTION_LOCATION pl "
//						+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//						+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//						+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//						+ "WHERE UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
//						+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' "
//						+" and (pl.location_type = 'Well' or pl.location_type like '%Facility%' or pl.location_type like '%Central Facility%') "
//						+ "and wm.MUWI like '%" + muwi + "%' ";
//				}
//				else
//				{
//					fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//							+ "FROM PRODUCTION_LOCATION pl "
//							+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//							+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//							+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//							+ "WHERE UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
//							+" and (pl.location_type = 'Well' or pl.location_type like '%Facility%' or pl.location_type like '%Central Facility%') "
//							+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' ";
//				}
//
//			}
//
//			// both not all
//			if (!(locationType.equalsIgnoreCase("All")) && !(tier.equalsIgnoreCase("All"))) {
//				
//				if (locationType.equalsIgnoreCase("Facility"))
//				{
//					if(ServicesUtil.isEmpty(tier) && ServicesUtil.isEmpty(muwi))
//					{
//						fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//								+ "FROM PRODUCTION_LOCATION pl "
//								+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//								+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//								+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//								+ "WHERE (pl.location_type like '%Facility%' or pl.location_type like '%Central Facility%') "
//								+ " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
//								+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' ";
//					}
//					if((!ServicesUtil.isEmpty(tier) && !ServicesUtil.isEmpty(muwi)) 
//						|| (!ServicesUtil.isEmpty(tier) && ServicesUtil.isEmpty(muwi)) 
//						|| ((ServicesUtil.isEmpty(tier) && !ServicesUtil.isEmpty(muwi))))
//						{
//						fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//								+ "FROM PRODUCTION_LOCATION pl "
//								+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//								+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//								+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//								+ "WHERE (pl.location_type like '%Facility%' or pl.location_type like '%Central Facility%') "
//								+ " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
//								+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' "
//						        + "and wt.tier like '%" + tier + "%' and wm.MUWI like '%" + muwi + "%' ";
//						}	
//				}
//				
//				if (locationType.equalsIgnoreCase("Well"))
//				{
//					if(!ServicesUtil.isEmpty(tier))
//					{
//					fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//							+ "FROM PRODUCTION_LOCATION pl "
//							+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//							+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//							+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//							+ " WHERE pl.location_type = 'Well' "
//							+ " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
//							+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' "
//					        + "and wt.tier like '%" + tier + "%' and wm.MUWI like '%" + muwi + "%' ";
//					}
//					else
//					{
//						fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//								+ "FROM PRODUCTION_LOCATION pl "
//								+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//								+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//								+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//								+ "WHERE pl.location_type = 'Well' "
//								+ " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
//								+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' "
//						        + " and wm.MUWI like '%" + muwi + "%' ";
//					}
//				}
//				
//				}
//
//			if (locationType.equalsIgnoreCase("All") && !(tier.equalsIgnoreCase("All"))) {
//				if(!ServicesUtil.isEmpty(tier))
//				{
//
//					fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//							+ "FROM PRODUCTION_LOCATION pl "
//							+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//							+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//							+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//							+ "WHERE wt.tier like '%" + tier + "%' " + " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase()
//							+ "%' " + "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' "
//							+ "and wm.MUWI like '%" + muwi + "%' and (pl.location_type = 'Well' OR pl.location_type like '%Facility%')";
//				}
//				else{
//					fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//							+ "FROM PRODUCTION_LOCATION pl "
//							+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//							+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//							+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//							+ "WHERE UPPER(pl.location_code) like '%" + locationCode.toUpperCase()
//							+ "%' " + "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' "
//							+ "and wm.MUWI like '%" + muwi + "%' and (pl.location_type = 'Well' OR pl.location_type like '%Facility%')";
//				}
//
//			}
//			
//			if (!(locationType.equalsIgnoreCase("All")) && (tier.equalsIgnoreCase("All"))) {
//				
//				//if(!locationType.equalsIgnoreCase("Facility") && !locationType.equalsIgnoreCase("Well"))
////				{
////					if(ServicesUtil.isEmpty(muwi))
////					{
////						fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
////								+ "FROM PRODUCTION_LOCATION pl "
////								+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
////								+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
////								+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
////								+ "WHERE pl.location_type like '%"+locationType+"%' "
////								+ "and (wt.tier like '%Tier A%' OR wt.tier like '%Tier B%' OR wt.tier like '%Tier C%')"
////								+ " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
////								+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' ";
////					}
////					if((!ServicesUtil.isEmpty(muwi)))
////						{
////						fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
////								+ "FROM PRODUCTION_LOCATION pl "
////								+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
////								+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
////								+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
////								+ "WHERE pl.location_type like '%"+locationType+"%'  "
////								+ "and (wt.tier like '%Tier A%' OR wt.tier like '%Tier B%' OR wt.tier like '%Tier C%')"
////								+ " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
////								+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' "
////						        + "and wm.MUWI like '%" + muwi + "%' ";
////						}	
////				}
//				if (locationType.equalsIgnoreCase("Facility"))
//				{
//					if(ServicesUtil.isEmpty(muwi))
//					{
//						fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//								+ "FROM PRODUCTION_LOCATION pl "
//								+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//								+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//								+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//								+ "WHERE (pl.location_type like '%Facility%' or pl.location_type like '%Central Facility%') "
//								+ " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
//								+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' ";
//					}
//					if((!ServicesUtil.isEmpty(muwi)))
//						{
//						fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//								+ "FROM PRODUCTION_LOCATION pl "
//								+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//								+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//								+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//								+ "WHERE (pl.location_type like '%Facility%' or pl.location_type like '%Central Facility%') "
//								+ " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
//								+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' "
//						        + "and wm.MUWI like '%" + muwi + "%' ";
//						}	
//				}
//				
//				if (locationType.equalsIgnoreCase("Well"))
//				{
//					fetchQuery = "SELECT UPPER(pl.location_code),UPPER(pl.location_text),pl.location_type,lc.latitude,lc.longitude,wt.tier,wm.MUWI "
//							+ "FROM PRODUCTION_LOCATION pl "
//							+ "LEFT OUTER JOIN LOCATION_COORDINATE lc ON pl.location_code = lc.location_code "
//							+ "LEFT OUTER JOIN WELL_TIER wt ON  pl.location_code = wt.location_code "
//							+ "LEFT OUTER JOIN WELL_MUWI wm ON  pl.location_code = wm.location_code "
//							+ "WHERE pl.location_type = 'Well' "
//							+ " and UPPER(pl.location_code) like '%" + locationCode.toUpperCase() + "%' "
//							+ "and UPPER(pl.location_text) like '%" + toUpperCaseLocationText + "%' "
//					        + "and wm.MUWI like '%" + muwi + "%' ";
//					
//				}
//			}

            

			System.err.println("[Murphy][TaskEventsDao][getLocationMasterDetails][fetchQuery]" + fetchQuery);

			String countQueryString = " SELECT COUNT(*) AS COUNT FROM " + "(" + fetchQuery + ")";
			Query query = this.getSession().createSQLQuery(countQueryString);
			countResult = ((BigInteger) query.uniqueResult()).intValue();
			logger.error("[Murphy][TaskEventsDao][getLocationMasterDetails][countResult]" + countResult);
 
			 
			
			if (page > 0) {
				int first = (page - 1) * page_size;
				int last = page_size;
				fetchQuery += " LIMIT " + last + " OFFSET " + first + "";
			}
			logger.error("[Murphy][TaskEventsDao][getLocationMasterDetails][fetchQuery]" + fetchQuery);

			Query q = this.getSession().createSQLQuery(fetchQuery);

			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				for (Object[] obj : response) {
					LocationHierarchyDto locationHierarchyDto = new LocationHierarchyDto();

				
						locationHierarchyDto.setLocCode(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						locationHierarchyDto.setLocationText(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						locationHierarchyDto.setLocationType(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
						locationHierarchyDto.setLatValue(ServicesUtil.isEmpty(obj[3]) ? null : (BigDecimal) obj[3]);
						locationHierarchyDto.setLongValue(ServicesUtil.isEmpty(obj[4]) ? null : (BigDecimal) obj[4]);
						locationHierarchyDto.setTier(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
						locationHierarchyDto.setMuwi(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
					
					
						locationHierarchyDto.setLocCode(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						locationHierarchyDto.setLocationText(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						locationHierarchyDto.setLocationType(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
						locationHierarchyDto.setLatValue(ServicesUtil.isEmpty(obj[3]) ? null : (BigDecimal) obj[3]);
						locationHierarchyDto.setLongValue(ServicesUtil.isEmpty(obj[4]) ? null : (BigDecimal) obj[4]);
						String tierValue=ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5];
						if(!ServicesUtil.isEmpty(tierValue) && tierValue.equals("TIER A"))
						{
							locationHierarchyDto.setTier("Tier A");	
						}
						else if(!ServicesUtil.isEmpty(tierValue) && tierValue.equals("TIER B"))
						{
							locationHierarchyDto.setTier("Tier B");	
						}
						else if(!ServicesUtil.isEmpty(tierValue) && tierValue.equals("TIER C"))
						{
							locationHierarchyDto.setTier("Tier C");	
						}
						else{
						locationHierarchyDto.setTier(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
						}
						locationHierarchyDto.setMuwi(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);

						listDto.add(locationHierarchyDto);
//						if(ServicesUtil.isEmpty(tier) && ServicesUtil.isEmpty(locationHierarchyDto.getTier()))
//						{
//							System.err.println("user requested for  empty tiers : " );
//							listDto.add(locationHierarchyDto);
//						}
//						if(!ServicesUtil.isEmpty(tier)
//								&&!(tier.equalsIgnoreCase("All"))
//								&& !ServicesUtil.isEmpty(locationHierarchyDto.getTier()))
//						{
//							listDto.add(locationHierarchyDto);
//						}
//						if(!ServicesUtil.isEmpty(tier)
//								&&(tier.equalsIgnoreCase("All")))
//						{
//							listDto.add(locationHierarchyDto);
//						}
				}
			}
			locationHierarchyReponseDto.setListLocationHierarchyDto(listDto);
			locationHierarchyReponseDto.setPageCount(new BigDecimal(page_size));
			locationHierarchyReponseDto.setTotalCount(new BigDecimal(countResult));

		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][getLocationMasterDetails][error]" + e.getMessage());
		}
		return locationHierarchyReponseDto;
	}

	public String updateLocationMaster(List<LocationHierarchyDto> listLocationHierarchyDto) {
		String response = null;
		int result1 = 0, result2 = 0;
		try {
			for (LocationHierarchyDto dto : listLocationHierarchyDto) {
				String loc_code = dto.getLocCode();
				if (!ServicesUtil.isEmpty(loc_code)) {
					// If latitude,longitude is changed
					if (!ServicesUtil.isEmpty(dto.getLatValue()) && !ServicesUtil.isEmpty(dto.getLongValue())) {
						boolean location_exists = checkLocationExists(loc_code, "LOCATION_COORDINATE");
						if (location_exists) {
							String updateLocationCoordinate = "UPDATE LOCATION_COORDINATE lc SET lc.latitude = "
									+ dto.getLatValue() + ",lc.longitude = " + dto.getLongValue()
									+ " WHERE lc.location_code = '" + loc_code + "'";
							logger.error("[Murphy][TaskEventsDao][updateLocationMaster][updateLocationCoordinate] "
									+ updateLocationCoordinate);
							Query query = this.getSession().createSQLQuery(updateLocationCoordinate);
							result1 = query.executeUpdate();
						} else {
							String loc_type = locDao.getLocationTypeByLocCode(loc_code);
							String insertLocationCoordinate = "INSERT INTO LOCATION_COORDINATE VALUES ('" + loc_code
									+ "','" + dto.getLatValue() + "','" + dto.getLongValue() + "','" + loc_type + "')";
							logger.error("[Murphy][TaskEventsDao][updateLocationMaster][insertLocationCoordinate] "
									+ insertLocationCoordinate);
							Query query = this.getSession().createSQLQuery(insertLocationCoordinate);
							result2 = query.executeUpdate();
						}
					}
					// If tier is changed
					if (!ServicesUtil.isEmpty(dto.getTier())) {
						boolean location_exists = checkLocationExists(loc_code, "WELL_TIER");
						if (location_exists) {
							String updateWellTier = "UPDATE WELL_TIER wt SET wt.tier = '" + dto.getTier()
									+ "' WHERE wt.location_code = '" + loc_code + "'";
							logger.error(
									"[Murphy][TaskEventsDao][updateLocationMaster][updateWellTier] " + updateWellTier);
							Query query_tier = this.getSession().createSQLQuery(updateWellTier);
							result2 = query_tier.executeUpdate();
						} else {
							String insertWellTier = "INSERT INTO WELL_TIER VALUES ('" + loc_code + "','" + dto.getTier()
									+ "')";
							logger.error(
									"[Murphy][TaskEventsDao][updateLocationMaster][insertWellTier] " + insertWellTier);
							Query query_tier = this.getSession().createSQLQuery(insertWellTier);
							result2 = query_tier.executeUpdate();
						}
					}
				}
			}
			if (result2 > 0 || result1 > 0)
				response = MurphyConstant.SUCCESS;

		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][updateLocationMaster][error]" + e.getMessage());
		}
		return response;
	}

	public boolean checkLocationExists(String locCode, String tableName) {
		boolean location_exists = false;
		Integer countResult = 0;
		try {
			String queryString = "SELECT count(*) FROM " + tableName + " WHERE location_code = '" + locCode + "'";
			logger.error("[Murphy][TaskEventsDao][checkLocationExists][queryString]" + queryString);
			Query query = this.getSession().createSQLQuery(queryString);
			countResult = ((BigInteger) query.uniqueResult()).intValue();
			if (countResult > 0)
				location_exists = true;
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][checkLocationExists][error]" + e.getMessage());
		}
		return location_exists;
	}

	public TaskListDto getNextTaskDetails(String userId) {
		TaskListDto taskListDto = new TaskListDto();
		String next_task_id = "";
		Double distance = -1.0;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);

		try {
			String fetchQuery = "SELECT tw.task_id FROM tm_task_owner AS tw INNER JOIN tm_task_evnts AS te "
					+ "ON tw.task_id = te.task_id WHERE task_owner = '" + userId + "' " + "AND te.status IN ('"
					+ MurphyConstant.INPROGRESS + "','" + MurphyConstant.ASSIGN + "') " + "AND te.origin = '"
					+ MurphyConstant.DISPATCH_ORIGIN + "' AND " + "start_time > TO_TIMESTAMP('"
					+ sdf.format(cal.getTime()) + "', 'yyyy-MM-dd HH24:mi:ss') " + "ORDER BY start_time ASC LIMIT 1;";

			logger.error("[Murphy][TaskEventsDao][getNextTaskDetails][fetchQuery]" + fetchQuery);

			Object obj = this.getSession().createSQLQuery(fetchQuery).uniqueResult();
			if (!ServicesUtil.isEmpty(obj))
				next_task_id = obj.toString();
			taskListDto = getTaskDetailsById(next_task_id, userId);

			if (!ServicesUtil.isEmpty(taskListDto)) {
				// Fetching next coordinates based on location of next task
				String next_loaction = taskListDto.getLocationCode();
				Coordinates next_coordinates = locDao.getCoordByCode(next_loaction);

				// Fetching current coordinates based on Geotab (device)
				// location of user
				UserIDPMappingDo user = userMappingDao.getUserByEmail(userId);
				Coordinates current_coordinates = null;

				if (!ServicesUtil.isEmpty(user.getSerialId())) {
					Coordinates geoResponse = GeoTabUtil.getLocBySerialId(user.getSerialId());
					if (!ServicesUtil.isEmpty(geoResponse)) {
						current_coordinates = geoResponse;
					}
				}
				// Using crow fly distance to check distance between
				// current_location and next task location
				if (!ServicesUtil.isEmpty(current_coordinates) && !ServicesUtil.isEmpty(next_coordinates))
					distance = GeoTabUtil.getDistance(current_coordinates, next_coordinates);

				logger.error("[Murphy][TaskEventsDao][getNextTaskDetails] current_coordinates : " + current_coordinates
						+ "next_coordinates " + next_coordinates + "distance : " + distance);

				taskListDto.setDistance(distance);
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][getNextTaskDetails][error]" + e.getMessage());
		}
		return taskListDto;
	}

	public TaskListDto getTaskDetailsById(String taskId, String userId) {
		TaskListDto dto = null;
		try {
			String queryString = "";
			queryString = "select * from (Select te.TASK_ID ,te.PROCESS_ID,te.CREATED_AT,te.DESCRIPTION,te.STATUS,te.tsk_subject,pe.STARTED_BY,tw.TASK_OWNER, "
					+ "tw.TASK_OWNER_DISP,te.origin ,pe.loc_code, pe.user_group , te.PARENT_ORIGIN ,null,te.REFRENCE_NUM,"
					+ "tw.start_time,pe.STARTED_BY_DISP from TM_TASK_EVNTS te join TM_PROC_EVNTS pe on te.PROCESS_ID = pe.PROCESS_ID "
					+ "left outer join TM_TASK_OWNER tw on te.TASK_ID = tw.TASK_ID where te.TASK_ID = '" + taskId
					+ "' and tw.task_owner = '" + userId + "' "
					+ "and pe.loc_code not in (select LOCATION_CODE from WELL_TIER wt) "
					+ "UNION Select te.TASK_ID ,te.PROCESS_ID,te.CREATED_AT,te.DESCRIPTION,te.STATUS,te.tsk_subject,pe.STARTED_BY,tw.TASK_OWNER, "
					+ "tw.TASK_OWNER_DISP,te.origin ,pe.loc_code, pe.user_group , te.PARENT_ORIGIN ,"
					+ "wt.TIER,te.REFRENCE_NUM,tw.start_time,pe.STARTED_BY_DISP from WELL_TIER wt, TM_TASK_EVNTS te join TM_PROC_EVNTS pe on te.PROCESS_ID = pe.PROCESS_ID left outer join "
					+ "TM_TASK_OWNER tw on te.TASK_ID = tw.TASK_ID where wt.LOCATION_CODE = pe.loc_code AND te.TASK_ID = '"
					+ taskId + "' and tw.task_owner = '" + userId + "')";
			Query q = this.getSession().createSQLQuery(queryString);
			System.err.println("[Murphy][TaskEventsDao][getTaskDetailsById][queryString]" + queryString);

			List<Object[]> responseList = q.list();
			if (!ServicesUtil.isEmpty(responseList)) {
				dto = new TaskListDto();
				for (Object[] obj : responseList) {
					dto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setProcessId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					// Send in epoch
					Date created_at = ServicesUtil.isEmpty(obj[2]) ? null : (Date) obj[2];
					dto.setCreatedAtInString(
							ServicesUtil.isEmpty(created_at) ? null : String.valueOf(created_at.getTime()));
					dto.setTaskDescription(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setStatus(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setDescription(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					dto.setCreatedByEmailId(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
					dto.setTaskOwner(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
					dto.setOrigin(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
					dto.setLocationCode(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
					dto.setCreatorGroupId(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
					dto.setParentOrigin(ServicesUtil.isEmpty(obj[12]) ? null : (String) obj[12]);
					dto.setTier(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
					dto.setTaskRefNum(ServicesUtil.isEmpty(obj[14]) ? null
							: (((String) obj[14]).substring(((String) obj[14]).length() - 6)));
					// Send in epoch
					Date epoch_start_time = ServicesUtil.isEmpty(obj[15]) ? null : (Date) obj[15];
					dto.setStart_time(
							ServicesUtil.isEmpty(epoch_start_time) ? null : String.valueOf(epoch_start_time.getTime()));
					dto.setCreatedBy(ServicesUtil.isEmpty(obj[16]) ? null : (String) obj[16]);
					if (!ServicesUtil.isEmpty(obj[10])) {
						String locCode = (String) obj[10];
						String locText = locDao.getLocationByLocCode(locCode);
						dto.setLocation(ServicesUtil.isEmpty(locText) ? null : locText);
					}
					if (!ServicesUtil.isEmpty(dto.getOrigin()))
						dto = getAttrValues(dto);
					dto.setCreatedOnGroup(null);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskEventsDao][getTaskDetailsById][error]" + e.getMessage());
		}
		return dto;
	}

	// CHG0037344-Inquiry to a field seat.
	public void setCurProcValue(String taskId, String currentProcessor) {

		String queryString = "";
		if (!ServicesUtil.isEmpty(currentProcessor))
			queryString = "UPDATE TM_TASK_EVNTS SET CUR_PROC='" + currentProcessor + "' WHERE TASK_ID='" + taskId + "'";
		else
			queryString = "UPDATE TM_TASK_EVNTS SET CUR_PROC=" + currentProcessor + " WHERE TASK_ID='" + taskId + "'";

		try {
			Query q = this.getSession().createSQLQuery(queryString);
			q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][setAssignedTo][error]" + e.getMessage());
			e.printStackTrace();
		}

	}
}