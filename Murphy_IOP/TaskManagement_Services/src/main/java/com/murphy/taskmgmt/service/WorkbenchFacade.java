package com.murphy.taskmgmt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.WorkBenchDao;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.WorkBenchResponseDto;
import com.murphy.taskmgmt.dto.WrokBenchAudiLogDto;
import com.murphy.taskmgmt.service.interfaces.WorkbenchFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("workbenchFacade")
public class WorkbenchFacade implements WorkbenchFacadeLocal{
	private static final Logger logger = LoggerFactory.getLogger(WorkbenchFacade.class);

	@Autowired
	WorkBenchDao workBenchDao;

	@Override
	public WorkBenchResponseDto getTaskList(String sortingOrder, String sortObject, String groupObject , String technicalRole, 
			String locationCode, String locationType , String status, String isObx) {
		WorkBenchResponseDto workBenchResponseDto = new WorkBenchResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		workBenchResponseDto.setResponseMessage(responseMessage);
		try{
			if(!ServicesUtil.isEmpty(sortingOrder)){
				if(sortingOrder.equalsIgnoreCase("ascending")){
					sortingOrder = "asc";
				}else if(sortingOrder.equalsIgnoreCase("descending")){
					sortingOrder = "desc";
				}
			}
			
			workBenchResponseDto.setWorkBenchDtoList(workBenchDao.getTaskList(sortingOrder, sortObject, groupObject , 
					technicalRole, locationCode, locationType , status, isObx));
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseMessage.setMessage("Workbench list fetched successfully");
		}catch(Exception e){
			logger.error("[Murphy][WorkbenchFacade][getTaskList][error]" + e.getMessage());
		}
		return workBenchResponseDto;
	}

	@Override
	public ResponseMessage updateTaskStatus(WrokBenchAudiLogDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			workBenchDao.updateTaskStatus(dto);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseMessage.setMessage("Updated Successfully");
		}catch(Exception e){
			logger.error("[Murphy][WorkbenchFacade][updateTaskStatus][error]" + e.getMessage());
		}
		return responseMessage;
	}
	
	@Override
	public ResponseMessage autoCancelObxTask() {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			workBenchDao.autoCancelObxTask();
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseMessage.setMessage("Cancelled Status Updated Successfully");
		}catch(Exception e){
			logger.error("[Murphy][WorkbenchFacade][autoCancelObxTask][error]" + e.getMessage());
		}
		return responseMessage;
	}
	

}
