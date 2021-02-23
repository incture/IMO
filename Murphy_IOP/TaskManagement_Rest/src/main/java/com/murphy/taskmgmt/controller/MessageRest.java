package com.murphy.taskmgmt.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.MessageDto;
import com.murphy.taskmgmt.dto.MessageUIDetailDto;
import com.murphy.taskmgmt.dto.MessageUIResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TeamsResponseDto;
import com.murphy.taskmgmt.service.interfaces.MessageFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/message", produces = "application/json")
public class MessageRest {

	@Autowired
	private MessageFacadeLocal messageFacadeLocal;

	@RequestMapping(value = "/getMessage", method = RequestMethod.GET)
	MessageUIDetailDto getMessage(@RequestParam(value = "messageId") Long messageId) {
		return messageFacadeLocal.getMessage(messageId);
	}

	@RequestMapping(value = "/getAllActiveMessages", method = RequestMethod.GET)
	MessageUIResponseDto getAllActiveMessages(@RequestParam(value = "user") String user,
			@RequestParam(value = "userType") List<String> userType,
			@RequestParam(value = "countryList", required = false) List<String> country,
			@RequestParam(value = "pageNo", required = false) Integer pageNo) {
		return messageFacadeLocal.getAllActiveMessagesOwnedBy(user, userType, country, pageNo);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	TeamsResponseDto create(@RequestBody JSONObject json, @RequestHeader String authorization) {
		return messageFacadeLocal.create(json, authorization);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	ResponseMessage update(@RequestBody MessageDto dto) {
		return messageFacadeLocal.update(dto);
	}
}
