package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.OpenTokDto;
import com.murphy.taskmgmt.dto.OpenTokResponseDto;
import com.murphy.taskmgmt.service.interfaces.OpenTokFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/openTok", produces = "application/json")
public class OpenTokRest {
	
	@Autowired
	OpenTokFacadeLocal openTokFacadeLocal;
	
	@RequestMapping(value = "/createARCall", method = RequestMethod.POST)
	public OpenTokResponseDto createCall(@RequestBody OpenTokDto openTokDto) {

		return openTokFacadeLocal.createCall(openTokDto);
	}
	
	@RequestMapping(value = "/updateARCallResponse", method = RequestMethod.PUT)
	public String updateARCallResponse(@RequestBody OpenTokDto openTokDto, @RequestParam(value = "responseBy") String responseBy ,
			@RequestParam(value = "actionType") String actionType , @RequestParam(value = "sendNotification" , required = false) String sendNotification){
		return openTokFacadeLocal.updateARCallResponse(openTokDto, responseBy , actionType , sendNotification);
	}

}
