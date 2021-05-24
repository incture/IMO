package com.incture.iopptw.dtos;

import java.util.List;

import lombok.Data;
@Data
public class PtwDetailsDto {
	private JsaReviewDto jsaReviewDto;
	private List<PtwHeaderDto> ptwHeaderDtoList;
	private List<PtwRequiredDocumentDto> ptwRequiredDocumentDtoList;
	private List<PtwApprovalDto> ptwApprovalDtoList;
	private PtwTestRecordDto ptwTestRecordDto;
	private List<PtwTestResultsDto> ptwTestResultsDtoList;
	private List<PtwCloseOutDto> ptwCloseOutDtolist;
	private PtwCwpWorkTypeDto ptwCwpWorkTypeDto;
	private PtwHwpWorkTypeDto ptwHwpWorkTypeDto;
	private PtwCseWorkTypeDto ptwCseWorkTypeDto;
	private List<PtwPeopleDto> ptwPeopleDtoList;

}
