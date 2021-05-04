package com.incture.ptw.dto;

import java.util.List;

import lombok.Data;

@Data
public class CloseOutReqDto {
	List<PtwCloseOut1Dto> ptwCloseOut1DtoList;
	List<PtwTestResultsDto> ptwTestResultsDtoList;
	String status;
}
