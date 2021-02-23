package com.murphy.taskmgmt.service.interfaces;

import java.util.HashMap;
import java.util.List;

import com.murphy.taskmgmt.dto.LocationResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.UserIDPMappingDto;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;

public interface UserIDPMappingFacadeLocal {

	ResponseMessage saveUsers(UserIDPMappingDto mappingDto);
	
	ResponseMessage deleteUsers(String emailId);

	UserIDPMappingDo getUserBySerialId(String serialId);

	UserIDPMappingDo getUserByLoginName(String loginName);

	List<UserIDPMappingDo> getUsers();

	HashMap<String, List<String>> fieldByRole(String role, String bizRole);

	LocationResponseDto wellLocByGroup(String techRole, String bizRole);

	List<UserIDPMappingDo> getIopTrainers();

}
