package com.murphy.taskmgmt.dao;

import java.util.UUID;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskTemplatesDto;
import com.murphy.taskmgmt.entity.TaskTemplatesDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("TaskTemplatesDao")
@Transactional
public class TaskTemplatesDao extends BaseDao<TaskTemplatesDo, TaskTemplatesDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskTemplatesDao.class);

	public TaskTemplatesDao() {
	}


	@Autowired
	private CustomAttrTemplateDao attrDao;
	
	@Override
	protected TaskTemplatesDto exportDto(TaskTemplatesDo entity) {
		TaskTemplatesDto dto = new TaskTemplatesDto();

		if (!ServicesUtil.isEmpty(entity.getTaskTemplateId()))
			dto.setTaskTemplateId(entity.getTaskTemplateId());
		if (!ServicesUtil.isEmpty(entity.getProcessTemplateId()))
			dto.setProcessTemplateId(entity.getProcessTemplateId());
		if (!ServicesUtil.isEmpty(entity.getPriority()))
			dto.setPriority(entity.getPriority());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			dto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getName()))
			dto.setName(entity.getName());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			dto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getTaskType()))
			dto.setTaskType(entity.getTaskType());
		if (!ServicesUtil.isEmpty(entity.getOwnerGroup()))
			dto.setOwnerGroup(entity.getOwnerGroup());
		if (!ServicesUtil.isEmpty(entity.getSla()))
			dto.setSla(entity.getSla());
		//		if(!ServicesUtil.isEmpty(entity.getShouldWait()))
		//			dto.setShouldWait(entity.getShouldWait());
		//		if(!ServicesUtil.isEmpty(entity.getIfAccepts()))
		//			dto.setIfAccepts(entity.getIfAccepts());
		//		if(!ServicesUtil.isEmpty(entity.getIfRejects()))
		//			dto.setIfRejects(entity.getIfRejects());
		if(!ServicesUtil.isEmpty(entity.getUrl()))
			dto.setUrl(entity.getUrl());
		if (!ServicesUtil.isEmpty(entity.getOwnerGroupId()))
			dto.setOwnerGroupId(entity.getOwnerGroupId());


		return dto;
	}

	@Override
	protected TaskTemplatesDo importDto(TaskTemplatesDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		TaskTemplatesDo entity = new TaskTemplatesDo();

		if (!ServicesUtil.isEmpty(fromDto.getTaskTemplateId()))
			entity.setTaskTemplateId(fromDto.getTaskTemplateId());
		if (!ServicesUtil.isEmpty(fromDto.getProcessTemplateId()))
			entity.setProcessTemplateId(fromDto.getProcessTemplateId());
		if (!ServicesUtil.isEmpty(fromDto.getPriority()))
			entity.setPriority(fromDto.getPriority());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			entity.setDescription(fromDto.getDescription());
		if (!ServicesUtil.isEmpty(fromDto.getName()))
			entity.setName(fromDto.getName());
		if (!ServicesUtil.isEmpty(fromDto.getSubject()))
			entity.setSubject(fromDto.getSubject());
		if (!ServicesUtil.isEmpty(fromDto.getTaskType()))
			entity.setTaskType(fromDto.getTaskType());
		if (!ServicesUtil.isEmpty(fromDto.getOwnerGroup()))
			entity.setOwnerGroup(fromDto.getOwnerGroup());
		if (!ServicesUtil.isEmpty(fromDto.getSla()))
			entity.setSla(fromDto.getSla());
		//		if(!ServicesUtil.isEmpty(fromDto.getShouldWait()))
		//			entity.setShouldWait(fromDto.getShouldWait());
		//		if(!ServicesUtil.isEmpty(fromDto.getIfAccepts()))
		//			entity.setIfAccepts(fromDto.getIfAccepts());
		//		if(!ServicesUtil.isEmpty(fromDto.getIfRejects()))
		//			entity.setIfRejects(fromDto.getIfRejects());
		if(!ServicesUtil.isEmpty(fromDto.getUrl()))
			entity.setUrl(fromDto.getUrl());
		if (!ServicesUtil.isEmpty(fromDto.getOwnerGroupId()))
			entity.setOwnerGroupId(fromDto.getOwnerGroupId());

		return entity;
	}


	public ResponseMessage createTaskTemp(TaskTemplatesDto dto){

		logger.error("Create TaskTemplatesDto"+dto);
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
		responseMessage.setStatus(MurphyConstant.SUCCESS);
		responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		try{
			String taskTemplateId = UUID.randomUUID().toString().replaceAll("-", "");
			dto.setTaskTemplateId(taskTemplateId);
			create(dto);
			if(!ServicesUtil.isEmpty(dto.getAttrList())){

				for(CustomAttrTemplateDto clDto : dto.getAttrList()){
					clDto.setTaskTempId(taskTemplateId);
					ResponseMessage errorReturn = attrDao.createCLTemp(clDto);
					if(!errorReturn.getStatus().equals(MurphyConstant.SUCCESS)){
						return errorReturn;
					}
				}
			}
			return responseMessage;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("[Murphy][TaskManagement][TaskTemplatesDao][createTaskTemp][error]"+e.getMessage());
		}
		responseMessage.setMessage("Create of task Failed");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		return responseMessage;
	}

	public ResponseMessage createTaskFromTemp(TaskTemplatesDto dto) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);






		return null;
	}

	public TaskTemplatesDto getTemplateById(String taskTemplateId) {

		try{
			String queryString = "select te from TaskTemplatesDo te where te.taskTemplateId= '"+taskTemplateId+"' ";
			Query q = this.getSession().createQuery(queryString);
			q.setParameter("isProcessed", true);
			TaskTemplatesDo taskTemplate = (TaskTemplatesDo) q.uniqueResult();
			if(!ServicesUtil.isEmpty(taskTemplate)){
				return exportDto(taskTemplate);
			}
		}
		catch(Exception e){
			logger.error("[Murphy][TaskOwnersDao][checkIfUserIsOwner][error]"+e.getMessage());
		}
		return null;


	}

}
