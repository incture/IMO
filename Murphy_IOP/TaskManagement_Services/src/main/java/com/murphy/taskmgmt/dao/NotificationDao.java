package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.service.EnersightProveDaily;
import com.murphy.integration.service.EnersightProveMonthly;
import com.murphy.taskmgmt.dto.AlarmNotificationDto;
import com.murphy.taskmgmt.dto.BypassNotificationDto;
import com.murphy.taskmgmt.dto.CygnetAlarmFeedDto;
import com.murphy.taskmgmt.dto.EINotificationDto;
import com.murphy.taskmgmt.dto.NotificationDto;
import com.murphy.taskmgmt.dto.PwHopperNotificationDto;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskNotificationDto;
import com.murphy.taskmgmt.entity.NotificationDo;
import com.murphy.taskmgmt.entity.NotificationDoPK;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.taskmgmt.websocket.FracServiceEndPoint;

@Repository("NotificationDao")
@Transactional
public class NotificationDao extends BaseDao<NotificationDo, NotificationDto> {

	private static final Logger logger = LoggerFactory.getLogger(NotificationDao.class);

	@Autowired
	FracNotificationDao fracNotificationDao;

	@Autowired
	HierarchyDao locDao;

	@Autowired
	PWHopperStagingDao hopperDao;

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ShiftRegisterDao shiftRegisterDao;

	@Override
	protected NotificationDo importDto(NotificationDto fromDto) {
		NotificationDo entity = new NotificationDo();
		entity.getNotificationDoPk().setObjectId(fromDto.getObjectId());
		entity.setModule(fromDto.getModule());
		entity.setLocationtext(fromDto.getLocationText());
		entity.setAcknowledgedAt(fromDto.getAcknowledgedAt());
		entity.getNotificationDoPk().setUserId(fromDto.getUserId());
		entity.setUserGroup(fromDto.getUserGroup());
		entity.setIsAcknowledged(fromDto.getIsAcknowledged());
		entity.setLocCode(fromDto.getLocationCode());
		entity.setLocType(fromDto.getLocationType());
		return entity;
	}

	@Override
	protected NotificationDto exportDto(NotificationDo entity) {
		NotificationDto dto = new NotificationDto();
		dto.setObjectId(entity.getNotificationDoPk().getObjectId());
		dto.setModule(entity.getModule());
		dto.setLocationText(entity.getLocationtext());
		dto.setAcknowledgedAt(entity.getAcknowledgedAt());
		dto.setUserId(entity.getNotificationDoPk().getUserId());
		dto.setUserGroup(entity.getUserGroup());
		dto.setIsAcknowledged(entity.getIsAcknowledged());
		dto.setLocationCode(entity.getLocCode());
		dto.setLocationType(entity.getLocType());
		return dto;
	}

	@SuppressWarnings("unchecked")
	public TaskNotificationDto getTaskDetails(String userEmailId, String userGroup) {
		TaskNotificationDto taskNotification = new TaskNotificationDto();
		NotificationDto taskNotifyDto = null;
		TaskEventsDto evntsDto = null;
		List<NotificationDto> taskNotifyDtoList = new ArrayList<>();
		int taskIdCount = 0;
		String objectId = "";
		System.err.println("UserEmailId--" + userEmailId + "userGroup--" + userGroup);
		try {
			//updateAllStatusChanges(userGroup, userEmailId);
			userGroup = ServicesUtil.getStringForInQuery(userGroup);

			String fetchQuery = "SELECT ND.LOCATION_TEXT,ND.USER_GROUP,ND.WELL_STATUS,ND.MODULE,ND.OBJECT_ID,TE.TSK_SUBJECT,ND.LOC_CODE,ND.LOC_TYPE "
					+ "FROM NOTIFICATION_DETAILS ND JOIN TM_TASK_EVNTS TE ON ND.OBJECT_ID=TE.TASK_ID WHERE ND.USER_ID='"
					+ userEmailId + "'" + " AND ND.IS_ACKNOWLEDGED='false' AND ND.USER_GROUP IN(" + userGroup
					+ ") AND ND.WELL_STATUS "
					+ "IN('IN PROGRESS','RESOLVED','RETURNED') AND ND.MODULE IN('Dispatch','Investigation','Inquiry')";

			Query q = this.getSession().createSQLQuery(fetchQuery);
			//logger.error(" NotificationQuery----" + fetchQuery);
			List<Object[]> taskListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(taskListResponse)) {
				for (Object[] obj : taskListResponse) {
					String userGroupStored = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
					boolean isFound = userGroup.indexOf(userGroupStored) != -1 ? true : false;
					if (isFound == true) {
						taskNotifyDto = new NotificationDto();
						evntsDto = new TaskEventsDto();
						objectId = ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4];
						evntsDto = getTaskDetailsInfo(objectId);
						taskNotifyDto.setLocationText(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						taskNotifyDto.setUserGroup(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						taskNotifyDto.setStatus(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
						taskNotifyDto.setModule(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
						taskNotifyDto.setObjectId(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
						taskNotifyDto.setSubject(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
						taskNotifyDto.setAssignedTo(evntsDto.getCurrentProcessorDisplayName());
						taskNotifyDto.setLocationCode(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
						taskNotifyDto.setLocationType(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
						taskNotifyDtoList.add(taskNotifyDto);
					}
				}
			}
			if (taskNotifyDtoList.size() > 0) {
				taskIdCount = taskNotifyDtoList.size();
				taskNotification.setTaskNotificationList(taskNotifyDtoList);
				taskNotification.setTaskCount(taskIdCount);
			}
		} catch (Exception e) {
			logger.error("[Murphy][NotificationDao][getTaskDetails][error]" + e.getMessage());
		}
		return taskNotification;
	}

	private void updateAllStatusChanges(String userGroup, String userId) {
		String userGroupList = null;
		NotificationDoPK taskDoPk = null;
		NotificationDo taskDo = null;

		try {
			userGroupList = ServicesUtil.getStringForInQuery(userGroup);

			String updateTaskStatus = "SELECT DISTINCT OBJECT_ID,WELL_STATUS,LOC_CODE,LOC_TYPE,LOCATION_TEXT,MODULE,USER_GROUP FROM NOTIFICATION_DETAILS"
					+ " WHERE IS_ACKNOWLEDGED='" + MurphyConstant.NO_ACK + "' AND USER_GROUP IN(" + userGroupList
					+ ") AND " + "USER_ID NOT IN('" + userId + "') AND MODULE IN('Dispatch','Inquiry','Investigation')";

			Query q = this.getSession().createSQLQuery(updateTaskStatus);
			//logger.error(" updateTaskStatusQuery----" + updateTaskStatus);
			List<Object[]> taskListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(taskListResponse)) {
				for (Object[] obj : taskListResponse) {
					taskDoPk = new NotificationDoPK();
					taskDo = new NotificationDo();
					taskDoPk.setObjectId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					taskDoPk.setUserId(userId);
					taskDo.setNotificationDoPk(taskDoPk);
					taskDo.setWellStatus(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					taskDo.setLocCode(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					taskDo.setLocType(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					taskDo.setLocationtext(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					taskDo.setModule(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					taskDo.setUserGroup(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
					taskDo.setIsAcknowledged(MurphyConstant.NO_ACK);
					saveorupdateTaskStatusDetails(taskDo);

				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][NotificationDao][updateAllStatusChanges][error]" + e.getMessage());
		}
	}

	public void createAlertForStatusChange(String objectId, String module, String status) {
		CygnetAlarmFeedDto alarmDetail = new CygnetAlarmFeedDto();
		NotificationDoPK notificationPk = null;
		NotificationDo alertDto = null;
		NotificationDto notifyDto = null;
		TaskEventsDto evntsDto = null;
		try {
			FracServiceEndPoint endPoint = new FracServiceEndPoint();
			Set<String> userNameList = endPoint.getAllClientUsers();
			if (!ServicesUtil.isEmpty(userNameList)) {
				for (String userDetails : userNameList) {
					String[] arrSplit = userDetails.split("#%#");
					String userName = arrSplit[0];
					String userGroup = arrSplit[1];
					boolean isROC = userGroup.indexOf("ROC") != -1 ? true : false;
					boolean isPOT = userGroup.indexOf("POT") != -1 ? true : false;
					boolean isEnginner = userGroup.indexOf("Engineer") != -1 ? true : false;
					if (module.equalsIgnoreCase(MurphyConstant.ALARM)) {
						if (isROC == true) {
							notificationPk = new NotificationDoPK();
							alertDto = new NotificationDo();
							notifyDto = new NotificationDto();
							alarmDetail = getMuwiDetailFromPointId(objectId);
							if (!ServicesUtil.isEmpty(alarmDetail)) {
								if (!ServicesUtil.isEmpty(alarmDetail.getMuwi())) {
									NotificationDto wellMuwiDto = new NotificationDto();
									wellMuwiDto = fracNotificationDao.getWellNameByMuwi(alarmDetail.getMuwi(), "");
									notifyDto.setLocationCode(wellMuwiDto.getLocationCode());
									notifyDto.setLocationType(wellMuwiDto.getLocationType());
									notifyDto.setLocationText(wellMuwiDto.getLocationText());
								} else {
									alarmDetail = getAlarmsDetailForPointID(objectId);
									notifyDto.setLocationCode(alarmDetail.getLocationCode());
									notifyDto.setLocationType(alarmDetail.getLocationType());
									notifyDto.setLocationText(alarmDetail.getFacility());
								}
							}

							notificationPk.setObjectId(objectId);
							notificationPk.setUserId(userName);
							alertDto.setModule(MurphyConstant.ALARM);
							alertDto.setUserGroup(userGroup);
							alertDto.setNotificationDoPk(notificationPk);
							alertDto.setWellStatus(status);
							alertDto.setLocationtext(notifyDto.getLocationText());
							alertDto.setIsAcknowledged(MurphyConstant.NO_ACK);
							alertDto.setLocCode(notifyDto.getLocationCode());
							alertDto.setLocType(notifyDto.getLocationType());
							saveorupdateTaskStatusDetails(alertDto);
						}

					} else if (module.equalsIgnoreCase(MurphyConstant.TASK)) {
						evntsDto = getTaskDetailsInfo(objectId);
						notifyDto = new NotificationDto();
						notifyDto = fracNotificationDao.getWellNameByMuwi("", evntsDto.getLocationCode());
						String parentOrigin = evntsDto.getParentOrigin();
						if (!parentOrigin.equalsIgnoreCase("OBX")) {
							notificationPk = new NotificationDoPK();
							alertDto = new NotificationDo();
							notificationPk.setObjectId(evntsDto.getTaskId());
							notificationPk.setUserId(userName);
							alertDto.setModule(evntsDto.getOrigin());
							alertDto.setUserGroup(evntsDto.getGroup());
							alertDto.setNotificationDoPk(notificationPk);
							alertDto.setWellStatus(evntsDto.getStatus());
							alertDto.setLocationtext(notifyDto.getLocationText());
							alertDto.setIsAcknowledged(MurphyConstant.NO_ACK);
							alertDto.setLocCode(notifyDto.getLocationCode());
							alertDto.setLocType(notifyDto.getLocationType());
							saveorupdateTaskStatusDetails(alertDto);
						}

					} else if (module.equalsIgnoreCase(MurphyConstant.PW_HOPPER)) {
						if ((isEnginner == true && isPOT == true) || isEnginner == true || isPOT == true) {

							notificationPk = new NotificationDoPK();
							alertDto = new NotificationDo();
							notifyDto = new NotificationDto();
							notifyDto = fracNotificationDao.getWellNameByMuwi(objectId, "");
							notificationPk.setObjectId(objectId);
							notificationPk.setUserId(userName);
							alertDto.setModule(MurphyConstant.PW_HOPPER);
							alertDto.setUserGroup(userGroup);
							alertDto.setNotificationDoPk(notificationPk);
							alertDto.setWellStatus(status);
							alertDto.setLocationtext(notifyDto.getLocationText());
							alertDto.setLocCode(notifyDto.getLocationCode());
							alertDto.setLocType(notifyDto.getLocationType());
							alertDto.setIsAcknowledged(MurphyConstant.NO_ACK);
							saveorupdateTaskStatusDetails(alertDto);
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][NotificationDao][createAlertForStatusChange][error]" + e.getMessage());
		}

	}

	public void saveorupdateTaskStatusDetails(NotificationDo notificationDo) {
		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(notificationDo);
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[Murphy][NotificationDao][saveorupdateTaskStatusDetails][Excep]" + e.getMessage());
			e.printStackTrace();

		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[Murphy][NotificationDao][finallyBlock][Error] Exception While Closing Session "
						+ e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private TaskEventsDto getTaskDetailsInfo(String taskId) {
		TaskEventsDto returnDto = new TaskEventsDto();
		try {
			String queryString = "";
			queryString = "Select te.TASK_ID ,te.PROCESS_ID,te.CREATED_AT,te.DESCRIPTION,te.STATUS,pe.SUBJECT,pe.STARTED_BY_DISP,tw.TASK_OWNER,tw.TASK_OWNER_DISP,te.origin ,pe.loc_code,pe.PROCESS_TYPE,tW.p_id , pe.user_group , te.PARENT_ORIGIN , te.PREV_TASK, te.TASK_TYPE,te.UPDATED_AT,te.UPDATED_BY  "
					+ " from TM_TASK_EVNTS te join TM_PROC_EVNTS pe on te.PROCESS_ID = pe.PROCESS_ID left outer join TM_TASK_OWNER tw on te.TASK_ID = tw.TASK_ID where te.TASK_ID = '"
					+ taskId + "' ";
			Query q = this.getSession().createSQLQuery(queryString);

			//logger.error("[Murphy][NotificationDao][getTaskDetailsInfo][queryString]" + queryString);

			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				returnDto = new TaskEventsDto();
				for (Object[] obj : response) {
					returnDto.setTaskId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					returnDto.setProcessId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					returnDto.setStatus(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					returnDto.setOrigin(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
					returnDto.setLocationCode(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
					returnDto.setGroup(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
					returnDto.setSubject(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					returnDto.setParentOrigin(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
					returnDto.setUpdatedBy(ServicesUtil.isEmpty(obj[18]) ? null : (String) obj[18]);
					returnDto.setCurrentProcessorDisplayName(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
				}

			}
		} catch (Exception e) {
			logger.error("[Murphy][NotificationDao][getTaskDetailsInfo][error]" + e.getMessage());
		}
		return returnDto;
	}

	@SuppressWarnings("unchecked")
	public AlarmNotificationDto getAlarmNotification(String userName, String userGroup) {
		AlarmNotificationDto alarmNotifyDto = new AlarmNotificationDto();
		List<NotificationDto> alarmdtoList = new ArrayList<>();
		NotificationDto alarmdto = null;
		String pointId = null;
		String location = null;
		try {
			updateStatusOfWell();
			String fetchQuery = "SELECT OBJECT_ID,WELL_STATUS,LOC_CODE,LOC_TYPE,LOCATION_TEXT FROM NOTIFICATION_DETAILS WHERE IS_ACKNOWLEDGED='"
					+ MurphyConstant.NO_ACK + "' AND USER_ID='" + userName + "' AND MODULE ='" + MurphyConstant.ALARM
					+ "' AND WELL_STATUS='" + MurphyConstant.ALARM_SEVERITY_1_ALARM + "'";
			Query q = this.getSession().createSQLQuery(fetchQuery);
			//logger.error(" AlarmQuery----" + fetchQuery);
			List<Object[]> taskListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(taskListResponse)) {
				alarmNotifyDto = new AlarmNotificationDto();
				for (Object[] obj : taskListResponse) {
					alarmdto = new NotificationDto();
					pointId = ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
					CygnetAlarmFeedDto alarmDetail = getAlarmsDetailForPointID(pointId);

					if (!ServicesUtil.isEmpty(alarmDetail)) {
						alarmdto.setLongDesc(alarmDetail.getLongDescription());
						alarmdto.setFacilityDesc(alarmDetail.getFacDescription());
					}
					alarmdto.setObjectId(pointId);
					alarmdto.setUserId(userName);
					alarmdto.setStatus(MurphyConstant.ALARM);
					alarmdto.setUserGroup(userGroup);
					alarmdto.setModule(MurphyConstant.ALARM);
					alarmdto.setLocationCode(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					alarmdto.setLocationType(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					alarmdto.setLocationText(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					alarmdtoList.add(alarmdto);

				}

			}
			alarmNotifyDto.setAlarmNotificationList(alarmdtoList);
			if (alarmdtoList.size() > 0) {
				int alarmCount = alarmdtoList.size();
				alarmNotifyDto.setAlarmCount(alarmCount);

			}

		} catch (Exception e) {
			logger.error("[Murphy][NotificationDao][getAlarmNotification][error]" + e.getMessage());

		}
		return alarmNotifyDto;
	}

	private CygnetAlarmFeedDto getAlarmsDetailForPointID(String pointId) {
		CygnetAlarmFeedDto cygnetDto = null;
		String fetchAlarmInfoQuery = null;
		String facilityName = null;
		try {
			fetchAlarmInfoQuery = "SELECT AL.FACILITY_DESC,AL.LONGDESCRIPTION,AL.ROUTE,AL.MUWI,FM.FACILITY_CODE,PL.LOCATION_TYPE,FM.FACILITY_NAME FROM"
					+ " CYGNETPROD.ALARMS2 AL JOIN FACILITY_MAPPING FM on FM.FACILITY_ID=AL.FACILITYID "
					+ "JOIN PRODUCTION_LOCATION PL ON pl.LOCATION_CODE=FM.FACILITY_CODE WHERE AL.POINTIDLONG='"
					+ pointId + "'";

			Query q = this.getSession().createSQLQuery(fetchAlarmInfoQuery);
		//	logger.error(" fetchAlarmInfoQuery----" + fetchAlarmInfoQuery);
			List<Object[]> taskListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(taskListResponse)) {
				Object[] obj = taskListResponse.get(0);
				cygnetDto = new CygnetAlarmFeedDto();
				facilityName = ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6];
				facilityName = facilityName.replace(MurphyConstant.FACILITY, "");
				cygnetDto.setFacDescription(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				cygnetDto.setLongDescription(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				cygnetDto.setRoute(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
				cygnetDto.setMuwi(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
				cygnetDto.setLocationCode(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
				cygnetDto.setLocationType(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
				cygnetDto.setFacility(facilityName.trim());

			}

		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][getAlarmsDetailForPointID][error]" + ex.getMessage());

		}
		return cygnetDto;

	}

	@SuppressWarnings("unused")
	private CygnetAlarmFeedDto getMuwiDetailFromPointId(String pointId) {
		CygnetAlarmFeedDto cygnetDto = null;
		String fetchAlarmInfoQuery = null;
		String facilityName = null;
		try {
			fetchAlarmInfoQuery = "SELECT FACILITY_DESC,LONGDESCRIPTION,ROUTE,MUWI FROM CYGNETPROD.ALARMS2 WHERE POINTIDLONG='"
					+ pointId + "'";

			Query q = this.getSession().createSQLQuery(fetchAlarmInfoQuery);
		//	logger.error(" fetchAlarmInfoQuery----" + fetchAlarmInfoQuery);
			List<Object[]> taskListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(taskListResponse)) {
				Object[] obj = taskListResponse.get(0);
				cygnetDto = new CygnetAlarmFeedDto();
				cygnetDto.setFacDescription(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				cygnetDto.setLongDescription(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				cygnetDto.setRoute(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
				cygnetDto.setMuwi(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
			}

		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][getMuwiDetailFromPointId][error]" + ex.getMessage());

		}
		return cygnetDto;

	}

	private void updateStatusOfWell() {
		Integer updatedCount = null;
		try {
			String updateAlarmsStatus = "UPDATE NOTIFICATION_DETAILS NT "
					+ "SET WELL_STATUS = (SELECT ALARM_SEVERITY FROM TM_CYGNET_ALARM_ACTIONS WHERE"
					+ " POINTIDLONG=NT.OBJECT_ID) WHERE OBJECT_ID in (SELECT POINTIDLONG "
					+ "FROM TM_CYGNET_ALARM_ACTIONS WHERE ALARM_SEVERITY NOT LIKE '1-Alarm')"
					+ "AND TO_DATE(TIME)>=(SELECT CURRENT_DATE FROM DUMMY))";
			Query q = this.getSession().createSQLQuery(updateAlarmsStatus);
		//	logger.error("updateAlarmsStatus" + updateAlarmsStatus);
			updatedCount = q.executeUpdate();
			if (updatedCount > 0) {
				logger.error("Well Status Updated Successfully");
			}
		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][updateStatusOfWell][error]" + ex.getMessage());
		}

	}

	@SuppressWarnings("unused")
	public void fetchAllAlarm() {
		String fetchAlarmQuery = null;
		try {
			fetchAlarmQuery = "SELECT POINTIDLONG,ALARM_SEVERITY FROM TM_CYGNET_ALARM_ACTIONS WHERE ALARM_SEVERITY='"
					+ MurphyConstant.ALARM_SEVERITY_1_ALARM + "'"
					+ "AND TO_DATE(TIME)>=(Select CURRENT_DATE FROM DUMMY)";
			Query q = this.getSession().createSQLQuery(fetchAlarmQuery);
		//	logger.error(" fetchAlarmQuery----" + fetchAlarmQuery);
			List<Object[]> alarmListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(alarmListResponse)) {
				for (Object[] obj : alarmListResponse) {
					String objectId = ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
					String alarmStatus = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
					String module = MurphyConstant.ALARM;
					createAlertForStatusChange(objectId, module, alarmStatus);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][NotificationDao][fetchAllAlarm][error]" + e.getMessage());
		}

	}

	public void fetchHopperWells(UIRequestDto requestDto, String userName,boolean isCanada,boolean isEFS) {
		EnersightProveDaily enersightProveDaily = new EnersightProveDaily();
		try {
			Map<String, String> configMap = hopperDao.getPWHConfigValue();
			String thresholdS = configMap.get(MurphyConstant.PWH_PERCENT_VARIANCE);
			String durationS = configMap.get(MurphyConstant.PWH_DURATION_IN_DAYS);
			if (!ServicesUtil.isEmpty(durationS) && !ServicesUtil.isEmpty(thresholdS)) {
				int threshold = Integer.parseInt(thresholdS), duration = Integer.parseInt(durationS);
				
				//Set Country Fields
				List<String> efsFields = Arrays.asList(MurphyConstant.HOPPER_FIELDS);
				List<String> caFields = Arrays.asList(MurphyConstant.CA_HOPPER_FIELD);
				
				//Check for EFS and Canada User Wells
				List<String> hopperWellCaList=null;
				List<String> hopperWellEFSList=null;
				List<String> hopperWellList = new ArrayList<String>();
				if(isCanada){
				logger.error("[Murphy][updatePwHopper][Inside Canada]");
				requestDto.setLocationCodeList(caFields);
				List<String> muwiCAList = locDao.getMuwiByLocationTypeAndCode(requestDto.getLocationType(),
						requestDto.getLocationCodeList());
				if (muwiCAList.size() > 0) {
					logger.error("[Murphy][updatePwHopper][Inside Canada]{muwiCAList >0}");
					 hopperWellCaList = enersightProveDaily.getMuwiWherVarLessThanThres(muwiCAList, duration,
							threshold, configMap.get(MurphyConstant.PWH_VERSION),MurphyConstant.CA_CODE);
					logger.error("[Murphy][updatePwHopper][Inside Canada]{hopperWellCaList}"+hopperWellCaList.size());
				}
				}
				if(isEFS){
				logger.error("[Murphy][updatePwHopper][Inside EFS]");
				requestDto.setLocationCodeList(efsFields);
				List<String> muwiEFSList = locDao.getMuwiByLocationTypeAndCode(requestDto.getLocationType(),
						requestDto.getLocationCodeList());
				if (muwiEFSList.size() > 0) {
					logger.error("[Murphy][updatePwHopper][Inside Canada]{muwiEFSList >0}");
					 hopperWellEFSList = enersightProveDaily.getMuwiWherVarLessThanThres(muwiEFSList, duration,
							threshold, configMap.get(MurphyConstant.PWH_VERSION),MurphyConstant.EFS_CODE);
				logger.error("[Murphy][updatePwHopper][Inside Canada]{hopperWellEFSList}"+hopperWellEFSList.size());
				}
				}
				
				if(!ServicesUtil.isEmpty(hopperWellCaList))
					hopperWellList.addAll(hopperWellCaList);
				if(!ServicesUtil.isEmpty(hopperWellEFSList))
					hopperWellList.addAll(hopperWellEFSList);


					if (hopperWellList.size() > 0) {
						logger.error("[Murphy][updatePwHopper][Inside Canada]{hopperWellList}"+hopperWellList.size());
						insertOrUpdateWells(hopperWellList, userName);
					}
				}
			
		} catch(Exception ex) {
			logger.error("[Murphy][NotificationDao][fetchHopperWells][error]" + ex.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private void insertOrUpdateWells(List<String> hopperWellList, String userId) {
		BigInteger count = null;
		int wellCount = 0;
		Query q = null;
		try {
			String muwiList = ServicesUtil.getStringFromList(hopperWellList);
			String checkWellQuery = "SELECT COUNT(*) FROM NOTIFICATION_DETAILS WHERE OBJECT_ID IN(" + muwiList
					+ ") AND MODULE='" + MurphyConstant.PW_HOPPER + "' AND USER_ID='" + userId + "'";
			String fetchAllMuwi = "SELECT DISTINCT OBJECT_ID FROM NOTIFICATION_DETAILS WHERE MODULE='"
					+ MurphyConstant.PW_HOPPER + "'";
			q = this.getSession().createSQLQuery(checkWellQuery);
			List<BigInteger> response = (List<BigInteger>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				count = response.get(0);

				if (count.equals(BigInteger.ZERO)) {
					insertHopperWellForNotification(hopperWellList);
					removeNonHopperWells(muwiList);
				} else {
					removeNonHopperWells(muwiList);
					q = this.getSession().createSQLQuery(fetchAllMuwi);
					// list from Notification Table
					List<String> dbHopperList1 = (List<String>) q.list();
					List<String> union = new ArrayList(dbHopperList1);
					// Union DbList with HopperWell list
					union.addAll(hopperWellList);
					// Intersection is only those in both.
					List<String> intersection = new ArrayList(dbHopperList1);
					intersection.retainAll(hopperWellList);
					// Symmetric difference is all except those in both.
					List<String> symmetricDifference = new ArrayList(union);
					Boolean isremoved = symmetricDifference.removeAll(intersection);

					if (isremoved == true) {
						insertHopperWellForNotification(symmetricDifference);
					}

				}
			}

		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][insertOrUpdateWells][error]" + ex.getMessage());
		}

	}

	private void removeNonHopperWells(String wellMuwi) {
		try {
			String deleteQuery = "DELETE FROM NOTIFICATION_DETAILS WHERE OBJECT_ID  NOT IN(" + wellMuwi
					+ ") AND MODULE='" + MurphyConstant.PW_HOPPER + "'";
		//	logger.error("Delete Query" + deleteQuery);
			int delNum = this.getSession().createSQLQuery(deleteQuery.trim()).executeUpdate();
		//	logger.error("DeleteQuery" + delNum);
			if (delNum > 0) {
				logger.error("Query Deleted SuccessFully");
			}

		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][removeNonHopperWells][error]");
		}

	}

	private void insertHopperWellForNotification(List<String> hopperWellList) {
		try {
			for (String muwi : hopperWellList) {
				String objectId = muwi;
				String module = MurphyConstant.PW_HOPPER;
				String status = MurphyConstant.NEW;
				createAlertForStatusChange(objectId, module, status);
			}
		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][insertHopperWellForNotification][error]" + ex.getMessage());

		}

	}

	@SuppressWarnings("null")
	public PwHopperNotificationDto getHopperWells(String userName, String userGroup) {
		List<NotificationDto> notifyDtoList = new ArrayList<>();
		PwHopperNotificationDto hopperDtoList = new PwHopperNotificationDto();
		NotificationDto hopperDto = null;

		try {
			String fetchQuery = "SELECT OBJECT_ID,WELL_STATUS,USER_ID,LOCATION_TEXT,LOC_CODE,LOC_TYPE FROM NOTIFICATION_DETAILS WHERE IS_ACKNOWLEDGED='"
					+ MurphyConstant.NO_ACK + "' AND USER_ID='" + userName + "' AND MODULE ='"
					+ MurphyConstant.PW_HOPPER + "' AND WELL_STATUS='" + MurphyConstant.NEW + "'";
			Query q = this.getSession().createSQLQuery(fetchQuery);
			//logger.error(" HopperQuery----" + fetchQuery);
			List<Object[]> taskListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(taskListResponse)) {
				hopperDtoList = new PwHopperNotificationDto();
				for (Object[] obj : taskListResponse) {
					hopperDto = new NotificationDto();
					hopperDto.setObjectId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					hopperDto.setUserId(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					hopperDto.setStatus(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					hopperDto.setUserGroup(userGroup);
					hopperDto.setModule(MurphyConstant.PW_HOPPER);
					hopperDto.setLocationText(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					hopperDto.setLocationCode(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					hopperDto.setLocationType(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					notifyDtoList.add(hopperDto);

				}
			}
			hopperDtoList.setPwHopperNotificationList(notifyDtoList);
			if (notifyDtoList.size() > 0) {
				int hopperCount = notifyDtoList.size();
				hopperDtoList.setHopperCount(hopperCount);

			}
		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][getHopperWells][error]" + ex.getMessage());

		}
		return hopperDtoList;
	}

	@SuppressWarnings("null")
	public BypassNotificationDto getBypassNotification(String userGroup) {
		List<NotificationDto> notifyDtoList = new ArrayList<>();
		NotificationDto bypassDto = null;
		BypassNotificationDto bypassDtoList = new BypassNotificationDto();
		String userGroupList = null;
		BigInteger count = null;
		int shiftAcceptedCount = 0;
		String severityLevel = null;
		try {
			userGroupList = ServicesUtil.getStringForInQuery(userGroup);
			String shiftChangeAcceptedQuery = "SELECT count(*) FROM SAFETY_APP_NOTIFICATION WHERE MODULE IN('Bypasslog','EnergyIsolation') AND IS_ACKNOWLEDGED='"
					+ MurphyConstant.NO_ACK + "' AND USER_GROUP IN(" + userGroupList + ") AND STATUS IN('"
					+ MurphyConstant.SAFETY_REQ_ACCEPTED + "','IN PROGRESS')";
			String fetchBypassQuery = "SELECT OBJECT_ID,ACTIVITY_LOG_ID,LOCATION_TEXT,STATUS,LOCATION_CODE,LOCATION_TYPE,SEVERITY,BYPASS_NUM FROM SAFETY_APP_NOTIFICATION "
					+ "WHERE IS_ACKNOWLEDGED='" + MurphyConstant.NO_ACK + "' AND MODULE='" + MurphyConstant.BY_PASS
					+ "' AND USER_GROUP IN(" + userGroupList + ")";
		//	logger.error("BypassNewQuery----" + fetchBypassQuery);
		//	logger.error("ShiftChanges Query" + shiftChangeAcceptedQuery);
			Query q = this.getSession().createSQLQuery(shiftChangeAcceptedQuery);
			List<BigInteger> response = (List<BigInteger>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				count = response.get(0);
				shiftAcceptedCount = count.intValue();
			}
			Query query = this.getSession().createSQLQuery(fetchBypassQuery);
			List<Object[]> taskListResponse = (List<Object[]>) query.list();
			if (!ServicesUtil.isEmpty(taskListResponse)) {
				bypassDtoList = new BypassNotificationDto();
				for (Object[] obj : taskListResponse) {
					bypassDto = new NotificationDto();
					String bypasslogMsg = ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3];
					String severity = ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6];
					String locationText = ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2];
					if (severity.equalsIgnoreCase("critical")) {
						severityLevel = MurphyConstant.BP_CRITICAL;
					} else if (severity.equalsIgnoreCase("regular")) {
						severityLevel = MurphyConstant.BP_REGULAR;
					}
					else if (severity.equalsIgnoreCase("high")) {
						severityLevel = MurphyConstant.BP_HIGH;
					}
					else if (severity.equalsIgnoreCase("low")) {
						severityLevel = MurphyConstant.BP_LOW;
					}
					else if (severity.equalsIgnoreCase("medium")) {
						severityLevel = MurphyConstant.BP_MEDIUM;
					}
					String bypassNum = ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7];
					if (bypasslogMsg.equalsIgnoreCase(MurphyConstant.BYPASS_CREATION))
						bypasslogMsg = severityLevel + " BypassLog " + bypassNum + " at " + locationText
								+ " has been created.";
					else if (bypasslogMsg.equalsIgnoreCase(MurphyConstant.SHIFT_CHANGE))
						bypasslogMsg = MurphyConstant.BP_SHIFTCHNG;
					else if (bypasslogMsg.equalsIgnoreCase(MurphyConstant.BYPASS_REJECT))
						bypasslogMsg = severityLevel + " BypassLog " + bypassNum + " has been Rejected.";
					bypassDto.setObjectId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					bypassDto.setActivityLogId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					bypassDto.setLocationText(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					bypassDto.setStatus(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					bypassDto.setUserGroup(userGroup);
					bypassDto.setModule(MurphyConstant.BY_PASS);
					bypassDto.setLocationCode(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					bypassDto.setLocationType(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					bypassDto.setSafetyAppMsg(bypasslogMsg);
					bypassDto.setBypassNum(bypassNum);
					bypassDto.setSeverity(severityLevel);
					notifyDtoList.add(bypassDto);

				}
			}
			bypassDtoList.setByPassLogNotificationList(notifyDtoList);
			if (notifyDtoList.size() > 0) {
				int bypassCount = notifyDtoList.size();
				bypassDtoList.setByPassLogCount(bypassCount);
				bypassDtoList.setShiftChangeAcceptedCount(shiftAcceptedCount);

			}

		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][getBypassNotification][error]" + ex.getMessage());
		}
		return bypassDtoList;
	}

	@SuppressWarnings("null")
	public EINotificationDto fetchEnergyIsolationNotification(String userGroup) {
		List<NotificationDto> notifyDtoList = new ArrayList<>();
		EINotificationDto eiDtoList = new EINotificationDto();
		NotificationDto eiDto = null;
		String userGroupList = null;
		try {
			userGroupList = ServicesUtil.getStringForInQuery(userGroup);
			String fetchBypassQuery = "SELECT OBJECT_ID,ACTIVITY_LOG_ID,LOCATION_TEXT,STATUS,LOCATION_CODE,LOCATION_TYPE,BYPASS_NUM FROM SAFETY_APP_NOTIFICATION "
					+ "WHERE IS_ACKNOWLEDGED='" + MurphyConstant.NO_ACK + "' AND MODULE='"
					+ MurphyConstant.ENRGY_ISOLATION + "' AND USER_GROUP IN(" + userGroupList + ")";
			Query q = this.getSession().createSQLQuery(fetchBypassQuery);
		//	logger.error("EnergyIsolationQuery----" + fetchBypassQuery);
			List<Object[]> taskListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(taskListResponse)) {
				eiDtoList = new EINotificationDto();
				for (Object[] obj : taskListResponse) {
					eiDto = new NotificationDto();
					eiDto.setObjectId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					eiDto.setActivityLogId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					eiDto.setLocationText(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					eiDto.setStatus(MurphyConstant.SHIFT_CHANGE);
					eiDto.setUserGroup(userGroup);
					eiDto.setModule(MurphyConstant.ENRGY_ISOLATION);
					eiDto.setLocationCode(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					eiDto.setLocationType(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					eiDto.setSafetyAppMsg(MurphyConstant.EI_SHIFTCHNG);
					eiDto.setBypassNum(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
					notifyDtoList.add(eiDto);

				}
			}
			eiDtoList.setEnergyIsolationNotificationList(notifyDtoList);
			if (notifyDtoList.size() > 0) {
				int eiCount = notifyDtoList.size();
				eiDtoList.setEnergyIsolationCount(eiCount);

			}

		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][fetchEnergyIsolationNotification][error]" + ex.getMessage());
		}
		return eiDtoList;
	}

	public void updateAcknowledgedWells(List<NotificationDto> notifyDtoList) {
		ResponseMessage message = new ResponseMessage();
		String updateQuery = null;
		Integer updatedCount = null;
		Date date = null;
		String notifiedAt = null;
		try {
			if (!ServicesUtil.isEmpty(notifyDtoList)) {
				for (NotificationDto updateDto : notifyDtoList) {
					date = new Date();
					SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					notifiedAt = formatDate.format(date);
					updateQuery = "UPDATE NOTIFICATION_DETAILS SET IS_ACKNOWLEDGED='" + updateDto.getIsAcknowledged()
							+ "',USER_GROUP='" + updateDto.getUserGroup() + "', ACKNOWLEDGED_AT='" + notifiedAt + "'"
							+ " WHERE OBJECT_ID='" + updateDto.getObjectId() + "' AND USER_ID='" + updateDto.getUserId()
							+ "' AND MODULE='" + updateDto.getModule() + "'";

					Query q = this.getSession().createSQLQuery(updateQuery);
					updatedCount = q.executeUpdate();
					if (updatedCount > 0) {
						message.setMessage("SuccessFully Updated Notified Wells");
						message.setStatus(MurphyConstant.SUCCESS);
						message.setStatusCode(MurphyConstant.CODE_SUCCESS);
					} else {
						message.setMessage("Failed While Updating Notified Wells");
						message.setStatus(MurphyConstant.FAILURE);
						message.setStatusCode(MurphyConstant.CODE_FAILURE);
					}

				}
			}
		} catch (Exception ex) {
			logger.error("[Murphy][NotificationDao][updateAcknowledgedWells][error]" + ex.getMessage());
		}
	}

	public ResponseMessage updateSafetyAppAcknowledgedWells(List<NotificationDto> bypassDtoList) {
		ResponseMessage message = new ResponseMessage();
		String updateQuery = null;
		Integer updatedCount = null;
		Date date = null;
		String notifiedAt = null;
		try {
			if (!ServicesUtil.isEmpty(bypassDtoList)) {
				for (NotificationDto updateDto : bypassDtoList) {
					date = new Date();
					SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					notifiedAt = formatDate.format(date);
					updateQuery = "UPDATE SAFETY_APP_NOTIFICATION SET IS_ACKNOWLEDGED='" + updateDto.getIsAcknowledged()
							+ "',USER_GROUP='" + updateDto.getUserGroup() + "', ACKNOWLEDGED_AT='" + notifiedAt
							+ "',USER_ID='" + updateDto.getUserId() + "'" + " WHERE OBJECT_ID='"
							+ updateDto.getObjectId() + "' AND ACTIVITY_LOG_ID='" + updateDto.getActivityLogId()
							+ "' AND MODULE='" + updateDto.getModule() + "'";

					Query q = this.getSession().createSQLQuery(updateQuery);
					updatedCount = q.executeUpdate();
					if (updatedCount > 0) {
						message.setMessage("Status Acknowledged Successfully");
						message.setStatus(MurphyConstant.SUCCESS);
						message.setStatusCode(MurphyConstant.CODE_SUCCESS);
					} else {
						message.setMessage("Failed While Updating Notified Wells");
						message.setStatus(MurphyConstant.FAILURE);
						message.setStatusCode(MurphyConstant.CODE_FAILURE);
					}

				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][NotificationDao][updateSafetyAppAcknowledgedWells][error]" + e.getMessage());

		}
		return message;

	}
	
	public String sendNotificationForFutureTask() {
		String message = "";
		List<String> taskList = null;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.MINUTE, -5);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.MINUTE, 5);
		
		try {
			String fetchQuery = "SELECT te.task_id,tw.task_owner_email FROM TM_TASK_EVNTS te "
					+ "INNER JOIN TM_TASK_OWNER tw ON te.task_id = tw.task_id "
					+ "INNER JOIN TM_ATTR_INSTS ins ON te.task_id = ins.task_id "
					+ "INNER JOIN TM_ATTR_TEMP temp ON temp.ATTR_ID = ins.ATTR_TEMP_ID "
					+ "WHERE te.status = '"+MurphyConstant.ASSIGN +"' AND te.task_type = '"+ MurphyConstant.HUMAN 
					+"' AND temp.attr_id = '123456789' AND ins.INS_VALUE <> '' AND NOTIFICATION_FLAG = false "
					+ "AND tw.START_TIME >= TO_TIMESTAMP('" + sdf.format(cal1.getTime()) 
					+ "') AND tw.START_TIME < TO_TIMESTAMP('" + sdf.format(cal2.getTime()) +"' )";
			Query q = this.getSession().createSQLQuery(fetchQuery);
			logger.error("[sendNotificationForFutureTask][fetchQuery] : " + fetchQuery); 
			
			List<Object[]> taskListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(taskListResponse)) {
				taskList = new ArrayList<String>();
				for (Object[] obj : taskListResponse) {
					if(!ServicesUtil.isEmpty(obj[0]) && !ServicesUtil.isEmpty(obj[1])){
						// Send Notifications for all the task whose operator is in shift
						String user_email = (String) obj[1];
						boolean in_shift = shiftRegisterDao.getShiftDetailsForEmp(user_email);
						if(in_shift){
							// Get user_login_name to send notifications
							String user_login_name = (String) this.getSession().createSQLQuery(
									"SELECT user_login_name FROM tm_user_idp_mapping WHERE user_email = '" + user_email + "'")
									.uniqueResult();
							// call notification API
							pushNotificationForTask(user_login_name,"You have a new task assigned");
							taskList.add((String) obj[0]);
						}
					}
				}
			}
			// Update notification_flag in table as true for all the task_ids where notification is sent
			if(!ServicesUtil.isEmpty(taskList) && taskList.size() > 0){
				String task_ids = ServicesUtil.getStringFromList(taskList);
				String updateQuery = "UPDATE TM_TASK_EVNTS SET NOTIFICATION_FLAG = true WHERE TASK_ID in (" + task_ids + ")"; 
				
				Query q1 = this.getSession().createSQLQuery(updateQuery);
				int updatedCount = q1.executeUpdate();
				if (updatedCount > 0) 
					message = "Notifications for future tasks sent successfully";
				else 
					message = "No notifications to be sent";
			}
			else{
				message = "No future tasks available in this run";
			}

		} catch (Exception ex) {
			message = "Failed While sending notifications for future tasks";
			logger.error("[Murphy][NotificationDao][sendNotificationForFutureTask][error]" + ex.getMessage());
		}
        return message;
	}
	
	public void pushNotificationForTask(String task_owner, String alert) {
		try {
			List<String> ownerList = new ArrayList<String>();
			ownerList.add(task_owner);
			
			JSONObject rootJson = new JSONObject();
			JSONObject notificationJson = new JSONObject();
			notificationJson.put("alert", alert);
			notificationJson.put("badge", 5);
			notificationJson.put("sound", "iphone_alarm.caf");
			
			rootJson.put("notification", notificationJson);
			rootJson.put("users", ownerList);

			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			JSONObject jsonObject = RestUtil.callRest(MurphyConstant.PUSH_NOTIFICATION_URL, rootJson.toString(), "POST",
					MurphyConstant.PUSH_NOTIFICATION_USERNAME, MurphyConstant.PUSH_NOTIFICATION_PASSWORD);
			logger.error("[pushNotificationForTask]Response onject: " + jsonObject);
		} catch (Exception e) {
			logger.error("[Murphy][NotificationDao][pushNotificationForTask][error]" + e.getMessage());

		}
	}

}
