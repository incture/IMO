package com.murphy.taskmgmt.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.dto.EnersightProveMonthlyDto;
import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.interfaces.EnersightProveDailyLocal;
import com.murphy.integration.interfaces.EnersightProveMonthlyLocal;
import com.murphy.integration.service.EnersightProveDaily;
import com.murphy.integration.service.EnersightProveMonthly;
import com.murphy.taskmgmt.dao.AlsInvestigationDao;
import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dao.CollaborationDao;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.CustomAttrInstancesDao;
import com.murphy.taskmgmt.dao.CustomAttrTemplateDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.NDTaskMappingDao;
import com.murphy.taskmgmt.dao.PWHopperStagingDao;
import com.murphy.taskmgmt.dao.ProcessEventsDao;
import com.murphy.taskmgmt.dao.RootCauseInstDao;
import com.murphy.taskmgmt.dao.ShiftRegisterDao;
import com.murphy.taskmgmt.dao.StopTimeDao;
import com.murphy.taskmgmt.dao.TaskEventsDao;
import com.murphy.taskmgmt.dao.TaskOwnersDao;
import com.murphy.taskmgmt.dao.UserIDPMappingDao;
import com.murphy.taskmgmt.dto.AttrTempResponseDto;
import com.murphy.taskmgmt.dto.CheckForCreateTaskDto;
import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomAttrValuesDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.DeviceListReponseDto;
import com.murphy.taskmgmt.dto.FLSOPResponseDto;
import com.murphy.taskmgmt.dto.FieldAvailabilityResponseDto;
import com.murphy.taskmgmt.dto.FieldResponseDto;
import com.murphy.taskmgmt.dto.IopTaskListDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.NDVTaskListDto;
import com.murphy.taskmgmt.dto.NDVTaskListResponseDto;
import com.murphy.taskmgmt.dto.PiggingSchedulerDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RootCauseInstDto;
import com.murphy.taskmgmt.dto.StopTimeDto;
import com.murphy.taskmgmt.dto.TaskDetailResponseDto;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskListDto;
import com.murphy.taskmgmt.dto.TaskListResponseDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.dto.UserTaskCountList;
import com.murphy.taskmgmt.entity.ProcessEventsDo;
import com.murphy.taskmgmt.service.interfaces.MessageFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.TaskManagementFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.TaskManagementInterface;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.taskmgmt.util.TaskEnumConstants;

@Service("taskManagementFacade")
public class TaskManagementFacade implements TaskManagementFacadeLocal, TaskManagementInterface {

	private static final Logger logger = LoggerFactory.getLogger(TaskManagementFacade.class);

	public TaskManagementFacade() {
	}

	// @Autowired
	// private CollaborationFacade collabFacade;

	// @Autowired
	// private PiggingScheldulerDao PiggingSchedulerDao;

	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private TaskOwnersDao taskOwnersDao;
	
	@Autowired
	private StopTimeDao stopTimeDao;
	
	@Autowired
	private ShiftRegisterDao shiftRegisterDao;

	@Autowired
	private ProcessEventsDao processEventsDao;

	// @Autowired
	// private PiggingScheldulerDao piggingScheldulerDao;

	@Autowired
	private PiggingSchedulerFacade piggingSchedulerFacade;

	@Autowired
	private CollaborationDao collaborationDao;

	// @Autowired
	// private NonDispatchTaskDao nonDispatchDao;

	@Autowired
	private AuditDao auditDao;

	@Autowired
	private NDTaskMappingDao mappingDao;

	@Autowired
	private CustomAttrInstancesDao attrDao;

	@Autowired
	private CustomAttrTemplateDao attrTempDao;

	@Autowired
	private HierarchyDao locDao;

	@Autowired
	private RootCauseInstDao rootCauseDao;

	@Autowired
	private AlsInvestigationDao alsInvestigationDao;

	@Autowired
	private ConfigDao configDao;

	@Autowired
	private PWHopperStagingDao pwHopperDao;

	@Autowired
	private UserIDPMappingDao userDao;
	
	@Autowired
	private MessageFacadeLocal messageFacade; 

	@Override
	public CustomTaskDto getTaskDetails(String taskId, String userType, String loggedInUser, String loggedInUserGrp,
			String hopperuserType) {
		CustomTaskDto taskDetail = new CustomTaskDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			if (ServicesUtil.isEmpty(userType))
				userType = MurphyConstant.USER_TYPE_ROC;
			TaskEventsDto dto = taskEventsDao.getTaskDetails(taskId, userType);
			Boolean potAssignedUser = false;

			if (!ServicesUtil.isEmpty(dto)) {
				//CHG0037344-Inquiry to a field seat.
				if (dto.getParentOrigin().equals(MurphyConstant.INQUIRY) && dto.getOrigin().equals(MurphyConstant.INQUIRY)
						&& dto.getStatus().equals(MurphyConstant.INPROGRESS)
						&& (!ServicesUtil.isEmpty(dto.getCurrentProcessor())
								|| !ServicesUtil.isEmpty(dto.getCurrentProcessorDisplayName()))
						&& loggedInUser.equals(dto.getCurrentProcessor()))
				{
					dto.setEditable(true);;
				}
				//CHG0037344-Inquiry to a field seat. 
				for (TaskOwnersDto taskOwnersDto : dto.getOwners()) {
					if (taskOwnersDto.getTaskOwner().equals(loggedInUser)) {
						potAssignedUser = true;
						break;
					}
				}
				taskDetail.setTaskEventDto(dto);
				taskDetail.setCollabrationDtos(collaborationDao.getCollaborationsById(dto.getProcessId(), userType));

				if (dto.getOrigin().equals(MurphyConstant.INVESTIGATON)) {

					taskDetail.setVarianceCustomAttr(attrDao.getCustomAttrIntancesbyId(dto.getProcessId(),
							dto.getStatus(), MurphyConstant.TEMP_ID_VARIANCE, userType, potAssignedUser, null));
					if (MurphyConstant.RESOLVE.equals(dto.getStatus())) {
						taskDetail.setObservationCustomAttr(attrTempDao.getCustomHeadersbyTaskTempId(
								MurphyConstant.TEMP_ID_OBSERVATION, "", "", MurphyConstant.READ,
								dto.getTaskId() + "','" + dto.getProcessId(), "", "", userType));
					} else {
						taskDetail.setObservationCustomAttr(attrDao.getCustomAttrIntancesbyId(
								dto.getTaskId() + "','" + dto.getProcessId(), dto.getStatus(),
								MurphyConstant.TEMP_ID_OBSERVATION, userType, potAssignedUser, null));
					}
					// if(!ServicesUtil.isEmpty(hopperuserType)){
					taskDetail.setCheckList(
							pwHopperDao.getCheckList(userType, dto.getLocationCode(), dto.getProcessId(),false));
					if (!ServicesUtil.isEmpty(taskDetail.getCheckList())) {
						taskDetail.setProactive(pwHopperDao.isProactiveCandidate(dto.getLocationCode()));
						taskDetail.setUserType(hopperuserType);
						taskDetail.setHopperId(pwHopperDao.getHopperId(dto.getLocationCode()));
					}
					// }

					taskDetail.setTaskSubmittedList(attrDao.getAllResolvedTask(dto.getProcessId()));
					taskDetail.setMuwiId(locDao.getMuwiByTaskId(dto.getTaskId(), dto.getOrigin()));

				} else if (dto.getOrigin().equals(MurphyConstant.INQUIRY)) {
					dto.setCreatedBy(taskEventsDao.getTaskCreator(dto.getProcessId()));
					if (dto.getGroup().equals(loggedInUserGrp)) {
						if (isOwnerAlsoAssignee(loggedInUser, dto.getOwners())) {
							taskDetail.setObservationCustomAttr(attrDao.getCustomAttrIntancesforInq(
									dto.getTaskId() + "','" + dto.getProcessId(), MurphyConstant.RESOLVE,
									MurphyConstant.TEMP_ID_INQUIRY_OBS, userType, true,loggedInUser));
						} else {
							taskDetail.setObservationCustomAttr(
									attrDao.getCustomAttrIntancesforInq(dto.getTaskId() + "','" + dto.getProcessId(),
											dto.getStatus(), MurphyConstant.TEMP_ID_INQUIRY_OBS, userType, true,loggedInUser));
						}
					} else {
						taskDetail.setObservationCustomAttr(
								attrDao.getCustomAttrIntancesforInq(dto.getTaskId() + "','" + dto.getProcessId(),
										dto.getStatus(), MurphyConstant.TEMP_ID_INQUIRY_OBS, userType, false,loggedInUser));
					}
					if (!ServicesUtil.isEmpty(taskDetail.getTaskEventDto().getLocationCode())) {
						taskDetail.getTaskEventDto().setLocationText(
								locDao.getLocationByLocCode(taskDetail.getTaskEventDto().getLocationCode()));
						taskDetail.getTaskEventDto().setLocationType(
								locDao.getLocationTypeByLocCode(taskDetail.getTaskEventDto().getLocationCode()));
					}
					//Adding for incident INC0077951
					String processIds = "";
					String process = "";
					//Add collaboration if any prev task exist
					if(!ServicesUtil.isEmpty(dto.getProcessId())){
						processIds = taskEventsDao.processIdHavingPrevTask(dto.getProcessId());
						if(!ServicesUtil.isEmpty(processIds)){
							process += processIds +"','"+dto.getProcessId() ;
						}else{
							process = dto.getProcessId();
						}
						taskDetail.setCollabrationDtos(collaborationDao.getCollaborationsById(process, userType));
					}
					String taskIdsForProcess = taskEventsDao.getTasksForProcess(process); //parameter as process
					taskDetail.setTaskSubmittedList(attrDao.getInquiryActivityLog(taskIdsForProcess, process));
					taskDetail.setMuwiId(locDao.getMuwiByTaskId(dto.getTaskId(), dto.getOrigin()));
					responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				} else {

					// Dispatch task
					taskDetail.setNdTaskList(mappingDao.getNDTasksByTaskId(taskId));
					
					// If it is pigging task
					if ("Pigging".equalsIgnoreCase(dto.getParentOrigin())) {
						// If it is pig launch task
						if ("SYSTEM".equalsIgnoreCase(dto.getTaskType())) {

							// Pigging time will be calculated
							if (!ServicesUtil.isEmpty(dto.getUpdatedAt()))
								taskDetail.setCustomAttr(attrDao.getCustomAttrIntancesbyId(taskId, dto.getStatus(),
										"PROC_PIG", userType, potAssignedUser, dto.getUpdatedAt()));
							else
								taskDetail.setCustomAttr(attrDao.getCustomAttrIntancesbyId(taskId, dto.getStatus(),
										"PROC_PIG", userType, potAssignedUser, dto.getCreatedAt()));
							if ("SYSTEM USER".equalsIgnoreCase(dto.getCreatedByDisplay()))
								dto.setOwners(new ArrayList<TaskOwnersDto>());
						}
						// It is pig retrival Task
						else
							taskDetail.setCustomAttr(attrDao.getCustomAttrIntancesbyId(taskId, dto.getStatus(), null,
									userType, potAssignedUser, null));
					}
					else if((MurphyConstant.P_ITA.equalsIgnoreCase(dto.getParentOrigin()) || MurphyConstant.P_ITA_DOP.equalsIgnoreCase(dto.getParentOrigin())) &&
							(MurphyConstant.DRAFT.equalsIgnoreCase(dto.getStatus())))
					{
						// If it is ITA task in Draft status
						List<CustomAttrTemplateDto> cstmAtr;
						List<CustomAttrTemplateDto> cstmAtr1;
						int index = 0;
						cstmAtr = attrDao.getCustomAttrIntancesbyId(taskId, dto.getStatus(), null,
								userType, potAssignedUser, null);
						
						cstmAtr1 = cstmAtr;
						for(CustomAttrTemplateDto customAttrTemplateDto : cstmAtr1)
						{
							if(customAttrTemplateDto.getClItemId().equalsIgnoreCase("1234")){
								index = cstmAtr1.indexOf(customAttrTemplateDto);
								cstmAtr.get(index).setIsEditable(true);
								cstmAtr.get(index).setLabelValue(null);
							}
						}
						taskDetail.setCustomAttr(cstmAtr);
					}
					else
					taskDetail.setCustomAttr(attrDao.getCustomAttrIntancesbyId(taskId, dto.getStatus(), null, userType,
							potAssignedUser, null));

					
					if (dto.getStatus().equals(MurphyConstant.RESOLVE)) {
						taskDetail.setMuwiId(locDao.getMuwiByTaskId(dto.getTaskId(), dto.getOrigin()));
					}
					if (dto.getStatus().equals(MurphyConstant.RESOLVE)
							|| dto.getStatus().equals(MurphyConstant.RETURN)) {
						taskDetail.setRootCauseList(rootCauseDao.getRootInstById(taskId, dto.getStatus()));
					}
					responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				}
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RECORD);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][getTaskDetails][error]" + e.getMessage());
		}
		taskDetail.setResponseMessage(responseMessage);
		return taskDetail;
	}

	@Override
	public AttrTempResponseDto getCustomHeaders(String taskTempId, String location, String locationType,
			String classification, String locationCode, String compressorName, String userType, int duration) {

		AttrTempResponseDto responseDto = new AttrTempResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		List<CustomAttrTemplateDto> varianceCustomAttr = null, observationCustomAttr = null, attrList = null;
		List<CustomAttrTemplateDto> testAttr = null;
		// List<InvestigationHistoryDto> investigationList = new
		// ArrayList<InvestigationHistoryDto>();
		try {

			if (!ServicesUtil.isEmpty(locationCode)) {
				if (ServicesUtil.isEmpty(location)) {
					location = locDao.getLocationByLocCode(locationCode);
				}
			}

			if (!ServicesUtil.isEmpty(taskTempId) && taskTempId.equals("123")) {

				/*
				 * if (!ServicesUtil.isEmpty(locationCode)) {
				 * if(MurphyConstant.COMPRESSOR.equals(locationType)){
				 * compressorName = location; LocationHierarchyDto locDto =
				 * locDao.getParentOfCompressor(locationCode).getResponseDto();
				 * location = locDto.getLocationText(); locationCode =
				 * locDto.getLocation(); locationType =
				 * locDto.getLocationType(); } else
				 * if(ServicesUtil.isEmpty(location)){ location =
				 * locDao.getLocationByLocCode(locationCode); } // locType =
				 * MurphyConstant.WELL; }
				 */
				attrList = attrTempDao.getCustomHeadersbyTaskTempId(taskTempId, location, classification, null, null,
						locationCode, compressorName, userType);
				responseDto.setLocHierarchy(locDao.getHierarchy(location, locationType));
			} else if (!ServicesUtil.isEmpty(taskTempId) && taskTempId.equals(MurphyConstant.TEMP_ID_INQUIRY)) {

				observationCustomAttr = attrTempDao.getCustomHeadersbyTaskTempId(MurphyConstant.TEMP_ID_INQUIRY_OBS,
						location, classification, null, null, locationCode, compressorName, userType);
				if (!ServicesUtil.isEmpty(observationCustomAttr)) {
					/* set AssignToGroup Based on userType */
					CustomAttrTemplateDto AssignToGroup = null;
					for (int i = 0; i < observationCustomAttr.size(); i++) {
						AssignToGroup = observationCustomAttr.get(i);
						if (AssignToGroup.getLabel().equals("Assign to group")) {
							break;
						}
					}
					if (!ServicesUtil.isEmpty(userType)) {
						// Start-CHG0037344-Inquiry to a field seat.
						logger.error("user type : " + userType);
						CustomAttrTemplateDto AssignToPerson = null;
						for (int i = 0; i < observationCustomAttr.size(); i++) {
							AssignToPerson = observationCustomAttr.get(i);
							if (AssignToPerson.getLabel().equals("Assign to person(s)")) {
								AssignToPerson.setIsMandatory(false);
								break;
							}
						}
						// Get assign to group values based on location's field
						String field_code = null; List<String> groupList = null;
						FieldResponseDto fieldResponseDto = locDao.getFeild(locationCode, locationType, true);
						
						if(!ServicesUtil.isEmpty(fieldResponseDto.getField()))
							field_code = locDao.getLocationCodeByLocText(fieldResponseDto.getField());
						
						if(!ServicesUtil.isEmpty(field_code))
							groupList = attrTempDao.getGroupRoles(field_code,userType);
							
						// End-CHG0037344-Inquiry to a field seat.
						
//						if(!ServicesUtil.isEmpty(userTypeInq)){
//							String assignTovalues = configDao.getConfigurationByRef(userTypeInq);
//							groupList = Arrays.asList(assignTovalues.split(","));
//						}
						List<CustomAttrValuesDto> valueDtos = new ArrayList<CustomAttrValuesDto>();
						for (String group : groupList) {
							if (group != null && group.length() != 0 && group != null) {
								CustomAttrValuesDto groupValueDto = new CustomAttrValuesDto();
								groupValueDto.setValue(group);
								valueDtos.add(groupValueDto);
							}
						}
						AssignToGroup.setValueDtos(valueDtos);
					}
				}
				// responseDto.setLocHierarchy(locDao.getHierarchy(location,
				// locationType));
			} else if (!ServicesUtil.isEmpty(taskTempId) && taskTempId.equals(MurphyConstant.TEMP_ID_VAR_OBS)) {

				EnersightProveMonthlyDto varianceDto = getProveData(locationCode, locationType.toUpperCase(), duration);
				testAttr = attrTempDao.getCustomHeadersbyTaskTempId(MurphyConstant.TEMP_ID_VARIANCE, "", "", null, null,
						locationCode, "", userType);
				CustomAttrTemplateDto customAttrTemplateDto = null;
				int i = 0;
				varianceCustomAttr = new ArrayList<CustomAttrTemplateDto>();
				for (CustomAttrTemplateDto varAttrTemplateDto : testAttr) {
					if (!ServicesUtil.isEmpty(varianceDto)) {

						// JSONObject jsonObj = (JSONObject)
						// json.get("enersightProveMonthlyDto");
						if (i == 0)
							varAttrTemplateDto
									.setLabelValue(Double.toString((Double) varianceDto.getAvgVarToForecastBoed()));
						if (i == 1)
							varAttrTemplateDto
									.setLabelValue(Double.toString((Double) varianceDto.getAvgPerVarToForecastBoed()));
						if (i == 2)
							varAttrTemplateDto.setLabelValue(Double.toString((Double) varianceDto.getAvgActualBoed()));
						if (i == 3)
							varAttrTemplateDto
									.setLabelValue(Double.toString((Double) varianceDto.getAvgForecastBoed()));
						if (i == 4)
							varAttrTemplateDto.setLabelValue(Double.toString((Double) varianceDto.getAvgOil()));
						if (i == 5)
							varAttrTemplateDto.setLabelValue(Double.toString((Double) varianceDto.getAvgWater()));
						if (i == 6)
							varAttrTemplateDto.setLabelValue(Double.toString((Double) varianceDto.getAvgGas()));
						if (i == 7)
							varAttrTemplateDto.setLabelValue(varianceDto.getLastProdDate());
						// varAttrTemplateDto.setLabelValue(ServicesUtil.convertFromZoneToZoneString(null,varianceDto.getLastProdDate(),"",
						// "", "", "yyyy-MM-dd"));

					}
					i++;
					customAttrTemplateDto = varAttrTemplateDto;
					varianceCustomAttr.add(customAttrTemplateDto);
				}

				observationCustomAttr = attrTempDao.getCustomHeadersbyTaskTempId(MurphyConstant.TEMP_ID_OBSERVATION,
						location, "", null, null, locationCode, "", userType);
				if (ServicesUtil.isEmpty(userType)) {
					responseDto.setCheckList(pwHopperDao.getCheckList(MurphyConstant.USER_TYPE_POT, locationCode, "",false));
				} else {
					responseDto.setCheckList(pwHopperDao.getCheckList(userType, locationCode, "",false));
				}

				responseDto.setProactive(false);
			}

			if (ServicesUtil.isEmpty(attrList)
					&& (ServicesUtil.isEmpty(varianceCustomAttr) && ServicesUtil.isEmpty(observationCustomAttr))) {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			} else {
				responseDto.setCustomAttr(attrList);
				responseDto.setVarianceCustomAttr(varianceCustomAttr);
				responseDto.setObservationCustomAttr(observationCustomAttr);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][getCustomHeaders][error]" + e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

	@Override
	public TaskListResponseDto getTaskList(String userId, String userType, String group, String taskType,
			String origin, String locationCode, Boolean isCreatedByMe,String device, String tier, 
			Boolean isForToday, String classification, String subClassification,String country) {
		TaskListResponseDto responseDto = new TaskListResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			//List<TaskListDto> taskList = taskEventsDao.getAllTasks(userId, userType, group, taskType, origin, locationCode, isCreatedByMe);
			responseDto = taskEventsDao.getAllTasks(userId, userType, group, taskType, origin, locationCode, isCreatedByMe,device,
					tier,isForToday,classification,subClassification,country);
			List<TaskListDto> taskList = responseDto.getTaskList();
			List<TaskListDto> taskList1 = null;
			
			if (taskList != null) {
				taskList1 = new LinkedList<>();
				for (Iterator<TaskListDto> iterator = taskList.iterator(); iterator.hasNext();) {
					TaskListDto dto = iterator.next();
					if (MurphyConstant.PIGGING.equalsIgnoreCase(dto.getParentOrigin())) {
						taskList1.add(dto);
						iterator.remove();
					}
					
				}
				taskList1.addAll(taskList);
			}
			

			if (ServicesUtil.isEmpty(taskList1)) {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			} else {
				responseDto.setTaskList(taskList1);

				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][getTaskList][error]" + e.getMessage());
		}

		responseDto.setResponseMessage(responseMessage);
		return responseDto;
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
//					userUpdatedAt = ServicesUtil.convertFromZoneToZone(null, dto.getUserUpdatedAt(),
//							MurphyConstant.CST_ZONE, MurphyConstant.UTC_ZONE, MurphyConstant.DATE_IOS_FORMAT,
//							MurphyConstant.DATE_DB_FORMATE);
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
					
					if(taskEvent.getParentOrigin().equals("Message") && (dto.getStatus().equals(MurphyConstant.RESOLVE)||dto.getStatus().equals(MurphyConstant.COMPLETE))){
						messageFacade.resolveMessage(Long.parseLong(taskEvent.getPrevTask()), dto.getStatus(), taskEvent);
						logger.error("Resolving Message and replying to Teams: "+Long.parseLong(taskEvent.getPrevTask()));
					}
					else if(taskEvent.getParentOrigin().equals("Message") && dto.getStatus().equals(MurphyConstant.INPROGRESS)){
						messageFacade.setMessageInProgress(Long.parseLong(taskEvent.getPrevTask()));
					}

					responseMessage.setMessage(MurphyConstant.TASK + status + MurphyConstant.SUCCESSFULLY);
					responseMessage.setStatus(MurphyConstant.SUCCESS);
					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				}
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][changeStatus][error]" + e.getMessage());
		}

		return responseMessage;
	}

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
		String assignedGrp="";
		try {
			String taskId = dto.getTaskEventDto().getTaskId();
			System.err.println("Update Task: " + dto);
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
						dto.getTaskEventDto().getLocationCode(), dto.getTaskEventDto().getOrigin());
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
						dto.getTaskEventDto().getLocationCode(), dto.getTaskEventDto().getOrigin());
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
					// if(!ServicesUtil.isEmpty( dto.getCheckList())){
					// if(pwHopperDao.checkIfInvestigationDataExists(dto.getTaskEventDto().getProcessId())){
					// pwHopperDao.updateProactive(dto.getTaskEventDto().getLocationCode(),
					// dto.isProactive(), dto.getTaskEventDto().getProcessId(),
					// dto.getHopperId(), true);
					// pwHopperDao.updateInvstInstance(dto.getTaskEventDto().getProcessId(),
					// dto.getUserType(), dto.getCheckList()
					// ,dto.getLoggedInUserId() );
					// }else{
					// String hopperId = ServicesUtil.getUUID();
					// pwHopperDao.updateProactive(dto.getTaskEventDto().getLocationCode(),
					// dto.isProactive(), dto.getTaskEventDto().getProcessId(),
					// hopperId, true);
					// pwHopperDao.createInvstInstance("",
					// dto.getTaskEventDto().getLocationCode(),
					// dto.getTaskEventDto().getProcessId(), dto.getCheckList()
					// ,hopperId ,dto.getLoggedInUserId());
					// }
					// }
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
			// }

			// }
			// }
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][updateTask][error]" + e.getMessage());
		}

		return responseMessage;
	}

	@Override
	public NDVTaskListResponseDto getNDVTaskList(String userRole, String userType, String loggedInUserType) {
		NDVTaskListResponseDto responseDto = new NDVTaskListResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			if (MurphyConstant.USER_TYPE_ENG.equals(loggedInUserType)) {
				userRole = ServicesUtil
						.getStringFromListForAls(userDao.getRolesForNDVTasks(userRole, MurphyConstant.USER_TYPE_POT));
			}
			List<NDVTaskListDto> resultList = taskEventsDao.getAllNDVTasks(userRole, userType);
			if (!ServicesUtil.isEmpty(resultList)) {
				responseDto.setTaskList(resultList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][getNDVTaskList][error]" + e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		responseDto.setAlsTaskCount(taskEventsDao.getTaskCount(MurphyConstant.USER_TYPE_ALS, userRole));
		responseDto.setEngTaskCount(taskEventsDao.getTaskCount(MurphyConstant.USER_TYPE_ENG, userRole));
		responseDto.setPotTaskCount(taskEventsDao.getTaskCount(MurphyConstant.USER_TYPE_POT, userRole));
		return responseDto;
	}

	@Override
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
				//To close the Message
				String taskId = taskEventsDao.getLatestTaskForProcess(processId);
				TaskEventsDto taskEventsDto = taskEventsDao.getTaskDetails(taskId, null);
				if(taskEventsDto.getParentOrigin().equals("Message")){
					messageFacade.resolveMessage(Long.parseLong(taskEventsDto.getPrevTask()), MurphyConstant.COMPLETE, taskEventsDto);
				}
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][getNDVTaskList][error]" + e.getMessage());
		}
		return responseMessage;
	}

	@Override
	public com.murphy.taskmgmt.dto.UIResponseDto assignedTasksToUserCount(String userId) {
		com.murphy.taskmgmt.dto.UIResponseDto uiResponseDto = new com.murphy.taskmgmt.dto.UIResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();

		try {
			uiResponseDto.setCount(taskEventsDao.assignedTasksToUserCount(userId));
			responseMessage.setMessage("Successful");
			responseMessage.setStatus("SUCCESS");
			responseMessage.setStatusCode("0");

		} catch (Exception e) {
			logger.error("[fetchProveDailyData] : ERROR- Exception while interacting with database " + e);
			responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");

		}
		uiResponseDto.setResponseMessage(responseMessage);
		return uiResponseDto;
	}

	@Override
	public UserTaskCountList assignedTasksToUsersCount(String pIdList) {
		UserTaskCountList uiResponseDto = new UserTaskCountList();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failed To Fetch Task Count");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode("1");

		try {
			List<String> pIdsList = Arrays.asList(pIdList.split("\\s*,\\s*"));
			uiResponseDto.setUserTaskCountList(taskEventsDao.assignedTasksToUsersCount(pIdsList));
			responseMessage.setMessage("Task Count Fetched Successfully");
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode("0");

		} catch (Exception e) {
			logger.error("[assignedTasksToUsersCount] : ERROR " + e);
			responseMessage.setMessage("Failed to fetch user Task Count");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");

		}
		uiResponseDto.setResponseMessage(responseMessage);
		return uiResponseDto;
	}

	public EnersightProveMonthlyDto getProveData(String locationCode, String locationType, int duration) {
		try {

			//EnersightProveMonthlyLocal proveLocal = new EnersightProveMonthly();
			EnersightProveDailyLocal proveLocal = new EnersightProveDaily();
			// String jsonString = DestinationUtil.executeWithDest("",
			// "https://approvealsd998e5467.us2.hana.ondemand.com/AppDownload/murphy/proveReport/fetchProveVarianceData?uwiId="+muwi,
			// MurphyConstant.HTTP_METHOD_GET, MurphyConstant.APPLICATION_JSON,
			// "", "","",false);
			UIRequestDto uiRequestDto = new UIRequestDto();
			if (ServicesUtil.isEmpty(duration) || duration == 0) {
				duration = 7;
			}
			uiRequestDto.setDuration(duration);
			uiRequestDto.setLocationCodeList(new ArrayList<String>());
			uiRequestDto.getLocationCodeList().add(locationCode);
			uiRequestDto.setLocationType(locationType);
			UIResponseDto response = proveLocal.fetchProveData(uiRequestDto).getUiResponseDto();
			if (!ServicesUtil.isEmpty(response.getEnersightProveMonthlyDtoList())) {
				return response.getEnersightProveMonthlyDtoList().get(0);
			}

			// System.err.println("[[getProveData]][Response] " + );
			// JSONParser parser = new JSONParser();
			// JSONObject json = null;
			// (JSONObject) parser.parse(jsonString);
			// long i = (Long) (String) json.get("totalResults");
			// System.err.println("[inside][getProveData]"+json);
			// return json;
		} catch (Exception e) {
			System.err.println("[getProveData]" + e.getMessage());
		}
		return null;
	}

	@Override
	public TaskListResponseDto getTaskHistory(String locationCode, String location) {
		TaskListResponseDto responseDto = new TaskListResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			List<TaskListDto> resultList = taskEventsDao.getLatestTasksForLoc(locationCode, location);
			if (!ServicesUtil.isEmpty(resultList)) {
				responseDto.setTaskList(resultList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][getNDVTaskList][error]" + e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

	@Override
	public FLSOPResponseDto getFLSOP(String classification, String subClassification) {
		FLSOPResponseDto responseDto = new FLSOPResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			String flsop = taskEventsDao.getFLSOPforSubClassification(classification, subClassification);
			if (!ServicesUtil.isEmpty(flsop)) {
				responseDto.setFlsop(flsop);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][getFLSOP][error]" + e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;

	}

	private boolean isOwnerAlsoAssignee(String loggedInUser, List<TaskOwnersDto> owners) {
		boolean response = false;
		for (TaskOwnersDto owner : owners) {
			if (loggedInUser.equals(owner.getTaskOwner())) {
				response = true;
				break;
			}
		}
		return response;

	}

	@SuppressWarnings("unused")
	private Date getEstimatedDate(Date now, double minuts) {
		LocalDateTime date = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
				.plusMinutes((long) Math.floor(minuts));
		if (minuts != 0)
			return java.util.Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
		else
			return null;
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
	
	//Enhancement to check single dispatch task per location
	@Override
	public CheckForCreateTaskDto checkForExistingTask(String locationCode) {
		ResponseMessage responseMessage = new ResponseMessage();
		CheckForCreateTaskDto checkForCreateTaskDto = new CheckForCreateTaskDto();
		responseMessage.setMessage("Failed To check the task");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			List<TaskListDto> taskListDto = taskEventsDao.checkForLocation(locationCode);
			if (!ServicesUtil.isEmpty(taskListDto)) {
				responseMessage.setMessage("Task already exists. Kindly close the previous task for this location!");
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				logger.error("TaskList Set DTO : " + taskListDto);
				checkForCreateTaskDto.setTaskList(taskListDto);
				checkForCreateTaskDto.setCanCreate(false);
			} else {
				responseMessage.setMessage("Create task");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				checkForCreateTaskDto.setCanCreate(true);

			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][checkForExistingTask][error]" + e.getMessage());
		}
		checkForCreateTaskDto.setResponseMessage(responseMessage);
		return checkForCreateTaskDto;
	}
	
	//Auto close of dispatch tasks for more than or equal to 14 days
	
		public ResponseMessage autoCloseStatus() {
			ResponseMessage responseMessage=null;
			try {
				responseMessage=new ResponseMessage();
				
				String response=taskEventsDao.autoChangeStatus();
				if(response==MurphyConstant.SUCCESS)
				{
				responseMessage.setMessage("auto Close task");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				}
				else
				{
					responseMessage.setMessage("auto Close task failed");
					responseMessage.setStatus(MurphyConstant.FAILURE);
					responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
				}
				
				
			} catch (Exception e) {
				logger.error("[TaskManagementFacade][autoCloseStatus][Exception] " + e.getMessage());
			}
			return responseMessage;
	
		}
		//GET ALL THE TASKS FOR IOP_ADMIN CONSOLE	
		@Override
		public TaskListResponseDto getAllTasksForAdmin(int page ,int pageSize , String taskType, String status ,String parentOrigin) {
			
			TaskListResponseDto responseDto = new TaskListResponseDto();
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setMessage(MurphyConstant.READ_FAILURE);
			responseMessage.setStatus(MurphyConstant.FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
			try {
				 responseDto = taskEventsDao.getAllTasksForAdmin(page , pageSize,taskType,status, parentOrigin );
				if (ServicesUtil.isEmpty(responseDto)) {
					responseMessage.setMessage(MurphyConstant.NO_RESULT);
				} else {
					
						responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				}

				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			} catch (Exception e) {
				logger.error("[Murphy][TaskManagementFacade][getTaskListForAdmin][error]" + e.getMessage());
			}

			responseDto.setResponseMessage(responseMessage);
			return responseDto;
		}


		
	//AUTO CLOSURE OF ALL TASKS FOR IOP_ADMIN CONSOLE
		@Override
		public ResponseMessage autoCloseTaskForAdmin(List<IopTaskListDto> taskId) {
			ResponseMessage responseMessage=null;
			try {
				responseMessage=new ResponseMessage();
				
				String response=taskEventsDao.autoChangeStatusForAdmin(taskId);
				if(response==MurphyConstant.SUCCESS)
				{
				responseMessage.setMessage("auto Close task");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				}
				else
				{
					responseMessage.setMessage("auto Close task failed");
					responseMessage.setStatus(MurphyConstant.FAILURE);
					responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
				}
				
				
			} catch (Exception e) {
				logger.error("[TaskManagementFacade][autoCloseStatus][Exception] " + e.getMessage());
			}
			return responseMessage;
		}

		
		@Override
		public ResponseMessage getTaskStatus(String userEmailId, String taskId, String processId, String userGroup) {
			ResponseMessage message=new ResponseMessage();
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			message.setStatus(MurphyConstant.SUCCESS);
			try{
				TaskOwnersDto ownerDto=taskEventsDao.getTaskStatus(userEmailId, taskId, processId, userGroup);
				if(ownerDto.getTaskStatus().equalsIgnoreCase(MurphyConstant.ACTIVE)){
					TaskEventsDto taskEvntsDto=taskEventsDao.getTaskDetails(taskId,null);
					
					if(taskEvntsDto.getOrigin().equalsIgnoreCase(MurphyConstant.DISPATCH_ORIGIN)){
						message.setMessage(TaskEnumConstants.valueOf(MurphyConstant.DISPATCH_ORIGIN).getStatusCode());
					}
					else if(taskEvntsDto.getOrigin().equalsIgnoreCase(MurphyConstant.INQUIRY)){
						message.setMessage(TaskEnumConstants.valueOf(MurphyConstant.INQUIRY).getStatusCode());
					}
					else if(taskEvntsDto.getOrigin().equalsIgnoreCase(MurphyConstant.INVESTIGATON)){
						message.setMessage(TaskEnumConstants.valueOf(MurphyConstant.INVESTIGATON).getStatusCode());
					}
				}
				else if(ownerDto.getTaskStatus().equalsIgnoreCase(MurphyConstant.COMPLETE)){
					message.setMessage("Inquiry already Closed");
				}else{
					message.setMessage("User Not Authorized");
				}
			}
			catch(Exception ex){
				logger.error("[TaskManagementFacade][autoCloseStatus][Exception] " + ex.getMessage());
				message.setStatusCode(MurphyConstant.CODE_FAILURE);
				message.setStatus(MurphyConstant.FAILURE);
				message.setMessage("Failed while Checking Task Status");
				
			}
			return message;
		}
		
//		Created by Ankit Kumar on 30 July 2019
		public Map<String, Double> defaultAssignee(String classification, String subClassification) {
			
//			Find a list of users/designation which are available to work in current shift.
			//Map<String, String> userNDesignationList = shiftRegisterDao.getEmpByShift();
			
//			Find the stop-time based on competence.
			//StopTimeDto competency = stopTimeDao.getStopTimeByCategory(classification, subClassification);
			
//			Create a map of users with approximate time of completion of work and eliminating incompetent users.
			Map<String, Double> userTimeMap = null;
			
//			if (!userNDesignationList.isEmpty()) {
//				userTimeMap = new HashMap<String, Double>();
//				for (Map.Entry<String, String> entry : userNDesignationList.entrySet()) {
//					String user = entry.getKey();
//					String designation = entry.getValue();
//					Double timeTaken = new Double(0);
//					
//					if (designation.equals("PRO_A")) {
//						timeTaken = competency.getProA().doubleValue();
//					} else if (designation.equals("PRO_B")) {
//						timeTaken = competency.getProB().doubleValue();
//					} else if (designation.equals("OBX")) {
//						timeTaken = competency.getObx().doubleValue();
//					} else if (designation.equals("SSE")) {
//						timeTaken = competency.getSse().doubleValue();
//					}
//					if (Double.compare(timeTaken, 0) != 0) {
//						userTimeMap.put(user, timeTaken);
//					}
//				}
//			}

			return userTimeMap;
		}
		public FieldAvailabilityResponseDto getFieldWiseAvailability(String technicalRole) {

			FieldAvailabilityResponseDto fieldAvailabilityResponseDto = new FieldAvailabilityResponseDto();
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setStatus(MurphyConstant.FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
			try {

				fieldAvailabilityResponseDto=taskEventsDao.fieldWiseAvailability(technicalRole);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

			} catch (Exception e) {
				responseMessage.setMessage(e.getMessage());
				logger.error("[Murphy][TaskManagementFacade][fieldWiseAvailability][error]" + e.getMessage());
			}
			fieldAvailabilityResponseDto.setResponseMessage(responseMessage);
			return fieldAvailabilityResponseDto;
		}

	@Override
	public ResponseMessage returnAllStatus(List<UpdateRequestDto> dtoList, String string) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {

			logger.error("[returnAllStatus][UpdateRequestDtoList] "+dtoList.toString());
			for(UpdateRequestDto dto :  dtoList){
				logger.error("[Inside For Loop][UpdateRequestDto] "+dto.toString());
				responseMessage = changeStatus(dto,"");
			}
		} catch (Exception e) {
			responseMessage.setMessage(e.getMessage());
			logger.error("[Murphy][TaskManagementFacade][returnAllStatus][error]" + e.getMessage());
		}
		return responseMessage;
	}

	@Override
	public LocationHierarchyResponseDto getLocationMasterDetails(int page, int page_size,String locationText,String locationCode,String locationType,String muwi,String tier) {
		LocationHierarchyResponseDto locationHierarchyReponseDto = new LocationHierarchyResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			locationHierarchyReponseDto = taskEventsDao.getLocationMasterDetails(page,page_size,locationText,locationCode,locationType,muwi,tier);
			if (!ServicesUtil.isEmpty(locationHierarchyReponseDto)) {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][getLocationDetails][error]" + e.getMessage());
		}
		locationHierarchyReponseDto.setMessage(responseMessage);
		return locationHierarchyReponseDto;
	}
	
	@Override
	public TaskDetailResponseDto getNextTaskDetails(String userId){
		TaskDetailResponseDto taskDetailResponseDto = new TaskDetailResponseDto();
		TaskListDto taskListDto = new TaskListDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		
		try{
			taskListDto = taskEventsDao.getNextTaskDetails(userId);
			if (!ServicesUtil.isEmpty(taskListDto)) {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagementFacade][getNextTaskDetails][error]" + e.getMessage());
		}
		taskDetailResponseDto.setTaskListDto(taskListDto);
		taskDetailResponseDto.setResponseMessage(responseMessage);
		return taskDetailResponseDto;
	}

	@Override
	public ResponseMessage updateLocationMaster(List<LocationHierarchyDto> listLocationHierarchyDto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);		
		try{
			String response = taskEventsDao.updateLocationMaster(listLocationHierarchyDto);
			if (!ServicesUtil.isEmpty(response)) {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);			
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagementFacade][updateLocationMaster][error]" + e.getMessage());
		}
		return responseMessage;
	}
}
