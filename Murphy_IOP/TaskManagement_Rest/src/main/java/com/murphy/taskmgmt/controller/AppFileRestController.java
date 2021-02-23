package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.AppFileDto;
import com.murphy.taskmgmt.dto.DownloadResponse;
import com.murphy.taskmgmt.dto.FileVersionDto;
import com.murphy.taskmgmt.service.interfaces.AppFileFacadeLocal;
import com.murphy.taskmgmt.util.ServicesUtil;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/appFile", produces = "application/json")
public class AppFileRestController {
	
	@Autowired
	private AppFileFacadeLocal fileLocal;
	
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public DownloadResponse getDownloadResponse(@RequestParam(value ="fileType") String fileType, 
			@RequestParam(value ="application") String application,@RequestParam(value ="osVersion", required = false) Double osVersion) {
		DownloadResponse downloadResponse = new DownloadResponse();
		AppFileDto dto = fileLocal.getFile(fileType, application,osVersion);
		if(!ServicesUtil.isEmpty(dto)) {
			downloadResponse.setUrl(dto.getFileUrl());
			downloadResponse.setVersion(dto.getFileVersion());
		}
		return downloadResponse;
	}
	
	@RequestMapping(value="/version", method=RequestMethod.GET)
	public FileVersionDto getVersion(@RequestParam(value ="fileType") String fileType, 
			@RequestParam(value ="application") String application,@RequestParam(value ="osVersion", required = false) Double osVersion) {
		return fileLocal.getFileVersion(fileType, application,osVersion);
	}
}
