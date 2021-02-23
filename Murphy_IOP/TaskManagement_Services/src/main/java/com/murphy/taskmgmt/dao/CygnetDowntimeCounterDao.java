package com.murphy.taskmgmt.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.CygnetDownTimeCounterDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.CygnetDowntimeCounterDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;



@Repository("cygnetDowntimeCounterDao")
public class CygnetDowntimeCounterDao extends BaseDao<CygnetDowntimeCounterDo, CygnetDownTimeCounterDto>{
	
	private static final Logger logger = LoggerFactory.getLogger(CygnetDowntimeCounterDao.class);

	@Override
	protected CygnetDowntimeCounterDo importDto(CygnetDownTimeCounterDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		
		CygnetDowntimeCounterDo entity = new CygnetDowntimeCounterDo();
		if (!ServicesUtil.isEmpty(fromDto.getId()))
			entity.setId(fromDto.getId());
		if (!ServicesUtil.isEmpty(fromDto.getPointId()))
			entity.setPointId(fromDto.getPointId());
		if (!ServicesUtil.isEmpty(fromDto.getAlarmSeverity()))
			entity.setAlarmSeverity(fromDto.getAlarmSeverity());
		if (!ServicesUtil.isEmpty(fromDto.getTimeStamp()))
			entity.setTimeStamp(fromDto.getTimeStamp());
		
		return entity;
	}

	@Override
	protected CygnetDownTimeCounterDto exportDto(CygnetDowntimeCounterDo entity) {
		CygnetDownTimeCounterDto dto = new CygnetDownTimeCounterDto();
		if (!ServicesUtil.isEmpty(entity.getId()))
			dto.setId(entity.getId());
		if (!ServicesUtil.isEmpty(entity.getPointId()))
			dto.setPointId(entity.getPointId());
		if (!ServicesUtil.isEmpty(entity.getAlarmSeverity()))
			dto.setAlarmSeverity(entity.getAlarmSeverity());
		if (!ServicesUtil.isEmpty(entity.getTimeStamp()))
			dto.setTimeStamp(entity.getTimeStamp());
		
		return dto;
	}
	
	@SuppressWarnings("unchecked")
	public boolean load(String pointId, String alarmSeverity, Date timeStamp){
		
		 boolean isRecordExist = false;
		 SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yy HH:mm:ss:SSS");
			try{
				String queryString = "select cdc.POINT_ID From CYGNET_DOWNTIME_COUNTER cdc "
				+"where cdc.POINT_ID ='"+pointId+"' and cdc.ALARM_SEVERITY='"+alarmSeverity+"' and cdc.TIME_STAMP=TO_TIMESTAMP('"+sf.format(timeStamp)+"','dd-Mon-yy hh24:mi:ss:ff3')";
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
	
	public ResponseMessage createDowntimeCounter(CygnetDowntimeCounterDo cygnetDowntimeCounterDo) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			this.getSession().persist(cygnetDowntimeCounterDo);
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
