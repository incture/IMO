package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.GroupsUserDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingCalFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ItaDopDao")
public class ItaDopDao {
	
	@Autowired
	HierarchyDao locDao;
	
	@Autowired
	private UserIDPMappingDao userDao;
	
	@Autowired
	private TaskSchedulingCalFacadeLocal taskSchedulingService;
	
	private static final Logger logger = LoggerFactory.getLogger(ItaDopDao.class);
	
	public ResponseMessage createTask(String loc_code,String classification,String subClassification, String taskType) {
		
		CustomTaskDto customTaskDto = new CustomTaskDto();
    	TaskEventsDto taskEventsDto = new TaskEventsDto();
    	ResponseMessage response = new ResponseMessage();
    	
    	try{
    		if (!ServicesUtil.isEmpty(loc_code)) {
    			String locationText = locDao.getLocationByLocCode(loc_code);
				// Fetch field(location text) from location code to check role(ROC)
				String field = locDao.getLocationByLocCode(loc_code.substring(0, 15));

				if ("Tilden Central".equalsIgnoreCase(field.trim()) || "Tilden North".equalsIgnoreCase(field.trim())
						|| "Tilden East".equalsIgnoreCase(field.trim()))
					field = "CentralTilden";
				else if ("Tilden West".equalsIgnoreCase(field.trim()))
					field = "WestTilden";
				else if ("Karnes North".equalsIgnoreCase(field.trim()) || "Karnes South".equalsIgnoreCase(field.trim()))
					field = "Karnes";
				logger.error("[Murphy][ItaDopDao][createTask][field]" + field);

				List<String> roles = new ArrayList<>();
				roles.add("ROC_" + field.trim());
				logger.error("Role: " + roles);

				// Get users based on role(ROC)
				List<GroupsUserDto> grpUser = userDao.getUsersBasedOnRole(roles);
				GroupsUserDto user = new GroupsUserDto();

				if (!ServicesUtil.isEmpty(grpUser))
					user = grpUser.get(0);         // Fetch first user from the ROC group
				// Get owner(ROC user) Information
				TaskOwnersDto owner = new TaskOwnersDto();
				owner.setEstResolveTime(10);
				if (!ServicesUtil.isEmpty(user.getUserId()))
					owner.setTaskOwner(user.getUserId());
				if (!ServicesUtil.isEmpty(user.getFirstName()) && !ServicesUtil.isEmpty(user.getLastName()))
					owner.setTaskOwnerDisplayName(user.getFirstName() + " " + user.getLastName());
				logger.error("Owner: " + owner.toString());

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
				custAttr1.setLabelValue(locationText);
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

				// Task Classification
				CustomAttrTemplateDto custAttr3 = new CustomAttrTemplateDto();
				custAttr3.setClItemId("12345");
				custAttr3.setDataType("Select");
				custAttr3.setIsDefault(true);
				custAttr3.setIsEditable(true);
				custAttr3.setIsMandatory(true);
				custAttr3.setLabel("Task Classification");
				custAttr3.setLabelValue(classification);
				custAttr3.setMaxLength(20);
				custAttr3.setSeqNumber(4);
				custAttr3.setShortDesc("Task Classification");
				custAttr3.setTaskTempId("123");

				customAttrList.add(custAttr3);

				// Task SubClassification
				CustomAttrTemplateDto custAttr4 = new CustomAttrTemplateDto();
				custAttr4.setClItemId("123456");
				custAttr4.setDataType("Select");
				custAttr4.setDependentOn("12345");
				custAttr4.setIsDefault(true);
				custAttr4.setIsEditable(true);
				custAttr4.setIsMandatory(true);
				custAttr4.setLabel("Sub Classification");
				custAttr4.setLabelValue(subClassification);
				custAttr4.setMaxLength(20);
				custAttr4.setSeqNumber(5);
				custAttr4.setShortDesc("Select from below");
				custAttr4.setTaskTempId("123");

				customAttrList.add(custAttr4);
				// Status
				CustomAttrTemplateDto custAttr5 = new CustomAttrTemplateDto();
				custAttr5.setClItemId("1234567");
				custAttr5.setDataType("Text");
				custAttr5.setDependentOn("12345");
				custAttr5.setIsDefault(true);
				custAttr5.setIsEditable(false);
				custAttr5.setIsMandatory(false);
				custAttr5.setLabel("Status");
				custAttr5.setLabelValue("DRAFT");
				custAttr5.setMaxLength(60);
				custAttr5.setSeqNumber(6);
				custAttr5.setShortDesc("Status");
				custAttr5.setTaskTempId("123");

				customAttrList.add(custAttr5);
				// Additional attribute
				CustomAttrTemplateDto custAttr6 = new CustomAttrTemplateDto();
				custAttr6.setClItemId("12345678");
				custAttr6.setDataType("Text");
				custAttr6.setDependentOn(null);
				custAttr6.setIsDefault(false);
				custAttr6.setIsEditable(false);
				custAttr6.setIsMandatory(false);
				custAttr6.setLabel(null);
				custAttr6.setLabelValue(null);
				custAttr6.setMaxLength(20);
				custAttr6.setSeqNumber(7);
				custAttr6.setShortDesc(null);
				custAttr6.setTaskTempId(null);

				customAttrList.add(custAttr6);

				customTaskDto.setCustomAttr(customAttrList);

				List<TaskOwnersDto> taskOwners = new ArrayList<TaskOwnersDto>();
				taskOwners.add(owner);
				taskEventsDto.setOwners(taskOwners);
				taskEventsDto.setCreatedByDisplay("SYSTEM USER");
				taskEventsDto.setCreatedBy("SYSTEM");
				taskEventsDto.setLocationCode(loc_code);
				taskEventsDto.setOrigin(taskType);
				taskEventsDto.setParentOrigin(MurphyConstant.P_ITA_DOP); 
				taskEventsDto.setStatus(MurphyConstant.DRAFT);
				taskEventsDto.setSubClassification(subClassification);
				taskEventsDto.setSubject(classification +" / " + subClassification);
				taskEventsDto.setGroup(group);
				taskEventsDto.setTaskType("SYSTEM");
				customTaskDto.setTaskEventDto(taskEventsDto);

				logger.error("[Murphy][ItaDopDao][createTask][Create task on location] " +loc_code );
				response =  taskSchedulingService.createTask(customTaskDto);
				logger.error("newClassification : "+classification);
    		}
    		else
    		{
    			response.setMessage("Unable to create the ITA DOP Task");
				response.setStatus(MurphyConstant.FAILURE);
				response.setStatusCode(MurphyConstant.CODE_FAILURE);
    		}
    	  }
    	  catch(Exception e)
    	{
    		  logger.error("[Murphy][ItaDopDao][createTask][Exception]" + e.getMessage());
    	}
    	return response;	
	}
	
}
