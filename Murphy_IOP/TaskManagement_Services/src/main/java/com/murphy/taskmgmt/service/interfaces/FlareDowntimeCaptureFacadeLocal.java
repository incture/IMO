package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.FlareCodeResponseDto;
import com.murphy.taskmgmt.dto.FlareDowntimeResponseDto;
import com.murphy.taskmgmt.dto.FlareDowntimeUpdateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface FlareDowntimeCaptureFacadeLocal {

	ResponseMessage createFlareDowntime(FlareDowntimeUpdateDto dto);

	FlareCodeResponseDto getFlareDowntimeCodes();

	FlareDowntimeResponseDto getDowntimeHierarchy(DowntimeRequestDto dtoGet);

	ResponseMessage updateFlareDowntime(FlareDowntimeUpdateDto dto);

}
