package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.ProcessTemplateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskTemplatesDto;
import com.murphy.taskmgmt.service.interfaces.TaskTemplateFacadeLocal;


@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/template", produces = "application/json" )
public class TaskTemplateRest {

	@Autowired
	TaskTemplateFacadeLocal facadeLocal;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseMessage createTemplate(@RequestBody ProcessTemplateDto dto) {
		return facadeLocal.createTemplate(dto);
	}
	
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public ProcessTemplateDto readTemplate(@RequestParam("procTemplateId") String procTemplateId) {
		return facadeLocal.readTemplate(procTemplateId);
	}
	
	@RequestMapping(value = "/createTask", method = RequestMethod.POST)
	public ResponseMessage createTask(@RequestBody TaskTemplatesDto dto) {
		return facadeLocal.createTaskTemplate(dto);
	}
	

}

