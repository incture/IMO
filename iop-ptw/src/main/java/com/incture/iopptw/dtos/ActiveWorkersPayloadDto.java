package com.incture.iopptw.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ActiveWorkersPayloadDto {
	private String facilityOrSite;
	private List<ActiveWorkersDto> ptwPeopleList;
}
