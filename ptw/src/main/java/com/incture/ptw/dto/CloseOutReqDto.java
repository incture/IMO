package com.incture.ptw.dto;

import java.util.List;

import lombok.Data;
@Data
public class CloseOutReqDto {
	List<PtwCloseOutDto> ptwCloseOutDtoList;
	List<PtwTestResultsDto> ptwTestResultsDtoList;
	String status;
}
