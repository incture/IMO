package com.incture.iopptw.dtos;

import java.util.List;

import lombok.Data;

@Data
public class PermitsByLocPayloadDto {
	private List<PermitsByLocInnerDto> CSE;
	private List<PermitsByLocInnerDto> CWP;
	private List<PermitsByLocInnerDto> HWP;

}
