package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.ActivityLogDto;
import com.murphy.taskmgmt.entity.ActivityLogDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ActivityLogDao")
@Transactional
public class ActivityLogDao extends BaseDao<ActivityLogDo, ActivityLogDto> {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityLogDao.class);
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][ActivityLogDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}

	protected ActivityLogDo importDto(ActivityLogDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		ActivityLogDo entity = new ActivityLogDo();
		entity.setId(fromDto.getId());
		entity.setFormId(fromDto.getFormId());
		entity.setIsApproved(fromDto.getIsApproved());
		entity.setPermIssueName(fromDto.getPermIssueName());
		entity.setPermIssueLoginName(fromDto.getPermIssueLoginName());
		entity.setPermIssueTime(fromDto.getPermIssueTime());
		entity.setType(fromDto.getType());
		return entity;
	}

	protected ActivityLogDto exportDto(ActivityLogDo entity) {
		ActivityLogDto toDto = new ActivityLogDto();
		toDto.setId(entity.getId());
		toDto.setFormId(entity.getFormId());
		toDto.setIsApproved(entity.getIsApproved());
		toDto.setPermIssueName(entity.getPermIssueName());
		toDto.setPermIssueLoginName(entity.getPermIssueLoginName());
		toDto.setPermIssueTime(entity.getPermIssueTime());
		toDto.setType(entity.getType());
		return toDto;
	}

	@SuppressWarnings("unchecked")
	public List<ActivityLogDto> getByFk(String fk) {
		List<ActivityLogDto> responseList = null;
		String query = "select ID, FORM_ID, PERM_ISSUER_NAME, IS_APPROVED, PERM_ISSUE_TIME, TYPE, PERM_ISSUER_LOGIN_NAME "
				+ "from EI_ACTIVITY_LOG ei where ei.FORM_ID = '" + fk + "' and ei.TYPE = 'PERMIT ISSUER' ORDER BY PERM_ISSUE_TIME";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			responseList = new ArrayList<ActivityLogDto>();
			ActivityLogDto activity = null;
			for (Object[] obj : resultList) {
				activity = new ActivityLogDto();
				activity.setId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				activity.setFormId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				activity.setPermIssueName(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
				activity.setIsApproved(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
				activity.setPermIssueTime(ServicesUtil.isEmpty(obj[4]) ? null : (Date) obj[4]);
				activity.setType(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
				activity.setPermIssueLoginName(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
				responseList.add(activity);
			}
		}
		return responseList;
	}
	
	public void createList(List<ActivityLogDto> list, String formId) {
		if (list != null) {
			for (ActivityLogDto activityLogDto : list) {
				activityLogDto.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				activityLogDto.setFormId(formId);
				try {
					create(activityLogDto);
				} catch (ExecutionFault e) {
					e.printStackTrace();
				} catch (InvalidInputFault e) {
					e.printStackTrace();
				} catch (NoResultFault e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateList(List<ActivityLogDto> list, String id, String formId) {
		if (list != null) {
			for (ActivityLogDto activityLogDto : list) {
				try {
					if (activityLogDto.getId() == null) {
						activityLogDto.setId(UUID.randomUUID().toString().replaceAll("-", ""));
						activityLogDto.setFormId(formId);
						create(activityLogDto);
						id = id.substring(id.length() - 6);
					} else if (activityLogDto.getFormId() == null) {
						activityLogDto.setFormId(formId);
						update(activityLogDto);
					} else {
						update(activityLogDto);
					}
				} catch (ExecutionFault e) {
					e.printStackTrace();
				} catch (InvalidInputFault e) {
					e.printStackTrace();
				} catch (NoResultFault e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void notifyUser(ActivityLogDto activity, String locationName, String equipId) {
		List<String> operatorIdList = new ArrayList<String>();
		String operatorId = activity.getPermIssueLoginName();
		operatorIdList.add(operatorId);
		String formId = activity.getFormId();
		String activityLogId = activity.getId();
		try {
			String dataJson = "ISOLATION_SHIFTCHANGE_ACTION" + "," + formId + "," + activityLogId + "," + new Date();
			String alert = "You have been assigned the Energy Isolation at " + locationName + " located on the "
					+ equipId + ". Please Accept to keep equipment in Energy Isolation.";
			JSONObject rootJson = new JSONObject();
			JSONObject notificationJson = new JSONObject();
			notificationJson.put("alert", alert);
			
			if (!ServicesUtil.isEmpty(dataJson)) {
				notificationJson.put("data", dataJson);
			}

			notificationJson.put("badge", 1);
			notificationJson.put("sound", "iphone_alarm.caf");
			rootJson.put("notification", notificationJson);
			rootJson.put("users", operatorIdList);

			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			JSONObject jsonObject = RestUtil.callRest(MurphyConstant.PUSH_NOTIFICATION_URL, rootJson.toString(), "POST",
					MurphyConstant.PUSH_NOTIFICATION_USERNAME, MurphyConstant.PUSH_NOTIFICATION_PASSWORD);
			logger.error("[Murphy][ActivityLogDao] inside try JSON object: " + jsonObject);
		} catch (Exception e) {
			logger.error("[Murphy][ActivityLogDao][notifyUser][error]" + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public String updateById(String id, String value) {
		String query = "UPDATE EI_ACTIVITY_LOG SET IS_APPROVED = '" + value + "' WHERE ID = '" + id + "'";
		Query q = this.getSession().createSQLQuery(query);
		q.executeUpdate();
		query = "select al.FORM_ID, ei.ID from EI_ACTIVITY_LOG al, EI_FORM ei where al.ID = '" + id + "' AND ei.FORM_ID = al.FORM_ID";
		q = this.getSession().createSQLQuery(query);
		List<Object[]> response = (List<Object[]>) q.list();
		String formId = null;
		String tempId = null;
		if (!ServicesUtil.isEmpty(response)) {
			for (Object[] obj : response) {
				formId = ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
				tempId = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
				break;
			}
		}
		if (value.equals("True")) {
			query = "update EI_FORM set PERM_ISSUE_NAME = (select PERM_ISSUER_NAME from EI_ACTIVITY_LOG where ID = '" + id + "'),"
					+ " PERM_ISSUER_LOGIN_NAME = (select PERM_ISSUER_LOGIN_NAME from EI_ACTIVITY_LOG where ID = '" + id + "'),"
					+ " IS_ACKNOWLEDGED = True,"
					+ " UPDATED_AT = TO_TIMESTAMP('"+ServicesUtil.convertFromZoneToZoneString(new Date(), null, "", "","", MurphyConstant.DATE_DB_FORMATE_SD)+"', 'yyyy-MM-dd HH24:mi:ss')"
					+ " where FORM_ID = '" + formId + "'";
			logger.error(query);
			q = this.getSession().createSQLQuery(query);
			q.executeUpdate();
		} else {
			query = "update EI_FORM set"
					+ " IS_ACKNOWLEDGED = False,"
					+ " UPDATED_AT = TO_TIMESTAMP('"+ServicesUtil.convertFromZoneToZoneString(new Date(), null, "", "","", MurphyConstant.DATE_DB_FORMATE_SD)+"', 'yyyy-MM-dd HH24:mi:ss')"
					+ " where FORM_ID = '" + formId + "'";
			logger.error(query);
			q = this.getSession().createSQLQuery(query);
			q.executeUpdate();
		}
		return tempId;
	}
	
	public void createRocActivity(List<String> formId) {
		for (String form : formId) {
			ActivityLogDto activity = new ActivityLogDto();
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			activity.setId(id);
			activity.setIsApproved("Null");
			activity.setFormId(form);
			activity.setType("ROC");
			activity.setPermIssueTime(new Date());
			try {
				create(activity);
			} catch (ExecutionFault e) {
				e.printStackTrace();
			} catch (InvalidInputFault e) {
				e.printStackTrace();
			} catch (NoResultFault e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getActivityStatus(String id) {
		this.getSession().flush();
		String query = "select IS_APPROVED from EI_ACTIVITY_LOG  where ID = '" + id + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<String> response = (List<String>) q.list();
		String isApproved = null;
		if (!ServicesUtil.isEmpty(response) && response.size() > 0) {
			for (String temp : response) {
				isApproved = temp;
				break;
			}
		}
		return isApproved;
	}
	
	public void updateStatusById(String id, String value) {
		String query = "UPDATE EI_ACTIVITY_LOG SET IS_APPROVED = '" + value + "' WHERE ID = '" + id + "'";
		Query q = this.getSession().createSQLQuery(query);
		q.executeUpdate();
		this.getSession().flush();
	}
	
	public void deleteRocActivities(List<String> activeForms) {
		String activeFormForIn = ServicesUtil.getStringFromList(activeForms);
		String query = "DELETE FROM EI_ACTIVITY_LOG where FORM_ID in (" + activeFormForIn + ") AND TYPE = 'ROC'";
		Query q = this.getSession().createSQLQuery(query);
		q.executeUpdate();
		this.getSession().flush();
	}
	
	public ActivityLogDto createActivity(String formId, String permIssueName, String permIssueLoginName) {
		ActivityLogDto activity = new ActivityLogDto();
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		activity.setFormId(formId);
		activity.setId(id);
		activity.setIsApproved("True");
		activity.setPermIssueName(permIssueName);
		activity.setPermIssueLoginName(permIssueLoginName);
		activity.setPermIssueTime(new Date());
		activity.setType("PERMIT ISSUER");
		return activity;
	}
	
}
