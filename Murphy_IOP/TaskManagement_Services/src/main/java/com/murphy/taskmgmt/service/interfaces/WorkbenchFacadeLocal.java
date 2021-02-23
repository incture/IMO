package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.WorkBenchDto;
import com.murphy.taskmgmt.dto.WorkBenchResponseDto;
import com.murphy.taskmgmt.dto.WrokBenchAudiLogDto;;

public interface WorkbenchFacadeLocal {
	
	public WorkBenchResponseDto getTaskList(String sortingOrder, String sortObject, String groupObject , String technicalRole, String locationCode, String locationType , String status, String isObx) ;
	public ResponseMessage updateTaskStatus(WrokBenchAudiLogDto dto);
	ResponseMessage autoCancelObxTask();

}
