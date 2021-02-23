package com.murphy.taskmgmt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.CanaryStagingDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.CanaryStagingNDVDo;
import com.murphy.taskmgmt.entity.CanaryStagingNDVPK;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("canaryStagingNDVDao")
public class CanaryStagingNDVDao extends BaseDao<CanaryStagingNDVDo, CanaryStagingDto> {

	private static final Logger logger = LoggerFactory.getLogger(CanaryStagingNDVDao.class);

	public CanaryStagingNDVDao() {
	}

	@Override
	protected CanaryStagingNDVDo importDto(CanaryStagingDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {

		CanaryStagingNDVDo entity = new CanaryStagingNDVDo();
		CanaryStagingNDVPK pk = new CanaryStagingNDVPK();
		if (!ServicesUtil.isEmpty(fromDto.getStagingId()))
			entity.setStagingId(fromDto.getStagingId());
		if (!ServicesUtil.isEmpty(fromDto.getMuwiId()))
			pk.setMuwiId(fromDto.getMuwiId());
		if (!ServicesUtil.isEmpty(fromDto.getParameterType()))
			pk.setParameterType(fromDto.getParameterType());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			pk.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getDataValue()))
			entity.setDataValue(fromDto.getDataValue());
		entity.setCanaryStagingNDVPK(pk);
		return entity;
	}

	@Override
	protected CanaryStagingDto exportDto(CanaryStagingNDVDo entity) {

		CanaryStagingDto dto = new CanaryStagingDto();
		CanaryStagingNDVPK pk = entity.getCanaryStagingNDVPK();
		
		if (!ServicesUtil.isEmpty(entity.getStagingId()))
			dto.setStagingId(entity.getStagingId());
		if (!ServicesUtil.isEmpty(pk.getMuwiId()))
			dto.setMuwiId(pk.getMuwiId());
		if (!ServicesUtil.isEmpty(pk.getParameterType()))
			dto.setParameterType(pk.getParameterType());
		if (!ServicesUtil.isEmpty(pk.getCreatedAt()))
			dto.setCreatedAt(pk.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getDataValue()))
			dto.setDataValue(entity.getDataValue());

		return dto;
	}

	public ResponseMessage createStaging(CanaryStagingDto dto, int count) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
//			create(dto);
			this.getSession().saveOrUpdate(importDto(dto));
			if((count % MurphyConstant.BATCH_PERSIST_SIZE) == 0) {
				this.getSession().flush();
				this.getSession().clear();
			}
			responseDto.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingNDVDao][createRecord][error]" + e.getMessage());
			responseDto.setMessage(MurphyConstant.CREATE_FAILURE);
		}
		return responseDto;
	}
	
/*	@SuppressWarnings("unchecked")
	public List<Object> getAllDatesInDesc(String date, String currentDate ){
		List<Object> result = null;
		try {
			String queryString = "SELECT DISTINCT(CREATED_AT) FROM TM_CANARY_STAGING_NDV WHERE( CREATED_AT > TO_TIMESTAMP('"+date+":00', 'yyyy-mm-dd hh24:mi:ss') AND CREATED_AT < TO_TIMESTAMP('"+currentDate+":00', 'yyyy-mm-dd hh24:mi:ss')) ORDER BY CREATED_AT";
			Query q = this.getSession().createSQLQuery(queryString);
//			logger.error("[Murphy][CanaryStagingNDVDao][deleteAllDataBeforeDate][queryString]" + queryString);
			result = (List<Object>)  q.list();

		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingNDVDao][deleteAllDataBeforeDate][error]" + e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	public String deleteAllDataBeforeDate(String date){
		String response = MurphyConstant.FAILURE;
		try {
			String queryString = "delete from TM_CANARY_STAGING_NDV where created_at < TO_TIMESTAMP('"+date+":00', 'yyyy-mm-dd hh24:mi:ss') ";
			Query q = this.getSession().createSQLQuery(queryString);
			logger.error("[Murphy][CanaryStagingNDVDao][deleteAllDataBeforeDate][queryString]" + queryString);
			Integer result = (Integer)  q.executeUpdate();
			response = MurphyConstant.SUCCESS;

		} catch (Exception e) {
			logger.error("[Murphy][CanaryStagingNDVDao][deleteAllDataBeforeDate][error]" + e.getMessage());
		}
		return response;

	}
*/


}