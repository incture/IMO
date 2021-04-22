package com.incture.ptw.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateServiceResponseDto {
	private String jsaPermitNumber;
	private String permitNumber;
	private List<String> ptwPermitNumber;
}
