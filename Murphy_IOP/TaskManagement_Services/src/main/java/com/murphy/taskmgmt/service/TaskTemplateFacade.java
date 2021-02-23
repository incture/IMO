package com.murphy.taskmgmt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.CustomAttrInstancesDao;
import com.murphy.taskmgmt.dao.ProcessTemplateDao;
import com.murphy.taskmgmt.dao.TaskEventsDao;
import com.murphy.taskmgmt.dao.TaskTemplatesDao;
import com.murphy.taskmgmt.dto.CustomAttrInstancesDto;
import com.murphy.taskmgmt.dto.ProcessTemplateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskTemplatesDto;
import com.murphy.taskmgmt.service.interfaces.TaskTemplateFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

/**
 * Session Bean implementation class ConsumeODataFacade
 */

@Service("taskTemplateService")
public class TaskTemplateFacade implements TaskTemplateFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(TaskTemplateFacade.class);

	@Autowired
	private ProcessTemplateDao processTemplateDao;

	@Autowired
	private TaskTemplatesDao taskTemplateDao;
	
	@Autowired
	private TaskEventsDao taskEventsDao;
	
	//	@Autowired
	//	private GroupsDao groupsDao;

	
	@Autowired
	private CustomAttrInstancesDao attrDao;

	public TaskTemplateFacade() {
	}

	@Override
	public ResponseMessage createTemplate(ProcessTemplateDto dto){
		return processTemplateDao.createProcessTemp(dto);
	}

	@Override
	public ProcessTemplateDto readTemplate(String procTemplateId){

		return processTemplateDao.readALLTaskTemplates(procTemplateId);
	}

	@Override
	public ResponseMessage createTaskTemplate(TaskTemplatesDto dto){
		return taskTemplateDao.createTaskTemp(dto);
	}

	@Override
	public ResponseMessage createTaskFromTemplate(TaskTemplatesDto dto){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		if(!ServicesUtil.isEmpty(dto.getTaskTemplateId())){

			try{
				TaskTemplatesDto  templateDto =  taskTemplateDao.getTemplateById( dto.getTaskTemplateId());
				if(!ServicesUtil.isEmpty(templateDto)){
					//	SimpleDateFormat formatter = new SimpleDateFormat(MurphyConstant.Murphy_DATE_FORMATE);
					Calendar calInstance =  Calendar.getInstance();
					TaskEventsDto taskEventDto = new TaskEventsDto();
					String taskId = UUID.randomUUID().toString().replaceAll("-", "");
					taskEventDto.setTaskId(taskId);
					taskEventDto.setProcessId(dto.getProcessTemplateId());
					taskEventDto.setCreatedAt(new Date(calInstance.getTimeInMillis()));
					taskEventDto.setCompletionDeadLine(new Date(ServicesUtil.getSLADueDate(calInstance,templateDto.getSla()).getTimeInMillis()));
					taskEventDto.setDescription(templateDto.getDescription());
					taskEventDto.setDetailUrl(templateDto.getUrl());
					taskEventDto.setName(templateDto.getName());
					taskEventDto.setSubject(templateDto.getSubject());
					taskEventDto.setStatus("READY");
					taskEventDto.setProcessName("");
					taskEventDto.setPriority(templateDto.getPriority());
					taskEventsDao.create(taskEventDto);
					/*if(status.equals(MurphyConstant.SUCCESS)){
						TaskOwnersDto ownerDto =null;
						GroupsDto groupDto = groupsDao.getGroupById(dto.getOwnerGroupId());
						if(!ServicesUtil.isEmpty(groupDto) && !ServicesUtil.isEmpty( groupDto.getGroupMembers())){

							for(GroupsUserDto  userDto: groupDto.getGroupMembers()){
								ownerDto = new TaskOwnersDto();
								ownerDto.setTaskId(taskId);
								ownerDto.setIsProcessed(false);
								ownerDto.setTaskOwner(userDto.getUserId());
								ownerDto.setTaskOwnerDisplayName(userDto.getUserName());
								ownerDto.setIsSubstituted(false);
								if(! taskOwnersDao.createOwner(ownerDto).equals(MurphyConstant.SUCCESS)){
									responseMessage.setMessage("Failed to create owners");
									return responseMessage;
								}
							}
						}else{
							responseMessage.setMessage("No Members found for the particular group");
							return responseMessage;
						}*/

						if(!ServicesUtil.isEmpty(dto.getClInstanceList())){
							for(CustomAttrInstancesDto  clInstance: dto.getClInstanceList()){
								if(! attrDao.createCLInstance(clInstance).equals(MurphyConstant.SUCCESS)){
									responseMessage.setMessage("Failed to create checkList Instances");
									return responseMessage;
								}
							}
						}

//					}

					responseMessage.setMessage("Task Created successfuly");	
					responseMessage.setStatus(MurphyConstant.SUCCESS);
					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
					return responseMessage;

				}else{
					responseMessage.setMessage("No Task Template found with the Id");
					return responseMessage;
				}
			}
			catch(Exception e ){
				logger.error("[Murphy][TaskManagementFacade][createTask][error]"+e.getMessage());
				responseMessage.setMessage("Task Creation failed ");
				return responseMessage;
			}
		}
		else{
			responseMessage.setMessage("Task Template Id is required to create a task");
			return responseMessage;
		}
	}



}
