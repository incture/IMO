package com.murphy.taskmgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.AppFileDao;
import com.murphy.taskmgmt.dto.AppFileDto;
import com.murphy.taskmgmt.dto.FileVersionDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.service.interfaces.AppFileFacadeLocal;

@Service
public class AppFileFacade implements AppFileFacadeLocal {

	@Autowired
	private AppFileDao appFileDao;

	@Override
	public ResponseMessage saveFile(AppFileDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failed to Save File");
		responseMessage.setStatus("FAILURE");
		responseMessage.setStatusCode("1");
		try {
			responseMessage = appFileDao.insertFile(dto);
		} catch (ExecutionFault e) {
			System.err.println("ExecutionFault while saving File : "+e);
		}
		return responseMessage;
	}

	@Override
	public AppFileDto getFile(String fileType, String application,Double osVersion) {
		return appFileDao.getFile(fileType, application,osVersion);
	}
	
	@Override
	public FileVersionDto getFileVersion(String fileType, String application,Double osVersion) {
		return appFileDao.getVersionList(fileType, application,osVersion);
	}


}
