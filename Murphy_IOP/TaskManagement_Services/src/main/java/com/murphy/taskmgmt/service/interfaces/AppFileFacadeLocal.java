package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.AppFileDto;
import com.murphy.taskmgmt.dto.FileVersionDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface AppFileFacadeLocal {

	ResponseMessage saveFile(AppFileDto dto);

	AppFileDto getFile(String fileType, String application,Double osVersion);

	FileVersionDto getFileVersion(String fileType, String application,Double osVersion);

}
