package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.ObxTaskDto;
import com.murphy.taskmgmt.dto.ObxTaskDtoDummy;
import com.murphy.taskmgmt.entity.ObxTaskDo;
import com.murphy.taskmgmt.entity.ObxTaskDoDummy;
import com.murphy.taskmgmt.entity.ObxTaskDoPKDummy;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ObxTaskDaoDummy")
public class ObxTaskDaoDummy extends BaseDao<ObxTaskDoDummy, ObxTaskDtoDummy>{

	private static final Logger logger = LoggerFactory.getLogger(ObxTaskDaoDummy.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected ObxTaskDoDummy importDto(ObxTaskDtoDummy fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		ObxTaskDoDummy entity=new ObxTaskDoDummy();
		entity.setField(fromDto.getField());
		entity.setLatitude(new BigDecimal(fromDto.getLatitude(),MathContext.DECIMAL64));
		entity.setLongitude(new BigDecimal(fromDto.getLongitude(),MathContext.DECIMAL64));
		entity.setObxTaskDoPK(new ObxTaskDoPKDummy());
		entity.getObxTaskDoPK().setDay(fromDto.getDay());
		entity.getObxTaskDoPK().setLocationCode(fromDto.getLocationCode());
		entity.setLocationText(fromDto.getLocationText());
		entity.setOwnerEmail(fromDto.getOwnerEmail());
		entity.setTier(fromDto.getTier());
		entity.setSequnceNumber(fromDto.getSequenceNumber());
		entity.setDriveTime(new BigDecimal(fromDto.getDriveTime(),MathContext.DECIMAL64));
		entity.setEstimatedTaskTime(new BigDecimal(20,MathContext.DECIMAL64));
		entity.setUpdatedBy(fromDto.getUpdatedBy());
		return entity;
	}

	@Override
	protected ObxTaskDtoDummy exportDto(ObxTaskDoDummy entity){
		ObxTaskDtoDummy dto=new ObxTaskDtoDummy();
		try{
		dto.setField(entity.getField());
		dto.setLatitude(entity.getLatitude().doubleValue());
		dto.setLongitude(entity.getLongitude().doubleValue());
		dto.setLocationText(entity.getLocationText());
		dto.setDay(entity.getObxTaskDoPK().getDay());
		dto.setLocationCode(entity.getObxTaskDoPK().getLocationCode());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setOwnerEmail(entity.getOwnerEmail());
		dto.setTier(entity.getTier());
		dto.setSequenceNumber(entity.getSequnceNumber());
		dto.setDriveTime(ServicesUtil.isEmpty(entity.getDriveTime())?0:entity.getDriveTime().doubleValue());
		}
		catch(Exception e){
			logger.error("[ObxTaskDao][exportDto] Exception "+e.getMessage());
		}
		return dto;
	}

	public void insertTasks(List<ObxTaskDto> taskList){
			for(ObxTaskDto task:taskList){
			try{
				logger.error("inside insert");
				ObxTaskDtoDummy taskDummy=convertFromActual(task);
				create(taskDummy);
				logger.error("after insert");
			}
			catch (Exception e) {
					logger.error("[ObxTaskDao][insertTasks] Exception "+ e.getMessage());
			}
		}
	}
	
	private ObxTaskDtoDummy convertFromActual(ObxTaskDto entity) {
		ObxTaskDtoDummy dummyDto=new ObxTaskDtoDummy();
		try{
			dummyDto.setField(entity.getField());
			dummyDto.setLatitude(entity.getLatitude().doubleValue());
			dummyDto.setLongitude(entity.getLongitude().doubleValue());
			dummyDto.setLocationCode(entity.getLocationCode());
			dummyDto.setLocationText(entity.getLocationText());
			dummyDto.setOwnerEmail(entity.getOwnerEmail());
			dummyDto.setTier(entity.getTier());
			dummyDto.setSequenceNumber(entity.getSequenceNumber());
			dummyDto.setDriveTime(ServicesUtil.isEmpty(entity.getDriveTime())?0:entity.getDriveTime().doubleValue());
			}
			catch(Exception e){
				logger.error("[ObxTaskDao][exportDto] Exception "+e.getMessage());
			}
		return dummyDto;
	}

	@SuppressWarnings("unchecked")
	public List<ObxTaskDtoDummy> getTasksOfUser(String userId) {
		List<ObxTaskDtoDummy> taskList=new ArrayList<ObxTaskDtoDummy>();
//		String query = "SELECT LOCATION_CODE,TASK_OWNER_EMAIL,FIELD,LOCATION_TEXT,TIER,LATITUDE,LONGITUDE,ALGORITHM FROM "
//				+ "OBX_TASK_ALLOCATION WHERE TASK_OWNER_EMAIL='"+userId+"'";
		try{
			Criteria criteria=this.getSession().createCriteria(ObxTaskDoDummy.class);
			criteria.add(Restrictions.eq("TASK_OWNER_EMAIL", userId));
			List<ObxTaskDoDummy> result=criteria.list();
			if(!ServicesUtil.isEmpty(result)){
				for(ObxTaskDoDummy obj:result){
					ObxTaskDtoDummy dto=exportDto(obj);
					taskList.add(dto);
				}
			}
		}
		catch (Exception e) {
			logger.error("[ObxTaskDao][getTasksOfUser] Exception "+ e.getMessage());
		}
		return taskList;
	}

	public void updateTasks(List<ObxTaskDtoDummy> taskList){
		Transaction tx = null;
		Session session = null;
		for(ObxTaskDtoDummy task:taskList){
		try{
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(this.importDto(task));
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[ObxTaskDao][updateTasks] Exception "+e.getMessage());
		}
		finally {
			try {
				if(!ServicesUtil.isEmpty(session))
					{
					session.close();
					}
			} catch (Exception e) {
				logger.error("[LocationDistancesDao][updateTasks][Exception] Exception While Closing Session " + e.getMessage());
			}
		}
	}
}
}
