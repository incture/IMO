package com.murphy.taskmgmt.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.SendMailDto;
import com.murphy.taskmgmt.util.MailAlertUtil;

@RestController
@CrossOrigin()
@ComponentScan("com.murphy")
@RequestMapping(value = "/mail", produces = "application/json")
public class MailUtilRest {

	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	public String updatedTaskForOperator(@RequestBody SendMailDto dto) {
		return new MailAlertUtil().sendMailWithCC(dto.getReceipentId(), dto.getCc(), dto.getSubjectName(),
				dto.getMessage(), dto.getUserName());
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "17th July 2020";
	}
}
