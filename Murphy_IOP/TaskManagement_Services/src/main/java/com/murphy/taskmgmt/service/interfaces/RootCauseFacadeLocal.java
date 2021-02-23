package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.RootCauseResponseDto;

public interface RootCauseFacadeLocal {

	RootCauseResponseDto getRootCauseByTaskId(String taskId, String status, String origin);

	RootCauseResponseDto getRootCauseByTaskIdInArray(String taskId, String status, String origin);

}
