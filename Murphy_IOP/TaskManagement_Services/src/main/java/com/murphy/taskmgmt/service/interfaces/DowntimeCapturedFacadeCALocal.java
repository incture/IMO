package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.DowntimeWellParentCodeDto;
import com.murphy.taskmgmt.dto.Response;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface DowntimeCapturedFacadeCALocal {

	ResponseMessage createDowntimeCapturedForCanada(DowntimeUpdateDto downTmeCanada);

	ResponseEntity<Response<List<DowntimeWellParentCodeDto>>> getActiveParenCodeForWellDowntime();

	DowntimeResponseDto getDowntimeHierarchy(DowntimeRequestDto dtoGet);

	ResponseMessage updateDowntimeCaptured(DowntimeUpdateDto dto);



}
