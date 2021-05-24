package com.incture.iopptw.dtos;

import java.util.List;

import lombok.Data;

@Data
public class CloseOutReqDto {
	List<PtwCloseOut1Dto> ptwCloseOutDtoList;
	List<PtwTestResultsDto> ptwTestResultsDtoList;
	String status;
}
