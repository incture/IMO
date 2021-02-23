package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import com.murphy.taskmgmt.dto.EIFormDto;
import com.murphy.taskmgmt.dto.EIFormListDto;
import com.murphy.taskmgmt.dto.EIFormListResponseDto;
import com.murphy.taskmgmt.dto.EIFormRolledUpDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.EIFormDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("EIFormDao")
@Transactional
public class EIFormDao extends BaseDao<EIFormDo, EIFormDto> {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IsolationDetailDao isolationDetailDao;

	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
	
	private static final Logger logger = LoggerFactory.getLogger(EIFormDao.class);
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][EIFormDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}

	@Override
	protected EIFormDo importDto(EIFormDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		EIFormDo entity = new EIFormDo();
		entity.setId(fromDto.getIdOrg());
		entity.setFormId(fromDto.getFormId());
		entity.setDate(fromDto.getDate());
		entity.setPlannedDate(fromDto.getPlannedDate());
		entity.setLocationId(fromDto.getLocationId());
		entity.setLocationName(fromDto.getLocationName());
		entity.setPermHoldName(fromDto.getPermHoldName());
		entity.setPermIssueName(fromDto.getPermIssueName());
		entity.setPermIssueLoginName(fromDto.getPermIssueLoginName());
//		entity.setContractorName(fromDto.getContractorName());
		entity.setExpectTime(fromDto.getExpectTime());
		entity.setPermIssueTime(fromDto.getPermIssueTime());
		entity.setPermHoldTime(fromDto.getPermHoldTime());
		entity.setEquipId(fromDto.getEquipId());
		entity.setWorkOrderNum(fromDto.getWorkOrderNum());
		entity.setEnergyType(fromDto.getEnergyType());
		entity.setReason(fromDto.getReason());
		entity.setOtherHazards(fromDto.getOtherHazards());
		entity.setEmpNotified(fromDto.isEmpNotified());
		entity.setLotoRemoved(fromDto.isLotoRemoved());
		entity.setLockTagRemoved(fromDto.isLockTagRemoved());
		entity.setWAInspected(fromDto.isWAInspected());
		entity.setJSAReviewed(fromDto.isJSAReviewed());
		entity.setStatus(fromDto.getStatus());
		entity.setText(fromDto.isText());
		entity.setAcknowledged(fromDto.isAcknowledged());
		entity.setUserGroup(fromDto.getUserGroup());
		entity.setCreatedAt(fromDto.getCreatedAt());
		entity.setUpdatedAt(fromDto.getUpdatedAt());
		entity.setShift(fromDto.getShift());
		entity.setLastShiftChange(fromDto.getLastShiftChange());
		
		return entity;
	}

	@Override
	public EIFormDto exportDto(EIFormDo entity) {
		EIFormDto toDto = new EIFormDto();
		toDto.setIdOrg(entity.getId());
		toDto.setId(entity.getId().substring(entity.getId().length() - 6));
		toDto.setFormId(entity.getFormId());
		toDto.setDate(entity.getDate());
		toDto.setPlannedDate(entity.getPlannedDate());
		toDto.setLocationId(entity.getLocationId());
		toDto.setLocationName(entity.getLocationName());
		toDto.setPermHoldName(entity.getPermHoldName());
		toDto.setPermIssueName(entity.getPermIssueName());
		toDto.setPermIssueLoginName(entity.getPermIssueLoginName());
//		toDto.setContractorName(entity.getContractorName());
		toDto.setExpectTime(entity.getExpectTime());
		toDto.setPermIssueTime(entity.getPermIssueTime());
		toDto.setPermHoldTime(entity.getPermHoldTime());
		toDto.setEquipId(entity.getEquipId());
		toDto.setWorkOrderNum(entity.getWorkOrderNum());
		toDto.setEnergyType(entity.getEnergyType());
		toDto.setReason(entity.getReason());
		toDto.setOtherHazards(entity.getOtherHazards());
		toDto.setEmpNotified(entity.isEmpNotified());
		toDto.setLotoRemoved(entity.isLotoRemoved());
		toDto.setLockTagRemoved(entity.isLockTagRemoved());
		toDto.setWAInspected(entity.isWAInspected());
		toDto.setJSAReviewed(entity.isJSAReviewed());
		toDto.setStatus(entity.getStatus());
		toDto.setUserGroup(entity.getUserGroup());
		toDto.setText(entity.isText());
		toDto.setAcknowledged(entity.isAcknowledged());
		toDto.setCreatedAt(entity.getCreatedAt());
		toDto.setUpdatedAt(entity.getUpdatedAt());
		toDto.setShift(entity.getShift());
		toDto.setLastShiftChange(entity.getLastShiftChange());
		
		return toDto;
	}
	
	@SuppressWarnings("unchecked")
	public EIFormListResponseDto getEIFormByLoc(String locationCodeString, String monthTime, int week, int page,
			int page_size,boolean isActive) {
		EIFormListResponseDto responseDto = new EIFormListResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		List<EIFormListDto> formList = null;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		if (monthTime.equalsIgnoreCase("one"))
			cal.add(Calendar.DATE, -30);
		else if (monthTime.equalsIgnoreCase("three"))
			cal.add(Calendar.DATE, -90);
		if (week > 0) {
			cal.add(Calendar.DATE, -(7 * week));
		}
		Integer countResult = 0;
		Query query = null;
		String queryString = "select i.ID, i.FORM_ID, i.EQUIP_ID, i.STATUS, i.CREATED_AT, i.UPDATED_AT, i.LOCATION_ID, i.LOCATION_NAME "
				+ "from EI_FORM as i where ";

		responseDto.setResponseMessage(responseMessage);

		try {
			if (!ServicesUtil.isEmpty(locationCodeString)) {
				queryString += " i.LOCATION_ID in (" + locationCodeString + ") and ";
			}
			if(isActive){
				queryString += " i.STATUS = 'IN PROGRESS' order by i.CREATED_AT desc ";
			}
			queryString += " (TO_DATE(i.CREATED_AT) >= '" + sdf.format(cal.getTime()) + "' "
					+ " or i.STATUS = 'IN PROGRESS'" + ") order by i.STATUS desc, i.CREATED_AT desc ";

			String countQueryString = "SELECT COUNT(*) AS COUNT FROM " + "(" + queryString + ")";
			query = this.getSession().createSQLQuery(countQueryString);
			countResult = ((BigInteger) query.uniqueResult()).intValue();

			if (page > 0) {
				int first = (page - 1) * page_size;
				int last = page_size;
				queryString += "LIMIT " + last + " OFFSET " + first + "";
			}

			logger.error("[EIFormDao][getEIFormByLoc] Query: " + queryString);

			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = (List<Object[]>) q.list();

			if (!ServicesUtil.isEmpty(response)) {
				EIFormListDto dto = null;
				formList = new ArrayList<EIFormListDto>();
				for (Object[] obj : response) {
					dto = new EIFormListDto();
					dto.setId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setFormId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					dto.setEquipmentId(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					dto.setStatus(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setCreatedAt(ServicesUtil.isEmpty(obj[4]) ? null : (Date) obj[4]);
					dto.setUpdatedAt(ServicesUtil.isEmpty(obj[5]) ? null : (Date) obj[5]);
					dto.setLocationId(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
					dto.setLocationName(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
					EIFormDto eiFormDto = new EIFormDto();
					String formId = dto.getFormId();
						eiFormDto.setFormId(formId);
						eiFormDto = getByKeys(eiFormDto);
						eiFormDto.setIsolationList(isolationDetailDao.getByFk(formId));
					dto.setEiFormDto(eiFormDto);
					if (dto.getId() != null) {
						dto.setId(dto.getId().substring(dto.getId().length() - 6));
					}
					formList.add(dto);
				}
				responseDto.setFormList(formList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				responseDto.getResponseMessage().setStatus(MurphyConstant.SUCCESS);
				responseDto.getResponseMessage().setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
			responseDto.setPageCount(new BigDecimal(page_size));
			responseDto.setTotalCount(new BigDecimal(countResult));
		} catch (Exception e) {
			System.err.println("[Murphy][EIFormDao][getEIFormByLoc][error]" + e.getMessage());
		}
		return responseDto;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<EIFormRolledUpDto> rolledUpData(String locationCodeString) {
		List<EIFormRolledUpDto> rolledUpDtoList = null;
		Query q;
		String queryString = "select distinct STATUS, COUNT(*) from EI_FORM ";
		if (!ServicesUtil.isEmpty(locationCodeString)) {
			queryString += "where LOCATION_ID in (" + locationCodeString + ") ";
		}
		queryString += "GROUP BY STATUS";
		q = this.getSession().createSQLQuery(queryString);
		List<Object[]> response = (List<Object[]>) q.list();
		if (!ServicesUtil.isEmpty(response)) {
			rolledUpDtoList = new ArrayList<EIFormRolledUpDto>();
			for (Object[] obj : response) {
				EIFormRolledUpDto rolledUpDto = new EIFormRolledUpDto();
				rolledUpDto.setStatus(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				rolledUpDto.setCount(ServicesUtil.isEmpty(obj[1]) ? null : (BigInteger) obj[1]);
				rolledUpDtoList.add(rolledUpDto);
			}
		}
		return rolledUpDtoList;
	}
	
	//original method of getActiveForms
//	@SuppressWarnings("unchecked")
//	public List<String> getActiveForms() {
//		
//		Calendar calRegularMax = Calendar.getInstance();
//		calRegularMax.add(calRegularMax.HOUR, -12);
//		Calendar calRegularMin = Calendar.getInstance();
//		calRegularMin.add(calRegularMin.HOUR, -12);
//		calRegularMin.add(calRegularMin.MINUTE, -5);
//		
//		Calendar calDailyMax = Calendar.getInstance();
//		calDailyMax.add(calDailyMax.DAY_OF_YEAR, -1);
//		Calendar calDailyMin = Calendar.getInstance();
//		calDailyMin.add(calDailyMin.DAY_OF_YEAR, -1);
//		calDailyMin.add(calDailyMin.MINUTE, -5);
//		
//		Calendar calWeeklyMax = Calendar.getInstance();
//		calWeeklyMax.add(calWeeklyMax.WEEK_OF_YEAR, -1);
//		Calendar calWeeklyMin = Calendar.getInstance();
//		calWeeklyMin.add(calWeeklyMin.WEEK_OF_YEAR, -1);
//		calWeeklyMin.add(calWeeklyMin.MINUTE, -5);
//		
//		String queryString = "select FORM_ID from EI_FORM where STATUS = '" + MurphyConstant.INPROGRESS + "' and ("
//				+ "(SHIFT = 'Regular' and LAST_SHIFT_CHANGE between TO_TIMESTAMP('" + sdf.format(calRegularMin.getTime()) + "') and TO_TIMESTAMP('" + sdf.format(calRegularMax.getTime()) + "')) or "
//						+ "(SHIFT = 'Daily' and LAST_SHIFT_CHANGE between TO_TIMESTAMP('" + sdf.format(calDailyMin.getTime()) + "') and TO_TIMESTAMP('" + sdf.format(calDailyMax.getTime()) + "')) or "
//						+ "(SHIFT = 'Weekly' and LAST_SHIFT_CHANGE between TO_TIMESTAMP('" + sdf.format(calWeeklyMin.getTime()) + "') and TO_TIMESTAMP('" + sdf.format(calWeeklyMax.getTime()) + "')))";
////		String queryString = "select FORM_ID from EI_FORM where STATUS = '" + MurphyConstant.INPROGRESS + "'";
//		Query q = this.getSession().createSQLQuery(queryString);
//		
//		logger.error("Active forms query: " + queryString);
//		try {
//			List<String> response = (List<String>) q.list();
//			logger.error("Active forms: " + response);
//			return response;
//		} catch (Exception e) {
//			return null;
//		}
//	}
//	
	@SuppressWarnings("unchecked")
	public void pushDataToNotifyRoc(List<String> activeForms) {
		String activeFormForIn = ServicesUtil.getStringFromList(activeForms);
		String queryString = "select ei.FORM_ID as FORM_ID, al.ID as ID, ei.LOCATION_NAME as LOCATION_NAME, ei.USER_GROUP as USER_GROUP, "
				+ "ei.PERM_ISSUER_LOGIN_NAME as PERM_ISSUER_LOGIN_NAME, ei.STATUS as STATUS, ei.LOCATION_ID as LOCATION_ID, ei.ID as ID_TEMP "
				+ "from EI_FORM as ei, EI_ACTIVITY_LOG as al "
				+ "where ei.FORM_ID in (" + activeFormForIn + ") "
				+ "and al.FORM_ID = ei.FORM_ID and al.TYPE = 'ROC'";
//		logger.error(queryString);
		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> response = (List<Object[]>) q.list();
		
		String formId, activityLogId, locationText, locationCode, userGroup, userId, status, id = null;
		if (!ServicesUtil.isEmpty(response)) {
			for (Object[] obj : response) {
				formId = ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
				activityLogId = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
				locationText = ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2];
				userGroup = ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3];
				userId = ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4];
				status = ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5];
				locationCode = ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6];
				id = ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7];
//				logger.error(formId);
				insertDataForPushNotificationToRoc(formId, activityLogId, "EnergyIsolation", 
						locationText, "false", userGroup, userId, status, "Well", locationCode, null, id);
			}
		}
	}
	
	private void insertDataForPushNotificationToRoc(String objectId, String activityLogId, String module,
			String locationText, String isAcknowledged, String userGroup, String userId, String status, String locationType,
			String locationCode, String severity, String bypassNum) {
		int response = 0;
		String bypassNumber = "";
		try {
			if ((!ServicesUtil.isEmpty(objectId)) && (!ServicesUtil.isEmpty(activityLogId))
					&& (!ServicesUtil.isEmpty(userGroup))) {
				bypassNumber= String.valueOf(Long.parseLong(bypassNum)%1000000);
				String query = "INSERT INTO safety_app_notification VALUES( '" + objectId + "', '" + activityLogId
						+ "', '" + module + "', '" + locationText + "', '" + isAcknowledged + "', '" + userGroup
						+ "', '" + userId + "', '" + status + "', null, null , '"  
						+ locationCode + "', '" + locationType + "', '" + severity + "', '" + bypassNumber + "')";

				response = this.getSession().createSQLQuery(query).executeUpdate();
				logger.error(String.valueOf(response));
			}

		} catch (Exception e) {
			logger.error("[Murphy][EIFormDao][insertDataForPushNotificationToRoc][error]" + e.getMessage());

		}

	}
	
	public void resetAck(List<String> activeForms) {
		String activeFormForIn = ServicesUtil.getStringFromList(activeForms);
		String query = "UPDATE EI_FORM SET IS_ACKNOWLEDGED = False, LAST_SHIFT_CHANGE = TO_TIMESTAMP('" + sdf.format(new Date()) + "') WHERE FORM_ID in (" + activeFormForIn + ")";
		logger.error("[Murphy][EIFormDao][resetAck] Query: " + query);
		Query q = this.getSession().createSQLQuery(query);
		q.executeUpdate();
		this.getSession().flush();
	}
	
	public void setAck(List<String> activeForms) {
		String activeFormForIn = ServicesUtil.getStringFromList(activeForms);
		String query = "UPDATE EI_FORM SET IS_ACKNOWLEDGED = True WHERE FORM_ID in (" + activeFormForIn + ")";
		logger.error("[Murphy][EIFormDao][resetAck] Query: " + query);
		Query q = this.getSession().createSQLQuery(query);
		q.executeUpdate();
		this.getSession().flush();
	}
	
	
	//added for EI incident
	@SuppressWarnings("unchecked")
	public List<String> getActiveForms() {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
	ZonedDateTime zd = ZonedDateTime.now(ZoneId.of("America/Chicago"));
	ZonedDateTime curTime = zd;

	ZonedDateTime calRegularMax = zd;
	ZonedDateTime calRegularMin = zd;

	ZonedDateTime calDailyMin = zd.withHour(5).withMinute(0).withSecond(0);
	ZonedDateTime calDailyMax = zd.plusDays(1).withHour(4).withMinute(59).withSecond(59);

	 


	ZonedDateTime calWeeklyMax = zd.plusDays(1).withHour(4).withMinute(59).withSecond(59);
	ZonedDateTime calWeeklyMin = zd.minusWeeks(1).plusDays(1).withHour(5).withMinute(0).withSecond(0);

	if(curTime.getHour() >= 5 && curTime.getHour() <=16){

	calRegularMax = calRegularMax.withHour(16).withMinute(59).withSecond(59);
	calRegularMin = calRegularMin.withHour(5).withMinute(0).withSecond(0);

	 

	}
	else if(curTime.getHour() < 5){
	calRegularMax = calRegularMax.withHour(4).withMinute(59).withSecond(59);
	calRegularMin = calRegularMin.minusDays(1).withHour(17).withMinute(0).withSecond(0);

	calDailyMin = calDailyMin.minusDays(1);
	calDailyMax = calDailyMax.minusDays(1);

	calWeeklyMax = calWeeklyMax.minusDays(1);
	calWeeklyMin = calWeeklyMin.minusDays(1);

	}
	else {

	calRegularMax = calRegularMax.plusDays(1);
	calRegularMax = calRegularMax.withHour(4).withMinute(59).withSecond(49);

	 

	calRegularMin = calRegularMin.withHour(17).withMinute(0).withSecond(0);
	}

	 

	String queryString = "select FORM_ID from EI_FORM where STATUS = '" + MurphyConstant.INPROGRESS + "' and ("
	+ "(SHIFT = 'Regular' and UTCTOLOCAL(LAST_SHIFT_CHANGE,'CST') not between TO_TIMESTAMP('" + calRegularMin.format(formatter) + "') and TO_TIMESTAMP('" + calRegularMax.format(formatter) + "')) or "
	+ "(SHIFT = 'Daily' and UTCTOLOCAL(LAST_SHIFT_CHANGE,'CST') not between TO_TIMESTAMP('" + calDailyMin.format(formatter) + "') and TO_TIMESTAMP('" + calDailyMax.format(formatter) + "')) or "
	+ "(SHIFT = 'Weekly' and UTCTOLOCAL(LAST_SHIFT_CHANGE,'CST') not between TO_TIMESTAMP('" + calWeeklyMin.format(formatter) + "') and TO_TIMESTAMP('" + calWeeklyMax.format(formatter) + "')))";
	Query q = this.getSession().createSQLQuery(queryString);

	logger.error("Active forms query: " + queryString);
	try {
	List<String> response = (List<String>) q.list();
	logger.error("Active forms: " + response);
	return response;
	} catch (Exception e) {
	return null;
	}
	}
	
}
