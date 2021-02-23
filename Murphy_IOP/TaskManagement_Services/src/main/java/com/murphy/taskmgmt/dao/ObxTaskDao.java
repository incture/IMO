package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.ObxOperatorWorkloadDetailsDto;
import com.murphy.taskmgmt.dto.ObxTaskDto;
import com.murphy.taskmgmt.entity.ObxTaskDo;
import com.murphy.taskmgmt.entity.ObxTaskDoPK;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ObxTaskDao")
public class ObxTaskDao extends BaseDao<ObxTaskDo, ObxTaskDto> {

	private static final Logger logger = LoggerFactory.getLogger(ObxTaskDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected ObxTaskDo importDto(ObxTaskDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		ObxTaskDo entity = new ObxTaskDo();
		entity.setObxTaskDoPK(new ObxTaskDoPK());
		entity.setField(fromDto.getField());
		entity.setLatitude(new BigDecimal(String.valueOf(fromDto.getLatitude())));
		entity.setLongitude(new BigDecimal(String.valueOf(fromDto.getLongitude())));
		entity.getObxTaskDoPK().setLocationCode(fromDto.getLocationCode());
		entity.setLocationText(fromDto.getLocationText());
		entity.getObxTaskDoPK().setDay(fromDto.getDay());
		entity.setOwnerEmail(fromDto.getOwnerEmail());
		entity.setTier(fromDto.getTier());
		entity.setSequenceNumber(fromDto.getSequenceNumber());
		entity.setDriveTime(new BigDecimal(fromDto.getDriveTime(), MathContext.DECIMAL64));
		entity.setEstimatedTaskTime(new BigDecimal(fromDto.getEstimatedTaskTime(),MathContext.DECIMAL64));
		entity.setRole(fromDto.getRole());
		entity.setClusterNumber(fromDto.getClusterNumber());
		entity.setUpdatedBy(fromDto.getUpdatedBy());
		entity.setIsObxUser(fromDto.getIsObxOperator());
		return entity;
	}

	@Override
	protected ObxTaskDto exportDto(ObxTaskDo entity) {
		ObxTaskDto dto = new ObxTaskDto();
		dto.setField(entity.getField());
		dto.setLatitude(entity.getLatitude().doubleValue());
		dto.setLongitude(entity.getLongitude().doubleValue());
		dto.setLocationCode(entity.getObxTaskDoPK().getLocationCode());
		dto.setLocationText(entity.getLocationText());
		dto.setDay(entity.getObxTaskDoPK().getDay());
		dto.setOwnerEmail(entity.getOwnerEmail());
		dto.setTier(entity.getTier());
		dto.setSequenceNumber(entity.getSequenceNumber());
		dto.setDriveTime(entity.getDriveTime().doubleValue());
		dto.setEstimatedTaskTime(entity.getEstimatedTaskTime().doubleValue());
		dto.setRole(entity.getRole());
		dto.setClusterNumber(entity.getClusterNumber());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setIsObxOperator(entity.getIsObxUser());
		return dto;
	}

	public void insertTasks(List<ObxTaskDto> taskList) {
		for (ObxTaskDto task : taskList) {
			try {
				create(task);
			} catch (Exception e) {
				logger.error("[ObxTaskDao][insertTasks] Exception " + e.getMessage());
			}
		}
	}

	public void updateTasks(List<ObxTaskDto> taskList) {
		Transaction tx = null;
		Session session = null;
		for (ObxTaskDto task : taskList) {
			try {
				session = sessionFactory.openSession();
				tx = session.beginTransaction();
				session.saveOrUpdate(this.importDto(task));
				session.flush();
				session.clear();
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				logger.error("[ObxTaskDao][updateTasks] Exception " + e.getMessage());
			} finally {
				try {
					if (!ServicesUtil.isEmpty(session)) {
						session.close();
					}
				} catch (Exception e) {
					logger.error("[LocationDistancesDao][updateTasks][Exception] Exception While Closing Session "
							+ e.getMessage());
				}
			}
		}
	}

	public void deleteJobs() {
		String query = "DELETE FROM OBX_TASK_ALLO";
		try {
			Query q = this.getSession().createSQLQuery(query);
			int result = (int) q.executeUpdate();
			logger.error("Jobs deleted " + result);
		} catch (Exception e) {
			logger.error("[ObxTaskDao][deleteJobs] Exception " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ObxTaskDto> getObxTaskDtoFromLocationCode(HashMap<String, List<String>> jobList, String field) {
		ArrayList<ObxTaskDto> tasks = new ArrayList<ObxTaskDto>();
		for (Entry<String, List<String>> userJobs : jobList.entrySet()) {
			String commaSeparatedLocations = "";
			for (String locationCode : userJobs.getValue()) {
				commaSeparatedLocations += locationCode + "','";
			}
			String query = "SELECT PL.LOCATION_CODE,LC.LATITUDE,LC.LONGITUDE,WT.TIER,PL.LOCATION_TEXT FROM "
					+ "PRODUCTION_LOCATION PL " + "JOIN LOCATION_COORDINATE LC ON LC.LOCATION_CODE=WV.LOCATION_CODE "
					+ "JOIN WELL_TIER WT ON WT.LOCATION_CODE=WV.LOCATION_CODE " + "WHERE PL.LOCATION_CODE in ('"
					+ commaSeparatedLocations + "')";
			try {
				Query q = this.getSession().createSQLQuery(query);
				List<Object[]> result = q.list();
				if (!ServicesUtil.isEmpty(result)) {
					for (Object[] obj : result) {
						ObxTaskDto dto = new ObxTaskDto();
						dto.setLocationCode((String) obj[0]);
						dto.setField(field);
						dto.setLatitude(((BigDecimal) obj[1]).doubleValue());
						dto.setLongitude(((BigDecimal) obj[2]).doubleValue());
						dto.setTier((String) obj[3]);
						dto.setLocationText((String) obj[4]);
						dto.setOwnerEmail(userJobs.getKey());
						dto.setSequenceNumber(userJobs.getValue().indexOf(dto.getLocationCode()));
						dto.setDriveTime(0.0);
//						dto.setIsObxOperator("false");
						tasks.add(dto);
					}
				}
			} catch (Exception e) {
				logger.error("[ObxTaskDao][deleteJobs] Exception " + e.getMessage());
			}
		}
		return tasks;
	}

	@SuppressWarnings("unchecked")
	public List<ObxTaskDto> getTasksOfCluster(int clusterNumber,int day) {
		List<ObxTaskDto> taskList = new ArrayList<ObxTaskDto>();
		// String query = "SELECT
		// LOCATION_CODE,TASK_OWNER_EMAIL,FIELD,LOCATION_TEXT,TIER,LATITUDE,LONGITUDE,ALGORITHM
		// FROM "
		// + "OBX_TASK_ALLO WHERE TASK_OWNER_EMAIL='"+userId+"'";
		try {
			Criteria criteria = this.getSession().createCriteria(ObxTaskDo.class);
			criteria.add(Restrictions.eq("clusterNumber", clusterNumber));
			criteria.add(Restrictions.eq("obxTaskDoPK.day", day));
			List<ObxTaskDo> result = criteria.list();
			if (!ServicesUtil.isEmpty(result)) {
				for (ObxTaskDo obj : result) {
					ObxTaskDto dto = exportDto(obj);
					taskList.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[ObxTaskDao][getTasksOfUser] Exception " + e.getMessage());
		}
		return taskList;
	}

	@SuppressWarnings("unchecked")
	public HashMap<Integer, List<ObxTaskDto>> getTasksOfRoleAndDay(String role, int day) {
		HashMap<Integer, List<ObxTaskDto>> response = new HashMap<>();
		String query = "SELECT LOCATION_CODE,TASK_OWNER_EMAIL,FIELD,LOCATION_TEXT,TIER,LATITUDE,LONGITUDE,SEQUENCE_NUMBER,DRIVE_TIME,ESTIMATED_TASK_TIME,CLUSTER_NUMBER,UPDATED_BY,DAY,ROLE,IS_OBX_USER FROM "
				+ "OBX_TASK_ALLO WHERE ROLE='" + role + "' AND DAY=" + day
				+ " ORDER BY CLUSTER_NUMBER,SEQUENCE_NUMBER ";
		try {
			List<Object[]> result = this.getSession().createSQLQuery(query).list();
			if (!ServicesUtil.isEmpty(result)) {
				int key = 0;
				for (Object[] obj : result) {
					if (obj[10].getClass().getName().equals("java.math.BigInteger")) {
						key = ((BigInteger) obj[10]).intValue();
					}
					else if(obj[10].getClass().getName().equals("java.lang.Integer"))
						key = ((Integer) obj[10]);
					ObxTaskDto dto = new ObxTaskDto();
					dto.setLocationCode((String) obj[0]);
					// dto.setOwnerEmail((String)obj[1]);
					dto.setField((String) obj[2]);
					dto.setLocationText((String) obj[3]);
					dto.setTier((String) obj[4]);
					dto.setLatitude(((BigDecimal) obj[5]).doubleValue());
					dto.setLongitude(((BigDecimal) obj[6]).doubleValue());
					if (obj[7].getClass().getName().equals("java.math.BigInteger")) {
						dto.setSequenceNumber(((BigInteger) obj[7]).intValue());
					}
					else if(obj[7].getClass().getName().equals("java.lang.Integer"))
						dto.setSequenceNumber((Integer) obj[7]);
					dto.setSequenceNumber(((Integer) obj[7]));
					dto.setDriveTime(((BigDecimal) obj[8]).doubleValue());
					dto.setClusterNumber(key);
					dto.setUpdatedBy((String) obj[11]);
					dto.setRole((String) obj[13]);
					dto.setDay(day);
					dto.setIsObxOperator((String)obj[14]);
					if (response.containsKey(key)) {
						response.get(key).add(dto);
					} else {
						response.put(key, new ArrayList<>());
						response.get(key).add(dto);
					}
				}
			}
		} catch (Exception e) {
			logger.error("[ObxTaskDao][getTasksOfRoleAndDay] Exception " + e.getMessage());
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, List<ObxTaskDto>> getTasksOfDayForUSers(int day) {
		HashMap<String, List<ObxTaskDto>> response = new HashMap<>();
		String query = "SELECT LOCATION_CODE,TASK_OWNER_EMAIL,SEQUENCE_NUMBER,DRIVE_TIME FROM "
				+ "OBX_TASK_ALLO WHERE DAY=" + day
				+ " AND LENGTH(TRIM('' from TASK_OWNER_EMAIL))<>0 ORDER BY TASK_OWNER_EMAIL,SEQUENCE_NUMBER ";
		try {
			List<Object[]> result = this.getSession().createSQLQuery(query).list();
			if (!ServicesUtil.isEmpty(result)) {
				for (Object[] obj : result) {
					if(!ServicesUtil.isEmpty(obj[1])){
					String key = (String)obj[1];
					ObxTaskDto dto = new ObxTaskDto();
					dto.setLocationCode((String) obj[0]);
					if (obj[2].getClass().getName().equals("java.math.BigInteger")) {
						dto.setSequenceNumber(((BigInteger) obj[2]).intValue());
					}
					else if(obj[2].getClass().getName().equals("java.lang.Integer"))
						dto.setSequenceNumber((Integer) obj[2]);
					
					if (obj[3].getClass().getName().equals("java.math.BigDecimal")) {
						dto.setDriveTime(((BigDecimal) obj[3]).doubleValue());
					}
					else if(obj[3].getClass().getName().equals("java.lang.Double"))
						dto.setDriveTime((Double) obj[3]);
					
					if (response.containsKey(key)) {
						response.get(key).add(dto);
					} else {
						response.put(key,new ArrayList<>());
						response.get(key).add(dto);
					}
				}
					}
			}
		} catch (Exception e) {
			logger.error("[ObxTaskDao][getTasksOfDayForUSers] Exception " + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings("unused")
	public void updateOwner(int day,String locationCode,String taskOwner,boolean isObxUser){
		String query =" UPDATE OBX_TASK_ALLO SET TASK_OWNER_EMAIL='"+taskOwner+"' ,IS_OBX_USER='"+isObxUser+"' WHERE DAY="+day+" AND LOCATION_CODE='"+locationCode+"'";
		try{
			int response=this.getSession().createSQLQuery(query).executeUpdate();
		}
		catch (Exception e) {
			logger.error("[ObxTaskDao][updateOwner] Exception " + e.getMessage() + " for "+query);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getDays(){
		List<Integer> days=new ArrayList<>();
		String query ="SELECT DISTINCT DAY FROM TM_WELL_VISIT";
		try{
			List<Integer> response=this.getSession().createSQLQuery(query).list();
			if(!ServicesUtil.isEmpty(response))
				return response;
		}
		catch (Exception e) {
			logger.error("[ObxTaskDao][getDays] Exception " + e.getMessage() + " for "+query);
			throw e;
		}
		return days;
	}
	
	@SuppressWarnings("unchecked")
	public List<ObxTaskDto> getTasksOfUser(int day,String userEmail) {
		List<ObxTaskDto> taskList = new ArrayList<ObxTaskDto>();
		// String query = "SELECT
		// LOCATION_CODE,TASK_OWNER_EMAIL,FIELD,LOCATION_TEXT,TIER,LATITUDE,LONGITUDE,ALGORITHM
		// FROM "
		// + "OBX_TASK_ALLO WHERE TASK_OWNER_EMAIL='"+userId+"'";
		try {
			Criteria criteria = this.getSession().createCriteria(ObxTaskDo.class);
			criteria.add(Restrictions.eq("ownerEmail", userEmail));
			criteria.add(Restrictions.eq("obxTaskDoPK.day", day));
			List<ObxTaskDo> result = criteria.list();
			if (!ServicesUtil.isEmpty(result)) {
				for (ObxTaskDo obj : result) {
					ObxTaskDto dto = exportDto(obj);
					taskList.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[ObxTaskDao][getTasksOfUser] Exception " + e.getMessage());
		}
		return taskList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ObxTaskDto> getTasksOfCluster(int clusterNumber,int day,List<String> field) {
		List<ObxTaskDto> taskList = new ArrayList<ObxTaskDto>();
		// String query = "SELECT
		// LOCATION_CODE,TASK_OWNER_EMAIL,FIELD,LOCATION_TEXT,TIER,LATITUDE,LONGITUDE,ALGORITHM
		// FROM "
		// + "OBX_TASK_ALLO WHERE TASK_OWNER_EMAIL='"+userId+"'";
		try {
			Criteria criteria = this.getSession().createCriteria(ObxTaskDo.class);
			criteria.add(Restrictions.eq("clusterNumber", clusterNumber));
			criteria.add(Restrictions.eq("obxTaskDoPK.day", day));
			criteria.add(Restrictions.in("field", field.toArray(new Object[field.size()])));
//			criteria.add(Restrictions.like("field", "%"+field+"%"));
			List<ObxTaskDo> result = criteria.list();
			if (!ServicesUtil.isEmpty(result)) {
				for (ObxTaskDo obj : result) {
					ObxTaskDto dto = exportDto(obj);
					taskList.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[ObxTaskDao][getTasksOfCluster] Exception " + e.getMessage());
		}
		return taskList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ObxTaskDto> getUnassignedWells(int day,List<String> field) {
		List<ObxTaskDto> unAssignedWells=new ArrayList<>();
		try{
			Criteria criteria = this.getSession().createCriteria(ObxTaskDo.class);
			criteria.add(Restrictions.eq("obxTaskDoPK.day", day));
			criteria.add(Restrictions.eq("ownerEmail", ""));
			criteria.add(Restrictions.in("field", field.toArray(new Object[field.size()])));
//			criteria.setProjection(Projections.groupProperty("clusterNumber"));
			List<ObxTaskDo> result = criteria.list();
			if(!ServicesUtil.isEmpty(result)){
				for(ObxTaskDo entity:result){
					unAssignedWells.add(exportDto(entity));
				}
			}
		}catch (Exception e) {
			logger.error("[ObxSchedulerDao][getUnassignedWells][Exception] " + e.getMessage());
		}
		return unAssignedWells;
	}
	
	@SuppressWarnings("unchecked")
	public List<ObxOperatorWorkloadDetailsDto> getUsersWithLessWorkload(int day,List<String> field){
		List<ObxOperatorWorkloadDetailsDto> operatorWithWorkLoadDetails=new ArrayList<>();
		String fieldInString=com.murphy.integration.util.ServicesUtil.getStringFromList(field);
		try{
		String query="SELECT SUM(DRIVE_TIME+ESTIMATED_TASK_TIME)AS WORKLOAD,TASK_OWNER_EMAIL FROM OBX_TASK_ALLO WHERE DAY="+day+" AND FIELD in ("+fieldInString+") GROUP BY TASK_OWNER_EMAIL" 
					+" ORDER BY WORKLOAD ASC ";
		List<Object[]> resultList = this.getSession().createSQLQuery(query).list();
		if(!ServicesUtil.isEmpty(resultList)){
			for(Object[] result:resultList){
				ObxOperatorWorkloadDetailsDto userDto=new ObxOperatorWorkloadDetailsDto();
				userDto.setObxOperatorfullName(ServicesUtil.isEmpty(result[1])?"":(String)result[1]);
				userDto.setWorkLoad(((BigDecimal)result[0]).doubleValue());
				operatorWithWorkLoadDetails.add(userDto);
			}
		}
		}
		catch (Exception e) {
			logger.error("[ObxSchedulerDao][getUsersWithLessWorkload][Exception] " + e.getMessage());
		}
		return operatorWithWorkLoadDetails;
	}
	
	public void revokeObxTasks() {
		try {
			Date currentDate = new Date();
			Calendar c = Calendar.getInstance();
//			c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			c.setTime(currentDate);
			c.add(Calendar.DATE, -1);
			Date date = c.getTime();
			String dateInString = new SimpleDateFormat("yyyy-MM-dd").format(date);
			String queryTaskStatusUpdate = "UPDATE TM_TASK_EVNTS SET STATUS='" + MurphyConstant.REVOKED
					+ "' WHERE TASK_ID IN (SELECT TE.TASK_ID FROM TM_TASK_EVNTS TE JOIN "
					+ " TM_TASK_OWNER TO ON TE.TASK_ID=TO.TASK_ID"
					+ " JOIN TM_PROC_EVNTS PE ON PE.PROCESS_ID=TE.PROCESS_ID "
					+ " WHERE PARENT_ORIGIN='OBX' AND TASK_TYPE='SYSTEM' AND CREATED_AT>TO_DATE('" + dateInString
					+ "','yyyy-MM-dd')" + "AND "
					// + "TO.TIER='"+MurphyConstant.TIER_C+"' AND "
					+ "TE.STATUS<>'" + MurphyConstant.COMPLETE + "')";
			logger.error("[ObxTaskDao][revokeObxTasks][UpdateCount] " + queryTaskStatusUpdate);
			int result = this.getSession().createSQLQuery(queryTaskStatusUpdate).executeUpdate();
			logger.error("[ObxTaskDao][revokeObxTasks][UpdateCount] " + result);
			revokeObxProcess();
		} catch (Exception e) {
			logger.error("[ObxTaskDao][revokeObxTasks][Exception] " + e.getMessage());
		}
	}
	
	public void revokeObxProcess(){
		try{
			Date currentDate = new Date();
			Calendar c = Calendar.getInstance();
//			c.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			c.setTime(currentDate);
			c.add(Calendar.DATE, -1);
			Date date = c.getTime();
			String dateInString = new SimpleDateFormat("yyyy-MM-dd").format(date);
			String queryStatusUpdate = "UPDATE TM_PROC_EVNTS SET STATUS='" + MurphyConstant.REVOKED
					+ "' WHERE PROCESS_ID IN (SELECT TE.PROCESS_ID FROM TM_TASK_EVNTS TE "
					+ "JOIN TM_TASK_OWNER TO ON TE.TASK_ID=TO.TASK_ID"
					+ " JOIN TM_PROC_EVNTS PE ON PE.PROCESS_ID=TE.PROCESS_ID "
					+ " WHERE TE.PARENT_ORIGIN='OBX' AND TE.TASK_TYPE='SYSTEM' AND TE.CREATED_AT>TO_DATE('" + dateInString
					+ "','yyyy-MM-dd')" + "AND "
					// + "TO.TIER='"+MurphyConstant.TIER_C+"' AND "
					+ "PE.STATUS<>'" + MurphyConstant.COMPLETE + "' AND TE.STATUS='"+MurphyConstant.REVOKED+"')";
			logger.error("[ObxTaskDao][revokeObxProcess][query] " + queryStatusUpdate);
			int result = this.getSession().createSQLQuery(queryStatusUpdate).executeUpdate();
			logger.error("[ObxTaskDao][revokeObxProcess][UpdateCount] " + result);
		}
		catch (Exception e) {
			logger.error("[ObxTaskDao][revokeObxProcess][Exception] " + e.getMessage());
		}
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	public List<ObxTaskDto> getAllOBXAllo(){
		List<ObxTaskDto> listofObx = null;
		List<ObxTaskDo> listofObxDo = null;
		Session session = null;
		try{
			session = getSession();
			listofObx = new ArrayList<>();
			listofObxDo = new ArrayList<>();
			
				listofObxDo = session.createQuery("From ObxTaskDo order by role,day,clusterNumber,sequenceNumber").list();
	
			
			for(ObxTaskDo obxTaskDo : listofObxDo){
				listofObx.add(exportDto(obxTaskDo));
			}
			
		}catch(Exception e){
			logger.error("[ObxTaskDao][getAllOBXAloo][Exception]"+ e.getMessage());
		}
		return listofObx;
	}
	public void setDriveTime(List<ObxTaskDto> list){
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			for(ObxTaskDto dto: list){
				session.createQuery("Update ObxTaskDo set driveTime=:drivetime where day=:day and clusterNumber=:cluster and sequenceNumber=:seq")
				.setParameter("drivetime", new BigDecimal(dto.getDriveTime()))
				.setParameter("day", dto.getDay())
				.setParameter("cluster", dto.getClusterNumber())
				.setParameter("seq", dto.getSequenceNumber())
				.executeUpdate();
				
			}
			transaction.commit();
			session.close();
			
		}catch(Exception e){
			logger.error("[ObxTaskDao][setDriveTime]"+ e.getMessage());
		}
	}
}
