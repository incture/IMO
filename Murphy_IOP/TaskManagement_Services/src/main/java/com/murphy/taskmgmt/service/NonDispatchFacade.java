package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dao.CustomAttrValuesDao;
import com.murphy.taskmgmt.dao.NDTaskMappingDao;
import com.murphy.taskmgmt.dao.NonDispatchTaskDao;
import com.murphy.taskmgmt.dto.AuditReportDto;
import com.murphy.taskmgmt.dto.CustomAttrValuesDto;
import com.murphy.taskmgmt.dto.NonDispatchResponseDto;
import com.murphy.taskmgmt.dto.NonDispatchTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskTypeResponseDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.service.interfaces.NonDispatchFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;



@Service("nonDispatchFacade")
public class NonDispatchFacade implements NonDispatchFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(NonDispatchFacade.class);

	public NonDispatchFacade() {
	}

	@Autowired
	private NonDispatchTaskDao ndTaskDao;

	@Autowired
	private AuditDao auditDao;

	@Autowired
	private NDTaskMappingDao mappingDao;


	@Autowired
	private CustomAttrValuesDao attrDao;

	@Override
	public ResponseMessage createTask(NonDispatchTaskDto dto) {
		return ndTaskDao.createInstance(dto);
	}

	@Override
	public ResponseMessage deleteTask(UpdateRequestDto dto) {
		return ndTaskDao.deleteInstance(dto);
	}

	@Override
	public NonDispatchResponseDto getAllTasks(String group, String location,String locType) {
		return ndTaskDao.readAllInstance(group, location,locType);
	}

	@Override
	public ResponseMessage updateTask(NonDispatchTaskDto dto){
		return ndTaskDao.updateInstance(dto);
	}

	@Override
	public ResponseMessage completeTask(UpdateRequestDto dto){
		return mappingDao.updateStatusOfInstance(dto);
	}


	@Override
	public TaskTypeResponseDto getTaskTypes(){
		TaskTypeResponseDto responseDto = new TaskTypeResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			List<CustomAttrValuesDto> attrList =   attrDao.getTaskTypes();
			if(ServicesUtil.isEmpty(attrList)){
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}else{
				responseDto.setValueDtos(attrList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][nonDispatchFacade][getTaskTypes][error]"+e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

	@Override 
	public List<AuditReportDto> getReport(){
		return auditDao.getReport();
	}

}

