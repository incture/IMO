package com.murphy.taskmgmt.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.integration.dto.GasBlowByDto;
import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomTaskDto;
import com.murphy.taskmgmt.dto.DOPVarianceDto;
import com.murphy.taskmgmt.dto.GroupsUserDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TaskOwnersDto;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingCalFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ItaWaterOilDao")
@Transactional
public class ItaWaterOilDao {
	
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
			logger.error("[Murphy][ItaWaterOilDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}
	}
	private static final Logger logger = LoggerFactory.getLogger(ItaWaterOilDao.class);
	
	@SuppressWarnings({ "unchecked" })
	public ResponseMessage createItaWaterOil(String tier,double oilThreshold, double waterThreshold,HashMap<String, Double> todayMap,HashMap<String, Double> yesterdayMap,
			HashMap<String, Double> daysAvgMap,String classification, String subClassification,String taskType) {

		String locCode = null, description = null, wellTier = null;
		Double water_var= null,oil_var = null;
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			HashMap<String, Double> diffMap = new HashMap<>();
			for (Entry<String, Double> today : todayMap.entrySet()) {
				for (Entry<String, Double> yesterday : yesterdayMap.entrySet()) {
					if (today.getKey().equalsIgnoreCase(yesterday.getKey())) {
						// Barrels difference between today and yesterday for oil and water 
						diffMap.put(today.getKey(),
								(yesterday.getValue() - today.getValue()));
						break;
					}
				}
			}
			ArrayList<String> keysPresent = new ArrayList<String>();
			for (Entry<String, Double> diffSet : diffMap.entrySet()) {
				if(!keysPresent.contains(diffSet.getKey())){
					String muwi = diffSet.getKey().split("_")[0];
					String tag = diffSet.getKey().split("_")[1];
					if (tag.equalsIgnoreCase("QTOILD")){
						oil_var = diffSet.getValue();
						water_var = diffMap.get((muwi +"_QTH2OD"));
						keysPresent.add((muwi +"_QTH2OD"));
					}
					else {
						water_var = diffSet.getValue();
						oil_var = diffMap.get((muwi +"_QTOILD"));
						keysPresent.add((muwi +"_QTOILD"));
					}
					String QueryLocation = "SELECT wm.location_code,tier FROM WELL_TIER AS wt INNER JOIN WELL_MUWI AS wm "
							+ "ON wt.location_code = wm.location_code WHERE muwi = '"+ muwi +"'";
					Query q = this.getSession().createSQLQuery(QueryLocation);
					//logger.error("[createItaWaterOil][QueryLocation]: " + QueryLocation);
					List<Object[]> objList = (List<Object[]>) q.list();
					if (!ServicesUtil.isEmpty(objList)) {
						for (Object[] obj : objList) {
							locCode  = ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0];
							wellTier = ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1];
						}
					}
					
					if(tier.equalsIgnoreCase(MurphyConstant.TIER_C) && wellTier.equalsIgnoreCase(tier))
					{
						if(yesterdayMap.get((muwi +"_QTH2OD")) == 0 && 
								(todayMap.get((muwi +"_QTOILD")) >= 1.5*(daysAvgMap.get((muwi +"_QTOILD"))))){
							description = "This task is created for Water carry over";
							// Create task
							responseMessage = createTask(locCode,classification, subClassification,taskType,description);
							logger.error("[Murphy][ItaWaterOilDao][createItaWaterOil][createdTask for TierC byAvg]"
									+ "[description] " + description + " ForMuwi : " + muwi);
						}
						else if(!ServicesUtil.isEmpty(water_var) && !ServicesUtil.isEmpty(oil_var)){
							if((water_var < -(waterThreshold)) && (oil_var > oilThreshold)){
								description = "This task is created for Oil carry over";
								// Create task
								responseMessage = createTask(locCode,classification, subClassification,taskType,description);
							}
							if((oil_var < -(oilThreshold)) && (water_var > waterThreshold)){
								description = "This task is created for Water carry over";
								// Create task
								responseMessage = createTask(locCode,classification, subClassification,taskType,description);
							}
							logger.error("[Murphy][ItaWaterOilDao][createItaWaterOil]"
									+ "[createdTask for TierC][description] " + description + " ForMuwi : " + muwi);
						}
						else{
							responseMessage.setStatus(MurphyConstant.SUCCESS);
							responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
							responseMessage.setMessage("Either Oil or water value is missing for MuwiID or could not pass 50% threshold");
						}
						logger.error("Muwi "+muwi +" well tier : "+ wellTier + " location_code : "+ locCode + "yesterday waterValue " + yesterdayMap.get((muwi +"_QTH2OD"))
						+ " today waterValue " + todayMap.get((muwi +"_QTH2OD")) +" yesterday Oilvalue " + yesterdayMap.get((muwi +"_QTOILD"))  
						+ " today OilValue " + todayMap.get((muwi +"_QTOILD")) + "7 days average of oil: " + daysAvgMap.get((muwi +"_QTOILD")));
					}
					else 
					{
						if(!ServicesUtil.isEmpty(water_var) && !ServicesUtil.isEmpty(oil_var) && wellTier.equalsIgnoreCase(tier)){
							if((water_var < -(waterThreshold)) && (oil_var > oilThreshold)){
								description = "This task is created for Oil carry over";
								// Create task
								responseMessage = createTask(locCode,classification, subClassification,taskType,description);
							}
							if((oil_var < -(oilThreshold)) && (water_var > waterThreshold)){
								description = "This task is created for Water carry over";
								// Create task
								responseMessage = createTask(locCode,classification, subClassification,taskType,description);
								
							}
							logger.error("[Murphy][ItaWaterOilDao][createItaWaterOil][createdTask]"
									+ "[description] " + description + " ForMuwi : " + muwi);
						}
						else{
							responseMessage.setStatus(MurphyConstant.SUCCESS);
							responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
							responseMessage.setMessage("Either Oil or water value is missing for MuwiID");
						}
						logger.error("Muwi "+muwi +" well tier : "+ wellTier + " location_code : "+ locCode + "yesterday waterValue " + yesterdayMap.get((muwi +"_QTH2OD"))
						+ " today waterValue " + todayMap.get((muwi +"_QTH2OD")) +" yesterday Oilvalue " + yesterdayMap.get((muwi +"_QTOILD"))  
						+ " today OilValue " + todayMap.get((muwi +"_QTOILD")) + "7 days average of oil: " + daysAvgMap.get((muwi +"_QTOILD"))
						+ " description : " + description);
					}
				}	
			}
			
		} catch (Exception e) {
			logger.error("[Murphy][ItaWaterOilDao][createItaWaterOil][error]" + e.getMessage());
			responseMessage.setMessage(MurphyConstant.READ_FAILURE);
			responseMessage.setStatus(MurphyConstant.FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return responseMessage;

	}
	
	public ResponseMessage createTask(String loc_code,String classification,String subClassification, 
		String taskType, String description) {
		
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
				logger.error("[Murphy][ItaWaterOilDao][createTask][field]" + field);

				List<String> roles = new ArrayList<>();
				roles.add("ROC_" + field.trim());
				logger.error("[ItaWaterOilDao][Role]: " + roles);

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
				logger.error("[ItaWaterOilDao][Owner]: " + owner.toString());

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
				taskEventsDto.setParentOrigin(MurphyConstant.P_ITA); 
				taskEventsDto.setStatus(MurphyConstant.DRAFT);
				taskEventsDto.setSubClassification(subClassification);
				taskEventsDto.setSubject(classification +" / " + subClassification);
				taskEventsDto.setGroup(group);
				taskEventsDto.setTaskType("SYSTEM");
				taskEventsDto.setDescription(description);
				customTaskDto.setTaskEventDto(taskEventsDto);

				logger.error("[Murphy][ItaWaterOilDao][createTask][Create task on location] " +loc_code );
				response =  taskSchedulingService.createTask(customTaskDto);
    		}
    		else
    		{
    			response.setMessage("Unable to create the ITA Task");
				response.setStatus(MurphyConstant.FAILURE);
				response.setStatusCode(MurphyConstant.CODE_FAILURE);
    		}
    	  }
    	  catch(Exception e)
    	{
    		  logger.error("[Murphy][ItaWaterOilDao][createTask][Exception]" + e.getMessage());
    	}
    	return response;	
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getLocCode(List<String> gasBlowByMuwiList) {

		String muwiList = ServicesUtil.getStringFromList(gasBlowByMuwiList);
		Map<String,String>locCodeMuwiMap = new HashMap<>();
		try {

			String queryString = "select location_code,muwi from well_muwi where muwi in(" + muwiList + ")";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = q.list();

			for (Object[] obj : response) {
				
				if(!ServicesUtil.isEmpty(obj[0]))
				{
					locCodeMuwiMap.put(obj[0].toString(), obj[1].toString());
				}
				
				//locCodeList.add(locCode);
			}

			return locCodeMuwiMap;
		} catch (Exception e) {
			logger.error("[Murphy][ItaOilWaterDao][getLocCode][error]" + e.getMessage());
			e.printStackTrace();
		}
		return null;

	}

	public Map<String,String> geConfigValuesForGasBlowby() {
		Map<String,String>configMap = new HashMap<>();
		
		try {

			String queryString = "select  config_id,config_desc_value from tm_config_values where config_id like 'GASBLOWBY%'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = q.list();

			for (Object[] obj : response) {
				
				if(!ServicesUtil.isEmpty(obj[0]))
				{
					configMap.put(obj[0].toString(), obj[1].toString());
				}
				
				//locCodeList.add(locCode);
			}

			return configMap;
		} catch (Exception e) {
			logger.error("[Murphy][ItaOilWaterDao][getLocCode][error]" + e.getMessage());
			e.printStackTrace();
		}
		return null;

	}

	public Map<Integer, GasBlowByDto> getGasBlowbyValuesForTest(String oilMeterMerrickId, String gasMeterMerrickId, String meterName, Double dailyOilValue) {
		Map<Integer, GasBlowByDto> oilGasMap = new HashMap<>();
		GasBlowByDto blowByDto = null;
		
		String meterNamesOil=getLikeQueryForGasBlowByOil(meterName);
		String meterNamesGas=getLikeQueryForGasBlowByGas(meterName);

		try {

			String queryString = "select * from gasblowby_temp where " + meterNamesOil + " and " + meterNamesGas
					+ " and daily_oil> " + dailyOilValue + " and oil_meter_mid not in(" + oilMeterMerrickId + ") and "
					+ "gas_meter_mid not in (" + gasMeterMerrickId + ")";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = q.list();

			for (Object[] obj : response) {

				blowByDto = new GasBlowByDto();

				blowByDto.setWellName(ServicesUtil.isEmpty(obj[0]) ? null : obj[0].toString());
				blowByDto.setMerrickID(ServicesUtil.isEmpty(obj[1]) ? null : Integer.parseInt(obj[1].toString()));
				blowByDto.setOilMeterName(ServicesUtil.isEmpty(obj[2]) ? null : obj[2].toString());
				blowByDto.setOilMeterMID(ServicesUtil.isEmpty(obj[3]) ? null : Integer.parseInt(obj[3].toString()));
				blowByDto.setGasMeterName(ServicesUtil.isEmpty(obj[4]) ? null : obj[4].toString());
				blowByDto.setGasMeterMID(ServicesUtil.isEmpty(obj[5]) ? null : Integer.parseInt(obj[5].toString()));

				if (!ServicesUtil.isEmpty(obj[6])) {
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
					blowByDto.setRecordDate(d.parse(obj[6].toString().split("\\s+")[0]));

				} else {
					blowByDto.setRecordDate(null);
				}

				blowByDto.setTotEstGasVol(ServicesUtil.isEmpty(obj[7]) ? null : Double.parseDouble(obj[7].toString()));
				blowByDto.setTotEstOilVol(ServicesUtil.isEmpty(obj[8]) ? null : Double.parseDouble(obj[8].toString()));
				blowByDto.setEstOilVol(ServicesUtil.isEmpty(obj[9]) ? null : Double.parseDouble(obj[9].toString()));
				blowByDto.setEstGasVol(ServicesUtil.isEmpty(obj[10]) ? null : Double.parseDouble(obj[10].toString()));
				blowByDto.setDayCount(ServicesUtil.isEmpty(obj[11]) ? null : (obj[11].toString()));
				blowByDto
						.setAvgYearlyGas(ServicesUtil.isEmpty(obj[12]) ? null : Double.parseDouble(obj[12].toString()));
				// blowByDto.setEstGasVol(ServicesUtil.isEmpty(obj[13])?null:Double.parseDouble(obj[13].toString()));
				blowByDto.setYearlyOilGasRatio(
						ServicesUtil.isEmpty(obj[14]) ? null : Double.parseDouble(obj[14].toString()));
				blowByDto.setDailyOilGasRatio(
						ServicesUtil.isEmpty(obj[15]) ? null : Double.parseDouble(obj[15].toString()));
				// blowByDto.(ServicesUtil.isEmpty(obj[16])?null:Double.parseDouble(obj[16].toString()));
				
				blowByDto.setMuwiId(ServicesUtil.isEmpty(obj[17])?null:obj[17].toString());
				
				oilGasMap.put(blowByDto.getMerrickID(), blowByDto);
			}

			return oilGasMap;
		} catch (Exception e) {
			logger.error("[Murphy][ItaOilWaterDao][getLocCode][error]" + e.getMessage());
			e.printStackTrace();
		}
		return null;

	}
	
	 private static String getLikeQueryForGasBlowByOil(String meterName)
	    {
	        String input[]=meterName.split(",");
	        String returnString="";
	        int c=0;
	        for(String s:input)
	        {
	            c++;
	            if(c<=input.length-1)
	            returnString=returnString+"oil_meter not like '%"+s+"%' and ";
	            else
	            returnString=returnString+"oil_meter not like '%"+s+"%'";
	        }
	        
	        return returnString.substring(0,returnString.length());
	    }
	 
	 private static String getLikeQueryForGasBlowByGas(String meterName)
	    {
	        String input[]=meterName.split(",");
	        String returnString="";
	        int c=0;
	        for(String s:input)
	        {
	            c++;
	            if(c<=input.length-1)
	            returnString=returnString+"gas_meter not like '%"+s+"%' and ";
	            else
	            returnString=returnString+"gas_meter not like '%"+s+"%'";
	        }
	        
	        return returnString.substring(0,returnString.length());
	    }


}
