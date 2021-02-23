package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomAttrValuesDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.StopTimeDto;
import com.murphy.taskmgmt.entity.CustomAttrTemplateDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("CustomAttrTemplateDao")
@Transactional
public class CustomAttrTemplateDao extends BaseDao<CustomAttrTemplateDo, CustomAttrTemplateDto> {

	private static final Logger logger = LoggerFactory.getLogger(CustomAttrTemplateDao.class);
	
	static SimpleDateFormat utcFormatter = new SimpleDateFormat("dd-MMM-yyyy, hh:mm:ss a");

	public CustomAttrTemplateDao() {
	}

	@Autowired
	private CustomAttrValuesDao valueDao;

	@Autowired
	HierarchyDao hierarchyDao;

	@Override
	protected CustomAttrTemplateDo importDto(CustomAttrTemplateDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		CustomAttrTemplateDo entity = new CustomAttrTemplateDo();
		if (!ServicesUtil.isEmpty(fromDto.getClItemId()))
			entity.setClItemId(fromDto.getClItemId());
		if (!ServicesUtil.isEmpty(fromDto.getTaskTempId()))
			entity.setTaskTempId(fromDto.getTaskTempId());
		if (!ServicesUtil.isEmpty(fromDto.getLabel()))
			entity.setLabel(fromDto.getLabel());
		if (!ServicesUtil.isEmpty(fromDto.getDataType()))
			entity.setDataType(fromDto.getDataType());
		if (!ServicesUtil.isEmpty(fromDto.getIsMandatory()))
			entity.setIsMandatory(fromDto.getIsMandatory());
		if (!ServicesUtil.isEmpty(fromDto.getMaxLength()))
			entity.setMaxLength(fromDto.getMaxLength());
		if (!ServicesUtil.isEmpty(fromDto.getIsDefault()))
			entity.setIsDefault(fromDto.getIsDefault());
		if (!ServicesUtil.isEmpty(fromDto.getSeqNumber()))
			entity.setSeqNumber(fromDto.getSeqNumber());
		if (!ServicesUtil.isEmpty(fromDto.getShortDesc()))
			entity.setShortDesc(fromDto.getShortDesc());
		if (!ServicesUtil.isEmpty(fromDto.getIsEditable()))
			entity.setIsEditable(fromDto.getIsEditable());
		if (!ServicesUtil.isEmpty(fromDto.getIsUpdatable()))
			entity.setIsUpdatable(fromDto.getIsUpdatable());

		/*
		 * if (!ServicesUtil.isEmpty(fromDto.getServiceUrl()))
		 * entity.setServiceUrl(fromDto.getServiceUrl()); if
		 * (!ServicesUtil.isEmpty(fromDto.getDataPath()))
		 * entity.setDataPath(fromDto.getDataPath());
		 */

		return entity;
	}

	@Override
	protected CustomAttrTemplateDto exportDto(CustomAttrTemplateDo entity) {

		CustomAttrTemplateDto dto = new CustomAttrTemplateDto();

		if (!ServicesUtil.isEmpty(entity.getClItemId()))
			dto.setClItemId(entity.getClItemId());
		if (!ServicesUtil.isEmpty(entity.getTaskTempId()))
			dto.setTaskTempId(entity.getTaskTempId());
		if (!ServicesUtil.isEmpty(entity.getLabel()))
			dto.setLabel(entity.getLabel());
		if (!ServicesUtil.isEmpty(entity.getDataType()))
			dto.setDataType(entity.getDataType());
		if (!ServicesUtil.isEmpty(entity.getIsMandatory()))
			dto.setIsMandatory(entity.getIsMandatory());
		if (!ServicesUtil.isEmpty(entity.getMaxLength()))
			dto.setMaxLength(entity.getMaxLength());
		if (!ServicesUtil.isEmpty(entity.getIsDefault()))
			dto.setIsDefault(entity.getIsDefault());
		if (!ServicesUtil.isEmpty(entity.getSeqNumber()))
			dto.setSeqNumber(entity.getSeqNumber());
		if (!ServicesUtil.isEmpty(entity.getShortDesc()))
			dto.setShortDesc(entity.getShortDesc());
		if (!ServicesUtil.isEmpty(entity.getIsEditable()))
			dto.setIsEditable(entity.getIsEditable());
		if (!ServicesUtil.isEmpty(entity.getIsUpdatable()))
			dto.setIsUpdatable(entity.getIsUpdatable());
		if (!ServicesUtil.isEmpty(entity.getDependentOn()))
			dto.setDependentOn(entity.getDependentOn());

		/*
		 * if (!ServicesUtil.isEmpty(entity.getServiceUrl()))
		 * dto.setServiceUrl(entity.getServiceUrl()); if
		 * (!ServicesUtil.isEmpty(entity.getDataPath()))
		 * dto.setDataPath(entity.getDataPath());
		 */

		return dto;
	}

	public ResponseMessage createCLTemp(CustomAttrTemplateDto dto) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			String clItemId = UUID.randomUUID().toString().replaceAll("-", "");
			dto.setClItemId(clItemId);

			create(dto);

			for (CustomAttrValuesDto optionDto : dto.getValueDtos()) {
				optionDto.setClItemId(clItemId);
				ResponseMessage errorReturn = valueDao.createCLOption(optionDto);
				if (!errorReturn.getStatus().equals(MurphyConstant.SUCCESS)) {
					return errorReturn;
				}
			}
			responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Murphy][TaskManagement][CheckListTemplateDao][createCLTemp][error]" + e.getMessage());
		}
		return responseMessage;
	}

	@SuppressWarnings("unchecked")
	public List<CustomAttrTemplateDto> getCustomHeadersbyTaskTempId(String taskTemplateId, String location,
			String classification, String serviceType, String taskId, String locationCode, String compressorName,
			String userType) {
		List<CustomAttrTemplateDto> responseDto = null;
		String queryString = "select distinct clt.ATTR_ID AS ITEM_ID,clt.LABEL AS LABEL,clt.DATA_TYPE AS DATA_TYPE,clt.IS_MAND AS IS_MAND"
				+ ",clt.MAX_LENGTH As MAX_LENGTH,clt.SEQ_NO AS SEQ_NO"
				+ ",clt.SHORT_DESC AS SHORT_DESC ,clt.IS_EDITABLE AS IS_EDITABLE ,clt.SERVICE_URL AS SERVICE_URL ,clt.IS_DEFAULT AS IS_DEFAULT ,clt.DATA_PATH AS DATA_PATH"
				+ ",clo.ATTR_VALUE AS ATTR_VALUE,clo.DEPENDENT_VALUE AS DEPENDENT_VALUE, clt.DEPENDENT_ON AS DEPENDENT_ON ,clt.TASK_TEMP_ID as temp_id";
		if (MurphyConstant.READ.equals(serviceType)) {
			queryString = queryString + " , ins.INS_VALUE as ins_value ";
		}
		queryString = queryString
				+ " from TM_ATTR_TEMP clt LEFT OUTER JOIN TM_ATTR_VALUES clo ON  clt.ATTR_ID  = clo.ITEM_ID ";
		if (MurphyConstant.READ.equals(serviceType)) {
			queryString = queryString
					+ "LEFT OUTER JOIN TM_ATTR_INSTS ins ON   clt.ATTR_ID = ins.ATTR_TEMP_ID where ins.task_id in ('"
					+ taskId + "') and ";
		} else {
			queryString = queryString + " where ";
		}
		queryString = queryString + " clt.TASK_TEMP_ID in ('" + taskTemplateId + "') order by 6 asc";

		// logger.error("[Murphy][TaskManagement][getCustomHeadersByTaskTempIdNEW][size][queryString]"
		// + queryString);

		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> resultList = (List<Object[]>) q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			responseDto = new ArrayList<CustomAttrTemplateDto>();
			Map<String, Integer> locationMap = new HashMap<String, Integer>();
			for (Object[] obj : resultList) {
				String listItem = (String) obj[0];
				if (locationMap.containsKey(listItem)) {
					if ((!MurphyConstant.CLASSIFICATION.equals((String) obj[1]))
							|| (ServicesUtil.isEmpty(classification))
							|| (MurphyConstant.CLASSIFICATION.equals((String) obj[1])
									&& !ServicesUtil.isEmpty(classification)
									&& !classification.equals((String) obj[11]))) {
						CustomAttrValuesDto valueDto = new CustomAttrValuesDto();
						valueDto.setValue(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
						valueDto.setDependentValue(ServicesUtil.isEmpty(obj[12]) ? null : (String) obj[12]);
						responseDto.get(locationMap.get(listItem)).getValueDtos().add(valueDto);
					}
				} else {
					CustomAttrTemplateDto dto = null;
					dto = getCustomAttrDto(obj, location, classification, serviceType, locationCode, compressorName);
					if (MurphyConstant.TEMP_ID_OBSERVATION.equals(taskTemplateId)
							&& MurphyConstant.USER_TYPE_ENG.equals(userType)) {
						dto.setIsEditable(false);
					}//SOC: Addition when user has both POT and ENG role for CANADA Only
					else if(MurphyConstant.USER_TYPE_ENG_POT.equals(userType))
						dto.setIsEditable(true);
					//EOC: Addition when user has both POT and ENG role for CANADA Only
					if (dto.getLabelValue() != null && dto.getLabel() != null && dto.getLabel().equals("Start Date") && dto.getClItemId().equals("123456789")) {
						try {
							Date utcDate = utcFormatter.parse(dto.getLabelValue());
							dto.setDateValue(utcDate);
						} catch (ParseException e) {
							logger.error("[getCustomHeadersbyTaskTempId] Error in parsing date5: " + e.toString());
						}
					}
					responseDto.add(dto);
					locationMap.put(listItem, responseDto.size() - 1);
				}
			}
		}
		return responseDto;
	}

	private CustomAttrTemplateDto getCustomAttrDto(Object[] obj, String location, String classification,
			String serviceType, String locationCode, String compressorName) {

		CustomAttrTemplateDto clDto = new CustomAttrTemplateDto();
		CustomAttrValuesDto valueDto = new CustomAttrValuesDto();
		clDto.setValueDtos(new ArrayList<CustomAttrValuesDto>());
		clDto.setClItemId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
		clDto.setLabel(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
		clDto.setDataType(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
		clDto.setIsMandatory(ServicesUtil.isEmpty(obj[3]) ? null : (((byte) obj[3]) != 0));
		clDto.setMaxLength(ServicesUtil.isEmpty(obj[4]) ? null : (Integer) obj[4]);
		clDto.setSeqNumber(ServicesUtil.isEmpty(obj[5]) ? null : (Integer) obj[5]);
		clDto.setShortDesc(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
		clDto.setIsEditable(ServicesUtil.isEmpty(obj[7]) ? null : (((byte) obj[7]) != 0));
		clDto.setIsDefault(ServicesUtil.isEmpty(obj[9]) ? null : (((byte) obj[9]) != 0));
		clDto.setDependentOn(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
		clDto.setTaskTempId(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);

		// clDto.setServiceUrl(ServicesUtil.isEmpty(obj[8]) ? null : (String)
		// obj[8]);
		// clDto.setDataPath(ServicesUtil.isEmpty(obj[10]) ? null : (String)
		// obj[10]);

		if (!ServicesUtil.isEmpty(classification) && MurphyConstant.CLASSIFICATION.equals(clDto.getLabel())) {
			CustomAttrValuesDto classificationDto = new CustomAttrValuesDto();
			classificationDto.setValue(classification);
			clDto.getValueDtos().add(classificationDto);
			clDto.setLabelValue(classification);
		}
		if (ServicesUtil.isEmpty(classification)
				|| (!ServicesUtil.isEmpty(classification) && !classification.equals((String) obj[11]))) {
			valueDto.setValue(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
			valueDto.setDependentValue(ServicesUtil.isEmpty(obj[12]) ? null : (String) obj[12]);
			clDto.getValueDtos().add(valueDto);
		}

		if (MurphyConstant.READ.equals(serviceType) && !MurphyConstant.ASSIGNEDTO.equals(clDto.getLabel())
				&& !MurphyConstant.ND_ASSIGNEDTO_GRP.equals(clDto.getLabel())) {
			clDto.setLabelValue(ServicesUtil.isEmpty(obj[15]) ? null : (String) obj[15]);
		} else {
			if (MurphyConstant.LOCATION.equals(clDto.getLabel())) {
				clDto.setLabelValue(location);
			} else if (MurphyConstant.TIER.equals(clDto.getLabel())) {
				clDto.setLabelValue(hierarchyDao.getTierByCode(locationCode));
			} else if (ServicesUtil.isEmpty(clDto.getLabel()) && !ServicesUtil.isEmpty(compressorName)) {
				clDto.setLabel(MurphyConstant.COMPRESSOR);
				clDto.setLabelValue(compressorName);
			}
		}

		return clDto;
	}
	
	public String getTaskClassification(String taskId){
		
		String classification = "";
		
		try{
			
			String queryString ="select INS_VALUE from TM_ATTR_INSTS i inner join iop.tm_task_evnts t "
					+ "on i.task_id = t.task_id where i.ATTR_TEMP_ID = '12345' and t.task_id = '"+taskId+ "'";
			Query q = this.getSession().createSQLQuery(queryString);
			classification = q.uniqueResult().toString();
			return classification;
			
		}catch(Exception e){
			
			logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getClassification][error]" + e.getMessage());
			
		}
		
		return classification;
		
	}

	public float getEstTimeForSubClass(String classification , String subClasification, String userId) {
		float response = 20;
		try {
			String role = getUserRole(userId);
			StopTimeDto timeSet = getStopTimeByCategory(classification,subClasification);
			
			if (!ServicesUtil.isEmpty(timeSet) && !ServicesUtil.isEmpty(role)) {
				
				if(role.equalsIgnoreCase(MurphyConstant.PRO_A)){
					response = timeSet.getProA();
				}
				if(role.equalsIgnoreCase(MurphyConstant.PRO_B)){
					response = timeSet.getProB();
				}
				if(role.equalsIgnoreCase(MurphyConstant.OBX_B)){
					response = timeSet.getObx();
				}
				if(role.equalsIgnoreCase(MurphyConstant.OBX_C)){
					response = timeSet.getObx();
				}
			}
			logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getEstTimeForSubClass][response] :" +response);

		} catch (Exception e) {
			logger.error(
					"[Murphy][TaskManagement][CustomAttrInstancesDao][getEstTimeForSubClass][error]" + e.getMessage());

		}
		return response;
	}
	
	public  String getUserRole(String userId){
		
		String role ="";
		
		try{
			
			String queryString ="select designation from emp_info where emp_email='"+userId+"'";
			Query q = this.getSession().createSQLQuery(queryString);
		    role = q.list().get(0).toString();
					
		}catch(Exception e){
			logger.error(
					"[Murphy][TaskManagement][CustomAttrInstancesDao][getUserRole][error]" + e.getMessage());
		}
		
		return role;
		
	}
	
	public StopTimeDto getStopTimeByCategory(String classification, String subClassification) {
		StopTimeDto timeSet = null;
		String queryString = "select PRO_A, PRO_B, OBX, SSE from TM_TASK_STOP_TIME_BY_ROLE where CLASSIFICATION = '" + classification + "' AND "
				+ "SUB_CLASSIFICATION = '" + subClassification + "'";
		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> response = (List<Object[]>) q.list();
		if (!ServicesUtil.isEmpty(response)) {
			timeSet = new StopTimeDto();
			for (Object[] obj : response) {
				timeSet.setProA(ServicesUtil.isEmpty(obj[0]) ? null : (int) obj[0]);
				timeSet.setProB(ServicesUtil.isEmpty(obj[1]) ? null : (int) obj[1]);
				timeSet.setObx(ServicesUtil.isEmpty(obj[2]) ? null : (int) obj[2]);
				timeSet.setSse(ServicesUtil.isEmpty(obj[3]) ? null : (int) obj[3]);
			}
			timeSet.setClassification(classification);
			timeSet.setSubClassification(subClassification);
		}
		return timeSet;
	}
	
	//This method retrieves pig retrieval time from previous task to use while reassigning an in progress pig launch task -INC0099761 
		public String getPigRetrievalofPrevTask(String originalTaskId) {
			String query = "select INS_VALUE from TM_ATTR_INSTS" + " where task_id = '" + originalTaskId
					+ "' and ATTR_TEMP_ID = 'PIG001'";

			String result = "";

			Object obj1 = this.getSession().createSQLQuery(query).uniqueResult();
			if (!ServicesUtil.isEmpty(obj1)) {
				if (!ServicesUtil.isEmpty(obj1.toString())) {
					result = obj1.toString();
				}
			}

			return result;
		}
		
		// Get values for assign to group field (in header) while creating inquiry - CHG0037344-Inquiry to a field seat.
		public List<String> getGroupRoles(String field,String userType){
			List<String> groups = null; 
			String queryString = null,businessRole = null;
			try{
				if (userType.equals(MurphyConstant.USER_TYPE_ENG))
					businessRole = "BUSINESSEROLE like '%ROC%' OR BUSINESSEROLE like '%POT%'";
				else if(userType.equals(MurphyConstant.USER_TYPE_ROC))
					businessRole = "BUSINESSEROLE like '%POT%'";
				else if(userType.equals(MurphyConstant.USER_TYPE_POT))
					businessRole = "BUSINESSEROLE like '%ROC%'";
				
				queryString = "select distinct BUSINESSEROLE from TM_ROLE_MAPPING where (" + businessRole + ") and "
						+ "FIELD like '" + field + "%'";
				logger.error("Query for finding group dropdown " + queryString);
				Query q = this.getSession().createSQLQuery(queryString);
				List<String> response = (List<String>) q.list();
				if (!ServicesUtil.isEmpty(response)) {
					groups = new ArrayList<String>();
					for (String group : response) {
						if (group != null && group.length() != 0 && group != null)
							groups.add(group);
					}
				}
				logger.error("groups are " + groups.toString());
			}catch(Exception e){
				logger.error(
						"[Murphy][TaskManagement][CustomAttrInstancesDao][getGroupRoles][error]" + e.getMessage());
			}
			return groups;
		}

}
