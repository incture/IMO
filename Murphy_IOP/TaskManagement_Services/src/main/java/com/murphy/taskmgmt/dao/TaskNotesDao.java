package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.TaskNoteDto;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("taskNotesDao")
@Transactional
public class TaskNotesDao {
	private static final Logger logger = LoggerFactory.getLogger(TaskNotesDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("TaskNotesDao.getSession()" + e.getMessage());
			return sessionFactory.openSession();
		}
	}

	public List<TaskNoteDto> getAllTasksDetails(String startedBy, String field, String fromDate, String toDate,
			List<String> origin) {

		// STEP 1 : Get All TasksId & Description
		Map<String, TaskNoteDto> taskIdtaskNoteMap = null;
		Map<String, String> processIdtoTaskIdMap = null;
		List<TaskNoteDto> taskNoteDtos = null;
		try {
			taskIdtaskNoteMap = getAllTaskIdAndDescription(startedBy, field, fromDate, toDate, origin);
			processIdtoTaskIdMap = new HashMap<>(taskIdtaskNoteMap.size());

			for (TaskNoteDto taskNoteDto : taskIdtaskNoteMap.values()) {
				processIdtoTaskIdMap.put(taskNoteDto.getProcessId(), taskNoteDto.getTaksId());
			}

		} catch (NoResultFault e) {
			logger.debug("TaskNotesDao.getAllTasksDetails() NoResultFault" + e.getMessage());
		}

		// STPE 2 :GET All tasks attribute Details by task ID
		if (!ServicesUtil.isEmpty(taskIdtaskNoteMap)) {
			taskNoteDtos = getAllTaskAttributes(taskIdtaskNoteMap, processIdtoTaskIdMap);
		}

		// STPE 3 :GET Latest Task Status by Task Id
		if (!ServicesUtil.isEmpty(taskIdtaskNoteMap)) {
			taskNoteDtos = getTaskStatusDetails(taskIdtaskNoteMap, processIdtoTaskIdMap, fromDate, toDate);
		}
		
		//STEP 4 :GET ROOT_CAUSE,ROOT_CAUSE DESCRIPTIOn
		if (!ServicesUtil.isEmpty(taskIdtaskNoteMap)) {
			taskNoteDtos = getRootCauseDetail(taskIdtaskNoteMap, processIdtoTaskIdMap, fromDate, toDate);
		}

		return taskNoteDtos;

	}
	
	private List<TaskNoteDto> getRootCauseDetail(Map<String, TaskNoteDto> taskIdtaskNoteMap,
			Map<String, String> processIdtoTaskIdMap, String fromDate, String toDate) {
        ArrayList<String> taskList = new ArrayList<>(taskIdtaskNoteMap.keySet());
		List<String> processIdList=new ArrayList<>(processIdtoTaskIdMap.keySet());
		
		//Iterate Each taskId to fetch Root Cause Details
		for(String taskUniqueId:taskList){
			
			TaskNoteDto taskdto=null;
			if (processIdList.contains(taskUniqueId)) {
				 taskdto = taskIdtaskNoteMap.get(processIdtoTaskIdMap.get(taskUniqueId));
			} else {
				 taskdto = taskIdtaskNoteMap.get(taskUniqueId);
			}	
			
		StringBuilder sql = new StringBuilder("SELECT TOP 1 ROOT_CAUSE,DESCRIPTION from TM_ROOTCAUSE_INSTS te").append(" ");
		sql.append("where te.TASK_ID = '"+taskUniqueId+"' ");
		sql.append("AND te.ACTION = '"+taskdto.getTaskStatus()+"' ");
		sql.append("AND te.CREATED_AT BETWEEN ('"+fromDate+"') and ('"+toDate+"') ORDER BY CREATED_AT DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		List<Object[]> resultList = query.list();
		if (!ServicesUtil.isEmpty(resultList)) {

			for (Object[] obj : resultList) {
                String rootCause=ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
				String rootCauseDescription = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];

				
				if (!ServicesUtil.isEmpty(taskdto)) {
					taskdto.setRootCause(rootCause);
					taskdto.setRootCauseDesc(rootCauseDescription);
				}

			}
			
		}
		}
		
		List<TaskNoteDto> taskNoteDtos = new ArrayList<>(taskIdtaskNoteMap.values());

		return taskNoteDtos;
	}

	@SuppressWarnings("unchecked")
	private List<TaskNoteDto> getTaskStatusDetails(Map<String, TaskNoteDto> taskIdtaskNoteMap,
			Map<String, String> processIdtoTaskIdMap,String fromDate,String toDate) {
		ArrayList<String> taskList = new ArrayList<>(taskIdtaskNoteMap.keySet());
		
		for(String taskUniqueId:taskList){
		StringBuilder sql = new StringBuilder("SELECT TOP 1 ACTION,TASK_ID FROM TM_AUDIT_TRAIL").append(" ");
		sql.append("WHERE TASK_ID ='"+taskUniqueId+"'");
		sql.append("AND CREATED_AT BETWEEN ('"+fromDate+"') and ('"+toDate+"') ORDER BY CREATED_AT DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		List<Object[]> resultList = query.list();
		List<String> processIdList=new ArrayList<>(processIdtoTaskIdMap.keySet());
		if (!ServicesUtil.isEmpty(resultList)) {

			for (Object[] obj : resultList) {
                String taskStatus=ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
				String taskId = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
				
                TaskNoteDto dto=null;
				
				if (processIdList.contains(taskId)) {
					dto = taskIdtaskNoteMap.get(processIdtoTaskIdMap.get(taskId));
				} else {
					dto = taskIdtaskNoteMap.get(taskId);
				}
				
				if (!ServicesUtil.isEmpty(dto)) {
					dto.setTaskStatus(taskStatus);
				}
			}
		}
		}
		
		List<TaskNoteDto> taskNoteDtos = new ArrayList<>(taskIdtaskNoteMap.values());

		return taskNoteDtos;
	}

	@SuppressWarnings("unchecked")
	private List<TaskNoteDto> getAllTaskAttributes(Map<String, TaskNoteDto> taskIdtaskNoteMap,
			Map<String, String> processIdtoTaskIdMap) {
		ArrayList<String> taskList = new ArrayList<>(taskIdtaskNoteMap.keySet());
		taskList.addAll(processIdtoTaskIdMap.keySet());
		StringBuilder sql = new StringBuilder("select task_id,attr_temp_id,ins_value from  tm_attr_insts").append(" ");
		sql.append("where task_id IN(" + ServicesUtil.getStringFromList(taskList) + ")");
		logger.debug("TaskNotesDao.getAllTaskAttributes() query " + sql.toString());
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		List<Object[]> resultList = query.list();
		List<String> processIdList = new ArrayList<>(processIdtoTaskIdMap.keySet());

		if (!ServicesUtil.isEmpty(resultList)) {

			for (Object[] obj : resultList) {

				String taskId = ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
				String attributeTmpId = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
				String instanceValue = ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2];

				TaskNoteDto dto = null;

				if (processIdList.contains(taskId)) {
					dto = taskIdtaskNoteMap.get(processIdtoTaskIdMap.get(taskId));
				} else {
					dto = taskIdtaskNoteMap.get(taskId);
				}

				if (!ServicesUtil.isEmpty(dto)) {
					switch (attributeTmpId) {
					case "123":
						dto.setWellName(instanceValue);
						break;
					case "12345":
						dto.setClassification(instanceValue);
						break;
					case "123456":
						dto.setSubClassification(instanceValue);
						break;

					case "INQ01":
						dto.setWellName(instanceValue);
						break;

					case "INQ05":
						dto.setClassification(instanceValue);
						break;
					case "NDV1":
						dto.setWellName(instanceValue);
						break;
					case "NDO5":
						dto.setClassification(instanceValue);
						break;
					case "NDO6":
						dto.setSubClassification(instanceValue);
						break;
					default:
						break;
					}
				}

			}
		}

		List<TaskNoteDto> taskNoteDtos = new ArrayList<>(taskIdtaskNoteMap.values());
		return taskNoteDtos;
	}

	@SuppressWarnings("unchecked")
	private Map<String, TaskNoteDto> getAllTaskIdAndDescription(String startedBy, String field, String fromDate,
			String toDate, List<String> origin) throws NoResultFault {

		StringBuilder sql = new StringBuilder("SELECT t.task_id,t.description,t.process_id").append(" ");
		sql.append("from tm_proc_evnts p , tm_task_evnts t").append(" ");
		sql.append("WHERE p.process_id=t.process_id").append(" ");
		sql.append("AND  STARTED_BY ='" + startedBy + "'").append(" ");
		sql.append("AND p.LOC_CODE LIKE '%" + field + "%'").append(" ");
		sql.append("AND p.STARTED_AT BETWEEN  '" + fromDate + "'  AND '" + toDate + "'").append(" ");
		sql.append("AND t.ORIGIN IN(" + ServicesUtil.getStringFromList(origin) + ")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		logger.debug("TaskNotesDao.getAllTaskIdAndDescription() quey " + sql.toString());

		List<Object[]> resultList = query.list();

		if (!ServicesUtil.isEmpty(resultList)) {

			Map<String, TaskNoteDto> taskIdTaskNoteMap = new HashMap<String, TaskNoteDto>();
			for (Object[] obj : resultList) {
				TaskNoteDto taskNoteDto = new TaskNoteDto();
				String taskId = ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
				String description = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
				String processId = ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2];
				taskNoteDto.setTaksId(taskId);
				taskNoteDto.setProcessId(processId);
				taskNoteDto.setDescription(description);
				taskIdTaskNoteMap.put(taskId, taskNoteDto);
			}

			return taskIdTaskNoteMap;

		} else {

			throw new NoResultFault("TaskNotesDao.getAllTaskIdAndDescription() No records found for the query");
		}

	}

}
