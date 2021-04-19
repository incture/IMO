package com.incture.ptw.dto;

import java.util.List;

import lombok.Data;

@Data
public class PermitsByLocPayloadDto {
	private List<PermitsByLocDataDto> CSE;
	private List<PermitsByLocDataDto> CWP;
	private List<PermitsByLocDataDto> HWP;

}
