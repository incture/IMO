package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.PipelineMeasurementDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.entity.PipelineMeasurementDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

/**
 * @author Ritwik.Jain
 *
 */
@Repository("PipeLineMeasurementDao")
@Transactional(rollbackOn = Exception.class)
public class PipelineMeasurementDao extends BaseDao<PipelineMeasurementDo, PipelineMeasurementDto> {

	private static final Logger logger = LoggerFactory.getLogger(PipelineMeasurementDao.class);
	@Override
	protected PipelineMeasurementDo importDto(PipelineMeasurementDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		PipelineMeasurementDo resDo = new PipelineMeasurementDo();
		if (!ServicesUtil.isEmpty(fromDto.getEquipmentId()))
			resDo.setEquipmentId(fromDto.getEquipmentId());

		if (!ServicesUtil.isEmpty(fromDto.getLineName()))
			resDo.setLineName(fromDto.getLineName());

		if (!ServicesUtil.isEmpty(fromDto.getFlocId()))
			resDo.setFlocId(fromDto.getFlocId());

		if (!ServicesUtil.isEmpty(fromDto.getLength()))
			resDo.setLength(fromDto.getLength());

		if (!ServicesUtil.isEmpty(fromDto.getDiameter()))
			resDo.setDiameter(fromDto.getDiameter());

		if (!ServicesUtil.isEmpty(fromDto.getStartLocation()))
			resDo.setStartLocation(fromDto.getStartLocation());
		
		if (!ServicesUtil.isEmpty(fromDto.getEndLocation()))
			resDo.setEndLocation(fromDto.getEndLocation());
		
		
		if (!ServicesUtil.isEmpty(fromDto.getFrequency()))
			resDo.setFrequency(fromDto.getFrequency());

		return resDo;
	}

	@Override
	protected PipelineMeasurementDto exportDto(PipelineMeasurementDo entity) {

		PipelineMeasurementDto dto = new PipelineMeasurementDto();

		if (!ServicesUtil.isEmpty(entity.getEquipmentId()))
			dto.setEquipmentId(entity.getEquipmentId());

		if (!ServicesUtil.isEmpty(entity.getLineName()))
			dto.setLineName(entity.getLineName());

		if (!ServicesUtil.isEmpty(entity.getFlocId()))
			dto.setFlocId(entity.getFlocId());

		if (!ServicesUtil.isEmpty(entity.getLength()))
			dto.setLength(entity.getLength());

		if (!ServicesUtil.isEmpty(entity.getDiameter()))
			dto.setDiameter(entity.getDiameter());

		if (!ServicesUtil.isEmpty(entity.getStartLocation()))
			dto.setStartLocation(entity.getStartLocation());
		
		if (!ServicesUtil.isEmpty(entity.getEndLocation()))
			dto.setEndLocation(entity.getEndLocation());
		
		if (!ServicesUtil.isEmpty(entity.getFrequency()))
			dto.setFrequency(entity.getFrequency());
		
		
		return dto;
	}
	
	public PipelineMeasurementDto getPipeLineMeasurment(String equipmentId){
		PipelineMeasurementDto resDto= new PipelineMeasurementDto();
		if(!ServicesUtil.isEmpty(equipmentId));
		{
			try{
				Criteria criteria=getSession().createCriteria(PipelineMeasurementDo.class);
				criteria.add(Restrictions.eq("equipmentId", equipmentId));
				resDto=exportDto((PipelineMeasurementDo)criteria.uniqueResult());
			}
			catch(Exception e){
				logger.error("[Murphy][PipeelineMeasurementFacad][getPipelineMeasurment][error]" + e.getMessage());
			}
		}
		return resDto;
	}
    
	public String getLocationText(String locationCode){
		String locationText="";
			if(!ServicesUtil.isEmpty(locationCode)){
			try{
				Query query=this.getSession().createSQLQuery("SELECT PL.LOCATION_TEXT FROM PRODUCTION_LOCATION AS PL where PL.LOCATION_CODE = '"+locationCode+"'");
				Object result=query.uniqueResult();
				
				locationText= result.toString();
			}
			catch(Exception e)
			{
				logger.error("[Murphy][PipeelineMeasurementFacad][getLocationText][error]" + e.getMessage());
			}
		}
			return locationText;
	}
	public String getLocationType(String locationCode){
		String locationType="";
			if(!ServicesUtil.isEmpty(locationCode)){
			try{
				Query query=this.getSession().createSQLQuery("SELECT PL.LOCATION_TYPE FROM PRODUCTION_LOCATION AS PL where PL.LOCATION_CODE = '"+locationCode+"'");
				Object result=query.uniqueResult();
				
				locationType= result.toString();
			}
			catch(Exception e)
			{
				logger.error("[Murphy][PipeelineMeasurementFacad][getLocationType][error]" + e.getMessage());
			}
		}
			return locationType;
	}
	
	public ResponseMessage updateTaskForPigging(TaskEventsDto dto){
   	 ResponseMessage res=new ResponseMessage();
   	 res.setStatus(MurphyConstant.FAILURE);
   	 try{
   		   
   		 String hql="update ProcessEventsDo pe set pe.startedBy = :createdBy,pe.startedByDisplayName= :startedByDisplayName where pe.processId= :processId";
				
           	Query query = getSession().createQuery(hql);
           	query.setParameter("createdBy",dto.getCreatedBy());
			query.setParameter("startedByDisplayName",dto.getCreatedByDisplay());
			query.setParameter("processId", dto.getProcessId());
			query.executeUpdate();
   		 res.setStatus(MurphyConstant.SUCCESS);
   		 
   		 
   	 }
   	 catch(Exception e){
   		 System.err.println("[Murphy][TaskEventsDao][saveOrUpdateTask][error]" + e.getMessage());
   	 }
   	 return res;
    }

	@SuppressWarnings("unchecked")
	public List<String> getMuwiForEquipment(String equipmentId) {
		List<String> functionalLocations= new ArrayList<>();
		try{
			Query query=this.getSession().createSQLQuery("SELECT PEL.LOCATION_CODE FROM PIGGING_EQUIPMENT_LOCATION AS PEL where PEL.EQUIPMENT_ID = '"+equipmentId+"'");
			List<Object> response = (List<Object>) query.list();
			
			for(Object locCode:response){
			    functionalLocations.add((String)locCode);
			}
			
		}
		catch(Exception e){
		
			 System.err.println("[Murphy][TaskEventsDao][getMuwiForEquipment][error]" + e.getMessage());
	}
		return functionalLocations;
}}
