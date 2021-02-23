package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.AttachmentResponseDto;
import com.murphy.taskmgmt.dto.CollaborationDto;
import com.murphy.taskmgmt.dto.CollaborationResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.CollaborationFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/collaboration", produces = "application/json")
public class CollaborationRest {

	@Autowired
	CollaborationFacadeLocal facadeLocal;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	ResponseMessage createTaskFromTemplate(@RequestBody CollaborationDto dto) {
		return facadeLocal.createCollaboration(dto);
	}

	@RequestMapping(value = "/readByTaskId", method = RequestMethod.GET)
	CollaborationResponseDto createTaskFromTemplate(@RequestParam(value = "taskId") String processId,
			@RequestParam(value = "userType", required = false) String userType) {
		return facadeLocal.getCollaboration(processId, userType);
	}

	@RequestMapping(value = "/getAttachmentById", method = RequestMethod.GET)
	AttachmentResponseDto getAttachmentById(@RequestParam(value = "fileId") String fileId) {
		return facadeLocal.getAttachmentById(fileId);
	}

	@RequestMapping(value = "/removeAttachment", method = RequestMethod.GET)
	ResponseMessage removeAttachment(@RequestParam(value = "fileId") String fileId) {
		return facadeLocal.removeAttachment(fileId);
	}

}
