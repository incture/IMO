package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
import com.murphy.taskmgmt.service.interfaces.GroupsFacadeLocal;


@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/group", produces = "application/json")
public class GroupUserRest {

	@Autowired
	GroupsFacadeLocal facadeLocal;


	@RequestMapping(value = "/getUsersById", method = RequestMethod.GET)
	public TaskOwnersResponeDto getReport(@RequestParam(value ="groupId") String groupId,
			@RequestParam(value ="userType" ,required = false) String userType,
			@RequestParam(value = "taskType", required = false) String taskType) {
		return facadeLocal.getUsersOfGroup(groupId,userType,taskType);
	}

}

