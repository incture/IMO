package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dao.CollaborationDao;
import com.murphy.taskmgmt.dao.CustomAttrTemplateDao;
import com.murphy.taskmgmt.dao.TaskEventsDao;
import com.murphy.taskmgmt.dao.TaskOwnersDao;
import com.murphy.taskmgmt.dto.CollaborationDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.StartTimeResponseDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
import com.murphy.taskmgmt.dto.TaskSchedulingResponseDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingCalFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;



@Service("taskSchedulingFacade")
public class TaskSchedulingFacade implements TaskSchedulingFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(TaskSchedulingFacade.class);

	public TaskSchedulingFacade() {
	}

	@Autowired
	private TaskOwnersDao ownerDao;

	@Autowired
	private TaskEventsDao taskDao;

	@Autowired
	private CustomAttrTemplateDao attrTempDao;
	
	@Autowired
	private CollaborationDao collaborationDao;

	@Autowired
	private AuditDao auditDao;
	
	@Autowired
	TaskSchedulingCalFacadeLocal calFacadeLocal;

//	@Autowired
//	private GeoTabDao geoDao;

	@Override 
	public ResponseMessage updatePriority(UpdateRequestDto dto){
		ResponseMessage response = new ResponseMessage();
		response.setMessage(MurphyConstant.UPDATE_FAILURE);
		response.setStatus(MurphyConstant.FAILURE);
		response.setStatusCode(MurphyConstant.CODE_FAILURE);
		//		if(ownerDao.updatePriority(dto.getUserId(), dto.getStartDate(),  dto.getEndDate(),dto.getTaskId()).equals(MurphyConstant.SUCCESS)){
		if(ownerDao.updatePriority(dto.getUserId(), dto.getCurrentPosition(), dto.getNewPosition(),dto.getTaskId()).equals(MurphyConstant.SUCCESS)){	
			response.setMessage(MurphyConstant.UPDATE_SUCCESS);
			response.setStatus(MurphyConstant.SUCCESS);
			response.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		return response;
	}

	@Override 
	public TaskSchedulingResponseDto getDataForScheduling(String userId, String orderBy ,String role,int timeZoneOffSet, 
			String fromDate,String toDate){ 
		timeZoneOffSet = - timeZoneOffSet;
		return  taskDao.getDataForScheduling(userId , orderBy ,role , timeZoneOffSet,fromDate,toDate);
	}

	@Override
	public TaskOwnersResponeDto getTaskOwnersbyId(String taskId ,String role){
		TaskOwnersResponeDto responseDto = new TaskOwnersResponeDto();
		ResponseMessage response = new ResponseMessage();
		response.setMessage(MurphyConstant.UPDATE_FAILURE);
		response.setStatus(MurphyConstant.FAILURE);
		response.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			List<TaskOwnersDto> ownersList = ownerDao.getTaskOwnersbyId(taskId ,role);
			if(!ServicesUtil.isEmpty(ownersList)){
				responseDto.setOwnerList(ownersList);
				response.setMessage(MurphyConstant.READ_SUCCESS);
			}else{
				response.setMessage(MurphyConstant.NO_RESULT);
			}
			response.setStatus(MurphyConstant.SUCCESS);
			response.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}catch(Exception e){
			logger.error("[Murphy][TaskSchedulingFacade][getTaskOwnersbyId][error]"+e.getMessage());
		}
		responseDto.setResponseMessage(response);
		return responseDto;

	}

	@Override
	public String intialUpdateOfPriority(){
		return  ownerDao.intialUpdateOfPriority();
	}

	@Override
	public StartTimeResponseDto getStartTimeForUser(String userId, String locationCode, String classification,
			String subClassification) {
		StartTimeResponseDto responseDto = new StartTimeResponseDto();
		ResponseMessage response = new ResponseMessage();
		response.setMessage(MurphyConstant.READ_FAILURE);
		response.setStatus(MurphyConstant.FAILURE);
		response.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			float resolveTime = attrTempDao.getEstTimeForSubClass(classification.trim(),subClassification.trim(), userId.trim());
			logger.error("[getStartTimeForUser][resolveTime] :" + resolveTime);
			float resolveTimeInSecs = resolveTime * 60 ;
			responseDto = ownerDao.getNewStartOfUser(userId, locationCode, resolveTimeInSecs,null);
			responseDto.setStartTimeInString(ServicesUtil.convertFromZoneToZoneString(responseDto.getStartTime(), "", MurphyConstant.UTC_ZONE, MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_DISPLAY_FORMAT));
			response.setMessage(MurphyConstant.READ_SUCCESS);
			response.setStatus(MurphyConstant.SUCCESS);
			response.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}catch(Exception e){
			logger.error("[Murphy][TaskSchedulingFacade][getTaskOwnersbyId][error]"+e.getMessage());
		}
		responseDto.setResponseMessage(response);
		return responseDto;
	}

	@Override
	public ResponseMessage reassignInAdminConsole(CustomTaskDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			if(!ServicesUtil.isEmpty(dto) && !ServicesUtil.isEmpty(dto.getTaskEventDto().getTaskId()) && !ServicesUtil.isEmpty(dto.getTaskEventDto().getOwners())){
				String originalTaskId = dto.getTaskEventDto().getTaskId();
				String originalProcId = dto.getTaskEventDto().getProcessId();
				
				//Cancelled Status Update in Audit Log
				auditDao.createInstance(MurphyConstant.CANCELLED,
						dto.getTaskEventDto().getForwardedBy(), originalTaskId);
				
				// Updating status in Process and task Events table
				String res = taskDao.updateStatusIopAdminConsole(dto.getTaskEventDto());
				
				//Making the Task Id , Message ID, Process ID as null since new Ids needs to be generated while copying it to new task.
				if(!ServicesUtil.isEmpty(dto.getCollabrationDtos())){
					logger.error("Collaboration Dto : "+dto.getCollabrationDtos());
					for(CollaborationDto collab : dto.getCollabrationDtos()){
						collab.setMessageId(null);
						collab.setProcessId(null);
						collab.setTaskId(null);
					}
				}
				
				// Owner : taskID = null since need new task Id
				logger.error("TaskOwnersDto Dto : "+dto.getTaskEventDto().getOwners());
				for(TaskOwnersDto ownerDto : dto.getTaskEventDto().getOwners()){
					ownerDto.setTaskId(null);
				}
				
				
				// Making task, Process id null in original Task so that new task is created &
				// Setting old task as Prev in New Task
				dto.getTaskEventDto().setTaskId(null);
				dto.getTaskEventDto().setProcessId(null);
				dto.getTaskEventDto().setPrevTask(originalTaskId);
				dto.getTaskEventDto().setCreatedAtInString(null);
				dto.getTaskEventDto().setCreatedAt(null);
				dto.getTaskEventDto().setTaskRefNum(null);
				dto.getTaskEventDto().setStatus(MurphyConstant.ASSIGN);
				String subject = dto.getTaskEventDto().getSubject().trim().split(" - ")[1].toString();
				dto.getTaskEventDto().setSubject(subject);
				
				//For pig launch task set the pig retrieval time from previous task - INC0099761
				if(dto.getTaskEventDto().getParentOrigin().equals(MurphyConstant.PIGGING)){
					if(dto.getCustomAttr().get(2).getClItemId().equals("PIG001")){
						String time = attrTempDao.getPigRetrievalofPrevTask(originalTaskId);
						dto.getCustomAttr().get(2).setLabelValue(time);
						dto.getCustomAttr().get(2).getValueDtos().get(0).setValue(time);
					}
				}
				
				// Calling Create Task to create new task
				logger.error("Custom Dto from IOP Admin Console : "+dto.toString());
				responseMessage = calFacadeLocal.createTask(dto);
				
				if(responseMessage.getStatus().equalsIgnoreCase(MurphyConstant.SUCCESS))
					responseMessage.setMessage(MurphyConstant.TASK + " " + MurphyConstant.REASSIGN + " " + MurphyConstant.SUCCESSFULLY);
				
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskSchedulingFacade][reassignInAdminConsole][error]" + e.getMessage());
		}
		return responseMessage;
	}


}

