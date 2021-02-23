package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RootCauseInstDto;
import com.murphy.taskmgmt.entity.RootCauseInstDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("RootCauseInstDao")
@Transactional
public class RootCauseInstDao extends BaseDao<RootCauseInstDo, RootCauseInstDto> {

	private static final Logger logger = LoggerFactory.getLogger(RootCauseInstDao.class);

	public RootCauseInstDao() {
	}

	@Override
	protected RootCauseInstDto exportDto(RootCauseInstDo entity) {
		RootCauseInstDto rootCauseInstDto = new RootCauseInstDto();
		if (!ServicesUtil.isEmpty(entity.getUniqueId()))
			rootCauseInstDto.setUniqueId(entity.getUniqueId());
		if (!ServicesUtil.isEmpty(entity.getTaskId()))
			rootCauseInstDto.setTaskId(entity.getTaskId());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			rootCauseInstDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			rootCauseInstDto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getAction()))
			rootCauseInstDto.setAction(entity.getAction());
		if (!ServicesUtil.isEmpty(entity.getSubClassification()))
			rootCauseInstDto.setSubClassification(entity.getSubClassification());
		if (!ServicesUtil.isEmpty(entity.getRootCause()))
			rootCauseInstDto.setRootCause(entity.getRootCause());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			rootCauseInstDto.setCreatedAt(entity.getCreatedAt());
		return rootCauseInstDto;
	}

	@Override
	protected RootCauseInstDo importDto(RootCauseInstDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		RootCauseInstDo entity = new RootCauseInstDo();
		if (!ServicesUtil.isEmpty(fromDto.getUniqueId()))
			entity.setUniqueId(fromDto.getUniqueId());
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			entity.setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			entity.setDescription(fromDto.getDescription());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getAction()))
			entity.setAction(fromDto.getAction());
		if (!ServicesUtil.isEmpty(fromDto.getSubClassification()))
			entity.setSubClassification(fromDto.getSubClassification());
		if (!ServicesUtil.isEmpty(fromDto.getRootCause()))
			entity.setRootCause(fromDto.getRootCause());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		return entity;
	}


	@SuppressWarnings("unchecked")
	public List<RootCauseInstDto> getRootInstById(String taskId ,String status){
		List<RootCauseInstDto> responseList = null;
		try{
			String queryString = "select te from RootCauseInstDo te where te.taskId = '"+taskId+"' and te.action = '"+status+"'"
					+ " and te.createdAt = (Select max(tx.createdAt)  from RootCauseInstDo tx where tx.taskId = '"+taskId+"' and tx.action = '"+status+"' )";
			Query q = this.getSession().createQuery(queryString);
			List<RootCauseInstDo> response = (List<RootCauseInstDo>) q.list();
			if(!ServicesUtil.isEmpty(response)){
				responseList = new ArrayList<RootCauseInstDto>();
				for(RootCauseInstDo entity : response){
					responseList.add(exportDto(entity));
				}
			}
//			logger.error("[Murphy][RootCauseInstDao][getRootInstById][query]"+queryString+"response"+response);
		}
		catch(Exception e){
			logger.error("[Murphy][RootCauseInstDao][getRootInstById][error]"+e.getMessage());
		}
		return responseList;
	}


	public ResponseMessage createRootCause(RootCauseInstDto dto){
		ResponseMessage response = new ResponseMessage();
		response.setMessage(MurphyConstant.CREATE_FAILURE);
		response.setStatus(MurphyConstant.FAILURE);
		response.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
//			System.err.println("Inside createRootCause "+ dto);
			create(dto);
			response.setMessage(MurphyConstant.CREATED_SUCCESS);
			response.setStatus(MurphyConstant.SUCCESS);
			response.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][RootCauseInstDao][createRootCause][error]"+e.getMessage());
		}
		
//		System.err.println("Inside createRootCause  response"+ response);
		return response;
	}





}
