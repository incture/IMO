package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.UserDetailsDto;
import com.murphy.taskmgmt.service.interfaces.UsermanagementFacadeLocal;


@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/user", produces = "application/json")
public class UserManagementRest {

	@Autowired
	UsermanagementFacadeLocal facadeLocal;

	@RequestMapping(value = "/getDetails", method = RequestMethod.GET)
	public UserDetailsDto sendNotification(@RequestParam("userName") String userName) {
		return facadeLocal.getUserDetails(userName);
	} 
	
	@RequestMapping(value = "/trackDir", method = RequestMethod.GET)
	public String trackDirection() {
		return facadeLocal.getTrackDirection();
	}
	
}

