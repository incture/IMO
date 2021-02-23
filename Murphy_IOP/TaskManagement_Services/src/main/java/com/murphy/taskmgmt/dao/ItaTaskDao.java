package com.murphy.taskmgmt.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.math.BigInteger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ItaTaskDao")
@Transactional
public class ItaTaskDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	ItaMappingDao itaMappingDao;

	@Autowired
	HierarchyDao locDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][BaseDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}

	ArrayList<String> taskList = null;
	private static final Logger logger = LoggerFactory.getLogger(ItaTaskDao.class);

	@SuppressWarnings("unchecked")
	public ResponseMessage checkTaskCount(int durationInDays, int numberOfTasksCreated, String Classification,
			String subClassification, String rootCause,String typeOfTaskToBeCreated, String taskClassificationITA,
			String taskSubClassificationITA) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -durationInDays);

		ResponseMessage responseMessage = new ResponseMessage();
		Query query = null;
		try {
			// Preparing query for rootCause in case rootCause is present
			String rootCauseQuery = null;
			if(!ServicesUtil.isEmpty(rootCause)){
				String rootCauseString = rootCause.replace(",", "','");
				rootCauseQuery = "INNER JOIN TM_ROOTCAUSE_INSTS rt ON rt.task_Id = te.task_id "
						+"WHERE rt.created_At = (SELECT max(rtx.created_At)  FROM TM_ROOTCAUSE_INSTS rtx WHERE rtx.task_id = te.task_id) AND"
						+"(rt.root_cause in('"+ rootCauseString+ "')) AND";			
			}
			else
				rootCauseQuery = "WHERE";
			
			// Query to fetch count and location of tasks for the given
			// classification and subClassification
			String queryString = "SELECT pe.loc_code,count(1) FROM TM_TASK_EVNTS te INNER JOIN TM_PROC_EVNTS pe"
					+ " ON pe.PROCESS_ID = te.PROCESS_ID "+ rootCauseQuery +" te.parent_origin NOT IN ('" + MurphyConstant.P_ITA
					+ "') AND te.TASK_ID IN ( SELECT TASK_ID FROM TM_ATTR_INSTS where INS_VALUE = '"
					+ subClassification + "' AND ATTR_TEMP_ID = '123456'" + " AND TASK_ID IN ("
					+ " SELECT i.task_id FROM TM_ATTR_INSTS i INNER JOIN TM_TASK_EVNTS t ON i.task_id = t.task_id"
					+ " WHERE INS_VALUE = '" + Classification + "' AND i.ATTR_TEMP_ID = '12345')) AND"
					+ " to_date(te.CREATED_AT) >= '" + sdf.format(cal.getTime())
					+ "' group by pe.LOC_CODE having count(1) >= " + numberOfTasksCreated;

			logger.error("[Murphy][ItaTaskDao][checkTaskCount][Query]" + queryString);
			query = this.getSession().createSQLQuery(queryString);

			List<Object[]> response = (List<Object[]>) query.list();
			if (!ServicesUtil.isEmpty(response)) {
				boolean countSatisfied = false;
				for (Object[] obj : response) {
					// if count is greater than or equal to 2 then create new
					// task
					if (((BigInteger) obj[1]).intValue() >= numberOfTasksCreated) {
						String loc_type = locDao.getLocationtypeByLocCode((String) obj[0]);
						logger.error("loc_type" + loc_type);
						if (!loc_type.trim().equalsIgnoreCase(MurphyConstant.FIELD)
								&& !loc_type.trim().equalsIgnoreCase(MurphyConstant.WELLPAD)) {
							countSatisfied = true;
							logger.error("createTask called for location " + (String) obj[0] +" and loc_type" + loc_type);
							responseMessage = itaMappingDao.createTask((String) obj[0], Classification,
									subClassification,rootCause,taskClassificationITA, taskSubClassificationITA,
									typeOfTaskToBeCreated, durationInDays);
							if (responseMessage.getStatus().equalsIgnoreCase(MurphyConstant.SUCCESS)) {
								taskList = itaMappingDao.getTaskList();
								createItaMapping(taskList, taskClassificationITA, taskSubClassificationITA,
										(String) obj[0],typeOfTaskToBeCreated);
							}
						}
					}
				}
				if (!countSatisfied) {
					logger.error("No record eligible for task creation");
					responseMessage.setMessage("No record eligible for task creation");
					responseMessage.setStatus(MurphyConstant.SUCCESS);
					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				}
			} else {
				logger.error("No record found for parameters passed");
				responseMessage.setMessage("No record found for parameters passed");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("[Murphy][ItaTaskDao][checkTaskCount]" + e.getMessage());
			responseMessage.setMessage("Error in Ita task count");
			responseMessage.setStatus(MurphyConstant.FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return responseMessage;
	}

	public void createItaMapping(ArrayList<String> taskList, String newClassification, String newSubClassification,
			String loc_code, String typeOfTaskToBeCreated) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			Query query = null;
			String ita_task_id = null;
			String classificationQuery = null;
			// If an inquiry is created 
			if(typeOfTaskToBeCreated.equalsIgnoreCase("Inquiry")){
				classificationQuery = "SELECT task_id FROM TM_ATTR_INSTS where INS_VALUE = '"+ newSubClassification+ "' and ATTR_TEMP_ID = 'INQ05'";
			}
			// If dispatch task is created
			if(typeOfTaskToBeCreated.equalsIgnoreCase("Dispatch")){
			classificationQuery = "select TASK_ID from TM_ATTR_INSTS where INS_VALUE = '" + newSubClassification
					+ "' and ATTR_TEMP_ID = '123456'" + " and TASK_ID in ("
					+ " select i.task_id from TM_ATTR_INSTS i inner join tm_task_evnts t on i.task_id = t.task_id"
					+ " where INS_VALUE = '" + newClassification + "' and i.ATTR_TEMP_ID = '12345')";
			}
			// Query to fetch new ITA created(task_id)
			String queryString = "select te.task_id from TM_TASK_EVNTS te inner join TM_PROC_EVNTS pe"
					+ " on pe.PROCESS_ID = te.PROCESS_ID where pe.loc_code = '" + loc_code + "' and te.parent_origin ='"
					+ MurphyConstant.P_ITA + "'" + " and te.TASK_ID in (" + classificationQuery + ")"
					+ " and to_date(te.CREATED_AT) = '" + sdf.format(cal.getTime()) + "'";

			logger.error("[Murphy][ItaTaskDao][createItaMapping][queryString]" + queryString);
			Object obj = this.getSession().createSQLQuery(queryString).uniqueResult();
			if (!ServicesUtil.isEmpty(obj))
				ita_task_id = obj.toString();
			logger.error("ita_task_id" + ita_task_id);
			// Insert data into ITA_MAPPING table
			try {
				for (String task_id : taskList) {
					String queryInsert = "insert into ITA_MAPPING values ('" + task_id + "' , '" + ita_task_id + "' , '"
							+ sdf.format(cal.getTime()) + "' )";

					logger.error("[Murphy][ItaTaskDao][createItaMapping][queryInsert]" + queryInsert);
					query = this.getSession().createSQLQuery(queryInsert);
					query.executeUpdate();
				}
			} catch (Exception e) {
				logger.error("[Murphy][ItaTaskDao][createItaMapping][Exception]" + e.getMessage());
			}
		} catch (Exception e) {
			logger.error("[Murphy][ItaTaskDao][createItaMapping][Exception]" + e.getMessage());
		}
	}
}
