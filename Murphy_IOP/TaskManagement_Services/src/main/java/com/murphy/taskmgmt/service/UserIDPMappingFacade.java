package com.murphy.taskmgmt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.UserIDPMappingDao;
import com.murphy.taskmgmt.dto.HierarchyRequestDto;
import com.murphy.taskmgmt.dto.HierarchyResponseDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.UserIDPMappingDto;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;
import com.murphy.taskmgmt.service.interfaces.UserIDPMappingFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("UserIDPMappingFacade")
public class UserIDPMappingFacade implements UserIDPMappingFacadeLocal {
	
	private static final Logger logger = LoggerFactory.getLogger(UserIDPMappingFacade.class);
	
	@Autowired
	UserIDPMappingDao mappingDao;
	
	@Autowired
	private HierarchyDao locDao;
	
	@Override
	public ResponseMessage saveUsers(UserIDPMappingDto mappingDto){
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Users Not saved or updated");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		boolean updateUser = mappingDao.saveOrUpdateUser(mappingDto);
		logger.info("INFO :: saveOrUpdate users : "+updateUser);
		if(updateUser = true) {
			responseMessage.setMessage("Users saved or updated successfully");
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		return responseMessage;
	}
	
	@Override
	public UserIDPMappingDo getUserBySerialId(String serialId) {
		return mappingDao.getUserBySerialId(serialId);
	}
	
	@Override
	public UserIDPMappingDo getUserByLoginName(String loginName) {
		return mappingDao.getUserByLoginName(loginName);
	}
	
	@Override
	public List<UserIDPMappingDo> getUsers() {
		return mappingDao.getAllUserDetails();
	}
	
	@Override
	public HashMap<String,List<String>> fieldByRole(String techRoles, String businessRoles) {
		return mappingDao.getFieldNameFromTechRole(techRoles, businessRoles);
	}
	
	@Override
	public LocationResponseDto wellLocByGroup(String techRoles, String businessRoles) {
		LocationResponseDto locResponseDto = new LocationResponseDto();
		ResponseMessage message = new ResponseMessage();
		message.setMessage(MurphyConstant.READ_FAILURE);
		message.setMessage(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		HierarchyResponseDto responseDto = new HierarchyResponseDto();
		List<LocationHierarchyDto> dto = null;
		try{
			List<String> fieldCode = mappingDao.getFieldCode(techRoles, businessRoles);
			logger.error("FieldCodes list "+fieldCode);
			dto = locDao.getWellListForFieldCode(fieldCode);
			if(!ServicesUtil.isEmpty(dto))
				responseDto.setLocationHierarchy(dto);
			if (ServicesUtil.isEmpty(responseDto.getLocationHierarchy())) {
				message.setMessage(MurphyConstant.NO_RESULT);
			} else {
				locResponseDto.setDto(responseDto);
			}
			message.setMessage(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}catch(Exception e){
			logger.error("[Murphy][UserIDPMappingFacade][wellLocByGroup][error]"+e.getMessage());
		}
		locResponseDto.setMessage(message);
		return locResponseDto;
	}

	@Override
	public ResponseMessage deleteUsers(String emailId) {	
		ResponseMessage message = new ResponseMessage();
		message.setMessage(MurphyConstant.READ_FAILURE);
		message.setMessage(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		String msg =  mappingDao.delete(emailId);	
		if(msg.equals(MurphyConstant.SUCCESS)){
			
			message.setMessage(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			
		}
		
		return message;
	}

	@Override
	public List<UserIDPMappingDo> getIopTrainers() {
		return mappingDao.getIopTrainers();
	}

}
