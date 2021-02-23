package com.murphy.taskmgmt.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.CustomAttrInstancesDto;
import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomAttrValuesDto;
import com.murphy.taskmgmt.dto.FieldResponseDto;
import com.murphy.taskmgmt.dto.RootCauseDto;
import com.murphy.taskmgmt.dto.TaskSubmittedDto;
import com.murphy.taskmgmt.entity.CustomAttrInstancesDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("customAttrInstancesDao")
@Transactional
public class CustomAttrInstancesDao extends BaseDao<CustomAttrInstancesDo, CustomAttrInstancesDto> {

	private static final Logger logger = LoggerFactory.getLogger(CustomAttrInstancesDao.class);
	static SimpleDateFormat utcFormatter = new SimpleDateFormat("dd-MMM-yyyy, hh:mm:ss a");

	public CustomAttrInstancesDao() {
	}

	@Autowired
	private CustomAttrTemplateDao attrTempDao;
	
	@Autowired
	private HierarchyDao locDao;

	@Autowired
	private ConfigDao configDao;

	@Override
	protected CustomAttrInstancesDo importDto(CustomAttrInstancesDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		CustomAttrInstancesDo entity = new CustomAttrInstancesDo();
		if (!ServicesUtil.isEmpty(fromDto.getClId()))
			entity.setClId(fromDto.getClId());
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			entity.setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getAttrId()))
			entity.setAttrId(fromDto.getAttrId());
		if (!ServicesUtil.isEmpty(fromDto.getValue()))
			entity.setValue(fromDto.getValue());

		return entity;
	}

	@Override
	protected CustomAttrInstancesDto exportDto(CustomAttrInstancesDo entity) {

		CustomAttrInstancesDto dto = new CustomAttrInstancesDto();
		if (!ServicesUtil.isEmpty(entity.getClId()))
			dto.setClId(entity.getClId());
		if (!ServicesUtil.isEmpty(entity.getTaskId()))
			dto.setTaskId(entity.getTaskId());
		if (!ServicesUtil.isEmpty(entity.getAttrId()))
			dto.setAttrId(entity.getAttrId());
		if (!ServicesUtil.isEmpty(entity.getValue()))
			dto.setValue(entity.getValue());

		return dto;
	}

	public String createCLInstance(CustomAttrInstancesDto dto) {
		String response = MurphyConstant.FAILURE;
		try {
			create(dto);
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][CheckListInstancesDao][createCLInstance][error]" + e.getMessage());
		}
		return response;

	}

	@SuppressWarnings("unchecked")
	public List<CustomAttrTemplateDto> getCustomAttrIntancesbyId(String taskId, String status, String taskTempId,
			String userType, Boolean potAssignedUser, Date updatedAt) {
		Date piggingTime = null;
		String queryString = "Select temp.DATA_TYPE,temp.LABEL,temp.SEQ_NO,ins.INS_VALUE,temp.IS_MAND,temp.IS_UPDATABLE,temp.ATTR_ID from TM_ATTR_INSTS ins inner join TM_ATTR_TEMP temp on temp.ATTR_ID = ins.ATTR_TEMP_ID where ins.TASK_ID in( '"
				+ taskId + "') ";

		if (!ServicesUtil.isEmpty(taskTempId) && !("PROC_PIG").equalsIgnoreCase(taskTempId)) {
			queryString = queryString + "and temp.TASK_TEMP_ID in ('" + taskTempId + "')";
		}
		queryString = queryString + " ORDER BY 3";

		// logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getCustomAttrIntancesbyId][queryString]"+queryString);

		try {
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				List<CustomAttrTemplateDto> responseDto = new ArrayList<CustomAttrTemplateDto>();
				CustomAttrTemplateDto dto = null;
				for (Object[] obj : resultList) {
					dto = new CustomAttrTemplateDto();
					dto.setDataType(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setLabel(ServicesUtil.isEmpty(obj[1]) ? null : ((String) obj[1]));
					dto.setSeqNumber(ServicesUtil.isEmpty(obj[2]) ? null : (Integer) obj[2]);
					dto.setClItemId(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
					if (!MurphyConstant.STATUS.equals(dto.getLabel())) {
						dto.setLabelValue(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
						if (obj[1] != null && obj[3] != null && "Start Date".equals(dto.getLabel()) && dto.getClItemId().equals("123456789")) {
							Date utcDate = utcFormatter.parse(obj[3].toString());
							dto.setDateValue(utcDate);
						}
					} else {
						dto.setLabelValue(status);
					}

					/*
					 * if ((MurphyConstant.ASSIGNEDTO).equalsIgnoreCase(dto.
					 * getLabel()) && "PROC_PIG".equalsIgnoreCase(taskTempId))
					 * dto.setLabelValue(null);
					 */
					if (ServicesUtil.isEmpty(dto.getLabel()) && !ServicesUtil.isEmpty(dto.getLabelValue())) {
						dto.setLabel(MurphyConstant.COMPRESSOR);
					}

					System.err.println("custom Id:" + dto.getClItemId() + " UpdateAd:" + updatedAt);
					// For Pigging Launch task
					if ("PIG001".equalsIgnoreCase(dto.getClItemId()) && !ServicesUtil.isEmpty(updatedAt)) {
						Double mins = Double.parseDouble(dto.getLabelValue());
						System.err.println("Pigging time calculation:" + mins);
						if ("RESOLVED".equalsIgnoreCase(status)) {
							piggingTime = ServicesUtil.getDateWithInterval(updatedAt, mins.intValue(),
									MurphyConstant.MINUTES);
						} else {
							piggingTime = ServicesUtil.getDateWithInterval(new Date(), mins.intValue(),
									MurphyConstant.MINUTES);
						}

						dto.setLabelValue(ServicesUtil.isEmpty(piggingTime) ? null
								: ServicesUtil.convertFromZoneToZoneString(piggingTime, null, MurphyConstant.UTC_ZONE,
										MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
										MurphyConstant.PIG_DATE_DISPLAY_FORMAT));

					}
					CustomAttrValuesDto valueDto = new CustomAttrValuesDto();
					valueDto.setValue(dto.getLabelValue());
					dto.setValueDtos(new ArrayList<CustomAttrValuesDto>());
					dto.getValueDtos().add(valueDto);

					if ((MurphyConstant.USER_TYPE_POT.equals(userType) && potAssignedUser.equals(true))
							|| MurphyConstant.COMPLETE.equals(status)) {
						dto.setIsEditable(false);
						dto.setIsEnabled(false);
					} else if ((MurphyConstant.RETURN.equals(status) || MurphyConstant.ASSIGN.equals(status) || MurphyConstant.REASSIGN.equals(status))
							&& !MurphyConstant.USER_TYPE_ENG.equals(userType)) {
						dto.setIsEditable(ServicesUtil.isEmpty(obj[5]) ? null : (((byte) obj[5]) != 0));
						dto.setIsEnabled(ServicesUtil.isEmpty(obj[5]) ? null : (((byte) obj[5]) != 0));
					} 
					//SOC: Addition when user has both POT and ENG role for CANADA Only
					else if(MurphyConstant.USER_TYPE_ENG_POT.equalsIgnoreCase(userType)){
						dto.setIsEditable(false);
						dto.setIsEnabled(false);
					}//EOC: Addition when user has both POT and ENG role for CANADA Only
					else{
						dto.setIsEditable(false);
						dto.setIsEnabled(false);
					}
					dto.setIsMandatory(ServicesUtil.isEmpty(obj[4]) ? null : (((byte) obj[4]) != 0));
					// For Pigging Launch editable=false
					if ("PIG001".equalsIgnoreCase(dto.getClItemId()) && !ServicesUtil.isEmpty(updatedAt))
						dto.setIsEditable(false);
					dto.setIsEnabled(false);

					responseDto.add(dto);
				}
				return responseDto;
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getCustomAttrIntancesbyId][error]"
					+ e.getMessage());
		}
		return null;
	}

	public String setAttrValueTo(String taskId, String taskOwner, String attrId) {

		String queryString = "Update TM_ATTR_INSTS set INS_VALUE = '" + taskOwner + "'  where ATTR_TEMP_ID in ('"
				+ attrId + "') and TASK_ID='" + taskId + "'";
		try {
			Query q = this.getSession().createSQLQuery(queryString);
			q.executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][setAssignedTo][error]" + e.getMessage());
		}
		return MurphyConstant.SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public List<RootCauseDto> getRootCause(String taskId, String status, String origin) {
		List<RootCauseDto> responseList = null;
		String subClassification;
		if (!MurphyConstant.INVESTIGATON.equals(origin)) {
			subClassification = ServicesUtil.getStringForInQuery(getSubClassification(taskId));
		} else {
			subClassification = "'" + MurphyConstant.PRIMARY_CLASSIFICATION + "','"
					+ MurphyConstant.SECONDARY_CLASSIFICATION + "'";
		}

		try {
			String queryString = "SELECT rt.SUB_CLASSIFICATION, rt.ROOT_CAUSE  FROM TM_ROOTCAUSE_MASTER AS rt where"
					+ " rt.SUB_CLASSIFICATION in (" + subClassification + ") and rt.STATUS = '" + status + "' "
					+ " AND rt.ACTIVE_FLAG = 'ACTIVE' AND rt.CLASSIFICATION = (SELECT max(ins_value) FROM tm_attr_insts"
					+ " WHERE ATTR_TEMP_ID in ('12345','NDO2') AND task_id = '"
					+ taskId + "')";
			Query q = this.getSession().createSQLQuery(queryString);
			// logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getRootCause][queryString]"
			// + queryString);

			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				responseList = new ArrayList<RootCauseDto>();
				RootCauseDto dto = null;
				Map<String, Integer> locMap = new HashMap<String, Integer>();
				for (Object[] obj : response) {
					String subCls = (String) obj[0];
					if (locMap.containsKey(subCls)) {
						responseList.get(locMap.get(subCls)).getRootCauseList().add((String) obj[1]);
					} else {
						dto = new RootCauseDto();
						dto.setRootCauseList(new ArrayList<String>());
						dto.setSubClassification(subCls);
						dto.setStatus(status);
						dto.getRootCauseList().add((String) obj[1]);
						locMap.put(subCls, responseList.size());
						responseList.add(dto);

					}
				}
			}
			// logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getRootCause][queryString]"+queryString
			// +"\n size"+response.size());
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getRootCause][error]" + e.getMessage());
		}
		return responseList;
	}

	@SuppressWarnings("unchecked")
	public List<RootCauseDto> getRootCauseInArray(String taskId, String status, String origin) {
		List<RootCauseDto> responseList = new ArrayList<RootCauseDto>();
		try {
			String endQuery = " and rt.ACTIVE_FLAG = 'ACTIVE' and rt.STATUS = '" + status + "' AND rt.CLASSIFICATION = (SELECT max(ins_value)"
					+ " FROM tm_attr_insts WHERE ATTR_TEMP_ID in ('12345','NDO2') AND task_id = '" + taskId + "') ";

			String queryString = "SELECT rt.SUB_CLASSIFICATION, rt.ROOT_CAUSE FROM TM_ROOTCAUSE_MASTER "
					+ "AS rt where rt.SUB_CLASSIFICATION"
					+ " in (select distinct rt.root_cause from TM_ROOTCAUSE_MASTER rt "
					+ "where rt.ACTIVE_FLAG = 'ACTIVE' and rt.sub_classification in('Issue Classification') )" + endQuery
					+ "order by sub_classification";

			String queryStringIssue = "SELECT rt.ROOT_CAUSE FROM TM_ROOTCAUSE_MASTER "
					+ "AS rt where rt.SUB_CLASSIFICATION  ='Issue Classification' " + endQuery + "order by root_cause";

			Query q = this.getSession().createSQLQuery(queryString);
			Query qIssue = this.getSession().createSQLQuery(queryStringIssue);

			// logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getRootCause][queryString]"
			// + queryString);
			// logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getRootCause][queryStringIssue]"
			// + queryStringIssue);

			RootCauseDto dto = null;

			/* For Issue classification */
			List<String> responseIssue = (List<String>) qIssue.list();
			dto = new RootCauseDto();
			dto.setRootCauseList(responseIssue);
			dto.setSubClassification(MurphyConstant.PRIMARY_CLASSIFICATION);
			dto.setClassification(MurphyConstant.PRIMARY_CLASSIFICATION);
			responseList.add(dto);

			/* For Sub classification */
			List<Object[]> response = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				List<String> str = new ArrayList<String>();
				Map<String, List<String>> locMap = new LinkedHashMap<String, List<String>>();
				for (Object[] obj : response) {
					String clsName = (String) obj[0];
					String subName = (String) obj[1];
					if (locMap.containsKey(clsName)) {
						str = locMap.get(clsName);
						str.add(subName);
					} else {
						str = new ArrayList<String>();
						str.add(subName);
						locMap.put(clsName, str);
					}
				}
				dto = new RootCauseDto();
				dto.setSubClassification(MurphyConstant.SECONDARY_CLASSIFICATION);
				dto.setClassification(MurphyConstant.SECONDARY_CLASSIFICATION);
				List<List<String>> strList = new ArrayList<List<String>>();
				for (Entry<String, List<String>> entry : locMap.entrySet()) {
					strList.add(entry.getValue());
				}
				dto.setSubClassList(strList);
				responseList.add(dto);
			}

		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getRootCause][error]" + e.getMessage());
		}
		return responseList;
	}

	public String getSubClassification(String taskId) {

		String response = null;
		try {
			String queryString = "SELECT max(ins_value) FROM tm_attr_insts WHERE ATTR_TEMP_ID in ('123456','NDO2') AND task_id = '"
					+ taskId + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			response = (String) q.uniqueResult();
			// logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getRootCause][queryString]"+queryString
			// +"\n [response]"+response);

		} catch (Exception e) {
			logger.error(
					"[Murphy][TaskManagement][CustomAttrInstancesDao][getSubClassification][error]" + e.getMessage());

		}
		return response;

	}

	@SuppressWarnings("unchecked")
	public List<TaskSubmittedDto> getAllResolvedTask(String processId) {

		List<TaskSubmittedDto> dtos = new ArrayList<TaskSubmittedDto>();
		String queryString = "SELECT pe.STARTED_AT AS CREATED_AT," + "'POT' AS TEAM,"
				+ "pe.STARTED_BY_DISP AS ASSIGN_TO," + " pe.SUBJECT,'" + MurphyConstant.COMPLETE
				+ "' AS STATUS, pe.COMPLETED_AT , "
				+ "(SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID = 'NDO5' AND  i.TASK_ID = pe.process_id) AS CLASSIFICATION ,"
				+ "(SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID = 'NDO6' AND  i.TASK_ID = pe.process_id) AS SUB_CLASSIFICATION "
				+ " FROM TM_PROC_EVNTS  pe " + "  where pe.process_id = '" + processId + "'  UNION ";
		queryString = queryString + "SELECT te.CREATED_AT AS CREATED_AT,"
				+ "(SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID = 'NDO2' AND  i.TASK_ID = te.TASK_ID ) AS TEAM,"
				+ "( SELECT i.INS_VALUE FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID = 'NDO3' AND i.TASK_ID = te.TASK_ID ) AS ASSIGN_TO,"
				// + " inst.ROOT_CAUSE,"
				+ " te.TSK_SUBJECT,te.STATUS AS STATUS, te.COMPLETED_AT , "
				+ "(SELECT i.ROOT_CAUSE FROM TM_ROOTCAUSE_INSTS AS i WHERE i.SUB_CLASSIFCATION = '"
				+ MurphyConstant.PRIMARY_CLASSIFICATION + "' AND  i.TASK_ID = te.TASK_ID ) AS CLASSIFICATION ,"
				+ "(SELECT i.ROOT_CAUSE FROM TM_ROOTCAUSE_INSTS AS i WHERE i.SUB_CLASSIFCATION = '"
				+ MurphyConstant.SECONDARY_CLASSIFICATION + "' AND  i.TASK_ID = te.TASK_ID ) AS SUB_CLASSIFICATION "
				+ " FROM TM_TASK_EVNTS  te "
				// + "left outer join TM_ROOTCAUSE_INSTS inst on
				// inst.task_id = te.task_id"
				+ "  where te.process_id = '" + processId + "'  "
				// + " and te.status in
				// ('"+MurphyConstant.COMPLETE+"','"+MurphyConstant.RESOLVE+"')"
				+ " ORDER BY 1";

		// logger.error("[Murphy][customAttrInstancesDao][getAllResolvedTask][queryString]"
		// + queryString);

		try {
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {

				TaskSubmittedDto dto = null;
				for (Object[] obj : resultList) {
					dto = new TaskSubmittedDto();
//					dto.setCreatedAtInString(ServicesUtil.isEmpty(obj[0]) ? null
//							: ServicesUtil.convertFromZoneToZoneString(null, obj[0], MurphyConstant.UTC_ZONE,
//									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
//									MurphyConstant.DATE_DISPLAY_FORMAT));
					
					dto.setCreatedAtInString(ServicesUtil.isEmpty(obj[0]) ? null
							: ServicesUtil.convertToEpoch(obj[0], MurphyConstant.DATE_DB_FORMATE));
					
					
					dto.setTeam(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					dto.setUser(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					// dto.setIssueCategory(ServicesUtil.isEmpty(obj[3])?null:(String)
					// obj[3]);
					dto.setDescription(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setStatus(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
//					dto.setCompletedAtInString(ServicesUtil.isEmpty(obj[5]) ? null
//							: ServicesUtil.convertFromZoneToZoneString(null, obj[5], MurphyConstant.UTC_ZONE,
//									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
//									MurphyConstant.DATE_DISPLAY_FORMAT));
					
					dto.setCompletedAtInString(ServicesUtil.isEmpty(obj[5]) ? null
							: ServicesUtil.convertToEpoch(obj[5], MurphyConstant.DATE_DB_FORMATE));
					
					dto.setDescription((ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]) + " - "
							+ (ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]));
					dtos.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][CustomAttrInstancesDao][getAllResolvedTask][error]" + e.getMessage());
		}
		return dtos;
	}

	@SuppressWarnings("unchecked")
	public List<CustomAttrTemplateDto> getCustomAttrIntancesforInq(String taskId, String status, String taskTempId,
			String userType, Boolean isAssigner,String loggedInUser) {
		
		//Start-CHG0037344-Inquiry to a field seat.
		String origin="";
		String parentOrigin="";
		String userTypeInq="";
		String currentProc="";
		String usrGrp="";
		String currentProcName="";
		String loc_code = "";
		//End-CHG0037344-Inquiry to a field seat.
		
		List<CustomAttrTemplateDto> responseDto = new ArrayList<CustomAttrTemplateDto>();
		LinkedHashMap<String, CustomAttrTemplateDto> locationMap = new LinkedHashMap<>();
		String queryString = "Select temp.DATA_TYPE,temp.LABEL,temp.SEQ_NO,ins.INS_VALUE,temp.IS_MAND,temp.IS_UPDATABLE,temp.ATTR_ID,temp.TASK_TEMP_ID from TM_ATTR_INSTS ins inner join TM_ATTR_TEMP temp on temp.ATTR_ID = ins.ATTR_TEMP_ID where ins.TASK_ID in( '"
				+ taskId + "') ";

		if (!ServicesUtil.isEmpty(taskTempId)) {
			queryString = queryString + "and temp.TASK_TEMP_ID in ('" + taskTempId + "')";
		}
		queryString = queryString + " ORDER BY 3";

		logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getCustomAttrIntancesforInq][queryString]"
				+ queryString);

		try {
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				CustomAttrTemplateDto dto = null;
				for (Object[] obj : resultList) {
					dto = new CustomAttrTemplateDto();
					dto.setDataType((ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]));
					dto.setLabel((ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]));
					dto.setSeqNumber((ServicesUtil.isEmpty(obj[2]) ? null : (Integer) obj[2]));
					dto.setLabelValue((ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]));
					dto.setIsMandatory(ServicesUtil.isEmpty(obj[4]) ? null : (((byte) obj[4]) != 0));
					dto.setIsEditable(ServicesUtil.isEmpty(obj[5]) ? null : (((byte) obj[5]) != 0));
					dto.setClItemId((ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]));
					dto.setTaskTempId((ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]));
					List<CustomAttrValuesDto> valuesDto = new ArrayList<CustomAttrValuesDto>();
					CustomAttrValuesDto valueDto = new CustomAttrValuesDto();
					valueDto.setValue(dto.getLabelValue());
					valuesDto.add(valueDto);
					dto.setValueDtos(valuesDto);
					locationMap.put(dto.getClItemId(), dto);
				}
				
				//Start-CHG0037344-Inquiry to a field seat.
				//Object[] result=getTaskDetails(taskId.split(",")[1]);
				Object[] result=getTaskDetails(taskId.split(",")[0]); // get task details based on task id instead of process id - INC0101728
				parentOrigin=result[0].toString();
				origin=result[1].toString();
				currentProc=ServicesUtil.isEmpty(result[2])?null:result[2].toString();
				usrGrp=ServicesUtil.isEmpty(result[3])?null:result[3].toString();
				currentProcName=ServicesUtil.isEmpty(result[4])?null:result[4].toString();
				loc_code= ServicesUtil.isEmpty(result[5])?null:result[5].toString();
				//End-CHG0037344-Inquiry to a field seat.
				
				if (isAssigner) {
					if (status.equals(MurphyConstant.INPROGRESS)) {
						locationMap.get("INQ02").setIsEditable(false);
						 /*List<CustomAttrValuesDto> valueDtosForGroup=new ArrayList<CustomAttrValuesDto>();
						 CustomAttrValuesDto groupValueDto=new CustomAttrValuesDto();
						 groupValueDto.setValue(locationMap.get(3).getLabelValue());
						 valueDtosForGroup.add(groupValueDto);
						 locationMap.get(3).setValueDtos(valueDtosForGroup);*/
						locationMap.get("INQ03").setIsEditable(false);
						
						//Start-CHG0037344-Inquiry to a field seat.
						if (parentOrigin.equals(MurphyConstant.INQUIRY) && origin.equals(MurphyConstant.INQUIRY) 
								&& !ServicesUtil.isEmpty(usrGrp) && !ServicesUtil.isEmpty(currentProc))
								locationMap.get("INQ03").setLabelValue(currentProcName);
						
						//End-CHG0037344-Inquiry to a field seat.
						
						
						
						// List<CustomAttrValuesDto> valueDtosForPerson=new
						// ArrayList<CustomAttrValuesDto>();
						// CustomAttrValuesDto personValueDto=new
						// CustomAttrValuesDto();
						// personValueDto.setValue(locationMap.get(4).getLabelValue());
						// valueDtosForPerson.add(personValueDto);
						// locationMap.get(4).setValueDtos(valueDtosForPerson);
					} else if (status.equals(MurphyConstant.RESOLVE)) {
						locationMap.get("INQ02").setIsEditable(true);
						
						
						//Start-CHG0037344-Inquiry to a field seat.
						/*if (parentOrigin.equals(MurphyConstant.INQUIRY) && origin.equals(MurphyConstant.INQUIRY)) {
							userTypeInq = "ENG_INQUIRY";
						} else {
							userTypeInq = userType;
						}*/
						// Get assign to group values based on location's field
						String field_code = null; List<String> groupList = null;
						String locationType = locDao.getLocationtypeByLocCode(loc_code);
						FieldResponseDto fieldResponseDto = locDao.getFeild(loc_code, locationType, true);
						
						if(!ServicesUtil.isEmpty(fieldResponseDto.getField()))
							field_code = locDao.getLocationCodeByLocText(fieldResponseDto.getField());
						
						if(!ServicesUtil.isEmpty(field_code))
							groupList = attrTempDao.getGroupRoles(field_code,userType);
						//End-CHG0037344-Inquiry to a field seat.
						
						List<CustomAttrValuesDto> valueDtos = new ArrayList<CustomAttrValuesDto>();
						for (String group : groupList) {
							if (group.length() != 0 && group != null) {
								CustomAttrValuesDto groupValueDto = new CustomAttrValuesDto();
								groupValueDto.setValue(group);
								valueDtos.add(groupValueDto);
							}
						}
						locationMap.get("INQ02").setValueDtos(valueDtos);
						locationMap.get("INQ03").setIsEditable(true);
						List<CustomAttrValuesDto> valueDtosForPersons = new ArrayList<CustomAttrValuesDto>();
						locationMap.get("INQ03").setValueDtos(valueDtosForPersons);
						// List<CustomAttrValuesDto> valueDtosForPerson=new
						// ArrayList<CustomAttrValuesDto>();
						// CustomAttrValuesDto personValueDto=new
						// CustomAttrValuesDto();
						// personValueDto.setValue(locationMap.get(4).getLabelValue());
						// valueDtosForPerson.add(personValueDto);
						// locationMap.get(4).setValueDtos(valueDtosForPerson);
					} else {
						locationMap.get("INQ02").setIsEditable(false);
						// List<CustomAttrValuesDto> valueDtosForGroup=new
						// ArrayList<CustomAttrValuesDto>();
						// CustomAttrValuesDto groupValueDto=new
						// CustomAttrValuesDto();
						// groupValueDto.setValue(locationMap.get(3).getLabelValue());
						// valueDtosForGroup.add(groupValueDto);
						// locationMap.get(3).setValueDtos(valueDtosForGroup);
						// List<CustomAttrValuesDto> valueDtosForPerson=new
						// ArrayList<CustomAttrValuesDto>();
						// CustomAttrValuesDto personValueDto=new
						// CustomAttrValuesDto();
						// personValueDto.setValue(locationMap.get(4).getLabelValue());
						// valueDtosForPerson.add(personValueDto);
						// locationMap.get(4).setValueDtos(valueDtosForPerson);
					}
				} else {
					
					// Start-CHG0037344-Inquiry to a field seat.
					/*if (!userType.equals(MurphyConstant.USER_TYPE_ENG) && parentOrigin.equals(MurphyConstant.INQUIRY)
							&& origin.equals(MurphyConstant.INQUIRY) && !ServicesUtil.isEmpty(usrGrp)
							&& (usrGrp.contains("IOP_Engineer_West") || usrGrp.contains("IOP_Engineer_East"))
							&& status.equals(MurphyConstant.INPROGRESS) && !ServicesUtil.isEmpty(currentProc)
							&& !currentProc.equals(loggedInUser)) {*/
					if (parentOrigin.equals(MurphyConstant.INQUIRY) && origin.equals(MurphyConstant.INQUIRY) 
							&& status.equals(MurphyConstant.INPROGRESS) && !ServicesUtil.isEmpty(currentProc)
							&& !currentProc.equals(loggedInUser)) {

						locationMap.get("INQ02").setIsEditable(false);

						locationMap.get("INQ03").setIsEditable(false);
						if(!ServicesUtil.isEmpty(currentProc))
						locationMap.get("INQ03").setLabelValue(currentProcName);
						
					// End-CHG0037344-Inquiry to a field seat.

					} else {
						locationMap.get("INQ02").setIsEditable(true);

						// Start-CHG0037344-Inquiry to a field seat.
						// Get assign to group values based on location's field
						String field_code = null; List<String> groupList = null;
						String locationType = locDao.getLocationtypeByLocCode(loc_code);
						FieldResponseDto fieldResponseDto = locDao.getFeild(loc_code, locationType, true);
						
						if(!ServicesUtil.isEmpty(fieldResponseDto.getField()))
							field_code = locDao.getLocationCodeByLocText(fieldResponseDto.getField());
						
						if(!ServicesUtil.isEmpty(field_code))
							groupList = attrTempDao.getGroupRoles(field_code,userType);
						
						// End-CHG0037344-Inquiry to a field seat.

						List<CustomAttrValuesDto> valueDtos = new ArrayList<CustomAttrValuesDto>();
						for (String group : groupList) {
							if (group.length() != 0 && group != null) {
								CustomAttrValuesDto groupValueDto = new CustomAttrValuesDto();
								groupValueDto.setValue(group);
								valueDtos.add(groupValueDto);
							}
						}
						locationMap.get("INQ02").setValueDtos(valueDtos);
						locationMap.get("INQ03").setIsEditable(true);
						List<CustomAttrValuesDto> valueDtosForPersons = new ArrayList<CustomAttrValuesDto>();
						locationMap.get("INQ03").setValueDtos(valueDtosForPersons);
						// List<CustomAttrValuesDto> valueDtosForPerson=new
						// ArrayList<CustomAttrValuesDto>();
						// CustomAttrValuesDto personValueDto=new
						// CustomAttrValuesDto();
						// personValueDto.setValue(locationMap.get(4).getLabelValue());
						// valueDtosForPerson.add(personValueDto);
						// locationMap.get(4).setValueDtos(valueDtosForPerson);
					}
				}
			}
			//Start-CHG0037344-Inquiry to a field seat.
			if (parentOrigin.equals(MurphyConstant.INQUIRY) && origin.equals(MurphyConstant.INQUIRY)) {
				
				locationMap.get("INQ03").setIsMandatory(false);
			}
			//End-CHG0037344-Inquiry to a field seat.
			
			for (CustomAttrTemplateDto attr : locationMap.values()) {
				responseDto.add(attr);
			}
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getCustomAttrIntancesforInq][error]"
					+ e.getMessage());
		}
		return responseDto;
	}
	
	//Start-CHG0037344-Inquiry to a field seat.
	// INC0101728 - this method fetches the details based on task id instead of process id as earlier 
		private Object[] getTaskDetails(String taskId) {
			try
			{
			//String qryString = "select parent_origin,origin,cur_proc from tm_task_evnts where task_id='"+taskId;
				String qryString = "select te.parent_origin,te.origin,te.cur_proc,tp.user_group,te.cur_proc_disp,tp.loc_code from tm_task_evnts te join "
						+ "tm_proc_evnts tp on te.process_id=tp.process_id where te.task_id='" + taskId + " and te.status <> 'COMPLETED'";
				
				logger.error("[Murphy][TaskManagementService][CustomAttrInstancesDao][getOriginOfTask][query] "+qryString);
			
			Query query = this.getSession().createSQLQuery(qryString);
			Object[] result = (Object[]) query.uniqueResult();
			if (!ServicesUtil.isEmpty(result)) {
				//Object[] result = resultList.get(0);
				return result;
			}
			}catch (Exception e) {
				logger.error("[Murphy][TaskManagementService][CustomAttrInstancesDao][getOriginOfTask][error] "+e.getMessage());
				e.printStackTrace();
			}
			return null;
		
		}//End-CHG0037344-Inquiry to a field seat.

	// @SuppressWarnings("unchecked")
	// public List<InvestigationHistoryDto> getInvestigationHistory(String
	// processId) {
	//
	// List<InvestigationHistoryDto> dtos = null;
	// String queryString = "SELECT pe.STARTED_AT as created_at,'' as
	// primary_job, '' as secondary_job ,'' as operation_status, pe.status AS
	// status FROM TM_ATTR_INSTS i ,tm_proc_evnts pe"
	// + " WHERE i.ATTR_TEMP_ID = 'NDV1' and i.INS_VALUE = (SELECT i.INS_VALUE
	// FROM TM_ATTR_INSTS AS i WHERE i.ATTR_TEMP_ID = 'NDV1' AND i.TASK_ID = '"
	// + processId + "') and i.task_id = pe.process_id ";
	// // "and pe.status = 'COMPLETED'";
	//
	// //
	// logger.error("[Murphy][customAttrInstancesDao][getInvestigationHistory][queryString]"+queryString);
	//
	// try {
	// Query q = this.getSession().createSQLQuery(queryString);
	// List<Object[]> resultList = q.list();
	// if (!ServicesUtil.isEmpty(resultList)) {
	// dtos = new ArrayList<InvestigationHistoryDto>();
	// InvestigationHistoryDto dto = null;
	// for (Object[] obj : resultList) {
	// dto = new InvestigationHistoryDto();
	// dto.setCreatedDateString(ServicesUtil.isEmpty(obj[0]) ? null
	// : ServicesUtil.convertFromZoneToZoneString(null, obj[0],
	// MurphyConstant.UTC_ZONE,
	// MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
	// MurphyConstant.DATE_DISPLAY_FORMAT));
	// dto.setCategory("Investigation");
	// dto.setPrimaryJobType(ServicesUtil.isEmpty(obj[1]) ? null : (String)
	// obj[1]);
	// dto.setSecondaryJobType(ServicesUtil.isEmpty(obj[2]) ? null : (String)
	// obj[2]);
	// dto.setOperationSummary(ServicesUtil.isEmpty(obj[3]) ? null : (String)
	// obj[3]);
	// dto.setStatus(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
	// dtos.add(dto);
	// }
	// }
	// } catch (Exception e) {
	// logger.error("[Murphy][CustomAttrInstancesDao][getInvestigationHistory][error]"
	// + e.getMessage());
	// }
	// return dtos;
	// }

	//
	// @SuppressWarnings("unchecked")
	// public List<CustomAttrTemplateDto> getCustomAttrIntancesbyId(String
	// taskId, String status, String userType) {
	// List<CustomAttrTemplateDto> responseDto = null;
	//
	// if (userType.equals(MurphyConstant.USER_TYPE_POT)) {
	// taskId = new TaskEventsDao().getLatestNotResolvedTask(taskId);
	// }
	// if (!ServicesUtil.isEmpty(taskId)) {
	// String queryString = "Select
	// temp.DATA_TYPE,temp.LABEL,temp.SEQ_NO,ins.INS_VALUE,temp.IS_MAND,"
	// + "temp.IS_UPDATABLE from TM_ATTR_INSTS ins inner join TM_ATTR_TEMP temp
	// "
	// + "on temp.ATTR_ID = ins.ATTR_TEMP_ID where ins.TASK_ID = '" + taskId +
	// "' ORDER BY 3";
	// try {
	// Query q = this.getSession().createSQLQuery(queryString);
	// List<Object[]> resultList = q.list();
	// if (!ServicesUtil.isEmpty(resultList)) {
	// responseDto = new ArrayList<CustomAttrTemplateDto>();
	// CustomAttrTemplateDto dto = null;
	// for (Object[] obj : resultList) {
	// dto = new CustomAttrTemplateDto();
	// dto.setDataType(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
	// dto.setLabel(ServicesUtil.isEmpty(obj[1]) ? null : ((String) obj[1]));
	// dto.setSeqNumber(ServicesUtil.isEmpty(obj[2]) ? null : (Integer) obj[2]);
	// if (!MurphyConstant.STATUS.equals(dto.getLabel())) {
	// dto.setLabelValue(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
	// } else {
	// dto.setLabelValue(status);
	// }
	//
	// CustomAttrValuesDto valueDto = new CustomAttrValuesDto();
	// valueDto.setValue(dto.getLabelValue());
	// dto.setValueDtos(new ArrayList<CustomAttrValuesDto>());
	// dto.getValueDtos().add(valueDto);
	//
	// if (MurphyConstant.RETURN.equals(status) ||
	// MurphyConstant.ASSIGN.equals(status)) {
	// dto.setIsEditable(ServicesUtil.isEmpty(obj[5]) ? null : (((byte) obj[5])
	// != 0));
	// } else {
	// dto.setIsEditable(false);
	// }
	// dto.setIsMandatory(ServicesUtil.isEmpty(obj[4]) ? null : (((byte) obj[4])
	// != 0));
	// responseDto.add(dto);
	// }
	// return responseDto;
	// }
	// } catch (Exception e) {
	// logger.error("[Murphy][TaskManagement][CustomAttrInstancesDao][getCustomAttrIntancesbyId][error]"
	// + e.getMessage());
	// }
	// } else {
	// // responseDto =
	// attrTempDao.getCustomHeadersbyTaskTempId(MurphyConstant.TEMP_ID_OBSERVATION,
	// "", "");
	// }
	// return responseDto;
	// }
	/*
	 * private Date getEstimatedDate(Date now, double minuts) { LocalDateTime
	 * date = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
	 * .plusMinutes((long) Math.floor(minuts)); if (minuts != 0) return
	 * java.util.Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	 * else return now; }
	 */

	@SuppressWarnings("unchecked")
	public List<TaskSubmittedDto> getInquiryActivityLog(String taskId, String processId) {

		List<TaskSubmittedDto> dtos = null;
		// Removed RowNumber() for complete inquiry and dispatch activity log
		String queryString = "(SELECT AU.CREATED_AT,UM.USER_FIRST_NAME,UM.USER_LAST_NAME,UM.USER_ROLE,PE.SUBJECT,AU.ACTION AS ACTION,TE.COMPLETED_AT FROM TM_AUDIT_TRAIL AU "
				+ " JOIN TM_TASK_EVNTS TE ON (AU.TASK_ID=TE.TASK_ID OR AU.TASK_ID = TE.PROCESS_ID) "
				+ " JOIN TM_PROC_EVNTS PE ON TE.PROCESS_ID=PE.PROCESS_ID "
				+ " JOIN TM_USER_IDP_MAPPING UM ON UM.USER_EMAIL=AU.ACTION_BY WHERE AU.TASK_ID in ('";
		String queryStringForProcessLevel = "(SELECT AU.CREATED_AT,UM.USER_FIRST_NAME,UM.USER_LAST_NAME,UM.USER_ROLE,PE.SUBJECT,AU.ACTION AS ACTION,TE.COMPLETED_AT FROM TM_AUDIT_TRAIL AU "
				+ " JOIN TM_TASK_EVNTS TE ON (AU.TASK_ID=TE.TASK_ID OR AU.TASK_ID = TE.PROCESS_ID) "
				+ " JOIN TM_PROC_EVNTS PE ON TE.PROCESS_ID=PE.PROCESS_ID "
				+ " JOIN TM_USER_IDP_MAPPING UM ON UM.USER_EMAIL=AU.ACTION_BY WHERE AU.TASK_ID in ('";
		String taskLevelQuery = queryString + taskId + "'))";
		String processLevelQuery = queryStringForProcessLevel + processId + "'))";
		String taskLatest = "(Select * FROM " + taskLevelQuery + " WHERE ACTION NOT IN ('CREATED','COMPLETED'))";
		String forwardQuery = "(SELECT * FROM " + taskLevelQuery + " WHERE  ACTION='FORWARDED')";
		String processAllQuery = "(SELECT * FROM " + processLevelQuery + ")";
		String unionQuery = "( " + taskLatest + " UNION " + forwardQuery + " UNION " + processAllQuery
				+ " ORDER BY 1 ASC  )";
		String finalQuery = "Select * FROM " + unionQuery + "";

		/*
		 * String queryString =
		 * "(SELECT AU.CREATED_AT,UM.USER_FIRST_NAME,UM.USER_LAST_NAME,UM.USER_ROLE,PE.SUBJECT,AU.ACTION AS ACTION,TE.COMPLETED_AT,ROW_NUMBER() OVER (PARTITION BY AU.TASK_ID ORDER BY AU.CREATED_AT DESC) as RN FROM TM_AUDIT_TRAIL AU "
		 * +
		 * " JOIN TM_TASK_EVNTS TE ON (AU.TASK_ID=TE.TASK_ID OR AU.TASK_ID = TE.PROCESS_ID) "
		 * + " JOIN TM_PROC_EVNTS PE ON TE.PROCESS_ID=PE.PROCESS_ID " +
		 * " JOIN TM_USER_IDP_MAPPING UM ON UM.USER_EMAIL=AU.ACTION_BY WHERE AU.TASK_ID in ('"
		 * ; String queryStringForProcessLevel =
		 * "(SELECT AU.CREATED_AT,UM.USER_FIRST_NAME,UM.USER_LAST_NAME,UM.USER_ROLE,PE.SUBJECT,AU.ACTION AS ACTION,TE.COMPLETED_AT,ROW_NUMBER() OVER (PARTITION BY AU.ACTION ORDER BY AU.CREATED_AT DESC) as RN FROM TM_AUDIT_TRAIL AU "
		 * +
		 * " JOIN TM_TASK_EVNTS TE ON (AU.TASK_ID=TE.TASK_ID OR AU.TASK_ID = TE.PROCESS_ID) "
		 * + " JOIN TM_PROC_EVNTS PE ON TE.PROCESS_ID=PE.PROCESS_ID " +
		 * " JOIN TM_USER_IDP_MAPPING UM ON UM.USER_EMAIL=AU.ACTION_BY WHERE AU.TASK_ID in ('"
		 * ; String taskLevelQuery = queryString + taskId + "'))"; String
		 * processLevelQuery = queryStringForProcessLevel + processId + "'))";
		 * String taskLatest = "(Select * FROM " + taskLevelQuery +
		 * " WHERE RN=1 AND ACTION NOT IN ('CREATED','COMPLETED'))"; String
		 * forwardQuery = "(SELECT * FROM " + taskLevelQuery +
		 * " WHERE  ACTION='FORWARDED')"; String processAllQuery =
		 * "(SELECT * FROM " + processLevelQuery + " WHERE RN=1)"; String
		 * unionQuery = "( " + taskLatest + " UNION " + forwardQuery + " UNION "
		 * + processAllQuery + " ORDER BY 1 ASC  )"; String finalQuery =
		 * "Select * FROM " + unionQuery + "";
		 */
		try {
			logger.error("[Murphy][CustomAttrInstancesDao][getInquiryActivityLog][Query]" + finalQuery);
			Query q = this.getSession().createSQLQuery(finalQuery);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				dtos = new ArrayList<TaskSubmittedDto>();
				TaskSubmittedDto dto = null;
				String user = null;
				for (Object[] obj : resultList) {
					dto = new TaskSubmittedDto();
//					dto.setCreatedAtInString(ServicesUtil.isEmpty(obj[0]) ? null
//							: ServicesUtil.convertFromZoneToZoneString(null, obj[0], MurphyConstant.UTC_ZONE,
//									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
//									MurphyConstant.DATE_DISPLAY_FORMAT));
					
					dto.setCreatedAtInString(ServicesUtil.isEmpty(obj[0]) ? null
							: ServicesUtil.convertToEpoch(obj[0], MurphyConstant.DATE_DB_FORMATE));
					
					user = (ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]) + " "
							+ (ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					dto.setUser(user);
					if (!ServicesUtil.isEmpty(obj[3])) {
						String user_role = (String) obj[3];
						if ((user_role.toLowerCase()).contains(MurphyConstant.USER_TYPE_POT.toLowerCase()))
							dto.setTeam(MurphyConstant.USER_TYPE_POT);
						else if ((user_role.toLowerCase()).contains(MurphyConstant.USER_TYPE_ENG.toLowerCase()))
							dto.setTeam(MurphyConstant.USER_TYPE_ENG);
						else if (user_role.toLowerCase().contains(MurphyConstant.USER_TYPE_ROC.toLowerCase()))
							dto.setTeam(MurphyConstant.USER_TYPE_ROC);
					}
					dto.setDescription(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setStatus(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
//					dto.setCompletedAtInString(ServicesUtil.isEmpty(obj[6]) ? null
//							: ServicesUtil.convertFromZoneToZoneString(null, obj[6], MurphyConstant.UTC_ZONE,
//									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
//									MurphyConstant.DATE_DISPLAY_FORMAT));
					
					dto.setCompletedAtInString(ServicesUtil.isEmpty(obj[6]) ? null
							: ServicesUtil.convertToEpoch(obj[6], MurphyConstant.DATE_DB_FORMATE));
					
					dtos.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][CustomAttrInstancesDao][getInquiryActivityLog][error]" + e.getMessage());
		}
		return dtos;
	}
}
