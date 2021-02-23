/**
 * 
 */
package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.integration.util.ServicesUtil;
import com.murphy.taskmgmt.dto.ATSTaskAssginmentDto;
import com.murphy.taskmgmt.entity.ATSTaskAssginmentDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;

/**
 * @author Rashmendra.Sai
 *
 */
@Repository("ATSTaskAssignmentDao")
public class ATSTaskAssignmentDao extends BaseDao<ATSTaskAssginmentDo, ATSTaskAssginmentDto> {

	private static final Logger logger = LoggerFactory.getLogger(ATSTaskAssignmentDao.class);

	@Override
	protected ATSTaskAssginmentDo importDto(ATSTaskAssginmentDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ATSTaskAssginmentDo entity = new ATSTaskAssginmentDo();
		entity.setEmailID(fromDto.getEmailID());
		entity.setEndTimeOfTask(fromDto.getEndTimeOfTask());
		entity.setLocCode(fromDto.getLocCode());
		entity.setLocText(fromDto.getLocText());
		entity.setSequence(fromDto.getSequence());
		entity.setStartTimeOfTask(fromDto.getStartTimeOfTask());
		entity.setTaskID(fromDto.getTaskID());
		entity.setWellTier(fromDto.getWellTier());
		entity.setRisk(fromDto.getRisk());
		entity.setStopTimeForTask(fromDto.getStopTimeForTask());
		entity.setUserDesignation(fromDto.getUserDesignation());
		entity.setRoadDriveTime(fromDto.getRoadDriveTime());
		entity.setHasAssigned(fromDto.getHasAssigned());
		return entity;
	}

	@Override
	protected ATSTaskAssginmentDto exportDto(ATSTaskAssginmentDo entity) {
		ATSTaskAssginmentDto dto = new ATSTaskAssginmentDto();
		dto.setEmailID(entity.getEmailID());
		dto.setLocCode(entity.getLocCode());
		dto.setLocText(entity.getLocText());
		dto.setEndTimeOfTask(entity.getEndTimeOfTask());
		dto.setSequence(entity.getSequence());
		dto.setStartTimeOfTask(entity.getStartTimeOfTask());
		dto.setTaskID(entity.getTaskID());
		dto.setWellTier(entity.getWellTier());
		dto.setRisk(entity.getRisk());
		dto.setStopTimeForTask(entity.getStopTimeForTask());
		dto.setUserDesignation(entity.getUserDesignation());
		dto.setRoadDriveTime(entity.getRoadDriveTime());
		dto.setHasAssigned(entity.getHasAssigned());
		return dto;
	}

	public void insertTasks(ATSTaskAssginmentDto taskAssignDto) {
		try {
			logger.error("[insertTasks] [Before Inserting in Table] ");
			create(taskAssignDto);
			logger.error("[insertTasks] [After Inserting in Table] ");
		} catch (Exception e) {
			logger.error("[ATSTaskAssignmentDao][insertTasks] Exception " + e.getMessage());
		}
	}

	public void insertAllTasksList(List<ATSTaskAssginmentDto> atsList) {
		deletePrevDayTask();
		try {
			for (ATSTaskAssginmentDto task : atsList) {
				try {
					create(task);
				} catch (Exception e) {
					logger.error("[ATSTaskAssignmentDao][insertAllTasksList] [Inner Exception] " + e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("[ATSTaskAssignmentDao][insertAllTasksList] Exception " + e.getMessage());
		}
	}

	public String deletePrevDayTask() {
		String response = MurphyConstant.FAILURE;
		try {
			String query = "DELETE FROM ATS_TASK_ASSIGNMENT";
			Query q = this.getSession().createSQLQuery(query);
			int result = (int) q.executeUpdate();
			if(result>=0)
				response = MurphyConstant.SUCCESS;
			logger.error("Task deleted " + result);
		} catch (Exception e) {
			logger.error("[ATSTaskAssignmentDao][deletePrevDayTask] Exception " + e.getMessage());
		}
		return response;
	}

	// Update TE,PE : Status change ; TOwner: email, Start Time, End Time
	@SuppressWarnings("unchecked")
	public String fetchATSTaskandUpdateTable() {
		String response = MurphyConstant.FAILURE;
		try {
			String fetchQuery = "select TASK_ID, TASK_OWNER_EMAIL, TASK_START_TIME, TASK_END_TIME , TIER,DRIVE_TIME,TASK_STOP_TIME from ATS_TASK_ASSIGNMENT where HAS_TASK_ASSIGNED = 'FALSE'";
			Query q = this.getSession().createSQLQuery(fetchQuery);
			List<Object[]> responseList = q.list();
			if (!ServicesUtil.isEmpty(responseList)) {
				for (Object[] obj : responseList) {
					if (!ServicesUtil.isEmpty(obj)) {
						// Updating Task Evnts and Proc Evnts on passing taskId
						response = updateStatusTaskEvntTable((String) obj[0], (Date) obj[2]);
						if (response.equalsIgnoreCase(MurphyConstant.SUCCESS))
							response = updateTaskOwnerTable((String) obj[0], (String) obj[1], (Date) obj[2],
									(Date) obj[3], (String) obj[4], (BigDecimal) obj[5], (int) obj[6] );

					}
				}
			} else{
				logger.error("[No Response]");
			}
		} catch (Exception e) {
			logger.error("[ATSTaskAssignmentDao][fetchATSTaskandUpdateTable] Exception " + e.getMessage());
		}
		return response;
	}

	public String updateStatusTaskEvntTable(String taskID, Date startTime) {
		String response = MurphyConstant.FAILURE;
		try {
			String queryTaskStatusUpdate = "UPDATE TM_TASK_EVNTS SET STATUS ='" + MurphyConstant.ASSIGN
					+ "',CREATED_AT = '"+startTime+"' WHERE (TASK_ID IN ('" + taskID + "') and status='" + MurphyConstant.NEW_TASK + "')";
			logger.error("updateStatusTaskEvntTable Query: " + queryTaskStatusUpdate);

			int result = this.getSession().createSQLQuery(queryTaskStatusUpdate).executeUpdate();
			logger.error("[updateStatusInTaskEvntTable][UpdateCount] " + result);
			if (result > 0) {
				response = MurphyConstant.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[ATSTaskAssignmentDao][updateStatusTaskEvntTable] Exception " + e.getMessage());
		}
		return response;
		
	}

	/*
	 * public void updateStatusProcEvntTable(String taskID) { try { String
	 * queryProcStatusUpdate = "UPDATE TM_PROC_EVNTS pe SET pe.STATUS ='" +
	 * MurphyConstant.ASSIGN + "' WHERE process_id IN (" +
	 * "select te.process_id from tm_task_evnts te where te.task_id in('" +
	 * taskID + "') and te.status ='" + MurphyConstant.ASSIGN +
	 * "') and pe.status='" + MurphyConstant.NEW_TASK + "')";
	 * logger.error("updateStatusProcEvntTable Query: " +
	 * queryProcStatusUpdate); int result =
	 * this.getSession().createSQLQuery(queryProcStatusUpdate).executeUpdate();
	 * logger.error("[updateStatusProcEvntTable][UpdateCount] " + result); }
	 * catch (Exception e) { logger.
	 * error("[ATSTaskAssignmentDao][updateStatusProcEvntTable] Exception " +
	 * e.getMessage()); } }
	 */

	public String updateTaskOwnerTable(String taskID, String taskOwnerEmail, Date startTime, Date endTime,
			String tier, BigDecimal driveTime, int taskStopTime) {
		String response = MurphyConstant.FAILURE;
		try {
			List<String> user = getUserDetailsByEmail(taskOwnerEmail);
			if (!ServicesUtil.isEmpty(user)) {
				String updateQuery = "UPDATE TM_TASK_OWNER tw SET tw.TASK_OWNER ='" + taskOwnerEmail
						+ "', tw.TASK_OWNER_EMAIL = '" + taskOwnerEmail + "', tw.TASK_OWNER_DISP = '" + user.get(0)
						+ "', tw.P_ID ='" + user.get(1) + "',tw.START_TIME = '" + startTime + "', tw.END_TIME='"
						+ endTime + "',EST_DRIVE_TIME = '"+((BigDecimal) driveTime).doubleValue()+"',EST_RESOLVE_TIME='"+Double.valueOf(taskStopTime)+
						"' WHERE TASK_ID IN ('" + taskID + "')";

				logger.error("updateTaskOwnerTable Query: " + updateQuery);
				int result = this.getSession().createSQLQuery(updateQuery).executeUpdate();
				logger.error("[updateTaskOwnerTable][UpdateCount] " + result);
				if (result > 0) {
					response = MurphyConstant.SUCCESS;
				}
			}
		} catch (Exception e) {
			logger.error("[ATSTaskAssignmentDao][updateTaskOwnerTable] Exception " + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public List<String> getUserDetailsByEmail(String taskOwnerEmail) {
		List<String> li = null;
		try {
			String query = "select user_first_name ,user_last_name,p_id from TM_USER_IDP_MAPPING where user_email = '"
					+ taskOwnerEmail + "'";
			logger.error("getUserDetailsByEmail query: " + query);
			Query q = this.getSession().createSQLQuery(query);
			List<Object[]> responseList = q.list();
			if (!ServicesUtil.isEmpty(responseList)) {
				li = new ArrayList<String>();
				for (Object[] obj : responseList) {
					if (!ServicesUtil.isEmpty(obj[0]) && !ServicesUtil.isEmpty(obj[1])) {
						li.add(((String) obj[0]) + " " + ((String) obj[1]));
						li.add(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
						break;
					}
					logger.error("No User Details Present for the emailID : " + taskOwnerEmail);
				}
			} else
				logger.error("[getUserDetailsByEmail][No data for passed EmailID] ");
		} catch (Exception e) {
			logger.error("[ATSTaskAssignmentDao][getUserDetailsByEmail] Exception " + e.getMessage());
		}
		return li;
	}
	
	public String updateATSTaskAssignmentTableStatus() {
		String response = MurphyConstant.FAILURE;
		try {
			String queryUpdate = "UPDATE ATS_TASK_ASSIGNMENT SET HAS_TASK_ASSIGNED ='"+MurphyConstant.TRUE.toUpperCase()+"' where HAS_TASK_ASSIGNED = 'FALSE'";
			int result = this.getSession().createSQLQuery(queryUpdate).executeUpdate();
			logger.error("[updateATSTaskAssignmentTableStatus][UpdateCount] " + result);
			if (result > 0) {
				response = MurphyConstant.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[ATSTaskAssignmentDao][updateATSTaskAssignmentTableStatus][Exception] " + e.getMessage());
		}
		return response;
	}


}
