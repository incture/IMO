package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.CustomAttrTemplateDto;
import com.murphy.taskmgmt.dto.CustomAttrValuesDto;
import com.murphy.taskmgmt.dto.ProcessTemplateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskTemplatesDto;
import com.murphy.taskmgmt.entity.ProcessTemplateDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ProcessTemplateDao")
@Transactional
public class ProcessTemplateDao extends BaseDao<ProcessTemplateDo, ProcessTemplateDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessTemplateDao.class);

	@Autowired
	private TaskTemplatesDao taskDao;
	
	public ProcessTemplateDao() {
	}

	@Override
	protected ProcessTemplateDo importDto(ProcessTemplateDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {

		ProcessTemplateDo entity = new ProcessTemplateDo();

		if (!ServicesUtil.isEmpty(fromDto.getProcessTemplateId()))
			entity.setProcessTemplateId(fromDto.getProcessTemplateId());
		if (!ServicesUtil.isEmpty(fromDto.getProcessName()))
			entity.setProcessName(fromDto.getProcessName());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedBy()))
			entity.setCreatedBy(fromDto.getCreatedBy());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedByDisplay()))
			entity.setCreatedByDisplay(fromDto.getCreatedByDisplay());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getSla()))
			entity.setSla(fromDto.getSla());
		if (!ServicesUtil.isEmpty(fromDto.getTempType()))
			entity.setTempType(fromDto.getTempType());
		if (!ServicesUtil.isEmpty(fromDto.getLastUpdated()))
			entity.setLastUpdated(fromDto.getLastUpdated());

		return entity;
	}

	@Override
	protected ProcessTemplateDto exportDto(ProcessTemplateDo entity) {

		ProcessTemplateDto dto = new ProcessTemplateDto();
		if (!ServicesUtil.isEmpty(entity.getProcessTemplateId()))
			dto.setProcessTemplateId(entity.getProcessTemplateId());
		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			dto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getCreatedBy()))
			dto.setCreatedBy(entity.getCreatedBy());
		if (!ServicesUtil.isEmpty(entity.getCreatedByDisplay()))
			dto.setCreatedByDisplay(entity.getCreatedByDisplay());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getSla()))
			dto.setSla(entity.getSla());
		if (!ServicesUtil.isEmpty(entity.getTempType()))
			dto.setTempType(entity.getTempType());
		if (!ServicesUtil.isEmpty(entity.getLastUpdated()))
			dto.setLastUpdated(entity.getLastUpdated());

		return dto;
	}


	public ResponseMessage createProcessTemp(ProcessTemplateDto dto){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
		responseMessage.setStatus(MurphyConstant.SUCCESS);
		responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		try{
			String processTemplateId = UUID.randomUUID().toString().replaceAll("-", "");
			dto.setProcessTemplateId(processTemplateId);
			dto.setCreatedAt(new Date());
			create(dto);
			if(!ServicesUtil.isEmpty(dto.getTaskTemplates())){
				for(TaskTemplatesDto taskTempDto : dto.getTaskTemplates()){
					taskTempDto.setProcessTemplateId(processTemplateId);
					ResponseMessage errorReturn = taskDao.createTaskTemp(taskTempDto);
					if(!errorReturn.getStatus().equals(MurphyConstant.SUCCESS)){
						return errorReturn;
					}
				}
			}
			return responseMessage;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("[Murphy][TaskManagement][ProcessTemplateDao][createProcessTemp][error]"+e.getMessage());
		}
		responseMessage.setMessage(MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		return responseMessage;
	}


	@SuppressWarnings({ "unchecked" })
	public ProcessTemplateDto readALLTaskTemplates(String processTemplateId){

		ProcessTemplateDto reponseDto = new ProcessTemplateDto();

		String queryString = "Select pt.PROC_TEMP_ID AS PROC_TEMP_ID,pt.PROC_NAME AS PROC_NAME,pt.CREATED_BY AS CREATED_BY,pt.CREATED_BY_DISP AS CREATED_BY_DISP,"
				+ "pt.SLA AS SLA,pt.TEMP_TYPE AS TEMP_TYPE,pt.LAST_UPDATED AS LAST_UPDATED,pt.CREATED_AT AS CREATED_AT"
				+ ",tt.TASK_TEMP_ID AS TASK_TEMP_ID,tt.DESCRIPTION AS DESCRIPTION,tt.SUBJECT AS SUBJECT,tt.NAME AS NAME"
				+ ",tt.PRIORITY AS PRIORITY,tt.SLA AS TASK_SLA,tt.TASK_TYPE AS TASK_TYPE,tt.TASK_OWNER_GRP AS TASK_OWNER_GRP"
				+ ",clt.SHORT_DESC AS SHORT_DESC,clt.IS_EDITABLE AS IS_EDITABLE,clt.SERVICE_URL AS SERVICE_URL,tt.URL AS URL"
				+ ",clt.ATTR_ID AS ITEM_ID,clt.LABEL AS LIST_ITEM,clt.DATA_TYPE AS DATA_TYPE,clt.IS_MAND AS IS_MAND"
				+ ",clt.MAX_LENGTH As MAX_LENGTH,clt.SEQ_NO AS SEQ_NO"
				+ ",clo.VALUE_ID AS VALUE_ID,clo.ATTR_VALUE AS ATTR_VALUE"
				+ " from TM_PROC_TEMPS pt INNER JOIN TM_TASK_TEMPS tt  ON pt.PROC_TEMP_ID = tt.PROC_TEMP_ID "
				+ "INNER JOIN  TM_ATTR_TEMP clt  ON tt.TASK_TEMP_ID = clt.TASK_TEMP_ID  INNER JOIN "
				+ "TM_ATTR_VALUES clo ON  clt.ATTR_ID  = clo.ITEM_ID "
				+ " where pt.PROC_TEMP_ID = '"+processTemplateId+"' ";

		Query query =  this.getSession().createSQLQuery(queryString.trim());
		//, "templateResults");
		List<Object[]> resultList = query.list();
		Map<String ,Integer> taskTempMap = new HashMap<String,Integer>();
		Map<String ,Integer> clMap = new HashMap<String,Integer>();
		TaskTemplatesDto taskTempDto = null;
		CustomAttrTemplateDto clDto = null;
		CustomAttrValuesDto optionDto = null;
		if(!ServicesUtil.isEmpty(resultList)){
			int i = 0;
			for(Object[] obj : resultList){
				if(i==0){
					reponseDto.setProcessTemplateId(ServicesUtil.isEmpty(obj[0]) ? null: (String) obj[0]);
					reponseDto.setProcessName(ServicesUtil.isEmpty(obj[1]) ? null: (String) obj[1]);
					reponseDto.setCreatedBy(ServicesUtil.isEmpty(obj[2]) ? null: (String) obj[2]);
					reponseDto.setCreatedByDisplay(ServicesUtil.isEmpty(obj[3]) ? null: (String) obj[3]);
					reponseDto.setSla(ServicesUtil.isEmpty(obj[4]) ? null: (String) obj[4]);
					reponseDto.setTempType(ServicesUtil.isEmpty(obj[5]) ? null: (String) obj[5]);
					reponseDto.setLastUpdated(ServicesUtil.isEmpty(obj[6]) ? null: ServicesUtil.convertFromZoneToZone(null, obj[6] ,MurphyConstant.UTC_ZONE, MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					reponseDto.setCreatedAt(ServicesUtil.isEmpty(obj[7]) ? null: ServicesUtil.convertFromZoneToZone(null, obj[7] ,MurphyConstant.UTC_ZONE, MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					reponseDto.setTaskTemplates(new ArrayList<TaskTemplatesDto>());
					i++;
				}

				String taskTempId  = (String) obj[8];
				if(!taskTempMap.containsKey(taskTempId)){
					taskTempDto = gettaskTempDto(obj);
					clDto = getCheckListDto(obj);
					clDto.getValueDtos().add(getValueDto(obj));
					clMap.put(clDto.getClItemId(), 0);
					reponseDto.getTaskTemplates().get(taskTempMap.get(taskTempId));
					reponseDto.getTaskTemplates().add(taskTempDto);
					taskTempMap.put(taskTempId, reponseDto.getTaskTemplates().size()-1);

				}else{

					taskTempDto = reponseDto.getTaskTemplates().get(taskTempMap.get(taskTempId));
					clDto = getCheckListDto(obj);
					optionDto = getValueDto(obj);
					String clItemId = clDto.getClItemId();

					if(clMap.containsKey(clItemId)){
						taskTempDto.getAttrList().get(clMap.get(clItemId)).getValueDtos().add(optionDto);
					}
					else{
						clDto.getValueDtos().add(optionDto);
						taskTempDto.getAttrList().add(clDto);
						clMap.put(clDto.getClItemId(), taskTempDto.getAttrList().size()-1);
					}

				}
			}
		}
		return reponseDto;
	}


	private CustomAttrTemplateDto getCheckListDto(Object[] obj){

		CustomAttrTemplateDto clDto = new CustomAttrTemplateDto();
		clDto.setValueDtos(new ArrayList<CustomAttrValuesDto>());
		clDto.setValues(new ArrayList<String>());

		clDto.setShortDesc(ServicesUtil.isEmpty(obj[16]) ? null: (String) obj[16]);
		clDto.setIsEditable(ServicesUtil.isEmpty(obj[17]) ? null: (Boolean) obj[17]);
//		clDto.setServiceUrl(ServicesUtil.isEmpty(obj[18]) ? null: (String) obj[18]);
		clDto.setClItemId(ServicesUtil.isEmpty(obj[20]) ? null: (String) obj[20]);
		clDto.setLabel(ServicesUtil.isEmpty(obj[21]) ? null: (String) obj[21]);
		clDto.setDataType(ServicesUtil.isEmpty(obj[22]) ? null: (String) obj[22]);
		clDto.setIsMandatory(ServicesUtil.isEmpty(obj[23]) ? null: (Boolean) obj[23]);
		clDto.setMaxLength(ServicesUtil.isEmpty(obj[24]) ? null: (Integer) obj[24]);
		clDto.setSeqNumber(ServicesUtil.isEmpty(obj[25]) ? null: (Integer) obj[25]);
		clDto.getValues().add(ServicesUtil.isEmpty(obj[27]) ? null: (String) obj[27]);

		return clDto;
	} 


	private TaskTemplatesDto gettaskTempDto(Object[] obj){

		TaskTemplatesDto taskTempDto = new TaskTemplatesDto();
		taskTempDto.setAttrList(new ArrayList<CustomAttrTemplateDto>());

		taskTempDto.setProcessTemplateId(ServicesUtil.isEmpty(obj[0]) ? null: (String) obj[0]);
		taskTempDto.setTaskTemplateId(ServicesUtil.isEmpty(obj[8]) ? null: (String) obj[8]);
		taskTempDto.setDescription(ServicesUtil.isEmpty(obj[9]) ? null: (String) obj[9]);
		taskTempDto.setSubject(ServicesUtil.isEmpty(obj[10]) ? null: (String) obj[10]);
		taskTempDto.setName(ServicesUtil.isEmpty(obj[11]) ? null: (String) obj[11]);
		taskTempDto.setPriority(ServicesUtil.isEmpty(obj[12]) ? null: (String) obj[12]);
		taskTempDto.setSla(ServicesUtil.isEmpty(obj[13]) ? null: (String) obj[13]);
		taskTempDto.setTaskType(ServicesUtil.isEmpty(obj[14]) ? null: (String) obj[14]);
		taskTempDto.setOwnerGroup(ServicesUtil.isEmpty(obj[15]) ? null: (String) obj[15]);
		taskTempDto.setUrl(ServicesUtil.isEmpty(obj[19]) ? null: (String) obj[19]);

		return taskTempDto;
	} 



	private CustomAttrValuesDto getValueDto(Object[] obj){

		CustomAttrValuesDto	optionDto = new CustomAttrValuesDto();
		optionDto.setClItemId(ServicesUtil.isEmpty(obj[20]) ? null: (String) obj[20]);
		optionDto.setValueId(ServicesUtil.isEmpty(obj[26]) ? null: (String) obj[26]);
		optionDto.setValue(ServicesUtil.isEmpty(obj[27]) ? null: (String) obj[27]);
		return optionDto;
	}
}
