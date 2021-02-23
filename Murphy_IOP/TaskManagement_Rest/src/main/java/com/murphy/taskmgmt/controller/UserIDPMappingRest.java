package com.murphy.taskmgmt.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.IopTaskListDto;
import com.murphy.taskmgmt.dto.LocationResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.UserIDPMappingDto;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;
import com.murphy.taskmgmt.service.interfaces.UserIDPMappingFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/userIdp", produces = "application/json")
public class UserIDPMappingRest {

	@Autowired
	UserIDPMappingFacadeLocal userIdpFacade;

	@RequestMapping(value = "/createOrUpdateUser", method = RequestMethod.POST)
	public ResponseMessage createOrUpdateUser(@RequestBody UserIDPMappingDto mappingDto) {
		return userIdpFacade.saveUsers(mappingDto);
	}
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public ResponseMessage deleteuser(@RequestParam("emailId") String emailId) {
		 return userIdpFacade.deleteUsers(emailId);
	}

	@RequestMapping(value = "/getUserBySerialId", method = RequestMethod.GET)
	public UserIDPMappingDo getUserBySerialId(@RequestParam("serialId") String serialId) {
		return userIdpFacade.getUserBySerialId(serialId);
	}

	@RequestMapping(value = "/getUserByLoginName", method = RequestMethod.GET)
	public UserIDPMappingDo getUserByLoginName(@RequestParam("loginName") String loginName) {
		return userIdpFacade.getUserByLoginName(loginName);
	}

	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public List<UserIDPMappingDo> getUsers() {
		return userIdpFacade.getUsers();
	}
	
	// Get Field Names based on Techincal and business role
	@RequestMapping(value = "/getFieldByRoleId", method = RequestMethod.GET)
	public HashMap<String,List<String>> getFieldByRole(@RequestParam("techRole") String techRole, @RequestParam("bizRole") String bizRole) {
		return userIdpFacade.fieldByRole(techRole, bizRole);
	}
	
	//Fetching list of wells for the roles assigned
	@RequestMapping(value = "/getWellByGroup", method = RequestMethod.GET)
	public LocationResponseDto getWellByGroupDetails(@RequestParam("techRole") String techRole, @RequestParam("bizRole") String bizRole) {
		return userIdpFacade.wellLocByGroup(techRole, bizRole);
	}
	
	@RequestMapping(value = "/getIopTrainers", method = RequestMethod.GET)
	public List<UserIDPMappingDo> getIopTrainers() {
		return userIdpFacade.getIopTrainers();
	}
	

}
