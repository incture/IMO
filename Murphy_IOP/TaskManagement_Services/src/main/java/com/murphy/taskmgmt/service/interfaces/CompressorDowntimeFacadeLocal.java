package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface CompressorDowntimeFacadeLocal {


	DowntimeResponseDto getDowntimeHierarchy(DowntimeRequestDto dtoGet);

	ResponseMessage updateCompressorDowntime(DowntimeUpdateDto downtimeDto);

	ResponseMessage createCompressorDowntime(DowntimeUpdateDto dto);

	DowntimeResponseDto getCompressorDowntimeCodes();

	
}
