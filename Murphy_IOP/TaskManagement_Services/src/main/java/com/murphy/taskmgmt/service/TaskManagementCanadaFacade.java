package com.murphy.taskmgmt.service;

/*
 * @author Prakash Kumar
 * Version 1.0
 * Since Sprint 6
 * */
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.AlsInvestigationDao;
import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dao.CustomAttrInstancesDao;
import com.murphy.taskmgmt.dao.NDTaskMappingDao;
import com.murphy.taskmgmt.dao.PWHopperStagingDao;
import com.murphy.taskmgmt.dao.ProcessEventsDao;
import com.murphy.taskmgmt.dao.RootCauseInstDao;
import com.murphy.taskmgmt.dao.TaskEventsDao;
import com.murphy.taskmgmt.dao.TaskOwnersCanadaDao;
import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.PiggingSchedulerDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RootCauseInstDto;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.entity.ProcessEventsDo;
import com.murphy.taskmgmt.service.interfaces.TaskManagementInterface;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
@Service("TaskManagementCanadaFacade")
public class TaskManagementCanadaFacade implements TaskManagementInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskManagementCanadaFacade.class);
	
	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private TaskOwnersCanadaDao taskOwnersDao;
	
	@Autowired
	private ProcessEventsDao processEventsDao;

	@Autowired
	private PiggingSchedulerFacade piggingSchedulerFacade;

	@Autowired
	private AuditDao auditDao;

	@Autowired
	private NDTaskMappingDao mappingDao;

	@Autowired
	private CustomAttrInstancesDao attrDao;

	@Autowired
	private RootCauseInstDao rootCauseDao;

	@Autowired
	private AlsInvestigationDao alsInvestigationDao;

	@Autowired
	private PWHopperStagingDao pwHopperDao;

	@Override
	public ResponseMessage updateTask(CustomTaskDto dto) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		if (MurphyConstant.CHECKLIST.equals(dto.getUpdateType())) {
			if (!ServicesUtil.isEmpty(dto.getCheckList())) {
				if (pwHopperDao.checkIfInvestigationDataExists(dto.getTaskEventDto().getProcessId())) {
					pwHopperDao.updateProactive(dto.getTaskEventDto().getLocationCode(), dto.isProactive(),
							dto.getTaskEventDto().getProcessId(), dto.getHopperId(), true);
					pwHopperDao.updateInvstInstance(dto.getTaskEventDto().getProcessId(), dto.getUserType(),
							dto.getCheckList(), dto.getLoggedInUserId());
				} else {
					String hopperId = ServicesUtil.getUUID();
					pwHopperDao.updateProactive(dto.getTaskEventDto().getLocationCode(), dto.isProactive(),
							dto.getTaskEventDto().getProcessId(), hopperId, true);
					pwHopperDao.createInvstInstance("", dto.getTaskEventDto().getLocationCode(),
							dto.getTaskEventDto().getProcessId(), dto.getCheckList(), hopperId,
							dto.getLoggedInUserId(),false);
				}
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseMessage.setMessage(MurphyConstant.CHECKLIST + " " + MurphyConstant.UPDATE_SUCCESS);
			}
		} else {
			return updateDataOfTask(dto);
		}
		return responseMessage;
	}
	
	public ResponseMessage updateDataOfTask(CustomTaskDto dto) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		String response = MurphyConstant.FAILURE;
		String assignedGrp = "";
		try {
			String taskId = dto.getTaskEventDto().getTaskId();
			System.err.println("Update Task: " + dto);
			
			//Uplift Factor - SOC
			String upliftFactor = null;
			if (!ServicesUtil.isEmpty(dto.getCustomAttr())){
				for(CustomAttrTemplateDto tempDto : dto.getCustomAttr() ){
					if (tempDto.getClItemId().equals("1234567890")) {
						upliftFactor = tempDto.getLabelValue();
						break;
					}
				}
			}
			//EOC 
			// Start-CHG0037344-Inquiry to a field seat.
			if(!ServicesUtil.isEmpty(dto.getObservationCustomAttr()))
			{
				for (int i = 0; i < dto.getObservationCustomAttr().size(); i++) {
					if (dto.getObservationCustomAttr().get(i).getLabel().equals("Assign to group")) {
						assignedGrp = dto.getObservationCustomAttr().get(i).getLabelValue();
						break;
					}
				}
			}
			//End-CHG0037344-Inquiry to a field seat.
			
			//If It is a Pigging task
			if (MurphyConstant.PIGGING.equalsIgnoreCase(dto.getTaskEventDto().getParentOrigin())){
				
				//It is pig launch task 
					if( MurphyConstant.SYSTEM.equalsIgnoreCase(dto.getTaskEventDto().getTaskType())) {

				//No updation in Pigging lauch time
				ResponseMessage res = UpdateTaskForPigging(dto.getTaskEventDto(),null);
				attrDao.setAttrValueTo(dto.getTaskEventDto().getTaskId(),dto.getCustomAttr().get(1).getLabelValue(), "1234");
			System.err.println("Update Response For Pigging: " + res);
			}
					
					//It is Pigging Retrival Task
					else{
						System.err.println("Pigging task with time :" + dto.getCustomAttr().get(2).getLabelValue());
						//Updtadate time can also be possible
					ResponseMessage res1 = UpdateTaskForPigging(dto.getTaskEventDto(),dto.getCustomAttr().get(2).getLabelValue());
					
					System.err.println("Update Response For Pigging: " + res1);
					}
			}
			// If it is ITA task and status is draft
			if((MurphyConstant.P_ITA.equalsIgnoreCase(dto.getTaskEventDto().getParentOrigin()) || MurphyConstant.P_ITA_DOP.equalsIgnoreCase(dto.getTaskEventDto().getParentOrigin()))
					&& (MurphyConstant.DRAFT.equalsIgnoreCase(dto.getTaskEventDto().getStatus()))){
				String updated = null;
					
				response = taskOwnersDao.updateOwners(dto.getTaskEventDto().getOwners(), taskId,
						dto.getTaskEventDto().getLocationCode(), dto.getTaskEventDto().getOrigin(), upliftFactor);
				if (MurphyConstant.SUCCESS.equals(response)) {
					updated = taskEventsDao.updateStatus(dto.getTaskEventDto().getTaskId(), dto.getTaskEventDto().getProcessId(), 
							dto.getTaskEventDto().getStatus());
					logger.error("Response from ITA update status :"+updated );
					
					String updateCreatedAt = taskEventsDao.updateCreatedAt(dto.getTaskEventDto().getTaskId());
					logger.error("Response from ITA updateCreatedAt :"+updateCreatedAt);
				}
			}
			
			if (MurphyConstant.DISPATCH.equals(dto.getTaskEventDto().getOrigin())) {
				response = mappingDao.updateTaskMappings(dto.getNdTaskList(), taskId);
			}
			// Follow -up start
						if(MurphyConstant.RESOLVE.equals(dto.getTaskEventDto().getStatus())){
							response = taskOwnersDao.updateOwnersStartTime(taskId,dto.getTaskEventDto().getOrigin());
							if (MurphyConstant.SUCCESS.equals(response)) {
								UpdateRequestDto updateDto = new UpdateRequestDto();
								updateDto.setTaskId(taskId);
								updateDto.setStatus(MurphyConstant.REASSIGN);
								updateDto.setUserId(dto.getTaskEventDto().getCreatedBy());
								updateDto.setUserDisplay(dto.getTaskEventDto().getCreatedBy());
								changeStatus(updateDto, dto.getTaskEventDto().getDescription());
								taskOwnersDao.updateRevisionOfProcess(taskId);
							}
						} 
			// Follow -up end
			if (MurphyConstant.ASSIGN.equals(dto.getTaskEventDto().getStatus())
					|| MurphyConstant.INPROGRESS.equals(dto.getTaskEventDto().getStatus())
					|| MurphyConstant.FORWARD.equals(dto.getTaskEventDto().getStatus())
					|| MurphyConstant.RETURN.equals(dto.getTaskEventDto().getStatus())) {

				response = taskOwnersDao.updateOwners(dto.getTaskEventDto().getOwners(), taskId,
						dto.getTaskEventDto().getLocationCode(), dto.getTaskEventDto().getOrigin(), upliftFactor);
				if (MurphyConstant.SUCCESS.equals(response)) {
					UpdateRequestDto updateDto = new UpdateRequestDto();

					updateDto.setTaskId(taskId);
					if ((dto.getTaskEventDto().getGroup().equals(dto.getLoggedInUserGrp())
							&& MurphyConstant.ASSIGN.equals(dto.getTaskEventDto().getStatus()))
							|| MurphyConstant.RETURN.equals(dto.getTaskEventDto().getStatus())) {
						updateDto.setStatus(MurphyConstant.REASSIGN);
						updateDto.setUserId(dto.getTaskEventDto().getCreatedBy());
						updateDto.setUserDisplay(dto.getTaskEventDto().getCreatedBy());
						changeStatus(updateDto, dto.getTaskEventDto().getDescription());
					} else if (MurphyConstant.FORWARD.equals(dto.getTaskEventDto().getStatus())) {
						auditDao.createInstance(dto.getTaskEventDto().getStatus(),
								dto.getTaskEventDto().getForwardedBy(), dto.getTaskEventDto().getTaskId());
						updateDto.setStatus(MurphyConstant.ASSIGN);
						updateDto.setUserId(dto.getTaskEventDto().getOwners().get(0).getTaskOwner());
						updateDto.setUserDisplay(dto.getTaskEventDto().getForwardedBy());
						changeStatus(updateDto, dto.getTaskEventDto().getDescription());

					}
				}
			}
			if (dto.getTaskEventDto().getOrigin().equals(MurphyConstant.INQUIRY)
					&& !(ServicesUtil.isEmpty(dto.getForwardToGrp()))) {
				response = attrDao.setAttrValueTo(taskId, dto.getForwardToGrp(), "INQ02");
			}
			//Start-CHG0037344-Inquiry to a field seat.
			if (!ServicesUtil.isEmpty(dto.getTaskEventDto().getIsGroupTask())
					&& dto.getTaskEventDto().getOrigin().equals(MurphyConstant.INQUIRY)
					&& dto.getTaskEventDto().getParentOrigin().equals(MurphyConstant.INQUIRY)
					&& dto.getTaskEventDto().getStatus().equals(MurphyConstant.ASSIGN)){
				if(dto.getTaskEventDto().getIsGroupTask().equalsIgnoreCase("true")){
					dto.getTaskEventDto().setCurrentProcessor(assignedGrp);
					taskEventsDao.setCurProcValue(dto.getTaskEventDto().getTaskId(),dto.getTaskEventDto().getCurrentProcessor());
				}
				else if(dto.getTaskEventDto().getIsGroupTask().equalsIgnoreCase("false"))
					taskEventsDao.setCurProcValue(dto.getTaskEventDto().getTaskId(),null);
			}
			//End-CHG0037344-Inquiry to a field seat.
			
			// if (MurphyConstant.SUCCESS.equals(response)) {
			String origin = dto.getTaskEventDto().getOrigin();
			if (!ServicesUtil.isEmpty(origin)) {
				if (MurphyConstant.DISPATCH_ORIGIN.equals(origin))
					responseMessage.setMessage(MurphyConstant.TASK + MurphyConstant.UPDATE_SUCCESS);
				else if (MurphyConstant.INVESTIGATON.equals(origin)) {
					responseMessage.setMessage(MurphyConstant.INVESTIGATON + " " + MurphyConstant.UPDATE_SUCCESS);
				} else if (MurphyConstant.INQUIRY.equals(origin)) {
					if (MurphyConstant.FORWARD.equals(dto.getTaskEventDto().getStatus())) {
						responseMessage.setMessage(MurphyConstant.INQUIRY + " " + MurphyConstant.FORWARD_SUCCESS);
					} else
						responseMessage.setMessage(MurphyConstant.INQUIRY + " " + MurphyConstant.UPDATE_SUCCESS);
				}
			} else {
				responseMessage.setMessage(MurphyConstant.UPDATE_SUCCESS);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementCanadaFacade][updateTask][error]" + e.getMessage());
		}

		return responseMessage;
	}
	
	private ResponseMessage UpdateTaskForPigging(TaskEventsDto taskEventsDto, String updatedTime) {
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			taskEventsDao.updateTaskForPigging(taskEventsDto);
			if (!ServicesUtil.isEmpty(updatedTime))
				responseMessage.setStatus(attrDao.setAttrValueTo(taskEventsDto.getTaskId(), updatedTime, "PIG001"));

			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (Exception ex) {
			responseMessage.setMessage("Failed To update");
			responseMessage.setStatus(MurphyConstant.FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return responseMessage;
	}
	
	public ResponseMessage changeStatus(UpdateRequestDto dto, String description) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
				String taskId = dto.getTaskId();
				TaskEventsDto taskEvent = taskEventsDao.getTaskDetails(taskId, null);
				if (!ServicesUtil.isEmpty(taskEvent) && MurphyConstant.PIGGING.equalsIgnoreCase(taskEvent.getParentOrigin())
						&& MurphyConstant.HUMAN.equalsIgnoreCase(taskEvent.getTaskType())
						&& MurphyConstant.COMPLETE.equalsIgnoreCase(dto.getStatus())) {
				    //HCI call for Pig Launch completion
					String workOrderNo = taskEvent.getPrevTask();
					PiggingSchedulerDto pigdto = new PiggingSchedulerDto();
					pigdto.setTaskID(taskId);
					pigdto.setWorkOrderNo(workOrderNo);
					piggingSchedulerFacade.updateWorkOrder(pigdto,MurphyConstant.PIGGING_RECEIVE);
				}

				Date userUpdatedAt = null;
				if (!ServicesUtil.isEmpty(dto.getUserUpdatedAt())) {
					//Epoch to DBFormat
					userUpdatedAt = new Date(Long.parseLong(dto.getUserUpdatedAt()));
				}
				Boolean schedulerRun = false;
				String response = taskEventsDao.updateTaskEventStatus(dto.getTaskId(), dto.getProcessId(), dto.getUserId(),
						dto.getUserDisplay(), dto.getStatus(), userUpdatedAt, description, schedulerRun,dto.getIsGroupTask());
				if (MurphyConstant.SUCCESS.equals(response)) {
					String status = dto.getStatus();
					if ((status.equals(MurphyConstant.RETURN) || status.equals(MurphyConstant.RESOLVE))
							&& !ServicesUtil.isEmpty(dto.getRootCauseList())) {
						Date d = new Date();
						for (RootCauseInstDto rootCauseDto : dto.getRootCauseList()) {
							rootCauseDto.setCreatedAt(d);
							rootCauseDto.setAction(status);
							rootCauseDto.setTaskId(dto.getTaskId());
							rootCauseDao.createRootCause(rootCauseDto);
						}
					}

					// Complete OBX task whenever it is resolved
					if (taskEvent.getParentOrigin().equals(MurphyConstant.OBX) && status.equals(MurphyConstant.RESOLVE)) {
						completeProcess(taskEvent.getProcessId(), dto.getUserId(), taskEvent.getOrigin());
					}
					
					// update user group according to location
					if(taskEvent.getParentOrigin().equals(MurphyConstant.OBX) && status.equals(MurphyConstant.RETURN)){
						String group = processEventsDao.updateUserGrp(dto.getTaskId());
						logger.error("[returnAllStatus][group updated] " + group);
					}
					
					auditDao.createInstance(dto.getStatus(), dto.getUserId(), dto.getTaskId());

					if (status.equals(MurphyConstant.COMPLETE)) {
						status = "Closed ";
					} else {
						status = status.toLowerCase() + " ";
					}

					responseMessage.setMessage(MurphyConstant.TASK + status + MurphyConstant.SUCCESSFULLY);
					responseMessage.setStatus(MurphyConstant.SUCCESS);
					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				}
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementCanadaFacade][changeStatus][error]" + e.getMessage());
		}

		return responseMessage;
	}
	
	public ResponseMessage completeProcess(String processId, String userId, String origin) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			ProcessEventsDo entity = new ProcessEventsDo();
			entity.setProcessId(processId);
			entity.setStatus(MurphyConstant.COMPLETE);
			taskEventsDao.updateTaskStatusToComplete(processId);
			processEventsDao.updateProcessStatusToComp(entity);
			if(origin.equalsIgnoreCase(MurphyConstant.INQUIRY)){
				TaskEventsDto evntsDto=taskEventsDao.getTaskDetailsForProcessId(processId);
				taskEventsDao.triggerMailNotification(MurphyConstant.COMPLETE, processId, evntsDto.getTaskId(), evntsDto.getParentOrigin(), evntsDto.getOrigin());
			}
			String muwi = processEventsDao.getMuwiByProcessId(processId);

			if (!ServicesUtil.isEmpty(muwi)) {
				alsInvestigationDao.createMethod(muwi, new Date(), MurphyConstant.JOB_COMP, MurphyConstant.SOURCE_IOP);
				alsInvestigationDao.removeRecord(muwi);
			}
			auditDao.createInstance(MurphyConstant.COMPLETE, userId, processId);
			if (ServicesUtil.isEmpty(origin)) {
				responseMessage.setMessage(
						MurphyConstant.INVESTIGATON + " " + MurphyConstant.CLOSED + MurphyConstant.SUCCESSFULLY);
			} else {
				responseMessage.setMessage(origin + " " + MurphyConstant.CLOSED + MurphyConstant.SUCCESSFULLY);
			}
			if (!ServicesUtil.isEmpty(origin) && MurphyConstant.INVESTIGATON.equals(origin)) {
				String locationCode = processEventsDao.getLocCodeByProcessId(processId);
				System.err.println("[Murphy][inside origin]" + origin + "[processId]" + processId + "[muwi]" + muwi
						+ "[locationCode]" + locationCode + "[!pwHopperDao.isProactiveCandidate(locationCode)]"
						+ pwHopperDao.isProactiveCandidate(locationCode));
				// If isProactive = true and the investigation is closed, then make isProActive as False
				// Changes for the Incident-INC0078343
				if (pwHopperDao.isProactiveCandidate(locationCode)) {
					pwHopperDao.updateProactive(locationCode, false, "", "", true);
				}
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementCanadaFacade][getNDVTaskList][error]" + e.getMessage());
		}
		return responseMessage;
	}


}
