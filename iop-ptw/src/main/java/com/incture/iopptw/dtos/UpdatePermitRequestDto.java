package com.incture.iopptw.dtos;

import java.util.List;

import lombok.Data;

@Data
public class UpdatePermitRequestDto {
	private List<PtwHeaderDto> ptwHeaderDtoList;
	private List<PtwPeopleDto> ptwPeopleDtoList;
	private List<PtwRequiredDocumentDto> ptwRequiredDocumentDtoList;
	private List<PtwApprovalDto> ptwApprovalDtoList;
	private PtwTestRecordDto ptwTestRecordDto;
	private List<PtwTestResultsDto> ptwTestResultsDtoList;
	private List<PtwCloseOutDto> ptwCloseOutDtoList;
	private PtwCwpWorkTypeDto ptwCwpWorkTypeDto;
	private PtwHwpWorkTypeDto ptwHwpWorkTypeDto;
	private PtwCseWorkTypeDto ptwCseWorkTypeDto;
}
