package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.ReasonForBypassDto;
import com.murphy.taskmgmt.dto.ReasonForBypassResponseListDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface ReasonForBypassFacadeLocal {

	ResponseMessage createReasonForBypass(ReasonForBypassDto dto);

	ReasonForBypassResponseListDto getReasonForBypassList();
}
