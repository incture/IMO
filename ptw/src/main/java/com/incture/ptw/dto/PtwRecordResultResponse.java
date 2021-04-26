package com.incture.ptw.dto;

import java.util.List;

import lombok.Data;

@Data
public class PtwRecordResultResponse {
	private PtwTestRecordDto ptwTestRecordDto;
	private List<PtwTestResultsDto> ptwTestResultsDtoList;
}
