package com.murphy.taskmgmt.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.GroupsUserDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingCalFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ItaMappingDao")
public class ItaMappingDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	HierarchyDao locDao;
	
	
	@Autowired
	private UserIDPMappingDao userDao;
	
	@Autowired
	private TaskSchedulingCalFacadeLocal taskSchedulingService;
	
	
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][BaseDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}
	
   private static final Logger logger = LoggerFactory.getLogger(ItaMappingDao.class);	
   ArrayList<String> taskList = null;
   
    @Transactional(value=TxType.REQUIRES_NEW)
    public ResponseMessage createTask(String loc_code,String classification,String subClassification, String rootCause,
    		String newClassification, String newSubClassification, String typeOfTaskToBeCreated, int durationInDays) {

    	CustomTaskDto customTaskDto = null;
    	ResponseMessage response = new ResponseMessage();
    	taskList = new ArrayList<String>();
    	boolean create = false;
    	try{
    		if (!ServicesUtil.isEmpty(loc_code)) {
    			taskList = createTaskList(loc_code,classification,subClassification,rootCause,taskList,durationInDays);
    			logger.error("tasklist : " + taskList.get(0));
    			create = taskEligiblility(loc_code,newClassification,newSubClassification, taskList,durationInDays,typeOfTaskToBeCreated);
    			
    			if(typeOfTaskToBeCreated.equalsIgnoreCase("Inquiry") && create){
    				customTaskDto = customTaskDtoForInquiry(newSubClassification,loc_code,typeOfTaskToBeCreated);
    				logger.error("[Murphy][ItaTaskDao][createTask][Create task on location] " +loc_code );
    				response =  taskSchedulingService.createTask(customTaskDto);
    				logger.error("Inquiry newSubClassification : "+newSubClassification);
    			}
    			else if (typeOfTaskToBeCreated.equalsIgnoreCase("Dispatch") && create) {
    				customTaskDto = customTaskDtoForDispatch(newClassification,newSubClassification,loc_code,typeOfTaskToBeCreated);
    				logger.error("[Murphy][ItaTaskDao][createTask][Create task on location] " +loc_code );
    				response =  taskSchedulingService.createTask(customTaskDto);
    				logger.error("newClassification : "+newClassification);
    			}
    			else {
    				logger.error("Unable to create the Task as ITA already present : create :" + create + " for task type : " + typeOfTaskToBeCreated);
    				response.setMessage("Unable to create the Task as ITA already present");
    				response.setStatus(MurphyConstant.FAILURE);
    				response.setStatusCode(MurphyConstant.CODE_FAILURE);
    			}
    		}
    		else {
    			logger.error("Unable to create the Task as loc code empty");
    			response.setMessage("Unable to create the Task as loc code empty");
    			response.setStatus(MurphyConstant.FAILURE);
    			response.setStatusCode(MurphyConstant.CODE_FAILURE);
    		}
    	}
    	catch(Exception e)
    	{
    		logger.error("[Murphy][ItaTaskDao][createTask][Exception]" + e.getMessage());
    		response.setMessage("Unable to create the Task");
    		response.setStatus(MurphyConstant.FAILURE);
    		response.setStatusCode(MurphyConstant.CODE_FAILURE);
    	}
    	return response;
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<String> createTaskList(String loc_code,String classification,String subClassification,String rootCause,
			ArrayList<String> taskList, int durationInDays)
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, - durationInDays);
		
		Query query = null;
		 try{
			String rootCauseQuery = null;
			if(!ServicesUtil.isEmpty(rootCause)){
				String rootCauseString = rootCause.replace(",", "','");
				rootCauseQuery = "INNER JOIN TM_ROOTCAUSE_INSTS rt ON rt.task_Id = te.task_id "
						+"WHERE rt.created_At = (SELECT max(rtx.created_At)  FROM TM_ROOTCAUSE_INSTS rtx WHERE rtx.task_id = te.task_id) AND"
						+"(rt.root_cause in('"+ rootCauseString+ "')) AND";			
			 }
			else
				rootCauseQuery = "WHERE";
		// Query to fetch list of tasks IDs for the given classification and subClassification for loc_code
		  String queryString = "select te.task_id from TM_TASK_EVNTS te inner join TM_PROC_EVNTS pe" 
                 +" on pe.PROCESS_ID = te.PROCESS_ID "+rootCauseQuery+" pe.loc_code = '" + loc_code 
                 + "' and te.parent_origin not in ('"+MurphyConstant.P_ITA + "') and te.TASK_ID in ("
	   			 +" select TASK_ID from TM_ATTR_INSTS where INS_VALUE = '"+ subClassification +"' and ATTR_TEMP_ID = '123456'"
				 +" and TASK_ID in ("
				 +" select i.task_id from TM_ATTR_INSTS i inner join tm_task_evnts t on i.task_id = t.task_id" 
				 +" where INS_VALUE = '" + classification + "' and i.ATTR_TEMP_ID = '12345'))"
				 +" and to_date(te.CREATED_AT) >= '" + sdf.format(cal.getTime()) +"'";
		  
		  logger.error("[Murphy][ItaTaskDao][createTaskList][Query]" + queryString);
		  query = this.getSession().createSQLQuery(queryString);
			
		  List<Object> response = (List<Object>) query.list();
		  if (!ServicesUtil.isEmpty(response)) {
			  for (Object obj : response) {
				  taskList.add(ServicesUtil.isEmpty(obj) ? null : (String) obj);
			  }
		  }
		 }
		 catch(Exception e)
		 {
			 logger.error("[Murphy][ItaTaskDao][createTaskList][Exception]" + e.getMessage());
		 }
		  return taskList;
	}    

    @SuppressWarnings("unchecked")
	public boolean taskEligiblility(String loc_code,String newClassification,String newSubClassification,
			ArrayList<String>taskList, int durationInDays,String typeOfTaskToBeCreated)
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, - durationInDays);
		boolean create = false;
		String task_id = null;
		 String task_id_not_Completed = null;
		Query query = null;
		ArrayList<String> taskListITA = new ArrayList<String>();
		
		try{
		  String classificationQuery = null;
		  // If an inquiry is created 
		  if(typeOfTaskToBeCreated.equalsIgnoreCase("Inquiry")){
			classificationQuery = "SELECT task_id FROM TM_ATTR_INSTS where INS_VALUE = '"+ newSubClassification+ "' and ATTR_TEMP_ID = 'INQ05'";
		  }
		  // If dispatch task is created
		  if(typeOfTaskToBeCreated.equalsIgnoreCase("Dispatch")){
			classificationQuery = "select TASK_ID from TM_ATTR_INSTS where INS_VALUE = '" + newSubClassification
				 + "' and ATTR_TEMP_ID = '123456'" + " and TASK_ID in ("
				 + " select i.task_id from TM_ATTR_INSTS i inner join tm_task_evnts t on i.task_id = t.task_id"
				 + " where INS_VALUE = '" + newClassification + "' and i.ATTR_TEMP_ID = '12345')";
		  }
		  // Query to check if ITA exists on location or not
		  String queryStringLocation = "select te.task_id from TM_TASK_EVNTS te inner join TM_PROC_EVNTS pe" 
				 +" on pe.PROCESS_ID = te.PROCESS_ID where pe.loc_code = '" + loc_code + "' and te.parent_origin = '"+MurphyConstant.P_ITA+ "'"
				 +" and te.TASK_ID in (" + classificationQuery + ")"
				 +" and to_date(te.CREATED_AT) >= '" + sdf.format(cal.getTime()) +"'";
		  
		  logger.error("[Murphy][ItaTaskDao][taskEligiblility][queryStringLocation]" + queryStringLocation);
		  Object obj_taskId = this.getSession().createSQLQuery(queryStringLocation).uniqueResult();
		  if(!ServicesUtil.isEmpty(obj_taskId))
			  task_id = obj_taskId.toString();
		  logger.error("task_id " + task_id);
		  if(!ServicesUtil.isEmpty(task_id))
		  {
			  // Query to find task_id of an Active ITA, that is status not COMPLETED or REJECTED
			  String queryNotCompleted = "select te.task_id from TM_TASK_EVNTS te,TM_PROC_EVNTS pe" 
					 +" where pe.PROCESS_ID = te.PROCESS_ID and pe.loc_code = '" + loc_code + "' and te.parent_origin = '"+MurphyConstant.P_ITA+ "'"
					 +" and te.status not in ('" + MurphyConstant.COMPLETE + "','"+ MurphyConstant.REJECTED + "')"
					 +" and te.TASK_ID in (" + classificationQuery + ")"
					 +" and to_date(te.CREATED_AT) >= '" + sdf.format(cal.getTime()) +"'";
			  
			  logger.error("[Murphy][ItaTaskDao][taskEligiblility][queryNotCompleted]" + queryNotCompleted);
			  Object obj1 = this.getSession().createSQLQuery(queryNotCompleted).uniqueResult();
			  if(!ServicesUtil.isEmpty(obj1))
				  task_id_not_Completed = obj1.toString();
			  logger.error("task_id_Completed "+task_id_not_Completed);
			  if(!ServicesUtil.isEmpty(task_id_not_Completed))
			  {
				  create = false;
			  }
			  else
			  {
				 // Query to get list of tasks from ITA_MAPPING in case ITA is completed/rejected
				 String queryMappingTable = "select dispatch_task_id from ITA_MAPPING where to_date(CREATED_AT) >= '"
						 + sdf.format(cal.getTime()) +"'";
				 
				 logger.error("[Murphy][ItaTaskDao][taskEligiblility][queryMappingTable]" + queryMappingTable);
				  query = this.getSession().createSQLQuery(queryMappingTable);
				  List<Object> response = (List<Object>) query.list();
				  if (!ServicesUtil.isEmpty(response)) {
					  for (Object obj : response) {
						  taskListITA.add(ServicesUtil.isEmpty(obj) ? null : (String) obj);
					  }
				  }
				  logger.error("taskListITA size :" + taskListITA.size());
				  // to check if any new task got created in tm_task_evnts table, if yes then create new ITA
				  logger.error("taskList before remove:" + taskList.size());
				  taskList.removeAll(taskListITA);
				  logger.error("taskList after remove:" + taskList.size());
				  if (!ServicesUtil.isEmpty(taskList))
				  {
					  create = true;
				  }
				  else{ 
					  create = false;
				  }
			  }
		  }
		  else{ 
			  create = true;
		  }
		}
		catch(Exception e)
		{
			logger.error("[Murphy][ItaTaskDao][taskEligiblility][Exception]" + e.getMessage());
		}
		return create;  
	}
    
    public CustomTaskDto customTaskDtoForDispatch(String newClassification, String newSubClassification,String loc_code,String typeOfTaskToBeCreated){
    	CustomTaskDto customTaskDto = new CustomTaskDto();
    	TaskEventsDto taskEventsDto = new TaskEventsDto();
    	
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
		logger.error("[Murphy][ItaMappingDao][customTaskDtoForDispatch][field]" + field);

		List<String> roles = new ArrayList<>();
		roles.add("ROC_" + field.trim());
		logger.error("[customTaskDtoForDispatch] Role: " + roles);

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
		logger.error("[customTaskDtoForDispatch] Owner: " + owner.toString());

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
		custAttr3.setLabelValue(newClassification);
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
		custAttr4.setLabelValue(newSubClassification);
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
		taskEventsDto.setOrigin(typeOfTaskToBeCreated);
		taskEventsDto.setParentOrigin(MurphyConstant.P_ITA); 
		taskEventsDto.setStatus(MurphyConstant.DRAFT);
		taskEventsDto.setSubClassification(newSubClassification);
		taskEventsDto.setSubject(newClassification +" / " + newSubClassification);
		taskEventsDto.setGroup(group);
		taskEventsDto.setTaskType("SYSTEM");
		customTaskDto.setTaskEventDto(taskEventsDto);
    	
    	return customTaskDto;
    	
    }
    
    public CustomTaskDto customTaskDtoForInquiry(String newSubClassification,String loc_code,String typeOfTaskToBeCreated){
    	CustomTaskDto customTaskDto = new CustomTaskDto();
    	TaskEventsDto taskEventsDto = new TaskEventsDto();
    	
    	String locationText = locDao.getLocationByLocCode(loc_code);
		// Fetch field(location text) from location code to check role(POT)
		String field = locDao.getLocationByLocCode(loc_code.substring(0, 15));
		String Pot_role = null;
		String well_tier = locDao.getTierByCode(loc_code);

		if ( "Catarina".equalsIgnoreCase(field.trim()) || "Tilden West".equalsIgnoreCase(field.trim()) || "Tilden North".equalsIgnoreCase(field.trim()))
			Pot_role = "West";
		else if ("Karnes North".equalsIgnoreCase(field.trim()) || "Karnes South".equalsIgnoreCase(field.trim()) || 
				"Tilden East".equalsIgnoreCase(field.trim()) || "Tilden Central".equalsIgnoreCase(field.trim()))
			Pot_role = "East";
		logger.error("[Murphy][ItaMappingDao][customTaskDtoForInquiry][field]" + field + "[well_tier]" + well_tier);

		List<String> roles = new ArrayList<>();
		roles.add("POT_" + Pot_role.trim());
		logger.error("[customTaskDtoForInquiry]Role: " + roles);

		// Get users based on role(POT)
		List<GroupsUserDto> grpUser = userDao.getUsersBasedOnRole(roles);
		GroupsUserDto user = new GroupsUserDto();

		if (!ServicesUtil.isEmpty(grpUser))
			user = grpUser.get(0);         // Fetch first user from the POT group
		// Get owner(POT user) Information
		TaskOwnersDto owner = new TaskOwnersDto();
		owner.setEstResolveTime(0);
		if (!ServicesUtil.isEmpty(user.getUserId()))
			owner.setTaskOwner(user.getUserId());
		if (!ServicesUtil.isEmpty(user.getFirstName()) && !ServicesUtil.isEmpty(user.getLastName()))
			owner.setTaskOwnerDisplayName(user.getFirstName() + " " + user.getLastName());
		logger.error("[customTaskDtoForInquiry] Owner: " + owner.toString());

		String group = "IOP_POT_" + Pot_role.trim();

		List<CustomAttrTemplateDto> customAttrList = new ArrayList<>();
		// Location
		CustomAttrTemplateDto custAttr1 = new CustomAttrTemplateDto();
		custAttr1.setClItemId("INQ01");
		custAttr1.setDataType("Input");
		custAttr1.setIsDefault(false);
		custAttr1.setIsEditable(false);
		custAttr1.setIsMandatory(true);
		custAttr1.setLabel("Location");
		custAttr1.setLabelValue(locationText);
		custAttr1.setMaxLength(0);
		custAttr1.setSeqNumber(1);
		custAttr1.setShortDesc("location");
		custAttr1.setTaskTempId("PROC_INQ");

		customAttrList.add(custAttr1);
		// Tier
		CustomAttrTemplateDto custAttr2 = new CustomAttrTemplateDto();
		custAttr2.setClItemId("INQ04");
		custAttr2.setDataType("Input");
		custAttr2.setIsDefault(false);
		custAttr2.setIsEditable(false);
		custAttr2.setIsMandatory(false);
		custAttr2.setLabel("Tier");
		custAttr2.setLabelValue(well_tier); 
		custAttr2.setMaxLength(0);
		custAttr2.setSeqNumber(2);
		custAttr2.setShortDesc("Tier");
		custAttr2.setTaskTempId("PROC_INQ");

		customAttrList.add(custAttr2);

		// Assign to group
		CustomAttrTemplateDto custAttr3 = new CustomAttrTemplateDto();
		custAttr3.setClItemId("INQ02");
		custAttr3.setDataType("Select");
		custAttr3.setIsDefault(false);
		custAttr3.setIsEditable(true);
		custAttr3.setIsMandatory(true);
		custAttr3.setLabel("Assign to group");
		custAttr3.setLabelValue("POT"); 
		custAttr3.setMaxLength(0);
		custAttr3.setSeqNumber(3);
		custAttr3.setShortDesc("Assign To Group");
		custAttr3.setTaskTempId("TASK_INQ");

		customAttrList.add(custAttr3);

		// Assign to person(s)
		CustomAttrTemplateDto custAttr4 = new CustomAttrTemplateDto();
		custAttr4.setClItemId("INQ03");
		custAttr4.setDataType("MultiSelect");
		custAttr4.setDependentOn("INQ02");
		custAttr4.setIsDefault(false);
		custAttr4.setIsEditable(true);
		custAttr4.setIsMandatory(true);
		custAttr4.setLabel("Assign to person(s)");
		custAttr4.setLabelValue(null);
		custAttr4.setMaxLength(0);
		custAttr4.setSeqNumber(4);
		custAttr4.setShortDesc("Assign to User");
		custAttr4.setTaskTempId("TASK_INQ");

		customAttrList.add(custAttr4);
		// Issue Classification
		CustomAttrTemplateDto custAttr5 = new CustomAttrTemplateDto();
		custAttr5.setClItemId("INQ05");
		custAttr5.setDataType("Select");
		custAttr5.setIsDefault(false);
		custAttr5.setIsEditable(true);
		custAttr5.setIsMandatory(true);
		custAttr5.setLabel("Issue Classification");
		custAttr5.setLabelValue(newSubClassification);
		custAttr5.setMaxLength(100);
		custAttr5.setSeqNumber(5);
		custAttr5.setShortDesc("Issue Classification");
		custAttr5.setTaskTempId("PROC_INQ");

		customAttrList.add(custAttr5);

		customTaskDto.setCustomAttr(customAttrList);
		customTaskDto.setProactive(false);

		List<TaskOwnersDto> taskOwners = new ArrayList<TaskOwnersDto>();
		taskOwners.add(owner);
		taskEventsDto.setOwners(taskOwners);
		taskEventsDto.setCreatedByDisplay("SYSTEM USER");
		taskEventsDto.setCreatedBy("SYSTEM");
		taskEventsDto.setLocationCode(loc_code);
		taskEventsDto.setOrigin(typeOfTaskToBeCreated);
		taskEventsDto.setParentOrigin(MurphyConstant.P_ITA); 
		taskEventsDto.setStatus(MurphyConstant.ASSIGN);
		taskEventsDto.setSubject(locationText + "-" + newSubClassification);
		taskEventsDto.setGroup(group);
		taskEventsDto.setTaskType("SYSTEM");
		customTaskDto.setTaskEventDto(taskEventsDto);
    	
    	return customTaskDto;
    	
    }
    
    public ArrayList<String> getTaskList()
    {
    	return taskList;
    }
}
