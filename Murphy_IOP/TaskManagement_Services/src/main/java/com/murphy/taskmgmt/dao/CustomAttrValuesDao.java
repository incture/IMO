package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.CustomAttrValuesDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.CustomAttrValuesDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("CustomAttrValuesDao")
@Transactional
public class CustomAttrValuesDao extends BaseDao<CustomAttrValuesDo, CustomAttrValuesDto> {

	private static final Logger logger = LoggerFactory.getLogger(CustomAttrValuesDao.class);

	public CustomAttrValuesDao() {
	}

	@Override
	protected CustomAttrValuesDo importDto(CustomAttrValuesDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {

		CustomAttrValuesDo entity = new CustomAttrValuesDo();
		if (!ServicesUtil.isEmpty(fromDto.getValueId()))
			entity.setValueId(fromDto.getValueId());
		if (!ServicesUtil.isEmpty(fromDto.getClItemId()))
			entity.setClItemId(fromDto.getClItemId());
		if (!ServicesUtil.isEmpty(fromDto.getValue()))
			entity.setValue(fromDto.getValue());
		if (!ServicesUtil.isEmpty(fromDto.getDependentValue()))
			entity.setDependentValue(fromDto.getDependentValue());

		return entity;
	}

	@Override
	protected CustomAttrValuesDto exportDto(CustomAttrValuesDo entity) {

		CustomAttrValuesDto dto = new CustomAttrValuesDto();
		if (!ServicesUtil.isEmpty(entity.getValueId()))
			dto.setValueId(entity.getValueId());
		if (!ServicesUtil.isEmpty(entity.getClItemId()))
			dto.setClItemId(entity.getClItemId());
		if (!ServicesUtil.isEmpty(entity.getValue()))
			dto.setValue(entity.getValue());
		if (!ServicesUtil.isEmpty(entity.getDependentValue()))
			dto.setDependentValue(entity.getDependentValue());

		return dto;
	}

	public ResponseMessage createCLOption(CustomAttrValuesDto dto){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try{
			create(dto);
			responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][CustomAttrValuesDao][createCLTemp][error]" +e.getMessage());
		}

		return responseMessage;
	}

	public List<CustomAttrValuesDto> getTaskTypes(){
		List<CustomAttrValuesDto> responseList = null;
		try{
			String queryString = "Select ndt from CustomAttrValuesDo ndt where ndt.dependentValue ='Non Downtime'";

			Query q =  this.getSession().createQuery(queryString.trim());
			@SuppressWarnings("unchecked")
			List<CustomAttrValuesDo> resultList = (List<CustomAttrValuesDo>) q.list();
			responseList = new ArrayList<CustomAttrValuesDto>();
			if(!ServicesUtil.isEmpty(resultList)){
				for(CustomAttrValuesDo entity : resultList){
					responseList.add(exportDto(entity));
				}
			}
//			CustomAttrValuesDto valueDto  = new CustomAttrValuesDto();
//			valueDto.setValue("Others");
//			responseList.add(valueDto);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][CustomAttrValuesDao][getTaskTypes][error]" +e.getMessage());
		}

		return responseList;
	}




}
