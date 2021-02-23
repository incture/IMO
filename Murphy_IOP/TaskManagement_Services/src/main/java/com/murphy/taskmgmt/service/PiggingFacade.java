package com.murphy.taskmgmt.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.etsi.uri.x01903.v13.impl.GenericTimeStampTypeImpl;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.murphy.integration.interfaces.EnersightProveDailyLocal;
import com.murphy.integration.service.EnersightProveDaily;
import com.murphy.taskmgmt.dao.HierarchyDao;

import com.murphy.taskmgmt.dao.PiggingScheldulerDao;
import com.murphy.taskmgmt.dao.PipelineMeasurementDao;
import com.murphy.taskmgmt.dao.UserIDPMappingDao;
import com.murphy.taskmgmt.dto.CollaborationDto;
import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomAttrValuesDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.GroupsUserDto;

import com.murphy.taskmgmt.dto.PiggingSchedulerDto;
import com.murphy.taskmgmt.dto.PipelineMeasurementDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.service.interfaces.PiggingFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingCalFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

/**
 * @author Ritwik.Jain
 *
 */
@Service("piggingFacade")
@Transactional
public class PiggingFacade implements PiggingFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(PiggingFacade.class);	
	
	@Autowired
	private PipelineMeasurementDao pipelineMeasurementDao;

	@Autowired
	private PiggingScheldulerDao piggingScheldulerDao;
	
	@Autowired
	private PiggingSchedulerFacade piggingSchedulerFacade;

	@Autowired
	private HierarchyDao hierarchyDao;

	@Autowired
	private TaskSchedulingCalFacadeLocal taskSchedulingService;

	@Autowired
	private UserIDPMappingDao userDao;

	// Calculation of pig retrival time for a Specific Equipment
	@Override
	public double calculatePiggingTime(String equipmentId) {
		double time = 0;
		try {
			PipelineMeasurementDto dto = pipelineMeasurementDao.getPipeLineMeasurment(equipmentId);

			if (!ServicesUtil.isEmpty(dto)) {

				int length = dto.getLength();
				int diameter = dto.getDiameter();
				Set<String> setmuwi = new HashSet<>();
				Map<String, String> map = new HashMap<>();
				List<String> functionalLocations = pipelineMeasurementDao.getMuwiForEquipment(equipmentId);
				if (!ServicesUtil.isEmpty(functionalLocations)) {
					for (String funcLoc : functionalLocations) {
						String muwi = hierarchyDao.getMuwiByLocationCode(funcLoc);
						if (!ServicesUtil.isEmpty(muwi))
							setmuwi.add(muwi);
					}
					System.err.println("Set of wells For time Calculation :" + setmuwi);
					EnersightProveDailyLocal enersightProveDaily = new EnersightProveDaily();
					map = enersightProveDaily.fetchPiggingValue(setmuwi);
					double bfpd = 0;
					double mcfpd = 0;
					System.err.println("Map Response from Prove: " + map);
					if (!ServicesUtil.isEmpty(map.get("WaterProduction")) || !ServicesUtil.isEmpty(map.get("Oil"))) {

						if (!ServicesUtil.isEmpty(map.get("WaterProduction")))
							bfpd += Double.parseDouble(map.get("WaterProduction"));
						if (!ServicesUtil.isEmpty(map.get("Oil")))
							bfpd += Double.parseDouble(map.get("Oil"));
						if (!ServicesUtil.isEmpty(map.get("Gas")))
							mcfpd = Double.parseDouble(map.get("Gas"));
						else
							return 0;

						double divisor = 34.32 * bfpd + 366.67 * mcfpd;

						if (!ServicesUtil.isEmpty(divisor)) {

							time = MurphyConstant.LATERAL_FACTOR * 60 * length * diameter * diameter / divisor;
						}
					}
				}
			}
			return time;
		} catch (Exception e) {
			return 0;
		}

	}

	// Create task and update Schedular Table
	@Override
	public ResponseMessage UpdatePiggingHistory(Date now) {

		List<PiggingSchedulerDto> dtos = piggingScheldulerDao.getByFlag(MurphyConstant.NEW);
		if (!ServicesUtil.isEmpty(dtos)) {

			System.err.println("Number of Equipment due:" + dtos.size());
			ResponseMessage res = new ResponseMessage();
			for (PiggingSchedulerDto dto : dtos) {
				res = createTask(dto.getEquipmentId(), dto.getWorkOrderNo());

				if (!ServicesUtil.isEmpty(res)) {
					if ((MurphyConstant.FAILURE).equals(res.getStatus()))
						res = piggingScheldulerDao.updateHistory(dto.getWorkOrderNo(), MurphyConstant.TASK_NOT_CREATED,
								now);
					else
						res = piggingScheldulerDao.updateHistory(dto.getWorkOrderNo(), MurphyConstant.TASK_CREATED,
								now);
				} else {
					res = piggingScheldulerDao.updateHistory(dto.getWorkOrderNo(), MurphyConstant.TASK_NOT_CREATED,
							now);

				}

			}

			return res;
		}

		else {
			ResponseMessage msg = new ResponseMessage();
			msg.setMessage("There is No equipment due today");
			msg.setStatus(MurphyConstant.SUCCESS);
			msg.setStatusCode(MurphyConstant.CODE_SUCCESS);

			return msg;
		}
	}

	// Create task for equipment Id and WorkOrder Number
	@Override
	public ResponseMessage createTask(String equipmentId, String workOrderNo) {

		CustomTaskDto customTaskDto = new CustomTaskDto();
		TaskEventsDto taskEventsDto = new TaskEventsDto();
		ResponseMessage response = new ResponseMessage();
		  int workOrderZeroCount=0;
		try {
			// fetching data for
			PipelineMeasurementDto pipelineMeasurementDto = pipelineMeasurementDao.getPipeLineMeasurment(equipmentId);
			System.err.println(pipelineMeasurementDto.toString());
			Date dateTime = null;
			if (!ServicesUtil.isEmpty(pipelineMeasurementDto)
					&& !ServicesUtil.isEmpty(pipelineMeasurementDto.getStartLocation())
					&& !ServicesUtil.isEmpty(pipelineMeasurementDto.getEndLocation())) {
				double piggingTime = calculatePiggingTime(equipmentId);

				System.err.println("Pigging time:" + piggingTime);

				String equipmentDes = pipelineMeasurementDto.getLineName();

				String location = "";
				if (!ServicesUtil.isEmpty(pipelineMeasurementDto.getStartLocation()))
					location = pipelineMeasurementDao.getLocationText(pipelineMeasurementDto.getStartLocation());

				String field = pipelineMeasurementDao
						.getLocationText(pipelineMeasurementDto.getFlocId().substring(0, 15));
				System.err.println("location: " + location + " field: " + field);
				List<String> roles = new ArrayList<>();

				if ("Tilden Central".equalsIgnoreCase(field.trim()) || "Tilden North".equalsIgnoreCase(field.trim())
						|| "Tilden East".equalsIgnoreCase(field.trim()))
					field = "CentralTilden";
				else if ("Tilden West".equalsIgnoreCase(field.trim()))
					field = "WestTilden";
				else if ("Karnes North".equalsIgnoreCase(field.trim()) || "Karnes South".equalsIgnoreCase(field.trim()))
					field = "Karnes";

				roles.add("ROC_" + field.trim());
				System.err.println("Role: " + roles);
				List<GroupsUserDto> grpUser = userDao.getUsersBasedOnRole(roles);
				GroupsUserDto user = new GroupsUserDto();

				if (!ServicesUtil.isEmpty(grpUser))
					user = grpUser.get(0);
				TaskOwnersDto owner = new TaskOwnersDto();
				// TODO get owner Information
				owner.setEstResolveTime(10);
				if (!ServicesUtil.isEmpty(user.getUserId()))
					owner.setTaskOwner(user.getUserId());
				if (!ServicesUtil.isEmpty(user.getFirstName()) && !ServicesUtil.isEmpty(user.getLastName()))
					owner.setTaskOwnerDisplayName(user.getFirstName() + " " + user.getLastName());
				System.err.println("Owner: " + owner.toString());

				String group = "IOP_TM_ROC_" + field.trim();

				List<CustomAttrTemplateDto> customAttrList = new ArrayList<>();
				
				// Location
				CustomAttrTemplateDto custAttr1 = new CustomAttrTemplateDto();
				custAttr1.setClItemId("123");
				custAttr1.setDataType("Input");
				custAttr1.setIsDefault(true);
				custAttr1.setIsEditable(false);
				custAttr1.setIsMandatory(true);
				custAttr1.setLabel("Location");
				custAttr1.setLabelValue(location);
				custAttr1.setMaxLength(60);
				custAttr1.setSeqNumber(1);
				custAttr1.setShortDesc("location");
				custAttr1.setTaskTempId("123");

				customAttrList.add(custAttr1);
				// Assign to person
				CustomAttrTemplateDto custAttr2 = new CustomAttrTemplateDto();
				custAttr2.setClItemId("1234");
				custAttr2.setDataType("MultiSelect");
				custAttr2.setIsDefault(false);
				custAttr2.setIsEditable(true);
				custAttr2.setIsMandatory(true);
				custAttr2.setLabel("Assign to person(s)");
				custAttr2.setLabelValue(null);
				custAttr2.setMaxLength(1000);
				custAttr2.setSeqNumber(2);
				custAttr2.setShortDesc("Assign to person(s)");
				custAttr2.setTaskTempId("123");

				customAttrList.add(custAttr2);

				// Date Time
				CustomAttrTemplateDto custAttr3 = new CustomAttrTemplateDto();
				custAttr3.setClItemId("PIG001");
				custAttr3.setDataType("DateTime");
				custAttr3.setIsDefault(true);
				custAttr3.setIsEditable(true);
				custAttr3.setIsEnabled(true);
				custAttr3.setIsMandatory(true);
				custAttr3.setLabel(MurphyConstant.DATE_TIME);
				custAttr3.setLabelValue("" + piggingTime);
				custAttr3.setMaxLength(30);
				custAttr3.setSeqNumber(3);
				custAttr3.setShortDesc("Calculated pigging time");
				custAttr3.setTaskTempId("PROC_PIG");

				customAttrList.add(custAttr3);

				// Task Classification
				CustomAttrTemplateDto custAttr4 = new CustomAttrTemplateDto();
				custAttr4.setClItemId("12345");
				custAttr4.setDataType("Select");
				custAttr4.setIsDefault(true);
				custAttr4.setIsEditable(true);
				custAttr4.setIsMandatory(true);
				custAttr4.setLabel("Task Classification");
				custAttr4.setLabelValue("Operations");
				custAttr4.setMaxLength(20);
				custAttr4.setSeqNumber(4);
				custAttr4.setShortDesc("Task Classification");
				custAttr4.setTaskTempId("123");

				customAttrList.add(custAttr4);

				// Task SubClassification
				CustomAttrTemplateDto custAttr5 = new CustomAttrTemplateDto();
				custAttr5.setClItemId("123456");
				custAttr5.setDataType("Select");
				custAttr5.setDependentOn("12345");
				custAttr5.setIsDefault(true);
				custAttr5.setIsEditable(true);
				custAttr5.setIsMandatory(true);
				custAttr5.setLabel("Sub Classification");
				custAttr5.setLabelValue(MurphyConstant.PIGGING_LAUNCH);
				custAttr5.setMaxLength(20);
				custAttr5.setSeqNumber(5);
				custAttr5.setShortDesc("Select from below");
				custAttr5.setTaskTempId("123");

				customAttrList.add(custAttr5);
				// Status
				CustomAttrTemplateDto custAttr6 = new CustomAttrTemplateDto();
				custAttr6.setClItemId("1234567");
				custAttr6.setDataType("Text");
				custAttr6.setDependentOn("12345");
				custAttr6.setIsDefault(true);
				custAttr6.setIsEditable(false);
				custAttr6.setIsMandatory(false);
				custAttr6.setLabel("Status");
				custAttr6.setLabelValue("NEW");
				custAttr6.setMaxLength(60);
				custAttr6.setSeqNumber(6);
				custAttr6.setShortDesc("Status");
				custAttr6.setTaskTempId("123");

				customAttrList.add(custAttr6);
				
				
				//Truncating trailing WorkOrder Zero
				System.err.println("WorkOrder Number from HCI"+workOrderNo);
				workOrderZeroCount=workOrderNo.length();
				workOrderNo=workOrderNo.replaceFirst("^0+(?!$)","");


				// WorkOrder Number
				CustomAttrTemplateDto custAttr7 = new CustomAttrTemplateDto();
				custAttr7.setClItemId("PIG004");
				custAttr7.setDataType("Text");
				custAttr7.setIsDefault(true);
				custAttr7.setIsEditable(false);
				custAttr7.setIsMandatory(true);
				custAttr7.setLabel("Work Order Number");
				custAttr7.setLabelValue(workOrderNo);
				custAttr7.setMaxLength(30);
				custAttr7.setSeqNumber(7);
				custAttr7.setShortDesc("workOrder Number");
				custAttr7.setTaskTempId("PROC_PIG");

				customAttrList.add(custAttr7);

				// Equipment Id
				CustomAttrTemplateDto custAttr8 = new CustomAttrTemplateDto();
				custAttr8.setClItemId("PIG002");
				custAttr8.setDataType("Text");
				custAttr8.setIsDefault(true);
				custAttr8.setIsEditable(false);
				custAttr8.setIsMandatory(true);
				custAttr8.setLabel(MurphyConstant.EQUIPMENT);
				custAttr8.setLabelValue(equipmentId);
				custAttr8.setMaxLength(20);
				custAttr8.setSeqNumber(8);
				custAttr8.setShortDesc("Equipment Id");
				custAttr8.setTaskTempId("PROC_PIG");

				customAttrList.add(custAttr8);
				// Equuipment Description
				CustomAttrTemplateDto custAttr9 = new CustomAttrTemplateDto();
				custAttr9.setClItemId("PIG003");
				custAttr9.setDataType("Text");
				custAttr9.setIsDefault(true);
				custAttr9.setIsEditable(false);
				custAttr9.setIsMandatory(true);
				custAttr9.setLabel(MurphyConstant.EQUIPMENT_DESCRIPTION);
				custAttr9.setLabelValue(equipmentDes);
				custAttr9.setMaxLength(20);
				custAttr9.setSeqNumber(9);
				custAttr9.setShortDesc("Equipment Description");
				custAttr9.setTaskTempId("PROC_PIG");

				customAttrList.add(custAttr9);

				customTaskDto.setCustomAttr(customAttrList);

				List<TaskOwnersDto> taskOwners = new ArrayList<TaskOwnersDto>();
				taskOwners.add(owner);
				taskEventsDto.setOwners(taskOwners);
				taskEventsDto.setCreatedByDisplay("SYSTEM USER");
				taskEventsDto.setCreatedBy("SYSTEM");
				taskEventsDto.setLocationCode(pipelineMeasurementDto.getStartLocation());
				taskEventsDto.setOrigin(MurphyConstant.DISPATCH_ORIGIN);
				taskEventsDto.setParentOrigin(MurphyConstant.PIGGING);
				taskEventsDto.setStatus(MurphyConstant.ASSIGN);
				taskEventsDto.setSubClassification(MurphyConstant.PIGGING_LAUNCH);
				taskEventsDto.setSubject("Operations / " + MurphyConstant.PIGGING_LAUNCH);
				taskEventsDto.setGroup(group);
				taskEventsDto.setTaskType("SYSTEM");
				taskEventsDto.setPrevTask(workOrderNo);
				customTaskDto.setTaskEventDto(taskEventsDto);
				
				
				//Add System Default Comment for Pig Launch
				if(!ServicesUtil.isEmpty(pipelineMeasurementDto)){
				List<CollaborationDto> collabDtoList=new ArrayList<CollaborationDto>();	
				CollaborationDto collaborationDto=new CollaborationDto();
				String piggMessage="* Pig Size : "+pipelineMeasurementDto.getDiameter()+"\r\n"+
						"* Pig Length : "+pipelineMeasurementDto.getLength()+"\r\n"+
						"* Equipment Description : "+pipelineMeasurementDto.getLineName();
				collaborationDto.setMessage(piggMessage);
			    collaborationDto.setUserDisplayName("SYSTEM USER");
			    collaborationDto.setUserId("SYSTEM");
				collabDtoList.add(collaborationDto);
				customTaskDto.setCollabrationDtos(collabDtoList);
				}
				
				
				
				
				if (!ServicesUtil.isEmpty(location))
					return taskSchedulingService.createTask(customTaskDto);
				else {
					response.setMessage("Unable to create the Task");
					response.setStatus(MurphyConstant.FAILURE);
					response.setStatusCode(MurphyConstant.CODE_FAILURE);

				}
				
			//	return taskSchedulingService.createTask(customTaskDto);

			} else {
				response.setMessage("Unable to create the Task");
				response.setStatus(MurphyConstant.FAILURE);
				response.setStatusCode(MurphyConstant.CODE_FAILURE);
			}
		} catch (Exception e) {

			System.err.println("[PiggingFacade][CreateTask]" + e.getMessage());
			response.setMessage("Unable to create the Task");
			response.setStatus(MurphyConstant.FAILURE);
			response.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return response;

	}

	// Calculation for nextDue date on the basis of frequency
	/*
	 * private Date getNextDueDate(Date now, int frequency) { LocalDateTime date
	 * =
	 * now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays
	 * (frequency);
	 * 
	 * return
	 * java.util.Date.from(date.atZone(ZoneId.systemDefault()).toInstant()); }
	 */

	// Calulation for pigging time
	private static Date getEstimatedDate(Date now, double minuts) {
		LocalDateTime date = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
				.plusMinutes((long) Math.floor(minuts));
		if (minuts != 0)
			return java.util.Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
		else
			return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<CustomAttrTemplateDto> custtemp = new ArrayList<>();

		Date date = new Date();
		Date date2 = new LocalDate(date).toDate();

		Date piggingTime = getEstimatedDate(date, 40);
		System.out.println(date2);
		System.out.println(ServicesUtil.isEmpty(piggingTime) ? null
				: ServicesUtil.convertFromZoneToZoneString(piggingTime, null, MurphyConstant.UTC_ZONE,
						MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));

		System.out.println(ServicesUtil.isEmpty(piggingTime) ? null
				: ServicesUtil.convertFromZoneToZoneString(piggingTime, null, MurphyConstant.UTC_ZONE,
						MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
	}

	// Post service for genrating Payload for creation of Pig retrival Task
	@Override
	public CustomTaskDto getRetrivePayload(CustomTaskDto dto) {
		List<CustomAttrTemplateDto> customAttributes = dto.getCustomAttr();
		String endLocationCode = "";
		PipelineMeasurementDto pipelineMeasurementDto=null;
		if (!ServicesUtil.isEmpty(customAttributes.get(7))) {
			String location = null;
			String workOrderNo=null;
			String taskId=null;
			String pigLaunchStatus=null;
			try {

				//HCI call after Pig Launch Completion
				PiggingSchedulerDto piggingDto=new PiggingSchedulerDto();
				workOrderNo=customAttributes.get(6).getLabelValue();
				pigLaunchStatus=piggingScheldulerDao.checkPigLaunchStatus(workOrderNo);
				if("Y".equalsIgnoreCase(pigLaunchStatus)){
					logger.error("SAP ECC Update for Pig Launch Status done");
				}
				else{
				taskId=piggingScheldulerDao.getTaskIdForWorkOrder(workOrderNo);
				if(!ServicesUtil.isEmpty(taskId)){
				piggingDto.setTaskID(taskId);
				piggingDto.setWorkOrderNo(workOrderNo);
				piggingSchedulerFacade.updateWorkOrder(piggingDto,MurphyConstant.PIGGING_LAUNCH);
				}
				else{
					logger.error("TaskId is Empty For WorkOrderNo"+workOrderNo);
				}
				}
				String equipmentId = customAttributes.get(7).getLabelValue();
				pipelineMeasurementDto = pipelineMeasurementDao
						.getPipeLineMeasurment(equipmentId);
				endLocationCode = pipelineMeasurementDto.getEndLocation();
				location = pipelineMeasurementDao.getLocationText(endLocationCode);

			} catch (Exception e) {
				location = null;

			}
			CustomAttrValuesDto customValue = new CustomAttrValuesDto();
			customValue.setValue(location);
			List<CustomAttrValuesDto> listValueLocation = new ArrayList<>();
			listValueLocation.add(customValue);
			CustomAttrTemplateDto customAttrLocation = customAttributes.get(0);
			customAttrLocation.setValueDtos(listValueLocation);
			customAttrLocation.setLabelValue(location);

			customAttributes.set(0, customAttrLocation);
			List<CustomAttrValuesDto> listValueAssing = new ArrayList<>();
			listValueAssing.add(new CustomAttrValuesDto());
			CustomAttrTemplateDto customAttrAssignToPerson = customAttributes.get(1);
			customAttrAssignToPerson.setValueDtos(listValueAssing);
			customAttrAssignToPerson.setLabelValue(null);
			customAttrAssignToPerson.setIsEditable(true);
			customAttributes.set(1, customAttrAssignToPerson);

			CustomAttrTemplateDto customAttrTime = customAttributes.get(2);
			customAttrTime.setIsEditable(true);
			customAttrTime.setIsEnabled(true);
			customAttributes.set(2, customAttrTime);

			List<CustomAttrValuesDto> listValuesub = new ArrayList<>();
			CustomAttrTemplateDto customAttrSubClass = customAttributes.get(4);
			customAttrSubClass.setLabelValue(MurphyConstant.PIGGING_RECEIVE);

			CustomAttrValuesDto customValues = new CustomAttrValuesDto();
			customValues.setValue(MurphyConstant.PIGGING_RECEIVE);
			listValuesub.add(customValues);
			customAttrSubClass.setValueDtos(listValuesub);
			customAttributes.set(4, customAttrSubClass);

		}
		dto.setCustomAttr(customAttributes);
		
		TaskEventsDto taskEventsDto = dto.getTaskEventDto();
		taskEventsDto.setSubClassification(MurphyConstant.PIGGING_RECEIVE);
		taskEventsDto.setSubject("Operation / " + MurphyConstant.PIGGING_RECEIVE);
		taskEventsDto.setTaskType(null);
		taskEventsDto.setLocationCode(endLocationCode);
		taskEventsDto.setOwners(new ArrayList<>());
		taskEventsDto.setTaskId(null);
		taskEventsDto.setProcessId(null);
		dto.setTaskEventDto(taskEventsDto);
		dto.setLocHierarchy(hierarchyDao.getHierarchy(hierarchyDao.getLocationByLocCode(endLocationCode),
				hierarchyDao.getLocationTypeByLocCode(endLocationCode)));

		return dto;
	}

}