package com.murphy.taskmgmt.service;

/*
 * @author Prakash Kumar
 * Version 1.0
 * Since Sprint 6
 * */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.AlsInvestigationDao;
import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.CustomAttrInstancesDao;
import com.murphy.taskmgmt.dao.CustomAttrTemplateDao;
import com.murphy.taskmgmt.dao.CygnetAlarmFeedDao;
import com.murphy.taskmgmt.dao.GeoTabDao;
import com.murphy.taskmgmt.dao.NDTaskMappingDao;
import com.murphy.taskmgmt.dao.NonDispatchTaskDao;
import com.murphy.taskmgmt.dao.PWHopperStagingDao;
import com.murphy.taskmgmt.dao.PipelineMeasurementDao;
import com.murphy.taskmgmt.dao.ProcessEventsDao;
import com.murphy.taskmgmt.dao.TaskEventsDao;
import com.murphy.taskmgmt.dao.TaskOwnersCanadaDao;
import com.murphy.taskmgmt.dto.CollaborationDto;
import com.murphy.taskmgmt.dto.CustomAttrInstancesDto;
import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.NDTaskMappingDto;
import com.murphy.taskmgmt.dto.NonDispatchTaskDto;
import com.murphy.taskmgmt.dto.PipelineMeasurementDto;
import com.murphy.taskmgmt.dto.ProcessEventsDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.StartTimeResponseDto;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingInterface;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.taskmgmt.util.UniqueSeq;
@Service("TaskSchedulingCanadaCalFacade")
public class TaskSchedulingCanadaCalFacade implements TaskSchedulingInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskSchedulingCanadaCalFacade.class);
	static SimpleDateFormat utcFormatter = new SimpleDateFormat("dd-MMM-yyyy, hh:mm:ss a");

	@Autowired
	private CollaborationFacade collabFacade;

	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private TaskOwnersCanadaDao taskOwnersDao;

	@Autowired
	private ProcessEventsDao processEventsDao;

	@Autowired
	private NonDispatchTaskDao nonDispatchDao;

	@Autowired
	private AuditDao auditDao;

	@Autowired
	private NDTaskMappingDao mappingDao;

	@Autowired
	private CustomAttrInstancesDao attrDao;

	@Autowired
	private CustomAttrTemplateDao attrTempDao;

	@Autowired
	private CygnetAlarmFeedDao alarmDao;

	@Autowired
	private AlsInvestigationDao alsInvestigationDao;

	@Autowired
	private GeoTabDao geoDao;

	@Autowired
	private PWHopperStagingDao pwHopperDao;

	@Autowired
	private ConfigDao configDao;

	// @Autowired
	// private EnersightProveMonthlyDao proveDao;

	@Autowired
	private PipelineMeasurementDao pipelineMeasurementDao;


	@Override
	public ResponseMessage createTask(CustomTaskDto dto) {


		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.TASK + MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		String processId = dto.getTaskEventDto().getProcessId(), taskOwners = "";
		String taskId = UUID.randomUUID().toString().replaceAll("-", "");
		String refNum = String.valueOf(UniqueSeq.getInstance().getNext());

		dto.getTaskEventDto().setCreatedAt(new Date());
		dto.getTaskEventDto().setUpdatedAt(new Date());
		dto.getTaskEventDto().setUpdatedBy(dto.getTaskEventDto().getCreatedByDisplay());
		dto.getTaskEventDto().setTaskRefNum(refNum);
		dto.getTaskEventDto().setNotificationFlag(true);

		try {

			if (ServicesUtil.isEmpty(processId)) {
				processId = UUID.randomUUID().toString().replaceAll("-", "");
				dto.getTaskEventDto().setProcessId(processId);
				// Adding loc string in below function for incident INC0079467
				ProcessEventsDto procDto = null;
				if (!ServicesUtil.isEmpty(dto.getLocHierarchy())) {
					if (!ServicesUtil.isEmpty(dto.getLocHierarchy().get(0).getLoc())) {
						String loc = dto.getLocHierarchy().get(0).getLoc();
						procDto = createProcForTask(dto.getTaskEventDto(), loc);
					}
				} else {
					procDto = createProcForTask(dto.getTaskEventDto(), null);
				}

				if (!ServicesUtil.isEmpty(procDto)) {
					processEventsDao.create(procDto);
				}

				if (!ServicesUtil.isEmpty(dto.getVarianceCustomAttr())) {
					for (CustomAttrTemplateDto customDto : dto.getVarianceCustomAttr()) {
						CustomAttrInstancesDto instanceDto = new CustomAttrInstancesDto();
						instanceDto.setTaskId(processId);
						instanceDto.setClId(customDto.getClItemId());
						instanceDto.setValue(customDto.getLabelValue());
						attrDao.create(instanceDto);
					}
				}
				if (!ServicesUtil.isEmpty(dto.getObservationCustomAttr())) {
					for (CustomAttrTemplateDto customDto : dto.getObservationCustomAttr()) {
						if (customDto.getTaskTempId().equals(MurphyConstant.TEMP_ID_PROCESS_OBS)
								|| customDto.getTaskTempId().equals(MurphyConstant.TEMP_ID_PROC_INQUIRY)) {
							CustomAttrInstancesDto instanceDto = new CustomAttrInstancesDto();
							instanceDto.setTaskId(processId);
							instanceDto.setValue(customDto.getLabelValue());
							instanceDto.setClId(customDto.getClItemId());
							attrDao.create(instanceDto);
						}
						/*
						 * else if
						 * (customDto.getTaskTempId().equals(MurphyConstant.
						 * TEMP_ID_INQUIRY)) { CustomAttrInstancesDto
						 * instanceDto = new CustomAttrInstancesDto();
						 * instanceDto.setTaskId(processId);
						 * instanceDto.setValue(customDto.getLabelValue());
						 * instanceDto.setClId(customDto.getClItemId());
						 * attrDao.create(instanceDto); }
						 */
					}
				}
				auditDao.createInstance(MurphyConstant.CREATED, dto.getTaskEventDto().getCreatedBy(), processId);
				if ((!ServicesUtil.isEmpty(dto.getTaskEventDto().getParentOrigin()))
						&& dto.getTaskEventDto().getParentOrigin().equals(MurphyConstant.INQUIRY)) {
					auditDao.createInstance(
							dto.getTaskEventDto().getOrigin().toUpperCase() + " " + MurphyConstant.CREATED,
							dto.getTaskEventDto().getCreatedBy(), dto.getTaskEventDto().getPrevTask());
				}
			} else {
				taskEventsDao.updateTaskStatusResolveToComplete(dto.getTaskEventDto().getProcessId());
			}

			if (!ServicesUtil.isEmpty(dto.getTaskEventDto().getOwners())) {
				float resolveTime = 0.0f;
				if (MurphyConstant.OBX_SUB_CLASSIFICATION.equals(dto.getTaskEventDto().getSubClassification())) {
					resolveTime = Float
							.parseFloat(configDao.getConfigurationByRef(MurphyConstant.DURATION_STOP_BY_WELLS));
				}
				for (TaskOwnersDto ownerDto : dto.getTaskEventDto().getOwners()) {
					ownerDto.setTaskId(taskId);
					taskOwners = taskOwners + ", " + ownerDto.getTaskOwnerDisplayName();

					if (!MurphyConstant.INVESTIGATON.equals(dto.getTaskEventDto().getOrigin())) {
						String classification = null;
						if (!ServicesUtil.isEmpty(dto.getCustomAttr())) {
							List<CustomAttrTemplateDto> cstmAtr = dto.getCustomAttr();
							for (CustomAttrTemplateDto customAttrTemplateDto : cstmAtr) {
								if (customAttrTemplateDto.getClItemId().equalsIgnoreCase("12345"))
									classification = customAttrTemplateDto.getLabelValue();
							}
							logger.error("Classification :" + classification);
							if (ServicesUtil.isEmpty(resolveTime) || resolveTime == 0) {
								resolveTime = attrTempDao.getEstTimeForSubClass(classification,
										dto.getTaskEventDto().getSubClassification(),
										dto.getTaskEventDto().getOwners().get(0).getOwnerEmail());
							}
						}
						String locationCode = dto.getTaskEventDto().getLocationCode();
						Double customTime = ownerDto.getCustomTime();
						Double totalTime = resolveTime + customTime;
						Double driveTime = 0.00;
						Date latestDate = new Date();

						StartTimeResponseDto responseDto = taskOwnersDao.getNewStartOfUser(ownerDto.getTaskOwner(),
								locationCode, resolveTime, dto.getTaskEventDto().getDriveTime());

						if (!ServicesUtil.isEmpty(responseDto)) {
							if (!ServicesUtil.isEmpty(responseDto.getStartTime()))
								latestDate = responseDto.getStartTime();
							else
								latestDate = new Date();
							if (!ServicesUtil.isEmpty(responseDto.getDriveTime()))
								driveTime = responseDto.getDriveTime();
							else
								driveTime = 1.00;
						} else
							logger.error("Response Dto is empty");

						if (dto.isFutureTask()) {
							driveTime = responseDto.getDriveTime();
							latestDate = dto.getCustomAttr().get(6).getDateValue();
							dto.getTaskEventDto().setNotificationFlag(false);
						}
						// TaskOwnersDto ownerLatestDto =
						// taskOwnersDao.getLastTaskOfUser(ownerDto.getTaskOwner());
						
						// Setting drive time to 0 for inquiry task 
						if(dto.getTaskEventDto().getOrigin().equals(MurphyConstant.INQUIRY)
								&& dto.getTaskEventDto().getParentOrigin().equals(MurphyConstant.INQUIRY)){
							logger.error("Drive time before changing to 0  : " + driveTime);
							driveTime = 0.00;
						}

						ownerDto.setTier(taskOwnersDao.getTierFromWellTier(locationCode));
						
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
						
						if (driveTime != -1) {
							if (!ServicesUtil.isEmpty(upliftFactor)) {
								driveTime = driveTime + (driveTime * Double.parseDouble(upliftFactor)/ 100.0);
								logger.error("[Murphy][TaskSchedulingCanadaCalFacade][createTask] Uplift Factor: "+ upliftFactor+" drive time with uplift factor: "+ driveTime);
							}
							totalTime = driveTime + totalTime;
						}
						//EOC
						ownerDto.setEstDriveTime(driveTime);
						ownerDto.setEstResolveTime(resolveTime);
						ownerDto.setCustomTime(customTime);
						ownerDto.setStartTime(latestDate);
						ownerDto.setEndTime(ServicesUtil.getDateWithInterval(latestDate, totalTime.intValue(),
								MurphyConstant.MINUTES));
					}

					taskOwnersDao.create(ownerDto);
				}

				if (!ServicesUtil.isEmpty(taskOwners))
					taskOwners = taskOwners.substring(1, taskOwners.length());
			}

			// Add default Comments for Pig Retrival Task
			if (!ServicesUtil.isEmpty(dto.getTaskEventDto())
					&& MurphyConstant.PIGGING.equalsIgnoreCase(dto.getTaskEventDto().getParentOrigin())
					&& MurphyConstant.PIGGING_RECEIVE.equalsIgnoreCase(dto.getTaskEventDto().getSubClassification())){
				if (!ServicesUtil.isEmpty(dto.getCustomAttr())) {
					for (CustomAttrTemplateDto customDto : dto.getCustomAttr()) {
						if (customDto.getLabel().equalsIgnoreCase("Equipment")
								&& !ServicesUtil.isEmpty(customDto.getLabelValue())) {
							PipelineMeasurementDto pipelineMeasurementDto = pipelineMeasurementDao
									.getPipeLineMeasurment(customDto.getLabelValue());
							if (!ServicesUtil.isEmpty(pipelineMeasurementDto)) {
								List<CollaborationDto> collabDtoList = new ArrayList<CollaborationDto>();
								CollaborationDto collaborationDto = new CollaborationDto();
								String piggMessage = "* Pig Size : "+pipelineMeasurementDto.getDiameter()+"\r\n"+
										"* Pig Length : "+pipelineMeasurementDto.getLength()+"\r\n"+
										"* Equipment Description : "+pipelineMeasurementDto.getLineName();
								collaborationDto.setMessage(piggMessage);
								collaborationDto.setUserDisplayName("SYSTEM USER");
								collaborationDto.setUserId("SYSTEM");
								collabDtoList.add(collaborationDto);
								dto.setCollabrationDtos(collabDtoList);
								break;
							}
						}
					}
				}
			}

			if (!ServicesUtil.isEmpty(dto.getCollabrationDtos())) {
				for (CollaborationDto collabDto : dto.getCollabrationDtos()) {
					collabDto.setProcessId(processId);
					collabDto.setTaskId(taskId);
					collabFacade.createCollaboration(collabDto, dto.getTaskEventDto().getParentOrigin());
				}
			}

			if (!ServicesUtil.isEmpty(dto.getNdTaskList())) {
				UpdateRequestDto updateDto = null;
				for (NonDispatchTaskDto ndTaskdto : dto.getNdTaskList()) {
					NDTaskMappingDto mappingDto = new NDTaskMappingDto();
					mappingDto.setTaskId(taskId);
					mappingDto.setNdTaskId(ndTaskdto.getTaskId());
					mappingDto.setStatus(MurphyConstant.INPROGRESS);
					updateDto = new UpdateRequestDto();
					updateDto.setTaskId(ndTaskdto.getTaskId());
					updateDto.setStatus(MurphyConstant.DISPATCH);
					nonDispatchDao.updateStatusOfInstance(updateDto);
					mappingDao.create(mappingDto);
				}
			}

			if (!ServicesUtil.isEmpty(dto.getCustomAttr())) {
				for (CustomAttrTemplateDto customDto : dto.getCustomAttr()) {
					CustomAttrInstancesDto instanceDto = new CustomAttrInstancesDto();
					instanceDto.setTaskId(taskId);
					if (MurphyConstant.ASSIGNEDTO.equals(customDto.getLabel())) {
						instanceDto.setValue(taskOwners);
					} else {
						if (MurphyConstant.LOCATION.equals(customDto.getLabel())) {
							dto.getTaskEventDto()
									.setSubject(customDto.getLabelValue() + " - " + dto.getTaskEventDto().getSubject());
						}
						instanceDto.setValue(customDto.getLabelValue());
					}
					instanceDto.setClId(customDto.getClItemId());
					attrDao.create(instanceDto);
				}
			}

			if (!ServicesUtil.isEmpty(dto.getObservationCustomAttr())) {
				for (CustomAttrTemplateDto customDto : dto.getObservationCustomAttr()) {
					if (customDto.getTaskTempId().equals(MurphyConstant.TEMP_ID_TASK_OBS)
							|| customDto.getTaskTempId().equals(MurphyConstant.TEMP_ID_TASK_INQUIRY)) {
						CustomAttrInstancesDto instanceDto = new CustomAttrInstancesDto();
						instanceDto.setTaskId(taskId);
						instanceDto.setValue(customDto.getLabelValue());
						if (MurphyConstant.ASSIGNEDTO.equals(customDto.getLabel())) {
							instanceDto.setValue(taskOwners);
						}
						instanceDto.setClId(customDto.getClItemId());
						attrDao.create(instanceDto);
					} else if (MurphyConstant.PRIMARY_CLASSIFICATION.equals(customDto.getLabel())
							|| MurphyConstant.SECONDARY_CLASSIFICATION.equals(customDto.getLabel())
							|| MurphyConstant.ISSUE_CLASSIFICATION_ID.equals(customDto.getClItemId())) {
						attrDao.setAttrValueTo(dto.getTaskEventDto().getProcessId(), customDto.getLabelValue(),
								customDto.getClItemId());
					}
				}
			}

			auditDao.createInstance(MurphyConstant.CREATED, dto.getTaskEventDto().getCreatedBy(), taskId);

			if (!ServicesUtil.isEmpty(dto.getPointId())) {
				alarmDao.updateAlarmFeed("'" + dto.getPointId() + "'", MurphyConstant.ALARM_DISPATCH, "Y");
			}

			dto.getTaskEventDto().setTaskId(taskId);
			if (ServicesUtil.isEmpty(dto.getTaskEventDto().getTaskType())) {
				dto.getTaskEventDto().setTaskType(MurphyConstant.HUMAN);
			}
			
			// Start-CHG0037344-Inquiry to a field seat.
			if (dto.getTaskEventDto().getOrigin().equals(MurphyConstant.INQUIRY)
					&& dto.getTaskEventDto().getParentOrigin().equals(MurphyConstant.INQUIRY)){
				if(dto.getTaskEventDto().getIsGroupTask().equalsIgnoreCase("true")){
					for (int i = 0; i < dto.getObservationCustomAttr().size(); i++) {
						if (dto.getObservationCustomAttr().get(i).getLabel().equals("Assign to group")) {
							dto.getTaskEventDto()
								.setCurrentProcessor(dto.getObservationCustomAttr().get(i).getLabelValue());
							break;
						}
					}	
				}
				else if(dto.getTaskEventDto().getIsGroupTask().equalsIgnoreCase("false")){
					dto.getTaskEventDto().setCurrentProcessor(null);
				}
		    }
		    // End-CHG0037344-Inquiry to a field seat.

			taskEventsDao.create(dto.getTaskEventDto());

			String origin = dto.getTaskEventDto().getOrigin();

			if (MurphyConstant.INVESTIGATON.equals(origin)) {
				/*
				 * alsInvestigationDao.createMethod(dto.getMuwiId(),
				 * ServicesUtil.convertFromZoneToZone(new Date(), null,
				 * MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,
				 * MurphyConstant.DATE_DB_FORMATE,
				 * MurphyConstant.DATE_DB_FORMATE),
				 * MurphyConstant.INVESTIGATION_STARTED,
				 * MurphyConstant.SOURCE_IOP);
				 * alsInvestigationDao.createRecord(dto.getMuwiId());
				 * if(!ServicesUtil.isEmpty( dto.getCheckList())){
				 * 
				 * System.err.println("");
				 * 
				 * // if(pwHopperDao.checkIfInvestigationDataExists(dto.
				 * getTaskEventDto().getProcessId())){ //
				 * pwHopperDao.updateProactive(dto.getTaskEventDto().
				 * getLocationCode(), dto.isProactive(),
				 * dto.getTaskEventDto().getProcessId(), dto.getHopperId(),
				 * true); //
				 * pwHopperDao.updateInvstInstance(dto.getTaskEventDto().
				 * getProcessId(), dto.getUserType(), dto.getCheckList() ); // }
				 * String hopperId = ServicesUtil.getUUID();
				 * pwHopperDao.updateProactive(dto.getTaskEventDto().
				 * getLocationCode(), dto.isProactive(),
				 * dto.getTaskEventDto().getProcessId(), hopperId, true);
				 * pwHopperDao.createInvstInstance("",
				 * dto.getTaskEventDto().getLocationCode(),
				 * dto.getTaskEventDto().getProcessId(), dto.getCheckList()
				 * ,hopperId,dto.getTaskEventDto().getCreatedBy(), false);
				 * 
				 * }
				 */
				alsInvestigationDao.createMethod(dto.getMuwiId(),
						ServicesUtil.convertFromZoneToZone(new Date(), null, "", "", MurphyConstant.DATE_DB_FORMATE,
								MurphyConstant.DATE_DB_FORMATE),
						MurphyConstant.INVESTIGATION_STARTED, MurphyConstant.SOURCE_IOP);
				alsInvestigationDao.createRecord(dto.getMuwiId());
				if (!ServicesUtil.isEmpty(dto.getCheckList()) || dto.isProactive()) {
					System.err.println("");
					String hopperId = ServicesUtil.getUUID();
					boolean hopper_location_exists = pwHopperDao.checkIfPwHopperExists(dto.getTaskEventDto().getLocationCode());
					if(hopper_location_exists){
						pwHopperDao.updateProactive(dto.getTaskEventDto().getLocationCode(), dto.isProactive(),
								dto.getTaskEventDto().getProcessId(), hopperId, true);
					}
					else {
						pwHopperDao.insertProactive(dto.getMuwiId(),dto.getTaskEventDto().getLocationCode(), dto.getTaskEventDto().getLocationText(),
								hopperId, dto.getTaskEventDto().getProcessId(), dto.isProactive());
					}
					if (dto.isProactive()) {
						if (ServicesUtil.isEmpty(dto.getUserType()))
							if (!ServicesUtil.isEmpty(pwHopperDao.getCheckList(MurphyConstant.USER_TYPE_POT,
									dto.getTaskEventDto().getLocationCode(), "", dto.isProactive())))
								dto.setCheckList(pwHopperDao.getCheckList(MurphyConstant.USER_TYPE_POT,
										dto.getTaskEventDto().getLocationCode(), "", dto.isProactive()));
					}
					pwHopperDao.createInvstInstance("", dto.getTaskEventDto().getLocationCode(),
							dto.getTaskEventDto().getProcessId(), dto.getCheckList(), hopperId,
							dto.getTaskEventDto().getCreatedBy(), dto.isProactive());

				}

			} else if (MurphyConstant.DISPATCH_ORIGIN.equals(origin)) {
				origin = MurphyConstant.TASK;
			}
			responseMessage.setMessage(origin + " " + MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (Exception e) {
			logger.error("[Murphy][TaskSchedulingCanadaCalFacade][createTask][error]" + e.getMessage());
		}
		return responseMessage;
	
	}

	private ProcessEventsDto createProcForTask(TaskEventsDto dto, String loc) {
		ProcessEventsDto procDto = new ProcessEventsDto();
		if (!ServicesUtil.isEmpty(dto)) {
			procDto = new ProcessEventsDto();
			String grp = "";
			procDto.setProcessId(dto.getProcessId());
			procDto.setStatus(MurphyConstant.INPROGRESS);
			procDto.setStartedAt(dto.getCreatedAt());
			procDto.setStartedBy(dto.getCreatedBy());
			procDto.setStartedByDisplayName(dto.getCreatedByDisplay());
			procDto.setSubject(dto.getSubject());
			procDto.setName("");
			procDto.setProcessDisplayName("");
			procDto.setGroup(dto.getGroup());
			// Adding loc for incident INC0079467
			if (!ServicesUtil.isEmpty(dto.getGroup())) {
				if (!ServicesUtil.isEmpty(loc) && !(dto.getGroup().toLowerCase().contains(loc.toLowerCase()))) {
					if (loc.equalsIgnoreCase("Catarina")) {
						grp = "IOP_TM_ROC_Montney";
					} else if (loc.equalsIgnoreCase("Kaybob")) {
						grp = "IOP_TM_ROC_Kaybob";
					}
					procDto.setExtraRole(grp);
				}
			}
			procDto.setLocationCode(dto.getLocationCode());
			procDto.setProcessType(dto.getTaskMode());
			procDto.setRequestId(taskEventsDao.getLatestKey("TM_PROC_EVNTS", "REQUEST_ID"));
		} else {
			logger.error("[TaskSchedulingCanadaCalFacade][createProcForTask] - Error Please verify TaskEvent Dto is empty!");
		}
		return procDto;
	}

	
}
