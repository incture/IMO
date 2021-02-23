package com.murphy.taskmgmt.dao;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.CygnetRecomDowntimeDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.CygnetRecomDowntimeDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("cygnetRecomDowntimeDao")
public class CygnetRecomDowntimeDao extends BaseDao<CygnetRecomDowntimeDo, CygnetRecomDowntimeDto>{
	
	private static final Logger logger = LoggerFactory.getLogger(CygnetDowntimeCounterDao.class);

	@Override
	protected CygnetRecomDowntimeDo importDto(CygnetRecomDowntimeDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		CygnetRecomDowntimeDo entity = new CygnetRecomDowntimeDo();
		if (!ServicesUtil.isEmpty(fromDto.getId()))
			entity.setId(fromDto.getId());
		if (!ServicesUtil.isEmpty(fromDto.getPointId()))
			entity.setPointId(fromDto.getPointId());
		if (!ServicesUtil.isEmpty(fromDto.getStartTime()))
			entity.setStartTime(fromDto.getStartTime());
		if (!ServicesUtil.isEmpty(fromDto.getEndTime()))
			entity.setEndTime(fromDto.getEndTime());
		if (!ServicesUtil.isEmpty(fromDto.getCygnetRecomDuration()))
			entity.setCygnetRecomDuration(fromDto.getCygnetRecomDuration());
		return entity;
	}

	@Override
	protected CygnetRecomDowntimeDto exportDto(CygnetRecomDowntimeDo entity) {
		CygnetRecomDowntimeDto dto = new CygnetRecomDowntimeDto();
		if (!ServicesUtil.isEmpty(entity.getId()))
			dto.setId(entity.getId());
		if (!ServicesUtil.isEmpty(entity.getPointId()))
			dto.setPointId(entity.getPointId());
		if (!ServicesUtil.isEmpty(entity.getStartTime()))
			dto.setStartTime(entity.getStartTime());
		if (!ServicesUtil.isEmpty(entity.getEndTime()))
			dto.setEndTime(entity.getEndTime());
		if (!ServicesUtil.isEmpty(entity.getCygnetRecomDuration()))
			dto.setCygnetRecomDuration(entity.getCygnetRecomDuration());
		return dto;
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public boolean load(String pointId, Date startTime){
		
		 boolean isRecordExist = false;
		 SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yy HH:mm:ss:SSS");
		
			try{
				String queryString = "select crd.POINT_ID From CYGNET_RECOM_DOWNTIME crd "
				+"where crd.POINT_ID ='"+pointId+"' and crd.START_TIME IS NOT NULL and crd.END_TIME IS NULL";
				Query q =  this.getSession().createSQLQuery(queryString.trim());
				List<Object[]> resultList = q.list();
				if(resultList.size() !=0){
					isRecordExist = true;
				}
			
		} catch(Exception e) {
			logger.error("Error in cygnet downtime"+e);
		}
		
		return isRecordExist;
		
	}
	
	@SuppressWarnings("unchecked")
	public void updateEndTime(String pointId, Date endTime){
		try{
			Date startTime = null;

			String queryString = "select crd.START_TIME From CYGNET_RECOM_DOWNTIME crd "
					+"where crd.POINT_ID ='"+pointId+"' and crd.END_TIME IS NULL";
			Query q =  this.getSession().createSQLQuery(queryString.trim());

		//	logger.error("[Murphy][TaskManagement][CygnetRecomDowntimeDao][updateEndTime][info][query]"+queryString);
			List<Date> resultList = q.list();
			if(!ServicesUtil.isEmpty(resultList)){	
				for(Date obj : resultList){
				//	logger.error("[Murphy][TaskManagement][CygnetRecomDowntimeDao][updateEndTime][info][obj]"+obj);
					startTime= obj;
				}
			}
            
			//logger.error("[Murphy][TaskManagement][CygnetRecomDowntimeDao][updateEndTime][info][starttime]"+startTime);
			
			if(startTime != null) {
				long startTimemillis = startTime.getTime();
								
				long datemillis = endTime.getTime();

				long recomTime = datemillis - startTimemillis;

				String queryString1 = "Update CYGNET_RECOM_DOWNTIME set END_TIME = '"+endTime+"',CYGNET_RECOM_DURATION = '"+recomTime+"'   where POINT_ID ='"+pointId+"' and END_TIME IS NULL";
				//logger.error("[Murphy][TaskManagement][CygnetRecomDowntimeDao][updateEndTime][info][queryupdate]"+queryString1);
				Query q1 = this.getSession().createSQLQuery(queryString1);
				q1.executeUpdate();
			}


		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][CygnetRecomDowntimeDao][updateEndTime][error]"+e.getMessage());
		}

	}
	
	
	public ResponseMessage createRecomDowntime(CygnetRecomDowntimeDo cygnetRecomDowntimeDo) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			this.getSession().persist(cygnetRecomDowntimeDo);
			responseDto.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingDao][createRecord][error]" + e.getMessage());
			responseDto.setMessage(MurphyConstant.CREATE_FAILURE);
		}
		return responseDto;
	}


}
